/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.AlgoritmoSsh
import com.jlnavas3.bovedalocal.crypto.GeneradorClavesSsh
import com.jlnavas3.bovedalocal.crypto.ParClavesSsh
import com.jlnavas3.bovedalocal.ui.componentes.BotonPrimario
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles

/**
 * Diálogo modal micro desacoplado para la generación offline de pares de claves SSH (Ed25519, RSA, ECDSA),
 * visualización de huella digital SHA-256 e inserción directa en la entrada de servidor.
 */
@Composable
fun DialogoGeneradorClavesSsh(
    alCerrar: () -> Unit,
    alAplicar: (privadaPem: String, publicaSsh: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    var algoritmo by remember { mutableStateOf(AlgoritmoSsh.ED25519) }
    var comentario by remember { mutableStateOf("boveda@local") }
    var parGenerado by remember { mutableStateOf<ParClavesSsh?>(null) }
    var mensajeCopiado by remember { mutableStateOf<String?>(null) }

    fun regenerar() {
        haptica.tic()
        parGenerado = GeneradorClavesSsh.generar(algoritmo, comentario)
        mensajeCopiado = null
    }

    LaunchedEffect(Unit) {
        regenerar()
    }

    AlertDialog(
        onDismissRequest = alCerrar,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Dns,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("Generador de claves SSH", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Genera un par de claves criptográficas seguras localmente sin conexión. Puedes insertar la clave privada y pública directamente en la ficha del servidor.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )

                // Selector de algoritmo
                Text(
                    text = "Tipo de clave",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TextoPrincipal
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AlgoritmoSsh.entries.forEach { item ->
                        val seleccionado = item == algoritmo
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(FormaPequena)
                                .background(if (seleccionado) ColorAcento else ColorCampoAjustes)
                                .clickable {
                                    algoritmo = item
                                    regenerar()
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.etiqueta,
                                fontSize = 11.sp,
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                                color = if (seleccionado) MaterialTheme.colorScheme.onPrimary else TextoPrincipal
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Comentario (ej. usuario@host)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { regenerar() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Regenerar clave")
                    }
                }

                parGenerado?.let { par ->
                    // Huella SHA-256
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaPequena)
                            .background(ColorCampoAjustes)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Huella SHA-256:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextoSecundario
                        )
                        Text(
                            text = par.huellaSha256,
                            style = EstiloMono.copy(fontSize = 11.sp),
                            color = ColorAcento
                        )
                    }

                    // Clave pública OpenSSH
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaPequena)
                            .background(ColorCampoAjustes)
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Clave pública (OpenSSH):",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextoSecundario
                            )
                            TextButton(
                                onClick = {
                                    Portapapeles.copiar(contexto, "Clave pública SSH", par.clavePublicaSsh)
                                    haptica.exito()
                                    mensajeCopiado = "¡Clave pública copiada!"
                                }
                            ) {
                                Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Copiar", fontSize = 11.sp)
                            }
                        }
                        Text(
                            text = par.clavePublicaSsh,
                            style = EstiloMono.copy(fontSize = 10.sp),
                            color = TextoPrincipal,
                            maxLines = 3
                        )
                    }

                    mensajeCopiado?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = ColorAcento,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        confirmButton = {
            BotonPrimario(
                texto = "Insertar en entrada",
                alPulsar = {
                    parGenerado?.let { par ->
                        haptica.exito()
                        alAplicar(par.clavePrivadaPem, par.clavePublicaSsh)
                    }
                }
            )
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar")
            }
        },
        modifier = modifier
    )
}
