/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
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
 * Artículo de ayuda exhaustivo sobre categorías personalizadas, iconos, colores y migración segura.
 */
@Composable
fun PantallaAyudaCategorias(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Categorías personalizadas",
        alVolver = alVolver,
        textoAccion = "Gestionar Categorías",
        iconoAccion = Icons.Filled.Folder,
        alEjecutarAccion = { vm.ir(Pantalla.Categorias()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Estructura vertical de tu bóveda") {
            Text(
                text = "Las Categorías proporcionan la clasificación primaria y excluyente de tus entradas. Cada elemento en Bóveda Local pertenece a una categoría principal (por ejemplo: 'Finanzas', 'Streaming', 'Correo', 'Compras', 'Juegos' o 'Servidores'), permitiéndote mantener compartimentada tu biblioteca de registros.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Creación, edición y diseño visual") {
            Text(
                text = "En la pantalla de Gestión de Categorías puedes:\n\n• Crear categorías ilimitadas: Asigna un nombre claro y representativo.\n\n• Catálogo de iconos temáticos: Elige entre decenas de iconos vectoriales para identificar de un vistazo el propósito de la categoría (bancos, llaves, ordenadores, carritos de compra, aviones, etc.).\n\n• Paleta de colores distintivos: Asocia un color de contraste que teñirá el squircle y las etiquetas de las entradas asociadas.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Eliminación segura sin pérdida de datos") {
            Text(
                text = "Si decides borrar una categoría que ya contiene cuentas guardadas, Bóveda Local no eliminará tus registros: se desplegará un asistente de reasignación que te permitirá mover en bloque todas las cuentas hacia otra categoría destino antes de retirar la categoría antigua.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Filtrado en el listado y comandos de búsqueda") {
            Text(
                text = "• Píldora de categorías: Pulsa '[ 📁 Categorías ]' en la barra superior de la pantalla principal para filtrar la vista con un solo toque.\n\n• Búsqueda sintáctica: Escribe 'cat:finanzas' o 'cat:servidores' en la caja de búsqueda para combinar el filtro de categoría con cualquier otro término o comando.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Combina categorías con etiquetas",
            texto = "Utiliza las Categorías para grandes áreas (ej: 'Finanzas') y añade Etiquetas transversales (ej: 'tarjetas', 'impuestos', '2026') para una organización multidimensional perfecta."
        )
    }
}
