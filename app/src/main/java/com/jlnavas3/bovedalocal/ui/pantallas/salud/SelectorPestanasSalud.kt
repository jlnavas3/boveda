package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
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
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            FilterChip(
                selected = pestanaActiva == PestanaSalud.REPETIDAS,
                onClick = { alSeleccionarPestana(PestanaSalud.REPETIDAS) },
                label = { Text("Repetidas ($gruposDuplicadosCount g / $totalDuplicadasCount)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = fondoBadgeParaTema(if (gruposDuplicadosCount > 0) Peligro else Menta),
                    selectedLabelColor = colorLegibleParaTema(if (gruposDuplicadosCount > 0) Peligro else Menta)
                )
            )
        }
        item {
            FilterChip(
                selected = pestanaActiva == PestanaSalud.COMUNES,
                onClick = { alSeleccionarPestana(PestanaSalud.COMUNES) },
                label = { Text("Filtradas ($muyComunesCount)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = fondoBadgeParaTema(if (muyComunesCount > 0) Peligro else Menta),
                    selectedLabelColor = colorLegibleParaTema(if (muyComunesCount > 0) Peligro else Menta)
                )
            )
        }
        item {
            FilterChip(
                selected = pestanaActiva == PestanaSalud.DEBILES,
                onClick = { alSeleccionarPestana(PestanaSalud.DEBILES) },
                label = { Text("Débiles ($debilesCount)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = fondoBadgeParaTema(if (debilesCount > 0) Peligro else Menta),
                    selectedLabelColor = colorLegibleParaTema(if (debilesCount > 0) Peligro else Menta)
                )
            )
        }
        item {
            FilterChip(
                selected = pestanaActiva == PestanaSalud.ANTIGUAS,
                onClick = { alSeleccionarPestana(PestanaSalud.ANTIGUAS) },
                label = { Text("Antiguas ($antiguasCount)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = fondoBadgeParaTema(ColorAcento),
                    selectedLabelColor = colorLegibleParaTema(ColorAcento)
                )
            )
        }
    }
}
