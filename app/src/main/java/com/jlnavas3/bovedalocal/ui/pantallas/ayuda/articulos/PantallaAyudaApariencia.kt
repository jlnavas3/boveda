/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
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
fun PantallaAyudaApariencia(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Temas y personalización",
        alVolver = alVolver,
        textoAccion = "Laboratorio de temas",
        iconoAccion = Icons.Filled.Palette,
        alEjecutarAccion = { vm.ir(Pantalla.LaboratorioTemas()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Control visual milimétrico") {
            Text(
                text = "Bóveda Local te permite adaptar cada rincón de la interfaz a tus gustos ergonómicos y estéticos sin alterar la seguridad de tus datos.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Opciones del Laboratorio") {
            Text(
                text = "• Temas cromáticos: Paletas oscuras puras (AMOLED / Obsidiana), tonos azul noche, esmeralda, ámbar o temas claros suaves.\n\n• Formas y curvaturas: Desde esquinas completamente cuadradas (0.dp) hasta formas orgánicas ultra-redondeadas (32.dp).\n\n• Bordes y micro-sombreados: Grosor de borde regulable y estilo de contorno de tarjeta.\n\n• Tipografía: Escala de texto y familias tipográficas para máxima legibilidad.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
