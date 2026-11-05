/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaPasoAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda sobre la personalización del cajón lateral y la reorganización fractal de ajustes.
 */
@Composable
fun PantallaAyudaMenuLateral(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Menú lateral y reorganización",
        alVolver = alVolver,
        textoAccion = "Personalizar Menú Lateral",
        iconoAccion = Icons.Filled.Menu,
        alEjecutarAccion = { vm.ir(Pantalla.PersonalizarMenuLateral()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Adapta la navegación a tu ergonomía") {
            Text(
                text = "Bóveda Local te permite personalizar integralmente tanto el cajón de navegación lateral (Drawer) como la estructura jerárquica de la pantalla de Ajustes para que tengas siempre al alcance de tu pulgar las secciones que más utilizas.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Personalizar barra lateral (02-APA-MNL)") {
            Text(
                text = "Desde Ajustes > Apariencia > Personalizar menú lateral puedes configurar:\n\n• Reordenación táctil: Arrastra cada elemento manteniendo pulsado el icono de agarre para ordenar tus herramientas según tu flujo de trabajo habitual.\n\n• Visibilidad y catálogo de accesos: Pulsa '+ Añadir opción' para explorar el catálogo completo de Bóveda Local. Puedes fijar cualquier pantalla o ajuste interno directamente en el menú lateral.\n\n• Modo unitario o múltiple: Al tocar el texto de una fila la añades de inmediato. Si tocas el icono '+' se transforma en un visto '✓' permitiéndote seleccionar múltiples opciones y añadirlas todas juntas al pulsar 'Añadir seleccionados'.\n\n• Conmutadores de diseño: Oculta la cabecera del menú si buscas minimalismo o desactiva las insignias de conteo (badges) para una apariencia más limpia.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Reorganización fractal de ajustes (06-SIS-AVZ-ORG)") {
            Text(
                text = "En Ajustes > Avanzada > Reorganizar ajustes dispones de una herramienta única para reestructurar el mapa completo de configuraciones de la aplicación:\n\n• Árbol jerárquico interactivo: Visualiza todos los nodos raíz (N1), secundarios (N2) y subniveles (N3) en una vista fractal fluida.\n\n• Reordenación de hermanos: Mueve cualquier ajuste arriba o abajo con las flechas o por arrastre para cambiar su orden de aparición.\n\n• Reparenting dinámico: ¿Prefieres que un ajuste profundo esté en la pantalla principal o dentro de otro grupo? Puedes cambiar su nodo padre a cualquier contenedor secundario o promoverlo directamente a la raíz Nivel 1.\n\n• Búsqueda en vivo: Filtra por nombre o ID en tiempo real; el árbol expandirá automáticamente los ancestros de los resultados coincidentes.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Flujo para añadir accesos al menú lateral") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TarjetaPasoAyuda(
                    numero = 1,
                    titulo = "Abre Personalizar menú lateral",
                    descripcion = "Accede desde el botón inferior en el menú lateral o desde Ajustes > Apariencia."
                )
                TarjetaPasoAyuda(
                    numero = 2,
                    titulo = "Toca '+ Añadir opción'",
                    descripcion = "Se abrirá el catálogo con buscador instantáneo de todas las pantallas y herramientas disponibles."
                )
                TarjetaPasoAyuda(
                    numero = 3,
                    titulo = "Selecciona unitario o múltiple",
                    descripcion = "Toca para agregar de inmediato o marca varios '+' para añadir un lote completo."
                )
            }
        }

        TarjetaConsejoAyuda(
            titulo = "Restablecimiento en cualquier momento",
            texto = "Tanto el menú lateral como el árbol de ajustes disponen de un botón 'Restablecer' en la esquina superior para volver a la distribución original recomendada si deseas deshacer tus personalizaciones."
        )
    }
}
