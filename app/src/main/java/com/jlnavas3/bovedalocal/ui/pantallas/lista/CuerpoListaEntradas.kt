package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.componentes.encontrarIndiceParaLetra
import com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorSitio
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CuerpoListaEntradas(
    visibles: List<Entrada>,
    ajustes: AjustesApp,
    criterioOrdenacion: CriterioOrdenacion,
    busqueda: String,
    modoSeleccion: Boolean,
    seleccionados: Set<String>,
    gruposExpandidos: Set<String>,
    densidadAltura: Dp,
    densidadMonograma: Int,
    espaciadoFilas: Dp,
    alAbrirEntrada: (String) -> Unit,
    alCopiarUsuario: (String, String) -> Unit,
    alCopiarContrasena: (String, String) -> Unit,
    alCopiarCodigoTotp: (String, String) -> Unit,
    alAlternarFavorito: (String) -> Unit,
    alEntrarEnSeleccion: (String) -> Unit,
    alAlternarSeleccion: (String) -> Unit,
    alAlternarGrupo: (String) -> Unit
) {
    val ambitoCorutina = rememberCoroutineScope()
    val densidad = LocalDensity.current
    val expandidoEnLista: (String) -> Boolean = { clave ->
        busqueda.isNotBlank() || gruposExpandidos.contains(clave)
    }
    val itemsAMostrar = remember(visibles, modoSeleccion, criterioOrdenacion, ajustes.agruparPorSitio) {
        construirItemsAgrupadosPorSitio(
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

    fun itemCoincideConLetra(item: ItemAgrupado, letra: Char?, incluirEnie: Boolean): Boolean {
        if (letra == null) return false
        val titulo = when (item) {
            is ItemAgrupado.Suelto -> item.entrada.titulo
            is ItemAgrupado.Grupo -> item.clave.removePrefix("www.")
            is ItemAgrupado.Hijo -> item.entrada.titulo
        }
        return letraInicialIndice(titulo, incluirEnie) == letra
    }

    val primerIndiceCoincidente = remember(itemsAMostrar, letraArrastrada, ajustes.indiceIncluirEnie, ajustes.indiceResaltarEntradas, ajustes.indiceResaltarSoloPrimera) {
        if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) null
        else if (ajustes.indiceResaltarSoloPrimera) {
            itemsAMostrar.indexOfFirst { itemCoincideConLetra(it, letraArrastrada, ajustes.indiceIncluirEnie) }.takeIf { it >= 0 }
        } else null
    }

    var segundosUnix by remember { mutableStateOf(System.currentTimeMillis() / 1000) }
    LaunchedEffect(Unit) {
        while (true) {
            segundosUnix = System.currentTimeMillis() / 1000
            delay(1000)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = estadoLista,
            modifier = Modifier
                .fillMaxSize()
                .reboteElastico(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = if (mostrarIndice) 36.dp else 20.dp,
                bottom = 110.dp
            ),
            verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
        ) {
            itemsIndexed(
                itemsAMostrar,
                key = { _, item ->
                    when (item) {
                        is ItemAgrupado.Suelto -> item.entrada.id
                        is ItemAgrupado.Grupo -> "grupo-${item.clave}"
                        is ItemAgrupado.Hijo -> "hijo-${item.entrada.id}"
                    }
                },
                contentType = { _, item ->
                    when (item) {
                        is ItemAgrupado.Suelto -> 0
                        is ItemAgrupado.Grupo -> 1
                        is ItemAgrupado.Hijo -> 2
                    }
                }
            ) { indice, item ->
                val coincideLetra = if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) {
                    false
                } else if (ajustes.indiceResaltarSoloPrimera) {
                    indice == primerIndiceCoincidente
                } else {
                    itemCoincideConLetra(item, letraArrastrada, ajustes.indiceIncluirEnie)
                }
                when (item) {
                    is ItemAgrupado.Grupo -> ComponenteGrupoLista(
                        clave = item.clave,
                        entradas = item.entradas,
                        expandido = expandidoEnLista(item.clave),
                        alturaFila = densidadAltura,
                        tamanoMonograma = densidadMonograma,
                        resaltado = coincideLetra,
                        alAlternar = { alAlternarGrupo(item.clave) },
                        contenidoEntrada = { entradaHija, indiceHijo, totalHijos ->
                            val coincideLetraHijo = if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) {
                                false
                            } else {
                                letraInicialIndice(entradaHija.titulo, ajustes.indiceIncluirEnie) == letraArrastrada
                            }
                            FilaEntrada(
                                entrada = entradaHija,
                                seleccionActiva = modoSeleccion,
                                seleccionado = seleccionados.contains(entradaHija.id),
                                alAbrir = { alAbrirEntrada(entradaHija.id) },
                                alCopiarUsuario = { alCopiarUsuario(entradaHija.id, entradaHija.usuario) },
                                alCopiarContrasena = { alCopiarContrasena(entradaHija.id, entradaHija.contrasena) },
                                alFavorito = { alAlternarFavorito(entradaHija.id) },
                                alCopiarCodigo = { alCopiarCodigoTotp(entradaHija.id, it) },
                                alPulsarLargo = { alEntrarEnSeleccion(entradaHija.id) },
                                alAlternarSeleccion = { alAlternarSeleccion(entradaHija.id) },
                                segundosUnix = segundosUnix,
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
                    is ItemAgrupado.Suelto -> FilaEntrada(
                        entrada = item.entrada,
                        seleccionActiva = modoSeleccion,
                        seleccionado = seleccionados.contains(item.entrada.id),
                        alAbrir = { alAbrirEntrada(item.entrada.id) },
                        alCopiarUsuario = { alCopiarUsuario(item.entrada.id, item.entrada.usuario) },
                        alCopiarContrasena = { alCopiarContrasena(item.entrada.id, item.entrada.contrasena) },
                        alFavorito = { alAlternarFavorito(item.entrada.id) },
                        alCopiarCodigo = { alCopiarCodigoTotp(item.entrada.id, it) },
                        alPulsarLargo = { alEntrarEnSeleccion(item.entrada.id) },
                        alAlternarSeleccion = { alAlternarSeleccion(item.entrada.id) },
                        segundosUnix = segundosUnix,
                        alturaFila = densidadAltura,
                        tamanoMonograma = densidadMonograma,
                        resaltado = coincideLetra,
                        separarDigitosTotp = ajustes.totpSepararDigitos,
                        mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                        enGrupo = false
                    )
                    is ItemAgrupado.Hijo -> Unit
                }
            }
        }

        if (mostrarIndice) {
            IndiceAlfabetico(
                alSeleccionarLetra = { letra ->
                    if (!ajustes.indiceAlinearConCresta) {
                        val indice = encontrarIndiceParaLetra(itemsAMostrar, letra, ajustes.indiceIncluirEnie)
                        if (indice != null && indice in itemsAMostrar.indices) {
                            ambitoCorutina.launch {
                                estadoLista.scrollToItem(indice, 0)
                            }
                        }
                    }
                },
                alSeleccionarLetraConOffset = { letra, touchY ->
                    val indice = encontrarIndiceParaLetra(itemsAMostrar, letra, ajustes.indiceIncluirEnie)
                    if (indice != null && indice in itemsAMostrar.indices) {
                        ambitoCorutina.launch {
                            if (!ajustes.indiceAlinearConCresta) {
                                estadoLista.scrollToItem(indice, 0)
                            } else {
                                val alturaFilaPx = with(densidad) { densidadAltura.toPx() }
                                val espaciadoFilasPx = with(densidad) { espaciadoFilas.toPx() }
                                val pasoItemPx = alturaFilaPx + espaciadoFilasPx
                                val touchYEnViewport = touchY + with(densidad) { 4.dp.toPx() }
                                val targetItemTop = touchYEnViewport - (alturaFilaPx / 2f)

                                if (targetItemTop <= 0f || pasoItemPx <= 0f) {
                                    estadoLista.scrollToItem(indice, 0)
                                } else {
                                    val numItems = (targetItemTop / pasoItemPx).toInt()
                                    val resto = targetItemTop - (numItems * pasoItemPx)
                                    if (resto <= 0.001f) {
                                        val indiceTop = (indice - numItems).coerceAtLeast(0)
                                        estadoLista.scrollToItem(indiceTop, 0)
                                    } else {
                                        val indiceTop = indice - numItems - 1
                                        if (indiceTop >= 0) {
                                            val scrollOffset = (pasoItemPx - resto).toInt().coerceAtLeast(0)
                                            estadoLista.scrollToItem(indiceTop, scrollOffset)
                                        } else {
                                            estadoLista.scrollToItem(0, 0)
                                        }
                                    }
                                }
                            }
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
