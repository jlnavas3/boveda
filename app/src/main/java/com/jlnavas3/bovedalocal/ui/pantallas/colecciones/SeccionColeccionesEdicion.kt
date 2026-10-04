package com.jlnavas3.bovedalocal.ui.pantallas.colecciones

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Coleccion
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
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeccionColeccionesEdicion(
    coleccionesDisponibles: List<Coleccion>,
    coleccionesSeleccionadas: List<String>,
    alCambiarColecciones: (List<String>) -> Unit,
    alCrearNuevaColeccion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Colecciones",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "Organiza esta entrada en colecciones personalizadas",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        val formaChip = RoundedCornerShape(10.dp)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            coleccionesDisponibles.forEach { col ->
                val estaSeleccionada = coleccionesSeleccionadas.contains(col.id)
                val colorPropio = IconosColecciones.parsearColorHex(col.colorHex) ?: ColorAcento
                val icono = IconosColecciones.obtenerIcono(col.icono)

                Box(
                    modifier = Modifier
                        .clip(formaChip)
                        .background(if (estaSeleccionada) colorPropio else ColorCampoAjustes)
                        .border(
                            width = if (estaSeleccionada) 1.dp else 0.8.dp,
                            color = if (estaSeleccionada) colorPropio else ColorSeparadorAjustes,
                            shape = formaChip
                        )
                        .clickable {
                            val nuevaLista = if (estaSeleccionada) {
                                coleccionesSeleccionadas - col.id
                            } else {
                                coleccionesSeleccionadas + col.id
                            }
                            alCambiarColecciones(nuevaLista)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (estaSeleccionada) Icons.Filled.Check else icono,
                            contentDescription = null,
                            tint = if (estaSeleccionada) Color.White else colorPropio,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = col.nombre,
                            color = if (estaSeleccionada) Color.White else TextoPrincipal,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (estaSeleccionada) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            // Chip para crear nueva colección directamente
            Box(
                modifier = Modifier
                    .clip(formaChip)
                    .background(ColorTarjetaAjustes)
                    .border(0.8.dp, ColorSeparadorAjustes, formaChip)
                    .clickable { alCrearNuevaColeccion() }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Nueva",
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
}
