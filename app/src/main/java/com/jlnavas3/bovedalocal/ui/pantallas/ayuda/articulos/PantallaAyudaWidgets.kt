/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Widgets
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
fun PantallaAyudaWidgets(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Widgets de escritorio",
        alVolver = alVolver,
        textoAccion = "Ajustes de Widgets",
        iconoAccion = Icons.Filled.Widgets,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesWidget()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Accesos ultrarrápidos desde tu launcher") {
            Text(
                text = "Puedes colocar accesos interactivos en la pantalla de inicio de tu teléfono para agilizar tus tareas diarias:",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Modalidades de Widget disponibles") {
            Text(
                text = "• Widget 1x1: Botón compacto de un solo icono que puedes configurar para 'Bloqueo rápido', 'Abrir Escáner QR' o 'Crear Entrada'.\n\n• Widget TOTP dinámico: Muestra en tiempo real tus códigos de verificación 2FA con barra de cuenta regresiva sin necesidad de abrir la aplicación completa.\n\n• Quick Settings Tile: Botón en la cortina de notificaciones de Android para bloquear tu bóveda instantáneamente desde cualquier app.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
