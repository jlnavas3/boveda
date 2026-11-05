/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.EstadoCaducidad
import com.jlnavas3.bovedalocal.data.EvaluadorCaducidadClaves
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.util.Calendar

/**
 * Composable micro desacoplado para configurar la fecha de caducidad y expiración
 * de contraseñas, presentado como fila colapsable homogénea con accesos rápidos.
 */
@Composable
fun CampoCaducidadEntrada(
    fechaCaducidad: Long?,
    alCambiarFecha: (Long?) -> Unit,
    ajustes: com.jlnavas3.bovedalocal.data.AjustesApp = com.jlnavas3.bovedalocal.data.AjustesApp(),
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val estado = EvaluadorCaducidadClaves.evaluar(fechaCaducidad)
    val colorEstado = when (estado) {
        EstadoCaducidad.EXPIRADA -> Peligro
        EstadoCaducidad.POR_EXPIRAR -> Advertencia
        EstadoCaducidad.VIGENTE -> TextoPrincipal
        EstadoCaducidad.SIN_CADUCIDAD -> TextoSecundario
    }

    var expandido by remember { mutableStateOf(false) }

    fun abrirSelectorFecha() {
        val calendario = Calendar.getInstance()
        if (fechaCaducidad != null && fechaCaducidad > 0L) {
            calendario.timeInMillis = fechaCaducidad
        }
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(contexto, { _, a, m, d ->
            val calSeleccionado = Calendar.getInstance().apply {
                set(Calendar.YEAR, a)
                set(Calendar.MONTH, m)
                set(Calendar.DAY_OF_MONTH, d)
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }
            alCambiarFecha(calSeleccionado.timeInMillis)
        }, anio, mes, dia).show()
    }

    val textoEstado = when (estado) {
        EstadoCaducidad.SIN_CADUCIDAD -> "Sin caducidad asignada"
        EstadoCaducidad.EXPIRADA -> {
            val dias = -EvaluadorCaducidadClaves.diasRestantes(fechaCaducidad!!)
            "Expirada hace $dias días (${EvaluadorCaducidadClaves.formatearFecha(fechaCaducidad, ajustes.formatoFecha)})"
        }
        EstadoCaducidad.POR_EXPIRAR -> {
            val dias = EvaluadorCaducidadClaves.diasRestantes(fechaCaducidad!!)
            "Expira en $dias días (${EvaluadorCaducidadClaves.formatearFecha(fechaCaducidad, ajustes.formatoFecha)})"
        }
        EstadoCaducidad.VIGENTE -> {
            val dias = EvaluadorCaducidadClaves.diasRestantes(fechaCaducidad!!)
            "Vigente · ${EvaluadorCaducidadClaves.formatearFecha(fechaCaducidad, ajustes.formatoFecha)} (en $dias d)"
        }
    }

    val insignia = when (estado) {
        EstadoCaducidad.EXPIRADA -> "Expirada"
        EstadoCaducidad.POR_EXPIRAR -> "Por expirar"
        EstadoCaducidad.VIGENTE -> "Vigente"
        EstadoCaducidad.SIN_CADUCIDAD -> null
    }

    FilaSeccionColapsableEdicion(
        icono = Icons.Filled.Timer,
        colorIcono = colorEstado,
        titulo = "Caducidad de contraseña",
        resumen = textoEstado,
        insigniaTexto = insignia,
        expandido = expandido,
        alAlternarExpandido = { expandido = !expandido },
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (fechaCaducidad != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = textoEstado,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = if (estado == EstadoCaducidad.SIN_CADUCIDAD) TextoPrincipal else colorEstado,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { alCambiarFecha(null) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Quitar caducidad",
                            tint = TextoSecundario,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // Fila de accesos rápidos para fijar caducidad
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipPresetCaducidad(texto = "30 días", alPulsar = {
                    alCambiarFecha(EvaluadorCaducidadClaves.calcularPreset(30))
                })
                ChipPresetCaducidad(texto = "90 días", alPulsar = {
                    alCambiarFecha(EvaluadorCaducidadClaves.calcularPreset(90))
                })
                ChipPresetCaducidad(texto = "180 días", alPulsar = {
                    alCambiarFecha(EvaluadorCaducidadClaves.calcularPreset(180))
                })
                ChipPresetCaducidad(texto = "1 año", alPulsar = {
                    alCambiarFecha(EvaluadorCaducidadClaves.calcularPreset(365))
                })
                ChipPresetCaducidad(
                    texto = "Elegir fecha…",
                    icono = Icons.Filled.CalendarMonth,
                    alPulsar = { abrirSelectorFecha() }
                )
            }
        }
    }
}

@Composable
private fun ChipPresetCaducidad(
    texto: String,
    alPulsar: () -> Unit,
    icono: androidx.compose.ui.graphics.vector.ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(FormaPequena)
            .background(ColorSeparadorAjustes.copy(alpha = 0.5f))
            .clickable(onClick = alPulsar)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = TextoSecundario,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = texto,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextoPrincipal
            )
        }
    }
}
