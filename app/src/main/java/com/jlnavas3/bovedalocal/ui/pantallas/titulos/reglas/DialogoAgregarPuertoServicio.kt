package com.jlnavas3.bovedalocal.ui.pantallas.titulos.reglas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoAgregarPuertoServicio(
    alConfirmar: (puerto: String, servicio: String) -> Unit,
    alDescartar: () -> Unit
) {
    var puerto by remember { mutableStateOf("") }
    var servicio by remember { mutableStateOf("") }

    val puertoLimpio = puerto.trim().removePrefix(":")
    val esPuertoValido = puertoLimpio.toIntOrNull()?.let { it in 1..65535 } ?: false
    val esValido = esPuertoValido && servicio.trim().isNotBlank()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Agregar servicio por puerto local",
        botonConfirmar = {
            TextButton(
                onClick = {
                    if (esValido) {
                        alConfirmar(puertoLimpio, servicio.trim())
                    }
                },
                enabled = esValido
            ) {
                Text("Guardar", color = if (esValido) ColorAcento else TextoSecundario)
            }
        },
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Asocia un puerto TCP/UDP en tu red local (o localhost) al nombre del servicio o panel homelab correspondiente:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = puerto,
                alCambiar = { nuevo ->
                    if (nuevo.all { it.isDigit() || it == ':' } && nuevo.length <= 6) {
                        puerto = nuevo
                    }
                },
                etiqueta = "Número de puerto (ej. 8006, 9000, 8123)",
                tecladoNumerico = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            CampoBoveda(
                valor = servicio,
                alCambiar = { servicio = it },
                etiqueta = "Nombre del servicio (ej. Proxmox, Portainer)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Al detectar este puerto en una IP o localhost, se nombrará automáticamente como este servicio.",
                color = TextoSecundario,
                fontSize = 12.sp
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaDialogoAgregarPuertoServicio() {
    BovedaTheme {
        DialogoAgregarPuertoServicio(
            alConfirmar = { _, _ -> },
            alDescartar = {}
        )
    }
}
