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
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PantallaAyudaAutocompletado(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Autocompletado del sistema",
        alVolver = alVolver,
        textoAccion = "Configurar Autocompletado",
        iconoAccion = Icons.Filled.AutoAwesome,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesAutocompletado()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Relleno automático en navegadores y apps") {
            Text(
                text = "El servicio de autocompletado nativo de Android permite que, al tocar un campo de usuario o contraseña en Chrome, Firefox o cualquier aplicación bancaria, aparezca una sugerencia flotante de Bóveda Local para rellenarlo al instante.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "¿Cómo activarlo en tu dispositivo?") {
            Text(
                text = "1. Ve a Ajustes > Herramientas > Autocompletado dentro de Bóveda Local.\n\n2. Pulsa 'Activar en ajustes de Android' para ser redirigido a la sección de 'Servicio de Autocompletado' del sistema.\n\n3. Selecciona 'Bóveda Local' como tu proveedor predeterminado.\n\n4. A partir de ese momento, cada vez que inicies sesión en una app, se te pedirá autenticarte con tu huella y tus datos se completarán con total seguridad sin copiarlos manualmente.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }
    }
}
