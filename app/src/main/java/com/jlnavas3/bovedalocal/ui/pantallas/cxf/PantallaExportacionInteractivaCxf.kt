/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes

/**
 * Pantalla interactiva que se presenta al usuario cuando un gestor externo (Google, Dashlane, etc.)
 * solicita importar credenciales de Bóveda Local mediante Android Credential Transfer.
 * Incluye cabecera limpia con contador y controles de selección, barra inferior fija de acciones,
 * índice alfabético lateral, soporte para teclado (imePadding) y respeto a los tokens de geometría y tipografía.
 */
@Composable
fun PantallaExportacionInteractivaCxf(
    gestorReceptor: String,
    entradasDisponibles: List<Entrada>,
    idsIniciales: Set<String>,
    alConfirmar: (List<Entrada>) -> Unit,
    alCancelar: () -> Unit,
    modifier: Modifier = Modifier,
    mostrarIdAjustes: Boolean = false,
    ajustes: com.jlnavas3.bovedalocal.data.AjustesApp = com.jlnavas3.bovedalocal.data.AjustesApp()
) {
    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }

    val idsSeleccionadas = remember(entradasDisponibles, idsIniciales) {
        mutableStateListOf<String>().apply {
            if (idsIniciales.isNotEmpty()) {
                addAll(idsIniciales.filter { id -> entradasDisponibles.any { it.id == id } })
            } else {
                addAll(entradasDisponibles.map { it.id })
            }
        }
    }

    val entradasVisibles = remember(entradasDisponibles, textoBusqueda) {
        if (textoBusqueda.isBlank()) entradasDisponibles
        else {
            val q = textoBusqueda.trim().lowercase()
            entradasDisponibles.filter {
                it.titulo.lowercase().contains(q) ||
                it.usuario.lowercase().contains(q) ||
                it.urls.any { u -> u.lowercase().contains(q) } ||
                it.notas.lowercase().contains(q)
            }
        }
    }

    val seleccionadasCountTotal = idsSeleccionadas.size

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding(),
        containerColor = ColorAjustesFondo,
        topBar = {
            BarraSuperiorTransferirCxf(
                gestorReceptor = gestorReceptor,
                totalDisponibles = entradasDisponibles.size,
                seleccionadasCount = seleccionadasCountTotal,
                busquedaVisible = busquedaVisible,
                textoBusqueda = textoBusqueda,
                mostrarId = mostrarIdAjustes,
                alVolver = alCancelar,
                alAlternarBusqueda = {
                    busquedaVisible = !busquedaVisible
                    if (!busquedaVisible) textoBusqueda = ""
                },
                alCambiarTextoBusqueda = { textoBusqueda = it },
                alCerrarBusqueda = {
                    textoBusqueda = ""
                    busquedaVisible = false
                },
                alSeleccionarTodas = {
                    entradasVisibles.forEach {
                        if (!idsSeleccionadas.contains(it.id)) idsSeleccionadas.add(it.id)
                    }
                },
                alDeseleccionarTodas = {
                    if (textoBusqueda.isBlank()) {
                        idsSeleccionadas.clear()
                    } else {
                        entradasVisibles.forEach {
                            idsSeleccionadas.remove(it.id)
                        }
                    }
                }
            )
        },
        bottomBar = {
            BarraAccionesTransferirCxf(
                seleccionadasCount = seleccionadasCountTotal,
                alTransferir = {
                    val seleccionadas = entradasDisponibles.filter { idsSeleccionadas.contains(it.id) }
                    alConfirmar(seleccionadas)
                }
            )
        }
    ) { paddingValues ->
        val estadoLista = rememberLazyListState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = estadoLista,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 14.dp, end = 36.dp),
                verticalArrangement = Arrangement.spacedBy(EspaciadoComponentes.coerceAtLeast(8.dp))
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(entradasVisibles, key = { it.id }) { entrada ->
                    val estaSeleccionada = idsSeleccionadas.contains(entrada.id)
                    FilaSeleccionCredencialCxf(
                        entrada = entrada,
                        seleccionada = estaSeleccionada,
                        alAlternar = { activo ->
                            if (activo) {
                                if (!idsSeleccionadas.contains(entrada.id)) idsSeleccionadas.add(entrada.id)
                            } else {
                                idsSeleccionadas.remove(entrada.id)
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (entradasVisibles.size >= 5 && textoBusqueda.isBlank()) {
                IndiceAlfabeticoCxf(
                    entradas = entradasVisibles,
                    estadoLista = estadoLista,
                    ajustes = ajustes,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 52.dp, bottom = 64.dp)
                )
            }
        }
    }
}

