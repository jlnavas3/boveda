/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonPrimario
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.barraDesplazamientoVertical
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorModalAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.bottomsheets.CampoBusquedaBottomSheet
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Bottom sheet modal reutilizable para vincular o desvincular entradas (tarjetas, cuentas o contactos)
 * a un perfil de identidad con selección múltiple ágil y opción de creación directa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetVincularEntradaPerfil(
    visible: Boolean,
    titulo: String,
    icono: ImageVector,
    colorAcento: Color = ColorAcento,
    entradasDisponibles: List<Entrada>,
    idsSeleccionados: List<String>,
    alCambiarSeleccion: (List<String>) -> Unit,
    alCrearNuevaEntrada: () -> Unit,
    alCerrar: () -> Unit
) {
    if (!visible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val configuracion = LocalConfiguration.current
    val alturaMaxima = (configuracion.screenHeightDp * 0.70f).dp
    val formaSheet = RoundedCornerShape(topStart = CurvaturaEsquinas, topEnd = CurvaturaEsquinas)

    var busqueda by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()

    val entradasFiltradas = remember(entradasDisponibles, busqueda) {
        val q = busqueda.trim().lowercase()
        if (q.isBlank()) entradasDisponibles else {
            entradasDisponibles.filter { ent ->
                ent.titulo.lowercase().contains(q) ||
                    ent.usuario.lowercase().contains(q) ||
                    ent.datosTarjeta?.numero?.contains(q) == true ||
                    ent.datosCuentaBancaria?.banco?.lowercase()?.contains(q) == true ||
                    ent.datosCuentaBancaria?.numeroCuenta?.contains(q) == true ||
                    ent.identidadEfectiva?.numero?.contains(q) == true ||
                    ent.contactoEfectivo?.ciudad?.lowercase()?.contains(q) == true
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = alCerrar,
        sheetState = sheetState,
        shape = formaSheet,
        containerColor = ColorModalAjustes,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = alturaMaxima)
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            // Cabecera: Icono + Título + Botón de crear nueva entrada
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = titulo,
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                IconButton(
                    onClick = {
                        alCerrar()
                        alCrearNuevaEntrada()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Crear nueva",
                        tint = colorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Buscador si hay más de 3 entradas
            if (entradasDisponibles.size > 3) {
                CampoBusquedaBottomSheet(
                    valor = busqueda,
                    alCambiar = { busqueda = it },
                    pista = "Buscar..."
                )
                Spacer(Modifier.height(10.dp))
            }

            if (entradasDisponibles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes elementos de este tipo en tu bóveda.",
                        color = TextoSecundario,
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .barraDesplazamientoVertical(
                            state = lazyListState,
                            ancho = 3.dp,
                            margenFin = 1.dp
                        ),
                    contentPadding = PaddingValues(end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(entradasFiltradas, key = { it.id }) { ent ->
                        val seleccionada = idsSeleccionados.contains(ent.id)
                        val forma = RoundedCornerShape(CurvaturaEsquinas)

                        val subtitulo = when (ent.tipo) {
                            TipoEntrada.TARJETA -> {
                                val num = ent.datosTarjeta?.numero.orEmpty()
                                val ultimos = if (num.length >= 4) "•••• ${num.takeLast(4)}" else num
                                val vence = ent.datosTarjeta?.vencimiento?.ifBlank { null }
                                if (vence != null) "$ultimos (Vence: $vence)" else ultimos
                            }
                            TipoEntrada.CUENTA_BANCARIA -> {
                                val banco = ent.datosCuentaBancaria?.banco.orEmpty()
                                val num = ent.datosCuentaBancaria?.numeroCuenta.orEmpty()
                                val tipo = ent.datosCuentaBancaria?.tipoCuenta.orEmpty()
                                listOf(banco, tipo, num).filter { it.isNotBlank() }.joinToString(" • ")
                            }
                            TipoEntrada.IDENTIDAD -> {
                                val tipo = ent.identidadEfectiva?.tipoDocumento.orEmpty()
                                val num = ent.identidadEfectiva?.numero.orEmpty().ifBlank { ent.usuario }
                                if (tipo.isNotBlank() && num.isNotBlank()) "$tipo • $num" else num
                            }
                            TipoEntrada.CONTACTO -> {
                                val tel = ent.usuario
                                val ciudad = ent.contactoEfectivo?.ciudad.orEmpty()
                                listOf(tel, ciudad).filter { it.isNotBlank() }.joinToString(" • ")
                            }
                            else -> ent.usuario.ifBlank { ent.notas.take(40) }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(forma)
                                .background(if (seleccionada) colorAcento.copy(alpha = 0.12f) else ColorTarjetaAjustes)
                                .clickable {
                                    val nuevaLista = if (seleccionada) {
                                        idsSeleccionados - ent.id
                                    } else {
                                        idsSeleccionados + ent.id
                                    }
                                    alCambiarSeleccion(nuevaLista)
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ContenedorIconoInsignia(
                                icono = icono,
                                tamano = TamanoInsignia.PEQUENO,
                                colorFondo = colorAcento.copy(alpha = 0.16f),
                                colorIcono = colorAcento
                            )

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = ent.titulo.ifBlank { "Sin título" },
                                    color = TextoPrincipal,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (subtitulo.isNotBlank()) {
                                    Text(
                                        text = subtitulo,
                                        color = TextoSecundario,
                                        fontSize = 11.5.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(Modifier.width(8.dp))

                            // Checkbox visual redondeado
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(if (seleccionada) colorAcento else Color.Transparent)
                                    .border(
                                        width = 1.5.dp,
                                        color = if (seleccionada) colorAcento else TextoSecundario.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (seleccionada) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            BotonPrimario(
                texto = "Listo",
                alPulsar = alCerrar,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
