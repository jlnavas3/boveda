package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda

@Composable
fun FormularioEdicionTarjeta(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
) {
    var mostrarNumero by remember { mutableStateOf(false) }
    var mostrarCvv by remember { mutableStateOf(false) }
    var mostrarPin by remember { mutableStateOf(false) }

    val titular = GestorCamposBase.valorDeCampo(campos, "Titular")
    val numero = GestorCamposBase.valorDeCampo(campos, "Número de tarjeta")
    val vencimiento = GestorCamposBase.valorDeCampo(campos, "Vencimiento")
    val cvv = GestorCamposBase.valorDeCampo(campos, "CVV")
    val pin = GestorCamposBase.valorDeCampo(campos, "PIN de tarjeta")

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
                val sanitizado = raw.filter { it.isDigit() || it == ' ' }
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Número de tarjeta", sanitizado, TipoCampo.TEXTO, sensible = true, formato = "TARJETA_CREDITO"))
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
                etiqueta = "Vencimiento (MM/AA)",
                alCambiar = { raw ->
                    val sanitizado = raw.filter { it.isDigit() || it == '/' }.take(5)
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Vencimiento", sanitizado, TipoCampo.FECHA, formato = "MM/AA"))
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

@Composable
fun FormularioEdicionCuentaBancaria(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
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
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Número de cuenta / IBAN", it, TipoCampo.TEXTO, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarIban,
            alAlternarMostrarContrasena = { mostrarIban = !mostrarIban },
            monoespaciada = true
        )

        CampoBoveda(
            valor = swift,
            etiqueta = "SWIFT / BIC / CLABE / CBU",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "SWIFT / CBU / CLABE", it, TipoCampo.TEXTO))
            },
            monoespaciada = true
        )
    }
}
