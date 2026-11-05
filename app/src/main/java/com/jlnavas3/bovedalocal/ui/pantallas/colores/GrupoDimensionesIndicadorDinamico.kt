/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.colores

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Grupo de controles contextuales que adapta sus sliders y etiquetas al estilo de indicador seleccionado.
 */
@Composable
fun GrupoDimensionesIndicadorDinamico(
    estilo: String,
    dimensionTemporal: Float,
    opacidadTemporal: Float,
    mostrarIdsAjustes: Boolean,
    alCambiarDimension: (Float) -> Unit,
    alRestablecerDimension: () -> Unit,
    alCambiarOpacidad: (Float) -> Unit,
    alRestablecerOpacidad: () -> Unit,
    alRestablecerGrupo: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (estilo == "ninguno") {
        ComponenteGrupo(
            etiqueta = "Dimensiones y apariencia de indicadores",
            idGrupo = "02-APA-THM-DAT-G02",
            mostrarId = mostrarIdsAjustes,
            modifier = modifier
        ) {
            Text(
                text = "Los indicadores están desactivados. Las tarjetas se mostrarán limpias sin insignias de campos.",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = TextoSecundario,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
        return
    }

    val (tituloSlider, rangoDimension, etiquetaMin, etiquetaMax, valorPorDefecto) = when (estilo) {
        "puntos" -> DimensionConfig(
            titulo = "Tamaño de los puntos",
            rango = 3f..10f,
            etiquetaMin = "3 dp",
            etiquetaMax = "10 dp",
            default = 5f
        )
        "iconos" -> DimensionConfig(
            titulo = "Tamaño de los iconos",
            rango = 8f..16f,
            etiquetaMin = "8 dp",
            etiquetaMax = "16 dp",
            default = 12.5f
        )
        "lateral" -> DimensionConfig(
            titulo = "Ancho de la franja lateral",
            rango = 2f..8f,
            etiquetaMin = "2 dp",
            etiquetaMax = "8 dp",
            default = 3.5f
        )
        else -> DimensionConfig(
            // "lineas"
            titulo = "Altura de las líneas",
            rango = 1f..8f,
            etiquetaMin = "1 dp",
            etiquetaMax = "8 dp",
            default = 2f
        )
    }

    ComponenteGrupo(
        etiqueta = "Dimensiones y apariencia de indicadores",
        idGrupo = "02-APA-THM-DAT-G02",
        mostrarId = mostrarIdsAjustes,
        alRestablecer = alRestablecerGrupo,
        modifier = modifier
    ) {
        val textoDimension = "${String.format(Locale.US, "%.1f", dimensionTemporal.coerceIn(rangoDimension))} dp"

        ComponenteSlider(
            titulo = tituloSlider,
            valor = dimensionTemporal.coerceIn(rangoDimension),
            valorTexto = textoDimension,
            rango = rangoDimension,
            etiquetaMin = etiquetaMin,
            etiquetaMax = etiquetaMax,
            idFila = "02-APA-THM-DAT-ALT",
            mostrarId = mostrarIdsAjustes,
            alCambiar = alCambiarDimension,
            alRestablecer = alRestablecerDimension
        )

        ComponenteSeparador()

        val porcentajeOpacidad = (opacidadTemporal * 100).roundToInt()
        ComponenteSlider(
            titulo = "Opacidad de los colores",
            valor = opacidadTemporal.coerceIn(0.05f, 1.0f),
            valorTexto = "$porcentajeOpacidad%",
            rango = 0.05f..1.0f,
            etiquetaMin = "5%",
            etiquetaMax = "100%",
            idFila = "02-APA-THM-DAT-OPC",
            mostrarId = mostrarIdsAjustes,
            alCambiar = alCambiarOpacidad,
            alRestablecer = alRestablecerOpacidad
        )
    }
}

private data class DimensionConfig(
    val titulo: String,
    val rango: ClosedFloatingPointRange<Float>,
    val etiquetaMin: String,
    val etiquetaMax: String,
    val default: Float
)
