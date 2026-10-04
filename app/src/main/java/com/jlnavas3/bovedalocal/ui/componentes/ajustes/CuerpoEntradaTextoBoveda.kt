package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.jlnavas3.bovedalocal.util.FormateadorCampos

/**
 * Microcomponente que renderiza el TextField nativo de Compose soportando tanto
 * edición plana como formateo dinámico reactivo con máscara de cursor (TextFieldValue).
 */
@Composable
fun CuerpoEntradaTextoBoveda(
    valor: String,
    etiqueta: String,
    alCambiar: (String) -> Unit,
    formateadorMascara: ((String) -> String)?,
    composablePlaceholder: (@Composable () -> Unit)?,
    readOnly: Boolean,
    habilitado: Boolean,
    esError: Boolean,
    varias: Boolean,
    estiloTexto: TextStyle,
    transformacionVisual: VisualTransformation,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
    opcionesTeclado: KeyboardOptions,
    keyboardActions: KeyboardActions,
    forma: RoundedCornerShape,
    colores: TextFieldColors,
    interactionSource: MutableInteractionSource
) {
    if (formateadorMascara != null) {
        var tfv by remember {
            mutableStateOf(TextFieldValue(text = valor, selection = TextRange(valor.length)))
        }
        if (tfv.text != valor) {
            val nuevoCursor = tfv.selection.end.coerceIn(0, valor.length)
            tfv = tfv.copy(text = valor, selection = TextRange(nuevoCursor))
        }
        TextField(
            value = tfv,
            onValueChange = { nuevo ->
                val transformado = FormateadorCampos.transformarConMascara(
                    nuevoTfv = nuevo,
                    textoAnterior = tfv.text,
                    formatear = formateadorMascara
                )
                tfv = transformado
                alCambiar(transformado.text)
            },
            label = { Text(etiqueta) },
            placeholder = composablePlaceholder,
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            enabled = habilitado,
            isError = esError,
            singleLine = !varias,
            minLines = if (varias) 3 else 1,
            textStyle = estiloTexto,
            visualTransformation = transformacionVisual,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = opcionesTeclado,
            keyboardActions = keyboardActions,
            shape = forma,
            colors = colores,
            interactionSource = interactionSource
        )
    } else {
        TextField(
            value = valor,
            onValueChange = alCambiar,
            label = { Text(etiqueta) },
            placeholder = composablePlaceholder,
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            enabled = habilitado,
            isError = esError,
            singleLine = !varias,
            minLines = if (varias) 3 else 1,
            textStyle = estiloTexto,
            visualTransformation = transformacionVisual,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = opcionesTeclado,
            keyboardActions = keyboardActions,
            shape = forma,
            colors = colores,
            interactionSource = interactionSource
        )
    }
}
