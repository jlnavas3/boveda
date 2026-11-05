/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.transiciones

import android.os.Build
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.jlnavas3.bovedalocal.ui.theme.Menta

/**
 * Fila de ajuste para habilitar o deshabilitar la navegación gestual predictiva
 * en dispositivos con Android 14 (API 34) o superior.
 */
@Composable
fun OpcionGestoPredictivo(
    gestoPredictivoHabilitado: Boolean,
    alCambiarGestoPredictivoHabilitado: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val esCompatible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val grosor = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 1.dp
    val colorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") ColorBordeActual else ColorSeparadorAjustes

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .border(grosor, colorBorde, forma)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Gesto de retroceso predictivo",
                color = ColorTextoAjustes,
                fontSize = (13.5f * EscalaTexto).sp,
                fontWeight = FontWeight.SemiBold
            )
            if (esCompatible) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(Menta.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Android 14+",
                        color = Menta,
                        fontSize = (10 * EscalaTexto).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (esCompatible) {
                    "Permite arrastrar la pantalla desde el borde lateral para previsualizar la pantalla anterior antes de soltarla."
                } else {
                    "Esta función requiere Android 14 o posterior. En esta versión de Android el retroceso funcionará de manera estándar."
                },
                color = ColorAjusteGris,
                fontSize = (11.5f * EscalaTexto).sp,
                lineHeight = (15 * EscalaTexto).sp,
                fontStyle = FontStyle.Italic
            )
        }

        Switch(
            checked = gestoPredictivoHabilitado && esCompatible,
            onCheckedChange = alCambiarGestoPredictivoHabilitado,
            enabled = esCompatible,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ColorAcento,
                uncheckedThumbColor = ColorAjusteGris,
                uncheckedTrackColor = ColorSeparadorAjustes
            )
        )
    }
}
