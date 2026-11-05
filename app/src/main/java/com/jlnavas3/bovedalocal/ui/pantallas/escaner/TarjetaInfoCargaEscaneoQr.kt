/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import kotlin.math.roundToInt

/**
 * Microcomponente que muestra la tarjeta flotante descriptiva con la carga de trabajo
 * del área de captura QR y consejos dinámicos mientras se ajusta el marco.
 */
@Composable
fun TarjetaInfoCargaEscaneoQr(
    visible: Boolean,
    tamanoVentanaDp: Dp,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        val relacion = tamanoVentanaDp.value / 250f
        val cargaPorcentaje = (relacion * relacion * 100f).roundToInt()

        val forma = RoundedCornerShape(CurvaturaEsquinas)

        Box(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clip(forma)
                .background(Color.Black.copy(alpha = 0.82f))
                .border(GrosorBorde, ColorAcento.copy(alpha = 0.35f), forma)
                .padding(12.dp)
        ) {
            Column {
                // Título con carga de trabajo
                val textoCarga = buildAnnotatedString {
                    append("Carga de trabajo del escáner: ")
                    withStyle(
                        SpanStyle(
                            color = if (cargaPorcentaje > 140) ColorAcento else Menta,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("$cargaPorcentaje%")
                    }
                    append(".")
                }

                Text(
                    text = textoCarga,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        lineHeight = 16.sp
                    )
                )

                Spacer(Modifier.height(4.dp))

                // Descripción contextual según tamaño
                val textoDescripcion = buildAnnotatedString {
                    append("Ideal para códigos de barras ")
                    withStyle(SpanStyle(color = ColorAcento, fontWeight = FontWeight.Bold)) {
                        when {
                            cargaPorcentaje < 90 -> append("pequeños")
                            cargaPorcentaje in 90..135 -> append("medianos")
                            else -> append("complejos y grandes")
                        }
                    }
                    append(".\n")
                    append("Úselo sólo con zoom para códigos de barras más pequeños.\n")
                    append("El zoom se ajusta con el control deslizante en la parte inferior.\n")
                    append("No sostengas el dispositivo demasiado cerca del código.")
                }

                Text(
                    text = textoDescripcion,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 15.sp
                    )
                )
            }
        }
    }
}
