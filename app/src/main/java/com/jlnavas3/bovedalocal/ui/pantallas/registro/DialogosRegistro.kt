package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun ModalCategoriasRegistro(
    categoriasOpciones: List<CategoriaOpcion>,
    categoriaSeleccionada: String,
    alSeleccionarCategoria: (String) -> Unit,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val esOscuro = isSystemInDarkTheme()
    val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White

    Dialog(
        onDismissRequest = alCerrar,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { alCerrar() },
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
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Evita cerrar al pulsar dentro */ }
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorAcento),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Filtrar por categoría",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.5.sp
                                )
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Muestra solo los eventos del área seleccionada",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        categoriasOpciones.forEach { opcion ->
                            val esSeleccionado = opcion.nombre == categoriaSeleccionada

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (esSeleccionado) opcion.color.copy(alpha = 0.12f)
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        haptica.tic()
                                        alSeleccionarCategoria(opcion.nombre)
                                        alCerrar()
                                    }
                                    .padding(horizontal = 14.dp, vertical = 11.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = opcion.icono,
                                    contentDescription = null,
                                    tint = if (esSeleccionado) opcion.color else TextoSecundario,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = opcion.nombre,
                                        color = if (esSeleccionado) opcion.color else TextoPrincipal,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (esSeleccionado) FontWeight.SemiBold else FontWeight.Normal,
                                            fontSize = 15.sp
                                        )
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = opcion.descripcion,
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = if (esSeleccionado) 6.dp else 1.5.dp,
                                            color = if (esSeleccionado) opcion.color else ColorAjusteGris.copy(alpha = 0.45f),
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = alCerrar
                        ) {
                            Text(
                                text = "Cerrar",
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

@Composable
fun DialogoOrdenacionRegistro(
    criterioActual: CriterioOrdenRegistro,
    alSeleccionarCriterio: (CriterioOrdenRegistro) -> Unit,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    AlertDialog(
        onDismissRequest = alCerrar,
        containerColor = ColorTarjetaAjustes,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 0.dp,
        title = {
            Text(
                "Ordenar por",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CriterioOrdenRegistro.entries.forEach { criterio ->
                    val seleccionado = criterio == criterioActual
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (seleccionado) ColorAcento.copy(alpha = 0.12f) else Color.Transparent)
                            .clickable {
                                haptica.tic()
                                alSeleccionarCriterio(criterio)
                                alCerrar()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            tint = if (seleccionado) ColorAcento else ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = criterio.etiqueta,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = if (seleccionado) ColorAcento else TextoPrincipal,
                            modifier = Modifier.weight(1f)
                        )
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = ColorAcento)
            }
        }
    )
}
