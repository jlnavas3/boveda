package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

@Composable
fun GrupoOrdenacionPredeterminada(
    mostrarId: Boolean,
    criterioSeleccionado: String,
    alSeleccionarCriterio: (CriterioOrdenacion) -> Unit,
    alRestablecerGrupo: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Ordenación predeterminada",
        idGrupo = "03.5.G3",
        mostrarId = mostrarId,
        descripcion = "Criterio de orden inicial de las cuentas en la bóveda",
        modifier = modifier
    ) {
        CriterioOrdenacion.entries.forEachIndexed { index, criterio ->
            if (index > 0) ComponenteSeparador()
            ComponenteRadio(
                titulo = criterio.etiqueta,
                icono = Icons.AutoMirrored.Filled.Sort,
                colorIcono = ColorIconosInternos,
                seleccionado = criterioSeleccionado == criterio.name,
                idFila = "03.5.${5 + index}",
                mostrarId = mostrarId,
                alSeleccionar = { alSeleccionarCriterio(criterio) }
            )
        }

        ComponenteSeparador()

        ComponenteBotonFila(
            titulo = "Restablecer grupo",
            alPulsar = alRestablecerGrupo
        )
    }
}
