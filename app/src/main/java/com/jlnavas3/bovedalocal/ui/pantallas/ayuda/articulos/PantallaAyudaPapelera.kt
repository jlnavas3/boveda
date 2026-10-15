/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
fun PantallaAyudaPapelera(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Papelera de reciclaje",
        alVolver = alVolver,
        textoAccion = "Abrir Papelera",
        iconoAccion = Icons.Filled.Delete,
        alEjecutarAccion = { vm.ir(Pantalla.Papelera()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Protección contra borrados accidentales") {
            Text(
                text = "Cuando eliminas una entrada, no se borra de inmediato de tu disco: se traslada a la Papelera de reciclaje con cifrado intacto para que puedas recuperarla si te equivocaste.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Restauración y purga automática") {
            Text(
                text = "• Restauración: Puedes entrar a la Papelera y devolver cualquier credencial a tu listado principal con su historial y claves intactas.\n\n• Vaciado definitivo: Puedes vaciar la papelera manualmente en cualquier momento.\n\n• Política de retención: En Ajustes puedes configurar la auto-purga a 30, 60 o 90 días para mantener limpia la bóveda sin preocuparte.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
