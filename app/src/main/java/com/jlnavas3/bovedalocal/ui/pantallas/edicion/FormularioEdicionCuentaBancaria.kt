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
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.util.FormateadorCampos

@Composable
fun FormularioEdicionCuentaBancaria(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    var mostrarIban by remember { mutableStateOf(false) }

    val banco = GestorCamposBase.valorDeCampo(campos, "Banco / Entidad")
    val titular = GestorCamposBase.valorDeCampo(campos, "Titular de la cuenta")
    val iban = GestorCamposBase.valorDeCampo(campos, "Número de cuenta / IBAN")
    val swift = GestorCamposBase.valorDeCampo(campos, "SWIFT / CBU / CLABE")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = banco,
            etiqueta = "Banco / Entidad financiera",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Banco / Entidad", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = titular,
            etiqueta = "Titular de la cuenta",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Titular de la cuenta", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = iban,
            etiqueta = "Número de cuenta / IBAN",
            alCambiar = { raw ->
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Número de cuenta / IBAN", raw, TipoCampo.TEXTO, sensible = true, formato = "IBAN"))
            },
            formateadorMascara = { raw ->
                FormateadorCampos.aplicarMascaraIban(raw, ajustes.formatoIban)
            },
            esContrasena = true,
            mostrarContrasena = mostrarIban,
            alAlternarMostrarContrasena = { mostrarIban = !mostrarIban },
            monoespaciada = true
        )

        CampoBoveda(
            valor = swift,
            etiqueta = "SWIFT / BIC / CLABE / CBU",
            alCambiar = { raw ->
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "SWIFT / CBU / CLABE", FormateadorCampos.sanitizarMayusculas(raw), TipoCampo.TEXTO, formato = "SWIFT_BIC"))
            },
            monoespaciada = true
        )
    }
}
