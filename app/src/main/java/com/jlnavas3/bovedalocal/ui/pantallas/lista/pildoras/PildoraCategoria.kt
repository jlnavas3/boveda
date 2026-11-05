/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.IconosCategorias
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Píldora compacta para el filtro de Categorías con menú desplegable contextual.
 * Muestra el icono [ 📁 Todas ▾ ] o [ 📁 <nombre> (N) ✕ ] cuando está activa.
 */
@Composable
fun PildoraCategoria(
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    conteoPorCategoria: Map<String, Int>,
    alAbrirSelector: () -> Unit = {},
    alSeleccionarCategoria: (String?) -> Unit,
    alGestionarCategorias: () -> Unit = {},
    totalEntradas: Int = 0,
    modifier: Modifier = Modifier
) {
    var menuAbierto by remember { mutableStateOf(false) }

    val categoriaActiva = categorias.firstOrNull { it.id == categoriaSeleccionadaId }
    val activo = categoriaActiva != null

    val colorBase = if (categoriaActiva != null) {
        IconosCategorias.parsearColorHex(categoriaActiva.colorHex) ?: ColorAcento
    } else {
        ColorAcento
    }

    val icono = if (categoriaActiva != null) {
        IconosCategorias.obtenerIcono(categoriaActiva.icono)
    } else {
        Icons.Filled.Folder
    }

    val texto = if (categoriaActiva != null) {
        val conteo = conteoPorCategoria[categoriaActiva.id]
        if (conteo != null && conteo > 0) "${categoriaActiva.nombre} ($conteo)" else categoriaActiva.nombre
    } else {
        null
    }

    val descripcionHud = if (categoriaActiva != null) "Categoría: ${categoriaActiva.nombre}" else "Filtrar por categoría"

    Box(modifier = modifier) {
        PildoraFiltroBase(
            icono = icono,
            texto = texto,
            activo = activo,
            colorAcento = colorBase,
            descripcionHud = descripcionHud,
            alPulsar = {
                alAbrirSelector()
                menuAbierto = true
            },
            alLimpiar = if (activo) { { alSeleccionarCategoria(null) } } else null
        )

        DropdownFiltroCategorias(
            expanded = menuAbierto,
            onDismissRequest = { menuAbierto = false },
            categorias = categorias,
            categoriaSeleccionadaId = categoriaSeleccionadaId,
            conteoPorCategoria = conteoPorCategoria,
            totalEntradas = totalEntradas,
            alSeleccionarCategoria = alSeleccionarCategoria,
            alGestionarCategorias = alGestionarCategorias
        )
    }
}
