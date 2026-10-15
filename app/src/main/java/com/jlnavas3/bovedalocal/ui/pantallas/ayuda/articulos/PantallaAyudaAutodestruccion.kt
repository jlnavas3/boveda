/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
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
fun PantallaAyudaAutodestruccion(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Autodestrucción de emergencia",
        alVolver = alVolver,
        textoAccion = "Configurar Autodestrucción",
        iconoAccion = Icons.Filled.Warning,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesAutodestruccion()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Defensa contra ataques físicos de fuerza bruta") {
            Text(
                text = "Si alguien sustrae físicamente tu dispositivo e intenta adivinar tu clave maestra una y otra vez, la función de autodestrucción borra de forma criptográfica e irrecuperable toda la base de datos local tras superar un umbral de intentos fallidos (por defecto 10 intentos).",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Recomendación vital") {
            Text(
                text = "Ten siempre una copia de seguridad externa (.cxf) o tu Kit de Emergencia impreso en un lugar seguro antes de activar umbrales de autodestrucción estrictos.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
