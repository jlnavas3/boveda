package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Menta

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
