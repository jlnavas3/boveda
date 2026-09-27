package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch

@Composable
fun GrupoAgrupamientoEIndicadores(
    mostrarId: Boolean,
    agruparPorSitio: Boolean,
    mostrarIndicadoresContenido: Boolean,
    alCambiarAgruparPorSitio: (Boolean) -> Unit,
    alCambiarMostrarIndicadores: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Agrupamiento",
        icono = Icons.Filled.Layers,
        colorIcono = Color(0xFF00ACC1),
        idGrupo = "03.1.G1",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Agrupar cuentas",
            icono = null,
            activo = agruparPorSitio,
            idFila = "03.1.1",
            mostrarId = mostrarId,
            alCambiar = alCambiarAgruparPorSitio
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSwitch(
            titulo = "Indicadores de contenido en tarjetas",
            icono = null,
            activo = mostrarIndicadoresContenido,
            idFila = "03.1.2",
            mostrarId = mostrarId,
            alCambiar = alCambiarMostrarIndicadores
        )
    }
}
