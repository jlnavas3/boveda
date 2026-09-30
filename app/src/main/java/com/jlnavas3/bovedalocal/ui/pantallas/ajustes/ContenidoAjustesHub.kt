package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

data class InfoGrupoAjustes(
    val nombre: String,
    val idGrupo: String,
    val icono: ImageVector
)

@Composable
fun VistaResultadosBusquedaAjustes(
    textoBusqueda: String,
    elementosFiltrados: List<ElementoMenuAjustes>,
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
                    icono = elem.icono,
                    colorIcono = elem.colorIcono,
                    idFila = elem.idEtiqueta,
                    mostrarId = mostrarIds,
                    valorTexto = elem.valorTexto,
                    alPulsar = elem.alPulsar
                )
            }
        }
    }
}

@Composable
fun VistaGruposAjustesHub(
    todosLosElementos: List<ElementoMenuAjustes>,
    mostrarIds: Boolean,
    modifier: Modifier = Modifier
) {
    val grupos = listOf(
        InfoGrupoAjustes("Seguridad", "01-SEG", Icons.Filled.Security),
        InfoGrupoAjustes("Apariencia", "02-APA", Icons.Filled.Palette),
        InfoGrupoAjustes("Lista de cuentas", "03-LST", Icons.Filled.Layers),
        InfoGrupoAjustes("Herramientas", "04-HER", Icons.Filled.Build),
        InfoGrupoAjustes("Copias y datos", "05-COP", Icons.Filled.Backup),
        InfoGrupoAjustes("Sistema", "06-SIS", Icons.Filled.Settings)
    )

    Column(modifier = modifier) {
        grupos.forEachIndexed { gIndex, grupo ->
            val elementosDelGrupo = todosLosElementos.filter { it.grupo == grupo.nombre }
            if (elementosDelGrupo.isNotEmpty()) {
                if (gIndex > 0) Spacer(Modifier.height(EspaciadoComponentes))
                ComponenteGrupo(
                    etiqueta = grupo.nombre,
                    icono = grupo.icono,
                    idGrupo = grupo.idGrupo,
                    mostrarId = mostrarIds
                ) {
                    elementosDelGrupo.forEachIndexed { index, elem ->
                        if (index > 0) ComponenteSeparador(sangriaInicio = 68.dp)
                        ComponenteNavegacion(
                            titulo = elem.titulo,
                            icono = elem.icono,
                            colorIcono = elem.colorIcono,
                            idFila = elem.idEtiqueta,
                            mostrarId = mostrarIds,
                            valorTexto = elem.valorTexto,
                            alPulsar = elem.alPulsar
                        )
                    }
                }
            }
        }
    }
}
