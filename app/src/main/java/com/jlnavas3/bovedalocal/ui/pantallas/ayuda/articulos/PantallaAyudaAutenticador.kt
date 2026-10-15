/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PantallaAyudaAutenticador(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Autenticador 2FA / TOTP",
        alVolver = alVolver,
        textoAccion = "Ver Autenticador",
        iconoAccion = Icons.Filled.Pin,
        alEjecutarAccion = { vm.ir(Pantalla.Autenticador) }
    ) {
        TarjetaInformativaAyuda(titulo = "Códigos de verificación en dos pasos") {
            Text(
                text = "Bóveda Local incluye un generador TOTP (Time-based One-Time Password) integrado compatible con Google Authenticator, Microsoft Authenticator y cualquier servicio estándar.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Ventajas clave") {
            Text(
                text = "• Copia con 1 toque: Al pulsar sobre el código temporal de 6 dígitos se copia al portapapeles y se inicia el temporizador de auto-borrado.\n\n• Sincronización con la entrada: El código 2FA vive dentro de la propia tarjeta de la credencial, evitando saltar entre dos apps distintas.\n\n• Vista global: Puedes acceder a la pantalla del Autenticador desde el menú lateral para ver todos tus códigos en vivo con círculo de cuenta regresiva.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
