package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador

@Composable
fun GrupoDensidadFilas(
    mostrarId: Boolean,
    densidadLista: String,
    alSeleccionarDensidad: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Densidad de lista",
        icono = Icons.Filled.Tune,
        colorIcono = Color(0xFF00ACC1),
        idGrupo = "03.1.G2",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        ComponenteRadio(
            titulo = "Predeterminada",
            icono = null,
            seleccionado = densidadLista == "predeterminada" || (densidadLista != "comoda" && densidadLista != "compacta"),
            idFila = "03.1.3",
            mostrarId = mostrarId,
            alSeleccionar = { alSeleccionarDensidad("predeterminada") }
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteRadio(
            titulo = "Cómoda",
            icono = null,
            seleccionado = densidadLista == "comoda",
            idFila = "03.1.4",
            mostrarId = mostrarId,
            alSeleccionar = { alSeleccionarDensidad("comoda") }
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteRadio(
            titulo = "Compacta",
            icono = null,
            seleccionado = densidadLista == "compacta",
            idFila = "03.1.5",
            mostrarId = mostrarId,
            alSeleccionar = { alSeleccionarDensidad("compacta") }
        )
    }
}
