package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

data class OpcionItemSelector<T>(
    val valor: T,
    val etiqueta: String,
    val icono: ImageVector? = null
)

/**
 * Fila con menú desplegable emergente para opciones múltiples compactas estilo Samsung One UI.
 */
@Composable
fun <T> ComponenteSelector(
    titulo: String,
    valorSeleccionado: T,
    opciones: List<OpcionItemSelector<T>>,
    alSeleccionar: (T) -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    habilitado: Boolean = true
) {
    var abierto by remember { mutableStateOf(false) }
    val etiquetaActual = opciones.firstOrNull { it.valor == valorSeleccionado }?.etiqueta ?: valorSeleccionado.toString()

    Box(modifier = modifier) {
        ComponenteFila(
            titulo = titulo,
            icono = icono,
            colorIcono = colorIcono,
            colorTinteIcono = colorTinteIcono,
            idFila = idFila,
            mostrarId = mostrarId,
            valorTexto = etiquetaActual,
            habilitado = habilitado,
            alPulsar = { if (habilitado) abierto = true },
            contenidoFinal = {
                Icon(
                    imageVector = Icons.Filled.UnfoldMore,
                    contentDescription = "Desplegar opciones",
                    tint = ColorAjusteGris.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        MenuDesplegableBoveda(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            opciones.forEachIndexed { index, opcion ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val esSeleccionado = opcion.valor == valorSeleccionado
                DropdownMenuItem(
                    leadingIcon = if (opcion.icono != null) {
                        {
                            Icon(
                                imageVector = opcion.icono,
                                contentDescription = null,
                                tint = if (esSeleccionado) ColorAcento else ColorAjusteGris,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null,
                    text = {
                        Text(
                            text = opcion.etiqueta,
                            color = if (esSeleccionado) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = if (esSeleccionado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null,
                    onClick = {
                        alSeleccionar(opcion.valor)
                        abierto = false
                    }
                )
            }
        }
    }
}
