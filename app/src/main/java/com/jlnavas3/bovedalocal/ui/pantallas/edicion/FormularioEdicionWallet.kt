package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda

@Composable
fun FormularioEdicionWallet(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
) {
    var mostrarSemilla by remember { mutableStateOf(false) }
    var mostrarClavePrivada by remember { mutableStateOf(false) }

    val red = GestorCamposBase.valorDeCampo(campos, "Red / Blockchain")
    val direccion = GestorCamposBase.valorDeCampo(campos, "Dirección pública")
    val semilla = GestorCamposBase.valorDeCampo(campos, "Frase semilla (Seed phrase)")
    val clavePrivada = GestorCamposBase.valorDeCampo(campos, "Clave privada")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = red,
            etiqueta = "Red / Blockchain (ej. Ethereum, Bitcoin, Solana)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Red / Blockchain", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = direccion,
            etiqueta = "Dirección pública (clave pública)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Dirección pública", it, TipoCampo.TEXTO))
            },
            monoespaciada = true
        )

        CampoBoveda(
            valor = semilla,
            etiqueta = "Frase semilla (12 o 24 palabras mnemónicas)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Frase semilla (Seed phrase)", it, TipoCampo.NOTAS, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarSemilla,
            alAlternarMostrarContrasena = { mostrarSemilla = !mostrarSemilla },
            monoespaciada = true,
            varias = true
        )

        CampoBoveda(
            valor = clavePrivada,
            etiqueta = "Clave privada (Private key)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Clave privada", it, TipoCampo.NOTAS, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarClavePrivada,
            alAlternarMostrarContrasena = { mostrarClavePrivada = !mostrarClavePrivada },
            monoespaciada = true,
            varias = true
        )
    }
}
