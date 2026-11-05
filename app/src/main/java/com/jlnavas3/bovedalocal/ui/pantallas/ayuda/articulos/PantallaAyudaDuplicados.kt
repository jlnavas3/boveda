/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
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
 * Artículo de ayuda exhaustivo sobre el detector multimodelo de duplicados en contraseñas, tarjetas y documentos.
 */
@Composable
fun PantallaAyudaDuplicados(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Detección de duplicados",
        alVolver = alVolver,
        textoAccion = "Ver Duplicados",
        iconoAccion = Icons.Filled.ContentCopy,
        alEjecutarAccion = { vm.ir(Pantalla.Duplicados) }
    ) {
        TarjetaInformativaAyuda(titulo = "El mayor riesgo en ciberseguridad") {
            Text(
                text = "El 65% de los usuarios reutiliza la misma contraseña en múltiples plataformas por comodidad. Si un foro o tienda online pequeña sufre una filtración, los cibercriminales usarán automáticamente esa combinación de correo y clave en bancos, correo principal y redes sociales mediante ataques masivos de relleno de credenciales (Credential Stuffing).\n\nEliminar contraseñas repetidas es la medida preventiva más eficaz que puedes tomar hoy.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Detector multimodelo inteligente") {
            Text(
                text = "Bóveda Local no solo busca contraseñas de texto repetidas; audita la totalidad de tus modelos nativos:\n\n• Contraseñas idénticas: Agrupa todas las cuentas que comparten la misma clave secreta.\n\n• Tarjetas bancarias duplicadas: Detecta si tienes guardado el mismo número de tarjeta (PAN) en distintas entradas creadas en diferentes momentos.\n\n• Cuentas bancarias duplicadas: Compara códigos IBAN para evitar duplicidad de registros financieros.\n\n• Documentos oficiales: Detecta identificaciones (DNI, Cédula, Pasaporte) con números repetidos.\n\n• Mismo usuario en idéntico servicio: Identifica si tienes dos registros creados para el mismo sitio web con igual usuario pero distinta información dispersa.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Agrupación visual clara y resolución") {
            Text(
                text = "En la pantalla de Duplicados, las cuentas se muestran agrupadas en tarjetas desplegables por contraseña compartida:\n\n• Puedes ver con un toque qué servicios están en peligro mutuo.\n• Incluye el botón 'Generar nueva clave' para actualizar cada cuenta por separado.\n• Si las entradas son duplicados de un mismo servicio, puedes lanzar el 'Asistente de Unificación Inteligente' para fusionarlas en un único registro perfecto.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Prioriza las cuentas puente",
            texto = "Si tienes una contraseña duplicada entre un sitio secundario y tu cuenta de Google, Apple o Microsoft, cambia de inmediato la cuenta principal: controlar tu correo da acceso al restablecimiento de todas las demás cuentas."
        )
    }
}
