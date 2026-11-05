/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Microcomponente del modal inferior de auditoría de salud de la bóveda.
 * Muestra el resumen estadístico global de contraseñas.
 */
@Composable
fun ModalAuditoriaSalud(
    abierto: Boolean,
    alCerrar: () -> Unit,
    clavesCount: Int,
    totalSobrantesDuplicadas: Int = 0,
    repetidasCount: Int,
    muyComunesCount: Int,
    debilesCount: Int,
    antiguasCount: Int,
    ignoradasCount: Int,
    umbralDias: Int,
    alIrDuplicados: () -> Unit = {},
    diagnosticoAccesibilidad: com.jlnavas3.bovedalocal.util.DiagnosticoAccesibilidad? = null
) {
    ModalInferiorBoveda(
        abierto = abierto,
        alCerrar = alCerrar,
        titulo = "Auditoría de Salud",
        descripcion = "$clavesCount contraseñas analizadas",
        icono = Icons.Filled.Analytics,
        colorIcono = ColorIconosInternos
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ResumenAuditoriaSalud(
                clavesCount = clavesCount,
                repetidasCount = repetidasCount,
                muyComunesCount = muyComunesCount,
                debilesCount = debilesCount,
                antiguasCount = antiguasCount,
                umbralAntiguedadDias = umbralDias,
                ignoradasCount = ignoradasCount
            )
        }
    }
}
