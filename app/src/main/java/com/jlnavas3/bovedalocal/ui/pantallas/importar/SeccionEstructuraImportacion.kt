/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Identidad

/**
 * Sección de importación para categorías e identidades con explicación de deduplicación.
 */
@Composable
fun SeccionEstructuraImportacion(
    categoriasList: List<Categoria>,
    importarCategorias: Boolean,
    alAlternarCategorias: () -> Unit,
    identidadesList: List<Identidad>,
    importarIdentidades: Boolean,
    alAlternarIdentidades: () -> Unit,
    modoDetallado: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (categoriasList.isEmpty() && identidadesList.isEmpty()) return

    ContenedorSeccionImportacion(
        titulo = "Estructura y perfiles",
        icono = Icons.Filled.Folder,
        modifier = modifier
    ) {
        if (categoriasList.isNotEmpty()) {
            FilaOpcionImportacion(
                titulo = "Categorías personalizadas",
                descripcion = "Carpetas y etiquetas para clasificar credenciales (se deduplican automáticamente con las existentes)",
                contador = categoriasList.size,
                seleccionado = importarCategorias,
                alAlternar = alAlternarCategorias,
                textoDetallado = FormateadorValoresImportacion.categorias(categoriasList),
                modoDetallado = modoDetallado
            )
        }

        if (identidadesList.isNotEmpty()) {
            FilaOpcionImportacion(
                titulo = "Identidades y perfiles",
                descripcion = "Correos principales y alias para autocompletado y vinculación de cuentas",
                contador = identidadesList.size,
                seleccionado = importarIdentidades,
                alAlternar = alAlternarIdentidades,
                textoDetallado = FormateadorValoresImportacion.identidades(identidadesList),
                modoDetallado = modoDetallado
            )
        }
    }
}
