package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

@Composable
fun DialogoComoFuncionaTotp(alDescartar: () -> Unit) {
    DialogoBoveda(
        onDismissRequest = alDescartar,
        icon = {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(fondoBadgeParaTema(Color2FA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Timer,
                    contentDescription = null,
                    tint = colorLegibleParaTema(Color2FA),
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        title = {
            Text(
                "¿Cómo funciona la verificación en dos pasos?",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Bóveda local calcula los códigos de 6 u 8 dígitos usando el reloj interno de tu dispositivo y la clave secreta compartida (RFC 6238).",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "No depende de servidores ni de conexión a internet para funcionar, manteniendo tus cuentas 100% protegidas y privadas.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Puedes activar la verificación en dos pasos escaneando el código QR del servicio o escribiendo la clave secreta manualmente con los botones de la esquina inferior.",
                    color = TextoSecundario.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            BotonTextoBoveda(
                texto = "Entendido",
                colorPersonalizado = Color2FA,
                alPulsar = alDescartar
            )
        }
    )
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun DialogoComoFuncionaTotpPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        DialogoComoFuncionaTotp(alDescartar = {})
    }
}

