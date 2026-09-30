package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Diálogo modal base unificado para toda la aplicación.
 *
 * Encapsula la lógica de diseño, fondo adaptativo, curvatura reactiva (`CurvaturaEsquinas`)
 * y aplicación automática de bordes dinámicos (`GrosorBorde`, `ColorBordeActual`, `EstiloBorde`),
 * eliminando la duplicación y contenedores hardcodeados en las pantallas.
 */
@Composable
fun DialogoBoveda(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(CurvaturaEsquinas),
    containerColor: Color = if (esOscuroActivo) Color(0xFF212023) else Color(0xFFFFFFFF),
    tonalElevation: Dp = 0.dp
) {
    val formaDialogo = shape as? RoundedCornerShape ?: RoundedCornerShape(CurvaturaEsquinas)
    val modificadorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        Modifier.border(GrosorBorde, ColorBordeActual, formaDialogo)
    } else Modifier

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier.then(modificadorBorde),
        dismissButton = dismissButton,
        icon = icon,
        title = title,
        text = text,
        shape = formaDialogo,
        containerColor = containerColor,
        tonalElevation = tonalElevation
    )
}

/**
 * Sobrecarga de conveniencia declarativa de alto nivel para DialogoBoveda.
 */
@Composable
fun DialogoBoveda(
    abierto: Boolean = true,
    alCerrar: () -> Unit,
    modifier: Modifier = Modifier,
    titulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color = ColorAcento,
    fondoIcono: Color? = null,
    botonConfirmar: (@Composable () -> Unit)? = null,
    botonDescartar: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(CurvaturaEsquinas),
    containerColor: Color = if (esOscuroActivo) Color(0xFF212023) else Color(0xFFFFFFFF),
    tonalElevation: Dp = 0.dp,
    contenido: @Composable ColumnScope.() -> Unit
) {
    if (!abierto) return

    DialogoBoveda(
        onDismissRequest = alCerrar,
        confirmButton = { botonConfirmar?.invoke() },
        modifier = modifier,
        dismissButton = botonDescartar,
        icon = icono?.let {
            {
                if (fondoIcono != null) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(fondoIcono),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = colorIcono,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = colorIcono,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        title = titulo?.let {
            {
                Text(
                    text = it,
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column {
                contenido()
            }
        },
        shape = shape,
        containerColor = containerColor,
        tonalElevation = tonalElevation
    )
}
