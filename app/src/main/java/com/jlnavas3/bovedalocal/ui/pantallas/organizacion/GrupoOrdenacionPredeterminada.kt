package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador

@Composable
fun GrupoOrdenacionPredeterminada(
    mostrarId: Boolean,
    criterioSeleccionado: String,
    alSeleccionarCriterio: (CriterioOrdenacion) -> Unit,
    alRestablecerGrupo: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Orden predeterminado",
        icono = Icons.AutoMirrored.Filled.Sort,
        colorIcono = Color(0xFF00ACC1),
        alRestablecer = alRestablecerGrupo,
        idGrupo = "03-LST-DES-G03",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        CriterioOrdenacion.entries.forEachIndexed { index, criterio ->
            if (index > 0) ComponenteSeparador(sangriaInicio = 16.dp)
            ComponenteRadio(
                titulo = criterio.etiqueta,
                icono = null,
                seleccionado = criterioSeleccionado == criterio.name,
                idFila = "03-LST-DES-ORD-${index + 1}",
                mostrarId = mostrarId,
                alSeleccionar = { alSeleccionarCriterio(criterio) }
            )
        }
    }
}
