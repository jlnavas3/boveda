/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

@Composable
fun PantallaNormalizadorTitulos(
    vm: VaultViewModel,
    esPostImportacion: Boolean = false,
    idsEspecificos: List<String>? = null,
    alVolver: () -> Unit = { vm.volverAtras() }
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val grupos by vm.gruposTitulos.collectAsStateWithLifecycle()
    val modo by vm.modoFormatoTitulos.collectAsStateWithLifecycle()
    val respetarManuales by vm.respetarTitulosManuales.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val esSeleccionEspecifica = idsEspecificos != null
    var soloComprometidos by rememberSaveable(esSeleccionEspecifica) { mutableStateOf(!esSeleccionEspecifica) }
    var modoAvanzado by rememberSaveable(esSeleccionEspecifica) { mutableStateOf(esSeleccionEspecifica) }

    LaunchedEffect(idsEspecificos) {
        val entradasParaNormalizar = if (idsEspecificos != null) {
            val todas = (vm.estado.value as? com.jlnavas3.bovedalocal.data.EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
            todas.filter { it.id in idsEspecificos }
        } else {
            null
        }
        vm.prepararNormalizacionTitulos(entradasParaNormalizar)
    }

    val gruposComprometidos = remember(grupos) { grupos.filter { vm.esGrupoComprometido(it) } }
    val gruposVisibles = if (soloComprometidos) gruposComprometidos else grupos
    val tlds = ajustes.tldsDescartables
    val totalComprometidas = remember(gruposVisibles, tlds) {
        gruposVisibles.sumOf { g ->
            g.entradas.count { com.jlnavas3.bovedalocal.util.CalculadorAvisosInternos.esTituloComprometido(it, tlds) }
        }
    }
    val totalCuentas = if (soloComprometidos) totalComprometidas else gruposVisibles.sumOf { it.entradas.size }
    val ejemplos = remember(gruposComprometidos) { vm.obtenerEjemplosNormalizacion() }

    val mostrarVistaRapida = !modoAvanzado && totalComprometidas > 0

    Scaffold(
        topBar = {
            BarraSuperiorPantalla(
                titulo = when {
                    idsEspecificos != null -> "Renombrar (${idsEspecificos.size})"
                    mostrarVistaRapida -> "Renombrar Títulos"
                    esPostImportacion -> "Revisar títulos importados"
                    else -> "Renombrar"
                },
                idEtiqueta = "03-LST-TIT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = alVolver,
                conSeparador = listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    if (!mostrarVistaRapida) {
                        IconButton(onClick = { soloComprometidos = !soloComprometidos }) {
                            Icon(
                                imageVector = if (soloComprometidos) Icons.Filled.FilterList else Icons.Filled.Visibility,
                                contentDescription = if (soloComprometidos) "Mostrar todos (${grupos.size})" else "Mostrar solo pendientes (${gruposComprometidos.size})",
                                tint = if (soloComprometidos) ColorAcento else com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
                            )
                        }
                        IconButton(onClick = { vm.ir(Pantalla.ReglasNormalizacion(idsEspecificos)) }) {
                            Icon(
                                imageVector = Icons.Filled.Tune,
                                contentDescription = "Reglas de URLs y redes",
                                tint = ColorAcento
                            )
                        }
                    } else {
                        IconButton(onClick = { modoAvanzado = true }) {
                            Icon(
                                imageVector = Icons.Filled.Tune,
                                contentDescription = "Opciones avanzadas",
                                tint = ColorAcento
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (!mostrarVistaRapida) {
                BarraInferiorAccionNormalizador(
                    totalCuentas = totalCuentas,
                    alAplicar = {
                        vm.aplicarNormalizacionTitulos {
                            alVolver()
                        }
                    },
                    alCancelar = alVolver
                )
            }
        },
        containerColor = ColorAjustesFondo
    ) { relleno ->
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val isDragged by listState.interactionSource.collectIsDraggedAsState()
        LaunchedEffect(isDragged) {
            if (isDragged) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        }
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = relleno.calculateTopPadding() + 8.dp,
                bottom = relleno.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (mostrarVistaRapida) {
                item(key = "vista_rapida_1toque") {
                    VistaEstandarizacionRapida(
                        totalComprometidas = totalComprometidas,
                        ejemplos = ejemplos,
                        alEstandarizarTodo = {
                            vm.estandarizarTitulosRapido {
                                alVolver()
                            }
                        },
                        alIrAvanzado = { modoAvanzado = true }
                    )
                }
            } else {
                item(key = "selector_modo") {
                    SelectorModoFormatoTitulos(
                        modoActual = modo,
                        respetarManuales = respetarManuales,
                        alCambiarModo = { vm.cambiarModoFormatoTitulos(it) },
                        alCambiarRespetarManuales = { vm.cambiarRespetarTitulosManuales(it) },
                        mostrarId = ajustes.mostrarIdsAjustes,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (gruposVisibles.isEmpty()) {
                    item(key = "estado_vacio_limpios") {
                        EstadoVacioTitulosLimpios(
                            alVerTodos = { soloComprometidos = false },
                            esVacioTotal = grupos.isEmpty()
                        )
                    }
                } else {
                    itemsIndexed(
                        items = gruposVisibles,
                        key = { indice, grupo -> "${indice}_${grupo.id}" }
                    ) { indice, grupo ->
                        TarjetaGrupoTitulos(
                            grupo = grupo,
                            modo = modo,
                            alCambiarNombre = { nuevoNombre ->
                                vm.actualizarNombreGrupo(grupo.id, nuevoNombre)
                            },
                            prefijosSubdominios = ajustes.prefijosSubdominios,
                            tldsConfigurados = ajustes.tldsDescartables,
                            alSeleccionarSegmento = { seleccionado, descartados ->
                                vm.eliminarPrefijoSubdominio(seleccionado)
                                vm.agregarPrefijosSubdominios(descartados)
                            },
                            alAlternarReglaPrefijo = { prefijo ->
                                vm.alternarPrefijoSubdominio(prefijo)
                            },
                            esSeleccionEspecifica = esSeleccionEspecifica,
                            alEnfocarCampo = {
                                coroutineScope.launch {
                                    delay(200)
                                    listState.animateScrollToItem(index = 1 + indice, scrollOffset = 0)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item(key = "espaciador_final_scroll") {
                    val insetsIme = WindowInsets.ime
                    val density = LocalDensity.current
                    val alturaIme = with(density) { insetsIme.getBottom(density).toDp() }
                    val alturaEspaciador = if (alturaIme > 0.dp) 450.dp else 80.dp
                    Spacer(modifier = Modifier.height(alturaEspaciador))
                }
            }
        }
    }
}
