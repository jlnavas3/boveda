/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallMerge
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
 * Artículo de ayuda exhaustivo sobre el asistente de unificación inteligente y resolución de discrepancias.
 */
@Composable
fun PantallaAyudaUnificacion(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Unificación y fusión inteligente",
        alVolver = alVolver,
        textoAccion = "Ver Duplicados para Unificar",
        iconoAccion = Icons.Filled.CallMerge,
        alEjecutarAccion = { vm.ir(Pantalla.Duplicados) }
    ) {
        TarjetaInformativaAyuda(titulo = "¿Por qué unificar en lugar de borrar?") {
            Text(
                text = "Al importar datos desde diferentes navegadores o respaldos antiguos, es muy frecuente terminar con entradas duplicadas de un mismo servicio. Sin embargo, borrar una de ellas a ciegas es peligroso: una entrada puede tener la contraseña más reciente, pero la otra puede guardar el código 2FA/TOTP o notas con preguntas de seguridad.\n\nEl Asistente de Unificación Inteligente fusiona ambas entradas en un único registro perfecto sin perder ningún dato.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Comparador de discrepancias campo a campo") {
            Text(
                text = "El asistente analiza ambas entradas y te presenta una interfaz comparativa intuitiva:\n\n• Si los datos coinciden (mismo usuario y misma URL), se unifican automáticamente.\n• Si hay conflicto (por ejemplo, dos contraseñas distintas), el asistente te muestra ambas opciones destacando la fecha de última modificación de cada una para que elijas cuál conservar con un solo toque.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Fusión sin pérdida (Lossless Merge)") {
            Text(
                text = "Bóveda Local aplica una política estricta de preservación de información:\n\n• Notas combinadas: Las notas adicionales de ambas cuentas se concatenan cronológicamente para no extraviar apuntes antiguos.\n• Etiquetas unificadas: Se conservan todas las etiquetas (tags) asignadas en ambas cuentas.\n• Campos personalizados y adjuntos: Si una entrada tenía un archivo adjunto o un PIN personalizado, se integra sin borrarse.\n• Claves anteriores al historial: La contraseña descartada se archiva automáticamente en el historial de la cuenta final.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Protección de papelera") {
            Text(
                text = "Tras confirmar la unificación, la entrada redundante no se elimina de forma destructiva del disco: se envía a la Papelera de Reciclaje con retención protegida por si en el futuro necesitaras consultarla o restaurarla.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Lanza la unificación desde Duplicados",
            texto = "En la pantalla de Duplicados, cuando dos cuentas compartan usuario o servicio, verás un botón azul 'Unificar'. Púlsalo para iniciar el asistente guiado."
        )
    }
}
