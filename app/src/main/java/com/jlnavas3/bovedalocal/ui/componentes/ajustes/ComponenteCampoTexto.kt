package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.FormateadorCampos

private class ContrasenaColorVisualTransformation : VisualTransformation {
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
 * Campo de texto reutilizable y sin bordes al estilo Honor MagicOS y Samsung One UI.
 * Soporta indicador cromático vertical en el borde izquierdo (`colorBordeIzquierdo`)
 * y contraseña coloreada cuando se activa la visibilidad.
 */
@Composable
fun ComponenteCampoTexto(
    valor: String,
    etiqueta: String,
    alCambiar: (String) -> Unit,
    modifier: Modifier = Modifier,
    tipo: TipoCampoTexto = TipoCampoTexto.TEXTO,
    placeholder: String? = null,
    icono: ImageVector? = null,
    mostrarIcono: Boolean = false,
    colorIcono: Color? = null,
    colorBordeIzquierdo: Color? = null,
    esContrasena: Boolean = (tipo == TipoCampoTexto.CONTRASENA),
    mostrarContrasena: Boolean? = null,
    alAlternarMostrarContrasena: (() -> Unit)? = null,
    monoespaciada: Boolean = false,
    varias: Boolean = (tipo == TipoCampoTexto.MULTILINEA),
    tecladoNumerico: Boolean = (tipo == TipoCampoTexto.NUMERICO),
    keyboardType: KeyboardType? = null,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false,
    habilitado: Boolean = true,
    botonLimpiar: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    alPulsar: (() -> Unit)? = null,
    formateadorMascara: ((String) -> String)? = null,
    esError: Boolean = false,
    mensajeError: String? = null
) {
    val forma = FormaPequena
    val esOscuro = esOscuroActivo

    // Superficie suave One UI / MagicOS
    val colorFondoCampo = if (esOscuro) Color(0xFF242327) else Color(0xFFF1F2F5)

    // Estado interno para visibilidad de contraseña si no se controla externamente
    var verContrasenaInterno by remember { mutableStateOf(false) }
    val esVisible = mostrarContrasena ?: verContrasenaInterno

    val iconoInicial = icono ?: when (tipo) {
        TipoCampoTexto.TEXTO -> Icons.Filled.Edit
        TipoCampoTexto.NUMERICO -> Icons.Filled.Pin
        TipoCampoTexto.CONTRASENA -> Icons.Filled.Lock
        TipoCampoTexto.FECHA -> Icons.Filled.DateRange
        TipoCampoTexto.HORA -> Icons.Filled.Schedule
        TipoCampoTexto.ENLACE -> Icons.Filled.Link
        TipoCampoTexto.MULTILINEA -> Icons.Filled.Description
    }

    val leadingIconComposable: (@Composable () -> Unit)? = if (mostrarIcono || icono != null) {
        {
            Icon(
                imageVector = iconoInicial,
                contentDescription = null,
                tint = colorLegibleParaTema(colorIcono ?: ColorAcento, esOscuro),
                modifier = Modifier.size(20.dp)
            )
        }
    } else null

    val trailingIconComposable: (@Composable () -> Unit)? = when {
        trailingIcon != null -> trailingIcon
        esContrasena -> {
            {
                IconButton(
                    onClick = {
                        if (alAlternarMostrarContrasena != null) {
                            alAlternarMostrarContrasena()
                        } else {
                            verContrasenaInterno = !verContrasenaInterno
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (esVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (esVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = TextoSecundario,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        botonLimpiar && valor.isNotEmpty() && !readOnly -> {
            {
                IconButton(onClick = { alCambiar("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Limpiar texto",
                        tint = TextoSecundario,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        else -> null
    }

    val tipoTecladoEfectivo = keyboardType ?: when {
        tecladoNumerico || tipo == TipoCampoTexto.NUMERICO -> if (esContrasena) KeyboardType.NumberPassword else KeyboardType.Number
        esContrasena || tipo == TipoCampoTexto.CONTRASENA -> KeyboardType.Password
        tipo == TipoCampoTexto.ENLACE -> KeyboardType.Uri
        else -> KeyboardType.Text
    }

    val coloresSinBordes = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        cursorColor = ColorAcento,
        focusedLabelColor = ColorAcento,
        unfocusedLabelColor = TextoSecundario,
        focusedTextColor = TextoPrincipal,
        unfocusedTextColor = TextoPrincipal,
        errorTextColor = TextoPrincipal
    )

    val opcionesTeclado = KeyboardOptions(
        keyboardType = tipoTecladoEfectivo,
        capitalization = capitalization,
        imeAction = imeAction
    )

    val transformacionVisual = remember(esContrasena, esVisible) {
        when {
            esContrasena && !esVisible -> PasswordVisualTransformation()
            esContrasena && esVisible -> ContrasenaColorVisualTransformation()
            else -> VisualTransformation.None
        }
    }

    val bloqueCampo = @Composable {
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
                placeholder = if (placeholder != null) { { Text(placeholder) } } else null,
                modifier = Modifier.fillMaxWidth(),
                readOnly = readOnly,
                enabled = habilitado,
                isError = esError,
                singleLine = !varias,
                minLines = if (varias) 3 else 1,
                textStyle = if (monoespaciada) {
                    MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.5.sp
                    )
                } else {
                    MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp)
                },
                visualTransformation = transformacionVisual,
                leadingIcon = leadingIconComposable,
                trailingIcon = trailingIconComposable,
                keyboardOptions = opcionesTeclado,
                keyboardActions = keyboardActions,
                shape = forma,
                colors = coloresSinBordes
            )
        } else {
            TextField(
                value = valor,
                onValueChange = alCambiar,
                label = { Text(etiqueta) },
                placeholder = if (placeholder != null) { { Text(placeholder) } } else null,
                modifier = Modifier.fillMaxWidth(),
                readOnly = readOnly,
                enabled = habilitado,
                isError = esError,
                singleLine = !varias,
                minLines = if (varias) 3 else 1,
                textStyle = if (monoespaciada) {
                    MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.5.sp
                    )
                } else {
                    MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp)
                },
                visualTransformation = transformacionVisual,
                leadingIcon = leadingIconComposable,
                trailingIcon = trailingIconComposable,
                keyboardOptions = opcionesTeclado,
                keyboardActions = keyboardActions,
                shape = forma,
                colors = coloresSinBordes
            )
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(colorFondoCampo)
                .then(
                    if (alPulsar != null && habilitado) {
                        Modifier.clickable { alPulsar() }
                    } else {
                        Modifier
                    }
                )
        ) {
            bloqueCampo()
            if (colorBordeIzquierdo != null) {
                Box(
                    modifier = Modifier
                        .width(4.5.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                        .background(colorBordeIzquierdo)
                )
            }
        }

        if (esError && !mensajeError.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = mensajeError,
                color = Peligro,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}
