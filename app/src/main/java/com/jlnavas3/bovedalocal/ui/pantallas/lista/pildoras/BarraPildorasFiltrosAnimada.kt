/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Envoltorio animado para la barra de píldoras de filtrado.
 * Permite el auto-ocultamiento suave detrás de la barra de búsqueda al hacer scroll.
 */
@Composable
fun BarraPildorasFiltrosAnimada(
    visible: Boolean,
    soloFavoritos: Boolean,
    filtroTipo: TipoEntrada?,
    solo2fa: Boolean = false,
    alAlternarFavoritos: () -> Unit,
    alLimpiarTipo: () -> Unit,
    alSeleccionarTipo: (TipoEntrada?) -> Unit = { alLimpiarTipo() },
    alSeleccionar2fa: () -> Unit = {},
    conteoPorTipo: Map<TipoEntrada, Int> = emptyMap(),
    conteo2fa: Int = 0,
    mostrarPildoraIdentidad: Boolean,
    identidades: List<Identidad>,
    identidadSeleccionadaId: String?,
    conteoPorIdentidad: Map<String, Int>,
    conteoSinIdentidad: Int,
    alAbrirSelectorIdentidad: () -> Unit = {},
    alSeleccionarIdentidad: (String?) -> Unit,
    alGestionarIdentidades: () -> Unit = {},
    mostrarPildoraCategoria: Boolean,
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    conteoPorCategoria: Map<String, Int>,
    alAbrirSelectorCategoria: () -> Unit = {},
    alSeleccionarCategoria: (String?) -> Unit,
    alGestionarCategorias: () -> Unit = {},
    mostrarPildoraEtiqueta: Boolean,
    filtroEtiqueta: String?,
    etiquetasDisponibles: List<String> = emptyList(),
    conteoPorEtiqueta: Map<String, Int> = emptyMap(),
    totalEntradas: Int = 0,
    alAbrirSelectorEtiqueta: () -> Unit = {},
    alSeleccionarEtiqueta: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) + fadeIn(),
        exit = shrinkVertically(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) + fadeOut(),
        modifier = modifier
    ) {
        FilaPildorasFiltros(
            soloFavoritos = soloFavoritos,
            filtroTipo = filtroTipo,
            solo2fa = solo2fa,
            alAlternarFavoritos = alAlternarFavoritos,
            alLimpiarTipo = alLimpiarTipo,
            alSeleccionarTipo = alSeleccionarTipo,
            alSeleccionar2fa = alSeleccionar2fa,
            conteoPorTipo = conteoPorTipo,
            conteo2fa = conteo2fa,
            mostrarPildoraIdentidad = mostrarPildoraIdentidad,
            identidades = identidades,
            identidadSeleccionadaId = identidadSeleccionadaId,
            conteoPorIdentidad = conteoPorIdentidad,
            conteoSinIdentidad = conteoSinIdentidad,
            alAbrirSelectorIdentidad = alAbrirSelectorIdentidad,
            alSeleccionarIdentidad = alSeleccionarIdentidad,
            alGestionarIdentidades = alGestionarIdentidades,
            mostrarPildoraCategoria = mostrarPildoraCategoria,
            categorias = categorias,
            categoriaSeleccionadaId = categoriaSeleccionadaId,
            conteoPorCategoria = conteoPorCategoria,
            alAbrirSelectorCategoria = alAbrirSelectorCategoria,
            alSeleccionarCategoria = alSeleccionarCategoria,
            alGestionarCategorias = alGestionarCategorias,
            mostrarPildoraEtiqueta = mostrarPildoraEtiqueta,
            filtroEtiqueta = filtroEtiqueta,
            etiquetasDisponibles = etiquetasDisponibles,
            conteoPorEtiqueta = conteoPorEtiqueta,
            totalEntradas = totalEntradas,
            alAbrirSelectorEtiqueta = alAbrirSelectorEtiqueta,
            alSeleccionarEtiqueta = alSeleccionarEtiqueta
        )
    }
}
