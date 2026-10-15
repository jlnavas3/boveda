/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.titulos.reglas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaChip
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun ChipOctetoRouter(
    octeto: Int,
    alEliminar: (Int) -> Unit,
    permitirEliminar: Boolean = true,
    modifier: Modifier = Modifier
) {
    val forma = FormaChip
    val tieneBorde = GrosorBorde > 0.dp && EstiloBorde != "ninguno"
    val colorBorde = if (tieneBorde) ColorBordeActual else ColorAcento.copy(alpha = 0.25f)
    val grosorEfectivo = if (tieneBorde) GrosorBorde else 1.dp
    val formaIcono = if (CurvaturaEsquinas == 0.dp) RoundedCornerShape(0.dp) else CircleShape

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(forma)
            .background(SuperficieAlta)
            .border(grosorEfectivo, colorBorde, forma)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = ".$octeto",
            color = TextoPrincipal,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (permitirEliminar) {
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(18.dp)
                    .clip(formaIcono)
                    .clickable { alEliminar(octeto) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Eliminar octeto $octeto",
                    tint = TextoSecundario,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaChipOctetoRouter() {
    BovedaTheme {
        ChipOctetoRouter(
            octeto = 1,
            alEliminar = {}
        )
    }
}
