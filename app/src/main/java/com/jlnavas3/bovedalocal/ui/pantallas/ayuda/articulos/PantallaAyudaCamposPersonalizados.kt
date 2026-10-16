/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
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
fun PantallaAyudaCamposPersonalizados(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Campos personalizados",
        alVolver = alVolver,
        textoAccion = "Formatos de Campos",
        iconoAccion = Icons.Filled.Tune,
        alEjecutarAccion = { vm.ir(Pantalla.FormatosCampos("03-LST-CAM")) }
    ) {
        TarjetaInformativaAyuda(titulo = "Flexibilidad para cualquier tipo de registro") {
            Text(
                text = "Más allá del usuario y la contraseña estándar, cada entrada de la bóveda puede contener cualquier número de campos personalizados con etiquetas y tipos semánticos específicos adaptados a tus necesidades.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Tipos ricos disponibles") {
            Text(
                text = "• Texto: Para datos visibles como identificadores de cliente, preguntas de seguridad o servidores.\n\n• Oculto / Contraseña: Para tokens de API, respuestas secretas o claves secundarias.\n\n• Booleano (Sí / No): Interruptor directo ideal para estados como '2FA Activado', 'Renovación automática' o 'Dispositivo de confianza'.\n\n• PIN numérico: Máscara específica para códigos numéricos de cajero, tarjetas SIM o cerraduras.\n\n• Fecha: Formato de calendario para vencimiento de licencias, renovaciones o contratos.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Plantillas reutilizables") {
            Text(
                text = "Si sueles añadir la misma estructura de campos a varias entradas (por ejemplo, 'Pregunta 1', 'Pregunta 2', 'PIN'), puedes guardar un conjunto de campos como una Plantilla Reutilizable para insertarla con un solo toque en nuevas entradas.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }
    }
}
