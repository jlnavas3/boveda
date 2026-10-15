/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Contenedor temático de tarjeta para artículos de ayuda que respeta plenamente el tema,
 * curvatura de esquinas y bordes del usuario.
 */
@Composable
fun SeccionGrupoAyuda(
    tituloGrupo: String,
    articulos: List<ArticuloAyuda>,
    alSeleccionarArticulo: (ArticuloAyuda) -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = tituloGrupo.uppercase(),
            color = ColorAjusteGris,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            ),
            modifier = Modifier.padding(start = 12.dp, bottom = 6.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(ColorTarjetaAjustes)
                .then(
                    if (GrosorBorde > 0.dp) {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else Modifier
                )
        ) {
            articulos.forEachIndexed { index, articulo ->
                FilaArticuloAyuda(
                    articulo = articulo,
                    alPulsar = { alSeleccionarArticulo(articulo) }
                )
                if (index < articulos.lastIndex) {
                    ComponenteSeparador(sangriaInicio = 16.dp)
                }
            }
        }
    }
}
