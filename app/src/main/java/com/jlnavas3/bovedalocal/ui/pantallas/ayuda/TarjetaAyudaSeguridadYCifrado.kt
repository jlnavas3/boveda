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
import androidx.compose.material.icons.filled.Lock
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
 * Tarjeta explicativa sobre la arquitectura de seguridad, cifrado AES-256-GCM y compresión BLZ1.
 */
@Composable
fun TarjetaAyudaSeguridadYCifrado(modifier: Modifier = Modifier) {
    ComponenteGrupo(
        etiqueta = "Seguridad y cifrado de datos",
        icono = Icons.Filled.Lock,
        colorIcono = Color(0xFF4CAF50),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Arquitectura de conocimiento cero (Zero-Knowledge):",
                fontSize = 13.sp,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Tus credenciales nunca viajan por internet ni se almacenan en la nube. Todo reside exclusivamente en tu dispositivo, protegido por una clave maestra que solo tú conoces.",
                fontSize = 12.sp,
                color = TextoSecundario,
                lineHeight = 17.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Cifrado y compresión de alta eficiencia:",
                fontSize = 13.sp,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Cada archivo de la bóveda se comprime primero con el algoritmo Deflate (cabecera BLZ1) y luego se cifra con AES-256-GCM con autenticación de integridad por hardware. Esto garantiza máxima velocidad y protección contra manipulaciones.",
                fontSize = 12.sp,
                color = TextoSecundario,
                lineHeight = 17.sp
            )
        }
    }
}
