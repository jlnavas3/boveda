package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda

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
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            ChipBoveda(
                texto = "Todos",
                conteo = totalGrupos,
                seleccionado = filtroActivo == FiltroDuplicados.TODOS,
                alPulsar = { alSeleccionarFiltro(FiltroDuplicados.TODOS) }
            )
        }
        if (totalSobrantesIdenticas > 0) {
            item {
                ChipBoveda(
                    texto = "Idénticos",
                    conteo = totalSobrantesIdenticas,
                    seleccionado = filtroActivo == FiltroDuplicados.IDENTICOS,
                    alPulsar = { alSeleccionarFiltro(FiltroDuplicados.IDENTICOS) }
                )
            }
        }
        if (cantPasskeys > 0) {
            item {
                ChipBoveda(
                    texto = "Passkey",
                    conteo = cantPasskeys,
                    seleccionado = filtroActivo == FiltroDuplicados.PASSKEY,
                    alPulsar = { alSeleccionarFiltro(FiltroDuplicados.PASSKEY) }
                )
            }
        }
        if (cantTotp > 0) {
            item {
                ChipBoveda(
                    texto = "TOTP",
                    conteo = cantTotp,
                    seleccionado = filtroActivo == FiltroDuplicados.TOTP,
                    alPulsar = { alSeleccionarFiltro(FiltroDuplicados.TOTP) }
                )
            }
        }
        if (cantAppsAndroid > 0) {
            item {
                ChipBoveda(
                    texto = "Apps Android",
                    conteo = cantAppsAndroid,
                    seleccionado = filtroActivo == FiltroDuplicados.APPS_ANDROID,
                    alPulsar = { alSeleccionarFiltro(FiltroDuplicados.APPS_ANDROID) }
                )
            }
        }
        if (cantWeb > 0) {
            item {
                ChipBoveda(
                    texto = "Sitios web",
                    conteo = cantWeb,
                    seleccionado = filtroActivo == FiltroDuplicados.SITIOS_WEB,
                    alPulsar = { alSeleccionarFiltro(FiltroDuplicados.SITIOS_WEB) }
                )
            }
        }
        if (cantMismaCuenta > 0) {
            item {
                ChipBoveda(
                    texto = "Misma cuenta",
                    conteo = cantMismaCuenta,
                    seleccionado = filtroActivo == FiltroDuplicados.MISMA_CUENTA,
                    alPulsar = { alSeleccionarFiltro(FiltroDuplicados.MISMA_CUENTA) }
                )
            }
        }
        if (cantVariantes > 0) {
            item {
                ChipBoveda(
                    texto = "Variantes",
                    conteo = cantVariantes,
                    seleccionado = filtroActivo == FiltroDuplicados.VARIANTES,
                    alPulsar = { alSeleccionarFiltro(FiltroDuplicados.VARIANTES) }
                )
            }
        }
    }
}
