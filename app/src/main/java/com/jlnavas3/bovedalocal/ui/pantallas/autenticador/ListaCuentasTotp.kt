package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
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
