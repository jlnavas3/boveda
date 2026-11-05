/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Microcomponente que renderiza el desglose estadístico de salud de las contraseñas.
 */
@Composable
fun ResumenAuditoriaSalud(
    clavesCount: Int,
    repetidasCount: Int,
    muyComunesCount: Int,
    debilesCount: Int,
    antiguasCount: Int,
    modifier: Modifier = Modifier,
    expandido: Boolean = true,
    alAlternarExpandido: () -> Unit = {},
    umbralAntiguedadDias: Int = 180,
    ignoradasCount: Int = 0,
    diagnosticoAccesibilidad: com.jlnavas3.bovedalocal.util.DiagnosticoAccesibilidad? = null
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RESUMEN DE AUDITORÍA",
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            )
        }

        GrupoAjustes {
            FilaMetricaSalud(
                etiqueta = "Contraseñas analizadas",
                valor = clavesCount.toString(),
                color = ColorTextoAjustes
            )
            SeparadorFilaSimple()
            FilaMetricaSalud(
                etiqueta = "Repetidas (en grupos)",
                valor = repetidasCount.toString(),
                color = if (repetidasCount == 0) Menta else Peligro
            )
            SeparadorFilaSimple()
            FilaMetricaSalud(
                etiqueta = "Muy comunes o filtradas",
                valor = muyComunesCount.toString(),
                color = if (muyComunesCount == 0) Menta else Peligro
            )
            SeparadorFilaSimple()
            FilaMetricaSalud(
                etiqueta = "Débiles (baja entropía)",
                valor = debilesCount.toString(),
                color = if (debilesCount == 0) Menta else Peligro
            )
            SeparadorFilaSimple()
            val etiquetaAntiguas = if (umbralAntiguedadDias <= 0) {
                "Sin actualizar (Desactivado)"
            } else {
                "Sin actualizar (> $umbralAntiguedadDias días)"
            }
            FilaMetricaSalud(
                etiqueta = etiquetaAntiguas,
                valor = antiguasCount.toString(),
                color = if (antiguasCount == 0) Menta else Advertencia
            )
            if (ignoradasCount > 0) {
                SeparadorFilaSimple()
                FilaMetricaSalud(
                    etiqueta = "Ignoradas de la auditoría",
                    valor = ignoradasCount.toString(),
                    color = ColorAjusteGris
                )
            }
        }
    }
}
