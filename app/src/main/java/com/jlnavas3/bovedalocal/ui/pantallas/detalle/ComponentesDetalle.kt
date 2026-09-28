package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/** Forma unificada para las tarjetas individuales de la pantalla de detalle. */
val FormaTarjetaDetalle = RoundedCornerShape(16.dp)

/**
 * Encabezado de sección normalizado al estilo One UI para grupos de datos en detalle.
 */
@Composable
fun EtiquetaSeccionDetalle(
    texto: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = texto.uppercase(),
        color = ColorAjusteGris,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        ),
        modifier = modifier.padding(start = 16.dp, bottom = 8.dp, top = 4.dp)
    )
}

/**
 * Contenedor atómico para tarjetas de datos en pantalla de detalle:
 * - Superficie suave adaptativa según el color temático.
 * - Borde perimetral sutil de 1 dp (35% opacidad) del color de acento del dato.
 * - Franja vertical izquierda distintiva de 4.5 dp.
 * - Esquinas suavemente redondeadas con soporte de clic y ripple delimitado.
 */
@Composable
fun TarjetaDatoDetalle(
    colorBorde: Color,
    modifier: Modifier = Modifier,
    alPulsar: (() -> Unit)? = null,
    contenido: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(FormaTarjetaDetalle)
            .background(fondoBadgeParaTema(colorBorde))
            .border(1.dp, colorBorde.copy(alpha = 0.35f), FormaTarjetaDetalle)
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

/**
 * Botón de copiar estandarizado con animación elástica de Check / Copiar y dimensiones uniformes (40x40 dp, icono 20 dp).
 */
@Composable
fun BotonCopiarDetalle(
    copiado: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = alPulsar,
        modifier = modifier.size(40.dp)
    ) {
        AnimatedVisibility(
            visible = copiado,
            enter = scaleIn(spring(dampingRatio = 0.5f)),
            exit = scaleOut(spring(dampingRatio = 0.6f))
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Copiado",
                tint = Menta,
                modifier = Modifier.size(20.dp)
            )
        }
        AnimatedVisibility(
            visible = !copiado,
            enter = scaleIn(spring(dampingRatio = 0.5f)),
            exit = scaleOut(spring(dampingRatio = 0.6f))
        ) {
            Icon(
                imageVector = Icons.Filled.ContentCopy,
                contentDescription = "Copiar",
                tint = ColorIconosInternos,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Botón de acción con icono estandarizado para la pantalla de detalle (40x40 dp, icono 20 dp).
 */
@Composable
fun BotonIconoDetalle(
    icono: ImageVector,
    descripcion: String,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = ColorIconosInternos
) {
    IconButton(
        onClick = alPulsar,
        modifier = modifier.size(40.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}
