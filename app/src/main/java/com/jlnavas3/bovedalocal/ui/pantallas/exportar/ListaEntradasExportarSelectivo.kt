package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Componente que renderiza el listado con scroll de credenciales disponibles para exportar,
 * mostrando un mensaje accesible si no hay resultados en la categoría o búsqueda activa.
 */
@Composable
fun ListaEntradasExportarSelectivo(
    entradas: List<Entrada>,
    idsSeleccionados: List<String>,
    alAlternarSeleccion: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (entradas.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(entradas, key = { it.id }) { entrada ->
                val marcada = idsSeleccionados.contains(entrada.id)
                FilaEntradaExportarSelectivo(
                    entrada = entrada,
                    marcada = marcada,
                    alAlternarMarcado = { checked ->
                        alAlternarSeleccion(entrada.id, checked)
                    }
                )
            }
        }
    }
}
