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
        etiqueta = "Accesos rápidos",
        icono = Icons.Filled.Widgets,
        colorIcono = ColorGenerador,
        idGrupo = "04-HER-MSK-G02",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Acceso directo en la pantalla de inicio",
        modifier = modifier
    ) {
        ComponenteNavegacion(
            titulo = "Personalizar widgets de escritorio",
            icono = null,
            idFila = "04-HER-MSK-WGT",
            mostrarId = mostrarIdsAjustes,
            alPulsar = alNavegarWidgets
        )
    }
}
