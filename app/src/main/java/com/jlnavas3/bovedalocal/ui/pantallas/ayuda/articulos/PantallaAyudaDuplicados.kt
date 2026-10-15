/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
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
fun PantallaAyudaDuplicados(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Contraseñas duplicadas",
        alVolver = alVolver,
        textoAccion = "Ver Duplicados",
        iconoAccion = Icons.Filled.CopyAll,
        alEjecutarAccion = { vm.ir(Pantalla.Duplicados()) }
    ) {
        TarjetaInformativaAyuda(titulo = "El mayor riesgo en ciberseguridad") {
            Text(
                text = "Reutilizar la misma contraseña es la causa del 80% de los hackeos de cuentas en internet. Si una tienda online sufre una brecha, los atacantes probarán esa misma combinación en tus correos, bancos y redes.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Detector inteligente de duplicados") {
            Text(
                text = "La herramienta de Duplicados agrupa todas las cuentas que comparten la misma clave y te permite acceder directamente a cambiarlas por contraseñas únicas generadas al azar.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
