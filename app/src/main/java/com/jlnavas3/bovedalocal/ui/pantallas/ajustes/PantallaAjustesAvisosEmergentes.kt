/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.PosicionToast
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.avisos.GrupoEstiloAvisosEmergentes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.avisos.GrupoPosicionAvisosEmergentes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.avisos.TarjetaPreviaAvisoEmergente
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla dedicada para la configuración completa de avisos emergentes (Toast):
 * - Selección de formato descriptivo o minimalista.
 * - Selección de posición (Superior, Inferior, Inferior sobre botones, Inferior empujando botones).
 * - Disparador de prueba interactivo.
 */
@Composable
fun PantallaAjustesAvisosEmergentes(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    ProveedorResaltadoAjustes(seccionDestino) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Avisos emergentes",
                idEtiqueta = "06-SIS-AVZ-TST",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .contenedorScrollAjustes(coordinador)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(
                    subtitulo = "Personaliza el formato visual y el punto de anclaje de las notificaciones emergentes temporales que confirman copias, guardados y acciones dentro de la bóveda."
                )

                Spacer(Modifier.height(10.dp))

                // Bloque Maestro: Activar / Desactivar Avisos
                ComponenteGrupo(
                    etiqueta = "General",
                    icono = Icons.Filled.Notifications,
                    colorIcono = ColorIconosInternos,
                    idGrupo = "06-SIS-AVZ-TST-G00",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Activar avisos emergentes",
                        subtitulo = "Muestra confirmaciones visuales al copiar, guardar y realizar acciones",
                        activo = ajustes.avisosEmergentesHabilitados,
                        alCambiar = { activado ->
                            haptica.tic()
                            vm.ajustarAvisosEmergentesHabilitados(activado)
                            if (activado) {
                                vm.avisar(
                                    mensaje = "Avisos emergentes activados",
                                    titulo = "Avisos emergentes"
                                )
                            }
                        },
                        icono = Icons.Filled.NotificationsActive,
                        colorIcono = ColorIconosInternos,
                        idFila = "06-SIS-AVZ-TST-F00",
                        mostrarId = ajustes.mostrarIdsAjustes
                    )
                }

                if (ajustes.avisosEmergentesHabilitados) {
                    Spacer(Modifier.height(EspaciadoComponentes))

                    // Bloque 1: Estilo y Formato
                    GrupoEstiloAvisosEmergentes(
                        toastDescriptivo = ajustes.toastDescriptivo,
                        mostrarIds = ajustes.mostrarIdsAjustes,
                        alCambiarEstilo = { descriptivo ->
                            haptica.tic()
                            vm.ajustarToastDescriptivo(descriptivo)
                            vm.avisar(
                                mensaje = if (descriptivo) "Formato descriptivo activado" else "Formato minimalista activado",
                                titulo = "Avisos emergentes"
                            )
                        }
                    )

                    Spacer(Modifier.height(EspaciadoComponentes))

                    // Bloque 2: Posición en Pantalla
                    GrupoPosicionAvisosEmergentes(
                        posicionActual = ajustes.posicionToast,
                        mostrarIds = ajustes.mostrarIdsAjustes,
                        alSeleccionarPosicion = { nuevaPosicion ->
                            haptica.tic()
                            vm.ajustarPosicionToast(nuevaPosicion)
                            vm.avisar(
                                mensaje = "Posición ajustada a ${nuevaPosicion.etiqueta}",
                                titulo = "Avisos emergentes"
                            )
                        }
                    )

                    Spacer(Modifier.height(EspaciadoComponentes))

                    // Bloque 3: Probar en vivo
                    TarjetaPreviaAvisoEmergente(
                        mostrarIds = ajustes.mostrarIdsAjustes,
                        alProbarAviso = {
                            haptica.exito()
                            vm.avisar(
                                mensaje = "Notificación de prueba en posición ${ajustes.posicionToast.etiqueta.lowercase()}",
                                titulo = "Demostración en vivo"
                            )
                        }
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
