/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import com.jlnavas3.bovedalocal.ui.componentes.BotonFlotanteSecundario
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente con los botones flotantes de acción (FAB) para la pantalla de autenticador:
 * añadir manualmente o escanear código QR de autenticación.
 */
@Composable
fun ColumnaAccionesFlotantesAutenticador(
    haptica: Haptica,
    alIngresarManual: () -> Unit,
    alEscanearQr: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formaFab = RoundedCornerShape(CurvaturaEsquinas)

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // FAB superior: Ingresar clave manual (+)
        BotonFlotanteSecundario(
            onClick = alIngresarManual,
            icono = Icons.Filled.Add,
            descripcion = "Escribir clave manualmente",
            containerColor = ColorTarjetaAjustes,
            contentColor = Color2FA,
            colorBorde = ColorBordeActual
        )

        // FAB inferior: Escanear código QR
        FloatingActionButton(
            onClick = {
                haptica.toque()
                alEscanearQr()
            },
            containerColor = ColorAcento,
            contentColor = ColorSobreAcento,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.QrCodeScanner,
                contentDescription = "Escanear código QR",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
