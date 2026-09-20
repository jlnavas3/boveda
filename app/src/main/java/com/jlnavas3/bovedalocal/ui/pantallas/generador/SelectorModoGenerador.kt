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
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

@Composable
fun SelectorModoGenerador(
    modoActual: String,
    alSeleccionarModo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(12.dp)
    val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
    val fondo = if (esOscuro) Color(0xFF161518) else Color(0xFFF4F4F6)
    val borde = if (esOscuro) Color(0xFF333238) else Color(0xFFDFDFE3)

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
                .clip(forma)
                .background(fondo)
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
            val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White
            androidx.compose.ui.window.Dialog(
                onDismissRequest = { abierto = false },
                properties = androidx.compose.ui.window.DialogProperties(
                    usePlatformDefaultWidth = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null
                        ) { abierto = false },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(26.dp))
                            .background(fondoModal)
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null
                            ) { /* Evita cerrar */ }
                            .padding(horizontal = 20.dp, vertical = 22.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Cabecera del modal
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(com.jlnavas3.bovedalocal.ui.theme.ColorGenerador),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icono,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Modo de generación",
                                        color = TextoPrincipal,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.5.sp
                                        )
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "Elige la estrategia para forjar la clave",
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(16.dp))

                            val opcionesModo = listOf(
                                Triple("Aleatoria", "Caracteres alfanuméricos y símbolos", Icons.Filled.Shuffle),
                                Triple("Frase Diceware", "Palabras memorables de alta entropía", Icons.AutoMirrored.Filled.MenuBook),
                                Triple("Por Patrón", "Estructura y plantillas personalizadas", Icons.Filled.Pattern)
                            )

                            Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)) {
                                opcionesModo.forEach { (modo, desc, ic) ->
                                    val seleccionado = modoActual == modo
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                if (seleccionado) com.jlnavas3.bovedalocal.ui.theme.ColorGenerador.copy(alpha = 0.12f)
                                                else Color.Transparent
                                            )
                                            .clickable {
                                                alSeleccionarModo(modo)
                                                abierto = false
                                            }
                                            .padding(horizontal = 14.dp, vertical = 11.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = ic,
                                            contentDescription = null,
                                            tint = if (seleccionado) com.jlnavas3.bovedalocal.ui.theme.ColorGenerador else TextoSecundario,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = modo,
                                                color = if (seleccionado) com.jlnavas3.bovedalocal.ui.theme.ColorGenerador else TextoPrincipal,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal,
                                                    fontSize = 15.sp
                                                )
                                            )
                                            Spacer(Modifier.height(2.dp))
                                            Text(
                                                text = desc,
                                                color = TextoSecundario,
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                            )
                                        }
                                        if (seleccionado) {
                                            Spacer(Modifier.width(12.dp))
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                tint = com.jlnavas3.bovedalocal.ui.theme.ColorGenerador,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
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
        }
    }
}
