package com.jlnavas3.bovedalocal.ui.pantallas.menulateral

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.NodoAjuste
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono

/**
 * Diálogo modal para explorar el catálogo de opciones y añadir un acceso directo a la barra lateral.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoAnadirItemMenuLateral(
    itemsDisponibles: List<NodoAjuste>,
    mostrarIds: Boolean,
    alSeleccionar: (NodoAjuste) -> Unit,
    alCerrar: () -> Unit
) {
    var filtro by remember { mutableStateOf("") }
    val filtrados = remember(filtro, itemsDisponibles) {
        val q = filtro.trim().lowercase()
        if (q.isBlank()) itemsDisponibles
        else {
            val palabras = q.split("\\s+".toRegex()).filter { it.isNotBlank() }
            itemsDisponibles.filter { nodo ->
                val corpus = "${nodo.titulo} ${nodo.subtitulo} ${nodo.id} ${nodo.grupo}".lowercase()
                palabras.all { corpus.contains(it) }
            }
        }
    }

    BasicAlertDialog(
        onDismissRequest = alCerrar
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ColorTarjetaAjustes,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Añadir a la barra lateral",
                    color = ColorTextoAjustes,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = filtro,
                    onValueChange = { filtro = it },
                    placeholder = { Text("Buscar pantallas y opciones...", color = ColorAjusteGris, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = ColorAjusteGris, modifier = Modifier.size(18.dp))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorAcento,
                        unfocusedBorderColor = ColorAjusteGris.copy(alpha = 0.3f),
                        focusedTextColor = ColorTextoAjustes,
                        unfocusedTextColor = ColorTextoAjustes
                    ),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                ) {
                    items(filtrados, key = { it.id }) { nodo ->
                        val (icono, color) = MapaAjustes.resolverIconoYColor(nodo)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    alSeleccionar(nodo)
                                    alCerrar()
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icono,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = nodo.titulo,
                                    color = ColorTextoAjustes,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                )
                                if (nodo.subtitulo.isNotBlank()) {
                                    Text(
                                        text = nodo.subtitulo,
                                        color = ColorAjusteGris,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                }
                                if (mostrarIds) {
                                    Text(
                                        text = nodo.id,
                                        color = ColorAcento,
                                        style = EstiloMono.copy(fontSize = 10.sp)
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Añadir",
                                tint = ColorAcento,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        ComponenteSeparador()
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = alCerrar) {
                        Text("Cerrar", color = ColorAjusteGris)
                    }
                }
            }
        }
    }
}
