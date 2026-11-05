/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosCuentaBancaria
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.util.FormateadorCampos

/**
 * Microcomponente desacoplado para edición directa y fuertemente tipada de cuentas bancarias.
 */
@Composable
fun FormularioEdicionCuentaBancaria(
    datos: DatosCuentaBancaria,
    alCambiar: (DatosCuentaBancaria) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    var mostrarIban by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = datos.banco,
            etiqueta = "Banco / Entidad financiera",
            alCambiar = {
                alCambiar(datos.copy(banco = it))
            },
            colorBordeIzquierdo = ColorDatosWeb
        )

        CampoBoveda(
            valor = datos.titular,
            etiqueta = "Titular de la cuenta",
            alCambiar = {
                alCambiar(datos.copy(titular = it))
            },
            colorBordeIzquierdo = ColorDatosUsuario
        )

        CampoBoveda(
            valor = datos.numeroCuenta,
            etiqueta = "Número de cuenta / IBAN",
            alCambiar = { raw ->
                val alfanumerico = raw.filter { it.isLetterOrDigit() }.uppercase()
                alCambiar(datos.copy(numeroCuenta = alfanumerico))
            },
            formateadorMascara = { raw ->
                FormateadorCampos.aplicarMascaraIban(raw, ajustes.formatoIban)
            },
            esContrasena = true,
            mostrarContrasena = mostrarIban,
            alAlternarMostrarContrasena = { mostrarIban = !mostrarIban },
            monoespaciada = true,
            colorBordeIzquierdo = ColorAcento
        )

        CampoBoveda(
            valor = datos.swiftBic,
            etiqueta = "SWIFT / BIC / CLABE / CBU",
            alCambiar = { raw ->
                alCambiar(datos.copy(swiftBic = FormateadorCampos.sanitizarMayusculas(raw)))
            },
            monoespaciada = true,
            colorBordeIzquierdo = ColorDatosWeb
        )
    }
}
