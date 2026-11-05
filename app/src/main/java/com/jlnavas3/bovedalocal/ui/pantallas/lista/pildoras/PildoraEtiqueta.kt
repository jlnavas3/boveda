/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Píldora compacta para el filtro de Etiquetas con menú desplegable contextual.
 * Muestra el icono [ 🏷️ Todas ▾ ] o [ 🏷️ <etiqueta> ✕ ] cuando está activa.
 */
@Composable
fun PildoraEtiqueta(
    etiquetas: List<String> = emptyList(),
    filtroEtiqueta: String?,
    conteoPorEtiqueta: Map<String, Int> = emptyMap(),
    totalEntradas: Int = 0,
    alAbrirSelector: () -> Unit = {},
    alSeleccionarEtiqueta: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuAbierto by remember { mutableStateOf(false) }

    val activo = !filtroEtiqueta.isNullOrBlank()
    val texto = if (activo) normalizarEtiqueta(filtroEtiqueta) else null
    val descripcionHud = if (activo) "Etiqueta: #$filtroEtiqueta" else "Filtrar por etiqueta"

    Box(modifier = modifier) {
        PildoraFiltroBase(
            icono = Icons.AutoMirrored.Filled.Label,
            texto = texto,
            activo = activo,
            colorAcento = ColorAcento,
            descripcionHud = descripcionHud,
            alPulsar = {
                alAbrirSelector()
                menuAbierto = true
            },
            alLimpiar = if (activo) { { alSeleccionarEtiqueta(null) } } else null
        )

        DropdownFiltroEtiquetas(
            expanded = menuAbierto,
            onDismissRequest = { menuAbierto = false },
            etiquetas = etiquetas,
            filtroEtiqueta = filtroEtiqueta,
            conteoPorEtiqueta = conteoPorEtiqueta,
            totalEntradas = totalEntradas,
            alSeleccionarEtiqueta = alSeleccionarEtiqueta
        )
    }
}
