package com.jlnavas3.bovedalocal.ui.pantallas.colores

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal

@Composable
fun DialogoSelectorColorDato(
    claveColor: String,
    colorInicial: Color,
    alCerrar: () -> Unit,
    alCambiarColor: (clave: String, nuevoColor: Color) -> Unit
) {
    val tituloModal = when (claveColor) {
        "usuario" -> "Usuario / Correo"
        "contrasena" -> "Contraseña"
        "2fa" -> "Código 2FA (TOTP)"
        "passkey" -> "Passkey WebAuthn"
        "web" -> "Sitio Web (URL)"
        else -> "App Android vinculada"
    }

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Color de $tituloModal",
        botonConfirmar = {
            TextButton(onClick = alCerrar) {
                Text("Aceptar", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    ) {
        SelectorColorEnTiempoReal(
            colorInicial = colorInicial,
            titulo = tituloModal
        ) { nuevoColor ->
            alCambiarColor(claveColor, nuevoColor)
        }
    }
}
