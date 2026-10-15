/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.scroll.GestorGruposExpandidos
import com.jlnavas3.bovedalocal.ui.scroll.GestorPosicionScroll
import com.jlnavas3.bovedalocal.ui.scroll.rememberGruposExpandidosPersistentes
import com.jlnavas3.bovedalocal.ui.scroll.rememberLazyListStatePersistente
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.MedidorFuerza

/**
 * Orquestador microgranular del contenido por pestaña de la auditoría de salud.
 */
@Composable
fun ContenidoPestanaSalud(
    pestanaActiva: PestanaSalud,
    duplicadas: List<List<Entrada>>,
    muyComunes: List<Entrada>,
    debiles: List<Entrada>,
    antiguas: List<Entrada>,
    ignoradas: List<Entrada> = emptyList(),
    textoBusqueda: String,
    ahora: Long,
    alCambiarClave: (Entrada) -> Unit,
    alVerDetalle: (String) -> Unit,
    alIgnorar: (Entrada) -> Unit = {},
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null,
    agruparPorSitio: Boolean = false,
    mostrarIndicadores: Boolean = false,
    espaciadoFilas: Dp = calcularEspaciadoFilas(),
    seleccionActiva: Boolean = false,
    seleccionados: Set<String> = emptySet(),
    alAlternarSeleccion: ((String) -> Unit)? = null,
    alAlternarSeleccionGrupo: ((Set<String>) -> Unit)? = null,
    alPulsarLargo: ((String) -> Unit)? = null,
    gestorScroll: GestorPosicionScroll? = null,
    gestorGrupos: GestorGruposExpandidos? = null
) {
    val consulta = textoBusqueda.trim()
    var gruposExpandidos by if (gestorGrupos != null) {
        rememberGruposExpandidosPersistentes("salud_grupos", gestorGrupos)
    } else {
        rememberSaveable { mutableStateOf(emptySet<String>()) }
    }
    val rellenoInferior = PaddingValues(bottom = if (seleccionActiva) 96.dp else 16.dp)

    Box(modifier = modifier) {
        when (pestanaActiva) {
            PestanaSalud.REPETIDAS -> {
                val estadoRepetidas = rememberLazyListStatePersistente("salud_repetidas", gestorScroll)
                PestanaRepetidasSalud(
                    duplicadas = duplicadas,
                    consulta = consulta,
                    agruparPorSitio = agruparPorSitio,
                    espaciadoFilas = espaciadoFilas,
                    rellenoInferior = rellenoInferior,
                    ajustes = ajustes,
                    mostrarIndicadores = mostrarIndicadores,
                    seleccionActiva = seleccionActiva,
                    seleccionados = seleccionados,
                    estadoLista = estadoRepetidas,
                    alCambiarClave = alCambiarClave,
                    alVerDetalle = alVerDetalle,
                    alIgnorar = alIgnorar,
                    alAlternarSeleccion = alAlternarSeleccion,
                    alAlternarSeleccionGrupo = alAlternarSeleccionGrupo,
                    alPulsarLargo = alPulsarLargo
                )
            }

            PestanaSalud.COMUNES -> {
                val filtradas = remember(muyComunes, consulta) {
                    if (consulta.isEmpty()) muyComunes
                    else muyComunes.filter { coincideBusquedaSalud(it, consulta) }
                }

                if (filtradas.isEmpty()) {
                    if (consulta.isNotEmpty()) {
                        MensajeExitoPestana(
                            icono = Icons.Filled.SearchOff,
                            titulo = "Sin resultados",
                            subtitulo = "No se encontraron contraseñas comunes para \"$consulta\".",
                            colorIcono = ColorAcento
                        )
                    } else {
                        MensajeExitoPestana(
                            icono = Icons.Filled.Check,
                            titulo = "Sin contraseñas filtradas",
                            subtitulo = "Ninguna de tus contraseñas coincide con listas globales de filtraciones públicas."
                        )
                    }
                } else {
                    val estadoComunes = rememberLazyListStatePersistente("salud_comunes", gestorScroll)
                    ListaProblemasSalud(
                        entradas = filtradas,
                        agruparPorSitio = agruparPorSitio,
                        mostrarIndicadores = mostrarIndicadores,
                        gruposExpandidos = gruposExpandidos,
                        espaciadoFilas = espaciadoFilas,
                        ajustes = ajustes,
                        estadoLista = estadoComunes,
                        alAlternarGrupo = { clave ->
                            gruposExpandidos = if (gruposExpandidos.contains(clave)) {
                                gruposExpandidos - clave
                            } else {
                                gruposExpandidos + clave
                            }
                        },
                        alCambiarClave = alCambiarClave,
                        alVerDetalle = alVerDetalle,
                        alIgnorar = alIgnorar,
                        infoDetalle = { "Filtrada" to Peligro },
                        seleccionActiva = seleccionActiva,
                        seleccionados = seleccionados,
                        alAlternarSeleccion = alAlternarSeleccion,
                        alPulsarLargo = alPulsarLargo
                    )
                }
            }

            PestanaSalud.DEBILES -> {
                val filtradas = remember(debiles, consulta) {
                    if (consulta.isEmpty()) debiles
                    else debiles.filter { coincideBusquedaSalud(it, consulta) }
                }

                if (filtradas.isEmpty()) {
                    if (consulta.isNotEmpty()) {
                        MensajeExitoPestana(
                            icono = Icons.Filled.SearchOff,
                            titulo = "Sin resultados",
                            subtitulo = "No se encontraron contraseñas débiles para \"$consulta\".",
                            colorIcono = ColorAcento
                        )
                    } else {
                        MensajeExitoPestana(
                            icono = Icons.Filled.Check,
                            titulo = "Sin contraseñas débiles",
                            subtitulo = "Todas tus contraseñas tienen suficiente longitud y complejidad."
                        )
                    }
                } else {
                    val estadoDebiles = rememberLazyListStatePersistente("salud_debiles", gestorScroll)
                    ListaProblemasSalud(
                        entradas = filtradas,
                        agruparPorSitio = agruparPorSitio,
                        mostrarIndicadores = mostrarIndicadores,
                        gruposExpandidos = gruposExpandidos,
                        espaciadoFilas = espaciadoFilas,
                        ajustes = ajustes,
                        estadoLista = estadoDebiles,
                        alAlternarGrupo = { clave ->
                            gruposExpandidos = if (gruposExpandidos.contains(clave)) {
                                gruposExpandidos - clave
                            } else {
                                gruposExpandidos + clave
                            }
                        },
                        alCambiarClave = alCambiarClave,
                        alVerDetalle = alVerDetalle,
                        alIgnorar = alIgnorar,
                        infoDetalle = { entrada ->
                            val fuerza = MedidorFuerza.medir(entrada.contrasena)
                            fuerza.etiqueta to Peligro
                        },
                        seleccionActiva = seleccionActiva,
                        seleccionados = seleccionados,
                        alAlternarSeleccion = alAlternarSeleccion,
                        alPulsarLargo = alPulsarLargo
                    )
                }
            }

            PestanaSalud.ANTIGUAS -> {
                val filtradas = remember(antiguas, consulta) {
                    if (consulta.isEmpty()) antiguas
                    else antiguas.filter { coincideBusquedaSalud(it, consulta) }
                }

                if (filtradas.isEmpty()) {
                    if (consulta.isNotEmpty()) {
                        MensajeExitoPestana(
                            icono = Icons.Filled.SearchOff,
                            titulo = "Sin resultados",
                            subtitulo = "No se encontraron contraseñas antiguas para \"$consulta\".",
                            colorIcono = ColorAcento
                        )
                    } else {
                        MensajeExitoPestana(
                            icono = Icons.Filled.Check,
                            titulo = "Contraseñas al día",
                            subtitulo = "Tus credenciales han sido actualizadas recientemente."
                        )
                    }
                } else {
                    val estadoAntiguas = rememberLazyListStatePersistente("salud_antiguas", gestorScroll)
                    ListaProblemasSalud(
                        entradas = filtradas,
                        agruparPorSitio = agruparPorSitio,
                        mostrarIndicadores = mostrarIndicadores,
                        gruposExpandidos = gruposExpandidos,
                        espaciadoFilas = espaciadoFilas,
                        ajustes = ajustes,
                        estadoLista = estadoAntiguas,
                        alAlternarGrupo = { clave ->
                            gruposExpandidos = if (gruposExpandidos.contains(clave)) {
                                gruposExpandidos - clave
                            } else {
                                gruposExpandidos + clave
                            }
                        },
                        alCambiarClave = alCambiarClave,
                        alVerDetalle = alVerDetalle,
                        alIgnorar = alIgnorar,
                        infoDetalle = { entrada ->
                            val dias = diasDesde(entrada.modificadaEn, ahora)
                            "hace $dias d" to ColorAcento
                        },
                        seleccionActiva = seleccionActiva,
                        seleccionados = seleccionados,
                        alAlternarSeleccion = alAlternarSeleccion,
                        alPulsarLargo = alPulsarLargo
                    )
                }
            }

            PestanaSalud.IGNORADAS -> {
                val filtradas = remember(ignoradas, consulta) {
                    if (consulta.isEmpty()) ignoradas
                    else ignoradas.filter { coincideBusquedaSalud(it, consulta) }
                }

                if (filtradas.isEmpty()) {
                    if (consulta.isNotEmpty()) {
                        MensajeExitoPestana(
                            icono = Icons.Filled.SearchOff,
                            titulo = "Sin resultados",
                            subtitulo = "No se encontraron entradas ignoradas para \"$consulta\".",
                            colorIcono = ColorAcento
                        )
                    } else {
                        MensajeExitoPestana(
                            icono = Icons.Filled.Check,
                            titulo = "Sin entradas ignoradas",
                            subtitulo = "Todas tus contraseñas activas participan en la auditoría de salud."
                        )
                    }
                } else {
                    val estadoIgnoradas = rememberLazyListStatePersistente("salud_ignoradas", gestorScroll)
                    ListaProblemasSalud(
                        entradas = filtradas,
                        agruparPorSitio = agruparPorSitio,
                        mostrarIndicadores = mostrarIndicadores,
                        gruposExpandidos = gruposExpandidos,
                        espaciadoFilas = espaciadoFilas,
                        ajustes = ajustes,
                        estadoLista = estadoIgnoradas,
                        alAlternarGrupo = { clave ->
                            gruposExpandidos = if (gruposExpandidos.contains(clave)) {
                                gruposExpandidos - clave
                            } else {
                                gruposExpandidos + clave
                            }
                        },
                        alCambiarClave = alCambiarClave,
                        alVerDetalle = alVerDetalle,
                        alIgnorar = alIgnorar,
                        infoDetalle = { "Ignorada" to ColorAjusteGris },
                        seleccionActiva = seleccionActiva,
                        seleccionados = seleccionados,
                        alAlternarSeleccion = alAlternarSeleccion,
                        alPulsarLargo = alPulsarLargo
                    )
                }
            }
        }
    }
}
