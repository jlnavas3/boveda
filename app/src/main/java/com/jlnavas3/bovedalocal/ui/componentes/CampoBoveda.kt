package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto

@Composable
fun CampoBoveda(
    valor: String,
    etiqueta: String,
    alCambiar: (String) -> Unit,
    modifier: Modifier = Modifier,
    esContrasena: Boolean = false,
    mostrarContrasena: Boolean = false,
    alAlternarMostrarContrasena: (() -> Unit)? = null,
    monoespaciada: Boolean = false,
    varias: Boolean = false,
    tecladoNumerico: Boolean = false,
    keyboardType: KeyboardType? = null,
    readOnly: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    alPulsar: (() -> Unit)? = null,
    formateadorMascara: ((String) -> String)? = null
) {
    val tipo = when {
        esContrasena -> TipoCampoTexto.CONTRASENA
        tecladoNumerico -> TipoCampoTexto.NUMERICO
        varias -> TipoCampoTexto.MULTILINEA
        else -> TipoCampoTexto.TEXTO
    }
    ComponenteCampoTexto(
        valor = valor,
        etiqueta = etiqueta,
        alCambiar = alCambiar,
        modifier = modifier,
        tipo = tipo,
        esContrasena = esContrasena,
        mostrarContrasena = if (alAlternarMostrarContrasena != null) mostrarContrasena else null,
        alAlternarMostrarContrasena = alAlternarMostrarContrasena,
        monoespaciada = monoespaciada,
        varias = varias,
        tecladoNumerico = tecladoNumerico,
        keyboardType = keyboardType,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        alPulsar = alPulsar,
        formateadorMascara = formateadorMascara
    )
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun CampoBovedaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            CampoBoveda(
                valor = "usuario@correo.com",
                etiqueta = "Usuario / Correo",
                alCambiar = {}
            )
            CampoBoveda(
                valor = "ContraseñaSecreta123",
                etiqueta = "Contraseña Maestra",
                esContrasena = true,
                mostrarContrasena = false,
                alAlternarMostrarContrasena = {},
                alCambiar = {}
            )
        }
    }
}

