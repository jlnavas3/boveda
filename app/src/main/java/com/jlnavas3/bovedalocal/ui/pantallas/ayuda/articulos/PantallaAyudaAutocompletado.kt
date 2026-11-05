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
 * Artículo de ayuda exhaustivo sobre el servicio de autocompletado en Android, tarjetas, envíos y reglas heurísticas.
 */
@Composable
fun PantallaAyudaAutocompletado(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Autocompletado del sistema",
        alVolver = alVolver,
        textoAccion = "Configurar Autocompletado",
        iconoAccion = Icons.Filled.AutoAwesome,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesAutocompletado()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Relleno nativo sin fricción en todo el sistema") {
            Text(
                text = "El servicio de autocompletado de Bóveda Local se integra directamente con el Framework de Autocompletado nativo de Android (Android Autofill Framework / Credential Manager). Permite que tus credenciales, medios de pago, direcciones de paquetería y documentos oficiales se rellenen en navegadores y apps con un solo toque y autenticación biométrica.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "1. Credenciales y sugerencias en teclado (IME)") {
            Text(
                text = "• Detección automática: Al tocar un campo de usuario o contraseña en Chrome, Firefox o apps nativas, Bóveda Local identifica el dominio web o el nombre de paquete de la aplicación.\n\n• Sugerencias en teclado (Inline Suggestions): En Android 11 o superior, si utilizas teclados como Gboard o SwiftKey, las cuentas sugeridas aparecen directamente sobre la barra superior del teclado, ofreciendo un acceso ergonómico sin tapar la pantalla.\n\n• Búsqueda manual rápida: Si un formulario tiene un dominio atípico, la sugerencia incluye un botón para buscar en tu bóveda al instante y vincularlo.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "2. Compras online y tarjetas bancarias") {
            Text(
                text = "Al llegar al carrito de compra o pasarela de pago (checkout):\n\n• Número de tarjeta (PAN): Rellena los 16 dígitos formateados con validación Luhn.\n• Fecha de vencimiento: Completa mes y año en el formato que exija la web (MM/AA o MM/AAAA).\n• Código de seguridad: Inserta el CVV / CVC de 3 o 4 dígitos.\n• Titular de la tarjeta: Nombre y apellidos tal como figuran en el plástico de pago.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "3. Direcciones de paquetería y documentos de identidad") {
            Text(
                text = "• Formularios de envío: Rellena calle, número, piso/departamento, ciudad, código postal, provincia, país y teléfono según la ficha de tu perfil activo.\n\n• Documentos oficiales: Rellena números de DNI, Cédula, RUT o Pasaporte en aerolíneas, trámites notariales o gubernamentales.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Reglas heurísticas y chips configurables (04-HER-PSK-RGL)") {
            Text(
                text = "Existen tiendas y páginas web que utilizan identificadores inusuales en sus campos de formulario. En Ajustes > Herramientas > Autocompletado > Reglas de detección puedes afinar y añadir chips de palabras clave para que el motor reconozca campos específicos de tarjetas, direcciones o documentos de cualquier país.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Copia automática de TOTP tras autocompletar",
            texto = "Si la cuenta autocompletada dispone de código 2FA, Bóveda Local copiará automáticamente el token de 6 dígitos en tu portapapeles. Al pulsar 'Siguiente' en el formulario web, solo tendrás que pulsar 'Pegar'."
        )
    }
}
