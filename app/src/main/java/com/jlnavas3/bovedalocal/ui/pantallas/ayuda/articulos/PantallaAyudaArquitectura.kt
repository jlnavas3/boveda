/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
fun PantallaAyudaArquitectura(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Arquitectura Zero-Knowledge",
        alVolver = alVolver,
        textoAccion = "Diagnóstico de seguridad",
        iconoAccion = Icons.Filled.Lock,
        alEjecutarAccion = { vm.ir(Pantalla.AcercaDe()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Privacidad absoluta por diseño") {
            Text(
                text = "Bóveda Local no requiere conexión a internet, no cuenta con servidores remotos, no recopila telemetría y no tiene publicidad. El permiso 'android.permission.INTERNET' ni siquiera existe en el manifiesto de la aplicación.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Criptografía y software libre") {
            Text(
                text = "• Algoritmos: Cifrado simétrico AES-256-GCM con autenticación criptográfica contra manipulación y derivación mediante PBKDF2.\n\n• Compresión previa: Los registros se empaquetan en binario comprimido con cabecera BLZ1 antes de ser cifrados.\n\n• Licencia GNU GPLv3: Código abierto auditable y transparente para garantizar que no existan puertas traseras.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
