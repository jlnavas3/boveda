/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.TipoDocumentoIdentidad
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Microcomponente desacoplado que renderiza un selector de tipo de documento oficial
 * mediante un menú desplegable nativo anclado (MenuDesplegableBoveda) al estilo de SelectorTipoEntrada.
 */
@Composable
fun SelectorTipoDocumentoDesplegable(
    tipoSeleccionado: String,
    alSeleccionarTipo: (String) -> Unit,
    tiposPersonalizados: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    var abierto by remember { mutableStateOf(false) }

    val docEnum = TipoDocumentoIdentidad.desdeSigla(tipoSeleccionado)
    val esSiglaEstandar = docEnum != TipoDocumentoIdentidad.OTRO
    val esPersonalizado = tiposPersonalizados.any { it.equals(tipoSeleccionado.trim(), ignoreCase = true) }

    val textoTipoMostrado = when {
        esSiglaEstandar -> "${docEnum.sigla} - ${docEnum.descripcion}"
        esPersonalizado -> "${tipoSeleccionado.trim().uppercase()} - Documento personalizado"
        tipoSeleccionado.isNotBlank() && tipoSeleccionado != "Otro" -> "${tipoSeleccionado.trim()} - Documento oficial"
        else -> "${TipoDocumentoIdentidad.OTRO.sigla} - ${TipoDocumentoIdentidad.OTRO.descripcion}"
    }

    Box(modifier = modifier.fillMaxWidth()) {
        CampoBoveda(
            valor = textoTipoMostrado,
            etiqueta = "Tipo de documento oficial",
            alCambiar = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Desplegar tipos de documento oficial",
                    tint = if (abierto) ColorAcento else ColorIconosInternos
                )
            },
            alPulsar = { abierto = true }
        )

        MenuDesplegableBoveda(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            TipoDocumentoIdentidad.entries.forEachIndexed { index, item ->
                if (index > 0) SeparadorOpcionMenu()
                val seleccionado = !esPersonalizado && item == docEnum
                DropdownMenuItem(
                    leadingIcon = {
                        Text(
                            text = item.sigla,
                            color = if (seleccionado) ColorAcento else ColorTitulos,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    },
                    text = {
                        Text(
                            text = item.descripcion,
                            color = if (seleccionado) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    trailingIcon = if (seleccionado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else null,
                    onClick = {
                        alSeleccionarTipo(item.sigla)
                        abierto = false
                    }
                )
            }

            tiposPersonalizados.forEach { siglaPers ->
                SeparadorOpcionMenu()
                val seleccionado = siglaPers.equals(tipoSeleccionado.trim(), ignoreCase = true)
                DropdownMenuItem(
                    leadingIcon = {
                        Text(
                            text = siglaPers.uppercase(),
                            color = if (seleccionado) ColorAcento else ColorTitulos,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    },
                    text = {
                        Text(
                            text = "Documento personalizado",
                            color = if (seleccionado) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    trailingIcon = if (seleccionado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else null,
                    onClick = {
                        alSeleccionarTipo(siglaPers.uppercase())
                        abierto = false
                    }
                )
            }
        }
    }
}
