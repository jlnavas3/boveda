/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BotonAccionSeleccionIcono
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.rememberEstadoSeleccionLista
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.BarraSuperiorPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.ContenidoListaPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.DialogoAjustesRetencionPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.DialogoConfirmarBorradoSeleccionadosPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.DialogoConfirmarRestauracionPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.DialogosPapelera
import com.jlnavas3.bovedalocal.ui.scroll.rememberGruposExpandidosPersistentes
import com.jlnavas3.bovedalocal.ui.scroll.rememberLazyListStatePersistente
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorTitulo

@Composable
fun PantallaPapelera(
    vm: VaultViewModel,
    estado: EstadoBoveda,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val papelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera ?: emptyList()
    val entradasActivas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val ahora = remember { System.currentTimeMillis() }
    var confirmarVaciar by remember { mutableStateOf(false) }
    var mostrarConfirmarBorradoSeleccionados by remember { mutableStateOf(false) }
    var mostrarConfirmarRestauracion by remember { mutableStateOf(false) }
    var mostrarDialogoRetencion by remember { mutableStateOf(false) }
    var aBorrarDefinitivo by remember { mutableStateOf<Entrada?>(null) }
    var conflictoRestaurar by remember { mutableStateOf<Entrada?>(null) }
    val estadoLista = rememberLazyListStatePersistente("papelera", vm.gestorScroll)
    var gruposExpandidos by rememberGruposExpandidosPersistentes("papelera", vm.gestorGrupos)
    val espaciadoFilas = calcularEspaciadoFilas(ajustes.densidadLista)

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }

    val estadoSeleccion = rememberEstadoSeleccionLista(papelera, haptica)

    BackHandler(enabled = estadoSeleccion.modoSeleccion || busquedaVisible || textoBusqueda.isNotBlank()) {
        if (estadoSeleccion.modoSeleccion) {
            estadoSeleccion.salirDeSeleccion()
        } else {
            busquedaVisible = false
            textoBusqueda = ""
        }
    }

    val filtradas = remember(papelera, textoBusqueda) {
        if (textoBusqueda.isBlank()) {
            papelera
        } else {
            val query = textoBusqueda.trim().lowercase()
            papelera.filter {
                it.titulo.lowercase().contains(query) ||
                it.usuario.lowercase().contains(query) ||
                it.notas.lowercase().contains(query)
            }
        }
    }

    val ordenadas = remember(filtradas) { filtradas.sortedByDescending { it.eliminadaEn } }
    val idsVisibles = remember(ordenadas) { ordenadas.map { it.id } }
    val todoSeleccionado = idsVisibles.isNotEmpty() && estadoSeleccion.seleccionados.containsAll(idsVisibles)

    val itemsAgrupados = remember(ordenadas, ajustes.agruparPorSitio) {
        construirItemsAgrupadosPorTitulo(
            entradas = ordenadas,
            agrupar = ajustes.agruparPorSitio,
            expandido = { false }
        )
    }

    val alRestaurarEntrada = { entrada: Entrada ->
        val conflicto = entradasActivas.find {
            it.id == entrada.id || (it.titulo.trim().equals(entrada.titulo.trim(), ignoreCase = true) && it.usuario.trim() == entrada.usuario.trim())
        }
        if (conflicto != null) {
            conflictoRestaurar = entrada
        } else {
            vm.restaurarDeLaPapelera(entrada.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPapelera(
            tieneElementos = papelera.isNotEmpty(),
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = {
                if (estadoSeleccion.modoSeleccion) {
                    estadoSeleccion.salirDeSeleccion()
                } else if (busquedaVisible || textoBusqueda.isNotBlank()) {
                    busquedaVisible = false
                    textoBusqueda = ""
                } else {
                    vm.volverAtras()
                }
            },
            alAlternarSeleccionarTodo = {
                estadoSeleccion.alternarSeleccionarTodo(idsVisibles)
            },
            todoSeleccionado = todoSeleccionado,
            modoSeleccion = estadoSeleccion.modoSeleccion,
            cantidadSeleccionados = estadoSeleccion.seleccionados.size,
            busquedaVisible = busquedaVisible,
            textoBusqueda = textoBusqueda,
            alAlternarBusqueda = {
                busquedaVisible = !busquedaVisible
                if (!busquedaVisible) textoBusqueda = ""
            },
            alCambiarTextoBusqueda = { textoBusqueda = it },
            alCerrarBusqueda = {
                busquedaVisible = false
                textoBusqueda = ""
            },
            alIrADisenoLista = { vm.ir(Pantalla.OrganizacionLista("02-APA-DES-GRP")) },
            alIrAAutodestruccion = { vm.ir(Pantalla.AjustesAutodestruccion("01-SEG-DES")) },
            alCambiarRetencion = { mostrarDialogoRetencion = true }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (papelera.isEmpty()) {
                    "Sin elementos en la papelera"
                } else if (textoBusqueda.isNotBlank()) {
                    "${filtradas.size} resultado${if (filtradas.size == 1) "" else "s"}"
                } else if (ajustes.diasRetencionPapelera <= 0) {
                    "${papelera.size} entrada${if (papelera.size == 1) "" else "s"} · Sin expiración automática"
                } else {
                    "${papelera.size} entrada${if (papelera.size == 1) "" else "s"} · Se borran tras ${ajustes.diasRetencionPapelera} días"
                }
            )

            Spacer(Modifier.height(10.dp))

            ContenidoListaPapelera(
                papelera = papelera,
                ordenadas = ordenadas,
                itemsAgrupados = itemsAgrupados,
                gruposExpandidos = gruposExpandidos,
                ajustes = ajustes,
                ahora = ahora,
                espaciadoFilas = espaciadoFilas,
                alAlternarGrupo = { clave ->
                    gruposExpandidos = if (gruposExpandidos.contains(clave)) {
                        gruposExpandidos - clave
                    } else {
                        gruposExpandidos + clave
                    }
                },
                alRestaurar = alRestaurarEntrada,
                alBorrarDefinitivo = { aBorrarDefinitivo = it },
                modifier = Modifier.weight(1f),
                estadoLista = estadoLista,
                seleccionados = estadoSeleccion.seleccionados,
                modoSeleccion = estadoSeleccion.modoSeleccion,
                alAlternarSeleccion = { id -> estadoSeleccion.alternarSeleccion(id) },
                alPulsarLargo = { id -> estadoSeleccion.entrarEnSeleccion(id) },
                alAlternarSeleccionLote = { ids -> estadoSeleccion.alternarSeleccionLote(ids) },
                alEntrarEnSeleccionLote = { ids -> estadoSeleccion.entrarEnSeleccionLote(ids) }
            )
        }

        // Panel inferior contextual: visible ÚNICAMENTE si hay elementos seleccionados
        if (estadoSeleccion.modoSeleccion && estadoSeleccion.seleccionados.isNotEmpty()) {
            val cantSel = estadoSeleccion.seleccionados.size
            BarraAccionesInferiorBoveda {
                BotonAccionSeleccionIcono(
                    icono = Icons.Filled.Unarchive,
                    descripcion = "Restaurar ($cantSel)",
                    habilitado = true,
                    colorIcono = ColorIconosInternos,
                    alPulsar = {
                        mostrarConfirmarRestauracion = true
                    }
                )
                BotonAccionSeleccionIcono(
                    icono = Icons.Filled.Delete,
                    descripcion = "Eliminar definitivamente ($cantSel)",
                    habilitado = true,
                    colorIcono = ColorIconosInternos,
                    alPulsar = {
                        mostrarConfirmarBorradoSeleccionados = true
                    }
                )
            }
        }
    }

    DialogosPapelera(
        confirmarVaciar = confirmarVaciar,
        cantidadPapelera = papelera.size,
        aBorrarDefinitivo = aBorrarDefinitivo,
        conflictoRestaurar = conflictoRestaurar,
        alDescartarVaciar = { confirmarVaciar = false },
        alConfirmarVaciar = {
            confirmarVaciar = false
            estadoSeleccion.salirDeSeleccion()
            vm.vaciarPapelera()
            vm.avisar("Papelera vaciada")
        },
        alDescartarBorrarDefinitivo = { aBorrarDefinitivo = null },
        alConfirmarBorrarDefinitivo = { entrada ->
            val id = entrada.id
            aBorrarDefinitivo = null
            vm.borrarDefinitivamente(id)
            vm.avisar("Entrada destruida")
        },
        alDescartarConflicto = { conflictoRestaurar = null },
        alSustituirConflicto = { entrada ->
            val id = entrada.id
            conflictoRestaurar = null
            vm.restaurarDeLaPapelera(id, sustituir = true)
        },
        alDuplicarConflicto = { entrada ->
            val id = entrada.id
            conflictoRestaurar = null
            vm.restaurarDeLaPapelera(id, sustituir = false)
        }
    )

    DialogoConfirmarBorradoSeleccionadosPapelera(
        abierto = mostrarConfirmarBorradoSeleccionados,
        cantidad = estadoSeleccion.seleccionados.size,
        alConfirmar = {
            val aBorrar = estadoSeleccion.seleccionados
            mostrarConfirmarBorradoSeleccionados = false
            estadoSeleccion.salirDeSeleccion()
            vm.borrarDefinitivamenteLote(aBorrar)
        },
        alDescartar = { mostrarConfirmarBorradoSeleccionados = false }
    )

    DialogoConfirmarRestauracionPapelera(
        abierto = mostrarConfirmarRestauracion,
        cantidad = if (estadoSeleccion.seleccionados.isNotEmpty()) estadoSeleccion.seleccionados.size else papelera.size,
        esTodaLaPapelera = estadoSeleccion.seleccionados.isEmpty(),
        alConfirmar = {
            val seleccionados = estadoSeleccion.seleccionados
            mostrarConfirmarRestauracion = false
            if (seleccionados.isNotEmpty()) {
                estadoSeleccion.salirDeSeleccion()
                vm.restaurarLoteDeLaPapelera(seleccionados)
            } else {
                estadoSeleccion.salirDeSeleccion()
                vm.restaurarTodasDeLaPapelera()
            }
        },
        alDescartar = { mostrarConfirmarRestauracion = false }
    )

    if (mostrarDialogoRetencion) {
        DialogoAjustesRetencionPapelera(
            diasActuales = ajustes.diasRetencionPapelera,
            alDescartar = { mostrarDialogoRetencion = false },
            alSeleccionarDias = { dias ->
                vm.ajustarDiasRetencionPapelera(dias)
                mostrarDialogoRetencion = false
                val desc = if (dias <= 0) "Sin expiración automática" else "Retención: $dias días"
                vm.avisar(desc)
            }
        )
    }
}
