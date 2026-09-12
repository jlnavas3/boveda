package com.jlnavas3.bovedalocal.util

import android.app.Activity
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jlnavas3.bovedalocal.data.Entrada
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GeneradorKitEmergencia {

    data class OpcionesKit(
        val incluirContrasenas: Boolean = false,
        val soloFavoritos: Boolean = false,
        val incluirNotas: Boolean = false
    )

    fun filtrarEntradas(entradas: List<Entrada>, opciones: OpcionesKit): List<Entrada> {
        return if (opciones.soloFavoritos) entradas.filter { it.favorito } else entradas
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
            appendLine("[${i + 1}] ${e.titulo.ifBlank { "Sin título" }} (${e.tipo.etiqueta})")
            if (e.usuario.isNotBlank()) appendLine("    Usuario : ${e.usuario}")
            if (opciones.incluirContrasenas) {
                val pass = e.contrasena.ifBlank { "(sin contraseña)" }
                appendLine("    Clave   : $pass")
            } else {
                appendLine("    Clave   : [ CONFIDENCIAL / ANOTAR MANUALMENTE _________ ]")
            }
            if (e.urls.isNotEmpty()) appendLine("    Sitio   : ${e.urls.first()}")
            if (!e.secretoTotp.isNullOrBlank()) appendLine("    2FA     : [Configurado]")
            if (opciones.incluirNotas && e.notas.isNotBlank()) {
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
            val tituloEsc = escaparHtml(e.titulo.ifBlank { "Sin título" })
            val usuarioEsc = escaparHtml(e.usuario.ifBlank { "—" })
            val claveEsc = escaparHtml(e.contrasena.ifBlank { "—" })
            val claveCol = if (opciones.incluirContrasenas) {
                "<code>$claveEsc</code>"
            } else {
                "<span style='color: #777; font-style: italic;'>[Anote manualmente]</span>"
            }
            val totpCol = if (!e.secretoTotp.isNullOrBlank()) "✓ Sí" else "—"
            val notasCol = if (opciones.incluirNotas && e.notas.isNotBlank()) {
                "<br/><small style='color: #555;'>Nota: ${escaparHtml(e.notas)}</small>"
            } else ""

            """
            <tr>
                <td style="text-align: center; color: #888;">${i + 1}</td>
                <td><strong>$tituloEsc</strong>$notasCol</td>
                <td><code>$usuarioEsc</code></td>
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
                    padding: 5px 6px;
                    font-size: 10px;
                    text-align: left;
                }
                td {
                    border: 1px solid #ddd;
                    padding: 5px 6px;
                    vertical-align: top;
                    word-break: break-all;
                    overflow-wrap: anywhere;
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
                        <th style="width: 28%;">Servicio / Título</th>
                        <th style="width: 29%;">Usuario</th>
                        <th style="width: 31%;">Contraseña</th>
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
