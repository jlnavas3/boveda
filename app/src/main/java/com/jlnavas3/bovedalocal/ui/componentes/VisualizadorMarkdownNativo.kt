/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.util.ParserTablaMarkdown
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente desacoplado que renderiza texto con formato Markdown estándar
 * directamente en Jetpack Compose, soportando encabezados, listas, casillas de verificación,
 * bloques de código, citas y estilos en línea (negrita, cursiva, código).
 */
@Composable
fun VisualizadorMarkdownNativo(
    contenido: String,
    modifier: Modifier = Modifier,
    colorTextoPorDefecto: Color = TextoPrincipal
) {
    if (contenido.isBlank()) {
        Text(
            text = "Sin contenido",
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            color = TextoSecundario,
            modifier = modifier
        )
        return
    }

    val lineas = remember(contenido) { contenido.lines() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        var enBloqueCodigo = false
        val bufferCodigo = StringBuilder()
        var i = 0

        while (i < lineas.size) {
            val linea = lineas[i]
            val lineaTrim = linea.trim()

            if (lineaTrim.startsWith("```")) {
                if (enBloqueCodigo) {
                    // Cerrar bloque de código
                    BloqueCodigoMarkdown(codigo = bufferCodigo.toString().trimEnd())
                    bufferCodigo.clear()
                    enBloqueCodigo = false
                } else {
                    enBloqueCodigo = true
                }
                i++
                continue
            }

            if (enBloqueCodigo) {
                bufferCodigo.appendLine(linea)
                i++
                continue
            }

            // Detección y renderizado de bloques de tabla Markdown GFM
            if (i + 1 < lineas.size && ParserTablaMarkdown.esFilaDelimitadora(lineas[i + 1]) && lineaTrim.contains("|")) {
                val bufferTabla = mutableListOf<String>()
                bufferTabla.add(linea)
                bufferTabla.add(lineas[i + 1])
                var j = i + 2
                while (j < lineas.size && lineas[j].isNotBlank() && lineas[j].contains("|")) {
                    bufferTabla.add(lineas[j])
                    j++
                }
                val tabla = ParserTablaMarkdown.parsear(bufferTabla)
                if (tabla != null) {
                    TablaMarkdownNativa(
                        tabla = tabla,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    i = j
                    continue
                }
            }

            when {
                lineaTrim == "---" || lineaTrim == "***" || lineaTrim == "___" -> {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 6.dp),
                        color = ColorSeparadorAjustes.copy(alpha = 0.5f)
                    )
                }
                linea.startsWith("# ") -> {
                    Text(
                        text = linea.removePrefix("# ").trim(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = colorTextoPorDefecto,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }
                linea.startsWith("## ") -> {
                    Text(
                        text = linea.removePrefix("## ").trim(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = colorTextoPorDefecto,
                        modifier = Modifier.padding(top = 3.dp, bottom = 1.dp)
                    )
                }
                linea.startsWith("### ") -> {
                    Text(
                        text = linea.removePrefix("### ").trim(),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.5.sp
                        ),
                        color = colorTextoPorDefecto,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                lineaTrim.startsWith("- [ ] ") || lineaTrim.startsWith("* [ ] ") -> {
                    val textoCheckbox = lineaTrim.removePrefix("- [ ] ").removePrefix("* [ ] ")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 1.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckBoxOutlineBlank,
                            contentDescription = "Pendiente",
                            tint = TextoSecundario,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = parsearEstilosEnLinea(textoCheckbox),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorTextoPorDefecto
                        )
                    }
                }
                lineaTrim.startsWith("- [x] ", ignoreCase = true) || lineaTrim.startsWith("* [x] ", ignoreCase = true) -> {
                    val textoCheckbox = lineaTrim.substring(6)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 1.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckBox,
                            contentDescription = "Completado",
                            tint = ColorAcento,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = parsearEstilosEnLinea(textoCheckbox),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = TextoSecundario
                        )
                    }
                }
                lineaTrim.startsWith("- ") || lineaTrim.startsWith("* ") -> {
                    val textoItem = lineaTrim.removePrefix("- ").removePrefix("* ")
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(vertical = 1.dp)
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = ColorAcento,
                            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                        )
                        Text(
                            text = parsearEstilosEnLinea(textoItem),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorTextoPorDefecto
                        )
                    }
                }
                lineaTrim.matches(Regex("^\\d+\\.\\s.*")) -> {
                    val puntoIdx = lineaTrim.indexOf('.')
                    val numero = lineaTrim.substring(0, puntoIdx + 1)
                    val textoItem = lineaTrim.substring(puntoIdx + 1).trim()
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(vertical = 1.dp)
                    ) {
                        Text(
                            text = numero,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = ColorAcento,
                            modifier = Modifier.padding(start = 2.dp, end = 6.dp)
                        )
                        Text(
                            text = parsearEstilosEnLinea(textoItem),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorTextoPorDefecto
                        )
                    }
                }
                lineaTrim.startsWith("> ") -> {
                    val textoCita = lineaTrim.removePrefix("> ")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.5.dp)
                                .height(20.dp)
                                .clip(FormaPequena)
                                .background(ColorAcento.copy(alpha = 0.7f))
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = parsearEstilosEnLinea(textoCita),
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                            color = colorTextoPorDefecto.copy(alpha = 0.9f)
                        )
                    }
                }
                // Texto centrado: <center>texto</center>, -> texto <- o :---: texto
                (lineaTrim.startsWith("<center>", ignoreCase = true) && lineaTrim.endsWith("</center>", ignoreCase = true)) ||
                (lineaTrim.startsWith("->") && lineaTrim.endsWith("<-") && lineaTrim.length > 4) ||
                (lineaTrim.startsWith(":---:") && lineaTrim.length > 5) -> {
                    val textoCentrado = when {
                        lineaTrim.startsWith("<center>", ignoreCase = true) ->
                            lineaTrim.substring(8, lineaTrim.length - 9).trim()
                        lineaTrim.startsWith("->") && lineaTrim.endsWith("<-") ->
                            lineaTrim.substring(2, lineaTrim.length - 2).trim()
                        else ->
                            lineaTrim.removePrefix(":---:").trim()
                    }
                    Text(
                        text = parsearEstilosEnLinea(textoCentrado),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorTextoPorDefecto,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 1.dp)
                    )
                }

                // Texto alineado a la derecha: <right>texto</right>, -> texto, >> texto o ---: texto
                (lineaTrim.startsWith("<right>", ignoreCase = true) && lineaTrim.endsWith("</right>", ignoreCase = true)) ||
                (lineaTrim.startsWith("->") && !lineaTrim.endsWith("<-")) ||
                lineaTrim.startsWith(">>") ||
                (lineaTrim.startsWith("---:") && lineaTrim.length > 4) -> {
                    val textoDerecha = when {
                        lineaTrim.startsWith("<right>", ignoreCase = true) ->
                            lineaTrim.substring(7, lineaTrim.length - 8).trim()
                        lineaTrim.startsWith("->") ->
                            lineaTrim.removePrefix("->").trim()
                        lineaTrim.startsWith(">>") ->
                            lineaTrim.removePrefix(">>").trim()
                        else ->
                            lineaTrim.removePrefix("---:").trim()
                    }
                    Text(
                        text = parsearEstilosEnLinea(textoDerecha),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorTextoPorDefecto,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 1.dp)
                    )
                }

                linea.isBlank() -> {
                    Spacer(Modifier.height(4.dp))
                }
                else -> {
                    Text(
                        text = parsearEstilosEnLinea(linea),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorTextoPorDefecto
                    )
                }
            }
            i++
        }

        if (enBloqueCodigo && bufferCodigo.isNotEmpty()) {
            BloqueCodigoMarkdown(codigo = bufferCodigo.toString().trimEnd())
        }
    }
}

@Composable
private fun BloqueCodigoMarkdown(codigo: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaPequena)
            .background(ColorCampoAjustes)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = codigo,
            style = EstiloMono.copy(fontSize = 12.5.sp),
            color = TextoPrincipal,
            modifier = Modifier.horizontalScroll(rememberScrollState())
        )
    }
}

/**
 * Parsea recursivamente etiquetas de estilo en línea Markdown:
 * **negrita**, *cursiva*, ~~tachado~~, `código`.
 */
internal fun parsearEstilosEnLinea(texto: String): AnnotatedString {
    return buildAnnotatedString {
        var cursor = 0
        val len = texto.length

        while (cursor < len) {
            when {
                // Negrita **texto**
                texto.startsWith("**", cursor) -> {
                    val fin = texto.indexOf("**", cursor + 2)
                    if (fin != -1) {
                        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        append(texto.substring(cursor + 2, fin))
                        pop()
                        cursor = fin + 2
                    } else {
                        append(texto[cursor])
                        cursor++
                    }
                }
                // Tachado ~~texto~~
                texto.startsWith("~~", cursor) -> {
                    val fin = texto.indexOf("~~", cursor + 2)
                    if (fin != -1) {
                        pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                        append(texto.substring(cursor + 2, fin))
                        pop()
                        cursor = fin + 2
                    } else {
                        append(texto[cursor])
                        cursor++
                    }
                }
                // Cursiva *texto*
                texto.startsWith("*", cursor) && !texto.startsWith("**", cursor) -> {
                    val fin = texto.indexOf("*", cursor + 1)
                    if (fin != -1 && fin > cursor + 1) {
                        pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                        append(texto.substring(cursor + 1, fin))
                        pop()
                        cursor = fin + 1
                    } else {
                        append(texto[cursor])
                        cursor++
                    }
                }
                // Código en línea `texto`
                texto.startsWith("`", cursor) -> {
                    val fin = texto.indexOf("`", cursor + 1)
                    if (fin != -1) {
                        pushStyle(
                            SpanStyle(
                                fontFamily = FontFamily.Monospace,
                                background = ColorAcento.copy(alpha = 0.12f),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        append(" ${texto.substring(cursor + 1, fin)} ")
                        pop()
                        cursor = fin + 1
                    } else {
                        append(texto[cursor])
                        cursor++
                    }
                }
                else -> {
                    append(texto[cursor])
                    cursor++
                }
            }
        }
    }
}
