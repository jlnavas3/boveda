package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

val ColorDigitos = Color(0xFFFFB74D)

fun contrasenaColoreada(texto: String): AnnotatedString = buildAnnotatedString {
    texto.forEach { c ->
        val color = when {
            c.isDigit() -> ColorDigitos
            c.isLetter() -> TextoPrincipal
            else -> Menta
        }
        withStyle(SpanStyle(color = color)) { append(c) }
    }
}
