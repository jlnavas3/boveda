package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoNombreBoveda(
    nombreActual: String,
    alCerrar: () -> Unit,
    alGuardar: (String) -> Unit
) {
    var nombreTemporal by remember(nombreActual) { mutableStateOf(nombreActual) }

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Nombre app",
        botonConfirmar = {
            TextButton(onClick = {
                alCerrar()
                alGuardar(nombreTemporal.trim())
            }) {
                Text("Guardar", color = ColorAcento)
            }
        },
        botonDescartar = {
            Row {
                TextButton(onClick = {
                    nombreTemporal = ""
                    alGuardar("")
                    alCerrar()
                }) {
                    Text("Restablecer", color = TextoSecundario)
                }
                TextButton(onClick = alCerrar) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        }
    ) {
        Column {
            Text(
                "Personaliza el nombre que se muestra en la cabecera del listado y en la barra lateral. Si se deja vacío se usará \"Bóveda local\".",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(14.dp))
            CampoBoveda(
                valor = nombreTemporal,
                etiqueta = "Nombre de la bóveda",
                alCambiar = { nombreTemporal = it }
            )
        }
    }
}
