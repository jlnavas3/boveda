/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.transiciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.util.DetectorAnimacionesSistema
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla principal de ajustes para personalizar las transiciones entre pantallas,
 * velocidad, físicas elásticas (Spring) y navegación gestual.
 *
 * Mantiene la previsualización interactiva flotante fija en la parte superior
 * y detecta si el sistema operativo tiene las animaciones desactivadas (escala 0x).
 */
@Composable
fun PantallaAjustesTransiciones(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var animacionesDesactivadas by remember {
        mutableStateOf(DetectorAnimacionesSistema.animacionesDesactivadas(contexto))
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                animacionesDesactivadas = DetectorAnimacionesSistema.animacionesDesactivadas(contexto)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    ProveedorResaltadoAjustes(seccionId) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                BarraSuperiorPantalla(
                    titulo = "Transiciones y animaciones",
                    idEtiqueta = "02-APA-TRN",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alVolver = { vm.volverAtras() },
                    conSeparador = false,
                    colorFondo = ColorAjustesFondo,
                    acciones = {
                        BotonMenuOpcionesPantalla(
                            grupos = emptyList(),
                            alRestablecerPantalla = {
                                haptica.tic()
                                vm.restablecerAjustesTransiciones()
                                vm.avisar("Transiciones restablecidas a valores por defecto")
                            }
                        )
                    }
                )

                // Previsualizador flotante fijado en la cabecera (no se oculta al scrolear)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    TarjetaPreviewTransicion(ajustes = ajustes)
                }

                // Lista de ajustes deslizable
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Aviso de accesibilidad / desarrollador si las animaciones del sistema están en 0x
                    if (animacionesDesactivadas) {
                        AvisoAnimacionesDesactivadas()
                        Spacer(Modifier.height(14.dp))
                    }

                    // Duración en milisegundos
                    SliderDuracionTransicion(
                        duracionMs = ajustes.duracionTransicionMs,
                        alCambiarDuracion = {
                            haptica.tic()
                            vm.ajustarDuracionTransicionMs(it)
                        }
                    )

                    Spacer(Modifier.height(14.dp))

                    // Físicas de resorte (Spring)
                    SeccionFisicaSpring(
                        springHabilitado = ajustes.springTransicionHabilitado,
                        alCambiarSpringHabilitado = {
                            haptica.tic()
                            vm.ajustarSpringHabilitado(it)
                        },
                        amortiguacionActualId = ajustes.springAmortiguacion,
                        alSeleccionarAmortiguacion = {
                            haptica.tic()
                            vm.ajustarSpringAmortiguacion(it)
                        }
                    )

                    Spacer(Modifier.height(14.dp))

                    // Regla de seguridad de desbloqueo (Fade)
                    OpcionReglaDesbloqueo(
                        desbloqueoFadeLimpio = ajustes.desbloqueoFadeLimpio,
                        alCambiarDesbloqueoFadeLimpio = {
                            haptica.tic()
                            vm.ajustarDesbloqueoFadeLimpio(it)
                        }
                    )

                    Spacer(Modifier.height(14.dp))

                    // Gesto de retroceso predictivo (Android 14+)
                    OpcionGestoPredictivo(
                        gestoPredictivoHabilitado = ajustes.gestoPredictivoHabilitado,
                        alCambiarGestoPredictivoHabilitado = {
                            haptica.tic()
                            vm.ajustarGestoPredictivoHabilitado(it)
                        }
                    )

                    Spacer(Modifier.height(20.dp))

                    // Selector de estilos nativos de Compose
                    SelectorEstiloTransicion(
                        estiloSeleccionadoId = ajustes.estiloTransicionPantalla,
                        alSeleccionarEstilo = {
                            haptica.tic()
                            vm.ajustarEstiloTransicion(it)
                        }
                    )

                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}
