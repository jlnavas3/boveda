package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.jlnavas3.bovedalocal.ui.componentes.FilaOpcionModal
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
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
    val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
    val fondo = if (esOscuro) Color(0xFF161518) else Color(0xFFF4F4F6)

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
                .height(42.dp)
                .clip(FormaCampo)
                .background(if (habilitado) fondo else fondo.copy(alpha = 0.5f))
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                    } else Modifier
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
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Caracteres",
                    color = if (abierto) ColorTitulos else TextoSecundario,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = textoResumen,
                    color = if (habilitado) TextoPrincipal else TextoSecundario.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir selector de caracteres",
                tint = if (habilitado) TextoSecundario else TextoSecundario.copy(alpha = 0.4f),
                modifier = Modifier.size(18.dp)
            )
        }

        if (abierto && habilitado) {
            val opcionesLista = listOf(
                Triple("Mayúsculas (A-Z)", "A, B, C... Z", opciones.mayusculas) to {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(mayusculas = !opciones.mayusculas))
                },
                Triple("Minúsculas (a-z)", "a, b, c... z", opciones.minusculas) to {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(minusculas = !opciones.minusculas))
                },
                Triple("Números (0-9)", "0, 1, 2... 9", opciones.digitos) to {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(digitos = !opciones.digitos))
                },
                Triple(
                    "Símbolos (#$!)",
                    if (opciones.simbolosPersonalizados.isNotBlank()) opciones.simbolosPersonalizados else "! @ # $ % & * - _ + =",
                    opciones.simbolos
                ) to {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(simbolos = !opciones.simbolos))
                }
            )

            ModalInferiorBoveda(
                abierto = abierto,
                alCerrar = { abierto = false },
                titulo = "Caracteres permitidos",
                descripcion = "Selecciona qué conjuntos incluir en la clave",
                icono = Icons.Filled.Tune,
                colorIcono = Color.White,
                fondoIcono = ColorGenerador,
                mostrarBotonCerrar = false
            ) {
                Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)) {
                    opcionesLista.forEach { (datos, alTocar) ->
                        val (nombre, desc, activo) = datos
                        FilaOpcionModal(
                            titulo = nombre,
                            descripcion = desc,
                            seleccionado = activo,
                            colorAcento = ColorGenerador,
                            alPulsar = { alTocar() },
                            controlFinal = {
                                SwitchBoveda(
                                    checked = activo,
                                    onCheckedChange = { alTocar() }
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
                ) {
                    androidx.compose.material3.TextButton(
                        onClick = { abierto = false }
                    ) {
                        Text(
                            text = "Listo",
                            color = ColorGenerador,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
