package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex

@Composable
fun SeccionDisenoEngranajes(
    partesEngranajes: List<InfoParteEngranaje>,
    parteEngranajeElegida: Int,
    alSeleccionarParte: (Int) -> Unit,
    mostrarIds: Boolean
) {
    var dropdownPartesEngranajeAbierto by remember { mutableStateOf(false) }
    val parteActual = partesEngranajes[parteEngranajeElegida.coerceIn(0, partesEngranajes.size - 1)]

    ComponenteGrupo(
        etiqueta = "Colores de las piezas",
        descripcion = "Personaliza la tonalidad cromática de cada elemento del engranaje",
        mostrarId = mostrarIds
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaCampo)
                        .background(Superficie)
                        .then(
                            if (GrosorBorde > 0.dp) {
                                Modifier.border(
                                    GrosorBorde,
                                    if (dropdownPartesEngranajeAbierto) ColorTitulos else ColorBordeActual,
                                    FormaCampo
                                )
                            } else {
                                Modifier
                            }
                        )
                        .clickable { dropdownPartesEngranajeAbierto = true }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(parteActual.colorActual)
                            .border(1.dp, TextoPrincipal.copy(alpha = 0.3f), CircleShape)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = parteActual.nombre,
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = parteActual.descripcion,
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = if (dropdownPartesEngranajeAbierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = TextoSecundario
                    )
                }

                MenuDesplegableBoveda(
                    expanded = dropdownPartesEngranajeAbierto,
                    onDismissRequest = { dropdownPartesEngranajeAbierto = false }
                ) {
                    partesEngranajes.forEachIndexed { idx, p ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(p.colorActual)
                                            .border(1.dp, TextoPrincipal.copy(alpha = 0.3f), CircleShape)
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Column {
                                        Text(p.nombre, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                                        Text(p.descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            },
                            onClick = {
                                alSeleccionarParte(idx)
                                dropdownPartesEngranajeAbierto = false
                            }
                        )
                        if (idx < partesEngranajes.size - 1) SeparadorOpcionMenu()
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            Text(
                text = "Color actual: ${parteActual.colorActual.aHex()}",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))

            SelectorColorEnTiempoReal(
                colorInicial = parteActual.colorActual,
                titulo = parteActual.nombre
            ) { nuevoColor ->
                parteActual.mutador(nuevoColor)
            }
        }

        ComponenteSeparador()
        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                parteActual.mutador(parteActual.colorPorDefecto)
            }
        )
    }
}
