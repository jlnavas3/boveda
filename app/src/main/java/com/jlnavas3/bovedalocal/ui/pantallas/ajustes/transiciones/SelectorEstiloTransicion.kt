/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.transiciones

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.jlnavas3.bovedalocal.data.EstiloTransicionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Selector de lista para los 7 estilos de transición con indicadores de consumo de GPU
 * y descripciones compactas en cursiva.
 */
@Composable
fun SelectorEstiloTransicion(
    estiloSeleccionadoId: String,
    alSeleccionarEstilo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    val formaOpcion = RoundedCornerShape(CurvaturaEsquinas)
    val formaRadio = if (CurvaturaEsquinas == 0.dp) RoundedCornerShape(0.dp) else CircleShape

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Estilo de transición",
            color = ColorTextoAjustes,
            fontSize = (14 * EscalaTexto).sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        EstiloTransicionPantalla.entries.forEach { estilo ->
            val seleccionado = estilo.id.equals(estiloSeleccionadoId, ignoreCase = true)

            val colorBadge = when {
                estilo.consumoGpu.contains("0%") || estilo.consumoGpu.contains("Mínimo") -> Menta
                estilo.consumoGpu.contains("Bajo") -> Color(0xFF00BCD4)
                else -> Color(0xFFFFB300) // Moderado
            }

            val grosorFila = if (seleccionado) 1.5.dp else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 1.dp
            val colorBordeFila = if (seleccionado) ColorAcento else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") ColorBordeActual else ColorSeparadorAjustes

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaOpcion)
                    .background(ColorTarjetaAjustes)
                    .border(
                        width = grosorFila,
                        color = colorBordeFila,
                        shape = formaOpcion
                    )
                    .clickable {
                        haptica.toque()
                        alSeleccionarEstilo(estilo.id)
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Indicador de selección
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size((20 * EscalaTexto).dp.coerceAtLeast(18.dp))
                        .clip(formaRadio)
                        .background(if (seleccionado) ColorAcento else Color.Transparent)
                        .border(1.5.dp, if (seleccionado) ColorAcento else ColorAjusteGris.copy(alpha = 0.6f), formaRadio),
                    contentAlignment = Alignment.Center
                ) {
                    if (seleccionado) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = ColorSobreAcento, modifier = Modifier.size((13 * EscalaTexto).dp.coerceAtLeast(11.dp)))
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = estilo.titulo,
                            color = ColorTextoAjustes,
                            fontSize = (13.5f * EscalaTexto).sp,
                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium
                        )

                        // Badge de consumo de GPU
                        Box(
                            modifier = Modifier
                                .clip(FormaPequena)
                                .background(colorBadge.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = estilo.consumoGpu,
                                color = colorBadge,
                                fontSize = (10 * EscalaTexto).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = estilo.descripcion,
                        color = ColorAjusteGris,
                        fontSize = (11.5f * EscalaTexto).sp,
                        lineHeight = (15 * EscalaTexto).sp,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = estilo.detalleRendimiento,
                        color = ColorAjusteGris.copy(alpha = 0.75f),
                        fontSize = (10.5f * EscalaTexto).sp,
                        lineHeight = (14 * EscalaTexto).sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}
