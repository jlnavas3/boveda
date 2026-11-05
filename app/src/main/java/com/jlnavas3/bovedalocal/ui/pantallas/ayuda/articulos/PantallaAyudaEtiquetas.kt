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
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda exhaustivo sobre etiquetas (tags) transversales, autocompletado y filtros múltiples.
 */
@Composable
fun PantallaAyudaEtiquetas(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Etiquetas (Tags) transversales",
        alVolver = alVolver,
        textoAccion = "Ver Lista Principal",
        iconoAccion = Icons.Filled.Label,
        alEjecutarAccion = { vm.ir(Pantalla.Lista) }
    ) {
        TarjetaInformativaAyuda(titulo = "Organización flexible sin rigidez") {
            Text(
                text = "Mientras que las categorías imponen una estructura estricta de una sola carpeta por cuenta, las Etiquetas (Tags) te permiten clasificar transversalmente tus registros. Una misma entrada puede tener tantas etiquetas como necesites para cruzar conceptos (por ejemplo: 'Netflix' puede tener las etiquetas 'streaming', 'suscripcion', 'familiar' y 'mensual').",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Autocompletado inteligente en edición") {
            Text(
                text = "Al redactar o editar cualquier registro:\n\n• Al escribir en el campo 'Etiquetas', Bóveda Local despliega una lista contextual con todas las etiquetas que ya has utilizado previamente en otras cuentas.\n• Toca cualquier sugerencia para añadirla al instante, garantizando coherencia en los nombres y evitando duplicidades accidentales por faltas tipográficas o tildes.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Filtrado táctil y por sintaxis de búsqueda") {
            Text(
                text = "• Píldora de Etiquetas: En la barra superior de la lista principal, toca '[ 🏷️ Etiquetas ]' para desplegar el selector de etiquetas con el recuento de cuentas de cada una.\n\n• Comandos de búsqueda: Puedes escribir 'tag:suscripcion' o '#familiar' en el buscador para filtrar en vivo o combinarlo con otros modificadores (ej: 'tag:trabajo has:totp').",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Etiquetas temporales para auditoría propia",
            texto = "Crea etiquetas como 'revisar', 'compartida' o 'temporal-2026' para agrupar cuentas que requieran tu atención o que planees dar de baja en el futuro próximo."
        )
    }
}
