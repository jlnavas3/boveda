/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.titulos.reglas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Router
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeccionConfiguracionRedLocal(
    plantillaRouter: String,
    plantillaServidor: String,
    octetosRouter: List<Int>,
    alCambiarPlantillaRouter: (String) -> Unit,
    alCambiarPlantillaServidor: (String) -> Unit,
    alAgregarOctetoClick: () -> Unit,
    alEliminarOcteto: (Int) -> Unit,
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Redes Privadas e IPs (RFC 1918)",
        icono = Icons.Filled.Router,
        colorIcono = ColorAcento,
        idGrupo = "03-LST-RGL-G02",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Formato automático para direcciones IP privadas (192.168.*, 10.*, 172.16-31.*, localhost). Usa '{ip}' como comodín.",
                color = TextoSecundario,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = plantillaRouter,
                alCambiar = alCambiarPlantillaRouter,
                etiqueta = "Plantilla de Routers y Puertas de Enlace",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Octetos finales identificados como router / gateway:",
                color = TextoSecundario,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                octetosRouter.forEach { octeto ->
                    ChipOctetoRouter(
                        octeto = octeto,
                        alEliminar = alEliminarOcteto,
                        permitirEliminar = octetosRouter.size > 1
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(com.jlnavas3.bovedalocal.ui.theme.FormaChip)
                        .background(ColorAcento.copy(alpha = 0.15f))
                        .clickable { alAgregarOctetoClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar octeto",
                            tint = ColorAcento,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Agregar",
                            color = ColorAcento,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = plantillaServidor,
                alCambiar = alCambiarPlantillaServidor,
                etiqueta = "Plantilla de Servidores y Equipos Locales",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaSeccionConfiguracionRedLocal() {
    BovedaTheme {
        SeccionConfiguracionRedLocal(
            plantillaRouter = "Router ({ip})",
            plantillaServidor = "Servidor ({ip})",
            octetosRouter = listOf(1, 254),
            alCambiarPlantillaRouter = {},
            alCambiarPlantillaServidor = {},
            alAgregarOctetoClick = {},
            alEliminarOcteto = {}
        )
    }
}
