/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import java.util.Calendar
import java.util.Locale

/**
 * Microcomponente desacoplado para edición directa y fuertemente tipada de tarjetas bancarias.
 */
@Composable
fun FormularioEdicionTarjeta(
    datos: DatosTarjetaBancaria,
    alCambiar: (DatosTarjetaBancaria) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    val contexto = LocalContext.current
    val ocultarPorDefecto = ajustes.seguridadVisualActiva && ajustes.ocultarContrasena
    var mostrarNumero by remember(ajustes.seguridadVisualActiva, ajustes.ocultarContrasena) { mutableStateOf(!ocultarPorDefecto) }
    var mostrarCvv by remember(ajustes.seguridadVisualActiva, ajustes.ocultarContrasena) { mutableStateOf(!ocultarPorDefecto) }
    var mostrarPin by remember(ajustes.seguridadVisualActiva, ajustes.ocultarContrasena) { mutableStateOf(!ocultarPorDefecto) }

    fun abrirSelectorFechaExpiracion() {
        val cal = Calendar.getInstance()
        var yActual = cal.get(Calendar.YEAR)
        var mActual = cal.get(Calendar.MONTH)
        if (datos.vencimiento.contains("/")) {
            val partes = datos.vencimiento.split("/")
            val mes = partes.getOrNull(0)?.toIntOrNull()
            if (mes != null && mes in 1..12) {
                mActual = mes - 1
            }
            val anio = partes.getOrNull(1)?.toIntOrNull()
            if (anio != null) {
                yActual = if (anio < 100) 2000 + anio else anio
            }
        }
        DatePickerDialog(contexto, { _, anio, mes, _ ->
            val mesStr = String.format(Locale.ROOT, "%02d", mes + 1)
            val anioStr = if (ajustes.formatoExpiracionTarjeta == FormateadorCampos.EXPIRACION_MMAAAA) {
                String.format(Locale.ROOT, "%04d", anio)
            } else {
                String.format(Locale.ROOT, "%02d", anio % 100)
            }
            val nuevoVencimiento = "$mesStr/$anioStr"
            alCambiar(datos.copy(vencimiento = nuevoVencimiento))
        }, yActual, mActual, 1).show()
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = datos.titular,
            etiqueta = "Titular (nombre como figura en el plástico)",
            alCambiar = {
                alCambiar(datos.copy(titular = it))
            },
            colorBordeIzquierdo = ColorDatosUsuario
        )

        CampoBoveda(
            valor = datos.numero,
            etiqueta = "Número de tarjeta",
            alCambiar = { raw ->
                val filtrado = raw.filter { it.isDigit() }.take(19)
                alCambiar(datos.copy(numero = filtrado))
            },
            formateadorMascara = { raw ->
                FormateadorCampos.aplicarMascaraTarjeta(raw, ajustes.formatoTarjeta)
            },
            esContrasena = true,
            mostrarContrasena = mostrarNumero,
            alAlternarMostrarContrasena = { mostrarNumero = !mostrarNumero },
            monoespaciada = true,
            tecladoNumerico = true,
            colorBordeIzquierdo = ColorAcento
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = datos.vencimiento,
                etiqueta = "Vencimiento (${ajustes.formatoExpiracionTarjeta})",
                alCambiar = { raw ->
                    alCambiar(datos.copy(vencimiento = raw))
                },
                formateadorMascara = { raw ->
                    FormateadorCampos.aplicarMascaraExpiracion(raw, ajustes.formatoExpiracionTarjeta)
                },
                trailingIcon = {
                    IconButton(onClick = { abrirSelectorFechaExpiracion() }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Elegir vencimiento",
                            tint = ColorIconosInternos
                        )
                    }
                },
                modifier = Modifier.weight(1f),
                tecladoNumerico = true,
                colorBordeIzquierdo = ColorDatos2FA
            )

            CampoBoveda(
                valor = datos.cvv,
                etiqueta = "CVV / CVC",
                alCambiar = { raw ->
                    val sanitizado = raw.filter { it.isDigit() }.take(4)
                    alCambiar(datos.copy(cvv = sanitizado))
                },
                esContrasena = true,
                mostrarContrasena = mostrarCvv,
                alAlternarMostrarContrasena = { mostrarCvv = !mostrarCvv },
                monoespaciada = true,
                tecladoNumerico = true,
                modifier = Modifier.weight(1f),
                colorBordeIzquierdo = ColorDatosContrasena
            )
        }

        CampoBoveda(
            valor = datos.pin,
            etiqueta = "PIN del cajero (opcional)",
            alCambiar = { raw ->
                val sanitizado = raw.filter { it.isDigit() }.take(12)
                alCambiar(datos.copy(pin = sanitizado))
            },
            esContrasena = true,
            mostrarContrasena = mostrarPin,
            alAlternarMostrarContrasena = { mostrarPin = !mostrarPin },
            monoespaciada = true,
            tecladoNumerico = true,
            colorBordeIzquierdo = ColorDatosContrasena
        )
    }
}
