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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SelectorCaracteresGenerador(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica,
    habilitado: Boolean = true,
    modifier: Modifier = Modifier
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = FormaCampo

    val activos = buildList {
        if (opciones.mayusculas) add("A-Z")
        if (opciones.minusculas) add("a-z")
        if (opciones.digitos) add("0-9")
        if (opciones.simbolos) add("#$!")
    }

    val textoResumen = when {
        !habilitado -> "No aplica"
        activos.isEmpty() -> "Ninguno"
        activos.size == 4 -> "Todos (4)"
        else -> activos.joinToString(" ")
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(forma)
                .background(if (habilitado) Superficie else Superficie.copy(alpha = 0.5f))
                .then(
                    if (GrosorBorde > 0.dp && (abierto || ColorBordeActual != Color.Transparent))
                        Modifier.border(GrosorBorde, if (abierto) ColorTitulos else ColorBordeActual, forma)
                    else Modifier
                )
                .clickable(enabled = habilitado) { abierto = true }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Tune,
                contentDescription = null,
                tint = when {
                    !habilitado -> TextoSecundario.copy(alpha = 0.4f)
                    abierto -> ColorIconosInternos
                    else -> TextoSecundario
                },
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Caracteres",
                    color = if (abierto) ColorTitulos else TextoSecundario,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = textoResumen,
                    color = if (habilitado) TextoPrincipal else TextoSecundario.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir selector de caracteres",
                tint = if (habilitado) TextoSecundario else TextoSecundario.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }

        MenuDesplegableBoveda(
            expanded = abierto && habilitado,
            onDismissRequest = { abierto = false }
        ) {
            // 1. Mayúsculas (A-Z)
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.TextFields,
                        contentDescription = null,
                        tint = if (opciones.mayusculas) ColorIconosInternos else TextoSecundario,
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Mayúsculas (A-Z)",
                            color = if (opciones.mayusculas) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (opciones.mayusculas) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                        Text(
                            text = "A, B, C... Z",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                trailingIcon = {
                    Switch(
                        checked = opciones.mayusculas,
                        onCheckedChange = { nuevo ->
                            haptica.tic()
                            alCambiarOpciones(opciones.copy(mayusculas = nuevo))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ColorSobreAcento,
                            checkedTrackColor = Ambar,
                            checkedBorderColor = Ambar,
                            uncheckedThumbColor = TextoSecundario,
                            uncheckedTrackColor = SuperficieAlta,
                            uncheckedBorderColor = TextoSecundario
                        )
                    )
                },
                onClick = {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(mayusculas = !opciones.mayusculas))
                }
            )

            SeparadorOpcionMenu()

            // 2. Minúsculas (a-z)
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.TextFields,
                        contentDescription = null,
                        tint = if (opciones.minusculas) ColorIconosInternos else TextoSecundario,
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Minúsculas (a-z)",
                            color = if (opciones.minusculas) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (opciones.minusculas) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                        Text(
                            text = "a, b, c... z",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                trailingIcon = {
                    Switch(
                        checked = opciones.minusculas,
                        onCheckedChange = { nuevo ->
                            haptica.tic()
                            alCambiarOpciones(opciones.copy(minusculas = nuevo))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ColorSobreAcento,
                            checkedTrackColor = Ambar,
                            checkedBorderColor = Ambar,
                            uncheckedThumbColor = TextoSecundario,
                            uncheckedTrackColor = SuperficieAlta,
                            uncheckedBorderColor = TextoSecundario
                        )
                    )
                },
                onClick = {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(minusculas = !opciones.minusculas))
                }
            )

            SeparadorOpcionMenu()

            // 3. Números (0-9)
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Numbers,
                        contentDescription = null,
                        tint = if (opciones.digitos) ColorIconosInternos else TextoSecundario,
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Números (0-9)",
                            color = if (opciones.digitos) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (opciones.digitos) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                        Text(
                            text = "0, 1, 2... 9",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                trailingIcon = {
                    Switch(
                        checked = opciones.digitos,
                        onCheckedChange = { nuevo ->
                            haptica.tic()
                            alCambiarOpciones(opciones.copy(digitos = nuevo))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ColorSobreAcento,
                            checkedTrackColor = Ambar,
                            checkedBorderColor = Ambar,
                            uncheckedThumbColor = TextoSecundario,
                            uncheckedTrackColor = SuperficieAlta,
                            uncheckedBorderColor = TextoSecundario
                        )
                    )
                },
                onClick = {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(digitos = !opciones.digitos))
                }
            )

            SeparadorOpcionMenu()

            // 4. Símbolos (#$!)
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Tag,
                        contentDescription = null,
                        tint = if (opciones.simbolos) ColorIconosInternos else TextoSecundario,
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Símbolos (#$!)",
                            color = if (opciones.simbolos) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (opciones.simbolos) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                        Text(
                            text = "! @ # $ % & * - _ + =",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                trailingIcon = {
                    Switch(
                        checked = opciones.simbolos,
                        onCheckedChange = { nuevo ->
                            haptica.tic()
                            alCambiarOpciones(opciones.copy(simbolos = nuevo))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ColorSobreAcento,
                            checkedTrackColor = Ambar,
                            checkedBorderColor = Ambar,
                            uncheckedThumbColor = TextoSecundario,
                            uncheckedTrackColor = SuperficieAlta,
                            uncheckedBorderColor = TextoSecundario
                        )
                    )
                },
                onClick = {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(simbolos = !opciones.simbolos))
                }
            )
        }
    }
}
