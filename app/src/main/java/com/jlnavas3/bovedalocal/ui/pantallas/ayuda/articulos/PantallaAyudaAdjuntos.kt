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
import androidx.compose.material.icons.filled.GridView
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda sobre archivos adjuntos cifrados, límite de tamaño, visor interno y exportación SAF.
 */
@Composable
fun PantallaAyudaAdjuntos(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Archivos adjuntos cifrados",
        alVolver = alVolver,
        textoAccion = "Ver Lista Principal",
        iconoAccion = Icons.Filled.GridView,
        alEjecutarAccion = { vm.ir(Pantalla.Lista) }
    ) {
        TarjetaInformativaAyuda(titulo = "Documentos blindados dentro de la bóveda") {
            Text(
                text = "Muchas veces, custodiar una cuenta exige guardar algo más que un texto: una foto de un pasaporte, una tarjeta de coordenadas bancarias, un certificado SSL o un documento PDF de recuperación. Bóveda Local te permite adjuntar estos archivos directamente dentro de la base de datos cifrada.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Límite técnico de 2 MB y rendimiento") {
            Text(
                text = "• Límite por archivo: Cada entrada admite adjuntos de hasta 2 MB de tamaño.\n\n• ¿Por qué existe este límite?: Guardar archivos masivos de video o fotos pesadas en bases de datos SQLite degradaría los tiempos de arranque, aumentaría el consumo de memoria RAM y ralentizaría la generación de copias de seguridad. El límite de 2 MB garantiza fluidez instantánea mientras cubre con holgura fotos comprimidas de tarjetas, documentos escaneados y claves criptográficas.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Visor interno seguro y exportación SAF") {
            Text(
                text = "• Visor integrado: Puedes previsualizar fotos y documentos dentro de Bóveda Local sin recurrir a visores externos de terceros que puedan copiar la imagen en memorias caché no cifradas.\n\n• Aislamiento de la Galería: Los adjuntos nunca aparecen en la galería de fotos pública de Android ni en Google Fotos.\n\n• Exportación controlada (SAF): Si necesitas rescatar el archivo original para un trámite externo, el botón 'Exportar archivo' invoca el Storage Access Framework oficial de Android para que elijas exactamente en qué carpeta guardarlo.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Integración en respaldos .bvda") {
            Text(
                text = "Todos los archivos adjuntos se incluyen automáticamente al generar una copia de seguridad .bvda, beneficiándose de la compresión BLZ1 para reducir su volumen en disco. Además, si deseas generar un respaldo ligero para compartir rápidamente, el Asistente de Exportación Selectiva te permite excluir los adjuntos con una sola casilla.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Consejo de compresión previa",
            texto = "Si la foto de tu pasaporte o DNI tomada con la cámara pesa 8 o 10 MB, redúcela antes de adjuntarla recortando únicamente el área del documento: ocupará menos de 500 KB y se visualizará con nitidez perfecta."
        )
    }
}
