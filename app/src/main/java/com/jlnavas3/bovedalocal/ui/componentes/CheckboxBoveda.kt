package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Casilla de verificación (Checkbox) unificada para toda la app.
 * Encapsula la háptica y los colores del tema.
 */
@Composable
fun CheckboxBoveda(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorAcento: Color = ColorAcento
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Checkbox(
        checked = checked,
        onCheckedChange = { nuevoValor ->
            haptica.tic()
            onCheckedChange?.invoke(nuevoValor)
        },
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = colorAcento,
            uncheckedColor = TextoSecundario,
            checkmarkColor = ColorSobreAcento
        )
    )
}
