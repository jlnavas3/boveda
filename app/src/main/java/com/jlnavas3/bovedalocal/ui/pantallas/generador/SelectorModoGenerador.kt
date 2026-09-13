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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material.icons.filled.Shuffle
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
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun SelectorModoGenerador(
    modoActual: String,
    alSeleccionarModo: (String) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = FormaCampo

    val icono = when (modoActual) {
        "Frase Diceware" -> Icons.AutoMirrored.Filled.MenuBook
        "Por Patrón" -> Icons.Filled.Pattern
        else -> Icons.Filled.Shuffle
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp && (abierto || ColorBordeActual != Color.Transparent))
                        Modifier.border(GrosorBorde, if (abierto) ColorTitulos else ColorBordeActual, forma)
                    else Modifier
                )
                .clickable { abierto = true }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icono,
                contentDescription = null,
                tint = if (abierto) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Modo de generación", color = if (abierto) ColorTitulos else TextoSecundario, style = MaterialTheme.typography.labelSmall)
                Text(modoActual, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), maxLines = 1)
            }
            Icon(
                if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir modos de generación",
                tint = TextoSecundario
            )
        }

        MenuDesplegableBoveda(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            val opcionesModo = listOf(
                Triple("Aleatoria", "Caracteres alfanuméricos y símbolos", Icons.Filled.Shuffle),
                Triple("Frase Diceware", "Palabras memorables de alta entropía", Icons.AutoMirrored.Filled.MenuBook),
                Triple("Por Patrón", "Estructura y plantillas personalizadas", Icons.Filled.Pattern)
            )

            opcionesModo.forEachIndexed { index, (modo, desc, ic) ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val seleccionado = modoActual == modo
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            ic,
                            contentDescription = null,
                            tint = if (seleccionado) ColorIconosInternos else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = {
                        Column {
                            Text(
                                modo,
                                color = if (seleccionado) ColorTitulos else TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal)
                            )
                            Text(
                                desc,
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    trailingIcon = if (seleccionado) {
                        { Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp)) }
                    } else null,
                    onClick = {
                        alSeleccionarModo(modo)
                        abierto = false
                    }
                )
            }
        }
    }
}
