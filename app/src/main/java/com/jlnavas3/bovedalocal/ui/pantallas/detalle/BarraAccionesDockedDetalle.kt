/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.ItemAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorAnclado
import com.jlnavas3.bovedalocal.ui.theme.ColorFavorito
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Barra inferior normalizada para la pantalla de detalle de entrada:
 * - Eliminar (icono neutro secundario)
 * - QR (icono neutro secundario)
 * - Anclar (azul zafiro si activo, neutro secundario si inactivo)
 * - Favorito (amarillo si activo, neutro secundario si inactivo)
 * - Editar (color de acento primario)
 *
 * Los textos siempre permanecen neutros (TextoSecundario), destacando únicamente el glifo del icono.
 */
@Composable
fun BarraAccionesDockedDetalle(
    esFavorito: Boolean,
    esAnclado: Boolean,
    alEliminar: () -> Unit,
    alCompartirQr: () -> Unit,
    alAlternarAnclado: () -> Unit,
    alAlternarFavorito: () -> Unit,
    alEditar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ItemAccionBarraInferior(
            icono = Icons.Filled.Delete,
            etiqueta = "Eliminar",
            alPulsar = alEliminar,
            colorIcono = ColorIconosInternos,
            colorTexto = TextoSecundario,
            descripcion = "Eliminar entrada"
        ),
        ItemAccionBarraInferior(
            icono = Icons.Filled.QrCode,
            etiqueta = "QR",
            alPulsar = alCompartirQr,
            colorIcono = ColorIconosInternos,
            colorTexto = TextoSecundario,
            descripcion = "Mostrar código QR"
        ),
        ItemAccionBarraInferior(
            icono = Icons.Filled.PushPin,
            etiqueta = "Anclar",
            alPulsar = alAlternarAnclado,
            colorIcono = if (esAnclado) ColorAnclado else ColorIconosInternos,
            colorTexto = TextoSecundario,
            descripcion = if (esAnclado) "Desanclar entrada" else "Anclar entrada"
        ),
        ItemAccionBarraInferior(
            icono = Icons.Filled.Star,
            etiqueta = "Favorito",
            alPulsar = alAlternarFavorito,
            colorIcono = if (esFavorito) ColorFavorito else ColorIconosInternos,
            colorTexto = TextoSecundario,
            descripcion = if (esFavorito) "Quitar de favoritos" else "Marcar como favorito"
        ),
        ItemAccionBarraInferior(
            icono = Icons.Filled.Edit,
            etiqueta = "Editar",
            alPulsar = alEditar,
            colorIcono = ColorAcento,
            colorTexto = TextoSecundario,
            descripcion = "Editar entrada"
        )
    )

    BarraAccionesInferiorBoveda(
        items = items,
        modifier = modifier
    )
}

