package com.jlnavas3.bovedalocal.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multi-preview estándar para componentes de Bóveda Local.
 * Genera de forma paralela la vista en Modo Claro y Modo Oscuro.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
@Preview(
    name = "1. Modo Claro",
    group = "Tema",
    showBackground = true,
    backgroundColor = 0xFFF6F8FC
)
@Preview(
    name = "2. Modo Oscuro",
    group = "Tema",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF121214
)
annotation class BovedaPreview

/**
 * Multi-preview para pantallas completas con barra de estado y navegación del sistema.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
@Preview(
    name = "1. Pantalla Claro",
    group = "Pantalla",
    showSystemUi = true
)
@Preview(
    name = "2. Pantalla Oscuro",
    group = "Pantalla",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true
)
annotation class BovedaPantallaPreview
