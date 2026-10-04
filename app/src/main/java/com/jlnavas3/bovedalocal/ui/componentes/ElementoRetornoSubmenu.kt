package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Cabecera de navegación para submenús desplegables multinivel con botón atrás integrado.
 */
@Composable
fun ElementoRetornoSubmenu(
    titulo: String,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 38.dp)
            .clickable {
                haptica.tic()
                alVolver()
            }
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Volver",
            tint = ColorAcento,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = TextoPrincipal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
