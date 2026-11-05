/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.ConfiguracionBovedaExportable

/**
 * Sección de importación para reglas de URLs, dominios y redes locales homelab.
 */
@Composable
fun SeccionReglasImportacion(
    config: ConfiguracionBovedaExportable?,
    actual: AjustesApp,
    importarReglasUrls: Boolean,
    alAlternarReglasUrls: () -> Unit,
    importarReglasHomelab: Boolean,
    alAlternarReglasHomelab: () -> Unit,
    modoDetallado: Boolean = false,
    modifier: Modifier = Modifier
) {
    ContenedorSeccionImportacion(
        titulo = "Reglas de URLs y Redes",
        icono = Icons.Filled.Language,
        modifier = modifier
    ) {
        FilaOpcionImportacion(
            titulo = "Reglas de URLs y dominios",
            descripcion = "Prefijos ignorados, TLDs descartables y catálogo de marcas personalizadas",
            seleccionado = importarReglasUrls,
            alAlternar = alAlternarReglasUrls,
            textoDetallado = FormateadorValoresImportacion.reglasUrls(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )

        FilaOpcionImportacion(
            titulo = "Reglas Homelab y red local",
            descripcion = "Puertos de servicios locales (8006, 9443, etc.) y plantillas de IP",
            seleccionado = importarReglasHomelab,
            alAlternar = alAlternarReglasHomelab,
            textoDetallado = FormateadorValoresImportacion.reglasHomelab(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )
    }
}
