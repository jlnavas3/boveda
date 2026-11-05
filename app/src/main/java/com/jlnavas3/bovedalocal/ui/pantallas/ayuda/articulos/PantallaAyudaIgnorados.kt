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
 * Artículo de ayuda sobre la exclusión intencionada de cuentas en la auditoría de salud.
 */
@Composable
fun PantallaAyudaIgnorados(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Ignorar en auditoría",
        alVolver = alVolver,
        textoAccion = "Auditoría de Salud",
        iconoAccion = Icons.Filled.VisibilityOff,
        alEjecutarAccion = { vm.ir(Pantalla.SaludBoveda) }
    ) {
        TarjetaInformativaAyuda(titulo = "Exclusiones legítimas sin falsas alarmas") {
            Text(
                text = "No todas las credenciales de tu bóveda tienen el mismo nivel de criticidad. Excluir ciertas cuentas de la auditoría de salud te permite alcanzar una puntuación del 100% en tu Escudo de Salud sin recibir advertencias innecesarias por cuentas donde la debilidad es intencionada.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Cuándo es adecuado ignorar una cuenta?") {
            Text(
                text = "• Cuentas de prueba y desarrollo: Credenciales en entornos locales (sandbox, localhost, bases de datos de test) con claves simples como 'admin' o 'password123'.\n\n• Dispositivos de red aislados: Routers antiguos, cámaras locales o impresoras de red sin salida a internet donde cambiar la clave rompería la compatibilidad.\n\n• Servicios web con restricciones absurdas: Plataformas que no permiten claves de más de 8 caracteres o que prohíben símbolos especiales, impidiéndote generar una contraseña de alta entropía.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Cómo ignorar o reincorporar una cuenta?") {
            Text(
                text = "• Desde la Auditoría de Salud: En la tarjeta de riesgo de la cuenta, pulsa 'Ignorar'. La advertencia desaparecerá y dejará de penalizar la puntuación global.\n\n• Desde la edición de la entrada: En 'Opciones avanzadas' puedes activar o desactivar el interruptor 'Ignorar en auditoría'.\n\n• Búsqueda rápida: Escribe el comando 'is:ignorado' en el buscador de la lista principal para revisar en cualquier momento todas las cuentas excluidas.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "No ignores cuentas críticas",
            texto = "Nunca uses la opción de ignorar en tu correo electrónico principal, entidades bancarias o redes sociales. Si una de estas cuentas aparece en amarillo o rojo, actualiza su contraseña de inmediato."
        )
    }
}
