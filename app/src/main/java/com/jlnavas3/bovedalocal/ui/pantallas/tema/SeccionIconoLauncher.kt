/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppShortcut
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun SeccionIconoLauncher(
    ajustes: AjustesApp,
    haptica: Haptica,
    reqLauncher: BringIntoViewRequester,
    alSolicitarCambioIcono: (VarianteIconoLauncher) -> Unit
) {
    ComponenteGrupo(
        etiqueta = "ÍCONO EN EL LAUNCHER",
        icono = Icons.Filled.AppShortcut,
        colorIcono = Color(0xFFE91E63),
        idGrupo = "02-APA-THM-G06",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = {
            if (ajustes.iconoLauncher != AjustesDefaults.Tema.ICONO_LAUNCHER) {
                alSolicitarCambioIcono(CatalogoIconosLauncher.buscarPorClave(AjustesDefaults.Tema.ICONO_LAUNCHER))
            }
        },
        modifier = Modifier.bringIntoViewRequester(reqLauncher)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // --- SECCIÓN 1: Bóveda Oscura ---
            Text(
                text = "Bóveda Oscura (Obsidiana)",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Fondo obsidiana con candado en acentos metálicos y neón",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )
            Spacer(Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CatalogoIconosLauncher.bovedaOscura.forEach { variante ->
                    val seleccionado = ajustes.iconoLauncher == variante.clave
                    MiniaturaIconoLauncher(
                        variante = variante,
                        seleccionado = seleccionado,
                        tamano = 44.dp,
                        onClick = {
                            haptica.tic()
                            alSolicitarCambioIcono(variante)
                        }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // --- SECCIÓN 2: Fondos Vibrantes ---
            Text(
                text = "Fondos Vibrantes",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Fondos a color con candado de alto contraste",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )
            Spacer(Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CatalogoIconosLauncher.fondosVibrantes.forEach { variante ->
                    val seleccionado = ajustes.iconoLauncher == variante.clave
                    MiniaturaIconoLauncher(
                        variante = variante,
                        seleccionado = seleccionado,
                        tamano = 44.dp,
                        onClick = {
                            haptica.tic()
                            alSolicitarCambioIcono(variante)
                        }
                    )
                }
            }
        }
    }
}
