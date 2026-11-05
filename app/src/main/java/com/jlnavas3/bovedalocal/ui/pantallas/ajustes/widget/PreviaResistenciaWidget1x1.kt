/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.CalculadorTiempoResistencia
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import kotlin.math.roundToInt

/**
 * Microcomponente que muestra en tiempo real la resistencia matemática y entropía
 * de la configuración actual del Widget 1x1.
 */
@Composable
fun PreviaResistenciaWidget1x1(
    ajustes: AjustesApp,
    modifier: Modifier = Modifier
) {
    val opciones = remember(
        ajustes.widget1x1Modo,
        ajustes.widget1x1Longitud,
        ajustes.widget1x1Simbolos,
        ajustes.widget1x1DicewarePalabras,
        ajustes.widget1x1DicewareSeparador,
        ajustes.widget1x1Patron,
        ajustes.generadorExcluirAmbiguos,
        ajustes.generadorIdiomaFrases
    ) {
        when (ajustes.widget1x1Modo) {
            "patron" -> OpcionesGenerador(
                modoPatron = true,
                patron = ajustes.widget1x1Patron
            )
            "diceware" -> OpcionesGenerador(
                modoFrase = true,
                palabras = ajustes.widget1x1DicewarePalabras,
                separadorFrase = ajustes.widget1x1DicewareSeparador,
                idiomaFrase = ajustes.generadorIdiomaFrases
            )
            else -> OpcionesGenerador(
                longitud = ajustes.widget1x1Longitud,
                mayusculas = true,
                minusculas = true,
                digitos = true,
                simbolos = true,
                simbolosPersonalizados = ajustes.widget1x1Simbolos,
                excluirAmbiguos = ajustes.generadorExcluirAmbiguos
            )
        }
    }

    val bits = remember(opciones) { PasswordGenerator.entropiaBits(opciones) }
    val tiempoResistencia = remember(bits) { CalculadorTiempoResistencia.formatear(bits) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(FormaPequena)
            .background(ColorCampoAjustes)
            .border(0.8.dp, ColorSeparadorAjustes, FormaPequena)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ColorGenerador.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Shield,
                        contentDescription = null,
                        tint = ColorGenerador,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        text = tiempoResistencia,
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Resistencia estimada del widget",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(FormaPequena)
                    .background(Menta.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${bits.roundToInt()} bits",
                    color = Menta,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
