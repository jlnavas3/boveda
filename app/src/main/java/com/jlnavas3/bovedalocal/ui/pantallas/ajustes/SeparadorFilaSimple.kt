package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Separador visual simple con sangría configurable (predeterminado 16.dp).
 */
@Composable
fun SeparadorFilaSimple(modifier: Modifier = Modifier, paddingInicio: Dp = 16.dp) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddingInicio)
            .height(0.5.dp)
            .background(ColorSeparadorAjustes)
    )
}
