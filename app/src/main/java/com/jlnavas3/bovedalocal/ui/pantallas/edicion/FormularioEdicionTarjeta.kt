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
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import java.util.Calendar
import java.util.Locale

@Composable
fun FormularioEdicionTarjeta(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    val contexto = LocalContext.current
    var mostrarNumero by remember { mutableStateOf(false) }
    var mostrarCvv by remember { mutableStateOf(false) }
    var mostrarPin by remember { mutableStateOf(false) }

    val titular = GestorCamposBase.valorDeCampo(campos, "Titular")
    val numero = GestorCamposBase.valorDeCampo(campos, "Número de tarjeta")
    val vencimiento = GestorCamposBase.valorDeCampo(campos, "Vencimiento")
    val cvv = GestorCamposBase.valorDeCampo(campos, "CVV")
    val pin = GestorCamposBase.valorDeCampo(campos, "PIN de tarjeta")

    fun abrirSelectorFechaExpiracion() {
        val cal = Calendar.getInstance()
        var yActual = cal.get(Calendar.YEAR)
        var mActual = cal.get(Calendar.MONTH)
        if (vencimiento.contains("/")) {
            val partes = vencimiento.split("/")
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
            alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Vencimiento", nuevoVencimiento, TipoCampo.FECHA, formato = ajustes.formatoExpiracionTarjeta))
        }, yActual, mActual, 1).show()
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = titular,
            etiqueta = "Titular (nombre como figura en el plástico)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Titular", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = numero,
            etiqueta = "Número de tarjeta",
            alCambiar = { raw ->
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Número de tarjeta", raw, TipoCampo.TEXTO, sensible = true, formato = "TARJETA_CREDITO"))
            },
            formateadorMascara = { raw ->
                FormateadorCampos.aplicarMascaraTarjeta(raw, ajustes.formatoTarjeta)
            },
            esContrasena = true,
            mostrarContrasena = mostrarNumero,
            alAlternarMostrarContrasena = { mostrarNumero = !mostrarNumero },
            monoespaciada = true,
            tecladoNumerico = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = vencimiento,
                etiqueta = "Vencimiento (${ajustes.formatoExpiracionTarjeta})",
                alCambiar = { raw ->
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Vencimiento", raw, TipoCampo.FECHA, formato = ajustes.formatoExpiracionTarjeta))
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
                tecladoNumerico = true
            )

            CampoBoveda(
                valor = cvv,
                etiqueta = "CVV / CVC",
                alCambiar = { raw ->
                    val sanitizado = raw.filter { it.isDigit() }.take(4)
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "CVV", sanitizado, TipoCampo.NUMERO, sensible = true))
                },
                esContrasena = true,
                mostrarContrasena = mostrarCvv,
                alAlternarMostrarContrasena = { mostrarCvv = !mostrarCvv },
                monoespaciada = true,
                tecladoNumerico = true,
                modifier = Modifier.weight(1f)
            )
        }

        CampoBoveda(
            valor = pin,
            etiqueta = "PIN del cajero (opcional)",
            alCambiar = { raw ->
                val sanitizado = raw.filter { it.isDigit() }.take(12)
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "PIN de tarjeta", sanitizado, TipoCampo.PIN, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarPin,
            alAlternarMostrarContrasena = { mostrarPin = !mostrarPin },
            monoespaciada = true,
            tecladoNumerico = true
        )
    }
}
