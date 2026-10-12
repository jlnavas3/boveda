/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
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
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CerrarTecladoAlHacerScroll
import com.jlnavas3.bovedalocal.ui.componentes.EstadoVacioPantalla
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.comunes.CabeceraLetraComunes
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.comunes.ChipFiltroComunes
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.comunes.DialogoEditorContrasenaComun
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.comunes.DialogoEliminarContrasenaComun
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.comunes.DialogoRestablecerContrasenasComunes
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.comunes.FilaContrasenaComun
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.comunes.TarjetaInformativaComunes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale

@Composable
fun PantallaContrasenasComunes(
    vm: VaultViewModel,
    seccionDestino: String? = null,
    alVolverManual: (() -> Unit)? = null
) {
    BackHandler(enabled = alVolverManual != null) {
        alVolverManual?.invoke()
    }
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var queryBusqueda by remember { mutableStateOf("") }
    var busquedaActiva by remember { mutableStateOf(false) }
    // 0: Todas, 1: Añadidas
    var filtroSeleccionado by remember { mutableIntStateOf(0) }

    // Diálogos
    var mostrarDialogoAnadir by remember { mutableStateOf(false) }
    var claveAEliminar by remember { mutableStateOf<String?>(null) }
    var mostrarDialogoRestablecer by remember { mutableStateOf(false) }

    val anadidas = ajustes.contrasenasComunesAnadidas
    val excluidas = ajustes.contrasenasComunesExcluidas

    val listaCompleta = remember(anadidas, excluidas) {
        ContrasenasComunes.obtenerListaCompleta(contexto, anadidas, excluidas)
    }

    val listaFiltrada = remember(listaCompleta, queryBusqueda, filtroSeleccionado, anadidas) {
        val q = queryBusqueda.trim().lowercase(Locale.ROOT)
        listaCompleta.filter { item ->
            val coincideFiltro = when (filtroSeleccionado) {
                1 -> anadidas.contains(item)
                else -> true
            }
            if (!coincideFiltro) return@filter false
            if (q.isEmpty()) true else item.lowercase(Locale.ROOT).contains(q)
        }
    }

    val gruposAlfabeticos = remember(listaFiltrada) {
        listaFiltrada.sorted().groupBy { it.firstOrNull()?.uppercaseChar() ?: '#' }
    }

    val estadoLista = rememberLazyListState()
    CerrarTecladoAlHacerScroll(estadoLista.isScrollInProgress)

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
                    titulo = "Contraseñas comunes",
                    idEtiqueta = "01-SEG-COM",
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

            // Tarjeta informativa colapsable
            TarjetaInformativaComunes(
                totalActivas = listaCompleta.size,
                totalAnadidas = anadidas.size,
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
                ChipFiltroComunes(
                    etiqueta = "Todas",
                    cantidad = listaCompleta.size,
                    seleccionado = filtroSeleccionado == 0,
                    alSeleccionar = {
                        haptica.toque()
                        filtroSeleccionado = 0
                    },
                    modifier = Modifier.weight(1f)
                )
                ChipFiltroComunes(
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

            // Lista de contraseñas agrupadas alfabéticamente
            if (listaFiltrada.isEmpty()) {
                val (tit, sub) = when {
                    queryBusqueda.isNotBlank() -> Pair("Sin coincidencias", "No se encontraron contraseñas para \"$queryBusqueda\"")
                    filtroSeleccionado == 1 -> Pair("Sin personalizadas", "No has añadido contraseñas comunes personalizadas aún")
                    else -> Pair("Lista vacía", "No hay contraseñas en la lista")
                }
                EstadoVacioPantalla(
                    icono = Icons.Filled.Security,
                    titulo = tit,
                    subtitulo = sub,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    state = estadoLista,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    gruposAlfabeticos.forEach { (letra, clavesDeLetra) ->
                        item(key = "header_$letra") {
                            CabeceraLetraComunes(
                                letra = letra,
                                cantidad = clavesDeLetra.size
                            )
                        }
                        items(clavesDeLetra, key = { it }) { contrasena ->
                            val esAnadida = anadidas.contains(contrasena)
                            FilaContrasenaComun(
                                contrasena = contrasena,
                                esAnadida = esAnadida,
                                alEliminar = {
                                    haptica.toque()
                                    claveAEliminar = contrasena
                                }
                            )
                        }
                    }
                    item(key = "espaciador_final") {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // FAB para añadir contraseña común
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
            Icon(Icons.Filled.Add, contentDescription = "Añadir contraseña común")
        }
    }

    // Diálogos
    if (mostrarDialogoAnadir) {
        DialogoEditorContrasenaComun(
            alConfirmar = { nuevaClave ->
                vm.agregarContrasenaComun(nuevaClave)
                vm.avisar("Contraseña añadida al catálogo común", "Seguridad")
                mostrarDialogoAnadir = false
            },
            alCerrar = { mostrarDialogoAnadir = false }
        )
    }

    claveAEliminar?.let { clave ->
        DialogoEliminarContrasenaComun(
            clave = clave,
            alConfirmar = {
                haptica.exito()
                vm.eliminarContrasenaComun(clave)
                vm.avisar("Contraseña excluida de la lista común", "Seguridad")
                claveAEliminar = null
            },
            alCerrar = { claveAEliminar = null }
        )
    }

    if (mostrarDialogoRestablecer) {
        DialogoRestablecerContrasenasComunes(
            alConfirmar = {
                haptica.exito()
                vm.restablecerContrasenasComunes()
                vm.avisar("Catálogo restablecido al estándar", "Seguridad")
                mostrarDialogoRestablecer = false
            },
            alCerrar = { mostrarDialogoRestablecer = false }
        )
    }
}
