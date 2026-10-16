/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
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
fun PantallaAyudaGenerador(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Generador de contraseñas",
        alVolver = alVolver,
        textoAccion = "Abrir Generador",
        iconoAccion = Icons.Filled.Key,
        alEjecutarAccion = { vm.ir(Pantalla.Generador) }
    ) {
        TarjetaInformativaAyuda(titulo = "Generador de contraseñas de alta entropía") {
            Text(
                text = "Permite generar combinaciones aleatorias criptográficamente seguras con longitud regulable (hasta 128 caracteres), permitiendo alternar mayúsculas, minúsculas, números y símbolos especiales.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Modo Frase de Paso (Diceware)") {
            Text(
                text = "Ideal para tu clave maestra o contraseñas que debas teclear en teclados físicos o consolas:\n\n• Genera secuencias de palabras aleatorias en español separadas por guiones o espacios (ej: 'guitarra-planeta-bosque-reloj').\n• Son extremadamente resistentes a ataques de fuerza bruta pero muy fáciles de recordar por el cerebro humano.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
