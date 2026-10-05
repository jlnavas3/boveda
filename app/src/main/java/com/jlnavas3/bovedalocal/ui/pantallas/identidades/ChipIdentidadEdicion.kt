package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Chip seleccionable individual para vincular una identidad dentro del formulario de edición.
 */
@Composable
fun ChipIdentidadEdicion(
    titulo: String,
    seleccionado: Boolean,
    colorBase: Color = ColorAcento,
    mostrarPunto: Boolean = false,
    mostrarIconoCheck: Boolean = false,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(14.dp)
    val fondo = if (seleccionado) colorBase else ColorTarjetaAjustes
    val colorBorde = if (seleccionado) colorBase else if (mostrarPunto) colorBase.copy(alpha = 0.45f) else ColorSeparadorAjustes

    Box(
        modifier = modifier
            .clip(forma)
            .background(fondo)
            .border(width = 0.8.dp, color = colorBorde, shape = forma)
            .clickable(onClick = alPulsar)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (mostrarIconoCheck && seleccionado) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = ColorSobreAcento,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
            } else if (mostrarPunto) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(if (seleccionado) ColorSobreAcento else colorBase)
                )
                Spacer(Modifier.width(6.dp))
            }
            TextoCuerpo(
                texto = titulo,
                tamano = TamanoCuerpo.MINI,
                color = if (seleccionado) ColorSobreAcento else if (mostrarPunto) TextoPrincipal else TextoSecundario,
                maxLineas = 1
            )
        }
    }
}
