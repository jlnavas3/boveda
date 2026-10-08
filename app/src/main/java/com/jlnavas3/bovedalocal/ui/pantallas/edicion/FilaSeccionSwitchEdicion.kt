package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente homogéneo con Switch para opciones binarias del formulario (Favorito, Ignorar en salud),
 * compartiendo las mismas dimensiones y lenguaje visual que FilaSeccionColapsableEdicion.
 */
@Composable
fun FilaSeccionSwitchEdicion(
    icono: ImageVector,
    colorIcono: Color,
    titulo: String,
    descripcion: String,
    activado: Boolean,
    alCambiar: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = FormaCampo
    val colorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        ColorBordeActual
    } else {
        ColorSeparadorAjustes.copy(alpha = 0.4f)
    }
    val grosor = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 0.8.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorCampoAjustes)
            .border(grosor, colorBorde, forma)
            .clickable { alCambiar(!activado) }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(colorIcono.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = TextoPrincipal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                color = TextoSecundario,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(8.dp))

        SwitchBoveda(
            checked = activado,
            onCheckedChange = alCambiar
        )
    }
}
