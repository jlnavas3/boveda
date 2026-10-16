/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.AdjuntoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.ExportadorAdjuntosSaf

/**
 * Tarjeta de detalle que agrupa los archivos adjuntos cifrados de una entrada con opciones
 * para previsualizar imágenes/texto y exportar cualquier archivo al almacenamiento local.
 */
@Composable
fun TarjetaAdjuntosDetalle(
    adjuntos: List<AdjuntoBoveda>,
    modifier: Modifier = Modifier
) {
    if (adjuntos.isEmpty()) return

    val contexto = LocalContext.current
    var adjuntoParaVisualizar by remember { mutableStateOf<AdjuntoBoveda?>(null) }
    var adjuntoParaExportar by remember { mutableStateOf<AdjuntoBoveda?>(null) }

    val lanzadorGuardar = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        BovedaApp.salidaTerminada(contexto)
        val adjunto = adjuntoParaExportar
        if (uri != null && adjunto != null) {
            val res = ExportadorAdjuntosSaf.guardarEnUri(contexto, adjunto, uri)
            res.onSuccess {
                Toast.makeText(contexto, "Archivo guardado con éxito", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(contexto, "Error al guardar: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        adjuntoParaExportar = null
    }

    Column(modifier = modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Archivos adjuntos (${adjuntos.size})")

        TarjetaDatoDetalle(colorBorde = ColorAcento) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                adjuntos.forEach { adjunto ->
                    FilaAdjuntoDetalle(
                        adjunto = adjunto,
                        alVisualizar = {
                            adjuntoParaVisualizar = adjunto
                        },
                        alExportar = {
                            adjuntoParaExportar = adjunto
                            BovedaApp.salidaPendiente(contexto)
                            try {
                                lanzadorGuardar.launch(adjunto.nombre)
                            } catch (_: Exception) {
                                BovedaApp.salidaTerminada(contexto)
                                Toast.makeText(contexto, "No se pudo iniciar el guardado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    adjuntoParaVisualizar?.let { adjunto ->
        DialogoVisorAdjunto(
            adjunto = adjunto,
            alCerrar = { adjuntoParaVisualizar = null }
        )
    }
}
