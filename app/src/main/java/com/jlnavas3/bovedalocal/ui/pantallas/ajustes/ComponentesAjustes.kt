package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorContraste

@Composable
fun EnlaceAjuste(texto: String, alPulsar: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text(
        texto,
        color = ColorAcento,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .clickable { alPulsar() }
            .padding(vertical = 4.dp)
    )
}

@Composable
fun DialogoContrasena(
    titulo: String,
    descripcion: String,
    textoBoton: String,
    alConfirmar: (String) -> Unit,
    alCancelar: () -> Unit
) {
    var valor by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = alCancelar,
        containerColor = ColorTarjetas,
        title = { Text(titulo) },
        text = {
            Column {
                Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                CampoBoveda(valor = valor, etiqueta = "Contraseña", alCambiar = { valor = it }, esContrasena = true)
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

@Composable
fun FilaAjuste(
    titulo: String,
    descripcion: String,
    activo: Boolean,
    habilitado: Boolean = true,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, color = TextoPrincipal, style = MaterialTheme.typography.titleMedium)
            Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        }
        Switch(
            checked = activo,
            enabled = habilitado,
            onCheckedChange = alCambiar,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ColorSobreAcento,
                checkedTrackColor = Ambar,
                checkedBorderColor = Ambar,
                uncheckedThumbColor = TextoSecundario,
                uncheckedTrackColor = SuperficieAlta,
                uncheckedBorderColor = TextoSecundario
            )
        )
    }
}

@Composable
fun TarjetaAjuste(
    titulo: String,
    icono: ImageVector,
    descripcion: String,
    inicialmenteAbierta: Boolean = false,
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    TarjetaBovedaDesplegable(
        titulo = titulo,
        icono = icono,
        descripcion = descripcion,
        inicialmenteAbierta = inicialmenteAbierta,
        contenido = contenido
    )
}

data class OpcionAjuste(
    val valor: String,
    val texto: String,
    val icono: ImageVector
)

@Composable
fun SelectorAjuste(
    titulo: String,
    icono: ImageVector,
    seleccionado: String,
    opciones: List<OpcionAjuste>,
    alSeleccionar: (String) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = FormaCampo
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp && (abierto || ColorBordeActual != Color.Transparent))
                        Modifier.border(GrosorBorde, if (abierto) ColorTitulos else ColorBordeActual, forma)
                    else Modifier
                )
                .clickable { abierto = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = if (abierto) ColorIconosInternos else TextoSecundario, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, color = if (abierto) ColorTitulos else TextoSecundario, style = MaterialTheme.typography.labelMedium)
                Text(seleccionado, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
            }
            Icon(
                if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir $titulo",
                tint = TextoSecundario
            )
        }
        MenuDesplegableBoveda(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            opciones.forEachIndexed { index, opcion ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            opcion.icono,
                            contentDescription = null,
                            tint = if (opcion.texto == seleccionado) ColorIconosInternos else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = {
                        Text(
                            opcion.texto,
                            color = if (opcion.texto == seleccionado) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = {
                        if (opcion.texto == seleccionado) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp))
                        }
                    },
                    onClick = {
                        alSeleccionar(opcion.valor)
                        abierto = false
                    }
                )
            }
        }
    }
}

@Composable
fun SwatchColor(color: Color, seleccionado: Boolean, descripcion: String, alPulsar: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(color)
            .border(width = if (seleccionado) 3.dp else 0.dp, color = TextoPrincipal, shape = CircleShape)
            .clickable { alPulsar() },
        contentAlignment = Alignment.Center
    ) {
        if (seleccionado) {
            Icon(Icons.Filled.Check, contentDescription = descripcion, tint = colorContraste(color))
        }
    }
}
