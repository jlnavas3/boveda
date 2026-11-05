/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.transiciones

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Banner informativo que se muestra cuando el dispositivo tiene las animaciones
 * desactivadas a nivel de sistema operativo (escala de animador en 0x).
 * Ofrece un enlace directo a los Ajustes de Accesibilidad para reactivarlas.
 */
@Composable
fun AvisoAnimacionesDesactivadas(
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val formaAviso = RoundedCornerShape(CurvaturaEsquinas)
    val grosorAviso = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 1.dp

    val fondoAviso = if (esOscuroActivo) Color(0xFF261D0F) else Color(0xFFFFF7ED)
    val bordeAviso = if (esOscuroActivo) Color(0xFF8D5C14) else Color(0xFFFED7AA)
    val textoAviso = if (esOscuroActivo) Color(0xFFFFB74D) else Color(0xFFC2410C)
    val fondoBoton = if (esOscuroActivo) Color(0xFF8D5C14).copy(alpha = 0.35f) else Color(0xFFFFEDD5)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(formaAviso)
            .background(fondoAviso)
            .border(grosorAviso, bordeAviso, formaAviso)
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                tint = textoAviso,
                modifier = Modifier.size((20 * EscalaTexto).dp.coerceAtLeast(18.dp))
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Animaciones desactivadas en el sistema",
                color = textoAviso,
                fontSize = (13 * EscalaTexto).sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Tu dispositivo tiene las animaciones desactivadas a nivel global (escala 0x en Accesibilidad o Desarrollador). Las transiciones no se moverán en pantalla hasta reactivarlas en los ajustes de Android.",
            color = ColorTextoAjustes.copy(alpha = 0.9f),
            fontSize = (11.5f * EscalaTexto).sp,
            lineHeight = (16 * EscalaTexto).sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Busca la opción «Quitar animaciones» o «Reducir movimiento» en Accesibilidad.",
            color = ColorAjusteGris,
            fontSize = (11 * EscalaTexto).sp,
            fontStyle = FontStyle.Italic
        )

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(FormaBoton)
                .background(fondoBoton)
                .clickable {
                    try {
                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        contexto.startActivity(intent)
                    } catch (_: Exception) {
                        try {
                            val intentGeneral = Intent(Settings.ACTION_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            contexto.startActivity(intentGeneral)
                        } catch (_: Exception) {}
                    }
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                contentDescription = null,
                tint = textoAviso,
                modifier = Modifier.size((16 * EscalaTexto).dp.coerceAtLeast(14.dp))
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Abrir Ajustes de Accesibilidad de Android",
                color = textoAviso,
                fontSize = (12 * EscalaTexto).sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
