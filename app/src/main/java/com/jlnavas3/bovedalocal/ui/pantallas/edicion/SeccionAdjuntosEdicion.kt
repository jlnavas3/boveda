/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.AdjuntoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.LectorAdjuntosSaf

/**
 * Sección desacoplada del formulario de edición para gestionar archivos adjuntos cifrados.
 */
@Composable
fun SeccionAdjuntosEdicion(
    adjuntos: List<AdjuntoBoveda>,
    alCambiarAdjuntos: (List<AdjuntoBoveda>) -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    val selectorArchivo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        BovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            val resultado = LectorAdjuntosSaf.leerAdjunto(contexto, uri)
            resultado.onSuccess { nuevoAdjunto ->
                alCambiarAdjuntos(adjuntos + nuevoAdjunto)
                Toast.makeText(contexto, "Archivo adjuntado: ${nuevoAdjunto.nombre}", Toast.LENGTH_SHORT).show()
            }.onFailure { e ->
                Toast.makeText(contexto, e.message ?: "Error al adjuntar archivo", Toast.LENGTH_LONG).show()
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        EtiquetaSeccion(texto = "Archivos adjuntos cifrados", icono = Icons.Filled.AttachFile)
        Spacer(Modifier.height(4.dp))

        ContenedorTarjeta {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (adjuntos.isNotEmpty()) {
                    adjuntos.forEach { adjunto ->
                        FilaAdjuntoEdicion(
                            adjunto = adjunto,
                            alEliminar = {
                                alCambiarAdjuntos(adjuntos.filterNot { it.id == adjunto.id })
                            }
                        )
                    }
                }

                BotonBorde(
                    texto = "Adjuntar archivo seguro",
                    icono = Icons.Filled.AttachFile,
                    alPulsar = {
                        BovedaApp.salidaPendiente(contexto)
                        try {
                            selectorArchivo.launch(arrayOf("*/*"))
                        } catch (e: Exception) {
                            BovedaApp.salidaTerminada(contexto)
                            Toast.makeText(contexto, "No se pudo abrir el selector de archivos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Los adjuntos se cifran localmente dentro del archivo de la bóveda (.bvda). Tamaño máximo de 2 MB por archivo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }
        }
    }
}
