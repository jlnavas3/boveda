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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
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
import com.jlnavas3.bovedalocal.data.Identidad
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
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

private val PALETA_COLORES_IDENTIDAD = listOf(
    "#0284C7", // Azul cielo
    "#2563EB", // Azul real
    "#7C3AED", // Violeta
    "#9333EA", // Púrpura
    "#DB2777", // Rosa
    "#DC2626", // Rojo
    "#EA580C", // Naranja
    "#D97706", // Ámbar
    "#059669", // Verde esmeralda
    "#0D9488", // Teal
    "#4B5563"  // Grafito
)

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
        mutableStateOf(identidadAEditar?.colorHex ?: PALETA_COLORES_IDENTIDAD.first())
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (!esEdicion) {
            focusRequester.requestFocus()
        }
    }

    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val colorAcentoFinal = parsearColorO(colorSeleccionadoHex ?: "", ColorAcento)

    val camposValidos = nombre.isNotBlank() && correoPrincipal.isNotBlank() && correoPrincipal.contains("@")

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
                        imageVector = Icons.Filled.Badge,
                        contentDescription = null,
                        tint = colorAcentoFinal,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = if (esEdicion) "Editar identidad" else "Nueva identidad",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextoPrincipal
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
                    Text(
                        text = "Color identificador",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoPrincipal,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PALETA_COLORES_IDENTIDAD.forEach { hex ->
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
            TextButton(
                onClick = {
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
                },
                enabled = camposValidos
            ) {
                Text(
                    text = if (esEdicion) "Guardar" else "Crear",
                    fontWeight = FontWeight.Bold,
                    color = if (camposValidos) colorAcentoFinal else TextoSecundario
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
