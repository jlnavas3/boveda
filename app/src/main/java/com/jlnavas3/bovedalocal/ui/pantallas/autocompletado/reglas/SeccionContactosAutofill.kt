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
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.FormaChip
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeccionContactosAutofill(
    paquetes: Set<String>,
    pistasTelefono: List<String>,
    pistasNombre: List<String>,
    alAgregarPaqueteClick: () -> Unit,
    alEliminarPaquete: (String) -> Unit,
    alAgregarPistaTelefonoClick: () -> Unit,
    alEliminarPistaTelefono: (String) -> Unit,
    alAgregarPistaNombreClick: () -> Unit,
    alEliminarPistaNombre: (String) -> Unit,
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Contactos y Mensajería",
        icono = Icons.Filled.ContactPhone,
        colorIcono = ColorIconosInternos,
        idGrupo = "04-HER-PSK-RGL-G05",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Detección contextual en aplicaciones de mensajería, llamadas o contactos para sugerir teléfonos y nombres de tus contactos guardados.",
                color = TextoSecundario,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sub-bloque 1: Aplicaciones habilitadas
            Text(
                text = "Aplicaciones con Detección Activa",
                color = TextoPrincipal,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Paquetes donde la bóveda analizará casillas de destinatarios para sugerir contactos.",
                color = TextoSecundario,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                paquetes.sorted().forEach { pkg ->
                    ChipPistaAutofill(
                        pista = pkg,
                        alEliminar = alEliminarPaquete
                    )
                }
                BotonAgregarElemento(alClick = alAgregarPaqueteClick)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = ColorBordeActual.copy(alpha = 0.5f), thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Sub-bloque 2: Palabras clave de teléfono
            Text(
                text = "Campos de Teléfono",
                color = TextoPrincipal,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Términos o IDs que identifican la casilla donde se ingresa el número telefónico.",
                color = TextoSecundario,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pistasTelefono.forEach { pista ->
                    ChipPistaAutofill(
                        pista = pista,
                        alEliminar = alEliminarPistaTelefono
                    )
                }
                BotonAgregarElemento(alClick = alAgregarPistaTelefonoClick)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = ColorBordeActual.copy(alpha = 0.5f), thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Sub-bloque 3: Palabras clave de nombre
            Text(
                text = "Campos de Nombre de Contacto",
                color = TextoPrincipal,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Términos o IDs que identifican la casilla donde se escribe el nombre del contacto.",
                color = TextoSecundario,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pistasNombre.forEach { pista ->
                    ChipPistaAutofill(
                        pista = pista,
                        alEliminar = alEliminarPistaNombre
                    )
                }
                BotonAgregarElemento(alClick = alAgregarPistaNombreClick)
            }
        }
    }
}

@Composable
private fun BotonAgregarElemento(alClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(FormaChip)
            .background(ColorAcento.copy(alpha = 0.15f))
            .clickable { alClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Agregar elemento",
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

@BovedaPreview
@Composable
private fun PreviaSeccionContactosAutofill() {
    BovedaTheme {
        SeccionContactosAutofill(
            paquetes = setOf("com.whatsapp", "org.telegram.messenger"),
            pistasTelefono = listOf("phone", "telefono", "mobile"),
            pistasNombre = listOf("contact_name", "first_name"),
            alAgregarPaqueteClick = {},
            alEliminarPaquete = {},
            alAgregarPistaTelefonoClick = {},
            alEliminarPistaTelefono = {},
            alAgregarPistaNombreClick = {},
            alEliminarPistaNombre = {}
        )
    }
}
