/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaAdvertenciaAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda sobre el Kit de Emergencia físico imprimible y pautas de custodia offline.
 */
@Composable
fun PantallaAyudaKitEmergencia(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Kit de emergencia físico",
        alVolver = alVolver,
        textoAccion = "Generar Kit de Emergencia",
        iconoAccion = Icons.Filled.Print,
        alEjecutarAccion = { vm.ir(Pantalla.KitEmergencia()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Tu salvavidas físico ante lo imprevisto") {
            Text(
                text = "Dado que Bóveda Local es 100% offline y Zero-Knowledge, no existen servidores externos, ni correos de recuperación, ni soporte técnico capaz de reiniciar tu contraseña. Si olvidas tu clave maestra o pierdes acceso a tu dispositivo, tu único salvavidas es el Kit de Emergencia físico.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Qué incluye el Kit de Emergencia?") {
            Text(
                text = "El generador crea una hoja de rescate con diseño optimizado para impresión en alta definición (PDF):\n\n• Clave maestra y nombre de la bóveda: Tu contraseña principal escrita con tipografía monoespaciada de alta claridad para distinguir caracteres como 1, l, I, 0 u O.\n\n• Ubicación de copias de respaldo: Campos para registrar en qué memorias USB, ordenadores o discos externos almacenas tus archivos .bvda.\n\n• Cuentas críticas prioritarias: Selección de credenciales vitales (correo principal, banco de cabecera) para acceso inmediato en emergencias.\n\n• Instrucciones de restauración: Guía paso a paso para que cualquier familiar o persona de confianza pueda instalar Bóveda Local en un nuevo terminal e importar tu respaldo.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaAdvertenciaAyuda(
            titulo = "Custodia física de máxima seguridad",
            texto = "Nunca guardes el PDF del Kit de Emergencia en servicios de almacenamiento en la nube sin cifrar ni le tomes una foto que pueda sincronizarse con Google Fotos. Imprímelo en papel físico e inmediatamente elimina el archivo temporal generado."
        )

        TarjetaConsejoAyuda(
            titulo = "Dónde custodiar la hoja impresa",
            texto = "Coloca el Kit impreso en un sobre sellado y firmado a través de la solapa. Guárdalo dentro de una caja fuerte ignífuga en tu hogar, en una caja de seguridad bancaria o bajo custodia de un albacea o persona de extrema confianza."
        )
    }
}
