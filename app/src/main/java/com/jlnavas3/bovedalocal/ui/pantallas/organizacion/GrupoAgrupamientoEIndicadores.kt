package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

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
        etiqueta = "Agrupamiento e indicadores",
        idGrupo = "03.5.G1",
        mostrarId = mostrarId,
        descripcion = "Organización visual de las tarjetas y cuentas en el listado",
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Agrupar cuentas",
            icono = Icons.Filled.Tune,
            colorIcono = ColorIconosInternos,
            activo = agruparPorSitio,
            idFila = "03.5.1",
            mostrarId = mostrarId,
            alCambiar = alCambiarAgruparPorSitio
        )
        ComponenteSeparador()
        ComponenteSwitch(
            titulo = "Indicadores de contenido en tarjetas",
            icono = Icons.Filled.Tune,
            colorIcono = Color(0xFF5C6BC0),
            activo = mostrarIndicadoresContenido,
            idFila = "03.5.2",
            mostrarId = mostrarId,
            alCambiar = alCambiarMostrarIndicadores
        )
    }
}
