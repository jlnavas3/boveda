package com.jlnavas3.bovedalocal.ui.pantallas.colecciones

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoCrearEditarColeccion(
    coleccionAEditar: Coleccion? = null,
    alGuardar: (nombre: String, icono: String, colorHex: String?) -> Unit,
    alDescartar: () -> Unit
) {
    val esEdicion = coleccionAEditar != null
    var nombre by remember { mutableStateOf(coleccionAEditar?.nombre ?: "") }
    var iconoSeleccionado by remember { mutableStateOf(coleccionAEditar?.icono ?: "carpeta") }
    var colorSeleccionadoHex by remember { mutableStateOf(coleccionAEditar?.colorHex) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val colorAcentoFinal = IconosColecciones.parsearColorHex(colorSeleccionadoHex) ?: ColorAcento

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = forma,
        containerColor = ColorTarjetaAjustes,
        modifier = Modifier.then(
            if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                Modifier.border(GrosorBorde, ColorBordeActual, forma)
            } else Modifier
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(FormaPequena)
                        .background(colorAcentoFinal.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = IconosColecciones.obtenerIcono(iconoSeleccionado),
                        contentDescription = null,
                        tint = colorAcentoFinal,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = if (esEdicion) "Editar Colección" else "Nueva Colección",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Nombre",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Ej. Finanzas, Trabajo, Juegos...", color = TextoSecundario.copy(alpha = 0.6f)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextoPrincipal,
                        unfocusedTextColor = TextoPrincipal,
                        focusedContainerColor = ColorCampoAjustes,
                        unfocusedContainerColor = ColorCampoAjustes,
                        focusedBorderColor = colorAcentoFinal,
                        unfocusedBorderColor = ColorSeparadorAjustes
                    ),
                    shape = FormaPequena,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )

                Spacer(Modifier.height(14.dp))

                // Selector de Icono
                Text(
                    text = "Icono representativo",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconosColecciones.OPCIONES.forEach { opcion ->
                        val seleccionado = iconoSeleccionado.equals(opcion.id, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(FormaPequena)
                                .background(if (seleccionado) colorAcentoFinal else ColorCampoAjustes)
                                .border(
                                    width = if (seleccionado) 1.5.dp else 0.8.dp,
                                    color = if (seleccionado) colorAcentoFinal else ColorSeparadorAjustes,
                                    shape = FormaPequena
                                )
                                .clickable { iconoSeleccionado = opcion.id },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = opcion.icono,
                                contentDescription = opcion.etiqueta,
                                tint = if (seleccionado) Color.White else TextoSecundario,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Selector de Color
                Text(
                    text = "Color identificador",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Opción sin color (usa acento del tema)
                    val sinColorSeleccionado = colorSeleccionadoHex == null
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(ColorAcento)
                            .border(
                                width = if (sinColorSeleccionado) 2.dp else 1.dp,
                                color = if (sinColorSeleccionado) TextoPrincipal else ColorSeparadorAjustes,
                                shape = CircleShape
                            )
                            .clickable { colorSeleccionadoHex = null },
                        contentAlignment = Alignment.Center
                    ) {
                        if (sinColorSeleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    IconosColecciones.COLORES_PREDETERMINADOS.forEach { hex ->
                        val color = IconosColecciones.parsearColorHex(hex) ?: Color.Gray
                        val seleccionado = colorSeleccionadoHex.equals(hex, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (seleccionado) 2.dp else 1.dp,
                                    color = if (seleccionado) TextoPrincipal else ColorSeparadorAjustes,
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
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nombre.isNotBlank()) {
                        alGuardar(nombre.trim(), iconoSeleccionado, colorSeleccionadoHex)
                    }
                },
                enabled = nombre.isNotBlank()
            ) {
                Text(
                    text = if (esEdicion) "Actualizar" else "Crear",
                    color = if (nombre.isNotBlank()) colorAcentoFinal else TextoSecundario,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    )
}
