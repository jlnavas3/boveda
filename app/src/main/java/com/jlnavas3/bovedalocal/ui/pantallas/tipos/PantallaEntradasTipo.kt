/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import com.jlnavas3.bovedalocal.ui.scroll.rememberLazyListStatePersistente
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraSuperiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla contenedor principal para visualizar y gestionar entradas por [TipoEntrada].
 */
@Composable
fun PantallaEntradasTipo(tipo: TipoEntrada, vm: VaultViewModel) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()
    val todasLasEntradas = (estadoBoveda as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val infoTipo = remember(tipo) { obtenerInfoTipoEntrada(tipo) }
    val todasLasEntradasDelTipo = remember(todasLasEntradas, tipo) {
        todasLasEntradas.filter { it.tipo == tipo }
    }
    val lazyListState = rememberLazyListStatePersistente("tipo_${tipo.name}", vm.gestorScroll)
    val conSeparador by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 0 }
    }
    val espaciadoFilas = calcularEspaciadoFilas(ajustes.densidadLista)

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }
    var criterioOrdenacion by remember { mutableStateOf(CriterioOrdenacion.NOMBRE_AZ) }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }

    var seleccionados by remember { mutableStateOf(setOf<String>()) }
    val modoSeleccion = seleccionados.isNotEmpty()

    BackHandler(enabled = modoSeleccion) {
        seleccionados = emptySet()
    }

    val entradasFiltradas = remember(todasLasEntradasDelTipo, textoBusqueda, soloFavoritos, criterioOrdenacion) {
        filtrarYOrdenarEntradasTipo(todasLasEntradasDelTipo, textoBusqueda, soloFavoritos, criterioOrdenacion)
    }

    val alCrearEntrada = {
        haptica.tic()
        vm.ir(Pantalla.Editar(id = null, tipoInicial = tipo))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
            .cerrarTecladoAlTocarFuera()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (modoSeleccion) {
                BarraSuperiorSeleccion(
                    cantidad = seleccionados.size,
                    todoSeleccionado = entradasFiltradas.isNotEmpty() && seleccionados.size == entradasFiltradas.size,
                    alCancelar = {
                        haptica.tic()
                        seleccionados = emptySet()
                    },
                    alSeleccionarTodo = {
                        haptica.tic()
                        seleccionados = entradasFiltradas.map { it.id }.toSet()
                    },
                    alDeseleccionarTodo = {
                        haptica.tic()
                        seleccionados = emptySet()
                    }
                )
            } else {
                BarraSuperiorEntradasTipo(
                    infoTipo = infoTipo,
                    cantidadEntradas = entradasFiltradas.size,
                    conSeparador = conSeparador,
                    busquedaVisible = busquedaVisible,
                    textoBusqueda = textoBusqueda,
                    soloFavoritos = soloFavoritos,
                    criterioOrdenacion = criterioOrdenacion,
                    menuOpcionesDesplegado = menuOpcionesDesplegado,
                    haptica = haptica,
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alVolver = { vm.volverAtras() },
                    alAlternarBusqueda = {
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible) textoBusqueda = ""
                    },
                    alCambiarTextoBusqueda = { textoBusqueda = it },
                    alCerrarBusqueda = {
                        busquedaVisible = false
                        textoBusqueda = ""
                    },
                    alAbrirMenu = { menuOpcionesDesplegado = true },
                    alCerrarMenu = { menuOpcionesDesplegado = false },
                    alAbrirOrdenacion = {
                        menuOpcionesDesplegado = false
                        mostrarDialogoOrdenacion = true
                    },
                    alAlternarFavoritos = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        soloFavoritos = !soloFavoritos
                    },
                    alRestablecerFiltros = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        textoBusqueda = ""
                        soloFavoritos = false
                        criterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ
                    }
                )
            }

            ContenidoListaEntradasTipo(
                modifier = Modifier.weight(1f),
                infoTipo = infoTipo,
                lazyListState = lazyListState,
                entradasFiltradas = entradasFiltradas,
                todasLasEntradas = todasLasEntradasDelTipo,
                espaciadoFilas = espaciadoFilas,
                haptica = haptica,
                modoSeleccion = modoSeleccion,
                seleccionados = seleccionados,
                ajustes = ajustes,
                vm = vm,
                alCrearEntrada = alCrearEntrada,
                alPulsarLargo = { id ->
                    haptica.toque()
                    seleccionados = seleccionados + id
                },
                alAlternarSeleccion = { id ->
                    seleccionados = if (seleccionados.contains(id)) seleccionados - id else seleccionados + id
                },
                alPulsarEntrada = { entrada ->
                    vm.ir(Pantalla.Detalle(entrada.id, idsContexto = entradasFiltradas.map { it.id }))
                },
                alAlternarFavorito = { id ->
                    haptica.tic()
                    vm.alternarFavorito(id)
                }
            )

            if (modoSeleccion) {
                BarraAccionesSeleccionTipo(
                    seleccionados = seleccionados,
                    todasLasEntradas = todasLasEntradasDelTipo,
                    haptica = haptica,
                    alAlternarFavoritos = { ids ->
                        vm.alternarFavoritosVarias(ids)
                        seleccionados = emptySet()
                    },
                    alComparar = { primera, todos ->
                        vm.ir(Pantalla.Detalle(id = primera, idsContexto = todos, modoComparacion = true))
                    },
                    alRespaldar = { idsParam ->
                        seleccionados = emptySet()
                        vm.ir(Pantalla.ExportarSelectivo("ids:$idsParam"))
                    },
                    alTransferirCxf = { _ ->
                        seleccionados = emptySet()
                    },
                    alAbrirRenombrar = { _ -> },
                    alAbrirBorrado = {
                        vm.eliminarVarias(seleccionados)
                        seleccionados = emptySet()
                    }
                )
            }
        }

        if (!modoSeleccion) {
            FabCrearEntradaTipo(
                infoTipo = infoTipo,
                alCrear = alCrearEntrada,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }

    if (mostrarDialogoOrdenacion) {
        DialogoOrdenacionLista(
            criterioActual = criterioOrdenacion,
            alSeleccionarCriterio = { criterio ->
                criterioOrdenacion = criterio
                mostrarDialogoOrdenacion = false
            },
            alCerrar = { mostrarDialogoOrdenacion = false }
        )
    }
}
