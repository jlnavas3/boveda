/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.EstadoCaducidad
import com.jlnavas3.bovedalocal.data.EvaluadorCaducidadClaves
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Composable micro desacoplado para mostrar la insignia y estado de expiración de una contraseña
 * en la pantalla de detalle de una entrada.
 */
@Composable
fun InsigniaCaducidadDetalle(
    fechaCaducidad: Long?,
    modifier: Modifier = Modifier
) {
    if (fechaCaducidad == null || fechaCaducidad <= 0L) return

    val estado = EvaluadorCaducidadClaves.evaluar(fechaCaducidad)
    val colorEstado = when (estado) {
        EstadoCaducidad.EXPIRADA -> Peligro
        EstadoCaducidad.POR_EXPIRAR -> Advertencia
        EstadoCaducidad.VIGENTE -> ColorAcento
        EstadoCaducidad.SIN_CADUCIDAD -> TextoSecundario
    }

    val dias = EvaluadorCaducidadClaves.diasRestantes(fechaCaducidad)
    val fechaStr = EvaluadorCaducidadClaves.formatearFecha(fechaCaducidad)

    val (icono, etiquetaBadge, descripcion) = when (estado) {
        EstadoCaducidad.EXPIRADA -> Triple(
            Icons.Filled.ErrorOutline,
            "Expirada",
            "Expiró hace ${-dias} días ($fechaStr) · Es aconsejable cambiarla cuanto antes."
        )
        EstadoCaducidad.POR_EXPIRAR -> Triple(
            Icons.Filled.Timer,
            "Próxima a expirar",
            "Caduca en $dias días ($fechaStr) · Recuerda renovarla."
        )
        EstadoCaducidad.VIGENTE -> Triple(
            Icons.Filled.CheckCircle,
            "Vigente",
            "Caduca el $fechaStr (en $dias días)."
        )
        EstadoCaducidad.SIN_CADUCIDAD -> return
    }

    TarjetaDatoDetalle(
        colorBorde = colorEstado,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    text = "Caducidad de contraseña",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TextoSecundario
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoPrincipal
                )
            }

            Box(
                modifier = Modifier
                    .clip(FormaPequena)
                    .background(colorEstado.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorEstado,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = etiquetaBadge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorEstado
                    )
                }
            }
        }
    }
}
