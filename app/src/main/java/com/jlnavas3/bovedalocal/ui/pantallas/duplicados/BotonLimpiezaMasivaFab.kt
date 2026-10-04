package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente del botón flotante de acción para ejecutar la limpieza masiva
 * de credenciales duplicadas idénticas en un solo toque.
 */
@Composable
fun BotonLimpiezaMasivaFab(
    visible: Boolean,
    totalSobrantesIdenticas: Int,
    haptica: Haptica,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = modifier.padding(end = 16.dp, bottom = 16.dp)
    ) {
        val formaFab = RoundedCornerShape(CurvaturaEsquinas)
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(formaFab)
                .background(ColorAcento)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                    } else Modifier
                )
                .clickable {
                    haptica.toque()
                    alPulsar()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AutoFixHigh,
                contentDescription = "Limpiar $totalSobrantesIdenticas copias idénticas",
                tint = ColorSobreAcento,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
