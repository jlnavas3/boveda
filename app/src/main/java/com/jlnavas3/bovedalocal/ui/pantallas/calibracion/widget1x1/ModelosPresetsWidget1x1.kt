package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Modelo de datos que describe un preset de diseño visual para el widget 1x1.
 */
data class PresetEstilo1x1(
    val nombre: String,
    val curvaturaDp: Float,
    val grosorDp: Float,
    val transparenciaFondo: Float,
    val colorFondo: String,
    val colorBorde: String,
    val colorIcono: String,
    val vidrioEsmerilado: Boolean,
    val esmeriladoIntensidad: Float = 0.60f,
    val bloquearProporcion: Boolean? = null
) {
    fun esSeleccionado(ajustes: AjustesApp): Boolean {
        val coincideCurvatura = kotlin.math.abs(ajustes.widget1x1CurvaturaEsquinasDp - curvaturaDp) < 0.5f
        val coincideGrosor = kotlin.math.abs(ajustes.widget1x1GrosorBordeDp - grosorDp) < 0.2f
        val coincideTransparencia = kotlin.math.abs(ajustes.widget1x1TransparenciaFondo - transparenciaFondo) < 0.05f
        val coincideFondo = ajustes.widget1x1ColorFondo.equals(colorFondo, ignoreCase = true)
        val coincideBorde = grosorDp <= 0.1f || ajustes.widget1x1ColorBorde.equals(colorBorde, ignoreCase = true)
        val coincideIcono = ajustes.widget1x1ColorIcono.equals(colorIcono, ignoreCase = true)
        val coincideVidrio = ajustes.widget1x1VidrioEsmerilado == vidrioEsmerilado
        return coincideCurvatura && coincideGrosor && coincideTransparencia && coincideFondo && coincideBorde && coincideIcono && coincideVidrio
    }
}

val presetsSinBordeWidget1x1 = listOf(
    PresetEstilo1x1("Grafito", 16f, 0f, 0.85f, "#1C1C1E", "#38383A", "#FFFFFF", vidrioEsmerilado = true),
    PresetEstilo1x1("OLED", 16f, 0f, 1.0f, "#000000", "#000000", "#FFFFFF", vidrioEsmerilado = false),
    PresetEstilo1x1("Plata", 16f, 0f, 0.70f, "#E5E5EA", "#E5E5EA", "#1C1C1E", vidrioEsmerilado = true),
    PresetEstilo1x1("Medianoche", 16f, 0f, 0.88f, "#0D1117", "#30363D", "#58A6FF", vidrioEsmerilado = true)
)

val presetsConBordeWidget1x1 = listOf(
    PresetEstilo1x1("Titanio", 16f, 1.2f, 0.85f, "#1C1C1E", "#48484A", "#FFFFFF", vidrioEsmerilado = true),
    PresetEstilo1x1("Cian", 16f, 1.2f, 0.85f, "#1C1C1E", "#0A84FF", "#0A84FF", vidrioEsmerilado = true),
    PresetEstilo1x1("Menta", 16f, 1.2f, 0.85f, "#1C1C1E", "#30D158", "#30D158", vidrioEsmerilado = true),
    PresetEstilo1x1("Blanco", 16f, 1.2f, 0.75f, "#2C2C2E", "#FFFFFF", "#FFFFFF", vidrioEsmerilado = true)
)

val presetsCircularesWidget1x1 = listOf(
    PresetEstilo1x1("Esfera Grafito", 50f, 0f, 0.85f, "#1C1C1E", "#38383A", "#FFFFFF", vidrioEsmerilado = true, bloquearProporcion = true),
    PresetEstilo1x1("Esfera Titanio", 50f, 1.2f, 0.85f, "#1C1C1E", "#48484A", "#FFFFFF", vidrioEsmerilado = true, bloquearProporcion = true),
    PresetEstilo1x1("Esfera Cian", 50f, 1.2f, 0.85f, "#1C1C1E", "#0A84FF", "#0A84FF", vidrioEsmerilado = true, bloquearProporcion = true),
    PresetEstilo1x1("Esfera OLED", 50f, 0f, 1.0f, "#000000", "#000000", "#FFFFFF", vidrioEsmerilado = false, bloquearProporcion = true)
)

@Composable
fun FilaSubgrupoEstiloWidget1x1(
    titulo: String,
    presets: List<PresetEstilo1x1>,
    ajustes: AjustesApp,
    onSeleccionar: (PresetEstilo1x1) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = titulo,
            color = TextoSecundario,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.forEach { preset ->
                val seleccionado = preset.esSeleccionado(ajustes)
                val colorFondoPreset = parsearColorO(preset.colorFondo, Color.DarkGray)
                val colorBordePreset = parsearColorO(preset.colorBorde, Color.Gray)
                val colorIconoPreset = parsearColorO(preset.colorIcono, Color.White)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (seleccionado) ColorAcento.copy(alpha = 0.20f) else SuperficieAlta)
                        .then(
                            if (seleccionado) Modifier.border(1.5.dp, ColorAcento, RoundedCornerShape(8.dp))
                            else Modifier.border(0.5.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(8.dp))
                        )
                        .clickable { onSeleccionar(preset) }
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(if (preset.curvaturaDp >= 40f) CircleShape else RoundedCornerShape(3.dp))
                                .background(colorFondoPreset)
                                .then(
                                    if (preset.grosorDp > 0.1f) Modifier.border(
                                        1.dp,
                                        colorBordePreset,
                                        if (preset.curvaturaDp >= 40f) CircleShape else RoundedCornerShape(3.dp)
                                    )
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(colorIconoPreset)
                            )
                        }
                        Text(
                            text = preset.nombre,
                            color = if (seleccionado) ColorAcento else TextoPrincipal,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
