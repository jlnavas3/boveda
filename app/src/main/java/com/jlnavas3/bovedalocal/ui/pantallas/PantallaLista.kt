package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlin.math.abs
import kotlin.math.roundToInt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BuildConfig
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.ui.componentes.AnilloTotp
import com.jlnavas3.bovedalocal.ui.componentes.IlustracionVacio
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
import com.jlnavas3.bovedalocal.ui.componentes.encontrarIndiceParaLetra
import androidx.compose.ui.text.style.TextOverflow
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BannerRecordatorioExportacion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.CampoBusquedaLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltro
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoFiltrosLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.FilaEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.FilaGrupoSitio
import com.jlnavas3.bovedalocal.ui.pantallas.lista.MenuLateral
import com.jlnavas3.bovedalocal.ui.pantallas.lista.SelectorFiltros
import com.jlnavas3.bovedalocal.ui.pantallas.lista.SelectorOrdenacion
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeDropdown
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSeparadorDropdown
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
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
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
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
        com.jlnavas3.bovedalocal.data.AnalizadorDuplicados.analizar(entradas).sumOf { it.entradasSecundarias.size }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { haptica.toque(); abrirMenu() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menú",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = ColorTitulos,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "${entradas.size} ${if (entradas.size == 1) "entrada" else "entradas"} cifradas",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        maxLines = 1
                    )
                }

                // Botón Búsqueda (Lupa)
                IconButton(
                    onClick = {
                        haptica.tic()
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible && busqueda.isNotBlank()) {
                            vm.buscar("")
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (busquedaVisible || busqueda.isNotBlank()) Ambar.copy(alpha = 0.16f) else com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar",
                        tint = if (busquedaVisible || busqueda.isNotBlank()) Ambar else ColorIconosInternos,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(6.dp))

                // Botón Tres Puntos (Filtros y Ordenación)
                Box {
                    val tieneFiltrosActivos = filtro != null || soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ
                    IconButton(
                        onClick = {
                            haptica.tic()
                            menuOpcionesDesplegado = true
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (tieneFiltrosActivos) Ambar.copy(alpha = 0.16f) else com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Más opciones",
                            tint = if (tieneFiltrosActivos) Ambar else ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    MenuDesplegableBoveda(
                        expanded = menuOpcionesDesplegado,
                        onDismissRequest = { menuOpcionesDesplegado = false },
                        modifier = Modifier.widthIn(min = 210.dp)
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Ordenar por...", color = TextoPrincipal) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                mostrarDialogoOrdenacion = true
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Tune, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Filtrar por tipo...", color = TextoPrincipal) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                mostrarDialogoFiltros = true
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = if (soloFavoritos) Ambar else ColorIconosInternos,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            text = {
                                Text(
                                    if (soloFavoritos) "Ver todas las cuentas" else "Solo favoritos",
                                    color = if (soloFavoritos) Ambar else TextoPrincipal
                                )
                            },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                vm.alternarSoloFavoritos()
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Tune, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                            },
                            text = {
                                Text(
                                    if (ajustes.agruparPorSitio) "Desagrupar cuentas" else "Agrupar cuentas",
                                    color = TextoPrincipal
                                )
                            },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                vm.ir(Pantalla.OrganizacionLista("03.5.1"))
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Tune, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                            },
                            text = {
                                Text(
                                    if (ajustes.mostrarIndicadoresContenido) "Esconder indicadores" else "Mostrar indicadores",
                                    color = TextoPrincipal
                                )
                            },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                vm.ir(Pantalla.OrganizacionLista("03.5.2"))
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.FileDownload, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Exportación selectiva", color = TextoPrincipal) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                vm.ir(Pantalla.ExportarSelectivo("todos"))
                            }
                        )
                        if (filtro != null || soloFavoritos || filtroEtiqueta != null) {
                            SeparadorOpcionMenu()
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(Icons.Filled.Close, contentDescription = null, tint = Peligro, modifier = Modifier.size(20.dp))
                                },
                                text = { Text("Restablecer filtros", color = Peligro) },
                                onClick = {
                                    menuOpcionesDesplegado = false
                                    haptica.tic()
                                    vm.filtrarPorTipo(null)
                                    if (soloFavoritos) vm.alternarSoloFavoritos()
                                    vm.filtrarPorEtiqueta(null)
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.width(6.dp))

                // Botón Bloquear
                IconButton(
                    onClick = { haptica.toque(); vm.bloquear() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Peligro.copy(alpha = 0.12f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Bloquear bóveda",
                        tint = Peligro,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Campo de Búsqueda Animado (One UI Expandible)
            androidx.compose.animation.AnimatedVisibility(
                visible = busquedaVisible || busqueda.isNotBlank(),
                enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    if (entradas.isEmpty()) {
                        IlustracionVacio()
                    } else {
                        Text(
                            "Nada coincide con esa búsqueda",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextoSecundario,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                val expandidoEnLista: (String) -> Boolean = { clave ->
                    modoSeleccion || busqueda.isNotBlank() || gruposExpandidos.contains(clave)
                }
                val itemsAMostrar = remember(visibles, modoSeleccion, criterioOrdenacion, ajustes.agruparPorSitio) {
                    com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorSitio(
                        entradas = visibles,
                        criterio = criterioOrdenacion,
                        agrupar = ajustes.agruparPorSitio,
                        expandido = { false }
                    )
                }

                val estadoLista = rememberLazyListState()
                val mostrarIndice = ajustes.mostrarIndiceAlfabetico &&
                    itemsAMostrar.size >= 5 &&
                    criterioOrdenacion == CriterioOrdenacion.NOMBRE_AZ

                var letraArrastrada by remember { mutableStateOf<Char?>(null) }

                fun itemCoincideConLetra(item: com.jlnavas3.bovedalocal.util.ItemAgrupado, letra: Char?, incluirEnie: Boolean): Boolean {
                    if (letra == null) return false
                    val titulo = when (item) {
                        is com.jlnavas3.bovedalocal.util.ItemAgrupado.Suelto -> item.entrada.titulo
                        is com.jlnavas3.bovedalocal.util.ItemAgrupado.Grupo -> item.clave.removePrefix("www.")
                        is com.jlnavas3.bovedalocal.util.ItemAgrupado.Hijo -> item.entrada.titulo
                    }
                    return com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice(titulo, incluirEnie) == letra
                }

                val primerIndiceCoincidente = remember(itemsAMostrar, letraArrastrada, ajustes.indiceIncluirEnie, ajustes.indiceResaltarEntradas, ajustes.indiceResaltarSoloPrimera) {
                    if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) null
                    else if (ajustes.indiceResaltarSoloPrimera) {
                        itemsAMostrar.indexOfFirst { itemCoincideConLetra(it, letraArrastrada, ajustes.indiceIncluirEnie) }.takeIf { it >= 0 }
                    } else null
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = estadoLista,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            start = 20.dp,
                            end = if (mostrarIndice) 36.dp else 20.dp,
                            bottom = 110.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
                    ) {
                        itemsIndexed(itemsAMostrar, key = { _, item ->
                            when (item) {
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Suelto -> item.entrada.id
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Grupo -> "grupo-${item.clave}"
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Hijo -> "hijo-${item.entrada.id}"
                            }
                        }) { indice, item ->
                            val coincideLetra = if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) {
                                false
                            } else if (ajustes.indiceResaltarSoloPrimera) {
                                indice == primerIndiceCoincidente
                            } else {
                                itemCoincideConLetra(item, letraArrastrada, ajustes.indiceIncluirEnie)
                            }
                            when (item) {
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Grupo -> ComponenteGrupoLista(
                                    clave = item.clave,
                                    entradas = item.entradas,
                                    expandido = expandidoEnLista(item.clave),
                                    alturaFila = densidadAltura,
                                    tamanoMonograma = densidadMonograma,
                                    resaltado = coincideLetra,
                                    alAlternar = {
                                        haptica.tic()
                                        gruposExpandidos = if (gruposExpandidos.contains(item.clave)) {
                                            gruposExpandidos - item.clave
                                        } else {
                                            gruposExpandidos + item.clave
                                        }
                                    },
                                    contenidoEntrada = { entradaHija, indiceHijo, totalHijos ->
                                        val coincideLetraHijo = if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) {
                                            false
                                        } else {
                                            com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice(entradaHija.titulo, ajustes.indiceIncluirEnie) == letraArrastrada
                                        }
                                        FilaEntrada(
                                            entrada = entradaHija,
                                            seleccionActiva = modoSeleccion,
                                            seleccionado = seleccionados.contains(entradaHija.id),
                                            alAbrir = { vm.ir(Pantalla.Detalle(entradaHija.id)) },
                                            alCopiarUsuario = {
                                                haptica.toque()
                                                vm.copiar("Usuario", entradaHija.usuario, sensible = false)
                                            },
                                            alCopiarContrasena = {
                                                haptica.exito()
                                                vm.copiar("Contraseña", entradaHija.contrasena, sensible = true)
                                            },
                                            alFavorito = { haptica.tic(); vm.alternarFavorito(entradaHija.id) },
                                            alCopiarCodigo = { codigo ->
                                                haptica.exito()
                                                vm.copiar("Código", codigo, sensible = true)
                                            },
                                            alPulsarLargo = { entrarEnSeleccion(entradaHija.id) },
                                            alAlternarSeleccion = { alternarSeleccion(entradaHija.id) },
                                            alturaFila = densidadAltura,
                                            tamanoMonograma = densidadMonograma,
                                            resaltado = coincideLetraHijo,
                                            separarDigitosTotp = ajustes.totpSepararDigitos,
                                            mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                            enGrupo = true,
                                            esUltimoEnGrupo = indiceHijo == totalHijos - 1
                                        )
                                    }
                                )
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Suelto -> FilaEntrada(
                                    entrada = item.entrada,
                                    seleccionActiva = modoSeleccion,
                                    seleccionado = seleccionados.contains(item.entrada.id),
                                    alAbrir = { vm.ir(Pantalla.Detalle(item.entrada.id)) },
                                    alCopiarUsuario = {
                                        haptica.toque()
                                        vm.copiar("Usuario", item.entrada.usuario, sensible = false)
                                    },
                                    alCopiarContrasena = {
                                        haptica.exito()
                                        vm.copiar("Contraseña", item.entrada.contrasena, sensible = true)
                                    },
                                    alFavorito = { haptica.tic(); vm.alternarFavorito(item.entrada.id) },
                                    alCopiarCodigo = { codigo ->
                                        haptica.exito()
                                        vm.copiar("Código", codigo, sensible = true)
                                    },
                                    alPulsarLargo = { entrarEnSeleccion(item.entrada.id) },
                                    alAlternarSeleccion = { alternarSeleccion(item.entrada.id) },
                                    alturaFila = densidadAltura,
                                    tamanoMonograma = densidadMonograma,
                                    resaltado = coincideLetra,
                                    separarDigitosTotp = ajustes.totpSepararDigitos,
                                    mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                    enGrupo = false
                                )
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Hijo -> Unit
                            }
                        }
                    }

                    if (mostrarIndice) {
                        IndiceAlfabetico(
                            alSeleccionarLetra = { letra ->
                                val indice = encontrarIndiceParaLetra(itemsAMostrar, letra, ajustes.indiceIncluirEnie)
                                if (indice != null && indice in itemsAMostrar.indices) {
                                    ambitoCorutina.launch {
                                        estadoLista.scrollToItem(indice)
                                    }
                                }
                            },
                            alCambiarLetraActiva = { letraArrastrada = it },
                            incluirEnie = ajustes.indiceIncluirEnie,
                            efectoOla = ajustes.indiceEfectoOla,
                            amplitudOlaDp = ajustes.indiceAmplitudOlaDp,
                            radioOlaDp = ajustes.indiceRadioOlaDp,
                            escalaMaximaLetras = ajustes.indiceEscalaLetras,
                            mostrarCirculo = ajustes.indiceMostrarCirculo,
                            tamanoCirculoDp = ajustes.indiceTamanoCirculoDp,
                            offsetCirculoDp = ajustes.indiceOffsetCirculoDp,
                            hapticaActiva = ajustes.indiceHaptica,
                            anchoZonaTactilDp = ajustes.indiceAnchoTactilDp,
                            tonoLetras = ajustes.indiceTonoLetras,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(top = 4.dp, bottom = 100.dp)
                        )
                    }
                }
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

    val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
    val colorDialogo = if (esOscuro) Color(0xFF212023) else Color(0xFFFFFFFF)

    if (dialogoBorrarSeleccion) {
        AlertDialog(
            onDismissRequest = { dialogoBorrarSeleccion = false },
            containerColor = colorDialogo,
            tonalElevation = 0.dp,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
            title = { Text("¿Mover ${seleccionados.size} entradas a la papelera?", color = TextoPrincipal) },
            text = { Text("Se pueden restaurar desde la papelera durante 30 días.", color = TextoSecundario) },
            confirmButton = {
                TextButton(onClick = {
                    dialogoBorrarSeleccion = false
                    val idsABorrar = seleccionados
                    salirDeSeleccion()
                    vm.eliminarVarias(idsABorrar)
                }) { Text("Mover a la papelera", color = Peligro) }
            },
            dismissButton = {
                TextButton(onClick = { dialogoBorrarSeleccion = false }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }

    if (dialogoRenombrarSeleccion) {
        AlertDialog(
            onDismissRequest = { dialogoRenombrarSeleccion = false },
            containerColor = colorDialogo,
            tonalElevation = 0.dp,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
            icon = {
                Icon(Icons.Filled.Edit, contentDescription = null, tint = Ambar)
            },
            title = {
                Text(
                    if (seleccionados.size == 1) "Renombrar título"
                    else "Renombrar título (${seleccionados.size} seleccionadas)",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    Text(
                        "Introduce el nuevo título para ${if (seleccionados.size == 1) "la entrada seleccionada" else "las ${seleccionados.size} entradas seleccionadas"}:",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto(
                        valor = textoNuevoTitulo,
                        etiqueta = "Nuevo título",
                        alCambiar = { textoNuevoTitulo = it },
                        capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onDone = {
                                val nuevo = textoNuevoTitulo.trim()
                                if (nuevo.isNotBlank()) {
                                    dialogoRenombrarSeleccion = false
                                    val idsARenombrar = seleccionados
                                    salirDeSeleccion()
                                    vm.renombrarVarias(idsARenombrar, nuevo)
                                    haptica.exito()
                                }
                            }
                        ),
                        botonLimpiar = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val nuevo = textoNuevoTitulo.trim()
                        if (nuevo.isNotBlank()) {
                            dialogoRenombrarSeleccion = false
                            val idsARenombrar = seleccionados
                            salirDeSeleccion()
                            vm.renombrarVarias(idsARenombrar, nuevo)
                            haptica.exito()
                        }
                    },
                    enabled = textoNuevoTitulo.isNotBlank()
                ) {
                    Text("Renombrar", color = if (textoNuevoTitulo.isNotBlank()) Ambar else TextoSecundario)
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogoRenombrarSeleccion = false }) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }
}
