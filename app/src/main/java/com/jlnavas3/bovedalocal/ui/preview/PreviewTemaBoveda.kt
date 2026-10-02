package com.jlnavas3.bovedalocal.ui.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionFormas

/**
 * Contenedor de previsualización que aplica el tema, los colores y las formas y bordes
 * según la configuración (Modo Claro / Modo Oscuro automático según la preview).
 */
@Composable
fun PreviewTemaBoveda(
    ajustes: AjustesApp = AjustesApp(),
    padding: Dp = 16.dp,
    contenido: @Composable () -> Unit
) {
    aplicarPersonalizacionFormas(ajustes)
    val esOscuro = isSystemInDarkTheme()
    BovedaTheme(temaApp = if (esOscuro) "oscuro" else "claro") {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            if (padding > 0.dp) {
                Box(modifier = Modifier.padding(padding)) {
                    contenido()
                }
            } else {
                contenido()
            }
        }
    }
}
