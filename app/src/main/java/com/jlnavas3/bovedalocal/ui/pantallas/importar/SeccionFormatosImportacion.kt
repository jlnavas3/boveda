/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.ConfiguracionBovedaExportable

/**
 * Sección de importación para formatos de datos, parámetros del generador y ciclo de vida de la papelera.
 */
@Composable
fun SeccionFormatosImportacion(
    config: ConfiguracionBovedaExportable?,
    actual: AjustesApp,
    importarFormatosLista: Boolean,
    alAlternarFormatosLista: () -> Unit,
    importarGeneradorTotp: Boolean,
    alAlternarGeneradorTotp: () -> Unit,
    importarRetencionPapelera: Boolean,
    alAlternarRetencionPapelera: () -> Unit,
    modoDetallado: Boolean = false,
    modifier: Modifier = Modifier
) {
    ContenedorSeccionImportacion(
        titulo = "Formatos y Herramientas",
        icono = Icons.Filled.Tune,
        modifier = modifier
    ) {
        FilaOpcionImportacion(
            titulo = "Formatos de visualización",
            descripcion = "Densidad de listas, ordenación por defecto y formatos de fecha, hora y números",
            seleccionado = importarFormatosLista,
            alAlternar = alAlternarFormatosLista,
            textoDetallado = FormateadorValoresImportacion.formatosLista(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )

        FilaOpcionImportacion(
            titulo = "Generador de contraseñas y TOTP",
            descripcion = "Longitud, caracteres y algoritmos para contraseñas y códigos de doble factor manuales",
            seleccionado = importarGeneradorTotp,
            alAlternar = alAlternarGeneradorTotp,
            textoDetallado = FormateadorValoresImportacion.generadorTotp(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )

        FilaOpcionImportacion(
            titulo = "Retención de papelera e historial",
            descripcion = "Días de conservación de entradas eliminadas y tamaño máximo del historial de contraseñas",
            seleccionado = importarRetencionPapelera,
            alAlternar = alAlternarRetencionPapelera,
            textoDetallado = FormateadorValoresImportacion.retencionPapelera(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )
    }
}
