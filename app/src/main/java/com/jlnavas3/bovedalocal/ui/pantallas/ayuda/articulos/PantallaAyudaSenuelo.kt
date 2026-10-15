/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
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
fun PantallaAyudaSenuelo(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Bóveda señuelo (Duress Vault)",
        alVolver = alVolver,
        textoAccion = "Configurar Bóveda Señuelo",
        iconoAccion = Icons.Filled.Visibility,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesSenuelo()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Protección bajo coacción o amenaza") {
            Text(
                text = "Permite definir una segunda clave maestra distinta a la real. Si alguna vez te ves forzado físicamente a desbloquear tu bóveda frente a un tercero, ingresas tu clave señuelo.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Qué ocurre al desbloquear con el señuelo?") {
            Text(
                text = "• La aplicación se abre con normalidad simulando ser legítima.\n\n• Se muestran únicamente entradas ficticias e inofensivas que hayas precargado como señuelo.\n\n• Tus credenciales reales permanecen 100% ocultas, cifradas y sin dejar rastro de su existencia.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
