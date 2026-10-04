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
fun DialogoCambioMaestra(
    alDescartar: () -> Unit,
    alConfirmar: (actual: String, nueva: String) -> Unit
) {
    var actualMaestra by remember { mutableStateOf("") }
    var nuevaMaestra by remember { mutableStateOf("") }

    DialogoBoveda(
        onDismissRequest = alDescartar,
        title = { Text("Cambiar contraseña maestra", color = TextoPrincipal) },
        text = {
            Column {
                CampoBoveda(valor = actualMaestra, etiqueta = "Contraseña actual", alCambiar = { actualMaestra = it }, esContrasena = true)
                Spacer(Modifier.height(10.dp))
                CampoBoveda(valor = nuevaMaestra, etiqueta = "Nueva contraseña", alCambiar = { nuevaMaestra = it }, esContrasena = true)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Se vuelve a cifrar toda la bóveda y se desactiva la huella.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = nuevaMaestra.length >= 10 && actualMaestra.isNotEmpty(),
                onClick = {
                    alConfirmar(actualMaestra, nuevaMaestra)
                }
            ) { Text("Cambiar", color = ColorAcento) }
        },
        dismissButton = { TextButton(onClick = alDescartar) { Text("Cancelar") } }
    )
}
