/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Microcomponente con el botón de confirmación de importación de contactos hacia la bóveda.
 */
@Composable
fun BotonConfirmarImportacionContactos(
    cantidadSeleccionados: Int,
    alConfirmar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val habilitado = cantidadSeleccionados > 0
    val texto = if (habilitado) {
        "Importar $cantidadSeleccionados contacto(s) a la Bóveda"
    } else {
        "Selecciona contactos para importar"
    }

    BotonColorido(
        texto = texto,
        icono = Icons.Filled.Download,
        color = ColorAcento,
        alPulsar = alConfirmar,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}
