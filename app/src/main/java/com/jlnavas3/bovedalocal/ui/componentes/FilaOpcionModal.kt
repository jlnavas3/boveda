package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Fila de opción estándar dentro de un modal o diálogo.
 * Respeta `FormaCampo` y dibuja un borde perimetral sutil al estar seleccionada.
 */
@Composable
fun FilaOpcionModal(
    titulo: String,
    seleccionado: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    descripcion: String? = null,
    icono: ImageVector? = null,
    colorAcento: Color = ColorAcento,
    controlFinal: @Composable (() -> Unit)? = null
) {
    val forma = FormaCampo
    val modificadorBorde = if (seleccionado && GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        Modifier.border(GrosorBorde, colorAcento.copy(alpha = 0.45f), forma)
    } else Modifier

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Color.Transparent)
            .then(modificadorBorde)
            .clickable { alPulsar() }
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (seleccionado) colorAcento else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                )
            )
            if (!descripcion.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = descripcion,
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )
            }
        }

        if (controlFinal != null) {
            Spacer(Modifier.width(8.dp))
            controlFinal()
        }
    }
}
