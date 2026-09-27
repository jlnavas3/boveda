package com.jlnavas3.bovedalocal.ui.pantallas.formas

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

private data class PresetFormaItem(
    val nombre: String,
    val curvatura: Float,
    val grosor: Float,
    val estilo: String,
    val espaciado: Float
)

private val PRESETS_FORMA = listOf(
    PresetFormaItem("Predeterminado", 6f, 0.8f, "marcado", 14f),
    PresetFormaItem("Redondeado", 18f, 1f, "sutil", 14f),
    PresetFormaItem("Neobrutalista", 0f, 2.5f, "marcado", 16f),
    PresetFormaItem("Píldora M3", 28f, 1f, "sutil", 16f),
    PresetFormaItem("Compacto", 8f, 1.5f, "sutil", 8f),
    PresetFormaItem("Sin bordes", 16f, 0f, "ninguno", 14f)
)

@Composable
fun PantallaFormasPresets(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    ContenedorPrincipal(
        titulo = "Estilos predefinidos",
        subtitulo = "Combinaciones armónicas de esquinas y trazos",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            SimuladorTarjetaInteractiva(
                ajustes = ajustes,
                haptica = haptica
            )
        }
    ) {
        ComponenteGrupo(
            etiqueta = "ESTILOS DISPONIBLES",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = ColorIconosInternos,
            alRestablecer = {
                haptica.tic()
                vm.aplicarPresetFormas(6f, 0.8f, "marcado", 14f)
            }
        ) {
            PRESETS_FORMA.forEachIndexed { indice, item ->
                val activo = ajustes.curvaturaEsquinasDp == item.curvatura &&
                    ajustes.grosorBordeDp == item.grosor &&
                    (item.estilo == "ninguno" && (ajustes.grosorBordeDp == 0f || ajustes.estiloBorde == "ninguno") || ajustes.estiloBorde == item.estilo)

                ComponenteRadio(
                    titulo = item.nombre,
                    icono = null,
                    seleccionado = activo,
                    alSeleccionar = {
                        haptica.tic()
                        vm.aplicarPresetFormas(item.curvatura, item.grosor, item.estilo, item.espaciado)
                    }
                )

                if (indice < PRESETS_FORMA.lastIndex) {
                    ComponenteSeparador()
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
