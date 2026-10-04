package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun SelectorModoEdicion(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    modifier: Modifier = Modifier
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = FormaCampo
    val fondo = ColorCampoAjustes

    val (icono, titulo) = when {
        opciones.modoFrase -> Icons.AutoMirrored.Filled.MenuBook to "Diceware"
        opciones.modoPatron -> Icons.Filled.Pattern to "Por patrón"
        else -> Icons.Filled.Shuffle to "Aleatoria"
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(forma)
                .background(fondo)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else Modifier
                )
                .clickable { abierto = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (abierto) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Desplegar modos",
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
        }

        MenuDesplegableBoveda(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            val listaModos = listOf(
                Triple("Aleatoria", Icons.Filled.Shuffle) {
                    opciones.copy(modoFrase = false, modoPatron = false)
                },
                Triple("Diceware", Icons.AutoMirrored.Filled.MenuBook) {
                    opciones.copy(modoFrase = true, modoPatron = false)
                },
                Triple("Por patrón", Icons.Filled.Pattern) {
                    opciones.copy(modoFrase = false, modoPatron = true)
                }
            )

            listaModos.forEachIndexed { index, (nombre, ic, fnCambio) ->
                if (index > 0) SeparadorOpcionMenu()
                val seleccionado = titulo == nombre
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = ic,
                            contentDescription = null,
                            tint = if (seleccionado) ColorIconosInternos else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = if (seleccionado) {
                        { Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp)) }
                    } else null,
                    text = {
                        Text(
                            text = nombre,
                            color = if (seleccionado) ColorIconosInternos else TextoPrincipal,
                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        alCambiarOpciones(fnCambio())
                        abierto = false
                    }
                )
            }
        }
    }
}
