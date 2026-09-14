package com.jlnavas3.bovedalocal.util

import android.app.Activity
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GeneradorKitEmergencia {

    data class OpcionesKit(
        val incluirContrasenas: Boolean = false,
        val soloFavoritos: Boolean = false,
        val incluirNotas: Boolean = false
    )

    data class DatosKitEntrada(
        val identificador: String,
        val secreto: String,
        val detallesExtras: List<Pair<String, String>> = emptyList()
    )

    fun filtrarEntradas(entradas: List<Entrada>, opciones: OpcionesKit): List<Entrada> {
        return if (opciones.soloFavoritos) entradas.filter { it.favorito } else entradas
    }

    fun extraerDatosEntrada(e: Entrada, incluirSecretos: Boolean): DatosKitEntrada {
        val campos = e.camposPersonalizados
        val etiquetasUsadas = mutableSetOf<String>()

        fun valor(clave: String): String {
            val v = GestorCamposBase.valorDeCampo(campos, clave)
            if (v.isNotBlank()) etiquetasUsadas.add(clave.lowercase())
            return v
        }

        var iden = ""
        var sec = ""

        when (e.tipo) {
            TipoEntrada.WIFI -> {
                val ssid = valor("Nombre de red (SSID)").ifBlank { valor("SSID") }
                val seguridad = valor("Tipo de seguridad").ifBlank { valor("Seguridad") }
                val claveWifi = valor("Contraseña Wi-Fi").ifBlank { valor("Contraseña") }.ifBlank { e.contrasena }
                iden = when {
                    ssid.isNotBlank() && seguridad.isNotBlank() -> "SSID: $ssid ($seguridad)"
                    ssid.isNotBlank() -> "SSID: $ssid"
                    seguridad.isNotBlank() -> "Seguridad: $seguridad"
                    else -> "Red Wi-Fi"
                }
                sec = claveWifi
            }
            TipoEntrada.TARJETA -> {
                val titular = valor("Titular")
                val numero = valor("Número de tarjeta").ifBlank { valor("Número") }
                val venc = valor("Vencimiento")
                val cvv = valor("CVV")
                val pin = valor("PIN de tarjeta").ifBlank { valor("PIN") }
                iden = listOfNotNull(
                    titular.takeIf { it.isNotBlank() }?.let { "Titular: $it" },
                    numero.takeIf { it.isNotBlank() }?.let { "Nº: $it" }
                ).joinToString("\n").ifBlank { "Tarjeta bancaria" }
                sec = listOfNotNull(
                    venc.takeIf { it.isNotBlank() }?.let { "Venc: $it" },
                    cvv.takeIf { it.isNotBlank() }?.let { "CVV: $it" },
                    pin.takeIf { it.isNotBlank() }?.let { "PIN: $it" }
                ).joinToString(" • ").ifBlank { e.contrasena }
            }
            TipoEntrada.CUENTA_BANCARIA -> {
                val banco = valor("Banco / Entidad").ifBlank { valor("Banco") }
                val titular = valor("Titular de la cuenta").ifBlank { valor("Titular") }
                val cuenta = valor("Número de cuenta / IBAN").ifBlank { valor("Número de cuenta") }.ifBlank { valor("IBAN") }
                val swift = valor("SWIFT / CBU / CLABE").ifBlank { valor("SWIFT") }.ifBlank { valor("CBU") }
                iden = listOfNotNull(
                    banco.takeIf { it.isNotBlank() }?.let { "Banco: $it" },
                    titular.takeIf { it.isNotBlank() }?.let { "Titular: $it" }
                ).joinToString("\n").ifBlank { "Cuenta bancaria" }
                sec = listOfNotNull(
                    cuenta.takeIf { it.isNotBlank() }?.let { "Cuenta/IBAN: $it" },
                    swift.takeIf { it.isNotBlank() }?.let { "SWIFT: $it" }
                ).joinToString(" • ").ifBlank { e.contrasena }
            }
            TipoEntrada.IDENTIDAD -> {
                val tipoDoc = valor("Tipo de documento").ifBlank { valor("Tipo") }
                val nombre = valor("Nombre completo").ifBlank { valor("Nombre") }
                val numDoc = valor("Número de documento").ifBlank { valor("Número") }
                val caducidad = valor("Caducidad")
                val pais = valor("País emisor").ifBlank { valor("País") }
                iden = listOfNotNull(
                    tipoDoc.takeIf { it.isNotBlank() }?.let { "Tipo: $it" },
                    nombre.takeIf { it.isNotBlank() }?.let { "Nombre: $it" }
                ).joinToString("\n").ifBlank { "Documento de identidad" }
                sec = listOfNotNull(
                    numDoc.takeIf { it.isNotBlank() }?.let { "Nº: $it" },
                    caducidad.takeIf { it.isNotBlank() }?.let { "Cad: $it" },
                    pais.takeIf { it.isNotBlank() }?.let { "País: $it" }
                ).joinToString(" • ").ifBlank { e.contrasena }
            }
            TipoEntrada.SERVIDOR -> {
                val host = valor("Host o IP").ifBlank { valor("Host") }.ifBlank { valor("IP") }
                val puerto = valor("Puerto")
                val userSsh = valor("Usuario SSH").ifBlank { valor("Usuario") }.ifBlank { e.usuario }
                val pass = valor("Clave privada / Password").ifBlank { valor("Password") }.ifBlank { valor("Clave privada") }.ifBlank { e.contrasena }
                iden = when {
                    userSsh.isNotBlank() && host.isNotBlank() -> "$userSsh@$host${if (puerto.isNotBlank()) ":$puerto" else ""}"
                    host.isNotBlank() -> "$host${if (puerto.isNotBlank()) ":$puerto" else ""}"
                    userSsh.isNotBlank() -> "Usuario: $userSsh"
                    else -> "Servidor"
                }
                sec = pass
            }
            TipoEntrada.WALLET -> {
                val red = valor("Red / Blockchain").ifBlank { valor("Red") }
                val dir = valor("Dirección pública").ifBlank { valor("Dirección") }
                val semilla = valor("Frase semilla (Seed phrase)").ifBlank { valor("Frase semilla") }.ifBlank { valor("Seed phrase") }
                val priv = valor("Clave privada")
                iden = listOfNotNull(
                    red.takeIf { it.isNotBlank() }?.let { "Red: $it" },
                    dir.takeIf { it.isNotBlank() }?.let { "Dir: $it" }
                ).joinToString("\n").ifBlank { "Cripto Wallet" }
                sec = listOfNotNull(
                    semilla.takeIf { it.isNotBlank() }?.let { "Semilla: $it" },
                    priv.takeIf { it.isNotBlank() }?.let { "Clave priv: $it" }
                ).joinToString("\n").ifBlank { e.contrasena }
            }
            TipoEntrada.PASSKEY -> {
                val rp = e.passkey?.rpName?.ifBlank { e.passkey?.rpId } ?: ""
                val u = e.passkey?.usuario?.ifBlank { e.usuario } ?: e.usuario
                iden = listOfNotNull(
                    u.takeIf { it.isNotBlank() }?.let { "Usuario: $it" },
                    rp.takeIf { it.isNotBlank() }?.let { "Sitio (RP): $it" }
                ).joinToString("\n").ifBlank { "Passkey FIDO2" }
                sec = "Credencial FIDO2 / WebAuthn (Protegida por hardware)"
            }
            TipoEntrada.NOTA -> {
                iden = "—"
                sec = e.notas.ifBlank { "(sin contenido)" }
            }
            TipoEntrada.LOGIN -> {
                iden = e.usuario.ifBlank { e.urls.firstOrNull() ?: "—" }
                sec = e.contrasena
            }
        }

        val extras = campos.filter { c ->
            c.valor.isNotBlank() && etiquetasUsadas.none { c.etiqueta.contains(it, ignoreCase = true) || it.contains(c.etiqueta, ignoreCase = true) }
        }.mapNotNull { c ->
            if (c.esSensibleEfectivo && !incluirSecretos) {
                null
            } else {
                c.etiqueta to c.valor
            }
        }

        return DatosKitEntrada(
            identificador = iden.ifBlank { "—" },
            secreto = sec.ifBlank { "—" },
            detallesExtras = extras
        )
    }

    /**
     * Genera un texto plano estructurado apto para imprimir o copiar a mano.
     */
    fun generarTexto(entradas: List<Entrada>, opciones: OpcionesKit): String = buildString {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val lista = filtrarEntradas(entradas, opciones)

        appendLine("================================================================")
        appendLine("           BÓVEDA LOCAL - KIT DE SEGURIDAD FÍSICO               ")
        appendLine("================================================================")
        appendLine("Fecha de generación: $fecha")
        appendLine("Total de cuentas incluidas: ${lista.size}")
        appendLine("Modo: ${if (opciones.incluirContrasenas) "Con contraseñas" else "Inventario seguro (sin contraseñas)"}")
        appendLine()
        appendLine("AVISO DE SEGURIDAD:")
        appendLine("Este documento físico debe ser almacenado en un lugar seguro")
        appendLine("(como una caja fuerte o sobre sellado). Nunca lo dejes a la vista.")
        appendLine("----------------------------------------------------------------")
        appendLine()

        lista.forEachIndexed { i, e ->
            val datos = extraerDatosEntrada(e, opciones.incluirContrasenas)
            appendLine("[${i + 1}] ${e.titulo.ifBlank { "Sin título" }} (${e.tipo.etiqueta})")
            if (datos.identificador.isNotBlank() && datos.identificador != "—") {
                datos.identificador.lines().forEach { linea ->
                    appendLine("    $linea")
                }
            }
            if (opciones.incluirContrasenas) {
                if (datos.secreto.isNotBlank() && datos.secreto != "—") {
                    datos.secreto.lines().forEach { linea ->
                        appendLine("    Clave   : $linea")
                    }
                }
            } else {
                if (datos.secreto.isNotBlank() && datos.secreto != "—") {
                    appendLine("    Clave   : ")
                }
            }
            if (e.urls.isNotEmpty()) appendLine("    Sitio   : ${e.urls.first()}")
            if (!e.secretoTotp.isNullOrBlank()) appendLine("    2FA     : [Configurado]")
            if (e.tipo == TipoEntrada.PASSKEY) appendLine("    Passkey : [FIDO2 Hardware]")
            datos.detallesExtras.forEach { (k, v) ->
                appendLine("    $k : $v")
            }
            if (opciones.incluirNotas && e.notas.isNotBlank() && e.tipo != TipoEntrada.NOTA) {
                appendLine("    Notas   : ${e.notas.replace("\n", " ")}")
            }
            appendLine()
        }
        appendLine("================================================================")
        appendLine("         Fin del documento - Generado por Bóveda Local          ")
        appendLine("================================================================")
    }

    /**
     * Escapa caracteres especiales para evitar inyecciones o que el motor HTML de Android
     * malinterprete contraseñas o datos que contengan <, >, &, ", o '.
     */
    fun escaparHtml(texto: String): String = texto
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;")

    /**
     * Genera HTML con estilo formal y limpio para impresión directa en papel o guardado en PDF.
     */
    fun generarHtml(entradas: List<Entrada>, opciones: OpcionesKit): String {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val lista = filtrarEntradas(entradas, opciones)

        val filas = lista.mapIndexed { i, e ->
            val datos = extraerDatosEntrada(e, opciones.incluirContrasenas)
            val tituloEsc = escaparHtml(e.titulo.ifBlank { "Sin título" })
            val tipoEsc = escaparHtml(e.tipo.etiqueta)
            val usuarioEsc = datos.identificador.split("\n").joinToString("<br/>") { escaparHtml(it) }
            val claveEsc = datos.secreto.split("\n").joinToString("<br/>") { escaparHtml(it) }

            val claveCol = if (opciones.incluirContrasenas) {
                if (datos.secreto == "—") "—" else "<code>$claveEsc</code>"
            } else {
                if (datos.secreto == "—") "—" else "&nbsp;"
            }
            val totpCol = if (!e.secretoTotp.isNullOrBlank()) "✓ Sí" else if (e.tipo == TipoEntrada.PASSKEY) "Passkey" else "—"

            val urlEsc = if (e.urls.isNotEmpty() && e.tipo != TipoEntrada.LOGIN) {
                "<br/><span style='color: #0066cc; font-size: 9px;'>${escaparHtml(e.urls.first())}</span>"
            } else ""

            val extrasCol = if (datos.detallesExtras.isNotEmpty()) {
                "<br/><div style='margin-top: 2px; font-size: 9px; color: #444;'>" +
                    datos.detallesExtras.joinToString("<br/>") { (k, v) ->
                        "<em>${escaparHtml(k)}:</em> <code>${escaparHtml(v)}</code>"
                    } + "</div>"
            } else ""

            val notasCol = if (opciones.incluirNotas && e.notas.isNotBlank() && e.tipo != TipoEntrada.NOTA) {
                "<br/><small style='color: #555;'>Nota: ${escaparHtml(e.notas)}</small>"
            } else ""

            """
            <tr>
                <td style="text-align: center; color: #888;">${i + 1}</td>
                <td><strong>$tituloEsc</strong> <span style="color: #666; font-size: 9px;">($tipoEsc)</span>$urlEsc$extrasCol$notasCol</td>
                <td>$usuarioEsc</td>
                <td>$claveCol</td>
                <td style="text-align: center;">$totpCol</td>
            </tr>
            """.trimIndent()
        }.joinToString("\n")

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8"/>
            <title>Bóveda Local - Kit de Emergencia</title>
            <style>
                @page {
                    size: auto;
                    margin: 12mm 10mm 12mm 10mm;
                }
                * {
                    box-sizing: border-box;
                }
                body {
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                    color: #111;
                    margin: 0;
                    padding: 8px;
                    font-size: 11px;
                    line-height: 1.3;
                }
                h1 {
                    font-size: 16px;
                    margin: 0 0 4px 0;
                    color: #000;
                }
                .meta {
                    color: #555;
                    font-size: 10px;
                    margin-bottom: 12px;
                }
                .aviso {
                    background: #fdf6e2;
                    border-left: 4px solid #b58900;
                    padding: 6px 10px;
                    margin-bottom: 12px;
                    font-size: 10px;
                    color: #664d03;
                }
                table {
                    width: 100%;
                    table-layout: fixed;
                    border-collapse: collapse;
                    empty-cells: show;
                    margin-top: 6px;
                }
                thead {
                    display: table-header-group;
                }
                tr {
                    page-break-inside: avoid;
                }
                th {
                    background: #f0f0f0;
                    border: 1px solid #ccc;
                    padding: 6px 6px;
                    font-size: 10px;
                    text-align: left;
                }
                td {
                    border: 1px solid #ddd;
                    padding: 7px 6px;
                    vertical-align: top;
                    word-break: break-all;
                    overflow-wrap: anywhere;
                    min-height: 24px;
                }
                code {
                    font-family: "Courier New", Courier, monospace;
                    font-size: 10px;
                    word-break: break-all;
                    overflow-wrap: anywhere;
                }
                .footer {
                    margin-top: 18px;
                    text-align: center;
                    color: #888;
                    font-size: 9px;
                    border-top: 1px solid #eee;
                    padding-top: 6px;
                }
            </style>
        </head>
        <body>
            <h1>Bóveda Local — Kit de Seguridad y Emergencia Físico</h1>
            <div class="meta">
                Generado el <strong>$fecha</strong> | Total cuentas: <strong>${lista.size}</strong> | Modo: <strong>${if (opciones.incluirContrasenas) "Con contraseñas visibles" else "Inventario sin contraseñas"}</strong>
            </div>
            <div class="aviso">
                <strong>ADVERTENCIA DE SEGURIDAD:</strong> Este documento físico debe custodiarse bajo llave en una caja fuerte o archivador seguro. Bóveda Local nunca sube tus claves a la nube.
            </div>
            <table>
                <thead>
                    <tr>
                        <th style="width: 5%; text-align: center;">#</th>
                        <th style="width: 27%;">Servicio / Tipo</th>
                        <th style="width: 31%;">Usuario / Identificador</th>
                        <th style="width: 30%;">Contraseña / Clave</th>
                        <th style="width: 7%; text-align: center;">2FA</th>
                    </tr>
                </thead>
                <tbody>
                    $filas
                </tbody>
            </table>
            <div class="footer">
                Bóveda Local • Cifrado Local Offline de Grado Militar • Documento Confidencial
            </div>
        </body>
        </html>
        """.trimIndent()
    }

    /**
     * Referencia fuerte para evitar que el Garbage Collector destruya el WebView
     * antes de que el servicio nativo de impresión termine de procesar el documento.
     */
    private var webViewActual: WebView? = null

    /**
     * Lanza el servicio de impresión nativo de Android (PrintManager) sin dependencias externas.
     * Permite al usuario "Guardar como PDF" o enviar a una impresora Wi-Fi/Bluetooth.
     */
    fun imprimir(actividad: Activity, html: String, nombreDoc: String = "Kit_Emergencia_BovedaLocal") {
        actividad.runOnUiThread {
            val webView = WebView(actividad)
            webViewActual = webView
            var impreso = false
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    if (impreso) return
                    impreso = true
                    val printManager = actividad.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return
                    val printAdapter = webView.createPrintDocumentAdapter(nombreDoc)
                    printManager.print(nombreDoc, printAdapter, PrintAttributes.Builder().build())
                }
            }
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
        }
    }
}
