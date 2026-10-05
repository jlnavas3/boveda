package com.jlnavas3.bovedalocal.ui.pantallas.categorias

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Dialogo modal molecular para crear o editar una [Categoria].
 */
@Composable
fun DialogoEditarCategoria(
    categoriaAEditar: Categoria? = null,
    alGuardar: (nombre: String, icono: String, colorHex: String?) -> Unit,
    alDescartar: () -> Unit
) {
    val esEdicion = categoriaAEditar != null
    var nombre by remember { mutableStateOf(categoriaAEditar?.nombre ?: "") }
    var iconoSeleccionado by remember { mutableStateOf(categoriaAEditar?.icono ?: "carpeta") }
    var colorSeleccionadoHex by remember { mutableStateOf(categoriaAEditar?.colorHex ?: IconosCategorias.COLORES_PREDETERMINADOS.first()) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (!esEdicion) {
            focusRequester.requestFocus()
        }
    }

    val colorAcentoFinal = IconosCategorias.parsearColorHex(colorSeleccionadoHex) ?: ColorAcento
    val iconoVector = IconosCategorias.obtenerIcono(iconoSeleccionado)

    DialogoBoveda(
        onDismissRequest = alDescartar,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ContenedorIconoInsignia(
                    icono = iconoVector,
                    tamano = TamanoInsignia.PEQUENO,
                    colorFondo = colorAcentoFinal.copy(alpha = 0.16f),
                    colorIcono = colorAcentoFinal
                )
                TextoTitulo(
                    texto = if (esEdicion) "Editar categoría" else "Nueva categoría",
                    estilo = EstiloTitulo.PEQUENO
                )
            }
        },
        confirmButton = {
            BotonTextoBoveda(
                texto = if (esEdicion) "Guardar" else "Crear",
                tipo = TipoBotonTexto.PRIMARIO,
                habilitado = nombre.isNotBlank(),
                alPulsar = {
                    if (nombre.isNotBlank()) {
                        alGuardar(nombre.trim(), iconoSeleccionado, colorSeleccionadoHex)
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
        },
        text = {
            Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { androidx.compose.material3.Text("Nombre de la categoría") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorAcento,
                    unfocusedBorderColor = ColorSeparadorAjustes,
                    focusedContainerColor = ColorCampoAjustes,
                    unfocusedContainerColor = ColorCampoAjustes,
                    focusedTextColor = TextoPrincipal,
                    unfocusedTextColor = TextoPrincipal
                ),
                shape = FormaPequena
            )

            // Selector de Icono
            TextoSubtitulo(texto = "Icono representativo")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconosCategorias.OPCIONES.forEach { opcion ->
                    val seleccionado = opcion.id.equals(iconoSeleccionado, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(FormaPequena)
                            .background(if (seleccionado) colorAcentoFinal.copy(alpha = 0.2f) else ColorCampoAjustes)
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
                            tint = if (seleccionado) colorAcentoFinal else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Selector de Color
            TextoSubtitulo(texto = "Color de la categoría")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconosCategorias.COLORES_PREDETERMINADOS.forEach { hex ->
                    val color = IconosCategorias.parsearColorHex(hex) ?: Color.Gray
                    val seleccionado = hex.equals(colorSeleccionadoHex, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (seleccionado) 2.5.dp else 0.dp,
                                color = if (seleccionado) Color.White else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { colorSeleccionadoHex = hex },
                        contentAlignment = Alignment.Center
                    ) {
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Seleccionado",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    )
}
