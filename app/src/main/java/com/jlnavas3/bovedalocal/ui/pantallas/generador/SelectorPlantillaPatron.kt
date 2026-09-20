package com.jlnavas3.bovedalocal.ui.pantallas.generador

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

val PLANTILLAS_PATRON_RAPIDO = listOf(
    "Clave Windows" to "XXXXX-XXXXX-XXXXX-XXXXX-XXXXX",
    "PIN 4 dígitos" to "9999",
    "PIN 6 dígitos" to "999999",
    "Token 16" to "XXXX-XXXX-XXXX-XXXX",
    "Frase + Dígitos" to "w-9999"
)

@Composable
fun SelectorPlantillaPatron(
    patronActual: String,
    alSeleccionarPlantilla: (String) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(12.dp)
    val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
    val fondo = if (esOscuro) Color(0xFF161518) else Color(0xFFF4F4F6)
    val borde = if (esOscuro) Color(0xFF333238) else Color(0xFFDFDFE3)

    val nombreSeleccionado = PLANTILLAS_PATRON_RAPIDO.firstOrNull { it.second == patronActual }?.first ?: "Plantilla personalizada"

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(forma)
                .background(fondo)
                .clickable { abierto = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = if (abierto) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Plantilla rápida", color = if (abierto) ColorTitulos else TextoSecundario, style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp))
                Text(nombreSeleccionado, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp), maxLines = 1)
            }
            Icon(
                if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir plantillas rápidas",
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
        }

        MenuDesplegableBoveda(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            PLANTILLAS_PATRON_RAPIDO.forEachIndexed { index, (nombre, patron) ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val seleccionado = patronActual == patron
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Pattern,
                            contentDescription = null,
                            tint = if (seleccionado) ColorIconosInternos else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = {
                        Column {
                            Text(
                                nombre,
                                color = if (seleccionado) ColorTitulos else TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal)
                            )
                            Text(
                                patron,
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    trailingIcon = if (seleccionado) {
                        { Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp)) }
                    } else null,
                    onClick = {
                        alSeleccionarPlantilla(patron)
                        abierto = false
                    }
                )
            }
        }
    }
}
