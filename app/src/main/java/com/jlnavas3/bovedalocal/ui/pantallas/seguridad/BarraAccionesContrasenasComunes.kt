/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.ItemAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Barra inferior fija de acciones para la pantalla de Contraseñas Comunes:
 * Acción para registrar una contraseña en la lista negra local.
 */
@Composable
fun BarraAccionesContrasenasComunes(
    alAnadirContrasena: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ItemAccionBarraInferior(
            icono = Icons.Filled.AddCircle,
            etiqueta = "Nueva",
            alPulsar = alAnadirContrasena,
            colorIcono = ColorAcento,
            descripcion = "Añadir contraseña común"
        )
    )

    BarraAccionesInferiorBoveda(
        items = items,
        modifier = modifier
    )
}
