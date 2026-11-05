/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAnclado
import com.jlnavas3.bovedalocal.ui.theme.ColorFavorito

/**
 * Modo de renderizado para los switches de opciones avanzadas.
 */
enum class ModoFiltroSwitches {
    TODOS,
    SOLO_ACTIVOS,
    SOLO_INACTIVOS
}

/**
 * Microcomponente desacoplado que renderiza los conmutadores de opciones avanzadas
 * (Favorito, Anclado, Ignorar en auditoría) filtrados según su estado de activación.
 */
@Composable
fun SeccionSwitchesEdicion(
    favorito: Boolean,
    alAlternarFavorito: (Boolean) -> Unit,
    anclado: Boolean,
    alAlternarAnclado: (Boolean) -> Unit,
    ignoradaEnSalud: Boolean,
    alAlternarIgnoradaEnSalud: (Boolean) -> Unit,
    modo: ModoFiltroSwitches = ModoFiltroSwitches.TODOS,
    modifier: Modifier = Modifier
) {
    val mostrarFavorito = when (modo) {
        ModoFiltroSwitches.TODOS -> true
        ModoFiltroSwitches.SOLO_ACTIVOS -> favorito
        ModoFiltroSwitches.SOLO_INACTIVOS -> !favorito
    }

    val mostrarAnclado = when (modo) {
        ModoFiltroSwitches.TODOS -> true
        ModoFiltroSwitches.SOLO_ACTIVOS -> anclado
        ModoFiltroSwitches.SOLO_INACTIVOS -> !anclado
    }

    val mostrarSalud = when (modo) {
        ModoFiltroSwitches.TODOS -> true
        ModoFiltroSwitches.SOLO_ACTIVOS -> ignoradaEnSalud
        ModoFiltroSwitches.SOLO_INACTIVOS -> !ignoradaEnSalud
    }

    if (!mostrarFavorito && !mostrarAnclado && !mostrarSalud) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (mostrarFavorito) {
            FilaSeccionSwitchEdicion(
                icono = Icons.Filled.Star,
                colorIcono = ColorFavorito,
                titulo = "Marcar como favorito",
                descripcion = "Destacar con estrella en la lista",
                activado = favorito,
                alCambiar = alAlternarFavorito
            )
        }

        if (mostrarAnclado) {
            FilaSeccionSwitchEdicion(
                icono = Icons.Filled.PushPin,
                colorIcono = ColorAnclado,
                titulo = "Anclado",
                descripcion = "Fijar en el grupo 'Anclados' arriba en la lista principal",
                activado = anclado,
                alCambiar = alAlternarAnclado
            )
        }

        if (mostrarSalud) {
            FilaSeccionSwitchEdicion(
                icono = Icons.Filled.Shield,
                colorIcono = Color(0xFFE57373),
                titulo = "Ignorar en auditoría",
                descripcion = "Excluir de análisis de contraseñas vulnerables",
                activado = ignoradaEnSalud,
                alCambiar = alAlternarIgnoradaEnSalud
            )
        }
    }
}
