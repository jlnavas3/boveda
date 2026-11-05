/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
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
 * Artículo de ayuda exhaustivo sobre el autenticador TOTP 2FA integrado, soporte multi-token y filtros rápidos.
 */
@Composable
fun PantallaAyudaAutenticador(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Autenticador 2FA / TOTP",
        alVolver = alVolver,
        textoAccion = "Abrir Autenticador",
        iconoAccion = Icons.Filled.Timer,
        alEjecutarAccion = { vm.ir(Pantalla.Autenticador) }
    ) {
        TarjetaInformativaAyuda(titulo = "Segundo factor de autenticación sin nube") {
            Text(
                text = "La verificación en dos pasos (2FA) añade una capa crítica de defensa: incluso si un atacante descubre tu contraseña, no podrá acceder sin el código temporal de 6 dígitos que rota cada 30 segundos.\n\nA diferencia de apps como Google Authenticator o Microsoft Authenticator que pueden sincronizar datos en servidores externos o perderse al cambiar de móvil sin respaldo, en Bóveda Local tus secretos TOTP se custodian cifrados junto a tus contraseñas dentro de tu base de datos local.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Compatibilidad total con el estándar RFC 6238") {
            Text(
                text = "El motor criptográfico de Bóveda Local calcula los códigos HMAC en tiempo real soportando:\n\n• Algoritmos hash: SHA-1 (estándar mayoritario), SHA-256 y SHA-512 para servicios de alta seguridad.\n• Periodos de rotación: Ventanas clásicas de 30 segundos o ventanas de 60 segundos.\n• Longitud de código: Claves habituales de 6 dígitos o códigos extendidos de 8 dígitos.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Soporte para múltiples códigos TOTP (Tokens Secundarios)") {
            Text(
                text = "Existen servicios corporativos o plataformas de infraestructura que solicitan dos factores distintos (por ejemplo, un token de acceso a la VPN empresarial y otro para el portal interno dentro de la misma cuenta).\n\nBóveda Local te permite registrar un TOTP principal y tokens TOTP secundarios en una única ficha de cuenta, mostrando cada código con su etiqueta distintiva sin necesidad de crear entradas duplicadas.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Píldora de filtro rápido en la lista principal") {
            Text(
                text = "En la barra superior de píldoras del listado principal encontrarás el filtro rápido '⏱️ 2FA (X)'. Al tocarlo, la lista se filtra al instante mostrando únicamente aquellas cuentas que tienen códigos 2FA activos, con el código visible y un botón de copia inmediata a un toque.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Copia automática al autocompletar",
            texto = "Activa en Ajustes > Herramientas > Autocompletado la opción 'Copiar TOTP automáticamente': al rellenar tu usuario y clave en una web, el código 2FA se colocará automáticamente en tu portapapeles listo para pegar en el siguiente paso."
        )
    }
}
