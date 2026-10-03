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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun BarraChipsFiltroDuplicados(
    filtroActivo: FiltroDuplicados,
    totalGrupos: Int,
    totalSobrantesIdenticas: Int,
    cantPasskeys: Int,
    cantTotp: Int,
    cantAppsAndroid: Int,
    cantWeb: Int,
    cantMismaCuenta: Int,
    cantVariantes: Int,
    alSeleccionarFiltro: (FiltroDuplicados) -> Unit
) {
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

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            FilterChip(
                selected = filtroActivo == FiltroDuplicados.TODOS,
                onClick = { alSeleccionarFiltro(FiltroDuplicados.TODOS) },
                label = { Text("Todos ($totalGrupos)") },
                colors = coloresChip,
                border = bordeChip
            )
        }
        if (totalSobrantesIdenticas > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.IDENTICOS,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.IDENTICOS) },
                    label = { Text("Idénticos ($totalSobrantesIdenticas)") },
                    colors = coloresChip,
                    border = bordeChip
                )
            }
        }
        if (cantPasskeys > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.PASSKEY,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.PASSKEY) },
                    label = { Text("Passkey ($cantPasskeys)") },
                    colors = coloresChip,
                    border = bordeChip
                )
            }
        }
        if (cantTotp > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.TOTP,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.TOTP) },
                    label = { Text("TOTP ($cantTotp)") },
                    colors = coloresChip,
                    border = bordeChip
                )
            }
        }
        if (cantAppsAndroid > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.APPS_ANDROID,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.APPS_ANDROID) },
                    label = { Text("Apps Android ($cantAppsAndroid)") },
                    colors = coloresChip,
                    border = bordeChip
                )
            }
        }
        if (cantWeb > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.SITIOS_WEB,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.SITIOS_WEB) },
                    label = { Text("Sitios web ($cantWeb)") },
                    colors = coloresChip,
                    border = bordeChip
                )
            }
        }
        if (cantMismaCuenta > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.MISMA_CUENTA,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.MISMA_CUENTA) },
                    label = { Text("Misma cuenta ($cantMismaCuenta)") },
                    colors = coloresChip,
                    border = bordeChip
                )
            }
        }
        if (cantVariantes > 0) {
            item {
                FilterChip(
                    selected = filtroActivo == FiltroDuplicados.VARIANTES,
                    onClick = { alSeleccionarFiltro(FiltroDuplicados.VARIANTES) },
                    label = { Text("Variantes ($cantVariantes)") },
                    colors = coloresChip,
                    border = bordeChip
                )
            }
        }
    }
}
