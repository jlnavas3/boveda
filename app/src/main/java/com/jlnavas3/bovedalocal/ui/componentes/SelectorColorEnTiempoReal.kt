/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreTarjetas
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

import com.jlnavas3.bovedalocal.ui.theme.aHexConAlfa

/**
 * Selector de color interactivo con actualización en tiempo real.
 * Permite ajustar Tono (Hue), Saturación, Brillo y opcionalmente Opacidad/Alfa (transparencia).
 */
@Composable
fun SelectorColorEnTiempoReal(
    colorInicial: Color,
    modifier: Modifier = Modifier,
    titulo: String = "Elegir color",
    permitirAlfa: Boolean = false,
    alCambiarColor: (Color) -> Unit
) {
    val hsvInicial = remember(colorInicial) { colorAhsv(colorInicial) }
    var hue by remember(colorInicial) { mutableFloatStateOf(hsvInicial.first) }
    var sat by remember(colorInicial) { mutableFloatStateOf(hsvInicial.second.coerceIn(0.0f, 1f)) }
    var valLum by remember(colorInicial) { mutableFloatStateOf(hsvInicial.third.coerceIn(0.0f, 1f)) }
    var alpha by remember(colorInicial) { mutableFloatStateOf(colorInicial.alpha) }

    val colorActual = remember(hue, sat, valLum, alpha) {
        Color.hsv(hue, sat, valLum, alpha)
    }

    val textoHex = remember(colorActual) { colorActual.aHexConAlfa() }

    val degradadoArcoiris = remember {
        Brush.horizontalGradient(
            listOf(
                Color.Red, Color(0xFFFF8A00), Color.Yellow, Color.Green,
                Color.Cyan, Color.Blue, Color(0xFF8A00FF), Color.Magenta, Color.Red
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Cabecera con previsualización en tiempo real y código HEX
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(FormaPequena)
                    .background(if (colorActual.alpha < 1f) Color.Gray.copy(alpha = 0.3f) else Superficie)
                    .border(GrosorBorde.coerceAtLeast(1.dp), Borde, FormaPequena)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorActual)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = if (colorActual.alpha == 0f) "$textoHex (Transparente)" else textoHex,
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                )

                if (permitirAlfa) {
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (alpha == 0f) ColorAcento.copy(alpha = 0.2f) else Superficie)
                                .border(1.dp, if (alpha == 0f) ColorAcento else Borde, RoundedCornerShape(6.dp))
                                .clickable {
                                    alpha = 0f
                                    val nuevo = Color.hsv(hue, sat, valLum, 0f)
                                    alCambiarColor(nuevo)
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Transparente",
                                color = if (alpha == 0f) ColorAcento else TextoSecundario,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (alpha == 1f) ColorAcento.copy(alpha = 0.2f) else Superficie)
                                .border(1.dp, if (alpha == 1f) ColorAcento else Borde, RoundedCornerShape(6.dp))
                                .clickable {
                                    alpha = 1f
                                    val nuevo = Color.hsv(hue, sat, valLum, 1f)
                                    alCambiarColor(nuevo)
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Opaco",
                                color = if (alpha == 1f) ColorAcento else TextoSecundario,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // Control deslizante de TONO (Hue 0° a 360°)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Tono", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(degradadoArcoiris)
            )
            SliderBoveda(
                value = hue,
                onValueChange = { nuevaHue ->
                    hue = nuevaHue
                    val nuevo = Color.hsv(hue, sat, valLum, alpha)
                    alCambiarColor(nuevo)
                },
                valueRange = 0f..360f,
                colors = SliderDefaults.colors(
                    thumbColor = colorActual,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }

        // Control deslizante de SATURACIÓN
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Saturación", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
            SliderBoveda(
                value = sat,
                onValueChange = { nuevaSat ->
                    sat = nuevaSat
                    val nuevo = Color.hsv(hue, sat, valLum, alpha)
                    alCambiarColor(nuevo)
                },
                valueRange = 0.05f..1f,
                colorAcento = colorActual
            )
        }

        // Control deslizante de BRILLO / LUMINOSIDAD
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Brillo", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
            SliderBoveda(
                value = valLum,
                onValueChange = { nuevoVal ->
                    valLum = nuevoVal
                    val nuevo = Color.hsv(hue, sat, valLum, alpha)
                    alCambiarColor(nuevo)
                },
                valueRange = 0.15f..1f,
                colorAcento = colorActual
            )
        }

        // Control deslizante de OPACIDAD / TRANSPARENCIA
        if (permitirAlfa) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Opacidad", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
                    Text("${(alpha * 100).toInt()}%", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
                }
                SliderBoveda(
                    value = alpha,
                    onValueChange = { nuevaAlpha ->
                        alpha = nuevaAlpha
                        val nuevo = Color.hsv(hue, sat, valLum, alpha)
                        alCambiarColor(nuevo)
                    },
                    valueRange = 0f..1f,
                    colorAcento = colorActual
                )
            }
        }
    }
}
