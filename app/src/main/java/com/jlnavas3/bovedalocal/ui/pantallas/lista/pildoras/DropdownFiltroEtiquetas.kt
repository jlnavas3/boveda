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
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Layers
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Menú desplegable contextual anclado al chip de filtro de Etiquetas.
 */
@Composable
fun DropdownFiltroEtiquetas(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    etiquetas: List<String>,
    filtroEtiqueta: String?,
    conteoPorEtiqueta: Map<String, Int>,
    totalEntradas: Int,
    alSeleccionarEtiqueta: (String?) -> Unit,
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
            val esTodas = filtroEtiqueta.isNullOrBlank()
            val textoTodas = if (totalEntradas > 0) "Todas ($totalEntradas)" else "Todas"
            ElementoMenuCompacto(
                texto = textoTodas,
                icono = Icons.Filled.Layers,
                colorIcono = if (esTodas) ColorAcento else ColorIconosInternos,
                colorTexto = if (esTodas) ColorAcento else TextoPrincipal,
                iconoFinal = if (esTodas) Icons.Filled.Check else null,
                colorIconoFinal = ColorAcento,
                onClick = {
                    alSeleccionarEtiqueta(null)
                    onDismissRequest()
                }
            )

            // 2. Lista de Etiquetas
            etiquetas.forEach { et ->
                val esActiva = filtroEtiqueta == et
                val normalizada = normalizarEtiqueta(et)
                val conteo = conteoPorEtiqueta[et] ?: 0
                val textoEtiqueta = if (conteo > 0) "$normalizada ($conteo)" else normalizada

                ElementoMenuCompacto(
                    texto = textoEtiqueta,
                    icono = Icons.AutoMirrored.Filled.Label,
                    colorIcono = if (esActiva) ColorAcento else ColorIconosInternos,
                    colorTexto = if (esActiva) ColorAcento else TextoPrincipal,
                    iconoFinal = if (esActiva) Icons.Filled.Check else null,
                    colorIconoFinal = ColorAcento,
                    onClick = {
                        alSeleccionarEtiqueta(et)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}
