/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
 * Artículo de ayuda exhaustivo sobre perfiles de bóveda, identidades y ficha de autocompletado.
 */
@Composable
fun PantallaAyudaIdentidades(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Perfiles de bóveda e identidades",
        alVolver = alVolver,
        textoAccion = "Gestionar Perfiles",
        iconoAccion = Icons.Filled.AccountCircle,
        alEjecutarAccion = { vm.ir(Pantalla.Perfiles()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Separa tus entornos digitales sin fricción") {
            Text(
                text = "Un Perfil representa un ámbito de tu vida (por ejemplo: 'Personal', 'Trabajo', 'Estudios' o 'Proyectos'). En lugar de mezclar tus contraseñas de ocio con las corporativas, los perfiles te permiten aislar visualmente tus credenciales manteniendo todo en una única bóveda cifrada.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Ficha ejecutiva: Autocompletado y pagos") {
            Text(
                text = "Cada perfil incluye una tarjeta de configuración inteligente donde vinculas tus elementos predeterminados para ese entorno:\n\n• Tarjeta bancaria: Tu tarjeta corporativa para el perfil 'Trabajo' y tu tarjeta de débito habitual para 'Personal'. En compras web, el autocompletado sugerirá la tarjeta correspondiente.\n\n• Dirección de envío / Contacto: Tu dirección postal de casa para paquetes personales o la dirección de la oficina para envíos profesionales.\n\n• Cuenta bancaria: Cuenta asociada para transferencias o facturación.\n\n• Documento de identidad: Tu DNI, Cédula, RUT o Pasaporte prioritario para trámites oficiales y compras aéreas.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Preferencia de identificador de usuario") {
            Text(
                text = "En la ficha del perfil puedes elegir si el motor de autocompletado debe priorizar tu Correo Electrónico o tu Documento de Identidad (DNI/RUT/Cédula) al interactuar con formularios web que soliciten uno u otro, evitando tener que corregir manualmente el campo sugerido.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Paleta de 19 colores e insignias visuales") {
            Text(
                text = "• Colores distintivos: Asigna uno de los 19 colores de la paleta temática a cada perfil para reconocer instantáneamente la pertenencia de una cuenta en el listado.\n\n• Píldora de perfiles en la lista principal: Pulsa la píldora '[ 👤 Perfiles ]' en la barra superior para alternar con un toque entre 'Personal', 'Trabajo' o 'Todos', filtrando la vista al instante.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Asignación rápida al crear cuentas",
            texto = "Al crear cualquier nueva credencial, selecciona su perfil en la parte superior del formulario. Si estás filtrando la lista por 'Trabajo', las nuevas cuentas se asociarán automáticamente a ese perfil sin pasos adicionales."
        )
    }
}
