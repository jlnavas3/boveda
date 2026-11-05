/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
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
 * Artículo de ayuda sobre el modo señuelo, contraseñas bajo coacción y denegación plausible.
 */
@Composable
fun PantallaAyudaSenuelo(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Bóveda señuelo bajo coacción",
        alVolver = alVolver,
        textoAccion = "Configurar Modo Señuelo",
        iconoAccion = Icons.Filled.VisibilityOff,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesSenuelo()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Denegación plausible ante amenazas") {
            Text(
                text = "En situaciones de asalto, retención ilegal o controles fronterizos invasivos, negarse a abrir la bóveda o destruirla de forma evidente puede poner en peligro tu integridad física. El Modo Señuelo (Duress Mode) ofrece una salida segura aplicando el principio de 'denegación plausible'.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Cómo opera el Modo Señuelo?") {
            Text(
                text = "En Ajustes > Seguridad > Modo señuelo (01-SEG-SEN):\n\n• Contraseña secundaria de coacción: Defines una clave de desbloqueo alternativa (PIN o frase).\n\n• Bóveda simulada creíble: Puedes cargar un catálogo de cuentas ficticias realistas (redes sociales genéricas, cuentas de compras con saldo bajo o claves estándar).\n\n• Desbloqueo transparente: Si eres coaccionado, introduces tu contraseña señuelo. Bóveda Local se abrirá con absoluta normalidad, sin ningún mensaje, banner ni advertencia que indique que está en modo señuelo.\n\n• Aislamiento criptográfico total: Tus verdaderas cuentas bancarias, criptomonedas y secretos permanecen matemáticamente bloqueados e invisibles. La base de datos no contiene índices ni punteros que delaten la existencia de las cuentas reales.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Comportamiento durante la sesión señuelo") {
            Text(
                text = "• Operación normal: El atacante puede navegar, buscar, copiar contraseñas o incluso editar las cuentas señuelo creyendo que tiene acceso completo.\n\n• Bloqueo de exportación real: Las herramientas de respaldo solo exportarán las cuentas simuladas.\n\n• Retorno a la normalidad: Al cerrar la sesión y volver a ingresar tu clave maestra auténtica, recuperarás el acceso completo a tu bóveda real.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Consejo para máxima verosimilitud",
            texto = "Añade entre 8 y 15 cuentas señuelo realistas con nombres de servicios populares (Spotify, Amazon, Instagram) y contraseñas que parezcan auténticas. Una bóveda completamente vacía levantaría sospechas de inmediato."
        )
    }
}
