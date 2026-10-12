/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoAcercaDeApp(
    alCopiarAuditoria: () -> Unit,
    alAbrirFichaApp: () -> Unit,
    modifier: Modifier = Modifier,
    mostrarIdsAjustes: Boolean = false,
    alVerInformacionApp: (() -> Unit)? = null,
    alVerLicencia: (() -> Unit)? = null
) {
    ComponenteGrupo(
        etiqueta = "Acerca de Bóveda Local",
        icono = Icons.Filled.Info,
        colorIcono = ColorIconosInternos,
        idGrupo = "06-SIS-DGN-G06",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Bóveda Local",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Gestor de contraseñas, passkeys y datos sensibles 100% offline, zero-knowledge, sin nube ni telemetría.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 16.sp
            )
        }

        if (alVerInformacionApp != null) {
            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Información de la aplicación",
                icono = Icons.Filled.Info,
                colorIcono = ColorIconosInternos,
                colorTinteIcono = androidx.compose.ui.graphics.Color.White,
                alPulsar = alVerInformacionApp
            )
        }

        if (alVerLicencia != null) {
            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Licencia de software (GNU GPLv3)",
                icono = Icons.Filled.Verified,
                colorIcono = ColorIconosInternos,
                colorTinteIcono = androidx.compose.ui.graphics.Color.White,
                alPulsar = alVerLicencia
            )
        }

        ComponenteSeparador()

        ComponenteBotonFila(
            titulo = "Copiar auditoría",
            icono = Icons.Filled.ContentCopy,
            colorIcono = ColorIconosInternos,
            colorTinteIcono = androidx.compose.ui.graphics.Color.White,
            alPulsar = alCopiarAuditoria
        )

        ComponenteSeparador()

        ComponenteBotonFila(
            titulo = "Ficha Android",
            icono = Icons.Filled.Info,
            colorIcono = ColorIconosInternos,
            colorTinteIcono = androidx.compose.ui.graphics.Color.White,
            alPulsar = alAbrirFichaApp
        )
    }
}
