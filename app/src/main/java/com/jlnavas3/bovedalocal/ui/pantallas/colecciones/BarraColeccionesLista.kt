package com.jlnavas3.bovedalocal.ui.pantallas.colecciones

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Layers
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BarraColeccionesLista(
    colecciones: List<Coleccion>,
    coleccionSeleccionadaId: String?,
    totalEntradas: Int,
    conteoPorColeccion: Map<String, Int>,
    alSeleccionarColeccion: (String?) -> Unit,
    alCrearColeccion: () -> Unit,
    alEditarColeccion: (Coleccion) -> Unit,
    alEliminarColeccion: (Coleccion) -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var menuColeccionAbierto by remember { mutableStateOf<Coleccion?>(null) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Chip "Todas"
        val esTodas = coleccionSeleccionadaId == null
        val formaChip = RoundedCornerShape(12.dp)
        Box(
            modifier = Modifier
                .clip(formaChip)
                .background(if (esTodas) ColorAcento else ColorTarjetaAjustes)
                .then(
                    if (esTodas) {
                        Modifier.border(0.8.dp, ColorAcento, formaChip)
                    } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                    } else {
                        Modifier.border(0.8.dp, ColorSeparadorAjustes, formaChip)
                    }
                )
                .combinedClickable(
                    onClick = {
                        haptica.toque()
                        alSeleccionarColeccion(null)
                    }
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Layers,
                    contentDescription = null,
                    tint = if (esTodas) ColorSobreAcento else ColorAcento,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "Todas ($totalEntradas)",
                    color = if (esTodas) ColorSobreAcento else TextoPrincipal,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (esTodas) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                )
            }
        }

        // Chips por cada colección
        colecciones.forEach { col ->
            val estaSeleccionada = coleccionSeleccionadaId == col.id
            val colorPropio = IconosColecciones.parsearColorHex(col.colorHex) ?: ColorAcento
            val icono = IconosColecciones.obtenerIcono(col.icono)
            val cantidad = conteoPorColeccion[col.id] ?: 0

            Box(
                modifier = Modifier
                    .clip(formaChip)
                    .background(if (estaSeleccionada) colorPropio else ColorTarjetaAjustes)
                    .then(
                        if (estaSeleccionada) {
                            Modifier.border(0.8.dp, colorPropio, formaChip)
                        } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                            Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                        } else {
                            Modifier.border(0.8.dp, ColorSeparadorAjustes, formaChip)
                        }
                    )
                    .combinedClickable(
                        onClick = {
                            haptica.toque()
                            if (estaSeleccionada) {
                                alSeleccionarColeccion(null)
                            } else {
                                alSeleccionarColeccion(col.id)
                            }
                        },
                        onLongClick = {
                            haptica.toque()
                            menuColeccionAbierto = col
                        }
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = if (estaSeleccionada) Color.White else colorPropio,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${col.nombre} ($cantidad)",
                        color = if (estaSeleccionada) Color.White else TextoPrincipal,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (estaSeleccionada) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }

                // Menú contextual para la colección al pulsar prolongado
                if (menuColeccionAbierto?.id == col.id) {
                    MenuDesplegableBoveda(
                        expanded = true,
                        onDismissRequest = { menuColeccionAbierto = null },
                        modifier = Modifier.widthIn(min = 180.dp)
                    ) {
                        ElementoMenuCompacto(
                            texto = "Editar colección",
                            icono = Icons.Filled.Edit,
                            colorIcono = colorPropio,
                            onClick = {
                                menuColeccionAbierto = null
                                alEditarColeccion(col)
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Eliminar colección",
                            icono = Icons.Filled.Delete,
                            colorIcono = Peligro,
                            onClick = {
                                menuColeccionAbierto = null
                                alEliminarColeccion(col)
                            }
                        )
                    }
                }
            }
        }

        // Chip "+" para crear nueva colección
        Box(
            modifier = Modifier
                .clip(formaChip)
                .background(ColorCampoAjustes)
                .border(0.8.dp, ColorSeparadorAjustes, formaChip)
                .combinedClickable(
                    onClick = {
                        haptica.toque()
                        alCrearColeccion()
                    }
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nueva colección",
                    tint = ColorAcento,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "Nueva",
                    color = ColorAcento,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}
