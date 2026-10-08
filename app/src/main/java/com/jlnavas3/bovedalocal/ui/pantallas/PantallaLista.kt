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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.jlnavas3.bovedalocal.ui.pantallas.lista.bottomsheets.BottomSheetFiltroCategorias
import com.jlnavas3.bovedalocal.ui.pantallas.lista.bottomsheets.BottomSheetFiltroEtiquetas
import com.jlnavas3.bovedalocal.ui.pantallas.lista.bottomsheets.BottomSheetFiltroIdentidades
import com.jlnavas3.bovedalocal.ui.pantallas.lista.rememberEstadoDialogosLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.rememberEstadoSeleccionLista
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
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
        "compacta" -> 42.dp
        "comoda" -> 54.dp
        else -> 64.dp
    }
    val densidadMonograma = when (densidad) {
        "compacta" -> 30
        "comoda" -> 36
        else -> 40
    }
    val espaciadoFilas = calcularEspaciadoFilas(densidad)
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val categorias = remember(estado) { (estado as? EstadoBoveda.Desbloqueada)?.categorias ?: emptyList() }
    val identidades = remember(estado) { (estado as? EstadoBoveda.Desbloqueada)?.identidades ?: emptyList() }
    val categoriaSeleccionadaId by vm.filtroCategoria.collectAsStateWithLifecycle()
    val identidadSeleccionadaId by vm.identidadSeleccionadaId.collectAsStateWithLifecycle()

    val jerarquia = ajustes.jerarquiaOrganizacionEfectiva

    val (categoriasEfectivas, conteoPorCategoria) = remember(
        entradas, categorias, identidades, identidadSeleccionadaId, jerarquia
    ) {
        if (jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA) {
            com.jlnavas3.bovedalocal.util.filtrarCategoriasPorIdentidad(categorias, entradas, identidades, identidadSeleccionadaId)
        } else {
            val conteos = categorias.associate { cat -> cat.id to entradas.count { it.categorias.contains(cat.id) } }
            Pair(categorias, conteos)
        }
    }

    val (identidadesEfectivas, conteoPorIdentidad, conteoSinIdentidad) = remember(
        entradas, identidades, categoriaSeleccionadaId, jerarquia
    ) {
        if (jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD) {
            com.jlnavas3.bovedalocal.util.filtrarIdentidadesPorCategoria(identidades, entradas, categoriaSeleccionadaId)
        } else {
            val conteos = identidades.associate { iden ->
                iden.id to entradas.count { entrada ->
                    com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
                }
            }
            val sinIden = entradas.count { entrada ->
                com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(entrada, identidades) == null
            }
            Triple(identidades, conteos, sinIden)
        }
    }

    // Si la categoría seleccionada ya no tiene elementos bajo la identidad activa, deseleccionar
    LaunchedEffect(categoriasEfectivas, categoriaSeleccionadaId, jerarquia) {
        if (jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA &&
            categoriaSeleccionadaId != null &&
            categoriasEfectivas.none { it.id == categoriaSeleccionadaId }
        ) {
            vm.seleccionarCategoria(null)
        }
    }

    // Si la identidad seleccionada ya no tiene elementos bajo la categoría activa, deseleccionar
    LaunchedEffect(identidadesEfectivas, identidadSeleccionadaId, jerarquia) {
        if (jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD &&
            identidadSeleccionadaId != null &&
            identidadSeleccionadaId != "__SIN_IDENTIDAD__" &&
            identidadesEfectivas.none { it.id == identidadSeleccionadaId }
        ) {
            vm.seleccionarIdentidad(null)
        }
    }

    val visibles = remember(entradas, busqueda, filtro, soloFavoritos, filtroEtiqueta, categoriaSeleccionadaId, identidadSeleccionadaId, criterioOrdenacion, identidades) {
        vm.entradasVisibles(entradas, identidades)
    }
    val etiquetasDisponibles = remember(entradas) { vm.etiquetasUsadas() }

    var busquedaVisible by remember { mutableStateOf(false) }
    var barraPildorasVisible by remember { mutableStateOf(true) }
    var mostrarSheetIdentidades by remember { mutableStateOf(false) }
    var mostrarSheetCategorias by remember { mutableStateOf(false) }
    var mostrarSheetEtiquetas by remember { mutableStateOf(false) }

    LaunchedEffect(busquedaVisible, busqueda) {
        if (busquedaVisible || busqueda.isNotBlank()) {
            barraPildorasVisible = true
        }
    }

    val conexionScrollPildoras = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -12f) {
                    if (barraPildorasVisible) barraPildorasVisible = false
                } else if (available.y > 12f) {
                    if (!barraPildorasVisible) barraPildorasVisible = true
                }
                return Offset.Zero
            }
        }
    }

    val abrirDrawerAlVolver by vm.abrirMenuLateralAlVolverALista.collectAsStateWithLifecycle()
    val estadoCajon = rememberDrawerState(
        initialValue = if (vm.abrirMenuLateralAlVolverALista.value) DrawerValue.Open else DrawerValue.Closed
    )
    val ambitoCorutina = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    fun abrirMenu() = ambitoCorutina.launch {
        focusManager.clearFocus()
        keyboardController?.hide()
        estadoCajon.open()
    }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(conexionScrollPildoras)
                .cerrarTecladoAlTocarFuera()
        ) {
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
                    categorias = categoriasEfectivas,
                    categoriaSeleccionadaId = categoriaSeleccionadaId,
                    conteoPorCategoria = conteoPorCategoria,
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
                    alSeleccionarCategoria = { id ->
                        haptica.tic()
                        vm.seleccionarCategoria(id)
                    },
                    alCrearCategoria = { dialogos.crearCategoria = true },
                    alEditarCategoria = { cat -> dialogos.categoriaParaEditar = cat },
                    alEliminarCategoria = { cat -> dialogos.categoriaParaEliminar = cat },
                    jerarquiaOrganizacion = jerarquia,
                    modoVisualizacionIdentidades = ajustes.modoVisualizacionIdentidades,
                    identidades = identidadesEfectivas,
                    identidadSeleccionadaId = identidadSeleccionadaId,
                    conteoPorIdentidad = conteoPorIdentidad,
                    conteoSinIdentidad = conteoSinIdentidad,
                    alSeleccionarIdentidad = { id ->
                        haptica.tic()
                        vm.seleccionarIdentidad(id)
                    },
                    barraPildorasVisible = barraPildorasVisible,
                    etiquetasDisponibles = etiquetasDisponibles,
                    alAbrirSelectorIdentidad = { mostrarSheetIdentidades = true },
                    alAbrirSelectorCategoria = { mostrarSheetCategorias = true },
                    alAbrirSelectorEtiqueta = { mostrarSheetEtiquetas = true },
                    alSeleccionarEtiqueta = { et ->
                        haptica.tic()
                        vm.filtrarPorEtiqueta(et)
                    },
                    alLimpiarTipo = {
                        haptica.tic()
                        vm.filtrarPorTipo(null)
                    }
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
                    categorias = categorias
                )
            }

            CapaInferiorAccionesLista(
                estadoSeleccion = estadoSeleccion,
                entradas = entradas,
                vm = vm,
                haptica = haptica,
                alTransferirCxf = { copia -> dialogos.prepararTransferirCxf(copia) },
                alRenombrar = { titulo -> dialogos.iniciarRenombrar(titulo) },
                alAsignarCategoria = { dialogos.asignarCategorias = true },
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
        categorias = categorias,
        haptica = haptica
    )

    BottomSheetFiltroIdentidades(
        visible = mostrarSheetIdentidades,
        identidades = identidadesEfectivas,
        identidadSeleccionadaId = identidadSeleccionadaId,
        conteoPorIdentidad = conteoPorIdentidad,
        totalEntradas = entradas.size,
        conteoSinIdentidad = conteoSinIdentidad,
        alSeleccionarIdentidad = { id ->
            haptica.tic()
            vm.seleccionarIdentidad(id)
        },
        alGestionarIdentidades = {
            haptica.tic()
            vm.ir(Pantalla.Identidades("03-LST-DES-GID"))
        },
        alCerrar = { mostrarSheetIdentidades = false }
    )

    BottomSheetFiltroCategorias(
        visible = mostrarSheetCategorias,
        categorias = categoriasEfectivas,
        categoriaSeleccionadaId = categoriaSeleccionadaId,
        conteoPorCategoria = conteoPorCategoria,
        totalEntradas = entradas.size,
        alSeleccionarCategoria = { id ->
            haptica.tic()
            vm.seleccionarCategoria(id)
        },
        alCrearCategoria = { dialogos.crearCategoria = true },
        alGestionarCategorias = {
            haptica.tic()
            vm.ir(Pantalla.Categorias("03-LST-CAT"))
        },
        alCerrar = { mostrarSheetCategorias = false }
    )

    BottomSheetFiltroEtiquetas(
        visible = mostrarSheetEtiquetas,
        etiquetas = etiquetasDisponibles,
        filtroEtiqueta = filtroEtiqueta,
        alSeleccionarEtiqueta = { et ->
            haptica.tic()
            vm.filtrarPorEtiqueta(et)
        },
        alCerrar = { mostrarSheetEtiquetas = false }
    )
}
