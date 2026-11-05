/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.unificar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Muestra el texto explicativo de cabecera indicando la cantidad de entradas a fusionar
 * e invitando a seleccionar la que se conservará como principal.
 */
@Composable
fun CabeceraDialogoUnificar(
    cantidad: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Se unificarán $cantidad credenciales seleccionadas en una sola entrada sin pérdida de datos. Selecciona cuál conservar como principal:",
        style = MaterialTheme.typography.bodyMedium,
        color = TextoSecundario,
        modifier = modifier.fillMaxWidth()
    )
}
