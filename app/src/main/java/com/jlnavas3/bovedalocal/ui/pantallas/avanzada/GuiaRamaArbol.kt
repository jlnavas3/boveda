/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual

/**
 * Dibuja las guías visuales de ramas para representar la jerarquía multinivel
 * en el árbol de reorganización de ajustes (líneas guía verticales y codos conectores).
 */
@Composable
fun GuiaRamaArbol(
    profundidad: Int,
    esUltimoHermano: Boolean,
    modifier: Modifier = Modifier,
    alturaFila: Dp = 38.dp
) {
    if (profundidad <= 0) return

    val anchoColumnaDp = 14.dp
    val anchoTotalDp = anchoColumnaDp * profundidad

    Canvas(
        modifier = modifier
            .width(anchoTotalDp)
            .height(alturaFila)
    ) {
        val colorGuia = ColorBordeActual.copy(alpha = 0.35f)
        val grosorPx = 1.2.dp.toPx()
        val anchoColumnaPx = size.width / profundidad
        val mitadAlturaPx = size.height / 2f

        for (i in 0 until profundidad) {
            val centroX = (i * anchoColumnaPx) + (anchoColumnaPx / 2f)
            val esUltimoNivel = (i == profundidad - 1)

            if (esUltimoNivel) {
                // Línea vertical que desciende hasta el codo o cruza la fila
                val finY = if (esUltimoHermano) mitadAlturaPx else size.height
                drawLine(
                    color = colorGuia,
                    start = Offset(centroX, 0f),
                    end = Offset(centroX, finY),
                    strokeWidth = grosorPx,
                    cap = StrokeCap.Round
                )
                // Codo conector horizontal hacia la tarjeta a la derecha
                drawLine(
                    color = colorGuia,
                    start = Offset(centroX, mitadAlturaPx),
                    end = Offset(size.width, mitadAlturaPx),
                    strokeWidth = grosorPx,
                    cap = StrokeCap.Round
                )
            } else {
                // Línea vertical continua para niveles ancestros
                drawLine(
                    color = colorGuia,
                    start = Offset(centroX, 0f),
                    end = Offset(centroX, size.height),
                    strokeWidth = grosorPx,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
