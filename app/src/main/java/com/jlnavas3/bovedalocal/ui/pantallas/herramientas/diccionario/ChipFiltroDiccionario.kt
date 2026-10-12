/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.util.Locale

@Composable
fun ChipFiltroDiccionario(
    etiqueta: String,
    cantidad: Int,
    seleccionado: Boolean,
    alSeleccionar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val fondo = if (seleccionado) ColorAcento else ColorTarjetaAjustes
    val colorEtiqueta = if (seleccionado) ColorSobreAcento else TextoSecundario
    val colorCantidad = if (seleccionado) ColorSobreAcento else TextoPrincipal

    Column(
        modifier = modifier
            .clip(forma)
            .background(fondo)
            .border(
                width = 1.dp,
                color = if (seleccionado) ColorAcento else Color.Transparent,
                shape = forma
            )
            .clickable(onClick = alSeleccionar)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = colorEtiqueta
        )
        Text(
            text = String.format(Locale.ROOT, "%,d", cantidad),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = colorCantidad
        )
    }
}
