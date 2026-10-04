package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Opción de menú desplegable compacta, ergonómica y totalmente adaptada al sistema
 * de tipografía y temas de la aplicación.
 */
@Composable
fun ElementoMenuCompacto(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorIcono: Color = ColorAcento,
    colorTexto: Color = TextoPrincipal,
    iconoFinal: ImageVector? = null,
    colorIconoFinal: Color = ColorIconosInternos.copy(alpha = 0.6f),
    habilitado: Boolean = true
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 38.dp)
            .clickable(enabled = habilitado) {
                haptica.tic()
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (habilitado) colorIcono else colorIcono.copy(alpha = 0.38f),
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(10.dp))
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = if (habilitado) colorTexto else colorTexto.copy(alpha = 0.38f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (iconoFinal != null) {
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = iconoFinal,
                contentDescription = null,
                tint = if (habilitado) colorIconoFinal else colorIconoFinal.copy(alpha = 0.38f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
