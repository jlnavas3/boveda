/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
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
 * Artículo de ayuda exhaustivo sobre el escáner de códigos QR para 2FA, controles sensoriales y tamaño de ventana.
 */
@Composable
fun PantallaAyudaEscaner(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Escáner QR para 2FA",
        alVolver = alVolver,
        textoAccion = "Ajustes de Cámara",
        iconoAccion = Icons.Filled.QrCodeScanner,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesCamara()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Lectura segura y directa de códigos 2FA") {
            Text(
                text = "Al activar la verificación en dos pasos en cualquier servicio web (Google, GitHub, bancos, redes sociales), se te presenta un código QR que contiene los parámetros criptográficos del token TOTP. El escáner integrado de Bóveda Local lo decodifica de forma instantánea sin intermediarios ni conexión a internet.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Controles en pantalla y experiencia de uso") {
            Text(
                text = "Durante el escaneo dispones de controles táctiles dedicados:\n\n• Linterna / Flash: Para iluminar pantallas o códigos impresos en entornos poco iluminados.\n• Control de Zoom: Acerca la imagen para leer códigos QR pequeños en pantallas lejanas o monitores de alta resolución.\n• Entrada manual en Base32: Si la cámara presenta dificultades físicas de enfoque, puedes pulsar 'Escribir manualmente' para pegar el texto secreto (Base32) que el servicio web ofrece como alternativa al código QR.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Parámetros configurables en Ajustes > Cámara (04-HER-CAM)") {
            Text(
                text = "Puedes adaptar el comportamiento del escáner en Ajustes > Herramientas > Cámara:\n\n• Tamaño de la ventana de escaneo: Deslizador dinámico entre 160 dp y 360 dp para ajustar el marco de captura a las dimensiones de tu teléfono o a tu preferencia visual.\n\n• Respuesta háptica: Siente una vibración breve y nítida en el motor háptico en cuanto el código QR es reconocido con éxito.\n\n• Confirmación auditiva: Emite un suave tono de confirmación sonoro al capturar el token.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Vinculación directa desde el formulario") {
            Text(
                text = "Si abres el escáner desde el botón 'Escanear QR' dentro del formulario de edición de una cuenta, el secreto 2FA, el emisor y el periodo se asocian de inmediato a esa entrada sin pasos adicionales, activando el generador de códigos de 6 dígitos en la ficha del elemento.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Algoritmos avanzados compatibles",
            texto = "El motor decodificador de Bóveda Local admite todos los parámetros del estándar RFC 6238: algoritmos SHA-1, SHA-256 y SHA-512, periodos de 30 o 60 segundos, y longitudes de 6 u 8 dígitos."
        )
    }
}
