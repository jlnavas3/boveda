package com.jlnavas3.bovedalocal.ui.pantallas.tipografia

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica

private data class PresetTipografiaItem(
    val nombre: String,
    val escala: Float,
    val peso: String,
    val cursiva: Boolean,
    val kerning: Float,
    val interlineado: Float,
    val familia: String
)

private val PRESETS_TIPOGRAFIA = listOf(
    PresetTipografiaItem("Equilibrado", 1.0f, "normal", false, 0.0f, 1.0f, "sans"),
    PresetTipografiaItem("Terminal Mono", 0.95f, "medio", false, 0.5f, 1.05f, "mono"),
    PresetTipografiaItem("Editorial", 1.05f, "normal", false, 0.2f, 1.10f, "serif"),
    PresetTipografiaItem("Compacto", 0.88f, "fino", false, -0.2f, 0.95f, "sans"),
    PresetTipografiaItem("Alta Legibilidad", 1.15f, "negrita", false, 0.3f, 1.15f, "sans")
)

@Composable
fun PantallaTipografiaPresets(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    ContenedorPrincipal(
        titulo = "Estilos predefinidos",
        subtitulo = "Combinaciones tipográficas armonizadas",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            PrevisualizacionTipografia()
        }
    ) {
        ComponenteGrupo(
            etiqueta = "ESTILOS DISPONIBLES",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = ColorIconosInternos,
            alRestablecer = {
                haptica.tic()
                vm.restablecerTipografia()
            }
        ) {
            PRESETS_TIPOGRAFIA.forEachIndexed { indice, item ->
                val activo = ajustes.escalaTexto == item.escala &&
                    ajustes.familiaFuente == item.familia &&
                    ajustes.pesoTexto == item.peso &&
                    ajustes.cursivaTexto == item.cursiva

                ComponenteRadio(
                    titulo = item.nombre,
                    icono = null,
                    seleccionado = activo,
                    alSeleccionar = {
                        haptica.tic()
                        vm.aplicarPresetTipografia(
                            escala = item.escala,
                            peso = item.peso,
                            cursiva = item.cursiva,
                            kerning = item.kerning,
                            interlineado = item.interlineado,
                            familia = item.familia
                        )
                    }
                )

                if (indice < PRESETS_TIPOGRAFIA.lastIndex) {
                    ComponenteSeparador()
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
