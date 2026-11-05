/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.avisos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch

/**
 * Grupo de configuración para el formato visual (Descriptivo vs Minimalista) de los avisos emergentes.
 */
@Composable
fun GrupoEstiloAvisosEmergentes(
    toastDescriptivo: Boolean,
    mostrarIds: Boolean,
    alCambiarEstilo: (Boolean) -> Unit
) {
    ComponenteGrupo(
        etiqueta = "Estilo y formato",
        icono = Icons.Filled.Palette,
        colorIcono = Color(0xFF8B5CF6),
        idGrupo = "06-SIS-AVZ-TST-G01",
        mostrarId = mostrarIds
    ) {
        ComponenteSwitch(
            titulo = "Formato descriptivo",
            subtitulo = if (toastDescriptivo) {
                "Tarjeta completa con cabecera diferenciada, título, icono temático y botón de cierre (✕)."
            } else {
                "Píldora minimalista de una sola línea, ligera y compacta con botón de cierre (✕)."
            },
            icono = null,
            activo = toastDescriptivo,
            idFila = "06-SIS-AVZ-TST-FMT",
            mostrarId = mostrarIds,
            alCambiar = alCambiarEstilo
        )
    }
}
