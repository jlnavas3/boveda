/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PantallaAyudaIdentidades(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Identidades y perfiles",
        alVolver = alVolver,
        textoAccion = "Gestionar Identidades",
        iconoAccion = Icons.Filled.AccountCircle,
        alEjecutarAccion = { vm.ir(Pantalla.Identidades()) }
    ) {
        TarjetaInformativaAyuda(titulo = "¿Qué es una Identidad?") {
            Text(
                text = "Una Identidad representa un ámbito de tu vida digital (por ejemplo: 'Personal', 'Trabajo', 'Estudios' o 'Familia'). Te permite agrupar y filtrar todas las credenciales relacionadas con una dirección de correo o cuenta principal.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Asociación y visualización") {
            Text(
                text = "• Al crear o editar una credencial, puedes asignarle una Identidad directamente.\n\n• En la pantalla principal, los chips superiores te permiten alternar entre identidades con un solo toque, mostrando únicamente las entradas de ese entorno.\n\n• En Ajustes > Lista puedes personalizar si deseas ver las identidades como Chips, Menú desplegable o desactivarlas si prefieres una lista unificada.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
