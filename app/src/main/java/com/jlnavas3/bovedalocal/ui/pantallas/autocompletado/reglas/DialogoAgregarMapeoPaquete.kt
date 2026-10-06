package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado.reglas

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
fun DialogoAgregarMapeoPaquete(
    alConfirmar: (paquete: String, dominio: String) -> Unit,
    alDescartar: () -> Unit
) {
    var paquete by remember { mutableStateOf("") }
    var dominio by remember { mutableStateOf("") }

    val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
    val dominioLimpio = dominio.trim().lowercase().removePrefix("https://").removePrefix("http://").removePrefix("www.").substringBefore('/')
    val esValido = paqueteLimpio.isNotBlank() && dominioLimpio.isNotBlank()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Vincular App a Dominio Web",
        botonConfirmar = {
            TextButton(
                onClick = {
                    if (esValido) {
                        alConfirmar(paqueteLimpio, dominioLimpio)
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
                text = "Asocia el identificador de paquete Android de una app instalada con su dominio web oficial para autocompletar contraseñas guardadas en la web dentro de la app nativa:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = paquete,
                alCambiar = { paquete = it },
                etiqueta = "Paquete de la app (ej. com.bancopichincha.banca)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            CampoBoveda(
                valor = dominio,
                alCambiar = { dominio = it },
                etiqueta = "Dominio web asociado (ej. pichincha.com)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Al abrir la app de Android, se sugerirán las credenciales guardadas para ese dominio.",
                color = TextoSecundario,
                fontSize = 12.sp
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaDialogoAgregarMapeoPaquete() {
    BovedaTheme {
        DialogoAgregarMapeoPaquete(
            alConfirmar = { _, _ -> },
            alDescartar = {}
        )
    }
}
