/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
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
 * Artículo de ayuda exhaustivo sobre criterios de ordenación, favoritos y agrupación inteligente por sitio.
 */
@Composable
fun PantallaAyudaOrdenacion(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Criterios de ordenación y grupos",
        alVolver = alVolver,
        textoAccion = "Ajustes de Organización",
        iconoAccion = Icons.AutoMirrored.Filled.Sort,
        alEjecutarAccion = { vm.ir(Pantalla.OrganizacionLista()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Control visual y jerárquico del listado") {
            Text(
                text = "A medida que tu bóveda crece hasta acumular cientos de cuentas, disponer de un orden adaptado a tus hábitos de navegación es crucial para encontrar cualquier registro en menos de dos segundos.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Criterios de ordenación disponibles") {
            Text(
                text = "Desde Ajustes > Lista de cuentas > Criterios de ordenación puedes elegir el criterio predeterminado:\n\n• Alfabético (A - Z / Z - A): Orden lexicográfico limpio por título normalizado con cabeceras de letra.\n\n• Por fecha de modificación: Sitúa en la cima las cuentas que acabas de actualizar o revisar recientemente.\n\n• Por fecha de creación: Muestra las cuentas en orden cronológico inverso desde que fueron creadas.\n\n• Por frecuencia de uso: Algoritmo que contabiliza cuántas veces abres o copias cada cuenta, situando en la parte superior las credenciales que utilizas a diario.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Agrupamiento inteligente por sitio web / dominio") {
            Text(
                text = "¿Tienes varias cuentas de Google, distintos usuarios para AWS o múltiples accesos a un mismo servidor? La opción 'Agrupar por sitio web' detecta los dominios coincidentes:\n\n• Consolida esas entradas bajo una única tarjeta expandible con el logo del servicio y un contador numérico (ej: 'Google (4 cuentas)').\n• Al pulsar sobre la tarjeta, se despliegan suavemente las cuentas hijas.\n• Mantiene tu listado despejado y ordenado sin largas filas repetidas de un mismo proveedor.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Cuentas Favoritas (Ancladas)") {
            Text(
                text = "Toca el icono de la estrella (⭐) en cualquier cuenta para marcarla como favorita. Las cuentas favoritas se agrupan en una sección destacada en la parte superior del listado para acceso inmediato, independientemente del criterio de ordenación seleccionado.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Alterna el orden al vuelo",
            texto = "En la barra superior de la lista principal, el icono de tres líneas con flecha te permite conmutar el orden en tiempo real sin tener que entrar a los Ajustes de la aplicación."
        )
    }
}
