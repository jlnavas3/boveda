package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonPrimario
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun SeccionEntradaManual(
    manual: String,
    alCambiarManual: (String) -> Unit,
    fallo: Boolean,
    soloManual: Boolean,
    alAgregarCodigo: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().clip(FormaTarjeta)) {
        Column(modifier = Modifier.fillMaxWidth().padding(0.dp)) {
            Text(
                if (soloManual) "Pega o escribe la clave" else "O escríbelo a mano",
                color = ColorTitulos,
                style = MaterialTheme.typography.titleMedium
            )
            if (soloManual) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "Es la clave que la web te da junto al QR, la que suele venir " +
                        "en bloques de cuatro letras. Sirve igual que escanearlo.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.height(10.dp))
            CampoBoveda(
                valor = manual,
                etiqueta = "Clave del 2FA o enlace otpauth://",
                alCambiar = alCambiarManual,
                monoespaciada = true
            )
            if (fallo) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "Eso no me sirve. Espero la clave en Base32 (letras A-Z y números 2-7) o un enlace otpauth://totp/...",
                    color = Peligro,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.height(12.dp))
            BotonPrimario(
                texto = "Añadir este código",
                icono = Icons.Filled.Add,
                activo = manual.isNotBlank(),
                alPulsar = alAgregarCodigo
            )
        }
    }
}
