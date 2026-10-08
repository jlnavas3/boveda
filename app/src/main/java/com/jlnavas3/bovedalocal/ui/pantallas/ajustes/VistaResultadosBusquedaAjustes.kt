package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun VistaResultadosBusquedaAjustes(
    textoBusqueda: String,
    elementosFiltrados: List<ItemBusquedaAjustes>,
    mostrarIds: Boolean,
    modifier: Modifier = Modifier
) {
    if (elementosFiltrados.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "No se encontraron ajustes que coincidan con \"$textoBusqueda\"",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        ComponenteGrupo(
            etiqueta = "Resultados (${elementosFiltrados.size})",
            modifier = modifier
        ) {
            elementosFiltrados.forEachIndexed { index, elem ->
                if (index > 0) ComponenteSeparador(sangriaInicio = 68.dp)
                ComponenteNavegacion(
                    titulo = elem.titulo,
                    subtitulo = "${elem.ruta} • ${elem.subtitulo}",
                    icono = elem.icono,
                    colorIcono = elem.colorIcono,
                    idFila = elem.idEtiqueta,
                    mostrarId = mostrarIds,
                    alPulsar = elem.alPulsar
                )
            }
        }
    }
}
