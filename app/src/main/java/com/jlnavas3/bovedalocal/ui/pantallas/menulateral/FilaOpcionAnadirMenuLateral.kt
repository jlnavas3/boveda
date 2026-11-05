/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.menulateral

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.NodoAjuste
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta

/**
 * Fila interactiva para cada opción disponible en el diálogo de añadir a la barra lateral:
 * - Al pulsar sobre el cuerpo (texto o icono principal): ejecuta [alPulsarFila] (añade unitario y cierra).
 * - Al pulsar sobre el icono derecho de acción: ejecuta [alConmutarAdicion] (añade o desmarca en selección múltiple continua).
 */
@Composable
fun FilaOpcionAnadirMenuLateral(
    nodo: NodoAjuste,
    estaSeleccionado: Boolean,
    mostrarIds: Boolean,
    alPulsarFila: () -> Unit,
    alConmutarAdicion: () -> Unit
) {
    val (icono, color) = MapaAjustes.resolverIconoYColor(nodo)
    val colorBotonAccion by animateColorAsState(
        targetValue = if (estaSeleccionado) Menta else ColorAcento,
        label = "colorBotonAccion"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { alPulsarFila() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(FormaPequena)
                .background(ColorCampoAjustes),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = ColorIconosInternos,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nodo.titulo,
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            )
            if (nodo.subtitulo.isNotBlank()) {
                Text(
                    text = nodo.subtitulo,
                    color = ColorAjusteGris,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            if (mostrarIds) {
                Text(
                    text = nodo.id,
                    color = ColorAcento,
                    style = EstiloMono.copy(fontSize = 10.sp)
                )
            }
        }

        IconButton(
            onClick = alConmutarAdicion,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        if (estaSeleccionado) Menta.copy(alpha = 0.16f)
                        else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (estaSeleccionado) Icons.Filled.Check else Icons.Filled.Add,
                    contentDescription = if (estaSeleccionado) "Quitar de selección" else "Añadir a selección",
                    tint = colorBotonAccion,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
