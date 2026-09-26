package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

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
fun BarraChipsFiltroDuplicados(
    filtroActivo: FiltroDuplicados,
    totalGrupos: Int,
    totalSobrantesIdenticas: Int,
    cantAppsAndroid: Int,
    cantWeb: Int,
    cantMismaCuenta: Int,
    cantVariantes: Int,
    alSeleccionarFiltro: (FiltroDuplicados) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            FilterChip(
                selected = filtroActivo == FiltroDuplicados.TODOS,
                onClick = { alSeleccionarFiltro(FiltroDuplicados.TODOS) },
                label = { Text("Todos ($totalGrupos)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = fondoBadgeParaTema(ColorAcento),
                    selectedLabelColor = colorLegibleParaTema(ColorAcento)
                )
            )
        }
        if (totalSobrantesIdenticas > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.IDENTICOS,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.IDENTICOS) },
                    label = { Text("Idénticos ($totalSobrantesIdenticas)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = fondoBadgeParaTema(Menta),
                        selectedLabelColor = colorLegibleParaTema(Menta)
                    )
                )
            }
        }
        if (cantAppsAndroid > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.APPS_ANDROID,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.APPS_ANDROID) },
                    label = { Text("Apps Android ($cantAppsAndroid)") }
                )
            }
        }
        if (cantWeb > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.SITIOS_WEB,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.SITIOS_WEB) },
                    label = { Text("Sitios web ($cantWeb)") }
                )
            }
        }
        if (cantMismaCuenta > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.MISMA_CUENTA,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.MISMA_CUENTA) },
                    label = { Text("Misma cuenta ($cantMismaCuenta)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = fondoBadgeParaTema(Peligro),
                        selectedLabelColor = colorLegibleParaTema(Peligro)
                    )
                )
            }
        }
        if (cantVariantes > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.VARIANTES,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.VARIANTES) },
                    label = { Text("Variantes ($cantVariantes)") }
                )
            }
        }
    }
}
