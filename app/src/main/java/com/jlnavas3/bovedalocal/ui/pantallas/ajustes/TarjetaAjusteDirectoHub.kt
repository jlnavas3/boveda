/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.InsigniaIdAjuste
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Tarjeta para opciones individuales de ajuste ubicadas directamente en el nivel raíz de Ajustes.
 * Permite acceder de inmediato a la pantalla o acción del ajuste sin requerir desplegar un grupo.
 */
@Composable
fun TarjetaAjusteDirectoHub(
    nodo: NodoAjuste,
    alPulsar: () -> Unit,
    mostrarId: Boolean,
    modifier: Modifier = Modifier
) {
    val formaTarjeta = RoundedCornerShape(CurvaturaEsquinas)
    val (icono, _) = MapaAjustes.resolverIconoYColor(nodo)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaTarjeta)
                } else Modifier
            )
            .clip(formaTarjeta)
            .background(ColorTarjetaAjustes)
            .clickable(onClick = alPulsar)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Squircle neutro con icono representativo dimensionado homogéneo
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(ColorCampoAjustes),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = ColorIconosInternos,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(Modifier.width(10.dp))

        // Textos: Título e ID
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nodo.titulo,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = TextoPrincipal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (mostrarId) {
                Spacer(Modifier.width(6.dp))
                InsigniaIdAjuste(id = nodo.id)
            }
        }

        Spacer(Modifier.width(12.dp))

        // Flecha indicadora de navegación a pantalla
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = ColorIconosInternos,
            modifier = Modifier.size(20.dp)
        )
    }
}
