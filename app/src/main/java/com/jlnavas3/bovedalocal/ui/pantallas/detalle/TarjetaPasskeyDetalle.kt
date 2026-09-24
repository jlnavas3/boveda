package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaPasskeyDetalle(
    passkey: DatosPasskey,
    usuarioEntrada: String = ""
) {
    val servicio = passkey.rpName.ifBlank { passkey.rpId }
    GrupoAjustes(etiqueta = "Llave de acceso (Passkey)") {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .background(ColorDatosPasskey)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "Servicio: $servicio",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Dominio: ${passkey.rpId}",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (passkey.usuario.isNotBlank() || usuarioEntrada.isNotBlank()) {
                    Text(
                        "Cuenta: ${passkey.usuario.ifBlank { usuarioEntrada }}",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
