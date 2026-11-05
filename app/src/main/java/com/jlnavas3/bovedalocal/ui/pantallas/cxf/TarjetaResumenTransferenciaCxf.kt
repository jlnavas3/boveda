/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.cxf.ResultadoConversionCxf
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys

/**
 * Microcomponente de resumen para la transferencia entrante CXF:
 * cabecera de proveedor, conteo de entradas encontradas e insignias descriptivas por tipo.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TarjetaResumenTransferenciaCxf(
    exportadorNombre: String,
    totalItems: Int,
    resultadoCxf: ResultadoConversionCxf,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TextoTitulo(
            texto = "Transferencia directa desde $exportadorNombre",
            estilo = EstiloTitulo.MEDIANO
        )
        Spacer(Modifier.height(4.dp))
        TextoSubtitulo(
            texto = "Se encontraron $totalItems credenciales listas para incorporar a tu bóveda:"
        )

        Spacer(Modifier.height(10.dp))

        // Resumen de insignias con estadísticas adaptadas al tema
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (resultadoCxf.totalPasskeys > 0) {
                InsigniaResumenCxf(
                    icono = Icons.Filled.VpnKey,
                    texto = "${resultadoCxf.totalPasskeys} llaves de acceso",
                    color = ColorPasskeys
                )
            }
            if (resultadoCxf.totalContrasenas > 0) {
                InsigniaResumenCxf(
                    icono = Icons.Filled.Lock,
                    texto = "${resultadoCxf.totalContrasenas} contraseñas",
                    color = Color(0xFF2196F3)
                )
            }
            if (resultadoCxf.totalTotp > 0) {
                InsigniaResumenCxf(
                    icono = Icons.Filled.QrCode,
                    texto = "${resultadoCxf.totalTotp} verificación en dos pasos",
                    color = Color2FA
                )
            }
        }
    }
}
