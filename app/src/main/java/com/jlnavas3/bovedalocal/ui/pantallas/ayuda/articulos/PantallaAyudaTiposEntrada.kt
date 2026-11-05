/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
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
 * Artículo de ayuda exhaustivo sobre los 9 tipos de datos nativos soportados en Bóveda Local.
 */
@Composable
fun PantallaAyudaTiposEntrada(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Tipos de entradas y datos nativos",
        alVolver = alVolver,
        textoAccion = "Ver Lista Principal",
        iconoAccion = Icons.Filled.GridView,
        alEjecutarAccion = { vm.ir(Pantalla.Lista) }
    ) {
        TarjetaInformativaAyuda(titulo = "Un gestor para toda tu vida digital") {
            Text(
                text = "Bóveda Local va mucho más allá de las contraseñas web. Cuenta con 9 modelos de datos de primera clase con formularios semánticos dedicados, validaciones de integridad, máscaras de privacidad y formateadores visuales específicos para cada necesidad.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "1. Cuentas de usuario y finanzas") {
            Text(
                text = "• Inicios de sesión (Login): Usuario, correo, clave, URL de acceso, token TOTP de 6 dígitos principal y secundario, passkeys vinculadas y notas.\n\n• Tarjetas bancarias: Número PAN de 16 o 15 dígitos formateado en bloques de 4 con verificación del algoritmo Luhn; fecha de vencimiento (mes/año); CVV/CVC; PIN de cajero y emisor visual automático (Visa, Mastercard, Amex, etc.). Permite autocompletar checkouts web con un solo toque.\n\n• Cuentas bancarias: Código IBAN internacional formateado, código BIC/SWIFT, titular de la cuenta y nombre de la entidad para transferencias inmediatas sin buscar documentos físicos.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "2. Identidad y conectividad") {
            Text(
                text = "• Documentos de identidad: Registro de DNI, Cédula de Identidad, RUT, Pasaporte, Licencia de conducir, Seguridad Social o documentos personalizados. Almacena número, fecha de expedición y caducidad para trámites o billetes de avión.\n\n• Redes Wi-Fi: Nombre de red (SSID), clave de acceso y tipo de cifrado (WPA2, WPA3, WEP). Incluye botón directo para copiar la contraseña o conectar el teléfono al router sin teclear.\n\n• Servidores y Homelab: Dirección IP o hostname, puerto de conexión (22, 2222, 8080, etc.), usuario, contraseña y clave SSH asimétrica vinculable.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "3. Criptomonedas, contactos y notas seguras") {
            Text(
                text = "• Billeteras Cripto (Wallets): Nombre de la red (Bitcoin, Ethereum, Solana, etc.), dirección pública para recibir fondos (con botón de copia), clave privada y frase semilla (Seed Phrase de 12 o 24 palabras) cifradas bajo llave.\n\n• Contactos y Direcciones: Nombre completo, empresa, teléfonos (móvil, fijo), correo electrónico y dirección postal (calle, número, código postal, ciudad, país), integrada con el autocompletado de tiendas online.\n\n• Notas seguras: Texto libre cifrado con soporte completo para formato Markdown enriquecido (encabezados, negrita, listas, bloques de código y tablas estructuradas).",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Filtro rápido por tipos en el listado") {
            Text(
                text = "En la parte superior de la pantalla principal dispones de la píldora de tipos de entrada. Al tocarla, puedes filtrar instantáneamente tu bóveda para ver solo tarjetas bancarias, solo redes Wi-Fi, solo documentos o cualquier tipo específico sin mezclar el resto.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Máscaras de seguridad y privacidad",
            texto = "Todos los campos sensibles (contraseñas, CVV, PINs de cajero, semillas cripto) se muestran ofuscados con puntos (••••). Pulsa el icono del ojo para desocultar temporalmente el dato en pantalla."
        )
    }
}
