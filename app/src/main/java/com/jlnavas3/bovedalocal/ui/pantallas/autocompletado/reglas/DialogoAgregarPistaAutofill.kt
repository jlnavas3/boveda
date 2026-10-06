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
fun DialogoAgregarPistaAutofill(
    tipoCampo: String,
    ejemploSugerido: String,
    alConfirmar: (String) -> Unit,
    alDescartar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    val limpio = texto.trim().lowercase()
    val esValido = limpio.isNotBlank()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Agregar palabra clave para $tipoCampo",
        botonConfirmar = {
            TextButton(
                onClick = {
                    if (esValido) {
                        alConfirmar(limpio)
                    }
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
                text = "Escribe el identificador o término que las apps o sitios web usan para solicitar tu $tipoCampo (ej. $ejemploSugerido):",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = texto,
                alCambiar = { texto = it },
                etiqueta = "Palabra clave o identificador",
                modifier = Modifier.fillMaxWidth()
            )
            if (esValido) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Campos que contengan '$limpio' serán autocompletados como $tipoCampo.",
                    color = TextoSecundario,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaDialogoAgregarPistaAutofill() {
    BovedaTheme {
        DialogoAgregarPistaAutofill(
            tipoCampo = "usuario",
            ejemploSugerido = "'cedula', 'dni', 'ruc'",
            alConfirmar = {},
            alDescartar = {}
        )
    }
}
