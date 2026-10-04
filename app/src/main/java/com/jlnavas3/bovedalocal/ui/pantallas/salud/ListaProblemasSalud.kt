package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorTitulo

/**
 * Microcomponente que renderiza una lista de problemas de salud (débiles, comunes, antiguas, ignoradas),
 * soportando visualización plana o agrupada por dominio/sitio.
 */
@Composable
fun ListaProblemasSalud(
    entradas: List<Entrada>,
    agruparPorSitio: Boolean,
    mostrarIndicadores: Boolean,
    gruposExpandidos: Set<String>,
    espaciadoFilas: Dp,
    ajustes: AjustesApp? = null,
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
                    ajustes = ajustes,
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
                                    ajustes = ajustes,
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
                            ajustes = ajustes,
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
