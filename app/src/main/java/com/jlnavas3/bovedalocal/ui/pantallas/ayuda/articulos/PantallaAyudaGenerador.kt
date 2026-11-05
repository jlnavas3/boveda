/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
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
 * Artículo de ayuda sobre el generador de contraseñas aleatorias, frases Diceware y cálculo de entropía.
 */
@Composable
fun PantallaAyudaGenerador(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Generador de contraseñas",
        alVolver = alVolver,
        textoAccion = "Abrir Generador",
        iconoAccion = Icons.Filled.AutoAwesome,
        alEjecutarAccion = { vm.ir(Pantalla.Generador) }
    ) {
        TarjetaInformativaAyuda(titulo = "Criptografía aleatoria impredecible") {
            Text(
                text = "Reutilizar contraseñas o emplear patrones basados en fechas o nombres es la causa del 80% de los hackeos de cuentas en internet. El Generador de Bóveda Local utiliza el generador de números pseudoaleatorios criptográficamente seguro de Android (SecureRandom) para producir claves con entropía máxima que los ordenadores no pueden predecir.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Modo 1: Contraseñas aleatorias fuertes") {
            Text(
                text = "Ideal para cuentas web, banca digital y servicios donde no necesitas memorizar la clave:\n\n• Longitud regulable: De 8 a 64 caracteres.\n• Conjuntos de caracteres: Activa o desactiva a tu gusto Mayúsculas (A-Z), Minúsculas (a-z), Números (0-9) y Símbolos especiales (!@#$%^&*).\n• Exclusión de caracteres ambiguos: Opcionalmente descarta caracteres visualmente idénticos (como 'l', '1', 'I', 'O', '0') para evitar confusiones al transcribir claves manualmente.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Modo 2: Frases Diceware (Fáciles de recordar)") {
            Text(
                text = "Diseñado para contraseñas maestras, claves de cifrado o inicios de sesión diarios en teclados físicos:\n\n• ¿Cómo funciona?: En lugar de símbolos caóticos difíciles de memorizar, concatena palabras del diccionario elegidas al azar (ej: 'reloj-bosque-planeta-arena').\n• Longitud por palabras: Selecciona de 3 a 8 palabras.\n• Separador configurable: Guion (-), punto (.), espacio o números aleatorios interpuestos.\n• Seguridad matemática: 5 palabras aleatorias ofrecen más de 65 bits de entropía, haciéndolas prácticamente imposibles de adivinar mediante fuerza bruta.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Medidor de entropía y tiempo de crackeo") {
            Text(
                text = "Bóveda Local evalúa en tiempo real la robustez matemática de cada clave generada:\n\n• Entropía en bits: Mide el espacio de combinaciones posibles.\n• Clasificación visual: Muy débil, Débil, Buena, Fuerte o Invulnerable.\n• Estimación temporal: Muestra cuánto tardaría un atacante con clusters de tarjetas gráficas modernas en descifrarla.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Historial local de claves generadas") {
            Text(
                text = "¿Generaste una contraseña, la pegaste en una página web pero cerraste la pestaña antes de guardarla en Bóveda Local? En Ajustes > Historial > Claves generadas puedes consultar las últimas contraseñas producidas para rescatarlas al instante.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Recomendación para tu contraseña maestra",
            texto = "Utiliza una frase Diceware de 5 o 6 palabras separadas por guiones para tu clave maestra de Bóveda Local. Será cómoda de escribir con los pulgares y ofrecerá resistencia contra ataques por supercomputación."
        )
    }
}
