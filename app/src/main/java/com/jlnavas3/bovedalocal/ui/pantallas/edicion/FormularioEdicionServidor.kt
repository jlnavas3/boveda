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
fun FormularioEdicionServidor(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
) {
    var mostrarClave by remember { mutableStateOf(false) }

    val host = GestorCamposBase.valorDeCampo(campos, "Host o IP")
    val puerto = GestorCamposBase.valorDeCampo(campos, "Puerto")
    val usuario = GestorCamposBase.valorDeCampo(campos, "Usuario SSH")
    val clave = GestorCamposBase.valorDeCampo(campos, "Clave privada / Password")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = host,
                etiqueta = "Host o IP",
                alCambiar = {
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Host o IP", it, TipoCampo.TEXTO))
                },
                modifier = Modifier.weight(2.2f)
            )

            CampoBoveda(
                valor = puerto,
                etiqueta = "Puerto",
                alCambiar = { raw ->
                    val sanitizado = raw.filter { it.isDigit() }.take(5)
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Puerto", sanitizado, TipoCampo.NUMERO))
                },
                tecladoNumerico = true,
                modifier = Modifier.weight(1f)
            )
        }

        CampoBoveda(
            valor = usuario,
            etiqueta = "Usuario SSH (ej. root, ubuntu)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Usuario SSH", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = clave,
            etiqueta = "Clave privada SSH o Contraseña",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Clave privada / Password", it, TipoCampo.NOTAS, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarClave,
            alAlternarMostrarContrasena = { mostrarClave = !mostrarClave },
            monoespaciada = true,
            varias = true
        )
    }
}
