/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
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
fun PantallaAyudaCategorias(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Categorías personalizadas",
        alVolver = alVolver,
        textoAccion = "Administrar Categorías",
        iconoAccion = Icons.Filled.Folder,
        alEjecutarAccion = { vm.ir(Pantalla.Categorias()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Organización por carpetas temáticas") {
            Text(
                text = "Las categorías son carpetas que te permiten clasificar tus entradas por propósito (por ejemplo: 'Finanzas', 'Streaming', 'Redes Sociales', 'Compras').",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Personalización visual") {
            Text(
                text = "• Asigna a cada categoría un color representativo y un icono distintivo.\n\n• En la pantalla principal puedes pulsar sobre la píldora de categorías para filtrar tu bóveda al instante.\n\n• Puedes reordenar o editar tus categorías en cualquier momento sin afectar las contraseñas contenidas.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
