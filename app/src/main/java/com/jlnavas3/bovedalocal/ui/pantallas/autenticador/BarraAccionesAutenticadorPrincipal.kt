/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.ItemAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Barra inferior fija de acciones para la pantalla de verificación en dos pasos (Autenticador):
 * - Escanear código QR
 * - Ingresar clave manualmente
 */
@Composable
fun BarraAccionesAutenticadorPrincipal(
    alEscanearQr: () -> Unit,
    alIngresarManual: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ItemAccionBarraInferior(
            icono = Icons.Filled.QrCodeScanner,
            etiqueta = "Escanear",
            alPulsar = alEscanearQr,
            colorIcono = ColorAcento,
            descripcion = "Escanear código QR"
        ),
        ItemAccionBarraInferior(
            icono = Icons.Filled.Add,
            etiqueta = "Manual",
            alPulsar = alIngresarManual,
            colorIcono = ColorIconosInternos,
            descripcion = "Escribir clave manualmente"
        )
    )

    BarraAccionesInferiorBoveda(
        items = items,
        modifier = modifier
    )
}
