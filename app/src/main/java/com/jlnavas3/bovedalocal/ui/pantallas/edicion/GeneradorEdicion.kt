package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorCaracteresGenerador
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun OpcionGeneradorCompacta(
    texto: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(78.dp)) {
        Text(texto, color = TextoSecundario, style = MaterialTheme.typography.labelSmall, maxLines = 1)
        Switch(
            checked = activo,
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
fun SelectorModoEdicion(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    modifier: Modifier = Modifier
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = FormaBoton

    val (icono, titulo) = when {
        opciones.modoFrase -> Icons.AutoMirrored.Filled.MenuBook to "Diceware"
        opciones.modoPatron -> Icons.Filled.Pattern to "Por patrón"
        else -> Icons.Filled.Shuffle to "Aleatoria"
    }

    Box(modifier = modifier) {
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
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (abierto) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Desplegar modos",
                tint = TextoSecundario,
                modifier = Modifier.size(20.dp)
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
                    text = {
                        Text(
                            text = nombre,
                            color = if (seleccionado) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    trailingIcon = if (seleccionado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null,
                    onClick = {
                        alCambiarOpciones(fnCambio())
                        abierto = false
                    }
                )
            }
        }
    }
}

@Composable
fun GeneradorEnLineaEdicion(
    opcionesGenerador: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    alGenerarContrasena: (String) -> Unit,
    haptica: Haptica
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BotonAmbar(
            texto = "Generar",
            icono = Icons.Filled.AutoAwesome,
            modifier = Modifier.weight(1f)
        ) {
            haptica.toque()
            alGenerarContrasena(PasswordGenerator.generar(opcionesGenerador))
        }
        SelectorModoEdicion(
            opciones = opcionesGenerador,
            alCambiarOpciones = {
                haptica.tic()
                alCambiarOpciones(it)
            },
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(Modifier.height(10.dp))

    when {
        opcionesGenerador.modoFrase -> {
            EtiquetaSeccion("Palabras Diceware: ${opcionesGenerador.palabras}")
            Slider(
                value = opcionesGenerador.palabras.toFloat(),
                onValueChange = {
                    val nuevo = it.roundToInt().coerceIn(3, 12)
                    if (nuevo != opcionesGenerador.palabras) {
                        haptica.tic()
                        alCambiarOpciones(opcionesGenerador.copy(palabras = nuevo))
                    }
                },
                valueRange = 3f..12f,
                steps = 8,
                colors = SliderDefaults.colors(
                    thumbColor = Ambar,
                    activeTrackColor = Ambar,
                    inactiveTrackColor = Borde
                )
            )
        }
        opcionesGenerador.modoPatron -> {
            CampoBoveda(
                valor = opcionesGenerador.patron,
                etiqueta = "Patrón (ej. XXXXX-XXXXX-XXXXX-XXXXX-XXXXX)",
                alCambiar = { alCambiarOpciones(opcionesGenerador.copy(patron = it)) },
                monoespaciada = true
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "X: alfanum | A: mayús | a: minús | 9: dígito | w: palabra Diceware",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 50% Slider de caracteres
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Longitud",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "${opcionesGenerador.longitud} car.",
                            color = ColorTitulos,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Slider(
                        value = opcionesGenerador.longitud.toFloat(),
                        onValueChange = {
                            val nuevo = it.roundToInt().coerceIn(8, 64)
                            if (nuevo != opcionesGenerador.longitud) {
                                haptica.tic()
                                alCambiarOpciones(opcionesGenerador.copy(longitud = nuevo))
                            }
                        },
                        valueRange = 8f..64f,
                        colors = SliderDefaults.colors(
                            thumbColor = Ambar,
                            activeTrackColor = Ambar,
                            inactiveTrackColor = Borde
                        )
                    )
                }

                // 50% Menú desplegable con todos los switches (A-Z, a-z, 0-9, #$!)
                SelectorCaracteresGenerador(
                    opciones = opcionesGenerador,
                    alCambiarOpciones = {
                        haptica.tic()
                        alCambiarOpciones(it)
                    },
                    haptica = haptica,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
