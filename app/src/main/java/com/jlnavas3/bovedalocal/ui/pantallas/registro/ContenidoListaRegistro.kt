package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris

/**
 * Muestra el contador, criterio activo y la lista desplazable de eventos del registro del sistema.
 */
@Composable
fun ContenidoListaRegistro(
    eventosOrdenados: List<EventoRegistro>,
    totalEventos: Int,
    criterioOrden: CriterioOrdenRegistro,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${eventosOrdenados.size} de $totalEventos eventos",
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = criterioOrden.etiqueta,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (eventosOrdenados.isEmpty()) {
                EstadoVacioRegistro(estaVacio = totalEventos == 0)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = eventosOrdenados,
                        key = { it.textoCompleto + it.timestamp }
                    ) { ev ->
                        TarjetaEventoRegistro(ev = ev)
                    }
                }
            }
        }
    }
}
