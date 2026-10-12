/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.EstadoVacioPantalla

/**
 * Estado vacío para la pantalla de gestión de categorías cuando aún no se ha creado ninguna o no hay coincidencias.
 */
@Composable
fun EstadoVacioCategorias(
    sinCategoriasEnTotal: Boolean = true,
    modifier: Modifier = Modifier
) {
    val titulo = if (sinCategoriasEnTotal) "Sin categorías creadas" else "No se encontraron categorías"
    val subtitulo = if (sinCategoriasEnTotal) {
        "Crea categorías temáticas (ej. Finanzas, Trabajo, Entretenimiento, Redes) para clasificar y filtrar fácilmente tus credenciales."
    } else {
        "Ninguna categoría coincide con la búsqueda aplicada."
    }

    EstadoVacioPantalla(
        icono = Icons.Filled.Folder,
        titulo = titulo,
        subtitulo = subtitulo,
        modifier = modifier
    )
}
