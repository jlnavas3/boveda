package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoContrasena(
    titulo: String,
    descripcion: String,
    textoBoton: String,
    alConfirmar: (String) -> Unit,
    alCancelar: () -> Unit
) {
    var valor by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    DialogoBoveda(
        onDismissRequest = alCancelar,
        title = { Text(titulo, color = TextoPrincipal) },
        text = {
            Column {
                Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                CampoBoveda(
                    valor = valor,
                    etiqueta = "Contraseña",
                    alCambiar = { valor = it },
                    esContrasena = true,
                    mostrarContrasena = mostrarContrasena,
                    alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena }
                )
            }
        },
        confirmButton = {
            TextButton(enabled = valor.length >= 8, onClick = { alConfirmar(valor) }) {
                Text(textoBoton, color = ColorAcento)
            }
        },
        dismissButton = { TextButton(onClick = alCancelar) { Text("Cancelar") } }
    )
}
