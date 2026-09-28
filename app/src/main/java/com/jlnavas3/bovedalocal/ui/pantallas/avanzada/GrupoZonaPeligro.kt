package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera

@Composable
fun GrupoZonaPeligro(
    mostrarIdsAjustes: Boolean,
    alSolicitarBorrado: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Zona de peligro",
        icono = Icons.Filled.Delete,
        colorIcono = ColorPapelera,
        idGrupo = "06-SIS-AVZ-G03",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Eliminación irreversible e inmediata de todas las contraseñas, notas y configuraciones",
        modifier = modifier
    ) {
        ComponenteNavegacion(
            titulo = "Borrar bóveda definitivamente",
            icono = null,
            idFila = "06-SIS-AVZ-DEL",
            mostrarId = mostrarIdsAjustes,
            alPulsar = alSolicitarBorrado
        )
    }
}
