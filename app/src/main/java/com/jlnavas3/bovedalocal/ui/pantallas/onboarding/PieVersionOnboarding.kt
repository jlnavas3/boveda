package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.BuildConfig
import com.jlnavas3.bovedalocal.ui.componentes.TextoPiePagina

/**
 * Pie de página discreto con la versión de la app y la autoría.
 */
@Composable
fun PieVersionOnboarding(
    modifier: Modifier = Modifier
) {
    TextoPiePagina(
        texto = "Bóveda Local · v${BuildConfig.VERSION_NAME} · by: jlnavas3",
        modifier = modifier.padding(bottom = 12.dp)
    )
}
