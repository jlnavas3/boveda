/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
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
fun PantallaAyudaKitEmergencia(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Kit de emergencia",
        alVolver = alVolver,
        textoAccion = "Generar Kit de Emergencia",
        iconoAccion = Icons.Filled.Description,
        alEjecutarAccion = { vm.ir(Pantalla.KitEmergencia()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Tu salvavidas físico fuera de línea") {
            Text(
                text = "Dado que Bóveda Local es 100% de conocimiento cero (Zero-Knowledge) y no almacena copias en servidores remotos, nadie en el mundo puede resetear tu clave maestra si la olvidas.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Cómo usar el Kit de Emergencia?") {
            Text(
                text = "1. Genera el documento PDF imprimible desde Ajustes > Kit de emergencia.\n\n2. Imprímelo en papel físico o anota los campos a mano.\n\n3. Escribe tu clave maestra y guárdalo en un lugar seguro bajo llave en tu domicilio (caja fuerte, archivador privado).\n\n4. En caso de daño irreparable de tu móvil o pérdida de acceso biométrico, este documento te garantizará recuperar el control de tu vida digital.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
