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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes

@Composable
fun PantallaAjustesAvisos(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Avisos y Asistencia",
                idEtiqueta = "02-AVI-ASI",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("02-AVI-ASI-G01", "Visualización"),
                            AccionSaltoGrupo("02-AVI-ASI-G02", "Flujos inteligentes"),
                            AccionSaltoGrupo("02-AVI-ASI-G03", "Recomendaciones activas")
                        )
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .contenedorScrollAjustes(coordinador)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(
                    subtitulo = "Centro de asistencia y recomendaciones internas. Funciona 100% dentro de tu bóveda sin permisos del sistema operativo ni exposición de datos a otras aplicaciones."
                )
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Visualización
                ComponenteGrupo(
                    etiqueta = "Visualización",
                    icono = Icons.Filled.Visibility,
                    colorIcono = Color(0xFF1E88E5),
                    idGrupo = "02-AVI-ASI-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Campana en la barra superior",
                        subtitulo = "Muestra un icono con el conteo de avisos activos en la pantalla principal.",
                        icono = null,
                        activo = ajustes.avisos.mostrarCampanaCabecera,
                        idFila = "02-AVI-ASI-CAM",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { activo ->
                            vm.repositorio.ajustes.actualizar {
                                it.copy(avisos = it.avisos.copy(mostrarCampanaCabecera = activo))
                            }
                        }
                    )

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    ComponenteSwitch(
                        titulo = "Avisos en pantalla bloqueada",
                        subtitulo = "Muestra una pastilla discreta con el número de avisos pendientes al abrir la app, sin revelar ningún dato sensible.",
                        icono = null,
                        activo = ajustes.avisos.mostrarAvisosPantallaBloqueo,
                        idFila = "02-AVI-ASI-BLO",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { activo ->
                            vm.repositorio.ajustes.actualizar {
                                it.copy(avisos = it.avisos.copy(mostrarAvisosPantallaBloqueo = activo))
                            }
                        }
                    )
                }

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo 2: Flujos inteligentes
                ComponenteGrupo(
                    etiqueta = "Flujos inteligentes",
                    icono = Icons.Filled.AutoFixHigh,
                    colorIcono = Color(0xFF8E24AA),
                    idGrupo = "02-AVI-ASI-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Sugerir normalización tras importar",
                        subtitulo = "Pregunta si deseas estandarizar los títulos inmediatamente después de una importación si se detectan URLs crudas.",
                        icono = null,
                        activo = ajustes.avisos.sugerirNormalizarTrasImportar,
                        idFila = "02-AVI-ASI-SUG",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { activo ->
                            vm.repositorio.ajustes.actualizar {
                                it.copy(avisos = it.avisos.copy(sugerirNormalizarTrasImportar = activo))
                            }
                        }
                    )
                }

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo 3: Recomendaciones activas
                ComponenteGrupo(
                    etiqueta = "Recomendaciones activas",
                    icono = Icons.Filled.NotificationsActive,
                    colorIcono = Color(0xFFE65100),
                    idGrupo = "02-AVI-ASI-G03",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Nombres no amigables",
                        subtitulo = "Avisar cuando existan credenciales con URLs o nombres de paquetes en lugar de un nombre amigable.",
                        icono = null,
                        activo = ajustes.avisos.avisarTitulosSinNormalizar,
                        idFila = "02-AVI-ASI-TIT",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { activo ->
                            vm.repositorio.ajustes.actualizar {
                                it.copy(avisos = it.avisos.copy(avisarTitulosSinNormalizar = activo))
                            }
                        }
                    )

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    ComponenteSwitch(
                        titulo = "Credenciales duplicadas",
                        subtitulo = "Avisar cuando se detecten cuentas con el mismo usuario o dominio repetido.",
                        icono = null,
                        activo = ajustes.avisos.avisarDuplicados,
                        idFila = "02-AVI-ASI-DUP",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { activo ->
                            vm.repositorio.ajustes.actualizar {
                                it.copy(avisos = it.avisos.copy(avisarDuplicados = activo))
                            }
                        }
                    )

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    ComponenteSwitch(
                        titulo = "Auditoría de seguridad",
                        subtitulo = "Avisar sobre contraseñas débiles o repetidas en múltiples servicios.",
                        icono = null,
                        activo = ajustes.avisos.avisarSaludContrasenas,
                        idFila = "02-AVI-ASI-SAU",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { activo ->
                            vm.repositorio.ajustes.actualizar {
                                it.copy(avisos = it.avisos.copy(avisarSaludContrasenas = activo))
                            }
                        }
                    )

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    ComponenteSwitch(
                        titulo = "Copia de seguridad periódica",
                        subtitulo = "Avisar cuando hayan transcurrido los días configurados desde la última copia de tu bóveda local.",
                        icono = null,
                        activo = ajustes.avisos.avisarRespaldoPendiente,
                        idFila = "02-AVI-ASI-COP",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { activo ->
                            vm.repositorio.ajustes.actualizar {
                                it.copy(avisos = it.avisos.copy(avisarRespaldoPendiente = activo))
                            }
                        }
                    )

                    if (ajustes.avisos.avisosIgnorados.isNotEmpty()) {
                        ComponenteSeparador(sangriaInicio = 16.dp)

                        ComponenteBotonFila(
                            titulo = "Restablecer avisos descartados",
                            valorTexto = "${ajustes.avisos.avisosIgnorados.size}",
                            idFila = "02-AVI-ASI-RST",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alPulsar = { vm.restablecerAvisosIgnorados() }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
