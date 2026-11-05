/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Composable micro desacoplado para activar o desactivar la visualización del texto
 * y panel de «Datos técnicos» en las llaves de acceso (Passkeys).
 */
@Composable
fun FilaAjusteMostrarDatosTecnicosPasskey(
    activo: Boolean,
    mostrarIdsAjustes: Boolean,
    alCambiar: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ComponenteSwitch(
            titulo = "Datos técnicos en llaves de acceso",
            activo = activo,
            alCambiar = alCambiar,
            icono = Icons.Outlined.Info,
            colorIcono = ColorDatosPasskey,
            idFila = "04-HER-PSK-TEC",
            mostrarId = mostrarIdsAjustes
        )
        Text(
            text = "Muestra la opción desplegable «Datos técnicos» para inspeccionar y copiar el Credential ID completo, fecha de registro y User Handle en el detalle de la llave.",
            style = MaterialTheme.typography.bodySmall,
            color = TextoSecundario,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}
