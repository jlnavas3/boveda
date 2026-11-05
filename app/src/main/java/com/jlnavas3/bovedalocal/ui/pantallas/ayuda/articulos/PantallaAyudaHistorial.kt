/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
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
 * Artículo de ayuda exhaustivo sobre el historial de contraseñas modificadas y claves generadas.
 */
@Composable
fun PantallaAyudaHistorial(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Historial de contraseñas",
        alVolver = alVolver,
        textoAccion = "Ajustes de Historial",
        iconoAccion = Icons.Filled.History,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesHistorial()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Red de seguridad ante cambios accidentales") {
            Text(
                text = "Modificar una contraseña en una cuenta web a menudo presenta contratiempos: un error de red en el servidor del servicio, un reinicio inesperado o una sesión bloqueada pueden dejarte sin acceso si sobreescribiste tu contraseña antigua antes de tiempo.\n\nEl sistema de historial de Bóveda Local actúa como una red de seguridad infalible para rescatar cualquier versión anterior.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Historial automático por entrada") {
            Text(
                text = "Cada vez que editas y guardas una contraseña existente en cualquier credencial:\n\n• Registro automático: El valor anterior se archiva de inmediato en una lista histórica interna con la fecha y hora exacta del cambio.\n• Ficha de detalle: En la parte inferior de la entrada puedes desplegar la sección 'Historial de contraseñas'.\n• Opciones directas: Puedes visualizar la clave antigua con el icono del ojo, copiarla al portapapeles con un toque o pulsar 'Restaurar' para devolverla como contraseña activa.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Historial global de claves generadas") {
            Text(
                text = "En Ajustes > Herramientas > Historial (04-HER-HST):\n\n• Registro del generador: Almacena temporalmente las contraseñas producidas en la herramienta Generador o mediante el widget de escritorio.\n• Rescate sin entrada: Si generaste una clave robusta pero olvidaste crear la entrada en tu bóveda, puedes rescatarla desde este panel durante los siguientes días.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Privacidad y purga del historial") {
            Text(
                text = "Tú tienes el control absoluto de qué se almacena:\n\n• Purga individual: Puedes eliminar entradas específicas del historial de una cuenta concreta.\n• Limpieza global: En Ajustes de Historial puedes vaciar de golpe todo el registro de contraseñas antiguas de la bóveda para garantizar que claves retiradas hace años no permanezcan en disco.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Audita la antigüedad de tus claves",
            texto = "El historial te permite comprobar cuándo cambiaste tu contraseña por última vez, ayudándote a identificar servicios críticos (como correo principal o banca) cuyas credenciales no hayan sido renovadas en periodos prolongados."
        )
    }
}
