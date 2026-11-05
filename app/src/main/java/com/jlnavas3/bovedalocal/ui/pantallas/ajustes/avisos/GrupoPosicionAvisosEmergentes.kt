/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.avisos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.PosicionToast
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador

/**
 * Grupo de configuración con opciones de selección exclusiva (RadioButtons)
 * para definir la posición en pantalla de los avisos emergentes.
 */
@Composable
fun GrupoPosicionAvisosEmergentes(
    posicionActual: PosicionToast,
    mostrarIds: Boolean,
    alSeleccionarPosicion: (PosicionToast) -> Unit
) {
    ComponenteGrupo(
        etiqueta = "Posición en pantalla",
        icono = Icons.Filled.VerticalAlignBottom,
        colorIcono = Color(0xFF00ACC1),
        idGrupo = "06-SIS-AVZ-TST-G02",
        mostrarId = mostrarIds
    ) {
        val opciones = PosicionToast.entries
        opciones.forEachIndexed { indice, opcion ->
            if (indice > 0) {
                ComponenteSeparador(sangriaInicio = 48.dp)
            }
            FilaOpcionPosicionToast(
                posicion = opcion,
                seleccionada = opcion == posicionActual,
                alSeleccionar = alSeleccionarPosicion
            )
        }
    }
}
