/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Tarjeta informativa que describe el estándar FIDO CXF y la interoperabilidad
 * segura entre gestores de contraseñas de Android.
 */
@Composable
fun TarjetaInformativaCxf(
    modifier: Modifier = Modifier
) {
    Surface(
        color = ColorTarjetaAjustes,
        shape = RoundedCornerShape(CurvaturaEsquinas),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CurvaturaEsquinas))
            .border(GrosorBorde, ColorBordeActual, RoundedCornerShape(CurvaturaEsquinas))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "Transferencia directa (FIDO CXF)",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextoPrincipal
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Permite enviar credenciales hacia otros administradores compatibles (como el Administrador de contraseñas de Google, Dashlane o Bitwarden) directamente en este dispositivo.",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "El intercambio se realiza mediante el sistema Android, de forma 100% local, cifrada y sin intermediarios en la nube. Al bloquearse la bóveda, la disponibilidad se revoca de inmediato.",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )
        }
    }
}
