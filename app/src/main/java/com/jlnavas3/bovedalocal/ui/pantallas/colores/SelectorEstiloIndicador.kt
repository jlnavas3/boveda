/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.colores

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewSidebar
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CropLandscape
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal

private data class OpcionEstilo(
    val clave: String,
    val titulo: String,
    val descripcion: String,
    val icono: ImageVector
)

private val OPCIONES_ESTILOS = listOf(
    OpcionEstilo("lineas", "Líneas superiores", "Barra segmentada en la parte superior", Icons.Filled.HorizontalRule),
    OpcionEstilo("puntos", "Puntos discretos", "Microesferas de color en la cabecera", Icons.Filled.MoreHoriz),
    OpcionEstilo("iconos", "Micro-íconos", "Iconos temáticos coloreados (llave, web, 2FA...)", Icons.Filled.CropLandscape),
    OpcionEstilo("lateral", "Franja lateral", "Barra vertical elegante en el margen izquierdo", Icons.AutoMirrored.Filled.ViewSidebar),
    OpcionEstilo("ninguno", "Desactivado", "Ocultar indicadores en las tarjetas", Icons.Filled.Block)
)

/**
 * Selector interactivo del estilo de indicador para las tarjetas de credenciales.
 */
@Composable
fun SelectorEstiloIndicador(
    estiloActual: String,
    mostrarIdsAjustes: Boolean,
    alSeleccionarEstilo: (String) -> Unit,
    modifier: Modifier = Modifier,
    alRestablecer: (() -> Unit)? = null
) {
    ComponenteGrupo(
        etiqueta = "Estilo de indicadores",
        idGrupo = "02-APA-THM-DAT-EST",
        mostrarId = mostrarIdsAjustes,
        alRestablecer = alRestablecer,
        modifier = modifier
    ) {
        ComponenteSelectorModal(
            titulo = "Estilo en tarjetas",
            valorSeleccionado = estiloActual,
            opciones = OPCIONES_ESTILOS.map {
                OpcionSelectorModal(
                    valor = it.clave,
                    etiquetaFila = it.titulo,
                    etiquetaModal = it.titulo,
                    descripcionModal = it.descripcion,
                    icono = it.icono
                )
            },
            alSeleccionar = alSeleccionarEstilo,
            idFila = "02-APA-THM-DAT-EST-SEL",
            mostrarId = mostrarIdsAjustes
        )
    }
}
