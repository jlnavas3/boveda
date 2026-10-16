/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AdjuntoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import java.util.Base64

/**
 * Diálogo desacoplado para previsualizar el contenido seguro de un archivo adjunto
 * (imágenes o texto plano) sin escribirlo en disco ni usar internet.
 */
@Composable
fun DialogoVisorAdjunto(
    adjunto: AdjuntoBoveda,
    alCerrar: () -> Unit
) {
    val bytes = remember(adjunto.contenidoBase64) {
        try {
            Base64.getDecoder().decode(adjunto.contenidoBase64)
        } catch (_: Exception) {
            byteArrayOf()
        }
    }

    val esImagen = remember(adjunto.mimeType) {
        adjunto.mimeType.startsWith("image/")
    }

    val imagenBitmap = remember(bytes, esImagen) {
        if (esImagen && bytes.isNotEmpty()) {
            try {
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
            } catch (_: Exception) {
                null
            }
        } else null
    }

    val textoContenido = remember(bytes, esImagen) {
        if (!esImagen && bytes.isNotEmpty() && (adjunto.mimeType.startsWith("text/") || adjunto.mimeType.contains("json") || adjunto.mimeType.contains("pem"))) {
            try {
                String(bytes, Charsets.UTF_8).take(10_000)
            } catch (_: Exception) {
                null
            }
        } else null
    }

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = adjunto.nombre,
        botonConfirmar = {
            BotonTextoBoveda(
                texto = "Cerrar",
                alPulsar = alCerrar
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            when {
                imagenBitmap != null -> {
                    Image(
                        bitmap = imagenBitmap,
                        contentDescription = adjunto.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp)
                            .clip(FormaTarjeta),
                        contentScale = ContentScale.Fit
                    )
                }
                textoContenido != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                            .clip(FormaTarjeta)
                            .background(ColorCampoAjustes)
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = textoContenido,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoPrincipal
                        )
                    }
                }
                else -> {
                    Text(
                        text = "Este formato de archivo (${adjunto.mimeType}) no admite vista previa en pantalla. Puedes guardarlo o exportarlo en tu almacenamiento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoPrincipal
                    )
                }
            }
        }
    }
}
