package com.jlnavas3.bovedalocal.util

import android.app.Activity
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
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

    fun filtrarEntradas(entradas: List<Entrada>, opciones: OpcionesKit): List<Entrada> =
        ExtractorDatosKitEmergencia.filtrarEntradas(entradas, opciones)

    fun extraerDatosEntrada(e: Entrada, incluirSecretos: Boolean): DatosKitEntrada =
        ExtractorDatosKitEmergencia.extraerDatosEntrada(e, incluirSecretos)

    fun escaparHtml(texto: String): String =
        PlantillaHtmlKitEmergencia.escaparHtml(texto)

    fun generarHtml(entradas: List<Entrada>, opciones: OpcionesKit): String =
        PlantillaHtmlKitEmergencia.generarHtml(entradas, opciones)

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
