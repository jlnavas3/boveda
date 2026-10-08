package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaPasskeyDetalle(
    passkey: DatosPasskey,
    usuarioEntrada: String = ""
) {
    val rpName = passkey.rpName.trim()
    val rpId = passkey.rpId.trim()
    val hayNombreDistinto = rpName.isNotBlank() && !rpName.equals(rpId, ignoreCase = true)

    val usuarioPasskey = passkey.usuario.trim()
    val usuarioBase = usuarioEntrada.trim()
    val mostrarCuenta = usuarioPasskey.isNotBlank() && !usuarioPasskey.equals(usuarioBase, ignoreCase = true)

    val codigoLlave = remember(passkey.credId) {
        formatearCodigoLlave(passkey.credId)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Llave de paso")
        TarjetaDatoDetalle(colorBorde = ColorDatosPasskey) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Título principal del servicio / dominio
                if (hayNombreDistinto) {
                    Text(
                        rpName,
                        color = ColorTitulos,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Dominio: $rpId",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        rpId.ifBlank { "Llave de paso WebAuthn" },
                        color = ColorTitulos,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                // Cuenta (solo si difiere de la mostrada arriba)
                if (mostrarCuenta) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        "Cuenta: $usuarioPasskey",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Código / Identificador de llave para distinguir entre llaves del mismo servicio
                if (codigoLlave.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "Código de llave:",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            codigoLlave,
                            color = ColorDatosPasskey,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                Spacer(Modifier.height(3.dp))
                Text(
                    "Algoritmo: ${passkey.algoritmo} (P-256)",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    "La clave privada permanece cifrada en hardware local y nunca se exporta en texto claro.",
                    color = Menta,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun formatearCodigoLlave(credId: String): String {
    val limpio = credId.trim()
    if (limpio.isBlank()) return ""
    if (limpio.contains("-")) {
        val partes = limpio.split("-").filter { it.isNotBlank() }
        return when {
            partes.size >= 3 -> "${partes.first()}…${partes.last()}"
            partes.isNotEmpty() -> partes.first()
            else -> limpio.take(10)
        }
    }
    return if (limpio.length > 14) {
        "${limpio.take(8)}…${limpio.takeLast(6)}"
    } else {
        limpio
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaPasskeyDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        TarjetaPasskeyDetalle(
            passkey = DatosPasskey(
                rpId = "google.com",
                rpName = "Google Accounts",
                userHandle = "user-12345",
                credId = "cred-passkey-abc",
                clavePrivada = "privkey-mock",
                usuario = "usuario@correo.com"
            )
        )
    }
}

