package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PlantillaHtmlKitEmergencia {

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
    fun generarHtml(entradas: List<Entrada>, opciones: GeneradorKitEmergencia.OpcionesKit): String {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val lista = ExtractorDatosKitEmergencia.filtrarEntradas(entradas, opciones)

        val filas = lista.mapIndexed { i, e ->
            val datos = ExtractorDatosKitEmergencia.extraerDatosEntrada(e, opciones.incluirContrasenas)
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
}
