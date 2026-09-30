package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoBorrarSeleccion(
    cantidad: Int,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "¿Mover $cantidad entradas a la papelera?",
        mensaje = "Se pueden restaurar desde la papelera durante 30 días.",
        textoConfirmar = "Mover a la papelera",
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.Delete,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}

@Composable
fun DialogoRenombrarSeleccion(
    cantidad: Int,
    textoNuevoTitulo: String,
    alCambiarTexto: (String) -> Unit,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        icono = Icons.Filled.Edit,
        colorIcono = Ambar,
        titulo = if (cantidad == 1) "Renombrar título" else "Renombrar título ($cantidad seleccionadas)",
        botonConfirmar = {
            TextButton(
                onClick = {
                    if (textoNuevoTitulo.isNotBlank()) {
                        alConfirmar()
                    }
                },
                enabled = textoNuevoTitulo.isNotBlank()
            ) {
                Text("Renombrar", color = if (textoNuevoTitulo.isNotBlank()) Ambar else TextoSecundario)
            }
        },
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    ) {
        Column {
            Text(
                "Introduce el nuevo título para ${if (cantidad == 1) "la entrada seleccionada" else "las $cantidad entradas seleccionadas"}:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            ComponenteCampoTexto(
                valor = textoNuevoTitulo,
                etiqueta = "Nuevo título",
                alCambiar = alCambiarTexto,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (textoNuevoTitulo.isNotBlank()) {
                            alConfirmar()
                        }
                    }
                ),
                botonLimpiar = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
