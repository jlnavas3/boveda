/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PantallaAyudaAliasCorreo(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Alias de correo (@)",
        alVolver = alVolver
    ) {
        TarjetaInformativaAyuda(titulo = "¿Por qué usar alias de correo?") {
            Text(
                text = "Usar tu correo electrónico personal en decenas de sitios web facilita que las empresas rastreen tus hábitos y que, en caso de filtración de datos, tu dirección sea bombardeada con correos de phishing y spam.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "El botón '@' en el formulario de edición") {
            Text(
                text = "Junto al campo 'Usuario o correo' encontrarás el botón '@':\n\n• Alias por sitio: Si tu correo es 'juan@gmail.com' y estás en Netflix, genera 'juan+netflix@gmail.com'.\n• Alias aleatorio: Genera sufijos únicos como 'juan+x7k9p2@gmail.com'.\n\nTodos los mensajes siguen llegando a tu bandeja de entrada habitual (estándar RFC 5233 soportado por Gmail, Proton, Outlook, Fastmail, etc.), pero si un servicio vende tus datos, sabrás de inmediato quién fue.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
