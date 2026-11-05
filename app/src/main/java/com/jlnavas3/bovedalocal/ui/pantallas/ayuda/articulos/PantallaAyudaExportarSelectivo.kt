/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda sobre la exportación e importación selectiva y modular de datos.
 */
@Composable
fun PantallaAyudaExportarSelectivo(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Exportación selectiva",
        alVolver = alVolver,
        textoAccion = "Exportar de forma selectiva",
        iconoAccion = Icons.Filled.UploadFile,
        alEjecutarAccion = { vm.ir(Pantalla.ExportarSelectivo()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Migración y compartición quirúrgica") {
            Text(
                text = "A diferencia de un respaldo completo que clona la base de datos entera, el Asistente de Exportación Selectiva te permite extraer un subconjunto específico de datos cifrados sin mezclar información privada que no deseas transferir.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Filtros modulares de extracción") {
            Text(
                text = "Puedes filtrar con precisión quirúrgica qué elementos incluir en el paquete cifrado:\n\n• Por Perfiles: Exporta exclusivamente tu entorno 'Trabajo' o 'Finanzas' manteniendo aislado tu perfil 'Personal'.\n\n• Por Categorías: Selecciona una o varias categorías temáticas concretas.\n\n• Por Tipos de Entrada: ¿Solo deseas exportar tus redes Wi-Fi, tus tarjetas bancarias o tus notas seguras? Marca los tipos nativos que requieras.\n\n• Exclusión de adjuntos: Si tienes fotos o PDFs pesados que inflan el archivo, puedes desmarcarlos para generar un archivo ligero de transferencia inmediata.\n\n• Exclusión de historial: Puedes omitir las contraseñas antiguas modificadas para entregar un archivo limpio.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Importación selectiva en destino") {
            Text(
                text = "Al recibir un archivo de exportación selectiva (.bvda) en otro dispositivo, se despliega la pantalla de Importación Selectiva:\n\n• Vista previa con casillas (checkboxes): El receptor revisa cada entrada individual antes de incorporar nada a su base de datos.\n• Detección de duplicados: El sistema alerta si alguna credencial ya existe en la bóveda de destino para evitar sobreescrituras accidentales.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Cifrado con clave dedicada",
            texto = "Al generar una exportación selectiva para transferirla a otra persona de confianza, asígnale una clave de cifrado temporal que puedas compartirle por un canal seguro (como un mensaje efímero cifrado punto a punto)."
        )
    }
}
