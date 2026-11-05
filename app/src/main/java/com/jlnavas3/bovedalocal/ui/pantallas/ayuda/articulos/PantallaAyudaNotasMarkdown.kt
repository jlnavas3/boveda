/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
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
 * Artículo de ayuda exhaustivo sobre notas seguras, sintaxis Markdown enriquecida y tablas estructuradas.
 */
@Composable
fun PantallaAyudaNotasMarkdown(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Notas seguras y Markdown",
        alVolver = alVolver,
        textoAccion = "Ver Lista de Cuentas",
        iconoAccion = Icons.Filled.Description,
        alEjecutarAccion = { vm.ir(Pantalla.Lista) }
    ) {
        TarjetaInformativaAyuda(titulo = "Documentos y secretos en texto enriquecido") {
            Text(
                text = "Tanto el tipo nativo 'Nota segura' como el campo 'Notas adicionales' presente en cualquier credencial cuentan con un renderizador completo de Markdown. Puedes estructurar procedimientos de recuperación, configuraciones de servidores, listas de inventario o diarios personales con formato visual elegante y cifrado de grado militar.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Guía de sintaxis Markdown soportada") {
            Text(
                text = "• Encabezados:\n  # Título principal (H1)\n  ## Subtítulo de sección (H2)\n  ### Apartado menor (H3)\n\n• Formato de texto:\n  **Texto en negrita** para énfasis fuerte.\n  *Texto en cursiva* para detalles sutiles.\n  ~~Texto tachado~~ para tareas descartadas.\n\n• Listas estructuradas:\n  - Elemento con viñeta (utilizando guion o asterisco).\n  1. Primer paso numerado\n  2. Segundo paso numerado\n\n• Bloques de cita y separadores:\n  > Texto de cita o advertencia\n  --- (Tres guiones para crear una línea divisoria)",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Tablas estructuradas profesionales") {
            Text(
                text = "Bóveda Local procesa tablas de datos con alineación visual automática:\n\n| Servicio | Puerto | Protocolo |\n| :--- | :---: | ---: |\n| SSH | 22 | TCP |\n| HTTPS | 443 | TCP |\n| DNS | 53 | UDP |\n\nLa aplicación dibujará una tabla nativa estilizada con bordes de contraste y cabeceras destacadas.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Bloques de código monoespaciados") {
            Text(
                text = "Para comandos de terminal, scripts, tokens de API o claves PEM:\n\n• Código en línea: Enmarca comandos breves entre comillas invertidas sencillas (`docker ps`).\n• Bloques de código multilínea: Delimita con tres comillas invertidas (```) al inicio y al final. Se renderizará en una caja monoespaciada con fondo diferenciado lista para copiar.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Alterna entre edición y vista previa",
            texto = "En la pantalla de edición de notas dispones del botón de vista previa para comprobar el aspecto final de tus tablas, listas y encabezados antes de guardar."
        )
    }
}
