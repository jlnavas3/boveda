/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Campo de búsqueda ágil para el Centro de Ayuda que respeta el tema y bordes configurados.
 */
@Composable
fun BarraBusquedaAyuda(
    consulta: String,
    alCambiarConsulta: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else Modifier
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = ColorIconosInternos,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(8.dp))

        BasicTextField(
            value = consulta,
            onValueChange = alCambiarConsulta,
            singleLine = true,
            cursorBrush = SolidColor(ColorAcento),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = TextoPrincipal,
                fontSize = 14.sp
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { campoTexto ->
                if (consulta.isEmpty()) {
                    Text(
                        text = "Buscar temas de ayuda...",
                        color = TextoSecundario.copy(alpha = 0.65f),
                        fontSize = 13.5.sp
                    )
                }
                campoTexto()
            }
        )

        if (consulta.isNotEmpty()) {
            IconButton(
                onClick = { alCambiarConsulta("") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Limpiar búsqueda",
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
