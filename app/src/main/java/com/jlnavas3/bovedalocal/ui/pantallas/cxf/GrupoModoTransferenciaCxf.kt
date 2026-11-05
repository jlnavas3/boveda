/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.ModoTransferenciaCxf
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Grupo de selección de modo de transferencia: Automática vs Manual.
 */
@Composable
fun GrupoModoTransferenciaCxf(
    modoActual: ModoTransferenciaCxf,
    mostrarId: Boolean,
    alCambiarModo: (ModoTransferenciaCxf) -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Modo de transferencia",
        idGrupo = "05-COP-CXF-G01",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        ComponenteRadio(
            titulo = "Automática",
            subtitulo = "Mantiene activa la transferencia mientras la bóveda esté desbloqueada",
            seleccionado = modoActual == ModoTransferenciaCxf.AUTOMATICA,
            icono = Icons.Filled.Autorenew,
            colorIcono = ColorAcento,
            idFila = "05-COP-CXF-MOD-AUT",
            mostrarId = mostrarId,
            alSeleccionar = { alCambiarModo(ModoTransferenciaCxf.AUTOMATICA) }
        )

        ComponenteSeparador()

        ComponenteRadio(
            titulo = "Manual",
            subtitulo = "Solo se activa al solicitarlo por un tiempo determinado",
            seleccionado = modoActual == ModoTransferenciaCxf.MANUAL,
            icono = Icons.Filled.TouchApp,
            colorIcono = ColorAcento,
            idFila = "05-COP-CXF-MOD-MAN",
            mostrarId = mostrarId,
            alSeleccionar = { alCambiarModo(ModoTransferenciaCxf.MANUAL) }
        )
    }
}
