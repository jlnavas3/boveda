package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Cabecera de navegación unificada para todas las pantallas secundarias de la app.
 * Incluye flecha hacia atrás vinculada a la pila de navegación, título, subtítulo opcional
 * y espacio para acciones contextuales en el extremo derecho.
 */
@Composable
fun CabeceraPantalla(
    titulo: String,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    acciones: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = alVolver,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver atrás",
                tint = ColorIconosInternos,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = ColorTitulos,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            if (!subtitulo.isNullOrBlank()) {
                Text(
                    text = subtitulo,
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if (acciones != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = acciones
            )
        }
    }
}
