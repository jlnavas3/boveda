/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Componente universal de estado vacío sobrio en escala de grises / neutro.
 * Sin botones o enlaces redundantes en medio, dejando la acción al botón flotante (+).
 */
@Composable
fun EstadoVacioPantalla(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ContenedorIconoInsignia(
                icono = icono,
                tamano = TamanoInsignia.HERO,
                colorFondo = ColorAcento.copy(alpha = 0.12f),
                colorIcono = ColorAcento,
                conBorde = true
            )
            Spacer(Modifier.height(18.dp))
            TextoTitulo(
                texto = titulo,
                estilo = EstiloTitulo.MEDIANO,
                alineacion = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            TextoSubtitulo(
                texto = subtitulo,
                alineacion = TextAlign.Center
            )
        }
    }
}
