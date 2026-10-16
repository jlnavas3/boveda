/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
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
fun PantallaAyudaCopias(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Copias de seguridad",
        alVolver = alVolver,
        textoAccion = "Ir a Copia de seguridad",
        iconoAccion = Icons.Filled.Backup,
        alEjecutarAccion = { vm.ir(Pantalla.CopiaSeguridad()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Formato seguro .bvda con compresión BLZ1") {
            Text(
                text = "Tus copias de seguridad se exportan en formato cifrado nativo .bvda. Antes de cifrarse con AES-256-GCM, los datos se comprimen con el algoritmo Deflate (BLZ1), reduciendo su tamaño en más de un 60% y agilizando las copias.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Inspección previa (Dry-Run)") {
            Text(
                text = "Al restaurar un archivo de copia, la aplicación no sobreescribe nada a ciegas: te muestra primero un resumen transparente con la cantidad de entradas, categorías, identidades y fecha exacta de creación del respaldo para que confirmes antes de aplicar.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Copias automáticas en almacenamiento local") {
            Text(
                text = "Puedes programar respaldos automáticos periódicos (diarios, semanales o mensuales) en una carpeta local de tu dispositivo o tarjeta SD.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }
    }
}
