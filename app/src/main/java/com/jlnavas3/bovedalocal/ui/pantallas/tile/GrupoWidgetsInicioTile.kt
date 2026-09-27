package com.jlnavas3.bovedalocal.ui.pantallas.tile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador

@Composable
fun GrupoWidgetsInicioTile(
    mostrarIdsAjustes: Boolean,
    alNavegarWidgets: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Widgets de inicio",
        idGrupo = "04.3.G2",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Acceso directo en la pantalla de inicio",
        modifier = modifier
    ) {
        ComponenteNavegacion(
            titulo = "Personalizar widgets de escritorio",
            icono = Icons.Filled.Widgets,
            colorIcono = ColorGenerador,
            idFila = "04.3.7",
            mostrarId = mostrarIdsAjustes,
            alPulsar = alNavegarWidgets
        )
    }
}
