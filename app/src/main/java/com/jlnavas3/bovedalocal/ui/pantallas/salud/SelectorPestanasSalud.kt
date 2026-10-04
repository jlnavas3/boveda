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
import com.jlnavas3.bovedalocal.data.Entrada
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
    ignoradasCount: Int = 0,
    textoBusqueda: String = "",
    gruposDuplicadosFiltrados: Int = gruposDuplicadosCount,
    totalDuplicadasFiltradas: Int = totalDuplicadasCount,
    muyComunesFiltrados: Int = muyComunesCount,
    debilesFiltrados: Int = debilesCount,
    antiguasFiltradas: Int = antiguasCount,
    ignoradasFiltradas: Int = ignoradasCount,
    modifier: Modifier = Modifier
) {
    val pestanasVisibles = remember(gruposDuplicadosCount, muyComunesCount, debilesCount, antiguasCount, ignoradasCount) {
        buildList {
            if (gruposDuplicadosCount > 0) add(PestanaSalud.REPETIDAS)
            if (muyComunesCount > 0) add(PestanaSalud.COMUNES)
            if (debilesCount > 0) add(PestanaSalud.DEBILES)
            if (antiguasCount > 0) add(PestanaSalud.ANTIGUAS)
            if (ignoradasCount > 0) add(PestanaSalud.IGNORADAS)
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
                PestanaSalud.IGNORADAS -> {
                    val textoLabel = if (buscando) "Ignoradas ($ignoradasFiltradas)" else "Ignoradas ($ignoradasCount)"
                    FilterChip(
                        selected = pestanaActiva == PestanaSalud.IGNORADAS,
                        onClick = { alSeleccionarPestana(PestanaSalud.IGNORADAS) },
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

@Composable
fun SelectorPestanasSalud(
    pestanaActiva: PestanaSalud,
    alSeleccionarPestana: (PestanaSalud) -> Unit,
    duplicadas: List<List<Entrada>>,
    muyComunes: List<Entrada>,
    debiles: List<Entrada>,
    antiguas: List<Entrada>,
    ignoradas: List<Entrada>,
    textoBusqueda: String,
    modifier: Modifier = Modifier
) {
    val q = textoBusqueda.trim()
    val gruposDuplicadosFiltrados = remember(duplicadas, q) {
        if (q.isEmpty()) duplicadas.size
        else duplicadas.count { grupo -> grupo.any { coincideBusquedaSalud(it, q) } }
    }
    val totalDuplicadasFiltradas = remember(duplicadas, q) {
        if (q.isEmpty()) duplicadas.sumOf { it.size }
        else duplicadas.sumOf { grupo -> grupo.count { coincideBusquedaSalud(it, q) } }
    }
    val muyComunesFiltrados = remember(muyComunes, q) {
        if (q.isEmpty()) muyComunes.size
        else muyComunes.count { coincideBusquedaSalud(it, q) }
    }
    val debilesFiltrados = remember(debiles, q) {
        if (q.isEmpty()) debiles.size
        else debiles.count { coincideBusquedaSalud(it, q) }
    }
    val antiguasFiltradas = remember(antiguas, q) {
        if (q.isEmpty()) antiguas.size
        else antiguas.count { coincideBusquedaSalud(it, q) }
    }
    val ignoradasFiltradas = remember(ignoradas, q) {
        if (q.isEmpty()) ignoradas.size
        else ignoradas.count { coincideBusquedaSalud(it, q) }
    }

    SelectorPestanasSalud(
        pestanaActiva = pestanaActiva,
        alSeleccionarPestana = alSeleccionarPestana,
        gruposDuplicadosCount = duplicadas.size,
        totalDuplicadasCount = duplicadas.sumOf { it.size },
        muyComunesCount = muyComunes.size,
        debilesCount = debiles.size,
        antiguasCount = antiguas.size,
        ignoradasCount = ignoradas.size,
        textoBusqueda = textoBusqueda,
        gruposDuplicadosFiltrados = gruposDuplicadosFiltrados,
        totalDuplicadasFiltradas = totalDuplicadasFiltradas,
        muyComunesFiltrados = muyComunesFiltrados,
        debilesFiltrados = debilesFiltrados,
        antiguasFiltradas = antiguasFiltradas,
        ignoradasFiltradas = ignoradasFiltradas,
        modifier = modifier
    )
}

