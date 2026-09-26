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
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.AmbarFuerte
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
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
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
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
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.abs
import kotlin.math.round

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
    val (a, b) = remember(semilla, titulo) { coloresMonograma(semilla.ifBlank { titulo }) }
    val pincelFondo = remember(a, b) { Brush.linearGradient(listOf(a, b)) }
    val colorTexto = remember(a) { colorContraste(a) }
    val forma = CircleShape

    val letras = remember(titulo) {
        val t = titulo.trim()
        if (t.isEmpty()) "?"
        else {
            val partes = t.split(' ').filter { it.isNotEmpty() }
            if (partes.size >= 2) {
                "${partes[0].first().uppercaseChar()}${partes[1].first().uppercaseChar()}"
            } else {
                "${partes[0].first().uppercaseChar()}"
            }
        }
    }

    Box(
        modifier = Modifier
            .size(tamano.dp)
            .clip(forma)
            .background(pincelFondo)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual.copy(alpha = 0.35f), forma)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letras,
            color = colorTexto,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano / 2.4f).sp
        )
    }
}

@Composable
fun TarjetaBoveda(
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
fun TarjetaBovedaDesplegable(
    titulo: String,
    icono: ImageVector,
    descripcion: String = "",
    inicialmenteAbierta: Boolean = false,
    modifier: Modifier = Modifier,
    colorIcono: Color = ColorIconosInternos,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    abiertaControlada: Boolean? = null,
    alAlternarAbierta: ((Boolean) -> Unit)? = null,
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    var abiertaLocal by remember(inicialmenteAbierta) { mutableStateOf(inicialmenteAbierta) }
    val abierta = abiertaControlada ?: abiertaLocal
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
                .clickable {
                    if (alAlternarAbierta != null) {
                        alAlternarAbierta(!abierta)
                    } else {
                        abiertaLocal = !abiertaLocal
                    }
                }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono distinguido con contenedor suave estilo barra lateral y etiqueta ID opcional
            val colorLegible = colorLegibleParaTema(colorIcono)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(FormaPequena)
                        .background(fondoBadgeParaTema(colorIcono)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorLegible,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                    Spacer(Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(fondoBadgeParaTema(colorIcono))
                            .padding(horizontal = 4.dp, vertical = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = idEtiqueta,
                            style = EstiloMono.copy(fontSize = 8.sp, fontWeight = FontWeight.Bold),
                            color = colorLegibleParaTema(colorIcono)
                        )
                    }
                }
            }

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
fun SeparadorOpcionMenu(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(0.8.dp)
            .background(if (esOscuroActivo) Color(0xFF2D2C30) else Color(0xFFEBEBEB))
    )
}

@Composable
fun BarraFuerza(
    fraccion: Float,
    etiqueta: String,
    tiempo: String,
    bits: Double? = null
) {
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
                .height(4.dp)
                .clip(CircleShape)
                .background(Borde.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(anchoAnimado)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(etiqueta, color = color, style = MaterialTheme.typography.labelLarge)
                if (bits != null && bits > 0) {
                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(color.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${round(bits).toInt()} bits",
                            color = color,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
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
fun EtiquetaSeccion(
    texto: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorIcono: Color = ColorIconosInternos
) {
    if (icono != null) {
        val colorLegible = colorLegibleParaTema(colorIcono)
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(FormaPequena)
                    .background(fondoBadgeParaTema(colorIcono)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorLegible,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                text = texto.uppercase(),
                color = ColorTitulos,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.5.sp)
            )
        }
    } else {
        Text(
            text = texto.uppercase(),
            color = ColorTitulos,
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.5.sp),
            modifier = modifier
        )
    }
}

/**
 * Menú desplegable estándar con contenedor delimitado por borde exterior adaptativo
 * y esquinas consistentes con el diseño de la aplicación.
 */
@Composable
fun MenuDesplegableBoveda(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = androidx.compose.ui.unit.DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(14.dp)
    val fondoMenu = if (esOscuroActivo) Color(0xFF262529) else Color(0xFFFFFFFF)

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = offset,
        properties = properties,
        modifier = modifier
            .clip(forma)
            .background(fondoMenu)
    ) {
        content()
    }
}

@Composable
fun CampoBoveda(
    valor: String,
    etiqueta: String,
    alCambiar: (String) -> Unit,
    modifier: Modifier = Modifier,
    esContrasena: Boolean = false,
    mostrarContrasena: Boolean = false,
    alAlternarMostrarContrasena: (() -> Unit)? = null,
    monoespaciada: Boolean = false,
    varias: Boolean = false,
    tecladoNumerico: Boolean = false,
    keyboardType: KeyboardType? = null,
    readOnly: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    alPulsar: (() -> Unit)? = null,
    formateadorMascara: ((String) -> String)? = null
) {
    val tipo = when {
        esContrasena -> TipoCampoTexto.CONTRASENA
        tecladoNumerico -> TipoCampoTexto.NUMERICO
        varias -> TipoCampoTexto.MULTILINEA
        else -> TipoCampoTexto.TEXTO
    }
    ComponenteCampoTexto(
        valor = valor,
        etiqueta = etiqueta,
        alCambiar = alCambiar,
        modifier = modifier,
        tipo = tipo,
        esContrasena = esContrasena,
        mostrarContrasena = if (alAlternarMostrarContrasena != null) mostrarContrasena else null,
        alAlternarMostrarContrasena = alAlternarMostrarContrasena,
        monoespaciada = monoespaciada,
        varias = varias,
        tecladoNumerico = tecladoNumerico,
        keyboardType = keyboardType,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        alPulsar = alPulsar,
        formateadorMascara = formateadorMascara
    )
}
