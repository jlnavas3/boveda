/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.AlturaIndicadores
import com.jlnavas3.bovedalocal.ui.theme.EstiloIndicadores
import com.jlnavas3.bovedalocal.ui.theme.OpacidadIndicadores

/**
 * Renderiza cualquier lista de segmentos indicadores (datos de entrada o máscara diff de sincronización)
 * respetando el estilo global configurado por el usuario.
 */
@Composable
fun IndicadorSegmentosTarjeta(
    segmentos: List<SegmentoIndicadorDato>,
    modifier: Modifier = Modifier,
    estilo: String = EstiloIndicadores,
    altura: Dp = AlturaIndicadores,
    opacidad: Float = OpacidadIndicadores
) {
    if (estilo == "ninguno") return

    when (estilo) {
        "puntos" -> {
            IndicadorPuntosTarjeta(
                segmentos = segmentos,
                modifier = modifier,
                diametro = altura,
                opacidad = opacidad
            )
        }
        "iconos" -> {
            IndicadorIconosTarjeta(
                segmentos = segmentos,
                modifier = modifier,
                tamanoIcono = altura,
                opacidad = opacidad
            )
        }
        "lateral" -> {
            IndicadorLateralTarjeta(
                segmentos = segmentos,
                modifier = modifier,
                ancho = altura,
                opacidad = opacidad
            )
        }
        else -> {
            IndicadorLineasTarjeta(
                segmentos = segmentos,
                modifier = modifier,
                altura = altura,
                opacidad = opacidad
            )
        }
    }
}

/**
 * Despachador principal que orquesta el renderizado del indicador de contenido de la tarjeta
 * según el estilo configurado (líneas superiores, puntos, iconos, franja lateral o ninguno).
 */
@Composable
fun IndicadorContenidoTarjeta(
    entrada: Entrada,
    modifier: Modifier = Modifier,
    estilo: String = EstiloIndicadores,
    altura: Dp = AlturaIndicadores,
    opacidad: Float = OpacidadIndicadores
) {
    if (estilo == "ninguno") return

    val segmentos = recordarSegmentosEntrada(entrada)
    IndicadorSegmentosTarjeta(
        segmentos = segmentos,
        modifier = modifier,
        estilo = estilo,
        altura = altura,
        opacidad = opacidad
    )
}
