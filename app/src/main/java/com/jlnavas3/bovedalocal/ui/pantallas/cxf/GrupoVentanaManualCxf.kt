/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Grupo de configuración para la ventana de transferencia manual:
 * selector de duración y conmutador de disponibilidad bajo demanda con cuenta regresiva.
 */
@Composable
fun GrupoVentanaManualCxf(
    segundosSeleccionados: Int,
    autoBloqueoActualSegundos: Int,
    transferenciaActiva: Boolean,
    segundosRestantes: Int,
    mostrarId: Boolean,
    alCambiarTiempo: (Int) -> Unit,
    alDetectarConflicto: (Int) -> Unit,
    alAlternarDisponibilidad: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val subtituloEstado = if (transferenciaActiva) {
        if (segundosRestantes == -1) {
            "Disponible para gestores externos hasta bloquear la bóveda"
        } else {
            val min = segundosRestantes / 60
            val seg = segundosRestantes % 60
            val formateado = String.format("%02d:%02d", min, seg)
            "Disponible para gestores externos. Expira en $formateado"
        }
    } else {
        "Activa la disponibilidad bajo demanda durante el tiempo configurado"
    }

    ComponenteGrupo(
        etiqueta = "Disponibilidad manual",
        idGrupo = "05-COP-CXF-G02",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        SelectorTiempoTransferenciaCxf(
            segundosSeleccionados = segundosSeleccionados,
            autoBloqueoActualSegundos = autoBloqueoActualSegundos,
            mostrarId = mostrarId,
            alSeleccionar = alCambiarTiempo,
            alDetectarConflicto = alDetectarConflicto
        )

        ComponenteSeparador()

        ComponenteSwitch(
            titulo = "Habilitar disponibilidad",
            subtitulo = subtituloEstado,
            activo = transferenciaActiva,
            icono = Icons.Filled.VpnKey,
            colorIcono = ColorAcento,
            idFila = "05-COP-CXF-SWT",
            mostrarId = mostrarId,
            alCambiar = alAlternarDisponibilidad
        )
    }
}
