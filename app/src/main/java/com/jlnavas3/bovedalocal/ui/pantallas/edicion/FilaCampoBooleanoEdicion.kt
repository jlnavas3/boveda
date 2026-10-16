/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Composable micro desacoplado para edición de campos tipo [com.jlnavas3.bovedalocal.data.TipoCampo.BOOLEANO].
 */
@Composable
fun FilaCampoBooleanoEdicion(
    campo: CampoPersonalizado,
    etiqueta: String,
    alModificar: (CampoPersonalizado) -> Unit,
    modifier: Modifier = Modifier
) {
    val activo = campo.valor.equals("true", ignoreCase = true)
    val tieneBorde = GrosorBorde > 0.dp && EstiloBorde != "ninguno"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(FormaCampo)
            .background(ColorCampoAjustes)
            .then(
                if (tieneBorde) Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                else Modifier
            )
            .clickable { alModificar(campo.copy(valor = (!activo).toString())) }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = etiqueta.ifBlank { "Campo Sí/No" },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextoPrincipal
            )
            Text(
                text = if (activo) "Activado (Sí)" else "Desactivado (No)",
                fontSize = 12.sp,
                color = if (activo) ColorAcento else TextoSecundario
            )
        }
        SwitchBoveda(
            checked = activo,
            onCheckedChange = { alModificar(campo.copy(valor = it.toString())) }
        )
    }
}
