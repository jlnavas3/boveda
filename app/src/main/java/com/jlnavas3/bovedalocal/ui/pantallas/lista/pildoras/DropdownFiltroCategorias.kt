/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.IconosCategorias
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Menú desplegable contextual anclado al chip de filtro de Categorías.
 */
@Composable
fun DropdownFiltroCategorias(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    conteoPorCategoria: Map<String, Int>,
    totalEntradas: Int,
    alSeleccionarCategoria: (String?) -> Unit,
    alGestionarCategorias: () -> Unit,
    modifier: Modifier = Modifier
) {
    MenuDesplegableBoveda(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.widthIn(min = 210.dp, max = 280.dp)
    ) {
        Column(
            modifier = Modifier
                .heightIn(max = 300.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Todas
            val esTodas = categoriaSeleccionadaId == null
            val textoTodas = if (totalEntradas > 0) "Todas ($totalEntradas)" else "Todas"
            ElementoMenuCompacto(
                texto = textoTodas,
                icono = Icons.Filled.Folder,
                colorIcono = if (esTodas) ColorAcento else ColorIconosInternos,
                colorTexto = if (esTodas) ColorAcento else TextoPrincipal,
                iconoFinal = if (esTodas) Icons.Filled.Check else null,
                colorIconoFinal = ColorAcento,
                onClick = {
                    alSeleccionarCategoria(null)
                    onDismissRequest()
                }
            )

            // 2. Lista de Categorías
            categorias.forEach { cat ->
                val esActiva = categoriaSeleccionadaId == cat.id
                val iconoCat = IconosCategorias.obtenerIcono(cat.icono)
                val conteo = conteoPorCategoria[cat.id] ?: 0
                val textoCat = if (conteo > 0) "${cat.nombre} ($conteo)" else cat.nombre

                ElementoMenuCompacto(
                    texto = textoCat,
                    icono = iconoCat,
                    colorIcono = if (esActiva) ColorAcento else ColorIconosInternos,
                    colorTexto = if (esActiva) ColorAcento else TextoPrincipal,
                    iconoFinal = if (esActiva) Icons.Filled.Check else null,
                    colorIconoFinal = ColorAcento,
                    onClick = {
                        alSeleccionarCategoria(cat.id)
                        onDismissRequest()
                    }
                )
            }

            // 3. Separador y Gestión
            SeparadorOpcionMenu()
            ElementoMenuCompacto(
                texto = "Gestionar categorías...",
                icono = Icons.Filled.Tune,
                colorIcono = ColorIconosInternos,
                colorTexto = TextoSecundario,
                onClick = {
                    onDismissRequest()
                    alGestionarCategorias()
                }
            )
        }
    }
}
