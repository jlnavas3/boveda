package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import com.jlnavas3.bovedalocal.util.MedidorFuerza
import com.jlnavas3.bovedalocal.util.claveAgrupacionPorTitulo
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorTitulo
import java.text.Normalizer

private fun normalizarTexto(texto: String): String {
    return Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .lowercase()
}

fun coincideBusquedaSalud(entrada: Entrada, consulta: String): Boolean {
    if (consulta.isBlank()) return true
    val q = normalizarTexto(consulta.trim())
    return normalizarTexto(entrada.titulo).contains(q) ||
           normalizarTexto(entrada.usuario).contains(q) ||
           entrada.urls.any { normalizarTexto(it).contains(q) } ||
           entrada.etiquetas.any { normalizarTexto(it).contains(q) } ||
           normalizarTexto(entrada.notas).contains(q)
}

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
    agruparPorSitio: Boolean = false,
    mostrarIndicadores: Boolean = false,
    espaciadoFilas: Dp = calcularEspaciadoFilas(),
    seleccionActiva: Boolean = false,
    seleccionados: Set<String> = emptySet(),
    alAlternarSeleccion: ((String) -> Unit)? = null,
    alPulsarLargo: ((String) -> Unit)? = null
) {
    val consulta = textoBusqueda.trim()
    var gruposExpandidos by rememberSaveable { mutableStateOf(emptySet<String>()) }
    val rellenoInferior = PaddingValues(bottom = if (seleccionActiva) 96.dp else 16.dp)

    Box(modifier = modifier) {
        when (pestanaActiva) {
            PestanaSalud.REPETIDAS -> {
                val gruposFiltrados = remember(duplicadas, consulta) {
                    if (consulta.isEmpty()) {
                        duplicadas.map { grupo -> grupo to grupo }
                    } else {
                        duplicadas.mapNotNull { grupo ->
                            val coincidentes = grupo.filter { coincideBusquedaSalud(it, consulta) }
                            if (coincidentes.isNotEmpty()) grupo to coincidentes else null
                        }
                    }
                }

                if (gruposFiltrados.isEmpty()) {
                    if (consulta.isNotEmpty()) {
                        MensajeExitoPestana(
                            icono = Icons.Filled.SearchOff,
                            titulo = "Sin resultados",
                            subtitulo = "No se encontraron contraseñas repetidas para \"$consulta\".",
                            colorIcono = ColorAcento
                        )
                    } else {
                        MensajeExitoPestana(
                            icono = Icons.Filled.Check,
                            titulo = "Sin contraseñas repetidas",
                            subtitulo = "Todas tus contraseñas son únicas en sus respectivas cuentas."
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(espaciadoFilas),
                        contentPadding = rellenoInferior
                    ) {
                        if (!agruparPorSitio) {
                            val todasEntradas = gruposFiltrados.flatMap { it.second }
                            items(todasEntradas, key = { it.id }) { entrada ->
                                FilaProblemaAgil(
                                    entrada = entrada,
                                    etiquetaDetalle = "Repetida",
                                    colorDetalle = Peligro,
                                    alCambiarRapido = { alCambiarClave(entrada) },
                                    alVerDetalle = { alVerDetalle(entrada.id) },
                                    alIgnorar = { alIgnorar(entrada) },
                                    mostrarIndicadores = mostrarIndicadores,
                                    enGrupo = false,
                                    seleccionActiva = seleccionActiva,
                                    seleccionado = seleccionados.contains(entrada.id),
                                    alAlternarSeleccion = { alAlternarSeleccion?.invoke(entrada.id) },
                                    alPulsarLargo = { alPulsarLargo?.invoke(entrada.id) }
                                )
                            }
                        } else {
                            items(gruposFiltrados, key = { it.first.first().contrasena }) { (grupoOriginal, entradasVisibles) ->
                                val claveGrupo = remember(grupoOriginal) { "rep_${grupoOriginal.first().contrasena.hashCode()}" }
                                val entradasOrdenadas = remember(entradasVisibles) {
                                    entradasVisibles.sortedBy { claveAgrupacionPorTitulo(it) }
                                }
                                TarjetaGrupoRepetido(
                                    grupo = entradasOrdenadas,
                                    totalEnGrupo = grupoOriginal.size,
                                    alCambiarClave = { alCambiarClave(it) },
                                    alVerDetalle = { alVerDetalle(it) },
                                    alIgnorar = alIgnorar,
                                    mostrarIndicadores = mostrarIndicadores,
                                    colapsable = true,
                                    expandido = gruposExpandidos.contains(claveGrupo),
                                    alAlternar = {
                                        gruposExpandidos = if (gruposExpandidos.contains(claveGrupo)) {
                                            gruposExpandidos - claveGrupo
                                        } else {
                                            gruposExpandidos + claveGrupo
                                        }
                                    },
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
                    ListaProblemasSalud(
                        entradas = filtradas,
                        agruparPorSitio = agruparPorSitio,
                        mostrarIndicadores = mostrarIndicadores,
                        gruposExpandidos = gruposExpandidos,
                        espaciadoFilas = espaciadoFilas,
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
                    ListaProblemasSalud(
                        entradas = filtradas,
                        agruparPorSitio = agruparPorSitio,
                        mostrarIndicadores = mostrarIndicadores,
                        gruposExpandidos = gruposExpandidos,
                        espaciadoFilas = espaciadoFilas,
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
                    ListaProblemasSalud(
                        entradas = filtradas,
                        agruparPorSitio = agruparPorSitio,
                        mostrarIndicadores = mostrarIndicadores,
                        gruposExpandidos = gruposExpandidos,
                        espaciadoFilas = espaciadoFilas,
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
                    ListaProblemasSalud(
                        entradas = filtradas,
                        agruparPorSitio = agruparPorSitio,
                        mostrarIndicadores = mostrarIndicadores,
                        gruposExpandidos = gruposExpandidos,
                        espaciadoFilas = espaciadoFilas,
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

@Composable
private fun ListaProblemasSalud(
    entradas: List<Entrada>,
    agruparPorSitio: Boolean,
    mostrarIndicadores: Boolean,
    gruposExpandidos: Set<String>,
    espaciadoFilas: Dp,
    alAlternarGrupo: (String) -> Unit,
    alCambiarClave: (Entrada) -> Unit,
    alVerDetalle: (String) -> Unit,
    alIgnorar: (Entrada) -> Unit = {},
    infoDetalle: (Entrada) -> Pair<String, Color>,
    seleccionActiva: Boolean = false,
    seleccionados: Set<String> = emptySet(),
    alAlternarSeleccion: ((String) -> Unit)? = null,
    alPulsarLargo: ((String) -> Unit)? = null
) {
    val rellenoInferior = PaddingValues(bottom = if (seleccionActiva) 96.dp else 16.dp)

    if (!agruparPorSitio) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(espaciadoFilas),
            contentPadding = rellenoInferior
        ) {
            items(entradas, key = { it.id }) { entrada ->
                val (etiqueta, color) = infoDetalle(entrada)
                FilaProblemaAgil(
                    entrada = entrada,
                    etiquetaDetalle = etiqueta,
                    colorDetalle = color,
                    alCambiarRapido = { alCambiarClave(entrada) },
                    alVerDetalle = { alVerDetalle(entrada.id) },
                    alIgnorar = { alIgnorar(entrada) },
                    mostrarIndicadores = mostrarIndicadores,
                    enGrupo = false,
                    seleccionActiva = seleccionActiva,
                    seleccionado = seleccionados.contains(entrada.id),
                    alAlternarSeleccion = { alAlternarSeleccion?.invoke(entrada.id) },
                    alPulsarLargo = { alPulsarLargo?.invoke(entrada.id) }
                )
            }
        }
    } else {
        val itemsAgrupados = remember(entradas, agruparPorSitio) {
            construirItemsAgrupadosPorTitulo(
                entradas = entradas,
                agrupar = true,
                expandido = { false }
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(espaciadoFilas),
            contentPadding = rellenoInferior
        ) {
            items(itemsAgrupados, key = { item ->
                when (item) {
                    is ItemAgrupado.Grupo -> "grupo_${item.clave}"
                    is ItemAgrupado.Suelto -> "suelto_${item.entrada.id}"
                    is ItemAgrupado.Hijo -> "hijo_${item.entrada.id}"
                }
            }) { item ->
                when (item) {
                    is ItemAgrupado.Grupo -> {
                        ComponenteGrupoLista(
                            clave = item.clave,
                            entradas = item.entradas,
                            expandido = gruposExpandidos.contains(item.clave),
                            alAlternar = { alAlternarGrupo(item.clave) },
                            contenidoEntrada = { entradaHija, _, _ ->
                                val (etiqueta, color) = infoDetalle(entradaHija)
                                FilaProblemaAgil(
                                    entrada = entradaHija,
                                    etiquetaDetalle = etiqueta,
                                    colorDetalle = color,
                                    alCambiarRapido = { alCambiarClave(entradaHija) },
                                    alVerDetalle = { alVerDetalle(entradaHija.id) },
                                    alIgnorar = { alIgnorar(entradaHija) },
                                    mostrarIndicadores = mostrarIndicadores,
                                    enGrupo = true,
                                    seleccionActiva = seleccionActiva,
                                    seleccionado = seleccionados.contains(entradaHija.id),
                                    alAlternarSeleccion = { alAlternarSeleccion?.invoke(entradaHija.id) },
                                    alPulsarLargo = { alPulsarLargo?.invoke(entradaHija.id) }
                                )
                            }
                        )
                    }
                    is ItemAgrupado.Suelto -> {
                        val (etiqueta, color) = infoDetalle(item.entrada)
                        FilaProblemaAgil(
                            entrada = item.entrada,
                            etiquetaDetalle = etiqueta,
                            colorDetalle = color,
                            alCambiarRapido = { alCambiarClave(item.entrada) },
                            alVerDetalle = { alVerDetalle(item.entrada.id) },
                            alIgnorar = { alIgnorar(item.entrada) },
                            mostrarIndicadores = mostrarIndicadores,
                            enGrupo = false,
                            seleccionActiva = seleccionActiva,
                            seleccionado = seleccionados.contains(item.entrada.id),
                            alAlternarSeleccion = { alAlternarSeleccion?.invoke(item.entrada.id) },
                            alPulsarLargo = { alPulsarLargo?.invoke(item.entrada.id) }
                        )
                    }
                    is ItemAgrupado.Hijo -> Unit
                }
            }
        }
    }
}
