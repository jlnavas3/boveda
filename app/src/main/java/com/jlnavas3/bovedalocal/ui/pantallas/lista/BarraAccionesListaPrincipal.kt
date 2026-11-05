/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AvisoInterno
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.BotonAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.componentes.barra.ItemAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.pantallas.avisos.iconoParaTipoAviso
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Barra inferior fija de acciones para la pantalla principal (Bóveda Local):
 * - Auditoría de accesibilidad (si está activa)
 * - Avisos y recomendaciones (🔔) únicamente si hay avisos activos:
 *     - Si hay 1 aviso: navegación directa sin modales.
 *     - Si hay múltiples avisos: menú emergente compacto anclado sobre el botón.
 * - Bloquear bóveda (🔒)
 * - Añadir nueva entrada (+)
 */
@Composable
fun BarraAccionesListaPrincipal(
    alBloquear: () -> Unit,
    alNuevaEntrada: () -> Unit,
    modifier: Modifier = Modifier,
    tieneAlertaAccesibilidad: Boolean = false,
    alAbrirAuditoriaAccesibilidad: (() -> Unit)? = null,
    avisos: List<AvisoInterno> = emptyList(),
    alNavegarAviso: ((Pantalla) -> Unit)? = null
) {
    var menuAvisosAbierto by remember { mutableStateOf(false) }

    BarraAccionesInferiorBoveda(modifier = modifier) {
        if (tieneAlertaAccesibilidad && alAbrirAuditoriaAccesibilidad != null) {
            BotonAccionBarraInferior(
                item = ItemAccionBarraInferior(
                    icono = Icons.Filled.Warning,
                    etiqueta = "Auditoría",
                    alPulsar = alAbrirAuditoriaAccesibilidad,
                    colorIcono = ColorIconosInternos,
                    descripcion = "Auditoría de accesibilidad"
                )
            )
        }

        if (avisos.isNotEmpty() && alNavegarAviso != null) {
            Box {
                BotonAccionBarraInferior(
                    item = ItemAccionBarraInferior(
                        icono = Icons.Filled.Notifications,
                        etiqueta = "Avisos (${avisos.size})",
                        alPulsar = {
                            if (avisos.size == 1) {
                                alNavegarAviso(avisos.first().pantallaDestino)
                            } else {
                                menuAvisosAbierto = true
                            }
                        },
                        colorIcono = ColorIconosInternos,
                        descripcion = "Avisos y recomendaciones (${avisos.size})"
                    )
                )

                if (avisos.size > 1) {
                    MenuDesplegableBoveda(
                        expanded = menuAvisosAbierto,
                        onDismissRequest = { menuAvisosAbierto = false }
                    ) {
                        avisos.forEach { aviso ->
                            ElementoMenuCompacto(
                                texto = aviso.titulo,
                                icono = iconoParaTipoAviso(aviso.tipo),
                                colorIcono = ColorIconosInternos,
                                colorTexto = TextoPrincipal,
                                onClick = {
                                    menuAvisosAbierto = false
                                    alNavegarAviso(aviso.pantallaDestino)
                                }
                            )
                        }
                    }
                }
            }
        }

        BotonAccionBarraInferior(
            item = ItemAccionBarraInferior(
                icono = Icons.Filled.Lock,
                etiqueta = "Bloquear",
                alPulsar = alBloquear,
                colorIcono = ColorIconosInternos,
                descripcion = "Bloquear bóveda"
            )
        )

        BotonAccionBarraInferior(
            item = ItemAccionBarraInferior(
                icono = Icons.Filled.AddCircle,
                etiqueta = "Nuevo",
                alPulsar = alNuevaEntrada,
                colorIcono = ColorAcento,
                descripcion = "Nueva entrada"
            )
        )
    }
}
