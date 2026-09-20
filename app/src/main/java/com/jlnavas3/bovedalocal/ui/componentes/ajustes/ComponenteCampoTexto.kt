package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Description
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.FormateadorCampos

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
 *
 * Características:
 * - Sin bordes duros (border = null, indicador transparente).
 * - Contenedor plano y suave con esquinas redondeadas (12.dp a 14.dp).
 * - Fondo adaptativo según tema Claro/Oscuro.
 * - Icono temático inicial opcional o automático.
 * - Soporte completo para máscaras, formateadores, passwords con alternancia de visibilidad
 *   y botones de borrado rápido.
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
    val forma = RoundedCornerShape(14.dp)
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
        focusedContainerColor = colorFondoCampo,
        unfocusedContainerColor = colorFondoCampo,
        disabledContainerColor = colorFondoCampo.copy(alpha = 0.6f),
        errorContainerColor = colorFondoCampo,
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
                modifier = modifier.fillMaxWidth(),
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
                visualTransformation = if (esContrasena && !esVisible) PasswordVisualTransformation() else VisualTransformation.None,
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
                modifier = modifier.fillMaxWidth(),
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
                visualTransformation = if (esContrasena && !esVisible) PasswordVisualTransformation() else VisualTransformation.None,
                leadingIcon = leadingIconComposable,
                trailingIcon = trailingIconComposable,
                keyboardOptions = opcionesTeclado,
                keyboardActions = keyboardActions,
                shape = forma,
                colors = coloresSinBordes
            )
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (alPulsar != null) {
            Box(modifier = Modifier.fillMaxWidth()) {
                bloqueCampo()
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(forma)
                        .clickable { alPulsar() }
                )
            }
        } else {
            bloqueCampo()
        }

        if (esError && !mensajeError.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = mensajeError,
                color = Peligro,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}
