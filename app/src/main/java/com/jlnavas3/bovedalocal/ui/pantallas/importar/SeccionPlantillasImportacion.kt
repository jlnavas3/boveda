/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.ConfiguracionBovedaExportable

/**
 * Sección de importación para plantillas de campos personalizadas.
 */
@Composable
fun SeccionPlantillasImportacion(
    config: ConfiguracionBovedaExportable?,
    importarPlantillas: Boolean,
    alAlternarPlantillas: () -> Unit,
    modoDetallado: Boolean = false,
    modifier: Modifier = Modifier
) {
    val totalPlantillas = config?.plantillasCamposPersonalizadas?.size ?: 0
    if (totalPlantillas == 0) return

    ContenedorSeccionImportacion(
        titulo = "Plantillas de campos",
        icono = Icons.Filled.Description,
        modifier = modifier
    ) {
        FilaOpcionImportacion(
            titulo = "Plantillas personalizadas",
            descripcion = "Estructuras predefinidas con campos personalizados para nuevas entradas",
            contador = totalPlantillas,
            seleccionado = importarPlantillas,
            alAlternar = alAlternarPlantillas,
            textoDetallado = FormateadorValoresImportacion.plantillas(config),
            modoDetallado = modoDetallado
        )
    }
}
