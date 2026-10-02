package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun SelectorPestanasSalud(
    pestanaActiva: PestanaSalud,
    alSeleccionarPestana: (PestanaSalud) -> Unit,
    gruposDuplicadosCount: Int,
    totalDuplicadasCount: Int,
    muyComunesCount: Int,
    debilesCount: Int,
    antiguasCount: Int,
    textoBusqueda: String = "",
    gruposDuplicadosFiltrados: Int = gruposDuplicadosCount,
    totalDuplicadasFiltradas: Int = totalDuplicadasCount,
    muyComunesFiltrados: Int = muyComunesCount,
    debilesFiltrados: Int = debilesCount,
    antiguasFiltradas: Int = antiguasCount,
    modifier: Modifier = Modifier
) {
    val pestanasVisibles = remember(gruposDuplicadosCount, muyComunesCount, debilesCount, antiguasCount) {
        buildList {
            if (gruposDuplicadosCount > 0) add(PestanaSalud.REPETIDAS)
            if (muyComunesCount > 0) add(PestanaSalud.COMUNES)
            if (debilesCount > 0) add(PestanaSalud.DEBILES)
            if (antiguasCount > 0) add(PestanaSalud.ANTIGUAS)
        }
    }

    if (pestanasVisibles.isEmpty()) return

    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val coloresChip = FilterChipDefaults.filterChipColors(
        containerColor = ColorTarjetaAjustes,
        labelColor = TextoSecundario,
        selectedContainerColor = ColorCampoAjustes,
        selectedLabelColor = ColorAcento
    )
    val bordeChip = FilterChipDefaults.filterChipBorder(
        enabled = true,
        selected = false,
        borderColor = ColorSeparadorAjustes,
        selectedBorderColor = ColorAcento
    )
    val buscando = textoBusqueda.isNotBlank()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(pestanasVisibles.size, key = { pestanasVisibles[it].name }) { index ->
            when (pestanasVisibles[index]) {
                PestanaSalud.REPETIDAS -> {
                    val textoLabel = if (buscando) {
                        "Repetidas ($gruposDuplicadosFiltrados g / $totalDuplicadasFiltradas)"
                    } else {
                        "Repetidas ($gruposDuplicadosCount g / $totalDuplicadasCount)"
                    }
                    FilterChip(
                        selected = pestanaActiva == PestanaSalud.REPETIDAS,
                        onClick = { alSeleccionarPestana(PestanaSalud.REPETIDAS) },
                        label = { Text(textoLabel) },
                        shape = forma,
                        border = bordeChip,
                        colors = coloresChip
                    )
                }
                PestanaSalud.COMUNES -> {
                    val textoLabel = if (buscando) "Filtradas ($muyComunesFiltrados)" else "Filtradas ($muyComunesCount)"
                    FilterChip(
                        selected = pestanaActiva == PestanaSalud.COMUNES,
                        onClick = { alSeleccionarPestana(PestanaSalud.COMUNES) },
                        label = { Text(textoLabel) },
                        shape = forma,
                        border = bordeChip,
                        colors = coloresChip
                    )
                }
                PestanaSalud.DEBILES -> {
                    val textoLabel = if (buscando) "Débiles ($debilesFiltrados)" else "Débiles ($debilesCount)"
                    FilterChip(
                        selected = pestanaActiva == PestanaSalud.DEBILES,
                        onClick = { alSeleccionarPestana(PestanaSalud.DEBILES) },
                        label = { Text(textoLabel) },
                        shape = forma,
                        border = bordeChip,
                        colors = coloresChip
                    )
                }
                PestanaSalud.ANTIGUAS -> {
                    val textoLabel = if (buscando) "Antiguas ($antiguasFiltradas)" else "Antiguas ($antiguasCount)"
                    FilterChip(
                        selected = pestanaActiva == PestanaSalud.ANTIGUAS,
                        onClick = { alSeleccionarPestana(PestanaSalud.ANTIGUAS) },
                        label = { Text(textoLabel) },
                        shape = forma,
                        border = bordeChip,
                        colors = coloresChip
                    )
                }
            }
        }
    }
}
