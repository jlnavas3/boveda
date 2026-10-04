package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Selector desplegable para cambiar el criterio de ordenación de la lista.
 */
@Composable
fun SelectorOrdenacion(
    criterio: CriterioOrdenacion,
    alCambiar: (CriterioOrdenacion) -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    val forma = FormaCampo

    Box {
        Row(
            modifier = Modifier
                .height(42.dp)
                .clip(forma)
                .background(ColorTarjetaAjustes)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    else Modifier
                )
                .clickable { desplegado = true }
                .padding(horizontal = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Ordenar lista",
                tint = if (desplegado) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }

        MenuDesplegableBoveda(
            expanded = desplegado,
            onDismissRequest = { desplegado = false }
        ) {
            CriterioOrdenacion.entries.forEachIndexed { index, op ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val activo = op == criterio
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            tint = if (activo) ColorIconosInternos else TextoSecundario
                        )
                    },
                    trailingIcon = if (activo) {
                        { Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp)) }
                    } else null,
                    text = {
                        Text(
                            op.etiqueta,
                            color = if (activo) ColorTitulos else TextoPrincipal,
                            fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        alCambiar(op)
                        desplegado = false
                    }
                )
            }
        }
    }
}
