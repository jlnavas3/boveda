/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.scroll.rememberLazyListStatePersistente
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import com.jlnavas3.bovedalocal.ui.componentes.CerrarTecladoAlHacerScroll
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import androidx.compose.ui.draw.clip

/**
 * Pantalla principal para la administracion de [Categoria] (CRUD completo).
 */
@Composable
fun PantallaGestionCategorias(
    vm: VaultViewModel,
    idDestinoInicial: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val estado by vm.estado.collectAsStateWithLifecycle()
    val categorias = (estado as? EstadoBoveda.Desbloqueada)?.categorias ?: emptyList()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    val conteosPorCategoria = remember(entradas, categorias) {
        categorias.associate { col ->
            col.id to entradas.count { it.categorias.contains(col.id) }
        }
    }

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    var ordenarPorCuentas by remember { mutableStateOf(false) }

    val categoriasFiltradas = remember(categorias, textoBusqueda, ordenarPorCuentas, conteosPorCategoria) {
        val q = textoBusqueda.trim().lowercase()
        val filtradas = if (q.isBlank()) categorias else {
            categorias.filter { col ->
                col.nombre.lowercase().contains(q)
            }
        }
        if (ordenarPorCuentas) {
            filtradas.sortedByDescending { conteosPorCategoria[it.id] ?: 0 }
        } else {
            filtradas.sortedBy { it.nombre.lowercase() }
        }
    }

    var mostrandoDialogoCrear by remember { mutableStateOf(false) }
    var categoriaAEditar by remember { mutableStateOf<Categoria?>(null) }
    var categoriaAEliminar by remember { mutableStateOf<Categoria?>(null) }

    val listState = rememberLazyListStatePersistente("categorias", vm.gestorScroll)

    CerrarTecladoAlHacerScroll(listState.isScrollInProgress)

    Scaffold(
        topBar = {
            Column {
                BarraSuperiorPantalla(
                    titulo = "Categorías",
                    idEtiqueta = idDestinoInicial ?: "03-LST-CAT",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alVolver = { vm.volverAtras() },
                    conSeparador = listState.firstVisibleItemScrollOffset > 0,
                    colorFondo = ColorAjustesFondo,
                    acciones = {
                        IconButton(
                            onClick = {
                                haptica.tic()
                                busquedaVisible = !busquedaVisible
                                if (!busquedaVisible) textoBusqueda = ""
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .then(
                                    if (busquedaVisible || textoBusqueda.isNotBlank()) {
                                        Modifier.background(ColorAcento.copy(alpha = 0.16f))
                                    } else Modifier
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Buscar categorías",
                                tint = if (busquedaVisible || textoBusqueda.isNotBlank()) ColorAcento else ColorIconosInternos,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Box {
                            IconButton(
                                onClick = {
                                    haptica.tic()
                                    menuOpcionesDesplegado = true
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = "Más opciones",
                                    tint = ColorIconosInternos,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            MenuDesplegableBoveda(
                                expanded = menuOpcionesDesplegado,
                                onDismissRequest = { menuOpcionesDesplegado = false },
                                modifier = Modifier.widthIn(min = 200.dp, max = 260.dp)
                            ) {
                                ElementoMenuCompacto(
                                    texto = if (ordenarPorCuentas) "Ordenar por nombre (A-Z)" else "Ordenar por cantidad",
                                    icono = if (ordenarPorCuentas) Icons.AutoMirrored.Filled.Sort else Icons.Filled.Numbers,
                                    colorIcono = ColorAcento,
                                    onClick = {
                                        menuOpcionesDesplegado = false
                                        haptica.tic()
                                        ordenarPorCuentas = !ordenarPorCuentas
                                    }
                                )
                            }
                        }
                    }
                )

                AnimatedVisibility(
                    visible = busquedaVisible || textoBusqueda.isNotBlank(),
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        BarraBusquedaAnimada(
                            valor = textoBusqueda,
                            alCambiar = { textoBusqueda = it },
                            alCerrar = {
                                busquedaVisible = false
                                textoBusqueda = ""
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    haptica.tic()
                    mostrandoDialogoCrear = true
                },
                containerColor = ColorAcento,
                contentColor = ColorSobreAcento
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nueva categoría"
                )
            }
        },
        containerColor = ColorAjustesFondo,
        modifier = Modifier.cerrarTecladoAlTocarFuera()
    ) { paddingValues ->
        if (categoriasFiltradas.isEmpty()) {
            EstadoVacioCategorias(
                sinCategoriasEnTotal = categorias.isEmpty(),
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .cerrarTecladoAlTocarFuera(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    TextoCuerpo(
                        texto = "Las categorías te permiten agrupar credenciales por área o temática (ej. Finanzas, Trabajo, Streaming). Puedes asignar varias categorías a una sola entrada.",
                        tamano = TamanoCuerpo.PEQUENO,
                        color = TextoSecundario,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                    )
                }

                items(categoriasFiltradas, key = { it.id }) { cat ->
                    FilaGestionCategoria(
                        categoria = cat,
                        conteoCuentas = conteosPorCategoria[cat.id] ?: 0,
                        alEditar = {
                            haptica.tic()
                            categoriaAEditar = cat
                        },
                        alEliminar = {
                            haptica.tic()
                            categoriaAEliminar = cat
                        }
                    )
                }
            }
        }
    }

    if (mostrandoDialogoCrear) {
        DialogoEditarCategoria(
            categoriaAEditar = null,
            alGuardar = { nombre, icono, colorHex ->
                mostrandoDialogoCrear = false
                vm.crearColeccion(
                    nombre = nombre,
                    icono = icono,
                    colorHex = colorHex
                )
            },
            alDescartar = { mostrandoDialogoCrear = false }
        )
    }

    categoriaAEditar?.let { cat ->
        DialogoEditarCategoria(
            categoriaAEditar = cat,
            alGuardar = { nombre, icono, colorHex ->
                categoriaAEditar = null
                vm.actualizarColeccion(
                    id = cat.id,
                    nombre = nombre,
                    icono = icono,
                    colorHex = colorHex
                )
            },
            alDescartar = { categoriaAEditar = null }
        )
    }

    categoriaAEliminar?.let { cat ->
        val vinculadas = conteosPorCategoria[cat.id] ?: 0
        DialogoEliminarCategoria(
            categoria = cat,
            cantidadEntradasVinculadas = vinculadas,
            alConfirmar = {
                categoriaAEliminar = null
                vm.eliminarColeccion(cat.id)
            },
            alDescartar = { categoriaAEliminar = null }
        )
    }
}
