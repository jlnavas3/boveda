/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.ConfiguracionBovedaExportable

/**
 * Sección de importación para políticas de seguridad, derivación criptográfica y privacidad visual.
 */
@Composable
fun SeccionSeguridadImportacion(
    config: ConfiguracionBovedaExportable?,
    actual: AjustesApp,
    importarSeguridadTiempos: Boolean,
    alAlternarSeguridadTiempos: () -> Unit,
    importarSeguridadCripto: Boolean,
    alAlternarSeguridadCripto: () -> Unit,
    importarSeguridadVisual: Boolean,
    alAlternarSeguridadVisual: () -> Unit,
    modoDetallado: Boolean = false,
    modifier: Modifier = Modifier
) {
    ContenedorSeccionImportacion(
        titulo = "Seguridad y Privacidad",
        icono = Icons.Filled.Security,
        modifier = modifier
    ) {
        FilaOpcionImportacion(
            titulo = "Tiempos y bloqueo",
            descripcion = "Tiempo de inactividad para autobloqueo, borrado de portapapeles y protección de pantalla",
            seleccionado = importarSeguridadTiempos,
            alAlternar = alAlternarSeguridadTiempos,
            textoDetallado = FormateadorValoresImportacion.seguridadTiempos(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )

        FilaOpcionImportacion(
            titulo = "Perfil Argon2id y límites",
            descripcion = "Parámetros de derivación criptográfica y control de reintentos fallidos",
            seleccionado = importarSeguridadCripto,
            alAlternar = alAlternarSeguridadCripto,
            textoDetallado = FormateadorValoresImportacion.seguridadCripto(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )

        FilaOpcionImportacion(
            titulo = "Seguridad y ocultamiento visual",
            descripcion = "Estilo de visualización de contraseñas (puntos fijos o variables) y temporizadores",
            seleccionado = importarSeguridadVisual,
            alAlternar = alAlternarSeguridadVisual,
            textoDetallado = FormateadorValoresImportacion.seguridadVisual(config, actual),
            modoDetallado = modoDetallado,
            habilitado = config != null
        )
    }
}
