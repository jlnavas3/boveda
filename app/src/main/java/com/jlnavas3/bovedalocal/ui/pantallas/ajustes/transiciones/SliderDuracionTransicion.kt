/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.transiciones

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import kotlin.math.roundToInt

/**
 * Control deslizante para calibrar la duración en milisegundos de la animación de pantalla.
 */
@Composable
fun SliderDuracionTransicion(
    duracionMs: Int,
    alCambiarDuracion: (Int) -> Unit,
    deshabilitado: Boolean = false,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val grosor = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 1.dp
    val colorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") ColorBordeActual else ColorSeparadorAjustes

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .border(grosor, colorBorde, forma)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Duración de transición",
                color = ColorTextoAjustes,
                fontSize = (13.5f * EscalaTexto).sp,
                fontWeight = FontWeight.SemiBold
            )

            Box(
                modifier = Modifier
                    .clip(FormaPequena)
                    .background(ColorAcento.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$duracionMs ms",
                    color = ColorAcento,
                    fontSize = (12 * EscalaTexto).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Tiempo que tarda la pantalla en completar su recorrido. El valor recomendado es 220 ms.",
            color = ColorAjusteGris,
            fontSize = (11.5f * EscalaTexto).sp,
            lineHeight = (15 * EscalaTexto).sp,
            fontStyle = FontStyle.Italic
        )

        Spacer(Modifier.height(8.dp))

        Slider(
            value = duracionMs.toFloat(),
            onValueChange = { alCambiarDuracion(((it / 10f).roundToInt() * 10).coerceIn(120, 380)) },
            valueRange = 120f..380f,
            steps = 25,
            enabled = !deshabilitado,
            colors = SliderDefaults.colors(
                thumbColor = ColorAcento,
                activeTrackColor = ColorAcento,
                inactiveTrackColor = ColorSeparadorAjustes
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Accesos rápidos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BotonAccesoRapidoDuracion("Rápido", duracionMs == 160) { alCambiarDuracion(160) }
            BotonAccesoRapidoDuracion("Nativo", duracionMs == 220) { alCambiarDuracion(220) }
            BotonAccesoRapidoDuracion("Suave", duracionMs == 300) { alCambiarDuracion(300) }
        }
    }
}
