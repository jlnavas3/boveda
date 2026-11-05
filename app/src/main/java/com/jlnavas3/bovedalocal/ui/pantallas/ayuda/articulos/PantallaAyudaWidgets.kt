/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Widgets
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
 * Artículo de ayuda exhaustivo sobre widgets de escritorio, visualizador TOTP dinámico y Quick Settings Tile.
 */
@Composable
fun PantallaAyudaWidgets(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Widgets y accesos rápidos",
        alVolver = alVolver,
        textoAccion = "Configurar Mosaico Tile",
        iconoAccion = Icons.Filled.Widgets,
        alEjecutarAccion = { vm.ir(Pantalla.TileConfiguracion()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Acceso ultrarrápido desde tu pantalla de inicio") {
            Text(
                text = "Bóveda Local cuenta con componentes nativos de escritorio para Android que te permiten generar claves o consultar tokens 2FA en una fracción de segundo sin tener que abrir y navegar por la aplicación completa.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "1. Widget 1x1: Generador instantáneo") {
            Text(
                text = "Un botón táctil minimalista del tamaño de un icono de aplicación:\n\n• Al pulsarlo, produce al instante una contraseña de alta entropía según tus preferencias guardadas (longitud, símbolos, números) y la coloca directamente en tu portapapeles con una confirmación háptica.\n• Ideal si necesitas generar claves sobre la marcha mientras creas cuentas en tu ordenador o navegas en el móvil.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "2. Widget Dinámico de Códigos 2FA / TOTP") {
            Text(
                text = "Un widget interactivo de tamaño adaptable para la pantalla de inicio:\n\n• Muestra tus códigos temporales de 6 dígitos favoritos con su nombre y cuenta regresiva circular en tiempo real.\n• Cuenta con un botón de copia individual en cada fila.\n• Seguridad biométrica: Si la bóveda está bloqueada, el widget ofusca los códigos y solicita tu huella dactilar de forma no invasiva antes de revelarlos, protegiéndote contra miradas indiscretas cuando tu móvil esté sobre una mesa.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "3. Quick Settings Tile (Ajustes rápidos en la barra de estado)") {
            Text(
                text = "En el panel desplegable de notificaciones y ajustes rápidos de Android puedes agregar el mosaico (Tile) de Bóveda Local:\n\n• Desliza dos veces hacia abajo desde la parte superior de la pantalla en tu teléfono.\n• Toca el lápiz de edición de mosaicos y arrastra el icono de 'Bóveda Local'.\n• Ahora podrás invocar el generador de contraseñas o el escáner de códigos QR desde cualquier juego, aplicación o pantalla sin interrumpir lo que estés haciendo.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Personaliza el tamaño del widget TOTP",
            texto = "Mantén presionado el widget TOTP en tu pantalla de inicio de Android para arrastrar sus bordes y mostrar desde 2 hasta 6 servicios simultáneos con scroll vertical suave."
        )
    }
}
