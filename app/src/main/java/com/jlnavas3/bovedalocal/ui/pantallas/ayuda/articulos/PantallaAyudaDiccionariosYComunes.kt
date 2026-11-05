/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
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
 * Artículo de ayuda exhaustivo sobre diccionarios Diceware y la lista negra offline de contraseñas comunes.
 */
@Composable
fun PantallaAyudaDiccionariosYComunes(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Diccionarios y contraseñas comunes",
        alVolver = alVolver,
        textoAccion = "Ver Contraseñas Comunes",
        iconoAccion = Icons.Filled.ListAlt,
        alEjecutarAccion = { vm.ir(Pantalla.ContrasenasComunes()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Diccionarios Diceware de alta calidad") {
            Text(
                text = "Para generar frases de contraseña (passphrases) sólidas, Bóveda Local incluye catálogos de palabras filtradas de forma estricta:\n\n• Palabras seleccionadas: Vocabularios de más de 7.776 términos cuidadosamente elegidos para garantizar legibilidad, evitar palabras fonéticamente confusas y asegurar un espacio combinatorio óptimo.\n• Longitud balanceada: Palabras de longitud media que no saturan los teclados móviles pero ofrecen máxima riqueza matemática.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Lista negra offline de contraseñas vulnerables") {
            Text(
                text = "Millones de ataques automatizados en internet no intentan adivinar caracteres al azar: prueban diccionarios de claves filtradas en brechas históricas conocidas (como RockYou, SecLists y vertederos de credenciales públicas).\n\nBóveda Local incorpora una base de datos compacta y optimizada con las contraseñas más inseguras y frecuentes del mundo.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Privacidad 100% offline: Sin consultas a servidores") {
            Text(
                text = "A diferencia de otros gestores o extensiones que envían hashes de tus contraseñas por internet a servicios externos como HaveIBeenPwned (arriesgando que tus claves sean interceptadas por terceros):\n\n• La comprobación en Bóveda Local se ejecuta 100% en la memoria de tu propio dispositivo.\n• Ni una sola letra ni un hash sale jamás de tu teléfono.\n• Al escribir una contraseña en el formulario de edición, si coincide con una clave común de la lista negra, recibirás una advertencia instantánea en milisegundos.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Explorador de contraseñas comunes (01-SEG-COM)") {
            Text(
                text = "Desde Ajustes > Seguridad > Contraseñas comunes puedes explorar la lista completa clasificada alfabéticamente para comprobar si alguna clave que utilizabas habitualmente en el pasado figura entre las contraseñas más vulnerables del planeta.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Regla de oro ante advertencias",
            texto = "Si la auditoría de Bóveda Local marca una de tus contraseñas como 'Común o Vulnerable', cámbiala de inmediato en el servicio correspondiente: los atacantes la probarán en los primeros segundos de cualquier intento de intrusión."
        )
    }
}
