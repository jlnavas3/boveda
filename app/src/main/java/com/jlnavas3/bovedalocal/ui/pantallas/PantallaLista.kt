package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.colecciones.BarraColeccionesLista
import com.jlnavas3.bovedalocal.ui.pantallas.colecciones.DialogoCrearEditarColeccion
import com.jlnavas3.bovedalocal.ui.pantallas.colecciones.DialogoAsignarColecciones
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BannerRecordatorioExportacion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraInferiorSeleccion
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraSuperiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraSuperiorLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltro
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.CuerpoListaEntradas
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoBorrarSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoFiltrosLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoRenombrarSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.EstadoVacioLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.MenuLateral
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltro
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoBorrarSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoFiltrosLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoRenombrarSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.EstadoVacioLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.FilaEntrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorSitio
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
    val coleccionSeleccionadaId by vm.filtroColeccion.collectAsStateWithLifecycle()
    val conteoPorColeccion = remember(entradas, colecciones) {
        colecciones.associate { col -> col.id to entradas.count { it.colecciones.contains(col.id) } }
    }
    val visibles = remember(entradas, busqueda, filtro, soloFavoritos, filtroEtiqueta, coleccionSeleccionadaId, criterioOrdenacion) {
        vm.entradasVisibles(entradas)
    }
    val etiquetasDisponibles = remember(entradas) { vm.etiquetasUsadas() }

    var coleccionParaEditar by remember { mutableStateOf<Coleccion?>(null) }
    var mostrarDialogoCrearColeccion by remember { mutableStateOf(false) }
    var coleccionParaEliminar by remember { mutableStateOf<Coleccion?>(null) }
    var mostrarDialogoAsignarColecciones by remember { mutableStateOf(false) }

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
                when (val res = com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.importarCredenciales(act)) {
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.Exito -> {
                        haptica.exito()
                        vm.ir(Pantalla.ConfirmarImportacionCxf(res.jsonPayload))
                    }
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.Cancelado -> {
                        // El usuario canceló la hoja del sistema
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

    // ------------------------------------------------------- selección múltiple
    var modoSeleccion by remember { mutableStateOf(false) }
    var seleccionados by remember { mutableStateOf(setOf<String>()) }
    var dialogoBorrarSeleccion by remember { mutableStateOf(false) }
    var mostrarDialogoExportarCxf by remember { mutableStateOf(false) }
    var entradasParaTransferirCxf by remember { mutableStateOf<List<Entrada>?>(null) }
    var dialogoRenombrarSeleccion by remember { mutableStateOf(false) }
    var textoNuevoTitulo by remember { mutableStateOf("") }

    // Claves "categoria|sitio" de los grupos por sitio que el usuario ha desplegado a mano.
    var gruposExpandidos by remember { mutableStateOf(setOf<String>()) }

    var busquedaVisible by remember { mutableStateOf(false) }
    var mostrarDialogoFiltros by remember { mutableStateOf(false) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }

    fun salirDeSeleccion() {
        modoSeleccion = false
        seleccionados = emptySet()
    }

    BackHandler(enabled = estadoCajon.isOpen || modoSeleccion || busqueda.isNotEmpty()) {
        when {
            estadoCajon.isOpen -> cerrarMenu()
            modoSeleccion -> salirDeSeleccion()
            busqueda.isNotEmpty() -> vm.buscar("")
        }
    }

    fun alternarSeleccion(id: String) {
        seleccionados = if (seleccionados.contains(id)) seleccionados - id else seleccionados + id
        if (seleccionados.isEmpty()) modoSeleccion = false
    }

    fun entrarEnSeleccion(id: String) {
        modoSeleccion = true
        seleccionados = setOf(id)
        haptica.tic()
    }

    fun alternarSeleccionLote(ids: Set<String>) {
        if (ids.isEmpty()) return
        haptica.tic()
        val todosEstanSeleccionados = seleccionados.containsAll(ids)
        seleccionados = if (todosEstanSeleccionados) {
            seleccionados - ids
        } else {
            seleccionados + ids
        }
        modoSeleccion = seleccionados.isNotEmpty()
    }

    fun entrarEnSeleccionLote(ids: Set<String>) {
        if (ids.isEmpty()) return
        haptica.tic()
        modoSeleccion = true
        seleccionados = seleccionados + ids
    }

    // Selecciona/deselecciona todo lo que se ve ahora mismo (respeta búsqueda y filtros).
    val todoSeleccionado = visibles.isNotEmpty() && seleccionados.containsAll(visibles.map { it.id })
    fun alternarSeleccionarTodo() {
        haptica.tic()
        if (todoSeleccionado) {
            seleccionados = emptySet()
            modoSeleccion = false
        } else {
            modoSeleccion = true
            seleccionados = visibles.map { it.id }.toSet()
        }
    }

    // Si la lista cambia de raíz (por ejemplo, tras borrar) no queremos ids fantasma.
    LaunchedEffect(entradas) {
        if (modoSeleccion) {
            val vivos = entradas.map { it.id }.toSet()
            seleccionados = seleccionados.intersect(vivos)
            if (seleccionados.isEmpty()) modoSeleccion = false
        }
    }

    val totalDuplicadas = remember(entradas) {
        AnalizadorDuplicados.analizar(entradas).sumOf { it.entradasSecundarias.size }
    }

    val formaCajon = RectangleShape
    val colorLineaBordeCajon = if (ColorBordeActual != Color.Transparent) {
        ColorBordeActual.copy(alpha = 0.38f)
    } else {
        Color.White.copy(alpha = 0.12f)
    }

    ModalNavigationDrawer(
        drawerState = estadoCajon,
        scrimColor = Color.Black.copy(alpha = 0.68f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .drawWithContent {
                        drawContent()
                        val strokeWidth = 2.5.dp.toPx()
                        drawLine(
                            color = colorLineaBordeCajon,
                            start = Offset(size.width - strokeWidth / 2, 0f),
                            end = Offset(size.width - strokeWidth / 2, size.height),
                            strokeWidth = strokeWidth
                        )
                    },
                drawerShape = formaCajon,
                drawerContainerColor = Superficie,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                MenuLateral(
                    nombreApp = ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
                    totalEntradas = entradas.size,
                    totalPapelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera?.size ?: 0,
                    totalDuplicadas = totalDuplicadas,
                    perfilArgon2 = vm.repositorio.perfilArgon2Actual(),
                    mostrarIds = ajustes.mostrarIdsAjustes,
                    ajustes = ajustes,
                    alIr = { destino -> vm.irDesdeMenuLateral(destino) },
                    alBloquear = { cerrarMenu(); haptica.toque(); vm.bloquear() }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (modoSeleccion) {
                    BarraSuperiorSeleccion(
                        cantidad = seleccionados.size,
                        todoSeleccionado = todoSeleccionado,
                        alCancelar = { salirDeSeleccion() },
                        alSeleccionarTodo = {
                            alternarSeleccionarTodo()
                        },
                        alDeseleccionarTodo = {
                            seleccionados = emptySet()
                            modoSeleccion = false
                        }
                    )
                } else {
                    BarraSuperiorLista(
                        nombreBoveda = ajustes.nombrePersonalizado,
                        totalEntradas = entradas.size,
                        busquedaVisible = busquedaVisible,
                        busquedaActiva = busqueda.isNotBlank(),
                        tieneFiltrosActivos = filtro != null || soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ,
                        soloFavoritos = soloFavoritos,
                        agruparPorSitio = ajustes.agruparPorSitio,
                        mostrarIndicadoresContenido = ajustes.mostrarIndicadoresContenido,
                        hayFiltrosParaRestablecer = filtro != null || soloFavoritos || filtroEtiqueta != null,
                        alAbrirMenu = { abrirMenu() },
                        alAlternarBusqueda = {
                            busquedaVisible = !busquedaVisible
                            if (!busquedaVisible && busqueda.isNotBlank()) {
                                vm.buscar("")
                            }
                        },
                        alMostrarOrdenacion = { mostrarDialogoOrdenacion = true },
                        alMostrarFiltros = { mostrarDialogoFiltros = true },
                        alAlternarSoloFavoritos = { vm.alternarSoloFavoritos() },
                        alIrOrganizacionGrupo = { vm.ir(Pantalla.OrganizacionLista("03-LST-DES-GRP")) },
                        alIrOrganizacionIndicadores = { vm.ir(Pantalla.OrganizacionLista("03-LST-DES-IND")) },
                        alIrExportarSelectivo = { vm.ir(Pantalla.ExportarSelectivo("todos")) },
                        alIrCopiaSeguridadManual = { vm.ir(Pantalla.CopiaSeguridad("05-COP-MAN")) },
                        alIrCopiaSeguridad = { vm.ir(Pantalla.CopiaSeguridad("05-COP-MAN-IMP")) },
                        alIrCsvGoogle = { vm.ir(Pantalla.CsvGoogle("05-COP-CSV-IMP")) },
                        alImportarDirectoCxf = dispararImportacionDirectoCxf,
                        alExportarDirectoCxf = { mostrarDialogoExportarCxf = true },
                        alRestablecerFiltros = {
                            vm.filtrarPorTipo(null)
                            if (soloFavoritos) vm.alternarSoloFavoritos()
                            vm.filtrarPorEtiqueta(null)
                        }
                    )

                    // Campo de Búsqueda Animado (One UI Expandible)
                    AnimatedVisibility(
                        visible = busquedaVisible || busqueda.isNotBlank(),
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 2.dp)
                        ) {
                            BarraBusquedaAnimada(
                                valor = busqueda,
                                alCambiar = { vm.buscar(it) },
                                alCerrar = {
                                    busquedaVisible = false
                                    vm.buscar("")
                                }
                            )
                        }
                    }

                    // Barra horizontal de colecciones (Todas + Colecciones personalizadas)
                    BarraColeccionesLista(
                        colecciones = colecciones,
                        coleccionSeleccionadaId = coleccionSeleccionadaId,
                        totalEntradas = entradas.size,
                        conteoPorColeccion = conteoPorColeccion,
                        alSeleccionarColeccion = { id ->
                            haptica.tic()
                            vm.seleccionarColeccion(id)
                        },
                        alCrearColeccion = {
                            mostrarDialogoCrearColeccion = true
                        },
                        alEditarColeccion = { col ->
                            coleccionParaEditar = col
                        },
                        alEliminarColeccion = { col ->
                            coleccionParaEliminar = col
                        }
                    )
                }

                val recordatorio = remember(ajustes, entradas) { vm.recordatorioExportacionInfo() }
                if (recordatorio != null) {
                    Spacer(Modifier.height((EspaciadoComponentes * 0.8f).coerceAtLeast(6.dp)))
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        BannerRecordatorioExportacion(
                            info = recordatorio,
                            alIr = { vm.ir(Pantalla.CopiaSeguridad("05-COP-MAN")) }
                        )
                    }
                }

                // Chips de Filtros Activos y Etiquetas
                val hayFiltroActivo = filtro != null || soloFavoritos || filtroEtiqueta != null
                if (hayFiltroActivo || etiquetasDisponibles.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (soloFavoritos) {
                            ChipFiltroActivo(
                                texto = "Favoritos",
                                icono = Icons.Filled.Star,
                                alLimpiar = { vm.alternarSoloFavoritos() }
                            )
                        }
                        if (filtro != null) {
                            ChipFiltroActivo(
                                texto = filtro?.etiqueta ?: "Filtro",
                                icono = Icons.Filled.FilterList,
                                alLimpiar = { vm.filtrarPorTipo(null) }
                            )
                        }
                        if (filtroEtiqueta != null) {
                            ChipFiltroActivo(
                                texto = normalizarEtiqueta(filtroEtiqueta!!),
                                icono = Icons.Filled.Sell,
                                alLimpiar = { vm.filtrarPorEtiqueta(null) }
                            )
                        }
                        etiquetasDisponibles.filter { it != filtroEtiqueta }.forEach { etiqueta ->
                            ChipFiltro(
                                texto = normalizarEtiqueta(etiqueta),
                                activo = false,
                                icono = Icons.Filled.Sell
                            ) {
                                vm.filtrarPorEtiqueta(etiqueta)
                            }
                        }
                    }
                }

                if (mostrarDialogoFiltros) {
                    DialogoFiltrosLista(
                        filtroActual = filtro,
                        alSeleccionarTipo = { tipo ->
                            haptica.tic()
                            vm.filtrarPorTipo(tipo)
                        },
                        alCerrar = { mostrarDialogoFiltros = false }
                    )
                }

                if (mostrarDialogoOrdenacion) {
                    DialogoOrdenacionLista(
                        criterioActual = criterioOrdenacion,
                        alSeleccionarCriterio = { crit ->
                            haptica.tic()
                            vm.cambiarCriterioOrdenacion(crit)
                        },
                        alCerrar = { mostrarDialogoOrdenacion = false }
                    )
                }

                Spacer(Modifier.height((EspaciadoComponentes * 0.8f).coerceAtLeast(6.dp)))

                if (visibles.isEmpty()) {
                    EstadoVacioLista(
                        entradasVacias = entradas.isEmpty(),
                        alImportarCopia = {
                            haptica.tic()
                            vm.ir(Pantalla.CopiaSeguridad("05-COP-MAN-IMP"))
                        },
                        alImportarCsvGoogle = {
                            haptica.tic()
                            vm.ir(Pantalla.CsvGoogle("05-COP-CSV-IMP"))
                        },
                        alImportarGoogleAuthenticator = {
                            haptica.tic()
                            vm.ir(Pantalla.Escaner())
                        },
                        alImportarDirectoCxf = dispararImportacionDirectoCxf
                    )
                } else {
                    CuerpoListaEntradas(
                        visibles = visibles,
                        ajustes = ajustes,
                        criterioOrdenacion = criterioOrdenacion,
                        busqueda = busqueda,
                        modoSeleccion = modoSeleccion,
                        seleccionados = seleccionados,
                        gruposExpandidos = gruposExpandidos,
                        densidadAltura = densidadAltura,
                        densidadMonograma = densidadMonograma,
                        espaciadoFilas = espaciadoFilas,
                        alAbrirEntrada = { id ->
                            val listaIdsVisibles = visibles.map { it.id }
                            vm.ir(Pantalla.Detalle(id, idsContexto = listaIdsVisibles))
                        },
                        alCopiarUsuario = { id, usuario ->
                            haptica.toque()
                            vm.copiar("Usuario", usuario, sensible = false)
                            vm.registrarUsoEntrada(id)
                        },
                        alCopiarContrasena = { id, contrasena ->
                            haptica.exito()
                            vm.copiar("Contraseña", contrasena, sensible = true)
                            vm.registrarUsoEntrada(id)
                        },
                        alCopiarCodigoTotp = { id, codigo ->
                            haptica.exito()
                            vm.copiar("Código", codigo, sensible = true)
                            vm.registrarUsoEntrada(id)
                        },
                        alAlternarFavorito = { id ->
                            haptica.tic()
                            vm.alternarFavorito(id)
                        },
                        alEntrarEnSeleccion = { entrarEnSeleccion(it) },
                        alAlternarSeleccion = { alternarSeleccion(it) },
                        alEntrarEnSeleccionLote = { entrarEnSeleccionLote(it) },
                        alAlternarSeleccionLote = { alternarSeleccionLote(it) },
                        alAlternarGrupo = { clave ->
                            haptica.tic()
                            gruposExpandidos = if (gruposExpandidos.contains(clave)) {
                                gruposExpandidos - clave
                            } else {
                                gruposExpandidos + clave
                            }
                        }
                    )
                }
            }

            if (!modoSeleccion) {
                val formaFab = RoundedCornerShape(CurvaturaEsquinas)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // FAB superior: Bloquear app (Candado)
                    SmallFloatingActionButton(
                        onClick = {
                            haptica.toque()
                            vm.bloquear()
                        },
                        containerColor = ColorTarjetaAjustes,
                        contentColor = ColorAcento,
                        shape = formaFab,
                        modifier = Modifier.then(
                            if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                                Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                            } else Modifier
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Bloquear bóveda",
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // FAB inferior: Nueva entrada (+)
                    FloatingActionButton(
                        onClick = { haptica.toque(); vm.ir(Pantalla.Editar(null)) },
                        containerColor = ColorAcento,
                        contentColor = ColorSobreAcento,
                        shape = formaFab,
                        modifier = Modifier.then(
                            if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                                Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                            } else Modifier
                        )
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Nueva entrada", modifier = Modifier.size(24.dp))
                    }
                }
            } else {
                val itemsSeleccionados = remember(entradas, seleccionados) {
                    entradas.filter { seleccionados.contains(it.id) }
                }
                val todosSonFavoritos = remember(itemsSeleccionados) {
                    itemsSeleccionados.isNotEmpty() && itemsSeleccionados.all { it.favorito }
                }

                BarraInferiorSeleccion(
                    cantidad = seleccionados.size,
                    todosSonFavoritos = todosSonFavoritos,
                    alAlternarFavoritos = {
                        val ids = seleccionados.toSet()
                        salirDeSeleccion()
                        vm.alternarFavoritosVarias(ids)
                    },
                    alComparar = {
                        val listaComparar = seleccionados.toList()
                        if (listaComparar.size >= 2) {
                            salirDeSeleccion()
                            vm.ir(
                                Pantalla.Detalle(
                                    id = listaComparar.first(),
                                    idsContexto = listaComparar,
                                    modoComparacion = true
                                )
                            )
                        }
                    },
                    alRespaldar = {
                        val ids = seleccionados.joinToString(",")
                        salirDeSeleccion()
                        vm.ir(Pantalla.ExportarSelectivo("ids:$ids"))
                    },
                    alTransferirCxf = {
                        val copia = itemsSeleccionados.toList()
                        salirDeSeleccion()
                        entradasParaTransferirCxf = copia
                    },
                    alRenombrar = {
                        val primera = entradas.firstOrNull { seleccionados.contains(it.id) }
                        textoNuevoTitulo = primera?.titulo ?: ""
                        dialogoRenombrarSeleccion = true
                    },
                    alAsignarColeccion = {
                        mostrarDialogoAsignarColecciones = true
                    },
                    alBorrar = {
                        dialogoBorrarSeleccion = true
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }

    if (dialogoBorrarSeleccion) {
        DialogoBorrarSeleccion(
            cantidad = seleccionados.size,
            alConfirmar = {
                dialogoBorrarSeleccion = false
                val idsABorrar = seleccionados
                salirDeSeleccion()
                vm.eliminarVarias(idsABorrar)
            },
            alDescartar = { dialogoBorrarSeleccion = false }
        )
    }

    if (dialogoRenombrarSeleccion) {
        DialogoRenombrarSeleccion(
            cantidad = seleccionados.size,
            textoNuevoTitulo = textoNuevoTitulo,
            alCambiarTexto = { textoNuevoTitulo = it },
            alConfirmar = {
                val nuevo = textoNuevoTitulo.trim()
                if (nuevo.isNotBlank()) {
                    dialogoRenombrarSeleccion = false
                    val idsARenombrar = seleccionados
                    salirDeSeleccion()
                    vm.renombrarVarias(idsARenombrar, nuevo)
                    haptica.exito()
                }
            },
            alDescartar = { dialogoRenombrarSeleccion = false }
        )
    }

    entradasParaTransferirCxf?.let { entradasSeleccionadas ->
        com.jlnavas3.bovedalocal.ui.pantallas.cxf.DialogoExportacionDirectaCxf(
            entradas = entradasSeleccionadas,
            esSeleccionPersonalizada = true,
            alCerrar = { entradasParaTransferirCxf = null }
        )
    }

    if (mostrarDialogoExportarCxf && estado is EstadoBoveda.Desbloqueada) {
        com.jlnavas3.bovedalocal.ui.pantallas.cxf.DialogoExportacionDirectaCxf(
            entradas = estado.entradas,
            esSeleccionPersonalizada = false,
            alCerrar = { mostrarDialogoExportarCxf = false }
        )
    }

    if (mostrarDialogoAsignarColecciones) {
        val seleccionadosLista = remember(seleccionados, entradas) {
            entradas.filter { seleccionados.contains(it.id) }
        }
        DialogoAsignarColecciones(
            entradasSeleccionadas = seleccionadosLista,
            coleccionesDisponibles = colecciones,
            alCrearNuevaColeccion = {
                mostrarDialogoCrearColeccion = true
            },
            alGuardar = { idsAgregar, idsQuitar ->
                vm.asignarColeccionesAEntradas(seleccionados, idsAgregar, idsQuitar)
                mostrarDialogoAsignarColecciones = false
                salirDeSeleccion()
                haptica.exito()
            },
            alDescartar = { mostrarDialogoAsignarColecciones = false }
        )
    }

    if (mostrarDialogoCrearColeccion || coleccionParaEditar != null) {
        DialogoCrearEditarColeccion(
            coleccionAEditar = coleccionParaEditar,
            alGuardar = { nombre, icono, colorHex ->
                val colEdit = coleccionParaEditar
                if (colEdit != null) {
                    vm.actualizarColeccion(colEdit.id, nombre, icono, colorHex)
                } else {
                    vm.crearColeccion(nombre, icono, colorHex)
                }
                mostrarDialogoCrearColeccion = false
                coleccionParaEditar = null
                haptica.exito()
            },
            alDescartar = {
                mostrarDialogoCrearColeccion = false
                coleccionParaEditar = null
            }
        )
    }

    coleccionParaEliminar?.let { col ->
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar colección?",
            mensaje = "Se eliminará la colección '${col.nombre}'. Las credenciales asociadas no se borrarán.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = androidx.compose.material.icons.Icons.Filled.Delete,
            alConfirmar = {
                val id = col.id
                coleccionParaEliminar = null
                vm.eliminarColeccion(id)
                haptica.exito()
            },
            alDescartar = { coleccionParaEliminar = null }
        )
    }
}
