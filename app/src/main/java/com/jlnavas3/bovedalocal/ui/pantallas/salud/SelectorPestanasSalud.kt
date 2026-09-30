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
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

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
    val bordeChip = if (GrosorBorde > 0.dp) BorderStroke(GrosorBorde, ColorBordeActual.copy(alpha = 0.5f)) else null
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
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = fondoBadgeParaTema(Peligro),
                            selectedLabelColor = colorLegibleParaTema(Peligro)
                        )
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
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = fondoBadgeParaTema(Peligro),
                            selectedLabelColor = colorLegibleParaTema(Peligro)
                        )
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
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = fondoBadgeParaTema(Peligro),
                            selectedLabelColor = colorLegibleParaTema(Peligro)
                        )
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
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = fondoBadgeParaTema(ColorAcento),
                            selectedLabelColor = colorLegibleParaTema(ColorAcento)
                        )
                    )
                }
            }
        }
    }
}
