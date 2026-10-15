/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.herramientas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import com.jlnavas3.bovedalocal.ui.scroll.rememberLazyListStatePersistente
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.Wordlist
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CerrarTecladoAlHacerScroll
import com.jlnavas3.bovedalocal.ui.componentes.EstadoVacioPantalla
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario.BotonPestanaIdioma
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario.CabeceraLetraDiccionario
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario.ChipFiltroDiccionario
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario.DialogoEditorPalabra
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario.DialogoEliminarPalabra
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario.DialogoRestablecerDiccionario
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario.FilaPalabraDiccionario
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario.IndiceAlfabeticoPalabras
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario.TarjetaEntropiaDiccionario
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale

@Composable
fun PantallaDiccionarioPalabras(
    vm: VaultViewModel,
    idiomaInicial: String = "es",
    seccionDestino: String? = null,
    alVolverManual: (() -> Unit)? = null
) {
    BackHandler(enabled = alVolverManual != null) {
        alVolverManual?.invoke()
    }
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var idiomaSeleccionado by remember(idiomaInicial) { mutableStateOf(if (idiomaInicial.equals("en", true)) "en" else "es") }
    var queryBusqueda by remember { mutableStateOf("") }
    var busquedaActiva by remember { mutableStateOf(false) }
    // 0: Todas, 1: Añadidas
    var filtroSeleccionado by remember { mutableIntStateOf(0) }

    // Diálogos
    var mostrarDialogoAnadir by remember { mutableStateOf(false) }
    var palabraAEditar by remember { mutableStateOf<String?>(null) }
    var palabraAEliminar by remember { mutableStateOf<String?>(null) }
    var mostrarDialogoRestablecer by remember { mutableStateOf(false) }

    val anadidas = remember(idiomaSeleccionado, ajustes.diccionarioAnadidasEs, ajustes.diccionarioAnadidasEn) {
        if (idiomaSeleccionado == "en") ajustes.diccionarioAnadidasEn else ajustes.diccionarioAnadidasEs
    }
    val excluidas = remember(idiomaSeleccionado, ajustes.diccionarioExcluidasEs, ajustes.diccionarioExcluidasEn) {
        if (idiomaSeleccionado == "en") ajustes.diccionarioExcluidasEn else ajustes.diccionarioExcluidasEs
    }

    val palabrasEfectivas = remember(idiomaSeleccionado, anadidas, excluidas) {
        Wordlist.obtenerPalabrasEfectivas(idiomaSeleccionado, anadidas, excluidas)
    }

    val entropiaPorPalabra = remember(palabrasEfectivas.size) {
        Wordlist.calcularEntropiaPorPalabra(palabrasEfectivas.size)
    }

    val palabrasFiltradas = remember(palabrasEfectivas, queryBusqueda, filtroSeleccionado, anadidas) {
        val q = queryBusqueda.trim().lowercase(Locale.ROOT)
        palabrasEfectivas.filter { palabra ->
            val coincideFiltro = when (filtroSeleccionado) {
                1 -> anadidas.contains(palabra)
                else -> true
            }
            if (!coincideFiltro) return@filter false
            if (q.isEmpty()) true else palabra.contains(q)
        }
    }

    val gruposAlfabeticos = remember(palabrasFiltradas) {
        palabrasFiltradas.sorted().groupBy { it.firstOrNull()?.uppercaseChar() ?: '#' }
    }

    val mapaIndices = remember(gruposAlfabeticos) {
        val mapa = mutableMapOf<Char, Int>()
        var indiceActual = 0
        gruposAlfabeticos.forEach { (letra, palabrasDeLetra) ->
            mapa[letra] = indiceActual
            indiceActual += 1 + palabrasDeLetra.size
        }
        mapa
    }

    val estadoLista = rememberLazyListStatePersistente("diccionario_palabras", vm.gestorScroll)
    CerrarTecladoAlHacerScroll(estadoLista.isScrollInProgress)

    var letraArrastrada by remember { mutableStateOf<Char?>(null) }

    val formaBorde = RoundedCornerShape(CurvaturaEsquinas)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
            .cerrarTecladoAlTocarFuera()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (busquedaActiva) {
                BarraBusquedaAnimada(
                    valor = queryBusqueda,
                    alCambiar = { queryBusqueda = it },
                    alCerrar = {
                        busquedaActiva = false
                        queryBusqueda = ""
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            } else {
                BarraSuperiorPantalla(
                    titulo = "Diccionario Diceware",
                    idEtiqueta = "04-HER-DIC",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alVolver = { alVolverManual?.invoke() ?: vm.volverAtras() },
                    colorFondo = ColorAjustesFondo,
                    acciones = {
                        IconButton(onClick = {
                            haptica.toque()
                            busquedaActiva = true
                        }) {
                            Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = TextoPrincipal)
                        }
                        IconButton(onClick = {
                            haptica.toque()
                            mostrarDialogoRestablecer = true
                        }) {
                            Icon(Icons.Filled.RestartAlt, contentDescription = "Restablecer", tint = TextoSecundario)
                        }
                    }
                )
            }

            // Selector de idioma (Español / English)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BotonPestanaIdioma(
                    titulo = "Español",
                    subtitulo = "${Wordlist.obtenerPalabrasEfectivas("es", ajustes.diccionarioAnadidasEs, ajustes.diccionarioExcluidasEs).size} palabras",
                    seleccionado = idiomaSeleccionado == "es",
                    alSeleccionar = {
                        haptica.toque()
                        idiomaSeleccionado = "es"
                    },
                    modifier = Modifier.weight(1f)
                )
                BotonPestanaIdioma(
                    titulo = "English",
                    subtitulo = "${Wordlist.obtenerPalabrasEfectivas("en", ajustes.diccionarioAnadidasEn, ajustes.diccionarioExcluidasEn).size} words",
                    seleccionado = idiomaSeleccionado == "en",
                    alSeleccionar = {
                        haptica.toque()
                        idiomaSeleccionado = "en"
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // Tarjeta de entropía Diceware desplegable (cerrada por defecto)
            TarjetaEntropiaDiccionario(
                entropiaPorPalabra = entropiaPorPalabra,
                totalActivas = palabrasEfectivas.size,
                totalExcluidas = excluidas.size,
                haptica = haptica,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            // Filtros rápidos con doble fila y cantidad centrada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipFiltroDiccionario(
                    etiqueta = "Todas",
                    cantidad = palabrasEfectivas.size,
                    seleccionado = filtroSeleccionado == 0,
                    alSeleccionar = {
                        haptica.toque()
                        filtroSeleccionado = 0
                    },
                    modifier = Modifier.weight(1f)
                )
                ChipFiltroDiccionario(
                    etiqueta = "Añadidas",
                    cantidad = anadidas.size,
                    seleccionado = filtroSeleccionado == 1,
                    alSeleccionar = {
                        haptica.toque()
                        filtroSeleccionado = 1
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // Contenedor de lista e índice alfabético lateral
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (palabrasFiltradas.isEmpty()) {
                    val (tit, sub) = when {
                        queryBusqueda.isNotBlank() -> Pair("Sin coincidencias", "No se encontraron palabras para \"$queryBusqueda\"")
                        filtroSeleccionado == 1 -> Pair("Sin personalizadas", "No has añadido palabras personalizadas aún")
                        else -> Pair("Diccionario vacío", "El diccionario está vacío")
                    }
                    EstadoVacioPantalla(
                        icono = Icons.AutoMirrored.Filled.MenuBook,
                        titulo = tit,
                        subtitulo = sub,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        state = estadoLista,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        gruposAlfabeticos.forEach { (letra, palabrasDeLetra) ->
                            item(key = "header_$letra") {
                                CabeceraLetraDiccionario(
                                    letra = letra,
                                    cantidad = palabrasDeLetra.size
                                )
                            }
                            items(palabrasDeLetra, key = { it }) { palabra ->
                                val esAnadida = anadidas.contains(palabra)
                                FilaPalabraDiccionario(
                                    palabra = palabra,
                                    esAnadida = esAnadida,
                                    alEditar = {
                                        haptica.toque()
                                        palabraAEditar = palabra
                                    },
                                    alEliminar = {
                                        haptica.toque()
                                        palabraAEliminar = palabra
                                    }
                                )
                            }
                        }
                        item(key = "espaciador_final") {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }

                // Índice lateral posicionado idénticamente a la pantalla principal
                if (ajustes.mostrarIndiceAlfabetico && palabrasFiltradas.size >= 10 && queryBusqueda.isBlank()) {
                    IndiceAlfabeticoPalabras(
                        ajustes = ajustes,
                        mapaIndices = mapaIndices,
                        estadoLista = estadoLista,
                        alCambiarLetraActiva = { letraArrastrada = it },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(top = 4.dp, bottom = 100.dp)
                    )
                }
            }
        }

        // FAB para añadir palabra
        FloatingActionButton(
            onClick = {
                haptica.toque()
                mostrarDialogoAnadir = true
            },
            containerColor = ColorAcento,
            contentColor = ColorSobreAcento,
            shape = formaBorde,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaBorde)
                    } else Modifier
                )
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Añadir palabra")
        }
    }

    // Diálogos
    if (mostrarDialogoAnadir) {
        DialogoEditorPalabra(
            titulo = "Añadir palabra al diccionario",
            textoInicial = "",
            idioma = idiomaSeleccionado,
            alConfirmar = { nuevaPalabra ->
                vm.agregarPalabraDiccionario(idiomaSeleccionado, nuevaPalabra)
                vm.avisar("Palabra añadida al diccionario", "Diccionario")
                mostrarDialogoAnadir = false
            },
            alCerrar = { mostrarDialogoAnadir = false }
        )
    }

    palabraAEditar?.let { palabraVieja ->
        DialogoEditorPalabra(
            titulo = "Modificar palabra",
            textoInicial = palabraVieja,
            idioma = idiomaSeleccionado,
            alConfirmar = { palabraNueva ->
                vm.editarPalabraDiccionario(idiomaSeleccionado, palabraVieja, palabraNueva)
                vm.avisar("Palabra actualizada", "Diccionario")
                palabraAEditar = null
            },
            alCerrar = { palabraAEditar = null }
        )
    }

    palabraAEliminar?.let { palabra ->
        DialogoEliminarPalabra(
            palabra = palabra,
            alConfirmar = {
                haptica.exito()
                vm.eliminarPalabraDiccionario(idiomaSeleccionado, palabra)
                vm.avisar("Palabra excluida del diccionario", "Diccionario")
                palabraAEliminar = null
            },
            alCerrar = { palabraAEliminar = null }
        )
    }

    if (mostrarDialogoRestablecer) {
        DialogoRestablecerDiccionario(
            idiomaSeleccionado = idiomaSeleccionado,
            alConfirmar = {
                haptica.exito()
                vm.restablecerDiccionario(idiomaSeleccionado)
                vm.avisar("Diccionario restablecido a valores de fábrica", "Diccionario")
                mostrarDialogoRestablecer = false
            },
            alCerrar = { mostrarDialogoRestablecer = false }
        )
    }
}
