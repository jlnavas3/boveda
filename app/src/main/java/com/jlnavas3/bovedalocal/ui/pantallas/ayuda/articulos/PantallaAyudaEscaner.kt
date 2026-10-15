/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
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
fun PantallaAyudaEscaner(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Escáner QR para 2FA",
        alVolver = alVolver,
        textoAccion = "Abrir Escáner QR",
        iconoAccion = Icons.Filled.QrCodeScanner,
        alEjecutarAccion = { vm.ir(Pantalla.Escaner()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Lectura de códigos QR segura") {
            Text(
                text = "Permite escanear el código QR que muestran los sitios web al activar la verificación en dos pasos (URI 'otpauth://totp/...').",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Opciones disponibles") {
            Text(
                text = "• Cámara con controles: Cuenta con botón de linterna para entornos oscuros, zoom regulable y selector de cámara.\n\n• Entrada manual: Si el sitio web te proporciona una clave secreta en texto plano (Base32), puedes pulsar 'Escribir manualmente' para ingresarla sin necesidad de cámara.\n\n• Vinculación automática: Si abres el escáner desde la edición de una entrada, se asocia directamente a ella.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
