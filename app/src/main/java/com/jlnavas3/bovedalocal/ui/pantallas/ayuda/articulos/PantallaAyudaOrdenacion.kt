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
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PantallaAyudaOrdenacion(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Ordenación y diseño",
        alVolver = alVolver,
        textoAccion = "Configurar Diseño de lista",
        iconoAccion = Icons.AutoMirrored.Filled.Sort,
        alEjecutarAccion = { vm.ir(Pantalla.OrganizacionLista()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Criterios de ordenación") {
            Text(
                text = "Puedes ordenar tus credenciales en cualquier momento tocando el icono de ordenación en la cabecera:\n\n• Nombre (A - Z / Z - A): Orden alfabético tradicional.\n• Modificación reciente: Prioriza las entradas actualizadas recientemente.\n• Uso frecuente: Sitúa arriba las contraseñas que más copias o consultas habitualmente.\n• Antigüedad de contraseña: Te ayuda a localizar las claves más viejas que convendría renovar.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Agrupación por sitio web") {
            Text(
                text = "Si tienes varias cuentas para el mismo dominio (por ejemplo, 3 cuentas en 'google.com' o 2 en 'amazon.es'), activa 'Agrupar por sitio' en Ajustes > Lista. La aplicación colapsará esas cuentas en una sola tarjeta elegante con selector desplegable.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }
    }
}
