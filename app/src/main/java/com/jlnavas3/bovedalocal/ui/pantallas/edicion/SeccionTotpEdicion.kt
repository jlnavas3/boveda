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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun SeccionTotpEdicion(
    totp: String,
    alCambiarTotp: (String) -> Unit,
    mostrarSecretoTotp: Boolean,
    alAlternarMostrarSecreto: () -> Unit,
    totpValido: Boolean
) {
    GrupoAjustes(etiqueta = "Autenticador 2FA (Opcional)") {
        Column(modifier = Modifier.padding(14.dp)) {
            ComponenteCampoTexto(
                valor = totp,
                etiqueta = "Clave secreta 2FA (TOTP)",
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
}
