package com.jlnavas3.bovedalocal.ui.pantallas.colecciones

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoAsignarColecciones(
    entradasSeleccionadas: List<Entrada>,
    coleccionesDisponibles: List<Coleccion>,
    alCrearNuevaColeccion: () -> Unit,
    alGuardar: (idsAgregar: Set<String>, idsQuitar: Set<String>) -> Unit,
    alDescartar: () -> Unit
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    // Calculamos qué colecciones tienen asignadas actualmente estas entradas
    val coleccionesIniciales = remember(entradasSeleccionadas) {
        val mapa = mutableSetOf<String>()
        entradasSeleccionadas.forEach { entrada ->
            mapa.addAll(entrada.colecciones)
        }
        mapa
    }

    var seleccionadas by remember { mutableStateOf(coleccionesIniciales.toSet()) }

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = forma,
        containerColor = ColorTarjetaAjustes,
        modifier = Modifier.then(
            if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                Modifier.border(GrosorBorde, ColorBordeActual, forma)
            } else Modifier
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(FormaPequena)
                        .background(ColorAcento.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Folder,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Colecciones",
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    val cant = entradasSeleccionadas.size
                    Text(
                        text = if (cant == 1) "1 entrada seleccionada" else "$cant entradas seleccionadas",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (coleccionesDisponibles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aún no tienes colecciones creadas.",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(coleccionesDisponibles, key = { it.id }) { col ->
                            val estaMarcada = seleccionadas.contains(col.id)
                            val colorPropio = IconosColecciones.parsearColorHex(col.colorHex) ?: ColorAcento
                            val icono = IconosColecciones.obtenerIcono(col.icono)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(FormaPequena)
                                    .background(if (estaMarcada) ColorCampoAjustes else Color.Transparent)
                                    .clickable {
                                        seleccionadas = if (estaMarcada) {
                                            seleccionadas - col.id
                                        } else {
                                            seleccionadas + col.id
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = estaMarcada,
                                    onCheckedChange = { chk ->
                                        seleccionadas = if (chk) {
                                            seleccionadas + col.id
                                        } else {
                                            seleccionadas - col.id
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = ColorAcento,
                                        uncheckedColor = ColorSeparadorAjustes
                                    ),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Icon(
                                    imageVector = icono,
                                    contentDescription = null,
                                    tint = colorPropio,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = col.nombre,
                                    color = TextoPrincipal,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (estaMarcada) FontWeight.SemiBold else FontWeight.Normal
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Botón "+ Nueva colección"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaPequena)
                        .background(ColorCampoAjustes)
                        .border(0.8.dp, ColorSeparadorAjustes, FormaPequena)
                        .clickable { alCrearNuevaColeccion() }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Crear nueva colección",
                        color = ColorAcento,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val idsAgregar = seleccionadas - coleccionesIniciales
                    val idsQuitar = coleccionesIniciales - seleccionadas
                    alGuardar(idsAgregar, idsQuitar)
                }
            ) {
                Text("Guardar", color = ColorAcento, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    )
}
