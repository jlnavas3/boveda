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
     * Genera HTML con estilo formal y limpio para impresión directa en papel o guardado en PDF.
     */
    fun generarHtml(entradas: List<Entrada>, opciones: OpcionesKit): String {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val lista = filtrarEntradas(entradas, opciones)

        val filas = lista.mapIndexed { i, e ->
            val claveCol = if (opciones.incluirContrasenas) {
                "<code>${e.contrasena.ifBlank { "—" }}</code>"
            } else {
                "<span style='color: #777; font-style: italic;'>[Anote manualmente]</span>"
            }
            val totpCol = if (!e.secretoTotp.isNullOrBlank()) "✓ Sí" else "—"
            val notasCol = if (opciones.incluirNotas && e.notas.isNotBlank()) {
                "<br/><small style='color: #555;'>Nota: ${e.notas}</small>"
            } else ""

            """
            <tr>
                <td style="text-align: center; color: #888;">${i + 1}</td>
                <td><strong>${e.titulo.ifBlank { "Sin título" }}</strong>$notasCol</td>
                <td><code>${e.usuario.ifBlank { "—" }}</code></td>
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
                body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif; color: #111; margin: 24px; font-size: 12px; }
                h1 { font-size: 18px; margin-bottom: 4px; color: #000; }
                .meta { color: #555; font-size: 11px; margin-bottom: 16px; }
                .aviso { background: #fdf6e2; border-left: 4px solid #b58900; padding: 8px 12px; margin-bottom: 20px; font-size: 11px; color: #664d03; }
                table { width: 100%; border-collapse: collapse; margin-top: 8px; }
                th { background: #f0f0f0; border: 1px solid #ccc; padding: 6px 8px; font-size: 11px; text-align: left; }
                td { border: 1px solid #ddd; padding: 6px 8px; vertical-align: top; }
                code { font-family: "Courier New", Courier, monospace; font-size: 11px; }
                .footer { margin-top: 24px; text-align: center; color: #888; font-size: 10px; border-top: 1px solid #eee; padding-top: 8px; }
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
                        <th style="width: 30px; text-align: center;">#</th>
                        <th>Servicio / Título</th>
                        <th>Usuario</th>
                        <th>Contraseña</th>
                        <th style="width: 50px; text-align: center;">2FA</th>
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
     * Lanza el servicio de impresión nativo de Android (PrintManager) sin dependencias externas.
     * Permite al usuario "Guardar como PDF" o enviar a una impresora Wi-Fi/Bluetooth.
     */
    fun imprimir(actividad: Activity, html: String, nombreDoc: String = "Kit_Emergencia_BovedaLocal") {
        actividad.runOnUiThread {
            val webView = WebView(actividad)
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    val printManager = actividad.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return
                    val printAdapter = webView.createPrintDocumentAdapter(nombreDoc)
                    printManager.print(nombreDoc, printAdapter, PrintAttributes.Builder().build())
                }
            }
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
        }
    }
}
