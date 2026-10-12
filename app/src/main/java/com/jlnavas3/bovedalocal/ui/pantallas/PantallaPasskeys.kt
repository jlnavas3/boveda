/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.activity.compose.BackHandler
import com.jlnavas3.bovedalocal.data.Entrada
import kotlinx.coroutines.launch
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraSuperiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.BarraAccionesSeleccionPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.BarraSuperiorPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.ContenidoListaPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.DialogosPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.filtrarYOrdenarPasskeys
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.Haptica
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PantallaPasskeys(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val todasLasPasskeys = remember(vm.repositorio.entradas()) { vm.repositorio.passkeys() }
    val formato = remember { SimpleDateFormat("d MMM yyyy", Locale.forLanguageTag("es-ES")) }
    val scrollState = rememberScrollState()
    val espaciadoFilas = calcularEspaciadoFilas(ajustes.densidadLista)

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }
    var criterioOrdenacion by remember { mutableStateOf(CriterioOrdenacion.NOMBRE_AZ) }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }

    val ambitoCorutina = androidx.compose.runtime.rememberCoroutineScope()
    val actividad = remember(contexto) {
        var c = contexto
        while (c is android.content.ContextWrapper) {
            if (c is android.app.Activity) break
            c = c.baseContext
        }
        c as? android.app.Activity
    }

    val dispararImportacionPasskeys: () -> Unit = {
        val act = actividad
        if (act != null) {
            haptica.tic()
            ambitoCorutina.launch {
                when (val res = com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.importarCredenciales(act)) {
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.Exito -> {
                        haptica.exito()
                        vm.ir(Pantalla.ConfirmarImportacionCxf(res.jsonPayload))
                    }
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.Cancelado -> {
                        // Cancelado por el usuario
                    }
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.SinOpciones -> {
                        haptica.error()
                        vm.mostrarAviso(res.mensaje)
                    }
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.Error -> {
                        haptica.error()
                        vm.mostrarError(res.mensaje)
                    }
                }
            }
        }
    }

    var seleccionados by remember { mutableStateOf(setOf<String>()) }
    val modoSeleccion = seleccionados.isNotEmpty()
    var dialogoRenombrarSeleccion by remember { mutableStateOf(false) }
    var nuevoTituloRenombrar by remember { mutableStateOf("") }
    var dialogoBorrarSeleccion by remember { mutableStateOf(false) }
    var mostrarDialogoExportarCxf by remember { mutableStateOf(false) }
    var entradasParaTransferirCxf by remember { mutableStateOf<List<Entrada>?>(null) }

    BackHandler(enabled = modoSeleccion) {
        seleccionados = emptySet()
    }

    val passkeysFiltradas = remember(todasLasPasskeys, textoBusqueda, soloFavoritos, criterioOrdenacion) {
        filtrarYOrdenarPasskeys(todasLasPasskeys, textoBusqueda, soloFavoritos, criterioOrdenacion)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (modoSeleccion) {
                BarraSuperiorSeleccion(
                    cantidad = seleccionados.size,
                    todoSeleccionado = passkeysFiltradas.isNotEmpty() && seleccionados.size == passkeysFiltradas.size,
                    alCancelar = {
                        haptica.tic()
                        seleccionados = emptySet()
                    },
                    alSeleccionarTodo = {
                        haptica.tic()
                        seleccionados = passkeysFiltradas.map { it.id }.toSet()
                    },
                    alDeseleccionarTodo = {
                        haptica.tic()
                        seleccionados = emptySet()
                    }
                )
            } else {
                BarraSuperiorPasskeys(
                    conSeparador = scrollState.value > 0,
                    busquedaVisible = busquedaVisible,
                    textoBusqueda = textoBusqueda,
                    soloFavoritos = soloFavoritos,
                    criterioOrdenacion = criterioOrdenacion,
                    menuOpcionesDesplegado = menuOpcionesDesplegado,
                    haptica = haptica,
                    idEtiqueta = seccionDestino ?: "03-LST-PAS",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alVolver = {
                        haptica.tic()
                        vm.volverAtras()
                    },
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
                    alIrExportacionSelectiva = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        vm.ir(Pantalla.ExportarSelectivo("passkeys"))
                    },
                    alIrSeguridadBiometria = {
                        vm.ir(Pantalla.Seguridad("01-SEG-BIO"))
                    },
                    alIrCopiaSeguridad = {
                        vm.ir(Pantalla.CopiaSeguridad("05-COP-SEG"))
                    },
                    alImportarPasskeys = dispararImportacionPasskeys,
                    alExportarDirectoCxf = { mostrarDialogoExportarCxf = true },
                    alRestablecerFiltros = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        textoBusqueda = ""
                        soloFavoritos = false
                        criterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ
                    }
                )
            }

            ContenidoListaPasskeys(
                modifier = Modifier.weight(1f),
                scrollState = scrollState,
                passkeysFiltradas = passkeysFiltradas,
                todasLasPasskeys = todasLasPasskeys,
                espaciadoFilas = espaciadoFilas,
                formato = formato,
                haptica = haptica,
                modoSeleccion = modoSeleccion,
                seleccionados = seleccionados,
                ajustes = ajustes,
                alImportarPasskeys = dispararImportacionPasskeys,
                alPulsarLargo = { id ->
                    haptica.toque()
                    seleccionados = seleccionados + id
                },
                alAlternarSeleccion = { id ->
                    seleccionados = if (seleccionados.contains(id)) seleccionados - id else seleccionados + id
                },
                alPulsarEntrada = { entrada ->
                    vm.ir(Pantalla.Detalle(entrada.id, idsContexto = passkeysFiltradas.map { it.id }))
                },
                alAlternarFavorito = { id ->
                    haptica.tic()
                    vm.alternarFavorito(id)
                }
            )

            if (modoSeleccion) {
                BarraAccionesSeleccionPasskeys(
                    seleccionados = seleccionados,
                    todasLasPasskeys = todasLasPasskeys,
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
                    alTransferirCxf = { copia ->
                        seleccionados = emptySet()
                        entradasParaTransferirCxf = copia
                    },
                    alAbrirRenombrar = { titulo ->
                        nuevoTituloRenombrar = titulo
                        dialogoRenombrarSeleccion = true
                    },
                    alAbrirBorrado = {
                        dialogoBorrarSeleccion = true
                    }
                )
            }
        }

        if (!modoSeleccion) {
            val formaFab = RoundedCornerShape(CurvaturaEsquinas)
            FloatingActionButton(
                onClick = dispararImportacionPasskeys,
                containerColor = ColorAcento,
                contentColor = ColorSobreAcento,
                shape = formaFab,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .then(
                        if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && ColorBordeActual != Color.Transparent) {
                            Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                        } else Modifier
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.VpnKey,
                    contentDescription = "Importar llaves de paso"
                )
            }
        }
    }

    DialogosPasskeys(
        mostrarDialogoOrdenacion = mostrarDialogoOrdenacion,
        criterioOrdenacion = criterioOrdenacion,
        alSeleccionarCriterio = { criterio ->
            criterioOrdenacion = criterio
            mostrarDialogoOrdenacion = false
        },
        alCerrarOrdenacion = { mostrarDialogoOrdenacion = false },
        dialogoBorrarSeleccion = dialogoBorrarSeleccion,
        cantidadSeleccionados = seleccionados.size,
        alConfirmarBorrado = {
            vm.eliminarVarias(seleccionados)
            seleccionados = emptySet()
            dialogoBorrarSeleccion = false
        },
        alDescartarBorrado = { dialogoBorrarSeleccion = false },
        dialogoRenombrarSeleccion = dialogoRenombrarSeleccion,
        textoNuevoTitulo = nuevoTituloRenombrar,
        alCambiarTextoRenombrar = { nuevoTituloRenombrar = it },
        alConfirmarRenombrado = {
            vm.renombrarVarias(seleccionados, nuevoTituloRenombrar)
            seleccionados = emptySet()
            dialogoRenombrarSeleccion = false
        },
        alDescartarRenombrado = { dialogoRenombrarSeleccion = false },
        entradasParaTransferirCxf = entradasParaTransferirCxf,
        alCerrarTransferirCxf = { entradasParaTransferirCxf = null },
        mostrarDialogoExportarCxf = mostrarDialogoExportarCxf,
        todasLasPasskeys = todasLasPasskeys,
        alCerrarExportarCxf = { mostrarDialogoExportarCxf = false },
        haptica = haptica
    )
}
