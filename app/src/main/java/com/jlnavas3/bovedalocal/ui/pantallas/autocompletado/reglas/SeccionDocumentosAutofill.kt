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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.TipoDocumentoIdentidad
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.FormaChip
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeccionDocumentosAutofill(
    pistas: List<String>,
    tiposPersonalizados: List<String> = emptyList(),
    alAgregarClick: () -> Unit,
    alEliminarPista: (String) -> Unit,
    alAgregarTipoClick: () -> Unit = {},
    alEliminarTipoPersonalizado: (String) -> Unit = {},
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Documentos de Identidad",
        icono = Icons.Filled.Badge,
        colorIcono = ColorIconosInternos,
        idGrupo = "04-HER-PSK-RGL-G04",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Palabras clave reconocidas en formularios para sugerir y autocompletar números de cédula, DNI, pasaporte, RUC, CURP o identificación personal.",
                color = TextoSecundario,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pistas.forEach { pista ->
                    ChipPistaAutofill(
                        pista = pista,
                        alEliminar = alEliminarPista
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(FormaChip)
                        .background(ColorAcento.copy(alpha = 0.15f))
                        .clickable { alAgregarClick() }
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

            Spacer(modifier = Modifier.height(18.dp))
            ComponenteSeparador()
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Siglas y Catálogo Oficial de Identidades",
                color = TextoPrincipal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Siglas oficiales disponibles al crear o editar identidades (CI, DNI, Pasaporte, etc.) más tus siglas personalizadas.",
                color = TextoSecundario,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TipoDocumentoIdentidad.SIGLAS_PREDETERMINADAS.forEach { sigla ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(FormaChip)
                            .background(TextoSecundario.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = sigla,
                            color = TextoPrincipal,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                tiposPersonalizados.forEach { siglaPers ->
                    ChipPistaAutofill(
                        pista = siglaPers,
                        alEliminar = alEliminarTipoPersonalizado
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(FormaChip)
                        .background(ColorAcento.copy(alpha = 0.15f))
                        .clickable { alAgregarTipoClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar tipo de documento",
                            tint = ColorAcento,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Añadir sigla",
                            color = ColorAcento,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaSeccionDocumentosAutofill() {
    BovedaTheme {
        SeccionDocumentosAutofill(
            pistas = listOf("cedula", "dni", "ruc", "curp", "rfc"),
            tiposPersonalizados = listOf("NIE", "SSN"),
            alAgregarClick = {},
            alEliminarPista = {},
            alAgregarTipoClick = {},
            alEliminarTipoPersonalizado = {}
        )
    }
}
