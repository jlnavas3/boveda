/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.TextoPiePagina
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Panel in-situ de configuración para la exportación selectiva (.bvda).
 * Ubicado sobre la barra de acciones inferior para permitir una exportación directa en un solo paso.
 */
@Composable
fun PanelConfiguracionExportacion(
    patronNombre: String,
    alCambiarPatronNombre: (String) -> Unit,
    nombreResuelto: String,
    clavePassword: String,
    alCambiarClavePassword: (String) -> Unit,
    mostrarClave: Boolean,
    alAlternarMostrarClave: () -> Unit,
    guardarEnDirectorioAuto: Boolean,
    alCambiarGuardarEnDirectorioAuto: (Boolean) -> Unit,
    usarAutoPassword: Boolean,
    alCambiarUsarAutoPassword: (Boolean) -> Unit,
    tieneAutoPassword: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorAjustesFondo)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(
                        width = GrosorBorde,
                        color = ColorBordeActual.copy(alpha = 0.3f)
                    )
                } else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Nombre del archivo resuelto (directo sin prefijo)
        TextoPiePagina(
            texto = nombreResuelto,
            color = ColorAcento,
            monoespaciada = true
        )

        Spacer(Modifier.height(6.dp))

        // Campo de patrón de nombre
        ComponenteCampoTexto(
            valor = patronNombre,
            etiqueta = "Patrón del nombre de archivo",
            alCambiar = alCambiarPatronNombre,
            placeholder = "{##}-selectivo-{FECHA}_{HORA}",
            monoespaciada = true
        )

        Spacer(Modifier.height(6.dp))

        // Campo de contraseña de cifrado
        ComponenteCampoTexto(
            valor = clavePassword,
            etiqueta = "Contraseña de cifrado",
            alCambiar = alCambiarClavePassword,
            esContrasena = true,
            mostrarContrasena = mostrarClave,
            alAlternarMostrarContrasena = alAlternarMostrarClave
        )

        Spacer(Modifier.height(6.dp))

        // Switch carpeta de respaldos automáticos
        FilaOpcionSwitchExportar(
            titulo = "Guardar en carpeta de respaldos automáticos",
            subtitulo = if (guardarEnDirectorioAuto) "Descargas/BovedaLocal/Backups" else "Explorador de archivos (SAF)",
            checked = guardarEnDirectorioAuto,
            onCheckedChange = alCambiarGuardarEnDirectorioAuto
        )

        // Switch clave de respaldo automático
        FilaOpcionSwitchExportar(
            titulo = "Usar clave de respaldo automático",
            subtitulo = if (tieneAutoPassword) "Configurada en Ajustes" else "No configurada en Ajustes",
            checked = usarAutoPassword && tieneAutoPassword,
            onCheckedChange = alCambiarUsarAutoPassword,
            habilitado = tieneAutoPassword
        )
    }
}
