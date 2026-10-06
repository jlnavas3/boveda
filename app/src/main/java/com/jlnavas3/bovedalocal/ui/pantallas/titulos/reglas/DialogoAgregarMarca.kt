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
fun DialogoAgregarMarca(
    alConfirmar: (dominio: String, nombre: String) -> Unit,
    alDescartar: () -> Unit
) {
    var dominio by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    val esValido = dominio.trim().isNotBlank() && nombre.trim().isNotBlank()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Agregar marca o servicio",
        botonConfirmar = {
            TextButton(
                onClick = {
                    if (esValido) {
                        alConfirmar(dominio.trim(), nombre.trim())
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
                text = "Define un nombre personalizado para una marca o dominio web (ej. 'miempresa.com' → 'Mi Empresa'):",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = dominio,
                alCambiar = { dominio = it },
                etiqueta = "Dominio o palabra clave (ej. miempresa)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            CampoBoveda(
                valor = nombre,
                alCambiar = { nombre = it },
                etiqueta = "Nombre a mostrar (ej. Mi Empresa S.A.)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sobrescribe la capitalización automática y las marcas oficiales.",
                color = TextoSecundario,
                fontSize = 12.sp
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaDialogoAgregarMarca() {
    BovedaTheme {
        DialogoAgregarMarca(
            alConfirmar = { _, _ -> },
            alDescartar = {}
        )
    }
}
