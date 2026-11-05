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
import androidx.compose.material.icons.filled.CreditCard
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
fun SeccionTarjetasAutofill(
    pistasNumero: List<String>,
    pistasCvv: List<String>,
    pistasExpiracion: List<String>,
    pistasTitular: List<String>,
    alAgregarNumeroClick: () -> Unit,
    alEliminarNumero: (String) -> Unit,
    alAgregarCvvClick: () -> Unit,
    alEliminarCvv: (String) -> Unit,
    alAgregarExpiracionClick: () -> Unit,
    alEliminarExpiracion: (String) -> Unit,
    alAgregarTitularClick: () -> Unit,
    alEliminarTitular: (String) -> Unit,
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Tarjetas de Pago y Crédito",
        icono = Icons.Filled.CreditCard,
        colorIcono = Color(0xFF00897B),
        idGrupo = "04-HER-PSK-RGL-G08",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Palabras clave reconocidas para sugerir y autocompletar números de tarjeta, códigos de seguridad CVV, fechas de expiración y nombre del titular.",
                color = TextoSecundario,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))

            // 1. Número de tarjeta
            Text(
                text = "Número de tarjeta (PAN)",
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
                pistasNumero.forEach { pista ->
                    ChipPistaAutofill(pista = pista, alEliminar = alEliminarNumero)
                }
                BotonAgregarPistaAutofill(alPulsar = alAgregarNumeroClick)
            }

            Spacer(modifier = Modifier.height(14.dp))
            ComponenteSeparador()
            Spacer(modifier = Modifier.height(12.dp))

            // 2. CVV / CVC
            Text(
                text = "Código de seguridad (CVV / CVC)",
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
                pistasCvv.forEach { pista ->
                    ChipPistaAutofill(pista = pista, alEliminar = alEliminarCvv)
                }
                BotonAgregarPistaAutofill(alPulsar = alAgregarCvvClick)
            }

            Spacer(modifier = Modifier.height(14.dp))
            ComponenteSeparador()
            Spacer(modifier = Modifier.height(12.dp))

            // 3. Fecha de expiración
            Text(
                text = "Fecha de vencimiento (Exp)",
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
                pistasExpiracion.forEach { pista ->
                    ChipPistaAutofill(pista = pista, alEliminar = alEliminarExpiracion)
                }
                BotonAgregarPistaAutofill(alPulsar = alAgregarExpiracionClick)
            }

            Spacer(modifier = Modifier.height(14.dp))
            ComponenteSeparador()
            Spacer(modifier = Modifier.height(12.dp))

            // 4. Titular
            Text(
                text = "Nombre del titular (Cardholder)",
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
                pistasTitular.forEach { pista ->
                    ChipPistaAutofill(pista = pista, alEliminar = alEliminarTitular)
                }
                BotonAgregarPistaAutofill(alPulsar = alAgregarTitularClick)
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
private fun PreviaSeccionTarjetasAutofill() {
    BovedaTheme {
        SeccionTarjetasAutofill(
            pistasNumero = listOf("cardnumber", "tarjeta"),
            pistasCvv = listOf("cvv", "cvc"),
            pistasExpiracion = listOf("exp", "vencimiento"),
            pistasTitular = listOf("titular", "cardholder"),
            alAgregarNumeroClick = {},
            alEliminarNumero = {},
            alAgregarCvvClick = {},
            alEliminarCvv = {},
            alAgregarExpiracionClick = {},
            alEliminarExpiracion = {},
            alAgregarTitularClick = {},
            alEliminarTitular = {}
        )
    }
}
