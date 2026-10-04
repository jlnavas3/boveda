package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.BuildConfig
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris

/**
 * Pie de página discreto con la versión de la app y la autoría.
 */
@Composable
fun PieVersionOnboarding(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Bóveda Local · v${BuildConfig.VERSION_NAME} · by: jlnavas3",
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
        color = ColorAjusteGris.copy(alpha = 0.85f),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        textAlign = TextAlign.Center,
        maxLines = 1
    )
}
