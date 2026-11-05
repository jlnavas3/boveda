/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Fila horizontal desplazable de píldoras maestras de filtrado.
 *
 * Se adapta dinámicamente a la configuración del usuario: si un usuario no tiene
 * identidades, categorías o etiquetas configuradas, la fila no consume espacio vertical alguno.
 */
@Composable
fun FilaPildorasFiltros(
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
    val tieneIdentidades = mostrarPildoraIdentidad && identidades.isNotEmpty()
    val tieneCategorias = mostrarPildoraCategoria && categorias.isNotEmpty()
    val hayElementos = totalEntradas > 0 || soloFavoritos || solo2fa || filtroTipo != null || tieneIdentidades || tieneCategorias || mostrarPildoraEtiqueta

    if (!hayElementos) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Píldora Favoritos (solo cuando está activo)
        PildoraFavoritos(
            activo = soloFavoritos,
            alAlternarFavoritos = alAlternarFavoritos
        )

        // 2. Píldora Tipo de Entrada (permanente)
        PildoraTipoEntrada(
            tipo = filtroTipo,
            solo2fa = solo2fa,
            conteoPorTipo = conteoPorTipo,
            conteo2fa = conteo2fa,
            totalEntradas = totalEntradas,
            alSeleccionarTipo = alSeleccionarTipo,
            alSeleccionar2fa = alSeleccionar2fa,
            alLimpiar = alLimpiarTipo
        )

        // 3. Píldora Identidades
        if (tieneIdentidades) {
            PildoraIdentidad(
                identidades = identidades,
                identidadSeleccionadaId = identidadSeleccionadaId,
                conteoPorIdentidad = conteoPorIdentidad,
                conteoSinIdentidad = conteoSinIdentidad,
                alAbrirSelector = alAbrirSelectorIdentidad,
                alSeleccionarIdentidad = alSeleccionarIdentidad,
                alGestionarIdentidades = alGestionarIdentidades,
                totalEntradas = totalEntradas
            )
        }

        // 4. Píldora Categorías
        if (tieneCategorias) {
            PildoraCategoria(
                categorias = categorias,
                categoriaSeleccionadaId = categoriaSeleccionadaId,
                conteoPorCategoria = conteoPorCategoria,
                alAbrirSelector = alAbrirSelectorCategoria,
                alSeleccionarCategoria = alSeleccionarCategoria,
                alGestionarCategorias = alGestionarCategorias,
                totalEntradas = totalEntradas
            )
        }

        // 5. Píldora Etiquetas
        if (mostrarPildoraEtiqueta) {
            PildoraEtiqueta(
                etiquetas = etiquetasDisponibles,
                filtroEtiqueta = filtroEtiqueta,
                conteoPorEtiqueta = conteoPorEtiqueta,
                totalEntradas = totalEntradas,
                alAbrirSelector = alAbrirSelectorEtiqueta,
                alSeleccionarEtiqueta = alSeleccionarEtiqueta
            )
        }
    }
}
