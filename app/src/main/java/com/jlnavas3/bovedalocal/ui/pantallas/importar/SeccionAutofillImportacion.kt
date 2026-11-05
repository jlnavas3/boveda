/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.ConfiguracionBovedaExportable

/**
 * Sección de importación para preferencias de autocompletado nativo y vinculación de aplicaciones.
 */
@Composable
fun SeccionAutofillImportacion(
    config: ConfiguracionBovedaExportable?,
    actual: AjustesApp,
    importarAutofillDeteccion: Boolean,
    alAlternarAutofillDeteccion: () -> Unit,
    importarMapeosApps: Boolean,
    alAlternarMapeosApps: () -> Unit,
    modoDetallado: Boolean = false,
    modifier: Modifier = Modifier
) {
    ContenedorSeccionImportacion(
        titulo = "Autocompletado",
        icono = Icons.Filled.Keyboard,
        modifier = modifier
    ) {
        FilaOpcionImportacion(
            titulo = "Detección de autocompletado",
            descripcion = "Palabras clave heurísticas para detectar campos de usuario, contraseña y TOTP",
            seleccionado = importarAutofillDeteccion,
            alAlternar = alAlternarAutofillDeteccion,
            textoDetallado = FormateadorValoresImportacion.autofillDeteccion(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )

        FilaOpcionImportacion(
            titulo = "Mapeo de apps y navegadores",
            descripcion = "Asociación de paquetes de Android con sitios web y navegadores vinculados",
            seleccionado = importarMapeosApps,
            alAlternar = alAlternarMapeosApps,
            textoDetallado = FormateadorValoresImportacion.mapeosApps(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )
    }
}
