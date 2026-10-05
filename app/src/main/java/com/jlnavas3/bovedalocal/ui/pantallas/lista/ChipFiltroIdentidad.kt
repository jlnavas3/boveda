package com.jlnavas3.bovedalocal.ui.pantallas.lista

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
 * Chip individual para el filtrado rápido por identidad en la cabecera de la lista principal.
 */
@Composable
fun ChipFiltroIdentidad(
    titulo: String,
    conteo: Int,
    seleccionado: Boolean,
    colorBase: Color = ColorAcento,
    mostrarPunto: Boolean = false,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(16.dp)
    val fondo = if (seleccionado) colorBase else ColorTarjetaAjustes
    val colorBorde = if (seleccionado) colorBase else if (mostrarPunto) colorBase.copy(alpha = 0.45f) else ColorSeparadorAjustes

    Box(
        modifier = modifier
            .clip(forma)
            .background(fondo)
            .border(width = 0.8.dp, color = colorBorde, shape = forma)
            .clickable(onClick = alPulsar)
            .padding(horizontal = 11.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (mostrarPunto) {
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
                color = if (seleccionado) ColorSobreAcento else TextoPrincipal,
                maxLineas = 1
            )
            Spacer(Modifier.width(5.dp))
            TextoCuerpo(
                texto = "$conteo",
                tamano = TamanoCuerpo.MINI,
                color = if (seleccionado) ColorSobreAcento.copy(alpha = 0.85f) else TextoSecundario,
                maxLineas = 1
            )
        }
    }
}
