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
import com.jlnavas3.bovedalocal.data.NivelReboteSpring
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Selector de nivel de rebote y amortiguación elástica de la física Spring.
 */
@Composable
fun SelectorAmortiguacionSpring(
    amortiguacionActualId: String,
    alSeleccionarAmortiguacion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val formaNivel = RoundedCornerShape(CurvaturaEsquinas)
    val formaRadio = if (CurvaturaEsquinas == 0.dp) RoundedCornerShape(0.dp) else CircleShape

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Nivel de rebote (Amortiguación)",
            color = ColorTextoAjustes,
            fontSize = (13 * EscalaTexto).sp,
            fontWeight = FontWeight.SemiBold
        )

        NivelReboteSpring.entries.forEach { nivel ->
            val seleccionado = nivel.id.equals(amortiguacionActualId, ignoreCase = true)
            val grosor = if (seleccionado) 1.5.dp else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 1.dp
            val colorBorde = if (seleccionado) ColorAcento else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") ColorBordeActual else ColorSeparadorAjustes

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaNivel)
                    .background(ColorCampoAjustes)
                    .border(
                        width = grosor,
                        color = colorBorde,
                        shape = formaNivel
                    )
                    .clickable { alSeleccionarAmortiguacion(nivel.id) }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size((18 * EscalaTexto).dp.coerceAtLeast(16.dp))
                        .clip(formaRadio)
                        .background(if (seleccionado) ColorAcento else Color.Transparent)
                        .border(1.5.dp, if (seleccionado) ColorAcento else ColorAjusteGris.copy(alpha = 0.6f), formaRadio),
                    contentAlignment = Alignment.Center
                ) {
                    if (seleccionado) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = ColorSobreAcento, modifier = Modifier.size((12 * EscalaTexto).dp.coerceAtLeast(10.dp)))
                    }
                }

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        text = nivel.etiqueta,
                        color = ColorTextoAjustes,
                        fontSize = (13 * EscalaTexto).sp,
                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = nivel.descripcion,
                        color = ColorAjusteGris,
                        fontSize = (11 * EscalaTexto).sp,
                        lineHeight = (14 * EscalaTexto).sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}
