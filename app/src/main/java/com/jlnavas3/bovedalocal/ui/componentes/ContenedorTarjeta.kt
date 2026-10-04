package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Contenedor tipo tarjeta (equivalente a card container en UI).
 * Aplica el color configurable de tarjetas, bordes configurables y curvatura de esquinas.
 */
@Composable
fun ContenedorTarjeta(
    modifier: Modifier = Modifier,
    colorFondo: Color = ColorTarjetas,
    colorBorde: Color = ColorBordeActual,
    radioEsquinas: Dp = CurvaturaEsquinas,
    grosorBorde: Dp = GrosorBorde,
    paddingInterno: Dp = 16.dp,
    alPulsar: (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(radioEsquinas)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(colorFondo)
            .then(
                if (grosorBorde > 0.dp && colorBorde != Color.Transparent) {
                    Modifier.border(grosorBorde, colorBorde, forma)
                } else {
                    Modifier
                }
            )
            .then(if (alPulsar != null) Modifier.clickable { alPulsar() } else Modifier)
            .padding(paddingInterno)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = contenido
        )
    }
}
