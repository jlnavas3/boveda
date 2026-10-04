package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Fila con indicador de botón de radio nativo para selectores de opción única.
 */
@Composable
fun FilaOpcionRadio(
    titulo: String,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    seleccionado: Boolean,
    alSeleccionar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptica.tic()
                alSeleccionar()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono ?: (if (seleccionado) ColorAcento else ColorAjusteGris),
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(14.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 15.5.sp
                )
            )
            if (!subtitulo.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(
                    width = if (seleccionado) 6.dp else 1.5.dp,
                    color = if (seleccionado) ColorAcento else ColorAjusteGris.copy(alpha = 0.5f),
                    shape = CircleShape
                )
        )
    }
}
