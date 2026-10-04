package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.InsigniaIdAjuste
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana

/**
 * Barra superior fija de navegación (Sticky Top Bar) para pantallas secundarias.
 * Queda anclada arriba, fuera del scroll, con botón atrás, título y espacio para acciones.
 */
@Composable
fun BarraSuperiorPantalla(
    titulo: String,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    conSeparador: Boolean = false,
    colorFondo: Color = Color.Unspecified,
    acciones: (@Composable RowScope.() -> Unit)? = null
) {
    val fondoBarra = if (colorFondo != Color.Unspecified) colorFondo else Obsidiana
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(fondoBarra)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonIconoCabecera(
                onClick = alVolver,
                icono = Icons.AutoMirrored.Filled.ArrowBack,
                descripcion = "Volver atrás"
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = titulo,
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                    Spacer(Modifier.height(2.dp))
                    InsigniaIdAjuste(id = idEtiqueta)
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
        if (conSeparador) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ColorBordeActual.copy(alpha = 0.35f))
            )
        }
    }
}
