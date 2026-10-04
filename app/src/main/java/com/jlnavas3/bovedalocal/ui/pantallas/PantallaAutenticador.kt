package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraSuperiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.BarraAccionesSeleccionAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.BarraSuperiorAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.ColumnaAccionesFlotantesAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.DialogosAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.ListaCuentasTotp
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.SeccionBusquedaYFiltrosAutenticador
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

/**
 * Pantalla del autenticador TOTP (códigos temporales de 2 pasos).
 */
@Composable
fun PantallaAutenticador(vm: VaultViewModel, estado: EstadoBoveda) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val conTotp = vm.entradasConTotp(entradas)

    var ahora by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
    LaunchedEffect(Unit) {
        while (true) {
            ahora = System.currentTimeMillis() / 1000
            delay(500)
        }
    }

    val scrollState = rememberScrollState()

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }
    var criterioOrdenacion by remember { mutableStateOf(CriterioOrdenacion.NOMBRE_AZ) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }
    var dialogoComoFunciona by remember { mutableStateOf(false) }

    var seleccionados by remember { mutableStateOf(setOf<String>()) }
    val modoSeleccion = seleccionados.isNotEmpty()
    var dialogoRenombrarSeleccion by remember { mutableStateOf(false) }
    var nuevoTituloRenombrar by remember { mutableStateOf("") }
    var dialogoBorrarSeleccion by remember { mutableStateOf(false) }

    BackHandler(enabled = modoSeleccion) {
        seleccionados = emptySet()
    }

    val totpFiltrados = remember(conTotp, textoBusqueda, soloFavoritos, criterioOrdenacion) {
        val q = textoBusqueda.trim().lowercase()
        conTotp
            .filter { entrada ->
                val coincideTexto = if (q.isBlank()) true else {
                    entrada.titulo.lowercase().contains(q) ||
                    entrada.totpEmisor.lowercase().contains(q) ||
                    entrada.usuario.lowercase().contains(q)
                }
                val coincideFavorito = if (soloFavoritos) entrada.favorito else true
                coincideTexto && coincideFavorito
            }
            .sortedWith { a, b ->
                val nombreA = a.totpEmisor.ifBlank { a.titulo }
                val nombreB = b.totpEmisor.ifBlank { b.titulo }
                when (criterioOrdenacion) {
                    CriterioOrdenacion.NOMBRE_AZ -> nombreA.compareTo(nombreB, ignoreCase = true)
                    CriterioOrdenacion.NOMBRE_ZA -> nombreB.compareTo(nombreA, ignoreCase = true)
                    CriterioOrdenacion.MODIFICACION_RECIENTE -> b.modificadaEn.compareTo(a.modificadaEn)
                    CriterioOrdenacion.ANTIGUEDAD -> a.creadaEn.compareTo(b.creadaEn)
                    CriterioOrdenacion.CREACION_RECIENTE -> b.creadaEn.compareTo(a.creadaEn)
                    CriterioOrdenacion.USO_RECIENTE -> b.ultimoUsoEn.compareTo(a.ultimoUsoEn)
                    CriterioOrdenacion.IGNORADAS -> b.ignoradaEnSalud.compareTo(a.ignoradaEnSalud)
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            if (modoSeleccion) {
                BarraSuperiorSeleccion(
                    cantidad = seleccionados.size,
                    todoSeleccionado = totpFiltrados.isNotEmpty() && seleccionados.size == totpFiltrados.size,
                    alCancelar = {
                        haptica.tic()
                        seleccionados = emptySet()
                    },
                    alSeleccionarTodo = {
                        haptica.tic()
                        seleccionados = totpFiltrados.map { it.id }.toSet()
                    },
                    alDeseleccionarTodo = {
                        haptica.tic()
                        seleccionados = emptySet()
                    }
                )
            } else {
                BarraSuperiorAutenticador(
                    busquedaVisible = busquedaVisible,
                    textoBusqueda = textoBusqueda,
                    soloFavoritos = soloFavoritos,
                    criterioOrdenacion = criterioOrdenacion,
                    conSeparador = scrollState.value > 0,
                    haptica = haptica,
                    idEtiqueta = "04-HER-2FA",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alVolver = { vm.volverAtras() },
                    alAlternarBusqueda = {
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible) textoBusqueda = ""
                    },
                    alSolicitarOrdenacion = { mostrarDialogoOrdenacion = true },
                    alAlternarFavoritos = { soloFavoritos = !soloFavoritos },
                    alExportarSelectivo = { vm.ir(Pantalla.ExportarSelectivo("2fa")) },
                    alImportarGoogleAuthenticator = { vm.ir(Pantalla.CamaraQr()) },
                    alMostrarComoFunciona = { dialogoComoFunciona = true },
                    alIrAjustesAutenticador = { vm.ir(Pantalla.AjustesAutenticador("04-HER-AUT")) },
                    alIrAjustesWidgetTotp = { vm.ir(Pantalla.WidgetTotpAjustes("04-HER-WGT-TOT")) },
                    alRestablecerFiltros = {
                        soloFavoritos = false
                        criterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ
                        textoBusqueda = ""
                        busquedaVisible = false
                    }
                )

                SeccionBusquedaYFiltrosAutenticador(
                    busquedaVisible = busquedaVisible,
                    textoBusqueda = textoBusqueda,
                    soloFavoritos = soloFavoritos,
                    alCambiarTextoBusqueda = { textoBusqueda = it },
                    alCerrarBusqueda = {
                        busquedaVisible = false
                        textoBusqueda = ""
                    },
                    alLimpiarFavoritos = { soloFavoritos = false }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                DescripcionPantalla(subtitulo = "Códigos de verificación en dos pasos calculados en el dispositivo")
                Spacer(Modifier.height(12.dp))

                ListaCuentasTotp(
                    totpFiltrados = totpFiltrados,
                    totalTotp = conTotp.size,
                    ahora = ahora,
                    separarDigitos = ajustes.totpSepararDigitos,
                    haptica = haptica,
                    ajustes = ajustes,
                    seleccionActiva = modoSeleccion,
                    seleccionados = seleccionados,
                    alPulsarLargo = { id ->
                        haptica.toque()
                        seleccionados = seleccionados + id
                    },
                    alAlternarSeleccion = { id ->
                        seleccionados = if (seleccionados.contains(id)) {
                            seleccionados - id
                        } else {
                            seleccionados + id
                        }
                    },
                    alCopiarCodigo = { codigo ->
                        vm.copiar("Código de verificación", codigo, true)
                    },
                    alAlternarFavorito = { id ->
                        vm.alternarFavorito(id)
                    },
                    mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                    espaciadoFilas = calcularEspaciadoFilas(ajustes.densidadLista),
                    alVerDetalle = { id ->
                        vm.ir(Pantalla.Detalle(id, idsContexto = totpFiltrados.map { it.id }))
                    }
                )

                Spacer(Modifier.height(110.dp))
            }
        }

        if (modoSeleccion) {
            BarraAccionesSeleccionAutenticador(
                seleccionados = seleccionados,
                conTotp = conTotp,
                haptica = haptica,
                alAlternarFavoritos = { ids ->
                    vm.alternarFavoritosVarias(ids)
                    seleccionados = emptySet()
                },
                alComparar = { primera, todos ->
                    vm.ir(
                        Pantalla.Detalle(
                            id = primera,
                            idsContexto = todos,
                            modoComparacion = true
                        )
                    )
                },
                alRespaldar = { idsParam ->
                    seleccionados = emptySet()
                    vm.ir(Pantalla.ExportarSelectivo("ids:$idsParam"))
                },
                alRenombrar = { titulo ->
                    nuevoTituloRenombrar = titulo
                    dialogoRenombrarSeleccion = true
                },
                alBorrar = { dialogoBorrarSeleccion = true },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        } else {
            ColumnaAccionesFlotantesAutenticador(
                haptica = haptica,
                alIngresarManual = { vm.ir(Pantalla.Escaner(soloManual = true)) },
                alEscanearQr = { vm.ir(Pantalla.CamaraQr()) },
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }

    DialogosAutenticador(
        mostrarDialogoOrdenacion = mostrarDialogoOrdenacion,
        criterioOrdenacion = criterioOrdenacion,
        alSeleccionarCriterio = { crit ->
            criterioOrdenacion = crit
        },
        alCerrarOrdenacion = { mostrarDialogoOrdenacion = false },
        dialogoComoFunciona = dialogoComoFunciona,
        alDescartarComoFunciona = { dialogoComoFunciona = false },
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
        haptica = haptica
    )
}
