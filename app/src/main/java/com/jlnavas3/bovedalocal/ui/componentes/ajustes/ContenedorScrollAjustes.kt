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
