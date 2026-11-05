/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente modal para seleccionar un contacto específico de un archivo (.vcf / .csv)
 * con múltiples entradas, con el fin de autorrellenar el formulario de edición.
 */
@Composable
fun ModalSeleccionContactoUnico(
    abierto: Boolean,
    contactos: List<ContactoDispositivo>,
    alSeleccionar: (ContactoDispositivo) -> Unit,
    alCerrar: () -> Unit
) {
    if (!abierto) return

    var consultaBusqueda by remember { mutableStateOf("") }

    val contactosFiltrados = remember(contactos, consultaBusqueda) {
        if (consultaBusqueda.isBlank()) {
            contactos
        } else {
            val q = consultaBusqueda.trim().lowercase()
            contactos.filter { c ->
                c.nombre.lowercase().contains(q) ||
                c.telefonos.any { it.contains(q) } ||
                c.correos.any { it.lowercase().contains(q) } ||
                (c.organizacion?.lowercase()?.contains(q) == true)
            }
        }
    }

    ModalInferiorBoveda(
        abierto = abierto,
        alCerrar = alCerrar,
        fijarAbajo = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            // Cabecera
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Seleccionar contacto",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Toca un contacto para rellenar los datos de esta entrada",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }

                IconButton(onClick = alCerrar) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cerrar",
                        tint = TextoSecundario
                    )
                }
            }

            BarraBusquedaContactos(
                consulta = consultaBusqueda,
                alCambiarConsulta = { consultaBusqueda = it }
            )

            if (contactosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay contactos que coincidan con la búsqueda.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(contactosFiltrados, key = { it.id }) { contacto ->
                        val inicial = contacto.nombre.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                        val datoSecundario = contacto.telefonos.firstOrNull()
                            ?: contacto.correos.firstOrNull()
                            ?: contacto.organizacion
                            ?: "Sin datos adicionales"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    alSeleccionar(contacto)
                                    alCerrar()
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ColorAcento.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = inicial,
                                    color = ColorAcento,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = contacto.nombre,
                                    color = TextoPrincipal,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = datoSecundario,
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
