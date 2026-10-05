package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Diálogo modal para la creación y edición de perfiles de [Identidad].
 */
@Composable
fun DialogoEditarIdentidad(
    identidadAEditar: Identidad? = null,
    alGuardar: (nombre: String, correoPrincipal: String, correosSecundarios: List<String>, colorHex: String?, icono: String) -> Unit,
    alDescartar: () -> Unit
) {
    val esEdicion = identidadAEditar != null
    var nombre by remember { mutableStateOf(identidadAEditar?.nombre ?: "") }
    var correoPrincipal by remember { mutableStateOf(identidadAEditar?.correoPrincipal ?: "") }
    var correosSecundariosTexto by remember {
        mutableStateOf(identidadAEditar?.correosSecundarios?.joinToString(", ") ?: "")
    }
    var colorSeleccionadoHex by remember {
        mutableStateOf(identidadAEditar?.colorHex ?: PaletaColoresIdentidad.first())
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (!esEdicion) {
            focusRequester.requestFocus()
        }
    }

    val colorAcentoFinal = parsearColorO(colorSeleccionadoHex ?: "", ColorAcento)
    val camposValidos = nombre.isNotBlank() && correoPrincipal.isNotBlank() && correoPrincipal.contains("@")

    DialogoBoveda(
        onDismissRequest = alDescartar,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ContenedorIconoInsignia(
                    icono = Icons.Filled.AccountCircle,
                    tamano = TamanoInsignia.PEQUENO,
                    colorFondo = colorAcentoFinal.copy(alpha = 0.16f),
                    colorIcono = colorAcentoFinal
                )
                TextoTitulo(
                    texto = if (esEdicion) "Editar identidad" else "Nueva identidad",
                    estilo = EstiloTitulo.PEQUENO
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del perfil (ej. Personal, Trabajo)") },
                    singleLine = true,
                    shape = FormaPequena,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ColorCampoAjustes,
                        unfocusedContainerColor = ColorCampoAjustes,
                        focusedBorderColor = colorAcentoFinal,
                        unfocusedBorderColor = ColorSeparadorAjustes,
                        focusedTextColor = TextoPrincipal,
                        unfocusedTextColor = TextoPrincipal
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )

                OutlinedTextField(
                    value = correoPrincipal,
                    onValueChange = { correoPrincipal = it },
                    label = { Text("Correo electrónico principal") },
                    placeholder = { Text("ejemplo@correo.com") },
                    singleLine = true,
                    shape = FormaPequena,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ColorCampoAjustes,
                        unfocusedContainerColor = ColorCampoAjustes,
                        focusedBorderColor = colorAcentoFinal,
                        unfocusedBorderColor = ColorSeparadorAjustes,
                        focusedTextColor = TextoPrincipal,
                        unfocusedTextColor = TextoPrincipal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = correosSecundariosTexto,
                    onValueChange = { correosSecundariosTexto = it },
                    label = { Text("Alias / correos adicionales (separados por coma)") },
                    placeholder = { Text("alias1@dominio.com, alias2@empresa.com") },
                    singleLine = false,
                    maxLines = 2,
                    shape = FormaPequena,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ColorCampoAjustes,
                        unfocusedContainerColor = ColorCampoAjustes,
                        focusedBorderColor = colorAcentoFinal,
                        unfocusedBorderColor = ColorSeparadorAjustes,
                        focusedTextColor = TextoPrincipal,
                        unfocusedTextColor = TextoPrincipal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    TextoSubtitulo(
                        texto = "Color identificador",
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PaletaColoresIdentidad.forEach { hex ->
                            val colorActual = parsearColorO(hex, ColorAcento)
                            val seleccionado = (colorSeleccionadoHex?.equals(hex, ignoreCase = true) == true)
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(colorActual)
                                    .border(
                                        width = if (seleccionado) 2.5.dp else 1.dp,
                                        color = if (seleccionado) TextoPrincipal else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { colorSeleccionadoHex = hex },
                                contentAlignment = Alignment.Center
                            ) {
                                if (seleccionado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            BotonTextoBoveda(
                texto = if (esEdicion) "Guardar" else "Crear",
                tipo = TipoBotonTexto.PRIMARIO,
                colorPersonalizado = colorAcentoFinal,
                habilitado = camposValidos,
                alPulsar = {
                    if (camposValidos) {
                        val secundarios = correosSecundariosTexto
                            .split(",", "\n", ";")
                            .map { it.trim() }
                            .filter { it.isNotBlank() && it.contains("@") }
                        alGuardar(
                            nombre.trim(),
                            correoPrincipal.trim(),
                            secundarios,
                            colorSeleccionadoHex,
                            "person"
                        )
                    }
                }
            )
        },
        dismissButton = {
            BotonTextoBoveda(
                texto = "Cancelar",
                tipo = TipoBotonTexto.SECUNDARIO,
                alPulsar = alDescartar
            )
        }
    )
}
