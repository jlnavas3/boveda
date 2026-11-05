/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.SeccionCategoriasEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.identidades.SeccionIdentidadEdicion
import com.jlnavas3.bovedalocal.ui.theme.ColorAnclado
import com.jlnavas3.bovedalocal.ui.theme.ColorFavorito

/**
 * Sección de organización de la credencial, desglosada en filas homogéneas individuales
 * (Identidad, Categoría, Etiqueta, Favorito, Anclado, Ignorar en auditoría) sin marcos envolventes dobles.
 */
@Composable
fun SeccionOrganizacionEdicion(
    categoriasDisponibles: List<Categoria> = emptyList(),
    categoriasSeleccionadas: List<String> = emptyList(),
    alCambiarCategorias: (List<String>) -> Unit = {},
    alCrearNuevaCategoria: () -> Unit = {},
    identidadesDisponibles: List<Identidad> = emptyList(),
    identidadSeleccionadaId: String? = null,
    alSeleccionarIdentidad: (Identidad?) -> Unit = {},
    etiquetas: List<String>,
    alCambiarEtiquetas: (List<String>) -> Unit,
    etiquetasSugeridas: List<String>,
    favorito: Boolean,
    alAlternarFavorito: (Boolean) -> Unit,
    anclado: Boolean = false,
    alAlternarAnclado: (Boolean) -> Unit = {},
    ignoradaEnSalud: Boolean = false,
    alAlternarIgnoradaEnSalud: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (identidadesDisponibles.isNotEmpty()) {
            SeccionIdentidadEdicion(
                identidadesDisponibles = identidadesDisponibles,
                identidadSeleccionadaId = identidadSeleccionadaId,
                alSeleccionarIdentidad = alSeleccionarIdentidad
            )
        }

        SeccionCategoriasEdicion(
            categoriasDisponibles = categoriasDisponibles,
            categoriasSeleccionadas = categoriasSeleccionadas,
            alCambiarCategorias = alCambiarCategorias,
            alCrearNuevaCategoria = alCrearNuevaCategoria
        )

        SeccionEtiquetasEdicion(
            etiquetas = etiquetas,
            alCambiarEtiquetas = alCambiarEtiquetas,
            etiquetasSugeridas = etiquetasSugeridas
        )

        FilaSeccionSwitchEdicion(
            icono = Icons.Filled.Star,
            colorIcono = ColorFavorito,
            titulo = "Marcar como favorito",
            descripcion = "Destacar con estrella en la lista",
            activado = favorito,
            alCambiar = alAlternarFavorito
        )

        FilaSeccionSwitchEdicion(
            icono = Icons.Filled.PushPin,
            colorIcono = ColorAnclado,
            titulo = "Anclado",
            descripcion = "Fijar en el grupo 'Anclados' arriba en la lista principal",
            activado = anclado,
            alCambiar = alAlternarAnclado
        )

        FilaSeccionSwitchEdicion(
            icono = Icons.Filled.Shield,
            colorIcono = Color(0xFFE57373),
            titulo = "Ignorar en auditoría",
            descripcion = "Excluir de análisis de contraseñas vulnerables",
            activado = ignoradaEnSalud,
            alCambiar = alAlternarIgnoradaEnSalud
        )
    }
}
