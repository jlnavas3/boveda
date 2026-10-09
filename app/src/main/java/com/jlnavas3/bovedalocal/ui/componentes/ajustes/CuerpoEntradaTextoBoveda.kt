package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.blindajeSemanticoSensible
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.FormateadorCampos

/**
 * Microcomponente que renderiza el TextField nativo de Compose soportando tanto
 * edición plana como formateo dinámico reactivo con máscara de cursor (TextFieldValue),
 * optimizado con BasicTextField y DecorationBox para una altura compacta y elegante (~46-48 dp).
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    interactionSource: MutableInteractionSource,
    esSensible: Boolean = false
) {
    val paddingCompacto = PaddingValues(
        start = if (leadingIcon != null) 4.dp else 12.dp,
        end = if (trailingIcon != null) 4.dp else 12.dp,
        top = if (varias) 8.dp else 4.dp,
        bottom = if (varias) 8.dp else 4.dp
    )

    if (formateadorMascara != null) {
        var tfv by remember {
            mutableStateOf(TextFieldValue(text = valor, selection = TextRange(valor.length)))
        }
        if (tfv.text != valor) {
            val nuevoCursor = tfv.selection.end.coerceIn(0, valor.length)
            tfv = tfv.copy(text = valor, selection = TextRange(nuevoCursor))
        }
        BasicTextField(
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
            modifier = Modifier
                .fillMaxWidth()
                .blindajeSemanticoSensible(esSensible, etiqueta),
            readOnly = readOnly,
            enabled = habilitado,
            singleLine = !varias,
            minLines = if (varias) 3 else 1,
            textStyle = estiloTexto.copy(color = TextoPrincipal),
            cursorBrush = SolidColor(ColorAcento),
            visualTransformation = transformacionVisual,
            keyboardOptions = opcionesTeclado,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = tfv.text,
                    innerTextField = innerTextField,
                    enabled = habilitado,
                    singleLine = !varias,
                    visualTransformation = transformacionVisual,
                    interactionSource = interactionSource,
                    isError = esError,
                    label = { Text(etiqueta) },
                    placeholder = composablePlaceholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    shape = forma,
                    colors = colores,
                    contentPadding = paddingCompacto,
                    container = {}
                )
            }
        )
    } else {
        BasicTextField(
            value = valor,
            onValueChange = alCambiar,
            modifier = Modifier
                .fillMaxWidth()
                .blindajeSemanticoSensible(esSensible, etiqueta),
            readOnly = readOnly,
            enabled = habilitado,
            singleLine = !varias,
            minLines = if (varias) 3 else 1,
            textStyle = estiloTexto.copy(color = TextoPrincipal),
            cursorBrush = SolidColor(ColorAcento),
            visualTransformation = transformacionVisual,
            keyboardOptions = opcionesTeclado,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = valor,
                    innerTextField = innerTextField,
                    enabled = habilitado,
                    singleLine = !varias,
                    visualTransformation = transformacionVisual,
                    interactionSource = interactionSource,
                    isError = esError,
                    label = { Text(etiqueta) },
                    placeholder = composablePlaceholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    shape = forma,
                    colors = colores,
                    contentPadding = paddingCompacto,
                    container = {}
                )
            }
        )
    }
}
