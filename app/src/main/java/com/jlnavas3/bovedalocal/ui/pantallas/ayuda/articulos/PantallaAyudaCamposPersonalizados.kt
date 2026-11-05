/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DynamicForm
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
 * Artículo de ayuda exhaustivo sobre campos personalizados, tipos ricos y plantillas reutilizables.
 */
@Composable
fun PantallaAyudaCamposPersonalizados(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Campos personalizados y tipos ricos",
        alVolver = alVolver,
        textoAccion = "Gestionar Plantillas",
        iconoAccion = Icons.Filled.DynamicForm,
        alEjecutarAccion = { vm.ir(Pantalla.PlantillasCampos()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Flexibilidad total para cualquier registro") {
            Text(
                text = "No todas las cuentas encajan en el esquema rígido de 'usuario y contraseña'. A menudo necesitas custodiar datos específicos como preguntas secretas de seguridad, códigos PUK de tu tarjeta SIM, tokens de API de desarrollo o fechas de renovación de contratos.\n\nEl sistema de campos personalizados de Bóveda Local te permite agregar cualquier número de propiedades a cualquier entrada con soporte para tipos de datos ricos.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Catálogo de tipos de campo disponibles") {
            Text(
                text = "Al pulsar '+ Añadir campo personalizado' en el formulario de edición, puedes seleccionar:\n\n• Texto simple: Para respuestas de seguridad, nombres de servidor, números de cliente o notas breves.\n\n• Oculto / Contraseña: Campo enmascarado con puntos ('••••') y botón de visualización con ojo. Se beneficia de la limpieza automática de portapapeles.\n\n• Código PIN: Campo numérico especializado para PINs de tarjetas SIM, alarmas domésticas o códigos de desbloqueo.\n\n• Booleano (Interruptor Sí/No): Un conmutador interactivo visual para marcar estados binarios (ej: '¿Requiere VPN?', '¿Facturación anual activada?', '¿Cuenta verificada?').\n\n• Fecha: Selector de calendario nativo para registrar fechas de vencimiento de pólizas, garantías de compra o renovaciones de dominio.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Plantillas reutilizables del sistema y de usuario") {
            Text(
                text = "En Ajustes > Apariencia > Plantillas de campos (02-APA-PLT) puedes diseñar plantillas preconcebidas:\n\n• Plantillas del sistema: Configuraciones listas para usar orientadas a servidores, banca o tarjetas prepago.\n\n• Plantillas personalizadas: Si gestionas muchos servidores o tiendas virtuales, crea una plantilla con los 4 o 5 campos que siempre necesitas (ej: 'IP', 'Puerto SSH', 'Usuario Root', 'Proveedor VPS'). Al crear una nueva cuenta, pulsa 'Aplicar plantilla' y todos los campos se insertarán automáticamente.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Reordenación táctil de campos",
            texto = "En la ficha de edición puedes arrastrar los campos personalizados verticalmente para colocar la información más relevante en la parte superior."
        )
    }
}
