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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/** Forma unificada para las tarjetas individuales de la pantalla de detalle, reactiva a "Formas y bordes". */
val FormaTarjetaDetalle: RoundedCornerShape
    get() = RoundedCornerShape(CurvaturaEsquinas)

/** Espaciado vertical entre cuadros en la pantalla de detalle, reactivo a "Formas y bordes". */
val EspaciadoDetalle: Dp
    get() = (EspaciadoComponentes * 0.55f).coerceIn(6.dp, 16.dp)

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
 * - Borde perimetral sutil que responde dinámicamente al grosor configurado en "Formas y bordes".
 * - Curvatura de esquinas vinculada a CurvaturaEsquinas.
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
