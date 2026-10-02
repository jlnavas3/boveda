package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.FormateadorCampos

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
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    alPulsar: (() -> Unit)? = null,
    formateadorMascara: ((String) -> String)? = null,
    esError: Boolean = false,
    mensajeError: String? = null
) {
    val forma = FormaCampo
    val esOscuro = esOscuroActivo

    // Superficie suave One UI / MagicOS vinculada a la paleta sobria (Capa 2)
    val colorFondoCampo = ColorCampoAjustes

    // Estado interno para visibilidad de contraseña si no se controla externamente
    var verContrasenaInterno by remember { mutableStateOf(false) }
    val esVisible = mostrarContrasena ?: verContrasenaInterno

    val leadingIconComposable: (@Composable () -> Unit)? = when {
        leadingIcon != null -> leadingIcon
        mostrarIcono || icono != null -> {
            val iconoEfectivo = icono ?: IconosCampoTexto.resolverIconoPorDefecto(tipo)
            val res: @Composable () -> Unit = {
                IconosCampoTexto.IconoInicio(
                    icono = iconoEfectivo,
                    colorIcono = colorIcono,
                    esOscuro = esOscuro
                )
            }
            res
        }
        else -> null
    }

    val trailingIconComposable: (@Composable () -> Unit)? = when {
        trailingIcon != null -> trailingIcon
        esContrasena -> {
            {
                IconosCampoTexto.IconoAlternarContrasena(
                    esVisible = esVisible,
                    onToggle = {
                        if (alAlternarMostrarContrasena != null) {
                            alAlternarMostrarContrasena()
                        } else {
                            verContrasenaInterno = !verContrasenaInterno
                        }
                    }
                )
            }
        }
        botonLimpiar && valor.isNotEmpty() && !readOnly -> {
            {
                IconosCampoTexto.BotonLimpiarTexto(onLimpiar = { alCambiar("") })
            }
        }
        else -> null
    }

    val tipoTecladoEfectivo = ConfiguradorCampoTexto.resolverTipoTeclado(
        tipo = tipo,
        tecladoNumerico = tecladoNumerico,
        esContrasena = esContrasena,
        keyboardTypeOverride = keyboardType
    )

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
        ConfiguradorCampoTexto.resolverTransformacionVisual(esContrasena, esVisible)
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val grosorConfigurado = if (GrosorBorde > 0.dp) GrosorBorde else 1.dp
    val borderModifier = when {
        isFocused && colorBordeIzquierdo != null -> {
            Modifier.border(grosorConfigurado, colorBordeIzquierdo.copy(alpha = 0.65f), forma)
        }
        isFocused -> {
            Modifier.border(grosorConfigurado, ColorAcento.copy(alpha = 0.65f), forma)
        }
        GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent -> {
            Modifier.border(GrosorBorde, ColorBordeActual, forma)
        }
        else -> Modifier
    }

    val estiloTexto = if (monoespaciada) {
        MaterialTheme.typography.bodyMedium.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 14.5.sp
        )
    } else {
        MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp)
    }
    val composablePlaceholder: (@Composable () -> Unit)? = placeholder?.let { { Text(it) } }

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
                placeholder = composablePlaceholder,
                modifier = Modifier.fillMaxWidth(),
                readOnly = readOnly,
                enabled = habilitado,
                isError = esError,
                singleLine = !varias,
                minLines = if (varias) 3 else 1,
                textStyle = estiloTexto,
                visualTransformation = transformacionVisual,
                leadingIcon = leadingIconComposable,
                trailingIcon = trailingIconComposable,
                keyboardOptions = opcionesTeclado,
                keyboardActions = keyboardActions,
                shape = forma,
                colors = coloresSinBordes,
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
                leadingIcon = leadingIconComposable,
                trailingIcon = trailingIconComposable,
                keyboardOptions = opcionesTeclado,
                keyboardActions = keyboardActions,
                shape = forma,
                colors = coloresSinBordes,
                interactionSource = interactionSource
            )
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(colorFondoCampo)
                .then(borderModifier)
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
                    modifier = Modifier.matchParentSize()
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.5.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .background(colorBordeIzquierdo)
                    )
                }
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

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun ComponenteCampoTextoPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
            ComponenteCampoTexto(
                valor = "admin@empresa.com",
                etiqueta = "Correo Electrónico",
                alCambiar = {},
                placeholder = "ejemplo@correo.com"
            )
            ComponenteCampoTexto(
                valor = "P@ssw0rdSecure!2024",
                etiqueta = "Contraseña",
                esContrasena = true,
                mostrarContrasena = false,
                alAlternarMostrarContrasena = {},
                alCambiar = {}
            )
            ComponenteCampoTexto(
                valor = "Entrada inválida",
                etiqueta = "Campo con Error",
                esError = true,
                mensajeError = "El formato introducido no es válido",
                alCambiar = {}
            )
        }
    }
}

