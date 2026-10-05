package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia
import com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.lista.CabeceraPrincipalLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.CajonLateralLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.CapaInferiorAccionesLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ContenidoPrincipalLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogosPantallaLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.rememberEstadoDialogosLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.rememberEstadoSeleccionLista
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(vm: VaultViewModel, estado: EstadoBoveda) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val filtro by vm.filtroTipo.collectAsStateWithLifecycle()
    val soloFavoritos by vm.soloFavoritos.collectAsStateWithLifecycle()
    val filtroEtiqueta by vm.filtroEtiqueta.collectAsStateWithLifecycle()
    val criterioOrdenacion by vm.criterioOrdenacion.collectAsStateWithLifecycle()
    val densidad = ajustes.densidadLista
    val densidadAltura = when (densidad) {
        "compacta" -> 48.dp
        "comoda" -> 60.dp
        else -> 74.dp
    }
    val densidadMonograma = when (densidad) {
        "compacta" -> 34
        "comoda" -> 40
        else -> 46
    }
    val espaciadoFilas = calcularEspaciadoFilas(densidad)
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val colecciones = remember(estado) { (estado as? EstadoBoveda.Desbloqueada)?.colecciones ?: emptyList() }
    val identidades = remember(estado) { (estado as? EstadoBoveda.Desbloqueada)?.identidades ?: emptyList() }
    val coleccionSeleccionadaId by vm.filtroColeccion.collectAsStateWithLifecycle()
    val identidadSeleccionadaId by vm.identidadSeleccionadaId.collectAsStateWithLifecycle()

    val conteoPorColeccion = remember(entradas, colecciones) {
        colecciones.associate { col -> col.id to entradas.count { it.colecciones.contains(col.id) } }
    }
    val conteoPorIdentidad = remember(entradas, identidades) {
        identidades.associate { iden ->
            iden.id to entradas.count { entrada ->
                com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
            }
        }
    }
    val conteoSinIdentidad = remember(entradas, identidades) {
        entradas.count { entrada ->
            com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(entrada, identidades) == null
        }
    }

    val visibles = remember(entradas, busqueda, filtro, soloFavoritos, filtroEtiqueta, coleccionSeleccionadaId, identidadSeleccionadaId, criterioOrdenacion, identidades) {
        vm.entradasVisibles(entradas, identidades)
    }
    val etiquetasDisponibles = remember(entradas) { vm.etiquetasUsadas() }

    val abrirDrawerAlVolver by vm.abrirMenuLateralAlVolverALista.collectAsStateWithLifecycle()
    val estadoCajon = rememberDrawerState(
        initialValue = if (vm.abrirMenuLateralAlVolverALista.value) DrawerValue.Open else DrawerValue.Closed
    )
    val ambitoCorutina = rememberCoroutineScope()
    fun abrirMenu() = ambitoCorutina.launch { estadoCajon.open() }
    fun cerrarMenu() = ambitoCorutina.launch { estadoCajon.close() }

    val actividad = remember(contexto) {
        var c = contexto
        while (c is android.content.ContextWrapper) {
            if (c is android.app.Activity) break
            c = c.baseContext
        }
        c as? android.app.Activity
    }

    val dispararImportacionDirectoCxf: () -> Unit = {
        val act = actividad
        if (act != null) {
            haptica.tic()
            ambitoCorutina.launch {
                when (val res = CxfGestorTransferencia.importarCredenciales(act)) {
                    is ResultadoImportacionCxf.Exito -> {
                        haptica.exito()
                        vm.ir(Pantalla.ConfirmarImportacionCxf(res.jsonPayload))
                    }
                    is ResultadoImportacionCxf.Cancelado -> { }
                    is ResultadoImportacionCxf.SinOpciones -> {
                        haptica.error()
                        vm.mostrarAviso(res.mensaje)
                    }
                    is ResultadoImportacionCxf.Error -> {
                        haptica.error()
                        vm.mostrarError(res.mensaje)
                    }
                }
            }
        }
    }

    LaunchedEffect(abrirDrawerAlVolver) {
        if (abrirDrawerAlVolver) {
            if (!estadoCajon.isOpen) {
                try {
                    estadoCajon.snapTo(DrawerValue.Open)
                } catch (_: Exception) {}
                if (!estadoCajon.isOpen) {
                    try {
                        estadoCajon.open()
                    } catch (_: Exception) {}
                }
            }
            delay(150)
            vm.abrirMenuLateralAlVolverALista.value = false
        }
    }

    val estadoSeleccion = rememberEstadoSeleccionLista(entradas, haptica)
    val dialogos = rememberEstadoDialogosLista()
    val visiblesIds = remember(visibles) { visibles.map { it.id } }
    val todoSeleccionado = visiblesIds.isNotEmpty() && estadoSeleccion.seleccionados.containsAll(visiblesIds)
    var busquedaVisible by remember { mutableStateOf(false) }
    var gruposExpandidos by remember { mutableStateOf(setOf<String>()) }

    BackHandler(enabled = estadoCajon.isOpen || estadoSeleccion.modoSeleccion || busqueda.isNotEmpty()) {
        when {
            estadoCajon.isOpen -> cerrarMenu()
            estadoSeleccion.modoSeleccion -> estadoSeleccion.salirDeSeleccion()
            busqueda.isNotEmpty() -> vm.buscar("")
        }
    }

    val totalDuplicadas = remember(entradas) {
        AnalizadorDuplicados.analizar(entradas).sumOf { it.entradasSecundarias.size }
    }

    CajonLateralLista(
        estadoCajon = estadoCajon,
        ajustes = ajustes,
        totalEntradas = entradas.size,
        totalPapelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera?.size ?: 0,
        totalDuplicadas = totalDuplicadas,
        perfilArgon2 = vm.repositorio.perfilArgon2Actual(),
        alIr = { destino -> vm.irDesdeMenuLateral(destino) },
        alBloquear = { cerrarMenu(); haptica.toque(); vm.bloquear() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                CabeceraPrincipalLista(
                    modoSeleccion = estadoSeleccion.modoSeleccion,
                    cantidadSeleccionados = estadoSeleccion.seleccionados.size,
                    todoSeleccionado = todoSeleccionado,
                    alCancelarSeleccion = { estadoSeleccion.salirDeSeleccion() },
                    alSeleccionarTodo = { estadoSeleccion.alternarSeleccionarTodo(visiblesIds) },
                    alDeseleccionarTodo = { estadoSeleccion.salirDeSeleccion() },
                    nombreBoveda = ajustes.nombrePersonalizado,
                    totalEntradas = entradas.size,
                    busquedaVisible = busquedaVisible,
                    busqueda = busqueda,
                    filtro = filtro,
                    soloFavoritos = soloFavoritos,
                    filtroEtiqueta = filtroEtiqueta,
                    criterioOrdenacion = criterioOrdenacion,
                    agruparPorSitio = ajustes.agruparPorSitio,
                    mostrarIndicadoresContenido = ajustes.mostrarIndicadoresContenido,
                    colecciones = colecciones,
                    coleccionSeleccionadaId = coleccionSeleccionadaId,
                    conteoPorColeccion = conteoPorColeccion,
                    alAbrirMenu = { abrirMenu() },
                    alAlternarBusqueda = {
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible && busqueda.isNotBlank()) {
                            vm.buscar("")
                        }
                    },
                    alCambiarBusqueda = { vm.buscar(it) },
                    alCerrarBusqueda = {
                        busquedaVisible = false
                        vm.buscar("")
                    },
                    alMostrarOrdenacion = { dialogos.mostrarOrdenacion = true },
                    alMostrarFiltros = { dialogos.mostrarFiltros = true },
                    alAlternarSoloFavoritos = { vm.alternarSoloFavoritos() },
                    alIrOrganizacionGrupo = { vm.ir(Pantalla.OrganizacionLista("03-LST-DES-GRP")) },
                    alIrOrganizacionIndicadores = { vm.ir(Pantalla.OrganizacionLista("03-LST-DES-IND")) },
                    alIrExportarSelectivo = { vm.ir(Pantalla.ExportarSelectivo("todos")) },
                    alIrCopiaSeguridadManual = { vm.ir(Pantalla.CopiaSeguridad("05-COP-MAN")) },
                    alIrCopiaSeguridad = { vm.ir(Pantalla.CopiaSeguridad("05-COP-MAN-IMP")) },
                    alIrCsvGoogle = { vm.ir(Pantalla.CsvGoogle("05-COP-CSV-IMP")) },
                    alImportarDirectoCxf = dispararImportacionDirectoCxf,
                    alExportarDirectoCxf = { dialogos.exportarCxf = true },
                    alRestablecerFiltros = {
                        vm.filtrarPorTipo(null)
                        if (soloFavoritos) vm.alternarSoloFavoritos()
                        vm.filtrarPorEtiqueta(null)
                    },
                    alSeleccionarColeccion = { id ->
                        haptica.tic()
                        vm.seleccionarColeccion(id)
                    },
                    alCrearColeccion = { dialogos.crearColeccion = true },
                    alEditarColeccion = { col -> dialogos.coleccionParaEditar = col },
                    alEliminarColeccion = { col -> dialogos.coleccionParaEliminar = col }
                )

                ContenidoPrincipalLista(
                    visibles = visibles,
                    entradas = entradas,
                    ajustes = ajustes,
                    criterioOrdenacion = criterioOrdenacion,
                    busqueda = busqueda,
                    filtro = filtro,
                    soloFavoritos = soloFavoritos,
                    filtroEtiqueta = filtroEtiqueta,
                    etiquetasDisponibles = etiquetasDisponibles,
                    modoSeleccion = estadoSeleccion.modoSeleccion,
                    seleccionados = estadoSeleccion.seleccionados,
                    gruposExpandidos = gruposExpandidos,
                    densidadAltura = densidadAltura,
                    densidadMonograma = densidadMonograma,
                    espaciadoFilas = espaciadoFilas,
                    vm = vm,
                    haptica = haptica,
                    alImportarDirectoCxf = dispararImportacionDirectoCxf,
                    alEntrarEnSeleccion = { estadoSeleccion.entrarEnSeleccion(it) },
                    alAlternarSeleccion = { estadoSeleccion.alternarSeleccion(it) },
                    alEntrarEnSeleccionLote = { estadoSeleccion.entrarEnSeleccionLote(it) },
                    alAlternarSeleccionLote = { estadoSeleccion.alternarSeleccionLote(it) },
                    alAlternarGrupo = { clave ->
                        haptica.tic()
                        gruposExpandidos = if (gruposExpandidos.contains(clave)) {
                            gruposExpandidos - clave
                        } else {
                            gruposExpandidos + clave
                        }
                    },
                    identidades = identidades,
                    identidadSeleccionadaId = identidadSeleccionadaId,
                    conteoPorIdentidad = conteoPorIdentidad,
                    totalEntradas = entradas.size,
                    conteoSinIdentidad = conteoSinIdentidad,
                    alSeleccionarIdentidad = { id ->
                        haptica.tic()
                        vm.seleccionarIdentidad(id)
                    }
                )
            }

            CapaInferiorAccionesLista(
                estadoSeleccion = estadoSeleccion,
                entradas = entradas,
                vm = vm,
                haptica = haptica,
                alTransferirCxf = { copia -> dialogos.prepararTransferirCxf(copia) },
                alRenombrar = { titulo -> dialogos.iniciarRenombrar(titulo) },
                alAsignarColeccion = { dialogos.asignarColecciones = true },
                alBorrar = { dialogos.borrarSeleccion = true }
            )
        }
    }

    DialogosPantallaLista(
        dialogos = dialogos,
        vm = vm,
        estado = estado,
        filtroActual = filtro,
        criterioActual = criterioOrdenacion,
        estadoSeleccion = estadoSeleccion,
        entradas = entradas,
        colecciones = colecciones,
        haptica = haptica
    )
}
