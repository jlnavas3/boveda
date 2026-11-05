/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.AlineacionColumnaTabla
import com.jlnavas3.bovedalocal.util.ModeloTablaMarkdown

/**
 * Microcomponente desacoplado que renderiza una tabla Markdown estilizada nativamente en Jetpack Compose,
 * con soporte para desplazamiento horizontal, cabecera resaltada, celdas alineadas y estilos en línea.
 */
@Composable
fun TablaMarkdownNativa(
    tabla: ModeloTablaMarkdown,
    modifier: Modifier = Modifier
) {
    if (tabla.columnas.isEmpty()) return

    // Calcula un ancho uniforme y proporcional por columna para garantizar alineación vertical perfecta
    val anchosColumnas = remember(tabla) {
        tabla.columnas.mapIndexed { colIdx, col ->
            val maxLen = maxOf(
                col.encabezado.length,
                tabla.filas.maxOfOrNull { it.getOrElse(colIdx) { "" }.length } ?: 0
            )
            (maxLen * 9.5f).coerceIn(110f, 250f).dp
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(FormaPequena)
            .border(1.dp, ColorSeparadorAjustes.copy(alpha = 0.7f), FormaPequena)
            .horizontalScroll(rememberScrollState())
    ) {
        Column {
            // Fila de encabezado
            Row(
                modifier = Modifier
                    .background(ColorCampoAjustes)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabla.columnas.forEachIndexed { colIdx, col ->
                    val ancho = anchosColumnas[colIdx]
                    val alineacionBox = when (col.alineacion) {
                        AlineacionColumnaTabla.IZQUIERDA -> Alignment.CenterStart
                        AlineacionColumnaTabla.CENTRO -> Alignment.Center
                        AlineacionColumnaTabla.DERECHA -> Alignment.CenterEnd
                    }
                    val alineacionTexto = when (col.alineacion) {
                        AlineacionColumnaTabla.IZQUIERDA -> TextAlign.Start
                        AlineacionColumnaTabla.CENTRO -> TextAlign.Center
                        AlineacionColumnaTabla.DERECHA -> TextAlign.End
                    }

                    Box(
                        modifier = Modifier
                            .width(ancho)
                            .padding(horizontal = 10.dp),
                        contentAlignment = alineacionBox
                    ) {
                        Text(
                            text = parsearEstilosEnLinea(col.encabezado),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            ),
                            color = ColorTitulos,
                            textAlign = alineacionTexto
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = ColorSeparadorAjustes
            )

            // Filas de datos
            tabla.filas.forEachIndexed { filaIdx, fila ->
                val fondoFila = if (filaIdx % 2 == 1) {
                    ColorCampoAjustes.copy(alpha = 0.35f)
                } else {
                    Color.Transparent
                }

                Row(
                    modifier = Modifier
                        .background(fondoFila)
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabla.columnas.forEachIndexed { colIdx, col ->
                        val celda = fila.getOrElse(colIdx) { "" }
                        val ancho = anchosColumnas[colIdx]
                        val alineacionBox = when (col.alineacion) {
                            AlineacionColumnaTabla.IZQUIERDA -> Alignment.CenterStart
                            AlineacionColumnaTabla.CENTRO -> Alignment.Center
                            AlineacionColumnaTabla.DERECHA -> Alignment.CenterEnd
                        }
                        val alineacionTexto = when (col.alineacion) {
                            AlineacionColumnaTabla.IZQUIERDA -> TextAlign.Start
                            AlineacionColumnaTabla.CENTRO -> TextAlign.Center
                            AlineacionColumnaTabla.DERECHA -> TextAlign.End
                        }

                        Box(
                            modifier = Modifier
                                .width(ancho)
                                .padding(horizontal = 10.dp),
                            contentAlignment = alineacionBox
                        ) {
                            Text(
                                text = parsearEstilosEnLinea(celda),
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = TextoPrincipal,
                                textAlign = alineacionTexto
                            )
                        }
                    }
                }

                if (filaIdx < tabla.filas.lastIndex) {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = ColorSeparadorAjustes.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}
