/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import kotlinx.coroutines.delay

/**
 * Fila protegida visualmente para previsualizar la contraseña importada respetando
 * la configuración de 'Seguridad visual' (desenfoque o máscara, y auto-ocultamiento).
 */
@Composable
fun FilaContrasenaImportacionCxf(
    contrasena: String,
    seguridadVisualActiva: Boolean,
    ocultarContrasena: Boolean,
    estiloOcultamiento: String,
    tiempoAutoOcultarSegundos: Int,
    modifier: Modifier = Modifier
) {
    if (contrasena.isBlank()) return

    var revelada by remember { mutableStateOf(false) }

    LaunchedEffect(revelada) {
        if (revelada && seguridadVisualActiva && tiempoAutoOcultarSegundos > 0) {
            delay(tiempoAutoOcultarSegundos * 1000L)
            revelada = false
        }
    }

    val protegida = seguridadVisualActiva && ocultarContrasena && !revelada

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextoSeguroVisual(
            texto = contrasena,
            oculto = protegida,
            estilo = estiloOcultamiento,
            estiloTexto = if (!protegida) {
                MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.5.sp
                )
            } else {
                MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
            },
            colorTexto = ColorTextoAjustes.copy(alpha = 0.9f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        if (seguridadVisualActiva && ocultarContrasena) {
            IconButton(
                onClick = { revelada = !revelada },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (revelada) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = ColorAjusteGris,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
