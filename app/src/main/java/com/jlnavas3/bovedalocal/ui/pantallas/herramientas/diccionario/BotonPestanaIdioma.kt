/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

@Composable
fun BotonPestanaIdioma(
    titulo: String,
    subtitulo: String,
    seleccionado: Boolean,
    alSeleccionar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val colorFondo = if (seleccionado) ColorAcento else ColorTarjetaAjustes
    val colorTexto = if (seleccionado) ColorSobreAcento else TextoPrincipal
    val colorSub = if (seleccionado) ColorSobreAcento.copy(alpha = 0.85f) else TextoSecundario

    Column(
        modifier = modifier
            .clip(forma)
            .background(colorFondo)
            .border(
                width = 1.dp,
                color = if (seleccionado) ColorAcento else Color.Transparent,
                shape = forma
            )
            .clickable(onClick = alSeleccionar)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )
        Text(
            text = subtitulo,
            style = MaterialTheme.typography.bodySmall,
            color = colorSub
        )
    }
}
