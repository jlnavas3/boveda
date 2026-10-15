/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
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
fun PantallaAyudaImportacion(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Importación de contraseñas",
        alVolver = alVolver,
        textoAccion = "Importar de Google",
        iconoAccion = Icons.Filled.FileDownload,
        alEjecutarAccion = { vm.ir(Pantalla.CsvGoogle()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Migración sencilla sin fricción") {
            Text(
                text = "Puedes traer fácilmente todas tus contraseñas almacenadas previamente en Google Chrome, Mozilla Firefox, Bitwarden u otros navegadores.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Pasos para importar desde Google Chrome") {
            Text(
                text = "1. En tu ordenador o navegador móvil, abre el Administrador de contraseñas de Google.\n\n2. Ve a Configuración > 'Exportar contraseñas' y descarga el archivo .csv.\n\n3. En Bóveda Local, ve a Ajustes > 'Importar de Google' y selecciona dicho archivo.\n\n4. Revisa las entradas detectadas y confirma la importación para cifrarlas en tu base de datos local.\n\nIMPORTANTE: Tras importar, elimina inmediatamente el archivo CSV sin cifrar de tus descargas por seguridad.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
