/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado.reglas

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaChip
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeccionDireccionesAutofill(
    pistasCalle: List<String>,
    pistasCp: List<String>,
    pistasCiudad: List<String>,
    pistasProvincia: List<String>,
    pistasPais: List<String>,
    alAgregarCalleClick: () -> Unit,
    alEliminarCalle: (String) -> Unit,
    alAgregarCpClick: () -> Unit,
    alEliminarCp: (String) -> Unit,
    alAgregarCiudadClick: () -> Unit,
    alEliminarCiudad: (String) -> Unit,
    alAgregarProvinciaClick: () -> Unit,
    alEliminarProvincia: (String) -> Unit,
    alAgregarPaisClick: () -> Unit,
    alEliminarPais: (String) -> Unit,
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Direcciones y Domicilios",
        icono = Icons.Filled.Home,
        colorIcono = Color(0xFF2563EB),
        idGrupo = "04-HER-PSK-RGL-G09",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Palabras clave reconocidas para sugerir y autocompletar calle, código postal, ciudad, provincia y país en formularios de entrega o facturación.",
                color = TextoSecundario,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))

            // 1. Calle
            Text(
                text = "Calle / Domicilio (Street)",
                color = TextoPrincipal,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pistasCalle.forEach { pista ->
                    ChipPistaAutofill(pista = pista, alEliminar = alEliminarCalle)
                }
                BotonAgregarPistaAutofill(alPulsar = alAgregarCalleClick)
            }

            Spacer(modifier = Modifier.height(14.dp))
            ComponenteSeparador()
            Spacer(modifier = Modifier.height(12.dp))

            // 2. CP
            Text(
                text = "Código Postal (ZIP / Postcode)",
                color = TextoPrincipal,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pistasCp.forEach { pista ->
                    ChipPistaAutofill(pista = pista, alEliminar = alEliminarCp)
                }
                BotonAgregarPistaAutofill(alPulsar = alAgregarCpClick)
            }

            Spacer(modifier = Modifier.height(14.dp))
            ComponenteSeparador()
            Spacer(modifier = Modifier.height(12.dp))

            // 3. Ciudad
            Text(
                text = "Ciudad / Localidad (City)",
                color = TextoPrincipal,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pistasCiudad.forEach { pista ->
                    ChipPistaAutofill(pista = pista, alEliminar = alEliminarCiudad)
                }
                BotonAgregarPistaAutofill(alPulsar = alAgregarCiudadClick)
            }

            Spacer(modifier = Modifier.height(14.dp))
            ComponenteSeparador()
            Spacer(modifier = Modifier.height(12.dp))

            // 4. Provincia
            Text(
                text = "Provincia / Estado / Región (State)",
                color = TextoPrincipal,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pistasProvincia.forEach { pista ->
                    ChipPistaAutofill(pista = pista, alEliminar = alEliminarProvincia)
                }
                BotonAgregarPistaAutofill(alPulsar = alAgregarProvinciaClick)
            }

            Spacer(modifier = Modifier.height(14.dp))
            ComponenteSeparador()
            Spacer(modifier = Modifier.height(12.dp))

            // 5. País
            Text(
                text = "País / Nación (Country)",
                color = TextoPrincipal,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pistasPais.forEach { pista ->
                    ChipPistaAutofill(pista = pista, alEliminar = alEliminarPais)
                }
                BotonAgregarPistaAutofill(alPulsar = alAgregarPaisClick)
            }
        }
    }
}

@Composable
private fun BotonAgregarPistaAutofill(alPulsar: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(FormaChip)
            .background(ColorAcento.copy(alpha = 0.15f))
            .clickable { alPulsar() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Agregar palabra clave",
                tint = ColorAcento,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Añadir palabra",
                color = ColorAcento,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaSeccionDireccionesAutofill() {
    BovedaTheme {
        SeccionDireccionesAutofill(
            pistasCalle = listOf("address", "calle"),
            pistasCp = listOf("zip", "postal"),
            pistasCiudad = listOf("city", "ciudad"),
            pistasProvincia = listOf("state", "provincia"),
            pistasPais = listOf("country", "pais"),
            alAgregarCalleClick = {},
            alEliminarCalle = {},
            alAgregarCpClick = {},
            alEliminarCp = {},
            alAgregarCiudadClick = {},
            alEliminarCiudad = {},
            alAgregarProvinciaClick = {},
            alEliminarProvincia = {},
            alAgregarPaisClick = {},
            alEliminarPais = {}
        )
    }
}
