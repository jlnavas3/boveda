/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import com.jlnavas3.bovedalocal.ui.componentes.BotonFlotanteSecundario
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Columna vertical de botones flotantes (FAB) para la pantalla principal:
 * 0. Alerta de accesibilidad (Advertencia ámbar, si hay apps sospechosas activas).
 * 1. Bloquear bóveda (Candado superior).
 * 2. Crear nueva entrada (+ inferior).
 */
@Composable
fun ColumnaAccionesFlotantesLista(
    alBloquear: () -> Unit,
    alNuevaEntrada: () -> Unit,
    modifier: Modifier = Modifier,
    tieneAlertaAccesibilidad: Boolean = false,
    alAbrirAuditoriaAccesibilidad: (() -> Unit)? = null
) {
    val formaFab = RoundedCornerShape(CurvaturaEsquinas)

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val fondoBase = ColorTarjetaAjustes
        val fondoBloquear = Peligro.copy(alpha = 0.14f).compositeOver(fondoBase)

        // FAB de advertencia: Alerta de servicios de accesibilidad no autorizados
        if (tieneAlertaAccesibilidad && alAbrirAuditoriaAccesibilidad != null) {
            val fondoAlerta = Advertencia.copy(alpha = 0.16f).compositeOver(fondoBase)
            BotonFlotanteSecundario(
                onClick = alAbrirAuditoriaAccesibilidad,
                icono = Icons.Filled.Warning,
                descripcion = "Auditoría de accesibilidad",
                containerColor = fondoAlerta,
                contentColor = Advertencia,
                colorBorde = Color.Transparent
            )
        }

        // FAB superior: Bloquear app (Candado en tono sobrio idéntico al menú lateral, sólido y sin borde)
        BotonFlotanteSecundario(
            onClick = alBloquear,
            icono = Icons.Filled.Lock,
            descripcion = "Bloquear bóveda",
            containerColor = fondoBloquear,
            contentColor = Peligro,
            colorBorde = Color.Transparent
        )

        // FAB inferior: Nueva entrada (+)
        FloatingActionButton(
            onClick = alNuevaEntrada,
            containerColor = ColorAcento,
            contentColor = ColorSobreAcento,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Nueva entrada",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
