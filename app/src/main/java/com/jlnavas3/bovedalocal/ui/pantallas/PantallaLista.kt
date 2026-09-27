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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BannerRecordatorioExportacion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraSeleccion
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
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoRenombrarSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.EstadoVacioLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.FilaEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.MenuLateral
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
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
    val espaciadoFilas = when (densidad) {
        "compacta" -> (EspaciadoComponentes * 0.45f).coerceAtLeast(3.dp)
        "comoda" -> (EspaciadoComponentes * 0.7f).coerceAtLeast(6.dp)
        else -> EspaciadoComponentes
    }
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val visibles = remember(entradas, busqueda, filtro, soloFavoritos, filtroEtiqueta, criterioOrdenacion) { vm.entradasVisibles(entradas) }
    val etiquetasDisponibles = remember(entradas) { vm.etiquetasUsadas() }

    val estadoCajon = rememberDrawerState(DrawerValue.Closed)
    val ambitoCorutina = rememberCoroutineScope()
    fun abrirMenu() = ambitoCorutina.launch { estadoCajon.open() }
    fun cerrarMenu() = ambitoCorutina.launch { estadoCajon.close() }

    // ------------------------------------------------------- selección múltiple
    var modoSeleccion by remember { mutableStateOf(false) }
    var seleccionados by remember { mutableStateOf(setOf<String>()) }
    var dialogoBorrarSeleccion by remember { mutableStateOf(false) }
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

    val formaCajon = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
    val modifierBordeCajon = if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
        Modifier.border(GrosorBorde, ColorBordeActual.copy(alpha = 0.5f), formaCajon)
    } else {
        Modifier
    }

    ModalNavigationDrawer(
        drawerState = estadoCajon,
        scrimColor = Color.Black.copy(alpha = 0.68f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .then(modifierBordeCajon),
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
                    alIr = { destino -> cerrarMenu(); vm.irDesdeMenuLateral(destino) },
                    alBloquear = { cerrarMenu(); haptica.toque(); vm.bloquear() }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (modoSeleccion) {
                    BarraSeleccion(
                        cantidad = seleccionados.size,
                        todoSeleccionado = todoSeleccionado,
                        alCancelar = { salirDeSeleccion() },
                        alRenombrar = {
                            val primera = entradas.firstOrNull { seleccionados.contains(it.id) }
                            textoNuevoTitulo = primera?.titulo ?: ""
                            dialogoRenombrarSeleccion = true
                        },
                        alSeleccionarTodo = { alternarSeleccionarTodo() },
                        alBorrar = { dialogoBorrarSeleccion = true }
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
                        alBloquear = { vm.bloquear() },
                        alAlternarBusqueda = {
                            busquedaVisible = !busquedaVisible
                            if (!busquedaVisible && busqueda.isNotBlank()) {
                                vm.buscar("")
                            }
                        },
                        alMostrarOrdenacion = { mostrarDialogoOrdenacion = true },
                        alMostrarFiltros = { mostrarDialogoFiltros = true },
                        alAlternarSoloFavoritos = { vm.alternarSoloFavoritos() },
                        alIrOrganizacionGrupo = { vm.ir(Pantalla.OrganizacionLista("03.5.1")) },
                        alIrOrganizacionIndicadores = { vm.ir(Pantalla.OrganizacionLista("03.5.2")) },
                        alIrExportarSelectivo = { vm.ir(Pantalla.ExportarSelectivo("todos")) },
                        alIrCopiaSeguridad = { vm.ir(Pantalla.CopiaSeguridad("02.1.3")) },
                        alIrCsvGoogle = { vm.ir(Pantalla.CsvGoogle("02.2.1")) },
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
                }

                val recordatorio = remember(ajustes, entradas) { vm.recordatorioExportacionInfo() }
                if (recordatorio != null) {
                    Spacer(Modifier.height((EspaciadoComponentes * 0.8f).coerceAtLeast(6.dp)))
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        BannerRecordatorioExportacion(
                            info = recordatorio,
                            alIr = { vm.ir(Pantalla.CopiaSeguridad("02.1.4")) }
                        )
                    }
                }

                // Chips de Filtros Activos y Etiquetas
                val hayFiltroActivo = filtro != null || soloFavoritos || filtroEtiqueta != null
                if (hayFiltroActivo || etiquetasDisponibles.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (soloFavoritos) {
                            ChipFiltroActivo(
                                texto = "★ Favoritos",
                                alLimpiar = { vm.alternarSoloFavoritos() }
                            )
                        }
                        if (filtro != null) {
                            ChipFiltroActivo(
                                texto = filtro?.etiqueta ?: "Filtro",
                                alLimpiar = { vm.filtrarPorTipo(null) }
                            )
                        }
                        if (filtroEtiqueta != null) {
                            ChipFiltroActivo(
                                texto = "#${normalizarEtiqueta(filtroEtiqueta!!)}",
                                alLimpiar = { vm.filtrarPorEtiqueta(null) }
                            )
                        }
                        etiquetasDisponibles.filter { it != filtroEtiqueta }.forEach { etiqueta ->
                            ChipFiltro("#${normalizarEtiqueta(etiqueta)}", false) {
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
                            vm.ir(Pantalla.CopiaSeguridad("02.1.3"))
                        },
                        alImportarCsvGoogle = {
                            haptica.tic()
                            vm.ir(Pantalla.CsvGoogle("02.2.1"))
                        }
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
                        alAbrirEntrada = { vm.ir(Pantalla.Detalle(it)) },
                        alCopiarUsuario = { usuario ->
                            haptica.toque()
                            vm.copiar("Usuario", usuario, sensible = false)
                        },
                        alCopiarContrasena = { contrasena ->
                            haptica.exito()
                            vm.copiar("Contraseña", contrasena, sensible = true)
                        },
                        alCopiarCodigoTotp = { codigo ->
                            haptica.exito()
                            vm.copiar("Código", codigo, sensible = true)
                        },
                        alAlternarFavorito = { id ->
                            haptica.tic()
                            vm.alternarFavorito(id)
                        },
                        alEntrarEnSeleccion = { entrarEnSeleccion(it) },
                        alAlternarSeleccion = { alternarSeleccion(it) },
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
                FloatingActionButton(
                    onClick = { haptica.toque(); vm.ir(Pantalla.Editar(null)) },
                    containerColor = Ambar,
                    contentColor = ColorSobreAcento,
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Nueva entrada", modifier = Modifier.size(24.dp))
                }
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
}
