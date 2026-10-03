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
import com.jlnavas3.bovedalocal.ui.componentes.FilaOpcionModal
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

@Composable
fun SelectorModoGenerador(
    modoActual: String,
    alSeleccionarModo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var abierto by remember { mutableStateOf(false) }
    val fondo = ColorCampoAjustes

    val icono = when (modoActual) {
        "Frase Diceware" -> Icons.AutoMirrored.Filled.MenuBook
        "Por Patrón" -> Icons.Filled.Pattern
        else -> Icons.Filled.Shuffle
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(FormaCampo)
                .background(fondo)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                    } else Modifier
                )
                .clickable { abierto = true }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icono,
                contentDescription = null,
                tint = if (abierto) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Modo de generación",
                    color = if (abierto) ColorTitulos else TextoSecundario,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    modoActual,
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
            Icon(
                if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir modos de generación",
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
        }

        if (abierto) {
            val opcionesModo = listOf(
                Triple("Aleatoria", "Caracteres alfanuméricos y símbolos", Icons.Filled.Shuffle),
                Triple("Frase Diceware", "Palabras memorables de alta entropía", Icons.AutoMirrored.Filled.MenuBook),
                Triple("Por Patrón", "Estructura y plantillas personalizadas", Icons.Filled.Pattern)
            )

            ModalInferiorBoveda(
                abierto = abierto,
                alCerrar = { abierto = false },
                titulo = "Modo de generación",
                descripcion = "Elige la estrategia para forjar la clave",
                icono = icono,
                colorIcono = Color.White,
                fondoIcono = ColorGenerador,
                mostrarBotonCerrar = false
            ) {
                Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)) {
                    opcionesModo.forEach { (modo, desc, ic) ->
                        val seleccionado = modoActual == modo
                        FilaOpcionModal(
                            titulo = modo,
                            descripcion = desc,
                            icono = ic,
                            seleccionado = seleccionado,
                            colorAcento = ColorGenerador,
                            alPulsar = {
                                alSeleccionarModo(modo)
                                abierto = false
                            },
                            controlFinal = if (seleccionado) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = ColorGenerador,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } else null
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
                            text = "Cancelar",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}
