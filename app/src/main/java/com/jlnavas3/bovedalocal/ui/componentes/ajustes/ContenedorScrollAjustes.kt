/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico

/**
 * Modificador para asignar al contenedor con scroll vertical para medir su posición relativa y dotarlo de rebote elástico.
 */
fun Modifier.contenedorScrollAjustes(coordinador: CoordinadorResaltadoAjustes?): Modifier =
    this
        .reboteElastico()
        .then(
            if (coordinador != null) {
                Modifier.onGloballyPositioned { coords ->
                    coordinador.contenedorCoordinates = coords
                }
            } else Modifier
        )
