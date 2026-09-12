package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaPasskeyDetalle(
    passkey: DatosPasskey,
    usuarioEntrada: String = ""
) {
    TarjetaPepo {
        EtiquetaSeccion("Passkey")
        Spacer(Modifier.height(8.dp))
        Text(
            "Servicio: ${passkey.rpName.ifBlank { passkey.rpId }}",
            color = TextoPrincipal,
            style = MaterialTheme.typography.bodyLarge
        )
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
        Spacer(Modifier.height(4.dp))
        Text(
            "La clave privada permanece cifrada dentro de la bóveda y nunca se muestra.",
            color = Menta,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
