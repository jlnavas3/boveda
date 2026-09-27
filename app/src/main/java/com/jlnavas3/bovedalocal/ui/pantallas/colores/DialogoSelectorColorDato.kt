package com.jlnavas3.bovedalocal.ui.pantallas.colores

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

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

    AlertDialog(
        onDismissRequest = alCerrar,
        shape = RoundedCornerShape(22.dp),
        containerColor = Superficie,
        title = {
            Text(
                text = "Color de $tituloModal",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            SelectorColorEnTiempoReal(
                colorInicial = colorInicial,
                titulo = tituloModal
            ) { nuevoColor ->
                alCambiarColor(claveColor, nuevoColor)
            }
        },
        confirmButton = {
            TextButton(onClick = alCerrar) {
                Text("Aceptar", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    )
}
