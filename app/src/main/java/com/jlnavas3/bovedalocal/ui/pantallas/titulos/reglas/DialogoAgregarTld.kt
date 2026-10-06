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
fun DialogoAgregarTld(
    alConfirmar: (String) -> Unit,
    alDescartar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    val limpio = texto.trim().lowercase().removePrefix(".").removeSuffix(".")
    val esValido = limpio.isNotBlank()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Agregar extensión de dominio",
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
                text = "Escribe la extensión o TLD que deseas recortar de los títulos (ej. 'tech', 'co', 'pe', 'ai', 'lan'):",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = texto,
                alCambiar = { texto = it },
                etiqueta = "Extensión (ej. .tech o tech)",
                modifier = Modifier.fillMaxWidth()
            )
            if (esValido) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Ejemplo: 'servicio.$limpio' → 'Servicio'",
                    color = TextoSecundario,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaDialogoAgregarTld() {
    BovedaTheme {
        DialogoAgregarTld(
            alConfirmar = {},
            alDescartar = {}
        )
    }
}
