/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton

@Composable
fun PantallaAjustesAvisos(
    vm: VaultViewModel
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

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
            colorFondo = ColorAjustesFondo
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DescripcionPantalla(
                subtitulo = "Centro de asistencia y recomendaciones internas. Funciona 100% dentro de tu bóveda sin permisos del sistema operativo ni exposición de datos a otras aplicaciones."
            )

            GrupoAjustes(etiqueta = "Visualización") {
                FilaAjuste(
                    titulo = "Campana en la barra superior",
                    descripcion = "Muestra un icono con el conteo de avisos activos en la pantalla principal.",
                    activo = ajustes.avisos.mostrarCampanaCabecera,
                    alCambiar = { activo ->
                        vm.repositorio.ajustes.actualizar {
                            it.copy(avisos = it.avisos.copy(mostrarCampanaCabecera = activo))
                        }
                    }
                )

                SeparadorFilaAjuste()

                FilaAjuste(
                    titulo = "Avisos en pantalla bloqueada",
                    descripcion = "Muestra una pastilla discreta con el número de avisos pendientes al abrir la app, sin revelar ningún dato sensible.",
                    activo = ajustes.avisos.mostrarAvisosPantallaBloqueo,
                    alCambiar = { activo ->
                        vm.repositorio.ajustes.actualizar {
                            it.copy(avisos = it.avisos.copy(mostrarAvisosPantallaBloqueo = activo))
                        }
                    }
                )
            }

            GrupoAjustes(etiqueta = "Flujos inteligentes") {
                FilaAjuste(
                    titulo = "Sugerir normalización tras importar",
                    descripcion = "Pregunta si deseas estandarizar los títulos inmediatamente después de una importación si se detectan URLs crudas.",
                    activo = ajustes.avisos.sugerirNormalizarTrasImportar,
                    alCambiar = { activo ->
                        vm.repositorio.ajustes.actualizar {
                            it.copy(avisos = it.avisos.copy(sugerirNormalizarTrasImportar = activo))
                        }
                    }
                )
            }

            GrupoAjustes(etiqueta = "Recomendaciones activas") {
                FilaAjuste(
                    titulo = "Títulos no estandarizados",
                    descripcion = "Avisar cuando existan credenciales con URLs o nombres de paquetes en lugar de un nombre amigable.",
                    activo = ajustes.avisos.avisarTitulosSinNormalizar,
                    alCambiar = { activo ->
                        vm.repositorio.ajustes.actualizar {
                            it.copy(avisos = it.avisos.copy(avisarTitulosSinNormalizar = activo))
                        }
                    }
                )

                SeparadorFilaAjuste()

                FilaAjuste(
                    titulo = "Credenciales duplicadas",
                    descripcion = "Avisar cuando se detecten cuentas con el mismo usuario o dominio repetido.",
                    activo = ajustes.avisos.avisarDuplicados,
                    alCambiar = { activo ->
                        vm.repositorio.ajustes.actualizar {
                            it.copy(avisos = it.avisos.copy(avisarDuplicados = activo))
                        }
                    }
                )

                SeparadorFilaAjuste()

                FilaAjuste(
                    titulo = "Auditoría de seguridad",
                    descripcion = "Avisar sobre contraseñas débiles o repetidas en múltiples servicios.",
                    activo = ajustes.avisos.avisarSaludContrasenas,
                    alCambiar = { activo ->
                        vm.repositorio.ajustes.actualizar {
                            it.copy(avisos = it.avisos.copy(avisarSaludContrasenas = activo))
                        }
                    }
                )

                SeparadorFilaAjuste()

                FilaAjuste(
                    titulo = "Copia de seguridad periódica",
                    descripcion = "Avisar cuando hayan transcurrido los días configurados desde la última copia de tu bóveda local.",
                    activo = ajustes.avisos.avisarRespaldoPendiente,
                    alCambiar = { activo ->
                        vm.repositorio.ajustes.actualizar {
                            it.copy(avisos = it.avisos.copy(avisarRespaldoPendiente = activo))
                        }
                    }
                )
            }

            if (ajustes.avisos.avisosIgnorados.isNotEmpty()) {
                Button(
                    onClick = { vm.restablecerAvisosIgnorados() },
                    shape = FormaBoton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorAcento,
                        contentColor = ColorSobreAcento
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Restablecer ${ajustes.avisos.avisosIgnorados.size} avisos descartados",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
