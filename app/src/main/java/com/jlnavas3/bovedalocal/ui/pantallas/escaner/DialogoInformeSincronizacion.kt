/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Update
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
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.sync.InformeSincronizacion
import com.jlnavas3.bovedalocal.sync.ItemCambioSync
import com.jlnavas3.bovedalocal.sync.TipoCambioSync
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorSegmentosTarjeta
import com.jlnavas3.bovedalocal.ui.pantallas.lista.recordarSegmentosDiff
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.EstiloIndicadores
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Filtro de categorías para la vista previa de cambios de sincronización.
 */
private enum class FiltroCambiosSync {
    TODOS,
    NUEVAS,
    ACTUALIZADAS,
    PAPELERA
}

/**
 * Diálogo modal para inspeccionar el informe detallado de la sincronización P2P.
 *
 * Muestra contadores agregados por tipo de cambio y un desglose entrada por entrada
 * sin exponer ningún secreto o texto sensible, iluminando únicamente los indicadores
 * visuales de los campos que fueron modificados o agregados.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialogoInformeSincronizacion(
    informe: InformeSincronizacion,
    alCerrar: () -> Unit
) {
    var filtroSeleccionado by remember { mutableStateOf(FiltroCambiosSync.TODOS) }

    val todosLosItems = remember(informe) {
        informe.nuevas + informe.actualizadas + informe.papelera
    }

    val itemsFiltrados = remember(filtroSeleccionado, informe) {
        when (filtroSeleccionado) {
            FiltroCambiosSync.TODOS -> todosLosItems
            FiltroCambiosSync.NUEVAS -> informe.nuevas
            FiltroCambiosSync.ACTUALIZADAS -> informe.actualizadas
            FiltroCambiosSync.PAPELERA -> informe.papelera
        }
    }

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Informe de Sincronización",
        icono = if (informe.hayCambios) Icons.Filled.Sync else Icons.Filled.CheckCircle,
        colorIcono = Menta,
        botonConfirmar = {
            BotonColorido(
                texto = "Entendido",
                icono = Icons.Filled.CheckCircle,
                color = ColorAcento,
                alPulsar = alCerrar
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Subtítulo con identidad del dispositivo remoto sincronizado
            if (!informe.dispositivoRemoto.isNullOrBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaCampo)
                        .background(ColorCampoAjustes)
                        .border(0.8.dp, ColorBordeActual, FormaCampo)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Devices,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Sincronizado con: ${informe.dispositivoRemoto}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Fila de chips de contadores interactivos
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ChipContadorSync(
                    etiqueta = "Todas",
                    cantidad = todosLosItems.size,
                    color = ColorAcento,
                    seleccionado = filtroSeleccionado == FiltroCambiosSync.TODOS,
                    alPulsar = { filtroSeleccionado = FiltroCambiosSync.TODOS }
                )
                if (informe.nuevas.isNotEmpty()) {
                    ChipContadorSync(
                        etiqueta = "Nuevas",
                        cantidad = informe.nuevas.size,
                        color = Menta,
                        seleccionado = filtroSeleccionado == FiltroCambiosSync.NUEVAS,
                        alPulsar = { filtroSeleccionado = FiltroCambiosSync.NUEVAS }
                    )
                }
                if (informe.actualizadas.isNotEmpty()) {
                    ChipContadorSync(
                        etiqueta = "Actualizadas",
                        cantidad = informe.actualizadas.size,
                        color = Advertencia,
                        seleccionado = filtroSeleccionado == FiltroCambiosSync.ACTUALIZADAS,
                        alPulsar = { filtroSeleccionado = FiltroCambiosSync.ACTUALIZADAS }
                    )
                }
                if (informe.papelera.isNotEmpty()) {
                    ChipContadorSync(
                        etiqueta = "Papelera",
                        cantidad = informe.papelera.size,
                        color = ColorPapelera,
                        seleccionado = filtroSeleccionado == FiltroCambiosSync.PAPELERA,
                        alPulsar = { filtroSeleccionado = FiltroCambiosSync.PAPELERA }
                    )
                }
                if (informe.sinCambiosCount > 0) {
                    ChipContadorSync(
                        etiqueta = "Sin cambios",
                        cantidad = informe.sinCambiosCount,
                        color = ColorAjusteGris,
                        seleccionado = false,
                        alPulsar = {}
                    )
                }
            }

            // Lista detallada de entradas modificadas con sus máscaras de indicadores
            if (itemsFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaCampo)
                        .background(ColorCampoAjustes)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (!informe.hayCambios) {
                            "Ambas bóvedas están al día. Todas las entradas (${informe.sinCambiosCount}) son idénticas."
                        } else {
                            "No hay entradas en esta categoría."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(itemsFiltrados, key = { it.id + it.tipoCambio.name }) { item ->
                        FilaInformeEntradaSync(item = item)
                    }
                }
            }
        }
    }
}

/**
 * Píldora interactiva con contador y color temático.
 */
@Composable
private fun ChipContadorSync(
    etiqueta: String,
    cantidad: Int,
    color: Color,
    seleccionado: Boolean,
    alPulsar: () -> Unit
) {
    val fondo = if (seleccionado) color.copy(alpha = 0.22f) else ColorCampoAjustes
    val bordeColor = if (seleccionado) color else ColorBordeActual

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(fondo)
            .border(1.dp, bordeColor, RoundedCornerShape(8.dp))
            .clickable(onClick = alPulsar)
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = "$etiqueta: $cantidad",
            fontSize = 12.sp,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
            color = if (seleccionado) TextoPrincipal else TextoSecundario
        )
    }
}

/**
 * Fila que renderiza una entrada modificada en la sincronización,
 * proyectando únicamente su título y la máscara de diff con los 4 estilos de indicadores visuales.
 */
@Composable
private fun FilaInformeEntradaSync(item: ItemCambioSync) {
    val segmentos = recordarSegmentosDiff(mascara = item.mascara)

    val (iconoTipo, colorTipo, badgeTexto) = when (item.tipoCambio) {
        TipoCambioSync.NUEVA -> Triple(Icons.Filled.AddCircle, Menta, "NUEVA")
        TipoCambioSync.ACTUALIZADA -> Triple(Icons.Filled.Update, Advertencia, "ACTUALIZADA")
        TipoCambioSync.ENVIADA_A_PAPELERA -> Triple(Icons.Filled.Delete, ColorPapelera, "PAPELERA")
        TipoCambioSync.RESTAURADA_DE_PAPELERA -> Triple(Icons.Filled.Restore, Menta, "RESTAURADA")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaCampo)
            .background(ColorCampoAjustes)
            .border(0.8.dp, ColorBordeActual, FormaCampo)
    ) {
        // Indicadores superiores (líneas) o laterales si están activos
        if (EstiloIndicadores == "lineas" || EstiloIndicadores == "lateral") {
            val alineacion = if (EstiloIndicadores == "lateral") Alignment.CenterStart else Alignment.TopCenter
            IndicadorSegmentosTarjeta(
                segmentos = segmentos,
                modifier = Modifier.align(alineacion),
                estilo = EstiloIndicadores
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = iconoTipo,
                contentDescription = badgeTexto,
                tint = colorTipo,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = item.titulo,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TextoPrincipal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // Indicadores de iconos o puntos al extremo derecho
            if (EstiloIndicadores == "iconos" || EstiloIndicadores == "puntos") {
                IndicadorSegmentosTarjeta(
                    segmentos = segmentos,
                    estilo = EstiloIndicadores
                )
            }
        }
    }
}
