/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
 * Artículo de ayuda sobre la arquitectura Zero-Knowledge, diseño offline y estándares criptográficos.
 */
@Composable
fun PantallaAyudaArquitectura(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Arquitectura Zero-Knowledge",
        alVolver = alVolver,
        textoAccion = "Acerca de Bóveda Local",
        iconoAccion = Icons.Filled.Info,
        alEjecutarAccion = { vm.ir(Pantalla.AcercaDe()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Privacidad absoluta por diseño físico") {
            Text(
                text = "La gran mayoría de gestores de contraseñas comerciales alojan tus secretos en servidores ajenos ('la nube'), exponiéndote a brechas masivas, filtraciones de credenciales maestras y cambios unilaterales en los términos de servicio.\n\nBóveda Local adopta un paradigma radicalmente opuesto: la soberanía de datos local absoluta.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "1. Cero Nube: Red exclusivamente local (P2P)") {
            Text(
                text = "• En el manifiesto de la aplicación el permiso android.permission.INTERNET se utiliza exclusivamente para sincronización directa entre dispositivos en la misma red local (LAN).\n• Comunicación protegida por túnel cifrado ChaCha20-Poly1305 con autenticación mutua y código de huella visual.\n• Cero servidores centrales, cero nube externa: ninguna contraseña o dato sale de tus dispositivos.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "2. Cero telemetría y cero rastreadores") {
            Text(
                text = "• No incluye Google Analytics, Firebase, Crashlytics, Mixpanel ni bibliotecas de publicidad o análisis de uso.\n• Tu comportamiento, tiempos de sesión, cantidad de cuentas o hábitos de uso son completamente privados e invisibles para cualquier entidad externa, incluidos los propios desarrolladores.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "3. Estándares criptográficos de grado militar") {
            Text(
                text = "• Base de datos SQLCipher: Cada página física de 4096 bytes en la base de datos se cifra de forma independiente mediante AES-256 en modo CBC/GCM con HMAC-SHA512 para garantizar la autenticidad e integridad contra manipulaciones.\n\n• Derivación de claves con Argon2id: Estándar criptográfico puntero que combina resistencia contra ataques de canal lateral en memoria y ataques por fuerza bruta en GPU/ASIC.\n\n• Limpieza de memoria (Zeroing): Las claves maestras se custodian en arrays de bytes efímeros que se sobreescriben activamente con ceros al cerrar la sesión o bloquear la aplicación.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "4. Licencia libre y código abierto (GPLv3)") {
            Text(
                text = "Bóveda Local es Software Libre bajo licencia GNU General Public License v3.0. Cualquier persona, auditor de seguridad o investigador independiente puede examinar línea por línea el código fuente para verificar la ausencia de puertas traseras y validar el rigor de la implementación.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Audita los permisos en los ajustes de tu móvil",
            texto = "Puedes comprobar en los Ajustes de Aplicaciones de tu teléfono que Bóveda Local no solicita ni posee permisos de acceso a Internet ni a la red, confirmando la garantía de aislamiento local."
        )
    }
}
