package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Sección para clave secreta TOTP/2FA presentada en fila colapsable homogénea.
 */
@Composable
fun SeccionTotpEdicion(
    totp: String,
    alCambiarTotp: (String) -> Unit,
    mostrarSecretoTotp: Boolean,
    alAlternarMostrarSecreto: () -> Unit,
    totpValido: Boolean,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(totp.isNotBlank()) }

    FilaSeccionColapsableEdicion(
        icono = Icons.Filled.Timer,
        colorIcono = ColorDatos2FA,
        titulo = "Verificación en dos pasos (2FA)",
        resumen = if (totp.isBlank()) "Clave secreta opcional" else "Configurado",
        insigniaTexto = if (totp.isNotBlank()) "Activo" else null,
        expandido = expandido,
        alAlternarExpandido = { expandido = !expandido },
        modifier = modifier
    ) {
        ComponenteCampoTexto(
            valor = totp,
            etiqueta = "Clave secreta de verificación",
            alCambiar = { alCambiarTotp(it.uppercase()) },
            tipo = TipoCampoTexto.CONTRASENA,
            mostrarIcono = true,
            icono = Icons.Filled.Timer,
            colorBordeIzquierdo = ColorDatos2FA,
            mostrarContrasena = mostrarSecretoTotp,
            alAlternarMostrarContrasena = alAlternarMostrarSecreto,
            monoespaciada = true
        )
        if (!totpValido) {
            Spacer(Modifier.height(6.dp))
            Text("Ese secreto no es Base32 válido", color = Peligro, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun SeccionTotpEdicionPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        SeccionTotpEdicion(
            totp = "JBSWY3DPEHPK3PXP",
            alCambiarTotp = {},
            mostrarSecretoTotp = true,
            alAlternarMostrarSecreto = {},
            totpValido = true
        )
    }
}

