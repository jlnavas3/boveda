/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun ListaCuentasTotp(
    totpFiltrados: List<Entrada>,
    totalTotp: Int,
    ahora: Long,
    separarDigitos: Boolean,
    haptica: Haptica,
    alCopiarCodigo: (String) -> Unit,
    alAlternarFavorito: (String) -> Unit,
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null,
    seleccionActiva: Boolean = false,
    seleccionados: Set<String> = emptySet(),
    alPulsarLargo: (String) -> Unit = {},
    alAlternarSeleccion: (String) -> Unit = {},
    mostrarIndicadores: Boolean = false,
    espaciadoFilas: Dp = calcularEspaciadoFilas(),
    alVerDetalle: (String) -> Unit = {}
) {
    if (totpFiltrados.isEmpty()) {
        EstadoVacioAutenticador(hayCuentasTotp = totalTotp > 0)
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
        ) {
            totpFiltrados.forEach { entrada ->
                TarjetaCuentaTotp(
                    entrada = entrada,
                    ahora = ahora,
                    separarDigitos = separarDigitos,
                    haptica = haptica,
                    alCopiarCodigo = alCopiarCodigo,
                    alAlternarFavorito = { alAlternarFavorito(entrada.id) },
                    ajustes = ajustes,
                    seleccionActiva = seleccionActiva,
                    seleccionado = seleccionados.contains(entrada.id),
                    alPulsarLargo = { alPulsarLargo(entrada.id) },
                    alAlternarSeleccion = { alAlternarSeleccion(entrada.id) },
                    mostrarIndicadores = mostrarIndicadores,
                    alVerDetalle = { alVerDetalle(entrada.id) }
                )
            }
        }
    }
}
