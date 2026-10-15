/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
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
fun PantallaAyudaPasskeys(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Llaves de paso (Passkeys)",
        alVolver = alVolver,
        textoAccion = "Ver Llaves de paso",
        iconoAccion = Icons.Filled.Fingerprint,
        alEjecutarAccion = { vm.ir(Pantalla.Passkeys()) }
    ) {
        TarjetaInformativaAyuda(titulo = "¿Qué es una Passkey?") {
            Text(
                text = "Las Llaves de paso (Passkeys) son el nuevo estándar internacional FIDO2 / WebAuthn que reemplaza las contraseñas tradicionales por pares de claves criptográficas asimétricas (pública y privada).",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Inmunidad total al Phishing") {
            Text(
                text = "• Tu clave privada jamás sale de tu dispositivo ni se comparte con los servidores del sitio web.\n\n• Es imposible que un sitio falso te robe la passkey porque el navegador valida criptográficamente el dominio exacto antes de firmar el desafío.\n\n• Puedes gestionar tus llaves de paso registradas desde la pantalla dedicada de Passkeys en el menú lateral.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
