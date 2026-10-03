package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp

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
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import com.jlnavas3.bovedalocal.util.Haptica

data class PresetEstiloTotp(
    val nombre: String,
    val curvaturaDp: Float,
    val grosorDp: Float,
    val transparenciaFondo: Float,
    val transparenciaFilas: Float,
    val colorBorde: String,
    val colorCodigo: String,
    val colorContador: String,
    val colorTitulo: String,
    val colorFilas: String,
    val vidrioEsmerilado: Boolean,
    val esmeriladoIntensidad: Float = 0.60f
) {
    fun esSeleccionado(ajustes: AjustesApp): Boolean {
        val coincideCurvatura = kotlin.math.abs(ajustes.widgetCurvaturaEsquinasDp - curvaturaDp) < 0.5f
        val coincideGrosor = kotlin.math.abs(ajustes.widgetGrosorBordeDp - grosorDp) < 0.2f
        val coincideTransparenciaFondo = kotlin.math.abs(ajustes.widgetTransparenciaFondo - transparenciaFondo) < 0.05f
        val coincideBorde = grosorDp <= 0.1f || ajustes.widgetColorBorde.equals(colorBorde, ignoreCase = true)
        val coincideCodigo = ajustes.widgetColorCodigo.equals(colorCodigo, ignoreCase = true)
        val coincideVidrio = ajustes.widgetTotpVidrioEsmerilado == vidrioEsmerilado
        return coincideCurvatura && coincideGrosor && coincideTransparenciaFondo && coincideBorde && coincideCodigo && coincideVidrio
    }
}

@Composable
fun SeccionPresetsWidgetTotp(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Estilos visuales",
        idGrupo = "04-HER-WGT-CAL-G00",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = modifier
    ) {
        val presetsSinBorde = listOf(
            PresetEstiloTotp("Grafito", 22f, 0f, 0.85f, 0.0f, "#38383A", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#1C1C1E", vidrioEsmerilado = true),
            PresetEstiloTotp("OLED", 22f, 0f, 1.0f, 0.0f, "#000000", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#1C1C1E", vidrioEsmerilado = false),
            PresetEstiloTotp("Plata", 22f, 0f, 0.70f, 0.25f, "#E5E5EA", "#0A84FF", "#1C1C1E", "#1C1C1E", "#E5E5EA", vidrioEsmerilado = true),
            PresetEstiloTotp("Medianoche", 22f, 0f, 0.88f, 0.15f, "#30363D", "#58A6FF", "#58A6FF", "#FFFFFF", "#161B22", vidrioEsmerilado = true)
        )

        val presetsConBorde = listOf(
            PresetEstiloTotp("Titanio", 22f, 1.2f, 0.85f, 0.0f, "#48484A", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#1C1C1E", vidrioEsmerilado = true),
            PresetEstiloTotp("Cian", 22f, 1.2f, 0.85f, 0.0f, "#0A84FF", "#0A84FF", "#0A84FF", "#FFFFFF", "#1C1C1E", vidrioEsmerilado = true),
            PresetEstiloTotp("Menta", 22f, 1.2f, 0.85f, 0.0f, "#30D158", "#30D158", "#30D158", "#FFFFFF", "#1C1C1E", vidrioEsmerilado = true),
            PresetEstiloTotp("Blanco", 22f, 1.2f, 0.75f, 0.20f, "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#2C2C2E", vidrioEsmerilado = true)
        )

        val presetsCirculares = listOf(
            PresetEstiloTotp("Cápsula Grafito", 32f, 0f, 0.85f, 0.0f, "#38383A", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#1C1C1E", vidrioEsmerilado = true),
            PresetEstiloTotp("Cápsula Titanio", 32f, 1.2f, 0.85f, 0.0f, "#48484A", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#1C1C1E", vidrioEsmerilado = true),
            PresetEstiloTotp("Cápsula Cian", 32f, 1.2f, 0.85f, 0.0f, "#0A84FF", "#0A84FF", "#0A84FF", "#FFFFFF", "#1C1C1E", vidrioEsmerilado = true),
            PresetEstiloTotp("Cápsula OLED", 32f, 0f, 1.0f, 0.0f, "#000000", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#1C1C1E", vidrioEsmerilado = false)
        )

        FilaSubgrupoEstiloTotp(
            titulo = "Sin bordes",
            presets = presetsSinBorde,
            ajustes = ajustes,
            onSeleccionar = { p ->
                haptica.tic()
                vm.aplicarPresetEstiloWidgetTotp(
                    curvaturaDp = p.curvaturaDp,
                    grosorDp = p.grosorDp,
                    transparenciaFondo = p.transparenciaFondo,
                    transparenciaFilas = p.transparenciaFilas,
                    colorBorde = p.colorBorde,
                    colorCodigo = p.colorCodigo,
                    colorContador = p.colorContador,
                    colorTitulo = p.colorTitulo,
                    colorFilas = p.colorFilas,
                    vidrioEsmerilado = p.vidrioEsmerilado,
                    esmeriladoIntensidad = p.esmeriladoIntensidad
                )
            }
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        FilaSubgrupoEstiloTotp(
            titulo = "Con bordes sutiles",
            presets = presetsConBorde,
            ajustes = ajustes,
            onSeleccionar = { p ->
                haptica.tic()
                vm.aplicarPresetEstiloWidgetTotp(
                    curvaturaDp = p.curvaturaDp,
                    grosorDp = p.grosorDp,
                    transparenciaFondo = p.transparenciaFondo,
                    transparenciaFilas = p.transparenciaFilas,
                    colorBorde = p.colorBorde,
                    colorCodigo = p.colorCodigo,
                    colorContador = p.colorContador,
                    colorTitulo = p.colorTitulo,
                    colorFilas = p.colorFilas,
                    vidrioEsmerilado = p.vidrioEsmerilado,
                    esmeriladoIntensidad = p.esmeriladoIntensidad
                )
            }
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        FilaSubgrupoEstiloTotp(
            titulo = "Circulares (Cápsula)",
            presets = presetsCirculares,
            ajustes = ajustes,
            onSeleccionar = { p ->
                haptica.tic()
                vm.aplicarPresetEstiloWidgetTotp(
                    curvaturaDp = p.curvaturaDp,
                    grosorDp = p.grosorDp,
                    transparenciaFondo = p.transparenciaFondo,
                    transparenciaFilas = p.transparenciaFilas,
                    colorBorde = p.colorBorde,
                    colorCodigo = p.colorCodigo,
                    colorContador = p.colorContador,
                    colorTitulo = p.colorTitulo,
                    colorFilas = p.colorFilas,
                    vidrioEsmerilado = p.vidrioEsmerilado,
                    esmeriladoIntensidad = p.esmeriladoIntensidad
                )
            }
        )
    }
}

@Composable
private fun FilaSubgrupoEstiloTotp(
    titulo: String,
    presets: List<PresetEstiloTotp>,
    ajustes: AjustesApp,
    onSeleccionar: (PresetEstiloTotp) -> Unit
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
                val colorBordePreset = parsearColorO(preset.colorBorde, Color.Gray)
                val colorCodigoPreset = parsearColorO(preset.colorCodigo, Color.White)

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
                                .clip(if (preset.curvaturaDp >= 30f) CircleShape else RoundedCornerShape(3.dp))
                                .background(Color(0xFF1C1C1E))
                                .then(
                                    if (preset.grosorDp > 0.1f) Modifier.border(
                                        1.dp,
                                        colorBordePreset,
                                        if (preset.curvaturaDp >= 30f) CircleShape else RoundedCornerShape(3.dp)
                                    )
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(colorCodigoPreset)
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
