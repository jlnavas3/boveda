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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.PreferenciaIdentificadorIdentidad
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Micro-componente desacoplado para elegir cuál es el identificador principal
 * que se usará por defecto para autocompletar credenciales (Correo vs Documento).
 */
@Composable
fun SelectorPreferenciaIdentificador(
    preferenciaActual: String,
    alSeleccionarPreferencia: (String) -> Unit,
    colorAcento: Color,
    siglaDocumento: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Identificador predeterminado en credenciales",
            fontSize = 11.5.sp,
            color = TextoSecundario,
            fontWeight = FontWeight.Medium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PildoraOpcionPreferencia(
                titulo = "Correo",
                icono = Icons.Filled.Email,
                seleccionado = preferenciaActual == PreferenciaIdentificadorIdentidad.CORREO,
                colorAcento = colorAcento,
                alPulsar = { alSeleccionarPreferencia(PreferenciaIdentificadorIdentidad.CORREO) },
                modifier = Modifier.weight(1f)
            )

            PildoraOpcionPreferencia(
                titulo = siglaDocumento,
                icono = Icons.Filled.Badge,
                seleccionado = preferenciaActual == PreferenciaIdentificadorIdentidad.DOCUMENTO,
                colorAcento = colorAcento,
                alPulsar = { alSeleccionarPreferencia(PreferenciaIdentificadorIdentidad.DOCUMENTO) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PildoraOpcionPreferencia(
    titulo: String,
    icono: ImageVector,
    seleccionado: Boolean,
    colorAcento: Color,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondo = if (seleccionado) colorAcento.copy(alpha = 0.14f) else ColorCampoAjustes
    val borde = if (seleccionado) colorAcento else ColorSeparadorAjustes
    val colorContenido = if (seleccionado) colorAcento else TextoPrincipal

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(fondo)
            .border(if (seleccionado) 1.5.dp else 0.8.dp, borde, RoundedCornerShape(8.dp))
            .clickable(onClick = alPulsar)
            .padding(horizontal = 10.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorContenido,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = titulo,
                fontSize = 13.sp,
                fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal,
                color = colorContenido
            )
        }
    }
}
