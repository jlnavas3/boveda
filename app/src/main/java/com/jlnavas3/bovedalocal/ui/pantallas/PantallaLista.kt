package com.jlnavas3.bovedalocal.ui.pantallas

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
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
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegablePepo
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
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

    // Claves "categoria|sitio" de los grupos por sitio que el usuario ha desplegado a mano.
    var gruposExpandidos by remember { mutableStateOf(setOf<String>()) }

    fun salirDeSeleccion() {
        modoSeleccion = false
        seleccionados = emptySet()
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

    ModalNavigationDrawer(
        drawerState = estadoCajon,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .then(
                        if (GrosorBorde > 0.dp) Modifier.border(
                            width = GrosorBorde,
                            color = ColorBordeDropdown,
                            shape = RoundedCornerShape(topEnd = CurvaturaEsquinas, bottomEnd = CurvaturaEsquinas)
                        ) else Modifier
                    ),
                drawerShape = RoundedCornerShape(topEnd = CurvaturaEsquinas, bottomEnd = CurvaturaEsquinas),
                drawerContainerColor = Superficie
            ) {
                MenuLateral(
                    nombreApp = ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
                    totalEntradas = entradas.size,
                    totalPapelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera?.size ?: 0,
                    alIr = { destino -> cerrarMenu(); vm.ir(destino) },
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
                    alSeleccionarTodo = { alternarSeleccionarTodo() },
                    alBorrar = { dialogoBorrarSeleccion = true }
                )
            } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { haptica.toque(); abrirMenu() }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = ColorIconosInternos)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = ColorTitulos
                    )
                    Text(
                        "${entradas.size} ${if (entradas.size == 1) "entrada" else "entradas"} cifradas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
            }
            }

            val diasSinExportar = remember(ajustes) { vm.diasSinExportar() }
            if (diasSinExportar != null) {
                Spacer(Modifier.height((EspaciadoComponentes * 0.8f).coerceAtLeast(6.dp)))
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    BannerRecordatorioExportacion(
                        dias = diasSinExportar,
                        alIr = { vm.ir(Pantalla.Ajustes) }
                    )
                }
            }

            Spacer(Modifier.height((EspaciadoComponentes * 0.8f).coerceAtLeast(6.dp)))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1.1f)) {
                    CampoBusquedaLista(valor = busqueda, alCambiar = { vm.buscar(it) })
                }
                Box(modifier = Modifier.weight(0.9f)) {
                    SelectorFiltros(
                        filtro = filtro,
                        soloFavoritos = soloFavoritos,
                        alTodo = {
                            vm.filtrarPorTipo(null)
                            if (soloFavoritos) vm.alternarSoloFavoritos()
                        },
                        alClaves = { vm.filtrarPorTipo(if (filtro == TipoEntrada.LOGIN) null else TipoEntrada.LOGIN) },
                        alPasskeys = { vm.filtrarPorTipo(if (filtro == TipoEntrada.PASSKEY) null else TipoEntrada.PASSKEY) },
                        alNotas = { vm.filtrarPorTipo(if (filtro == TipoEntrada.NOTA) null else TipoEntrada.NOTA) },
                        alFavoritos = { vm.alternarSoloFavoritos() }
                    )
                }
                SelectorOrdenacion(
                    criterio = criterioOrdenacion,
                    alCambiar = {
                        haptica.tic()
                        vm.cambiarCriterioOrdenacion(it)
                    }
                )
            }
            if (etiquetasDisponibles.isNotEmpty()) {
                Spacer(Modifier.height((EspaciadoComponentes * 0.5f).coerceAtLeast(4.dp)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    etiquetasDisponibles.forEach { etiqueta ->
                        ChipFiltro("#${normalizarEtiqueta(etiqueta)}", filtroEtiqueta == etiqueta) {
                            vm.filtrarPorEtiqueta(if (filtroEtiqueta == etiqueta) null else etiqueta)
                        }
                    }
                }
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
                    modoSeleccion || gruposExpandidos.contains(clave)
                }
                val itemsAMostrar = remember(visibles, gruposExpandidos, modoSeleccion, criterioOrdenacion, ajustes.agruparPorSitio) {
                    com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorSitio(
                        entradas = visibles,
                        criterio = criterioOrdenacion,
                        agrupar = ajustes.agruparPorSitio,
                        expandido = expandidoEnLista
                    )
                }

                val estadoLista = rememberLazyListState()
                val mostrarIndice = ajustes.mostrarIndiceAlfabetico && itemsAMostrar.size >= 5

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
                        items(itemsAMostrar, key = { item ->
                            when (item) {
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Suelto -> item.entrada.id
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Grupo -> "grupo-${item.clave}"
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Hijo -> "hijo-${item.entrada.id}"
                            }
                        }) { item ->
                            when (item) {
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Grupo -> FilaGrupoSitio(
                                    clave = item.clave,
                                    cantidad = item.entradas.size,
                                    expandido = expandidoEnLista(item.clave),
                                    alturaFila = densidadAltura,
                                    tamanoMonograma = densidadMonograma,
                                    alAlternar = {
                                        haptica.tic()
                                        gruposExpandidos = if (gruposExpandidos.contains(item.clave)) {
                                            gruposExpandidos - item.clave
                                        } else {
                                            gruposExpandidos + item.clave
                                        }
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
                                    tamanoMonograma = densidadMonograma
                                )
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Hijo -> Box(modifier = Modifier.padding(start = 16.dp)) {
                                    FilaEntrada(
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
                                        tamanoMonograma = densidadMonograma
                                    )
                                }
                            }
                        }
                    }

                    if (mostrarIndice) {
                        IndiceAlfabetico(
                            alSeleccionarLetra = { letra ->
                                val indice = encontrarIndiceParaLetra(itemsAMostrar, letra)
                                if (indice != null && indice in itemsAMostrar.indices) {
                                    ambitoCorutina.launch {
                                        estadoLista.scrollToItem(indice)
                                    }
                                }
                            },
                            efectoOla = ajustes.indiceEfectoOla,
                            amplitudOlaDp = ajustes.indiceAmplitudOlaDp,
                            radioOlaDp = ajustes.indiceRadioOlaDp,
                            escalaMaximaLetras = ajustes.indiceEscalaLetras,
                            mostrarCirculo = ajustes.indiceMostrarCirculo,
                            offsetCirculoDp = ajustes.indiceOffsetCirculoDp,
                            hapticaActiva = ajustes.indiceHaptica,
                            anchoZonaTactilDp = ajustes.indiceAnchoTactilDp,
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
            shape = FormaBoton,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .then(
                    if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                        Modifier.border(GrosorBorde, ColorBordeActual, FormaBoton)
                    } else {
                        Modifier
                    }
                )
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Nueva entrada")
        }
        }
    }
    }

    if (dialogoBorrarSeleccion) {
        AlertDialog(
            onDismissRequest = { dialogoBorrarSeleccion = false },
            containerColor = SuperficieAlta,
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
}

/**
 * Menú lateral al estilo Solid Explorer: se abre deslizando desde el borde
 * izquierdo. Cabecera con el nombre de la app, cuerpo con los accesos que antes
 * vivían en la barra superior, y en el pie la versión compilada.
 */
@Composable
private fun MenuLateral(
    nombreApp: String,
    totalEntradas: Int,
    totalPapelera: Int,
    alIr: (Pantalla) -> Unit,
    alBloquear: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 12.dp)
    ) {
        // Cabecera Premium
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(FormaCampo)
                        .background(SuperficieAlta)
                        .then(
                            if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeDropdown, FormaCampo)
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = Ambar,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        nombreApp,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = ColorTitulos,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Menta)
                        )
                        Text(
                            "$totalEntradas ${if (totalEntradas == 1) "entrada" else "entradas"}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = TextoSecundario
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorSeparadorDropdown))
        Spacer(Modifier.height(10.dp))

        // Opciones de navegación con contenedor y feedback
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ItemMenu(
                texto = "Generar contraseñas",
                icono = Icons.Filled.AutoAwesome,
                colorIcono = ColorGenerador
            ) { alIr(Pantalla.Generador) }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ItemMenu(
                    texto = "Passkeys",
                    icono = Icons.Filled.Fingerprint,
                    colorIcono = ColorPasskeys
                ) { alIr(Pantalla.Passkeys) }
            }

            ItemMenu(
                texto = "Autenticador 2FA",
                icono = Icons.Filled.Timer,
                colorIcono = Color2FA
            ) { alIr(Pantalla.Autenticador) }

            ItemMenu(
                texto = "Salud de la bóveda",
                icono = Icons.Filled.HealthAndSafety,
                colorIcono = ColorSalud
            ) { alIr(Pantalla.SaludBoveda) }

            ItemMenu(
                texto = "Papelera",
                icono = Icons.Filled.Delete,
                colorIcono = if (totalPapelera > 0) Ambar else ColorIconosInternos,
                badge = if (totalPapelera > 0) totalPapelera.toString() else null
            ) { alIr(Pantalla.Papelera) }

            ItemMenu(
                texto = "Configuración",
                icono = Icons.Filled.Settings,
                colorIcono = ColorIconosInternos
            ) { alIr(Pantalla.Ajustes) }

            Spacer(Modifier.height(6.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorSeparadorDropdown))
            Spacer(Modifier.height(6.dp))

            ItemMenu(
                texto = "Bloquear bóveda",
                icono = Icons.Filled.Lock,
                colorIcono = Peligro,
                colorTexto = Peligro
            ) { alBloquear() }
        }

        // Pie de Menú estilizado
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorSeparadorDropdown))
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Bóveda local",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorTitulos
                )
                Text(
                    "v${BuildConfig.VERSION_NAME} · 100% offline",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Menta.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    "AES-256",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Menta
                )
            }
        }
    }
}

@Composable
private fun ItemMenu(
    texto: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    colorIcono: androidx.compose.ui.graphics.Color = ColorIconosInternos,
    colorTexto: androidx.compose.ui.graphics.Color = TextoPrincipal,
    badge: String? = null,
    alPulsar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaCampo)
            .clickable { alPulsar() }
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(FormaPequena)
                .background(colorIcono.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
            color = colorTexto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Ambar.copy(alpha = 0.18f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Ambar
                )
            }
        }
    }
}

@Composable
private fun BarraSeleccion(
    cantidad: Int,
    todoSeleccionado: Boolean,
    alCancelar: () -> Unit,
    alSeleccionarTodo: () -> Unit,
    alBorrar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = alCancelar) {
            Icon(Icons.Filled.Close, contentDescription = "Cancelar selección", tint = TextoPrincipal)
        }
        Text(
            "$cantidad ${if (cantidad == 1) "seleccionada" else "seleccionadas"}",
            style = MaterialTheme.typography.titleMedium,
            color = TextoPrincipal,
            modifier = Modifier.weight(1f).padding(start = 4.dp)
        )
        IconButton(onClick = alSeleccionarTodo) {
            Icon(
                Icons.Filled.SelectAll,
                contentDescription = if (todoSeleccionado) "Deseleccionar todo" else "Seleccionar todo",
                tint = if (todoSeleccionado) Ambar else TextoPrincipal
            )
        }
        IconButton(onClick = alBorrar, enabled = cantidad > 0) {
            Icon(Icons.Filled.Delete, contentDescription = "Borrar seleccionadas", tint = Peligro)
        }
    }
}

@Composable
private fun BannerRecordatorioExportacion(dias: Long, alIr: () -> Unit) {
    val forma = FormaTarjeta
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
            .clickable { alIr() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Hace $dias días que no exportas una copia",
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Toca para ir a Ajustes > Copia de seguridad",
                color = TextoSecundario,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ChipFiltro(texto: String, activo: Boolean, alPulsar: () -> Unit) {
    val forma = FormaPequena
    Box(
        modifier = Modifier
            .clip(forma)
            .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(Superficie, Superficie)))
            .then(
                if (!activo && GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
            .clickable { alPulsar() }
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text(
            texto,
            color = if (activo) ColorSobreAcento else TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun CampoBusquedaLista(valor: String, alCambiar: (String) -> Unit) {
    val forma = FormaCampo
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(forma)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, if (valor.isBlank()) ColorBordeActual else Ambar, forma)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = if (valor.isBlank()) TextoSecundario else Ambar, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value = valor,
            onValueChange = alCambiar,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextoPrincipal),
            cursorBrush = SolidColor(Ambar),
            modifier = Modifier.weight(1f)
        )
        if (valor.isNotBlank()) {
            IconButton(
                onClick = { alCambiar("") },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Limpiar búsqueda",
                    tint = TextoSecundario,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun SelectorFiltros(
    filtro: TipoEntrada?,
    soloFavoritos: Boolean,
    alTodo: () -> Unit,
    alClaves: () -> Unit,
    alPasskeys: () -> Unit,
    alNotas: () -> Unit,
    alFavoritos: () -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    val etiqueta = when {
        filtro == null && !soloFavoritos -> "Todo"
        filtro == TipoEntrada.LOGIN && soloFavoritos -> "Claves + ★"
        filtro == TipoEntrada.PASSKEY && soloFavoritos -> "Passkeys + ★"
        filtro == TipoEntrada.NOTA && soloFavoritos -> "Notas + ★"
        filtro == TipoEntrada.LOGIN -> "Claves"
        filtro == TipoEntrada.PASSKEY -> "Passkeys"
        filtro == TipoEntrada.NOTA -> "Notas"
        soloFavoritos -> "Favoritos"
        else -> "Filtros"
    }
    val forma = FormaCampo

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp) {
                        Modifier.border(GrosorBorde, if (desplegado || filtro != null || soloFavoritos) Ambar else ColorBordeActual, forma)
                    } else {
                        Modifier
                    }
                )
                .clickable { desplegado = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Tune,
                contentDescription = null,
                tint = if (desplegado || filtro != null || soloFavoritos) Ambar else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(etiqueta, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium, maxLines = 1, modifier = Modifier.weight(1f))
            Icon(
                if (desplegado) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir filtros",
                tint = TextoSecundario
            )
        }

        MenuDesplegablePepo(
            expanded = desplegado,
            onDismissRequest = { desplegado = false },
            modifier = Modifier.widthIn(min = 170.dp)
        ) {
            OpcionFiltro("Todo", Icons.Filled.SelectAll, filtro == null && !soloFavoritos) { alTodo(); desplegado = false }
            SeparadorOpcionMenu()
            OpcionFiltro("Claves", Icons.Filled.Lock, filtro == TipoEntrada.LOGIN) { alClaves(); desplegado = false }
            SeparadorOpcionMenu()
            OpcionFiltro("Passkeys", Icons.Filled.Fingerprint, filtro == TipoEntrada.PASSKEY) { alPasskeys(); desplegado = false }
            SeparadorOpcionMenu()
            OpcionFiltro("Notas", Icons.Filled.Menu, filtro == TipoEntrada.NOTA) { alNotas(); desplegado = false }
            SeparadorOpcionMenu()
            OpcionFiltro("Favoritos", Icons.Filled.Star, soloFavoritos) { alFavoritos(); desplegado = false }
        }
    }
}

@Composable
private fun OpcionFiltro(texto: String, icono: ImageVector, activo: Boolean, alPulsar: () -> Unit) {
    DropdownMenuItem(
        leadingIcon = {
            Icon(
                icono,
                contentDescription = null,
                tint = if (activo) ColorAcento else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        },
        text = {
            Text(
                texto,
                color = if (activo) ColorAcento else TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingIcon = {
            if (activo) Icon(Icons.Filled.Check, contentDescription = null, tint = ColorAcento, modifier = Modifier.size(18.dp))
        },
        onClick = alPulsar
    )
}

@Composable
private fun SelectorOrdenacion(
    criterio: CriterioOrdenacion,
    alCambiar: (CriterioOrdenacion) -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    val forma = FormaCampo

    Box {
        Row(
            modifier = Modifier
                .height(46.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp) {
                        Modifier.border(GrosorBorde, if (desplegado) ColorAcento else ColorBordeActual, forma)
                    } else {
                        Modifier
                    }
                )
                .clickable { desplegado = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Ordenar lista",
                tint = if (desplegado) ColorAcento else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }

        MenuDesplegablePepo(
            expanded = desplegado,
            onDismissRequest = { desplegado = false }
        ) {
            CriterioOrdenacion.entries.forEachIndexed { index, op ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val activo = op == criterio
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            tint = if (activo) ColorAcento else TextoSecundario
                        )
                    },
                    trailingIcon = if (activo) {
                        { Icon(Icons.Filled.Check, contentDescription = null, tint = ColorAcento, modifier = Modifier.size(18.dp)) }
                    } else null,
                    text = {
                        Text(
                            op.etiqueta,
                            color = if (activo) ColorTitulos else TextoPrincipal,
                            fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        alCambiar(op)
                        desplegado = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FilaGrupoSitio(
    clave: String,
    cantidad: Int,
    expandido: Boolean,
    alturaFila: androidx.compose.ui.unit.Dp = 74.dp,
    tamanoMonograma: Int = 46,
    alAlternar: () -> Unit
) {
    val forma = FormaTarjeta
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(alturaFila)
            .clip(forma)
            .background(ColorTarjetas)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
            .clickable { alAlternar() }
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Monograma(titulo = clave.ifBlank { "?" }, semilla = clave, tamano = tamanoMonograma)
        Spacer(Modifier.width(if (alturaFila.value <= 48f) 10.dp else 14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                clave,
                style = if (alturaFila.value <= 48f) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                color = TextoPrincipal,
                maxLines = 1
            )
            Text(
                "$cantidad ${if (cantidad == 1) "cuenta" else "cuentas"}",
                style = if (alturaFila.value <= 48f) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
        }
        Icon(
            if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expandido) "Contraer" else "Expandir",
            tint = ColorIconosInternos
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun FilaEntrada(
    entrada: Entrada,
    seleccionActiva: Boolean,
    seleccionado: Boolean,
    alAbrir: () -> Unit,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit,
    alFavorito: () -> Unit,
    alCopiarCodigo: (String) -> Unit = {},
    alPulsarLargo: () -> Unit,
    alAlternarSeleccion: () -> Unit,
    alturaFila: androidx.compose.ui.unit.Dp = 74.dp,
    tamanoMonograma: Int = 46
) {
    val compacta = alturaFila.value <= 48f
    val forma = FormaTarjeta
    val contenidoFila: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaFila)
                .clip(forma)
                .background(if (seleccionado) Ambar.copy(alpha = 0.22f) else ColorTarjetas)
                .then(
                    if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else {
                        Modifier
                    }
                )
                .combinedClickable(
                    onClick = { if (seleccionActiva) alAlternarSeleccion() else alAbrir() },
                    onLongClick = { if (!seleccionActiva) alPulsarLargo() }
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (seleccionActiva) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (seleccionado) Ambar else Borde),
                    contentAlignment = Alignment.Center
                ) {
                    if (seleccionado) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = ColorSobreAcento, modifier = Modifier.size(if (compacta) 16.dp else 18.dp))
                    }
                }
            } else {
                Monograma(
                    titulo = entrada.titulo.ifBlank { "?" },
                    semilla = entrada.urls.firstOrNull() ?: entrada.passkey?.rpId ?: entrada.titulo,
                    tamano = tamanoMonograma
                )
            }
            Spacer(Modifier.width(if (compacta) 10.dp else 14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entrada.titulo.ifBlank { "Sin título" },
                    style = if (compacta) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                    color = TextoPrincipal,
                    maxLines = 1
                )
                Text(
                    when (entrada.tipo) {
                        TipoEntrada.PASSKEY -> "Passkey · ${entrada.passkey?.rpId ?: ""}"
                        TipoEntrada.NOTA -> "Nota segura"
                        TipoEntrada.LOGIN -> entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" }
                    },
                    style = if (compacta) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    maxLines = 1
                )
            }
            val secreto = entrada.secretoTotp
            if (!seleccionActiva && !secreto.isNullOrBlank()) {
                var ahora by remember { mutableStateOf(System.currentTimeMillis() / 1000) }
                LaunchedEffect(secreto) {
                    while (true) {
                        ahora = System.currentTimeMillis() / 1000
                        kotlinx.coroutines.delay(1000)
                    }
                }
                val periodo = entrada.totpPeriodo.toLong()
                val codigo = remember(ahora / periodo, secreto) {
                    try {
                        Totp.codigo(
                            secreto = Base32.decodificar(secreto),
                            segundosUnix = ahora,
                            digitos = entrada.totpDigitos,
                            periodo = periodo
                        )
                    } catch (e: Exception) {
                        "------"
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { alCopiarCodigo(codigo) }.padding(horizontal = 4.dp)
                ) {
                    Text(codigo, style = EstiloMono, color = ColorTitulos)
                    Spacer(Modifier.width(if (compacta) 4.dp else 8.dp))
                    // 34dp es el minimo en el que el numero de dentro se lee de un vistazo.
                    AnilloTotp(
                        codigo = "",
                        segundosRestantes = Totp.segundosRestantes(ahora, periodo),
                        tamano = if (compacta) 28 else 34,
                        periodo = periodo
                    )
                }
            }
            if (!seleccionActiva) {
                IconButton(onClick = alFavorito, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Favorito",
                        tint = if (entrada.favorito) Ambar else ColorBordeActual.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }

    // El deslizar para copiar solo tiene sentido fuera del modo de selección: si no,
    // un swipe accidental copiaría una contraseña mientras se intenta marcar varias.
    if (seleccionActiva) {
        contenidoFila()
    } else {
        val contexto = LocalContext.current
        val haptica = remember { Haptica(contexto) }
        val scope = rememberCoroutineScope()
        val animOffset = remember { Animatable(0f) }
        var anchoFilaPx by remember { mutableFloatStateOf(0f) }
        var dioHapticaTope by remember { mutableStateOf(false) }

        LaunchedEffect(entrada.id) {
            animOffset.snapTo(0f)
        }

        val topeMaximo = anchoFilaPx * 0.45f
        val topeActual by rememberUpdatedState(topeMaximo)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaFila)
                .onSizeChanged { anchoFilaPx = it.width.toFloat() }
        ) {
            val offsetActual = animOffset.value
            val limite = topeActual
            val progreso = if (limite > 0f) (abs(offsetActual) / limite).coerceIn(0f, 1f) else 0f
            val escala = 0.85f + 0.20f * progreso
            val opacidad = 0.4f + 0.6f * progreso

            // Fondo dinámico: solo se dibuja y visualiza el lado correspondiente al deslizamiento activo
            if (offsetActual > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(forma)
                        .background(Menta.copy(alpha = 0.14f + 0.10f * progreso))
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.scale(escala)
                    ) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            tint = Menta.copy(alpha = opacidad),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Copiar usuario",
                            color = Menta.copy(alpha = opacidad),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else if (offsetActual < 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(forma)
                        .background(Ambar.copy(alpha = 0.14f + 0.10f * progreso))
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.scale(escala)
                    ) {
                        Text(
                            "Copiar contraseña",
                            color = Ambar.copy(alpha = opacidad),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            Icons.Filled.Key,
                            contentDescription = null,
                            tint = Ambar.copy(alpha = opacidad),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Tarjeta superior con tope estricto al 45% y animación de retorno
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(animOffset.value.roundToInt(), 0) }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = {
                                dioHapticaTope = false
                            },
                            onDragEnd = {
                                val maximo = topeActual
                                if (maximo > 0f) {
                                    val alcanzado = abs(animOffset.value) >= maximo * 0.94f
                                    if (alcanzado) {
                                        if (animOffset.value > 0f) {
                                            alCopiarUsuario()
                                        } else {
                                            alCopiarContrasena()
                                        }
                                    }
                                }
                                dioHapticaTope = false
                                scope.launch {
                                    animOffset.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            },
                            onDragCancel = {
                                dioHapticaTope = false
                                scope.launch {
                                    animOffset.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                val maximo = topeActual
                                if (maximo > 0f) {
                                    change.consume()
                                    val nuevoOffset = (animOffset.value + dragAmount).coerceIn(-maximo, maximo)
                                    scope.launch { animOffset.snapTo(nuevoOffset) }

                                    val enTope = abs(nuevoOffset) >= maximo * 0.96f
                                    if (enTope && !dioHapticaTope) {
                                        haptica.tic()
                                        dioHapticaTope = true
                                    } else if (!enTope && dioHapticaTope) {
                                        dioHapticaTope = false
                                    }
                                }
                            }
                        )
                    }
            ) {
                contenidoFila()
            }
        }
    }
}

