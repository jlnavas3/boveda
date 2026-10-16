/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
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
fun PantallaAyudaAdjuntos(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Archivos adjuntos cifrados",
        alVolver = alVolver,
        textoAccion = "Ir a la Bóveda",
        iconoAccion = Icons.Filled.AttachFile,
        alEjecutarAccion = { vm.ir(Pantalla.Lista) }
    ) {
        TarjetaInformativaAyuda(titulo = "Cifrado local dentro del contenedor .bvda") {
            Text(
                text = "Bóveda Local permite vincular archivos confidenciales (documentos de identidad, contratos, fotos de tarjetas de crédito o certificados) directamente a cualquier entrada de tu bóveda. Los datos se cifran con la misma clave maestra y viajan dentro del contenedor seguro .bvda o .cxf, sin usar servidores ni nubes de terceros.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Cómo adjuntar y gestionar archivos?") {
            Text(
                text = "1. Abre cualquier entrada existente o pulsa sobre el botón '+' para crear una nueva.\n\n2. En la sección 'Archivos adjuntos cifrados', toca 'Adjuntar archivo seguro'.\n\n3. Selecciona el archivo deseado desde el explorador del sistema (Storage Access Framework).\n\n4. Para proteger el rendimiento y la fluidez en el dispositivo móvil, existe un límite seguro de 2 MB por archivo adjunto.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Visor offline y exportación segura") {
            Text(
                text = "• Previsualización en memoria: Las imágenes (PNG, JPEG, WebP) y archivos de texto plano (TXT, JSON, PEM) se pueden previsualizar directamente con el botón de ojo sin escribirlos en el disco desprotegido.\n\n• Exportación mediante SAF: Con el botón de compartir/guardar puedes exportar cualquier adjunto a la carpeta que elijas de tu almacenamiento local con solo un toque.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }
    }
}
