/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Tarjeta explicativa sobre el panel de salud de contraseñas, auditoría continua e ignorar elementos.
 */
@Composable
fun TarjetaAyudaAuditoriaSalud(modifier: Modifier = Modifier) {
    ComponenteGrupo(
        etiqueta = "Salud y auditoría de la bóveda",
        icono = Icons.Filled.Shield,
        colorIcono = Color(0xFFE57373),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Insignia de salud global en la cabecera:",
                fontSize = 13.sp,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "El icono de escudo en la barra principal muestra en tiempo real cuántas contraseñas requieren atención (reutilizadas, débiles, antiguas o sin segundo factor). Toca el escudo para ir directo a la auditoría detallada.",
                fontSize = 12.sp,
                color = TextoSecundario,
                lineHeight = 17.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Ignorar en auditoría:",
                fontSize = 13.sp,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Si tienes cuentas secundarias o de prueba que no necesitas corregir, puedes marcarlas como 'Ignorar en auditoría' desde la edición de la entrada o seleccionando varias y abriendo el menú de los 3 puntos en la barra inferior.",
                fontSize = 12.sp,
                color = TextoSecundario,
                lineHeight = 17.sp
            )
        }
    }
}
