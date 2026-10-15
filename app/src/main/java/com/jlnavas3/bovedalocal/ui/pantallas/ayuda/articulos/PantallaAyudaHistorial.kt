/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PantallaAyudaHistorial(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Historial de contraseñas",
        alVolver = alVolver
    ) {
        TarjetaInformativaAyuda(titulo = "Protección contra sobreescrituras") {
            Text(
                text = "¿Alguna vez cambiaste una contraseña, la guardaste en la app y el servicio web falló diciendo que la clave anterior aún era necesaria? El Historial de contraseñas evita que pierdas accesos.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Cómo consultarlo?") {
            Text(
                text = "• Al ver el Detalle de cualquier credencial, pulsa en el botón 'Historial' para ver todas las contraseñas anteriores con su fecha exacta de modificación.\n\n• Puedes copiar o restaurar cualquier clave antigua con un solo clic.\n\n• En Ajustes > Seguridad puedes definir cuántas versiones conservar por cada entrada o purgar el historial si deseas máxima privacidad.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
