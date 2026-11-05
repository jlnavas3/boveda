/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
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
 * Artículo de ayuda sobre el generador de alias de correo electrónico (RFC 5233) y delimitadores dinámicos.
 */
@Composable
fun PantallaAyudaAliasCorreo(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Alias de correo (@)",
        alVolver = alVolver,
        textoAccion = "Configurar en Generador",
        iconoAccion = Icons.Filled.AlternateEmail,
        alEjecutarAccion = { vm.ir(Pantalla.Generador) }
    ) {
        TarjetaInformativaAyuda(titulo = "¿Por qué usar alias de correo?") {
            Text(
                text = "Usar tu dirección de correo electrónico real en decenas de aplicaciones y tiendas online facilita que empresas de publicidad rastreen tus compras y que, ante una brecha de seguridad, tu buzón sea blanco de phishing y correo no deseado (spam).\n\nCon los alias de correo de Bóveda Local, cada servicio recibe una dirección única mientras todos los mensajes continúan llegando a tu bandeja de entrada habitual sin crear cuentas adicionales.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "El estándar de subdireccionamiento (RFC 5233)") {
            Text(
                text = "La inmensa mayoría de proveedores modernos (Gmail, Proton, Outlook, Fastmail, Apple iCloud) soportan subdireccionamiento automático:\n\n• Si tu correo es 'usuario@gmail.com', cualquier correo enviado a 'usuario+netflix@gmail.com' entra directamente a tu buzón.\n• Si recibes spam en ese alias, sabrás exactamente que Netflix (o la empresa receptora) compartió o filtró tus datos, pudiendo crear una regla de filtrado en tu buzón para bloquearlo de inmediato.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Delimitadores y parámetros configurables") {
            Text(
                text = "En Ajustes > Herramientas > Generador puedes configurar:\n\n• Correo base: Tu dirección de correo real donde recibirás los mensajes.\n\n• Delimitador personalizable: Algunos sitios web tienen formularios mal programados que rechazan el símbolo '+'. Puedes elegir entre:\n  - Más (+): Estándar oficial RFC 5233.\n  - Guion (-): Ideal para sitios que prohíben signos especiales.\n  - Punto (.): Alternativa discreta.\n\n• Longitud de sufijo aleatorio: Define la longitud del código aleatorio entre 4 y 12 caracteres (ej: 'usuario+k8x2@gmail.com').",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "El botón '@' en el formulario de edición") {
            Text(
                text = "Al crear o editar una credencial, verás un botón '@' a la derecha del campo 'Usuario o correo':\n\n• Si tocas '@' y la entrada tiene un título o URL (ej: Spotify), genera automáticamente: 'tu_correo+spotify@dominio.com'.\n• Si vuelves a tocar, genera un alias con sufijo aleatorio único.\n• Puedes alternar entre ambos modos con un solo toque sin teclear manualmente.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Crea reglas de filtrado en tu proveedor",
            texto = "En tu cliente de correo (como Gmail o Proton Mail), puedes crear filtros que apliquen etiquetas automáticamente según el alias (ej: todo lo que llegue a '+compras' se etiqueta en 'Facturas'), manteniendo tu bandeja impecable."
        )
    }
}
