/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

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
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.Identidad
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
import com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada

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
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import androidx.compose.ui.draw.clip

/**
 * Pantalla principal para la administración de [Identidad] (CRUD completo).
 */
@Composable
fun PantallaGestionIdentidades(
    vm: VaultViewModel,
    idDestinoInicial: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val estado by vm.estado.collectAsStateWithLifecycle()
    val identidades = (estado as? EstadoBoveda.Desbloqueada)?.identidades ?: emptyList()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    // Conteo en vivo de cuentas asociadas por cada identidad
    val conteosPorIdentidad = remember(entradas, identidades) {
        identidades.associate { iden ->
            iden.id to entradas.count { entrada ->
                resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
            }
        }
    }

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    var ordenarPorCuentas by remember { mutableStateOf(false) }

    val identidadesFiltradas = remember(identidades, textoBusqueda, ordenarPorCuentas, conteosPorIdentidad) {
        val q = textoBusqueda.trim().lowercase()
        val filtradas = if (q.isBlank()) identidades else {
            identidades.filter { iden ->
                iden.nombre.lowercase().contains(q) ||
                    iden.correoPrincipal.lowercase().contains(q) ||
                    iden.correosSecundarios.any { it.lowercase().contains(q) }
            }
        }
        if (ordenarPorCuentas) {
            filtradas.sortedByDescending { conteosPorIdentidad[it.id] ?: 0 }
        } else {
            filtradas.sortedBy { it.nombre.lowercase() }
        }
    }

    var mostrandoDialogoCrear by remember { mutableStateOf(false) }
    var identidadAEditar by remember { mutableStateOf<Identidad?>(null) }
    var identidadAEliminar by remember { mutableStateOf<Identidad?>(null) }

    val listState = rememberLazyListStatePersistente("identidades", vm.gestorScroll)

    CerrarTecladoAlHacerScroll(listState.isScrollInProgress)

    Scaffold(
        topBar = {
            Column {
                BarraSuperiorPantalla(
                    titulo = "Identidades",
                    idEtiqueta = idDestinoInicial ?: "03-LST-IDE",
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
                                .then(if (busquedaVisible || textoBusqueda.isNotBlank()) Modifier.background(ColorAcento.copy(alpha = 0.16f)) else Modifier)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Buscar identidades",
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
                    contentDescription = "Nueva identidad"
                )
            }
        },
        containerColor = ColorAjustesFondo,
        modifier = Modifier.cerrarTecladoAlTocarFuera()
    ) { paddingValues ->
        if (identidadesFiltradas.isEmpty()) {
            EstadoVacioIdentidades(
                sinIdentidadesEnTotal = identidades.isEmpty(),
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
                        texto = "Vinculación inteligente activa. Las cuentas que utilicen el correo principal o alias de cada identidad se agrupan automáticamente sin necesidad de editarlas a mano.",
                        tamano = TamanoCuerpo.PEQUENO,
                        color = TextoSecundario,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                    )
                }

                items(identidadesFiltradas, key = { it.id }) { iden ->
                    FilaGestionIdentidad(
                        identidad = iden,
                        conteoCuentas = conteosPorIdentidad[iden.id] ?: 0,
                        alEditar = {
                            haptica.tic()
                            identidadAEditar = iden
                        },
                        alEliminar = {
                            haptica.tic()
                            identidadAEliminar = iden
                        }
                    )
                }
            }
        }
    }

    if (mostrandoDialogoCrear) {
        DialogoEditarIdentidad(
            identidadAEditar = null,
            alGuardar = { nombre, correoPrincipal, correosSecundarios, colorHex, icono ->
                mostrandoDialogoCrear = false
                vm.crearIdentidad(
                    nombre = nombre,
                    correoPrincipal = correoPrincipal,
                    correosSecundarios = correosSecundarios,
                    colorHex = colorHex,
                    icono = icono
                )
            },
            alDescartar = { mostrandoDialogoCrear = false }
        )
    }

    identidadAEditar?.let { iden ->
        DialogoEditarIdentidad(
            identidadAEditar = iden,
            alGuardar = { nombre, correoPrincipal, correosSecundarios, colorHex, icono ->
                identidadAEditar = null
                vm.actualizarIdentidad(
                    id = iden.id,
                    nombre = nombre,
                    correoPrincipal = correoPrincipal,
                    correosSecundarios = correosSecundarios,
                    colorHex = colorHex,
                    icono = icono
                )
            },
            alDescartar = { identidadAEditar = null }
        )
    }

    identidadAEliminar?.let { iden ->
        val vinculadas = conteosPorIdentidad[iden.id] ?: 0
        DialogoEliminarIdentidad(
            identidad = iden,
            cantidadEntradasVinculadas = vinculadas,
            alConfirmar = {
                identidadAEliminar = null
                vm.eliminarIdentidad(iden.id)
            },
            alDescartar = { identidadAEliminar = null }
        )
    }
}
