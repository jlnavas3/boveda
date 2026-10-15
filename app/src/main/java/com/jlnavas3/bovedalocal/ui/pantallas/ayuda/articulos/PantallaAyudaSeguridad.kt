/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
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
fun PantallaAyudaSeguridad(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Seguridad y biometría",
        alVolver = alVolver,
        textoAccion = "Ajustes de Seguridad",
        iconoAccion = Icons.Filled.Security,
        alEjecutarAccion = { vm.ir(Pantalla.Ajustes("01-SEG")) }
    ) {
        TarjetaInformativaAyuda(titulo = "Desbloqueo biométrico por hardware") {
            Text(
                text = "Tu clave maestra nunca se guarda en texto claro en memoria. Si activas el desbloqueo biométrico, se almacena una clave derivada en el chip de seguridad seguro de Android (Android Keystore / TEE / StrongBox), requiriendo tu huella o rostro para liberarla.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Políticas de protección en primer plano") {
            Text(
                text = "• Bloqueo por inactividad: Cierra la bóveda automáticamente al apagar la pantalla o tras un periodo sin interacción (1, 3, 5 o 15 minutos).\n\n• Auto-vaciado de portapapeles: Elimina automáticamente del portapapeles cualquier usuario, contraseña o código 2FA tras 30 o 60 segundos para evitar que otras aplicaciones los lean en segundo plano.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
