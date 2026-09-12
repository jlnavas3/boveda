package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.util.IconosMarcas
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.AmbarFuerte
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeDropdown
import com.jlnavas3.bovedalocal.ui.theme.ColorEncabezadoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeparadorDropdown
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.util.Dominios
import kotlin.math.abs

fun coloresMonograma(semilla: String): Pair<Color, Color> {
    val base = Dominios.raiz(semilla).ifEmpty { semilla }.lowercase()
    var hash = 2166136261u.toInt()
    for (c in base) {
        hash = (hash xor c.code) * 16777619
    }
    val tono = (abs(hash) % 360).toFloat()
    val primero = Color.hsv(tono, 0.55f, 0.92f)
    val segundo = Color.hsv((tono + 28f) % 360f, 0.72f, 0.78f)
    return primero to segundo
}

@Composable
fun Monograma(titulo: String, semilla: String, tamano: Int = 46) {
    val (a, b) = coloresMonograma(semilla.ifBlank { titulo })
    val colorTexto = colorContraste(a)
    val iconoMarca = IconosMarcas.buscar(semilla, titulo)
    val radio = (tamano * (com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinasDp / 54f)).coerceIn(0f, tamano / 2f).dp
    val forma = RoundedCornerShape(radio)
    Box(
        modifier = Modifier
            .size(tamano.dp)
            .clip(forma)
            .background(Brush.linearGradient(listOf(a, b)))
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual.copy(alpha = 0.35f), forma)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (iconoMarca != null) {
            Icon(
                painter = painterResource(iconoMarca),
                contentDescription = titulo,
                tint = colorTexto,
                modifier = Modifier.size((tamano * 0.52f).dp)
            )
        } else {
            val letras = titulo.trim().split(Regex("\\s+"))
                .filter { it.isNotEmpty() }
                .take(2)
                .joinToString("") { it.first().uppercase() }
            Text(
                text = letras,
                color = colorTexto,
                fontWeight = FontWeight.Bold,
                fontSize = (tamano / 2.4f).sp
            )
        }
    }
}

@Composable
fun TarjetaPepo(
    modifier: Modifier = Modifier,
    alPulsar: (() -> Unit)? = null,
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    val forma = FormaTarjeta
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetas)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
            .then(if (alPulsar != null) Modifier.clickable { alPulsar() } else Modifier)
            .padding(16.dp)
    ) {
        Column(content = contenido)
    }
}

/**
 * Tarjeta desplegable con encabezado distinguido (tono más oscuro, icono grande a la izquierda,
 * textos verticalmente centrados, borde separador inferior y chevron a la derecha).
 * Completamente plana: sin sombras ni blur, gobernada por el borde y curvatura configurados.
 */
@Composable
fun TarjetaPepoDesplegable(
    titulo: String,
    icono: ImageVector,
    descripcion: String = "",
    inicialmenteAbierta: Boolean = false,
    modifier: Modifier = Modifier,
    colorIcono: Color = ColorIconosInternos,
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    var abierta by remember { mutableStateOf(inicialmenteAbierta) }
    val forma = FormaTarjeta

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(if (abierta) ColorTarjetas else ColorEncabezadoTarjeta)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
    ) {
        // Encabezado holgado con tono sutilmente más oscuro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorEncabezadoTarjeta)
                .clickable { abierta = !abierta }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono a la izquierda y más grande
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.width(14.dp))

            // Textos centrados verticalmente
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = titulo,
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                if (descripcion.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = descripcion,
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            // Indicador de desplegado a la derecha
            Icon(
                imageVector = if (abierta) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (abierta) "Contraer" else "Expandir",
                tint = TextoSecundario,
                modifier = Modifier.size(24.dp)
            )
        }

        // Borde separador entre encabezado y cuerpo cuando está desplegada
        if (abierta) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (GrosorBorde > 0.dp) GrosorBorde else 1.dp)
                    .background(if (ColorBordeActual != Color.Transparent) ColorBordeActual else Borde)
            )

            // Cuerpo de la tarjeta con padding holgado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorTarjetas)
                    .padding(16.dp),
                content = contenido
            )
        }
    }
}

@Composable
fun BotonAmbar(
    texto: String,
    modifier: Modifier = Modifier,
    activo: Boolean = true,
    icono: ImageVector? = null,
    alPulsar: () -> Unit
) {
    val escala by animateFloatAsState(
        targetValue = if (activo) 1f else 0.98f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "escalaBoton"
    )
    val forma = FormaBoton
    val colorTexto = if (activo) ColorSobreAcento else TextoSecundario
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(forma)
            .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(Borde, Borde)))
            .then(
                if (GrosorBorde > 0.dp && !activo) {
                    Modifier.border(GrosorBorde, Borde, forma)
                } else {
                    Modifier
                }
            )
            .clickable(enabled = activo) { alPulsar() },
        contentAlignment = Alignment.Center
    ) {
        if (icono != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorTexto,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = texto,
                    color = colorTexto,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = (15 * escala * EscalaTexto).sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Text(
                text = texto,
                color = colorTexto,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = (15 * escala * EscalaTexto).sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun BotonBorde(
    texto: String,
    modifier: Modifier = Modifier,
    color: Color = TextoPrincipal,
    icono: ImageVector? = null,
    alPulsar: () -> Unit
) {
    val forma = FormaBoton
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(forma)
            .then(
                if (GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
            .clickable { alPulsar() },
        contentAlignment = Alignment.Center
    ) {
        if (icono != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = texto,
                    color = color,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = (15 * EscalaTexto).sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Text(
                text = texto,
                color = color,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = (15 * EscalaTexto).sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun CampoPepo(
    valor: String,
    etiqueta: String,
    alCambiar: (String) -> Unit,
    modifier: Modifier = Modifier,
    esContrasena: Boolean = false,
    mostrarContrasena: Boolean = false,
    alAlternarMostrarContrasena: (() -> Unit)? = null,
    monoespaciada: Boolean = false,
    varias: Boolean = false,
    tecladoNumerico: Boolean = false
) {
    val forma = FormaCampo
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        singleLine = !varias,
        minLines = if (varias) 3 else 1,
        textStyle = if (monoespaciada) {
            MaterialTheme.typography.bodyLarge.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
        } else {
            MaterialTheme.typography.bodyLarge
        },
        visualTransformation = if (esContrasena && !mostrarContrasena) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (esContrasena && alAlternarMostrarContrasena != null) {
            {
                IconButton(onClick = alAlternarMostrarContrasena) {
                    Icon(
                        imageVector = if (mostrarContrasena) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (mostrarContrasena) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = TextoSecundario,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        } else null,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = when {
                tecladoNumerico -> KeyboardType.Number
                esContrasena -> KeyboardType.Password
                else -> KeyboardType.Text
            }
        ),
        shape = forma,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Superficie,
            unfocusedContainerColor = Superficie,
            focusedIndicatorColor = Ambar,
            unfocusedIndicatorColor = if (GrosorBorde > 0.dp) ColorBordeActual else Color.Transparent,
            focusedLabelColor = Ambar,
            unfocusedLabelColor = TextoSecundario,
            cursorColor = Ambar,
            focusedTextColor = TextoPrincipal,
            unfocusedTextColor = TextoPrincipal
        )
    )
}

@Composable
fun BarraFuerza(fraccion: Float, etiqueta: String, tiempo: String) {
    val anchoAnimado by animateFloatAsState(
        targetValue = fraccion,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "fuerza"
    )
    val color by animateColorAsState(
        targetValue = when {
            fraccion < 0.35f -> Peligro
            fraccion < 0.65f -> AmbarFuerte
            fraccion < 0.85f -> Ambar
            else -> Menta
        },
        animationSpec = spring(dampingRatio = 0.7f),
        label = "colorFuerza"
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape)
                .background(Borde)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(anchoAnimado)
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(etiqueta, color = color, style = MaterialTheme.typography.labelLarge)
            Text(
                tiempo,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun EtiquetaSeccion(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto.uppercase(),
        color = ColorTitulos,
        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.5.sp),
        modifier = modifier
    )
}

/**
 * Menú desplegable estándar con contenedor delimitado por borde exterior adaptativo
 * y esquinas consistentes con el diseño de la aplicación.
 */
@Composable
fun MenuDesplegablePepo(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: androidx.compose.ui.unit.DpOffset = androidx.compose.ui.unit.DpOffset(0.dp, 0.dp),
    properties: androidx.compose.ui.window.PopupProperties = androidx.compose.ui.window.PopupProperties(focusable = true),
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    val forma = FormaTarjeta
    val grosor = if (GrosorBorde > 0.dp) GrosorBorde else 0.8.dp

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = offset,
        properties = properties,
        modifier = modifier
            .clip(forma)
            .background(SuperficieAlta)
            .border(grosor, ColorBordeDropdown, forma)
    ) {
        content()
    }
}

/**
 * Divisor / separador sutil entre opciones de un menú desplegable, adaptado dinámicamente
 * al tema claro u oscuro para no resaltar excesivamente ni quedar invisible.
 */
@Composable
fun SeparadorOpcionMenu(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(0.8.dp)
            .background(ColorSeparadorDropdown)
    )
}
