/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tema

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

data class PresetRapidoLab(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val colorMuestra: Color,
    val lumFondo: Float,
    val lumTarjeta: Float,
    val lumCampo: Float,
    val lumModal: Float,
    val lumBorde: Float,
    val lumTextoPrincipal: Float,
    val lumTextoSecundario: Float,
    val tonoGlobal: Float,
    val saturacionTinte: Float
)

/**
 * Fila horizontal desplazable de presets rápidos de 1 toque para el Laboratorio de Temas.
 */
@Composable
fun FilaPresetsRapidosLab(
    modoOscuro: Boolean,
    presetActivoId: String?,
    alSeleccionarPreset: (PresetRapidoLab) -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    val presets = if (modoOscuro) PRESETS_OSCUROS else PRESETS_CLAROS
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Presets rápidos de estilo",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = ColorAjusteGris
            )
            Text(
                text = "1 toque",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = ColorAcento
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.forEach { preset ->
                val seleccionado = preset.id == presetActivoId
                ChipPresetRapido(
                    preset = preset,
                    seleccionado = seleccionado,
                    alPulsar = {
                        haptica.tic()
                        alSeleccionarPreset(preset)
                    }
                )
            }
        }
    }
}

@Composable
private fun ChipPresetRapido(
    preset: PresetRapidoLab,
    seleccionado: Boolean,
    alPulsar: () -> Unit
) {
    val forma = if (CurvaturaEsquinas == 0.dp) RoundedCornerShape(0.dp) else RoundedCornerShape(10.dp)
    val formaMuestra = if (CurvaturaEsquinas == 0.dp) RoundedCornerShape(0.dp) else CircleShape
    val colorBorde = if (seleccionado) {
        ColorAcento
    } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        ColorBordeActual
    } else {
        ColorSeparadorAjustes.copy(alpha = 0.5f)
    }

    val anchoBorde = if (seleccionado) 1.5.dp else 0.8.dp
    val colorFondo = if (seleccionado) ColorAcento.copy(alpha = 0.16f) else ColorCampoAjustes

    Row(
        modifier = Modifier
            .clip(forma)
            .background(colorFondo)
            .border(anchoBorde, colorBorde, forma)
            .clickable { alPulsar() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(formaMuestra)
                .background(preset.colorMuestra)
                .border(0.5.dp, Color.White.copy(alpha = 0.2f), formaMuestra)
        )
        Spacer(Modifier.width(7.dp))
        Text(
            text = preset.nombre,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = (12 * EscalaTexto).sp,
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.SemiBold
            ),
            color = if (seleccionado) ColorAcento else TextoPrincipal
        )
        if (seleccionado) {
            Spacer(Modifier.width(5.dp))
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Activo",
                tint = ColorAcento,
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

fun encontrarPresetPorValores(
    lumFondo: Float,
    lumTarjeta: Float,
    lumCampo: Float,
    lumModal: Float,
    lumBorde: Float,
    lumTextoPrincipal: Float,
    lumTextoSecundario: Float,
    tonoGlobal: Float,
    saturacionTinte: Float,
    esOscuro: Boolean
): String? {
    val presets = if (esOscuro) PRESETS_OSCUROS else PRESETS_CLAROS
    return presets.firstOrNull { p ->
        kotlin.math.abs(p.lumFondo - lumFondo) < 0.015f &&
        kotlin.math.abs(p.lumTarjeta - lumTarjeta) < 0.015f &&
        kotlin.math.abs(p.lumCampo - lumCampo) < 0.015f &&
        kotlin.math.abs(p.lumModal - lumModal) < 0.015f &&
        kotlin.math.abs(p.lumBorde - lumBorde) < 0.015f &&
        kotlin.math.abs(p.lumTextoPrincipal - lumTextoPrincipal) < 0.015f &&
        kotlin.math.abs(p.lumTextoSecundario - lumTextoSecundario) < 0.015f &&
        kotlin.math.abs(p.tonoGlobal - tonoGlobal) < 1.0f &&
        kotlin.math.abs(p.saturacionTinte - saturacionTinte) < 0.015f
    }?.id
}

val PRESETS_OSCUROS = listOf(
    PresetRapidoLab(
        id = "amoled",
        nombre = "Negro AMOLED",
        descripcion = "0% negro absoluto",
        colorMuestra = Color(0xFF000000),
        lumFondo = 0.00f,
        lumTarjeta = 0.00f,
        lumCampo = 0.06f,
        lumModal = 0.10f,
        lumBorde = 0.16f,
        lumTextoPrincipal = 0.545f,
        lumTextoSecundario = 0.48f,
        tonoGlobal = 0f,
        saturacionTinte = 0.00f
    ),
    PresetRapidoLab(
        id = "obsidiana",
        nombre = "Obsidiana",
        descripcion = "Clásico sobrio",
        colorMuestra = Color(0xFF09090A),
        lumFondo = 0.09f,
        lumTarjeta = 0.16f,
        lumCampo = 0.21f,
        lumModal = 0.22f,
        lumBorde = 0.16f,
        lumTextoPrincipal = 0.85f,
        lumTextoSecundario = 0.65f,
        tonoGlobal = 215f,
        saturacionTinte = 0.12f
    ),
    PresetRapidoLab(
        id = "grafito",
        nombre = "Grafito Carbón",
        descripcion = "Oscuro suave",
        colorMuestra = Color(0xFF1E2124),
        lumFondo = 0.14f,
        lumTarjeta = 0.22f,
        lumCampo = 0.28f,
        lumModal = 0.26f,
        lumBorde = 0.22f,
        lumTextoPrincipal = 0.92f,
        lumTextoSecundario = 0.72f,
        tonoGlobal = 215f,
        saturacionTinte = 0.06f
    ),
    PresetRapidoLab(
        id = "titanio",
        nombre = "Titanio Nórdico",
        descripcion = "Tinte metálico frío",
        colorMuestra = Color(0xFF191D24),
        lumFondo = 0.08f,
        lumTarjeta = 0.15f,
        lumCampo = 0.22f,
        lumModal = 0.20f,
        lumBorde = 0.20f,
        lumTextoPrincipal = 0.90f,
        lumTextoSecundario = 0.70f,
        tonoGlobal = 210f,
        saturacionTinte = 0.22f
    ),
    PresetRapidoLab(
        id = "sepia",
        nombre = "Sepia Nocturno",
        descripcion = "Tinte cálido nocturno",
        colorMuestra = Color(0xFF1C1917),
        lumFondo = 0.08f,
        lumTarjeta = 0.14f,
        lumCampo = 0.20f,
        lumModal = 0.18f,
        lumBorde = 0.18f,
        lumTextoPrincipal = 0.90f,
        lumTextoSecundario = 0.70f,
        tonoGlobal = 35f,
        saturacionTinte = 0.20f
    )
)

private val PRESETS_CLAROS = listOf(
    PresetRapidoLab(
        id = "blanco_puro",
        nombre = "Blanco Puro",
        descripcion = "Contraste nítido",
        colorMuestra = Color(0xFFFFFFFF),
        lumFondo = 1.00f,
        lumTarjeta = 0.94f,
        lumCampo = 0.88f,
        lumModal = 0.96f,
        lumBorde = 0.80f,
        lumTextoPrincipal = 0.08f,
        lumTextoSecundario = 0.35f,
        tonoGlobal = 0f,
        saturacionTinte = 0.00f
    ),
    PresetRapidoLab(
        id = "obsidiana_claro",
        nombre = "Nube Sobria",
        descripcion = "Clásico claro sobrio",
        colorMuestra = Color(0xFFF2F5F9),
        lumFondo = 0.95f,
        lumTarjeta = 0.89f,
        lumCampo = 0.83f,
        lumModal = 0.93f,
        lumBorde = 0.78f,
        lumTextoPrincipal = 0.14f,
        lumTextoSecundario = 0.38f,
        tonoGlobal = 215f,
        saturacionTinte = 0.12f
    ),
    PresetRapidoLab(
        id = "grafito_suave",
        nombre = "Perla Neutra",
        descripcion = "Gris suave equilibrado",
        colorMuestra = Color(0xFFEBECEE),
        lumFondo = 0.94f,
        lumTarjeta = 0.88f,
        lumCampo = 0.82f,
        lumModal = 0.92f,
        lumBorde = 0.78f,
        lumTextoPrincipal = 0.12f,
        lumTextoSecundario = 0.36f,
        tonoGlobal = 215f,
        saturacionTinte = 0.04f
    ),
    PresetRapidoLab(
        id = "titanio_polar",
        nombre = "Polar Nórdico",
        descripcion = "Tinte frío glacial",
        colorMuestra = Color(0xFFE8EEF5),
        lumFondo = 0.94f,
        lumTarjeta = 0.87f,
        lumCampo = 0.80f,
        lumModal = 0.91f,
        lumBorde = 0.76f,
        lumTextoPrincipal = 0.12f,
        lumTextoSecundario = 0.34f,
        tonoGlobal = 210f,
        saturacionTinte = 0.18f
    ),
    PresetRapidoLab(
        id = "crema_calido",
        nombre = "Sepia Diurno",
        descripcion = "Tinte cálido suave",
        colorMuestra = Color(0xFFFAF6EE),
        lumFondo = 0.96f,
        lumTarjeta = 0.90f,
        lumCampo = 0.84f,
        lumModal = 0.93f,
        lumBorde = 0.80f,
        lumTextoPrincipal = 0.15f,
        lumTextoSecundario = 0.38f,
        tonoGlobal = 35f,
        saturacionTinte = 0.16f
    )
)
