/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.menulateral

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.NodoAjuste
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Diálogo modal para explorar el catálogo de opciones y añadir accesos directos a la barra lateral:
 * - Selección unitaria rápida al pulsar sobre la fila (añade y cierra).
 * - Selección múltiple continua al pulsar sobre el icono '+' (se convierte en visto ✓ y permite seleccionar varios).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoAnadirItemMenuLateral(
    itemsDisponibles: List<NodoAjuste>,
    mostrarIds: Boolean,
    alSeleccionarUnico: (NodoAjuste) -> Unit,
    alConfirmarMultiples: (List<NodoAjuste>) -> Unit,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    var filtro by remember { mutableStateOf("") }
    val itemsSeleccionados = remember { mutableStateListOf<NodoAjuste>() }

    val filtrados = remember(filtro, itemsDisponibles) {
        val q = filtro.trim().lowercase()
        if (q.isBlank()) itemsDisponibles
        else {
            val palabras = q.split("\\s+".toRegex()).filter { it.isNotBlank() }
            itemsDisponibles.filter { nodo ->
                val corpus = "${nodo.titulo} ${nodo.subtitulo} ${nodo.id} ${nodo.grupo}".lowercase()
                palabras.all { corpus.contains(it) }
            }
        }
    }

    BasicAlertDialog(
        onDismissRequest = alCerrar
    ) {
        val tieneBorde = GrosorBorde > 0.dp && EstiloBorde != "ninguno"
        Surface(
            shape = FormaTarjeta,
            color = ColorTarjetaAjustes,
            border = if (tieneBorde) BorderStroke(GrosorBorde, ColorBordeActual) else null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Añadir a la barra lateral",
                    color = ColorTextoAjustes,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = filtro,
                    onValueChange = { filtro = it },
                    placeholder = { Text("Buscar...", color = ColorAjusteGris, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = ColorAjusteGris, modifier = Modifier.size(18.dp))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = FormaCampo,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorAcento,
                        unfocusedBorderColor = ColorAjusteGris.copy(alpha = 0.3f),
                        focusedTextColor = ColorTextoAjustes,
                        unfocusedTextColor = ColorTextoAjustes
                    ),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                ) {
                    items(filtrados, key = { it.id }) { nodo ->
                        val estaSeleccionado = itemsSeleccionados.any { it.id == nodo.id }
                        FilaOpcionAnadirMenuLateral(
                            nodo = nodo,
                            estaSeleccionado = estaSeleccionado,
                            mostrarIds = mostrarIds,
                            alPulsarFila = {
                                haptica.tic()
                                alSeleccionarUnico(nodo)
                                alCerrar()
                            },
                            alConmutarAdicion = {
                                haptica.tic()
                                if (estaSeleccionado) {
                                    itemsSeleccionados.removeAll { it.id == nodo.id }
                                } else {
                                    itemsSeleccionados.add(nodo)
                                }
                            }
                        )
                        ComponenteSeparador()
                    }
                }

                Spacer(Modifier.height(12.dp))

                BotonAccionDialogoAnadir(
                    cantidadSeleccionados = itemsSeleccionados.size,
                    alConfirmar = {
                        if (itemsSeleccionados.isNotEmpty()) {
                            haptica.exito()
                            alConfirmarMultiples(itemsSeleccionados.toList())
                            alCerrar()
                        } else {
                            alCerrar()
                        }
                    },
                    alCerrar = alCerrar
                )
            }
        }
    }
}
