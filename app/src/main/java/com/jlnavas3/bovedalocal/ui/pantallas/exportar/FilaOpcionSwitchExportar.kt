/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoPiePagina
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Fila de opción con switch desacoplada para el panel de configuración de exportación.
 */
@Composable
fun FilaOpcionSwitchExportar(
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(FormaPequena)
            .clickable(enabled = habilitado) {
                onCheckedChange(!checked)
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            TextoCuerpo(
                texto = titulo,
                tamano = TamanoCuerpo.PEQUENO,
                color = if (habilitado) TextoPrincipal else TextoSecundario.copy(alpha = 0.5f)
            )
            TextoPiePagina(
                texto = subtitulo,
                color = if (habilitado) TextoSecundario else TextoSecundario.copy(alpha = 0.4f)
            )
        }

        SwitchBoveda(
            checked = checked && habilitado,
            onCheckedChange = { if (habilitado) onCheckedChange(it) }
        )
    }
}
