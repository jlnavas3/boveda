package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreTarjetas
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/** Convierte Color de Compose a HSV (tono 0..360, saturación 0..1, brillo 0..1). */
fun colorAhsv(color: Color): Triple<Float, Float, Float> {
    val r = color.red
    val g = color.green
    val b = color.blue
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min
    val h = when {
        delta == 0f -> 0f
        max == r -> ((g - b) / delta * 60f + 360f) % 360f
        max == g -> ((b - r) / delta * 60f + 120f) % 360f
        else -> ((r - g) / delta * 60f + 240f) % 360f
    }
    val s = if (max == 0f) 0f else delta / max
    val v = max
    return Triple(h, s, v)
}

/**
 * Selector de color interactivo con actualización en tiempo real.
 * Permite ajustar Tono (Hue), Saturación, Brillo, elegir colores rápidos
 * o ingresar un código Hexadecimal directamente.
 */
@Composable
fun SelectorColorEnTiempoReal(
    colorInicial: Color,
    modifier: Modifier = Modifier,
    titulo: String = "Elegir color",
    mostrarPresets: Boolean = true,
    alCambiarColor: (Color) -> Unit
) {
    val hsvInicial = remember(colorInicial) { colorAhsv(colorInicial) }
    var hue by remember(colorInicial) { mutableFloatStateOf(hsvInicial.first) }
    var sat by remember(colorInicial) { mutableFloatStateOf(hsvInicial.second.coerceIn(0.1f, 1f)) }
    var valLum by remember(colorInicial) { mutableFloatStateOf(hsvInicial.third.coerceIn(0.2f, 1f)) }

    val colorActual = remember(hue, sat, valLum) {
        Color.hsv(hue, sat, valLum)
    }

    var textoHex by remember(colorActual) { mutableStateOf(colorActual.aHex()) }

    val degradadoArcoiris = remember {
        Brush.horizontalGradient(
            listOf(
                Color.Red, Color(0xFFFF8A00), Color.Yellow, Color.Green,
                Color.Cyan, Color.Blue, Color(0xFF8A00FF), Color.Magenta, Color.Red
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Cabecera con previsualización en tiempo real y código HEX
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(FormaPequena)
                        .background(colorActual)
                        .border(GrosorBorde.coerceAtLeast(1.dp), Borde, FormaPequena)
                )
                Column {
                    Text(
                        text = titulo,
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = textoHex,
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                    )
                }
            }
        }

        // Control deslizante de TONO (Hue 0° a 360°)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Tono", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(degradadoArcoiris)
            )
            Slider(
                value = hue,
                onValueChange = { nuevaHue ->
                    hue = nuevaHue
                    val nuevo = Color.hsv(hue, sat, valLum)
                    alCambiarColor(nuevo)
                },
                valueRange = 0f..360f,
                colors = SliderDefaults.colors(
                    thumbColor = colorActual,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }

        // Control deslizante de SATURACIÓN
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Saturación", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
            Slider(
                value = sat,
                onValueChange = { nuevaSat ->
                    sat = nuevaSat
                    val nuevo = Color.hsv(hue, sat, valLum)
                    alCambiarColor(nuevo)
                },
                valueRange = 0.05f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = colorActual,
                    activeTrackColor = colorActual,
                    inactiveTrackColor = Borde
                )
            )
        }

        // Control deslizante de BRILLO / LUMINOSIDAD
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Brillo", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
            Slider(
                value = valLum,
                onValueChange = { nuevoVal ->
                    valLum = nuevoVal
                    val nuevo = Color.hsv(hue, sat, valLum)
                    alCambiarColor(nuevo)
                },
                valueRange = 0.15f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = colorActual,
                    activeTrackColor = colorActual,
                    inactiveTrackColor = Borde
                )
            )
        }

        // Paleta rápida de colores recomendados (presets)
        if (mostrarPresets) {
            Text("Colores predeterminados", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PaletaAcento.entries.forEach { paleta ->
                    val seleccionado = colorActual.aHex().equals(paleta.base.aHex(), ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(paleta.base)
                            .border(
                                width = if (seleccionado) 2.5.dp else 1.dp,
                                color = if (seleccionado) TextoPrincipal else Borde,
                                shape = CircleShape
                            )
                            .clickable {
                                val hsv = colorAhsv(paleta.base)
                                hue = hsv.first
                                sat = hsv.second
                                valLum = hsv.third
                                alCambiarColor(paleta.base)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = colorContraste(paleta.base),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
