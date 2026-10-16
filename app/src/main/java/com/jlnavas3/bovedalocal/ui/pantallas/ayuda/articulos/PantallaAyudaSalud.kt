/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
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
fun PantallaAyudaSalud(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Salud de la bóveda",
        alVolver = alVolver,
        textoAccion = "Auditar Salud de la Bóveda",
        iconoAccion = Icons.Filled.Shield,
        alEjecutarAccion = { vm.ir(Pantalla.SaludBoveda) }
    ) {
        TarjetaInformativaAyuda(titulo = "Auditoría de seguridad continua") {
            Text(
                text = "La pantalla de Salud analiza todas tus contraseñas en tiempo real (100% en local, sin enviar datos a ningún servidor) para advertirte sobre vulnerabilidades antes de que ocurra una brecha.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Categorías de análisis") {
            Text(
                text = "• Contraseñas vulnerables o débiles: Menos de 8 caracteres o patrones predecibles.\n• Contraseñas reutilizadas: Misma clave usada en múltiples sitios web (un peligro si uno es hackeado).\n• Contraseñas caducadas: Claves que han superado su fecha límite fijada o requieren renovación periódica.\n• Contraseñas muy antiguas: Claves que no se han actualizado en más de 1 año.\n• Cuentas sin segundo factor (2FA): Servicios donde convendría añadir TOTP.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Caducidad y expiración programada") {
            Text(
                text = "Al crear o editar cualquier entrada puedes asignarle una fecha de expiración con accesos rápidos (30 días, 90 días, 6 meses, 1 año o fecha personalizada). La pantalla de Salud cuenta con una pestaña dedicada 'Caducadas' que te avisa con insignias preventivas cuándo es momento de cambiar la contraseña.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Auditoría de filtraciones con Filtro de Bloom offline") {
            Text(
                text = "A diferencia de otros gestores que envían hashes de tus claves a servidores en internet, Bóveda Local incluye un Filtro de Bloom binario precompilado dentro de la propia aplicación. Comprueba al instante si tu contraseña aparece en filtraciones públicas masivas conocidas con cero consumo de datos móviles y absoluta privacidad.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Insignia de escudo en la cabecera") {
            Text(
                text = "En la pantalla principal, el icono de escudo te indica de un vistazo si tu bóveda está protegida (verde) o cuántos problemas requieren tu atención (ámbar/rojo). Al tocarlo accedes directo a resolverlos.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }
    }
}
