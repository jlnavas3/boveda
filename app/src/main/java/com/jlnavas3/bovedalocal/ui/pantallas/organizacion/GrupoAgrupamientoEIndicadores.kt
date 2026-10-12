/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

@Composable
fun GrupoAgrupamientoEIndicadores(
    mostrarId: Boolean,
    agruparPorSitio: Boolean,
    mostrarIndicadoresContenido: Boolean,
    alCambiarAgruparPorSitio: (Boolean) -> Unit,
    alCambiarMostrarIndicadores: (Boolean) -> Unit,
    alIrAColoresDatos: () -> Unit,
    alIrANormalizadorTitulos: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Agrupamiento",
        icono = Icons.Filled.Layers,
        colorIcono = ColorAcento,
        idGrupo = "02-APA-DES-G01",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Agrupar cuentas",
            icono = null,
            activo = agruparPorSitio,
            idFila = "02-APA-DES-GRP",
            mostrarId = mostrarId,
            alCambiar = alCambiarAgruparPorSitio
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSwitch(
            titulo = "Indicadores de contenido en tarjetas",
            icono = null,
            activo = mostrarIndicadoresContenido,
            idFila = "02-APA-DES-IND",
            mostrarId = mostrarId,
            alCambiar = alCambiarMostrarIndicadores
        )
        if (mostrarIndicadoresContenido) {
            ComponenteSeparador(sangriaInicio = 16.dp)
            ComponenteNavegacion(
                titulo = "Colores de campos y datos",
                icono = null,
                idFila = "02-APA-THM-G04",
                mostrarId = mostrarId,
                alPulsar = alIrAColoresDatos
            )
        }
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteNavegacion(
            titulo = "Asistente de títulos de sitios web",
            subtitulo = "Limpiar nombres técnicos de Google / URLs",
            icono = null,
            idFila = "02-APA-DES-TIT",
            mostrarId = mostrarId,
            alPulsar = alIrANormalizadorTitulos
        )
    }
}
