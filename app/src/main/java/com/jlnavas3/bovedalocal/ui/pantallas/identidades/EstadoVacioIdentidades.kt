/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.EstadoVacioPantalla

/**
 * Estado vacío para la pantalla de gestión de identidades cuando aún no se ha creado ninguna o no hay coincidencias.
 */
@Composable
fun EstadoVacioIdentidades(
    sinIdentidadesEnTotal: Boolean = true,
    modifier: Modifier = Modifier
) {
    val titulo = if (sinIdentidadesEnTotal) "Sin identidades creadas" else "No se encontraron identidades"
    val subtitulo = if (sinIdentidadesEnTotal) {
        "Crea identidades (ej. Personal, Trabajo, Compras) asociadas a tus correos. Las credenciales existentes se vincularán de forma automática e inteligente."
    } else {
        "Ninguna identidad coincide con la búsqueda aplicada."
    }

    EstadoVacioPantalla(
        icono = Icons.Filled.AccountCircle,
        titulo = titulo,
        subtitulo = subtitulo,
        modifier = modifier
    )
}
