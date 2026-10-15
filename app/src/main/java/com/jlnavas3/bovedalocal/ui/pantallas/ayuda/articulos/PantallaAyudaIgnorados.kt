/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PantallaAyudaIgnorados(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Ignorar en auditoría",
        alVolver = alVolver
    ) {
        TarjetaInformativaAyuda(titulo = "¿Para qué sirve ignorar una entrada?") {
            Text(
                text = "Hay ocasiones en que tienes cuentas temporales, routers locales o sistemas de prueba donde no necesitas cumplir con políticas estrictas de contraseñas. Marcar 'Ignorar en auditoría' evita que afecten tu porcentaje de salud de la bóveda.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Dos formas de aplicarlo") {
            Text(
                text = "1. Desde la edición: Abre la entrada para editar, ve a la sección inferior de auditoría y activa la casilla 'Ignorar en auditoría'.\n\n2. En lote desde el listado principal: Selecciona una o varias entradas manteniendo pulsado, toca el menú de los 3 puntos en la barra inferior flotante y pulsa 'Ignorar en auditoría'.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
