package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada

/**
 * VisualTransformation que aplica color a caracteres de contraseñas visibles.
 */
internal class ContrasenaColorVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val coloreado = contrasenaColoreada(text.text)
        return TransformedText(coloreado, OffsetMapping.Identity)
    }
}

/**
 * Tipos de campo de entrada para adaptación automática de teclado, iconos y transformación.
 */
enum class TipoCampoTexto {
    TEXTO,
    NUMERICO,
    CONTRASENA,
    FECHA,
    HORA,
    ENLACE,
    MULTILINEA
}

/**
 * Funciones de utilidad para resolver la transformación visual y el teclado en campos de texto.
 */
internal object ConfiguradorCampoTexto {

    fun resolverTransformacionVisual(esContrasena: Boolean, esVisible: Boolean): VisualTransformation {
        return when {
            esContrasena && !esVisible -> PasswordVisualTransformation()
            esContrasena && esVisible -> ContrasenaColorVisualTransformation()
            else -> VisualTransformation.None
        }
    }

    fun resolverTipoTeclado(
        tipo: TipoCampoTexto,
        tecladoNumerico: Boolean,
        esContrasena: Boolean,
        keyboardTypeOverride: KeyboardType?
    ): KeyboardType {
        return keyboardTypeOverride ?: when {
            tecladoNumerico || tipo == TipoCampoTexto.NUMERICO -> if (esContrasena) KeyboardType.NumberPassword else KeyboardType.Number
            esContrasena || tipo == TipoCampoTexto.CONTRASENA -> KeyboardType.Password
            tipo == TipoCampoTexto.ENLACE -> KeyboardType.Uri
            else -> KeyboardType.Text
        }
    }
}
