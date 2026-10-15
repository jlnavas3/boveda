/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
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
fun PantallaAyudaEtiquetas(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Etiquetas (Tags)",
        alVolver = alVolver
    ) {
        TarjetaInformativaAyuda(titulo = "Clasificación transversal ágil") {
            Text(
                text = "Mientras que una entrada suele pertenecer a una sola categoría, puede tener múltiples etiquetas simultáneas (ej: 'urgente', 'temporal', 'compartido', 'servidor-madrid').",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Cómo utilizarlas?") {
            Text(
                text = "• En el formulario de creación/edición, escribe cualquier palabra en el campo de etiquetas y pulsa Enter o la coma para agregarla.\n\n• La aplicación recuerda tus etiquetas previas y te las sugiere automáticamente conforme vas escribiendo.\n\n• En la barra de búsqueda puedes escribir 'tag:nombre' o pulsar la píldora superior de etiquetas para filtrar rápidamente.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
