package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Componente molecular estandarizado para Chips en toda la aplicación.
 * Conserva la altura compacta y esquinas suaves de las identidades, incorporando
 * soporte para iconos temáticos, puntos de estado, checks, contadores y botón de remover.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChipBoveda(
    texto: String,
    modifier: Modifier = Modifier,
    seleccionado: Boolean = false,
    alPulsar: (() -> Unit)? = null,
    alPulsarProlongado: (() -> Unit)? = null,
    alRemover: (() -> Unit)? = null,
    icono: ImageVector? = null,
    conteo: Int? = null,
    conteoTexto: String? = null,
    colorBase: Color = ColorAcento,
    mostrarPunto: Boolean = false,
    mostrarCheck: Boolean = false,
    colorFondoPersonalizado: Color? = null,
    colorTextoPersonalizado: Color? = null,
    colorBordePersonalizado: Color? = null,
    forma: Shape = RoundedCornerShape(16.dp)
) {
    val fondo = colorFondoPersonalizado ?: if (seleccionado) colorBase else ColorTarjetaAjustes

    val borde = colorBordePersonalizado ?: if (seleccionado) {
        colorBase
    } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        ColorBordeActual
    } else if (mostrarPunto) {
        colorBase.copy(alpha = 0.45f)
    } else {
        ColorSeparadorAjustes
    }

    val grosorBorde = if (seleccionado) {
        0.8.dp
    } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        GrosorBorde
    } else {
        0.8.dp
    }

    val modificadorInteraccion = if (alPulsar != null && alPulsarProlongado != null) {
        Modifier.combinedClickable(
            onClick = alPulsar,
            onLongClick = alPulsarProlongado
        )
    } else if (alPulsar != null) {
        Modifier.clickable(onClick = alPulsar)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(forma)
            .background(fondo)
            .border(width = grosorBorde, color = borde, shape = forma)
            .then(modificadorInteraccion)
            .padding(horizontal = 11.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (mostrarCheck && seleccionado) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = ColorSobreAcento,
                    modifier = Modifier.size(13.dp)
                )
            } else if (icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = if (seleccionado) ColorSobreAcento else colorBase,
                    modifier = Modifier.size(14.dp)
                )
            } else if (mostrarPunto) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(if (seleccionado) ColorSobreAcento else colorBase)
                )
            }

            val colorTexto = colorTextoPersonalizado ?: if (seleccionado) ColorSobreAcento else TextoPrincipal
            TextoCuerpo(
                texto = texto,
                tamano = TamanoCuerpo.MINI,
                color = colorTexto,
                maxLineas = 1
            )

            val textoConteo = conteoTexto ?: conteo?.toString()
            if (textoConteo != null) {
                TextoCuerpo(
                    texto = textoConteo,
                    tamano = TamanoCuerpo.MINI,
                    color = if (seleccionado) ColorSobreAcento.copy(alpha = 0.85f) else TextoSecundario,
                    maxLineas = 1
                )
            }

            if (alRemover != null) {
                Spacer(Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Quitar",
                    tint = if (seleccionado) ColorSobreAcento else TextoSecundario,
                    modifier = Modifier
                        .size(13.dp)
                        .clickable(onClick = alRemover)
                )
            }
        }
    }
}
