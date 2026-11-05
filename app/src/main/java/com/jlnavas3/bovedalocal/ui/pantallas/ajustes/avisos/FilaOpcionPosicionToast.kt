/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.avisos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.PosicionToast
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Fila individual con RadioButton que representa una opción de posición para los avisos emergentes.
 */
@Composable
fun FilaOpcionPosicionToast(
    posicion: PosicionToast,
    seleccionada: Boolean,
    alSeleccionar: (PosicionToast) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { alSeleccionar(posicion) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = seleccionada,
            onClick = { alSeleccionar(posicion) },
            colors = RadioButtonDefaults.colors(
                selectedColor = ColorAcento,
                unselectedColor = ColorAjusteGris.copy(alpha = 0.6f)
            )
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (posicion.esPredeterminada) "${posicion.etiqueta} (Predeterminado)" else posicion.etiqueta,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (seleccionada) FontWeight.SemiBold else FontWeight.Normal,
                color = if (seleccionada) ColorAcento else ColorTextoAjustes,
                fontSize = 15.sp
            )
            Text(
                text = posicion.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = ColorAjusteGris,
                fontSize = 12.5.sp,
                lineHeight = 16.sp
            )
        }
    }
}
