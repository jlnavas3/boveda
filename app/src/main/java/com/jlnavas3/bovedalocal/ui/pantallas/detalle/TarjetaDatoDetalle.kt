package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/** Forma unificada para las tarjetas individuales de la pantalla de detalle, reactiva a "Formas y bordes". */
val FormaTarjetaDetalle: RoundedCornerShape
    get() = RoundedCornerShape(CurvaturaEsquinas)

/** Espaciado vertical entre cuadros en la pantalla de detalle, reactivo a "Formas y bordes". */
val EspaciadoDetalle: Dp
    get() = (EspaciadoComponentes * 0.55f).coerceIn(6.dp, 16.dp)

/**
 * Contenedor atómico para tarjetas de datos en pantalla de detalle.
 */
@Composable
fun TarjetaDatoDetalle(
    colorBorde: Color,
    modifier: Modifier = Modifier,
    alPulsar: (() -> Unit)? = null,
    contenido: @Composable BoxScope.() -> Unit
) {
    val forma = FormaTarjetaDetalle
    val grosorEfectivo = if (GrosorBorde > 0.dp) GrosorBorde else 1.dp
    val colorBordeEfectivo = colorBorde.copy(alpha = 0.35f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .border(grosorEfectivo, colorBordeEfectivo, forma)
            .then(
                if (alPulsar != null) Modifier.clickable { alPulsar() }
                else Modifier
            )
    ) {
        contenido()
        // Franja vertical izquierda acentuada
        Box(
            modifier = Modifier.matchParentSize()
        ) {
            Box(
                modifier = Modifier
                    .width(4.5.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .background(colorBorde)
            )
        }
    }
}
