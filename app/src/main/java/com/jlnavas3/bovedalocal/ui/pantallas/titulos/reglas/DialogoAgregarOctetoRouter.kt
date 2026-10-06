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
fun DialogoAgregarOctetoRouter(
    alConfirmar: (Int) -> Unit,
    alDescartar: () -> Unit
) {
    var octetoTexto by remember { mutableStateOf("") }
    val numero = octetoTexto.trim().toIntOrNull()
    val esValido = numero != null && numero in 1..254

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Agregar octeto de puerta de enlace",
        botonConfirmar = {
            TextButton(
                onClick = {
                    numero?.let { alConfirmar(it) }
                },
                enabled = esValido
            ) {
                Text("Agregar", color = if (esValido) ColorAcento else TextoSecundario)
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
                text = "Ingresa el último número de la IP (1 al 254) que identifica a un router o gateway en tu red local:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = octetoTexto,
                alCambiar = { nuevo ->
                    if (nuevo.all { it.isDigit() } && nuevo.length <= 3) {
                        octetoTexto = nuevo
                    }
                },
                etiqueta = "Último octeto (ej. 1, 254, 250, 2)",
                tecladoNumerico = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Las IPs que terminen en este número usarán la plantilla de Router/Gateway.",
                color = TextoSecundario,
                fontSize = 12.sp
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaDialogoAgregarOctetoRouter() {
    BovedaTheme {
        DialogoAgregarOctetoRouter(
            alConfirmar = {},
            alDescartar = {}
        )
    }
}
