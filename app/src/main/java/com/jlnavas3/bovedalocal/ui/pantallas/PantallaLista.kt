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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
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
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BannerRecordatorioExportacion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.CampoBusquedaLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltro
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
                    perfilArgon2 = vm.repositorio.perfilArgon2Actual(),
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
                IconButton(
                    onClick = { haptica.toque(); abrirMenu() },
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menú",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(42.dp)
                    )
                }
                Spacer(Modifier.width(6.dp))
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
                IconButton(
                    onClick = { haptica.toque(); vm.bloquear() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(FormaBoton)
                        .background(Peligro.copy(alpha = 0.15f))
                        .then(
                            if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                                Modifier.border(GrosorBorde, Peligro.copy(alpha = 0.4f), FormaBoton)
                            } else {
                                Modifier.border(1.dp, Peligro.copy(alpha = 0.3f), FormaBoton)
                            }
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Bloquear bóveda",
                        tint = Peligro,
                        modifier = Modifier.size(22.dp)
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
                        alSeleccionarTipo = { tipo ->
                            haptica.tic()
                            vm.filtrarPorTipo(if (filtro == tipo) null else tipo)
                        },
                        alFavoritos = {
                            haptica.tic()
                            vm.alternarSoloFavoritos()
                        },
                        alTodo = {
                            haptica.tic()
                            vm.filtrarPorTipo(null)
                            if (soloFavoritos) vm.alternarSoloFavoritos()
                        }
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
                                is com.jlnavas3.bovedalocal.util.ItemAgrupado.Grupo -> FilaGrupoSitio(
                                    clave = item.clave,
                                    cantidad = item.entradas.size,
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
                                    resaltado = coincideLetra
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
                                        tamanoMonograma = densidadMonograma,
                                        resaltado = coincideLetra
                                    )
                                }
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
