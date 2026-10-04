package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Componente que renderiza el listado con scroll de credenciales disponibles para exportar,
 * con espaciados y dimensiones idénticas a la lista principal.
 */
@Composable
fun ListaEntradasExportarSelectivo(
    entradas: List<Entrada>,
    idsSeleccionados: List<String>,
    alAlternarSeleccion: (String, Boolean) -> Unit,
    espaciadoFilas: Dp = 8.dp,
    densidadAltura: Dp = 74.dp,
    densidadMonograma: Int = 46,
    modifier: Modifier = Modifier
) {
    if (entradas.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay entradas registradas en esta categoría.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 130.dp),
            verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
        ) {
            items(entradas, key = { it.id }) { entrada ->
                val marcada = idsSeleccionados.contains(entrada.id)
                FilaEntradaExportarSelectivo(
                    entrada = entrada,
                    marcada = marcada,
                    alAlternarMarcado = { checked ->
                        alAlternarSeleccion(entrada.id, checked)
                    },
                    alturaFila = densidadAltura,
                    tamanoMonograma = densidadMonograma
                )
            }
        }
    }
}
