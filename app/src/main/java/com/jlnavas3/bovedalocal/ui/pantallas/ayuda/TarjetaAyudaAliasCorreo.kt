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
import androidx.compose.material.icons.filled.AlternateEmail
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
 * Tarjeta explicativa sobre el generador de alias de correo electrónico (RFC 5233).
 */
@Composable
fun TarjetaAyudaAliasCorreo(modifier: Modifier = Modifier) {
    ComponenteGrupo(
        etiqueta = "Generador de alias de correo (@)",
        icono = Icons.Filled.AlternateEmail,
        colorIcono = Color(0xFF00ACC1),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Protege tu correo real contra filtraciones:",
                fontSize = 13.sp,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Al registrarte en nuevos servicios, toca el icono '@' junto al campo 'Usuario o correo'. Puedes generar un alias etiquetado como 'tu_correo+netflix@gmail.com' o un código aleatorio.",
                fontSize = 12.sp,
                color = TextoSecundario,
                lineHeight = 17.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "¿Cómo funciona?",
                fontSize = 13.sp,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Casi todos los proveedores de correo (Gmail, Proton, Outlook, Fastmail, iCloud) soportan subdireccionamiento RFC 5233: todos los mensajes llegarán a tu bandeja principal, pero si un servicio filtra tu correo, sabrás exactamente quién fue y podrás crear reglas de filtrado para bloquear spam.",
                fontSize = 12.sp,
                color = TextoSecundario,
                lineHeight = 17.sp
            )
        }
    }
}
