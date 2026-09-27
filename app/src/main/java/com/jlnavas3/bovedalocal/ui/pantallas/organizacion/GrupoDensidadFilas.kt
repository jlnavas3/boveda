package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

@Composable
fun GrupoDensidadFilas(
    mostrarId: Boolean,
    densidadLista: String,
    alSeleccionarDensidad: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Densidad de filas",
        idGrupo = "03.5.G2",
        mostrarId = mostrarId,
        descripcion = "Altura y espacio vertical de cada fila en el listado",
        modifier = modifier
    ) {
        ComponenteRadio(
            titulo = "Predeterminada",
            icono = Icons.Filled.Tune,
            colorIcono = ColorIconosInternos,
            seleccionado = densidadLista == "predeterminada" || (densidadLista != "comoda" && densidadLista != "compacta"),
            idFila = "03.5.2",
            mostrarId = mostrarId,
            alSeleccionar = { alSeleccionarDensidad("predeterminada") }
        )
        ComponenteSeparador()
        ComponenteRadio(
            titulo = "Cómoda",
            icono = Icons.Filled.Tune,
            colorIcono = ColorIconosInternos,
            seleccionado = densidadLista == "comoda",
            idFila = "03.5.3",
            mostrarId = mostrarId,
            alSeleccionar = { alSeleccionarDensidad("comoda") }
        )
        ComponenteSeparador()
        ComponenteRadio(
            titulo = "Compacta",
            icono = Icons.Filled.Tune,
            colorIcono = ColorIconosInternos,
            seleccionado = densidadLista == "compacta",
            idFila = "03.5.4",
            mostrarId = mostrarId,
            alSeleccionar = { alSeleccionarDensidad("compacta") }
        )
    }
}
