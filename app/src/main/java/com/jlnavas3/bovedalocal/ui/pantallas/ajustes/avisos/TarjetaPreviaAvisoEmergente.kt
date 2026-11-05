/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.avisos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo

/**
 * Tarjeta interactiva con botón para disparar un aviso emergente de demostración
 * y verificar la posición y el estilo configurados en tiempo real.
 */
@Composable
fun TarjetaPreviaAvisoEmergente(
    mostrarIds: Boolean,
    alProbarAviso: () -> Unit
) {
    ComponenteGrupo(
        etiqueta = "Comprobación en vivo",
        icono = Icons.Filled.PlayArrow,
        colorIcono = Color(0xFF10B981),
        idGrupo = "06-SIS-AVZ-TST-G03",
        mostrarId = mostrarIds
    ) {
        ComponenteBotonFila(
            titulo = "Probar aviso emergente",
            valorTexto = "Disparar",
            icono = null,
            idFila = "06-SIS-AVZ-TST-PRV",
            mostrarId = mostrarIds,
            alPulsar = alProbarAviso,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
