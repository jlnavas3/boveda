package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.ModoVisualizacionIdentidades
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Grupo de ajustes para configurar el modo de visualización de las identidades
 * en el listado principal de credenciales.
 */
@Composable
fun GrupoModoIdentidadesAjustes(
    modoActual: ModoVisualizacionIdentidades,
    mostrarId: Boolean,
    alSeleccionarModo: (ModoVisualizacionIdentidades) -> Unit,
    alGestionarIdentidades: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Identidades en lista principal",
        icono = Icons.Filled.AccountCircle,
        colorIcono = Color(0xFF0284C7),
        idGrupo = "03-LST-DES-GID",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        ComponenteRadio(
            titulo = "Chips de filtro superior (Recomendado)",
            seleccionado = modoActual == ModoVisualizacionIdentidades.CHIPS,
            alSeleccionar = { alSeleccionarModo(ModoVisualizacionIdentidades.CHIPS) },
            idFila = "03-LST-DES-CHP",
            mostrarId = mostrarId,
            colorAcento = ColorAcento
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteRadio(
            titulo = "Secciones agrupadas plegables",
            seleccionado = modoActual == ModoVisualizacionIdentidades.SECCIONES,
            alSeleccionar = { alSeleccionarModo(ModoVisualizacionIdentidades.SECCIONES) },
            idFila = "03-LST-DES-SEC",
            mostrarId = mostrarId,
            colorAcento = ColorAcento
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteRadio(
            titulo = "Desactivado",
            seleccionado = modoActual == ModoVisualizacionIdentidades.DESACTIVADO,
            alSeleccionar = { alSeleccionarModo(ModoVisualizacionIdentidades.DESACTIVADO) },
            idFila = "03-LST-DES-OFF",
            mostrarId = mostrarId,
            colorAcento = ColorAcento
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteNavegacion(
            titulo = "Administrar identidades y perfiles...",
            icono = null,
            idFila = "03-LST-IDE",
            mostrarId = mostrarId,
            alPulsar = alGestionarIdentidades
        )
    }
}
