package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.GrupoDuplicado
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente que renderiza el scroll LazyColumn de las entradas o grupos duplicados.
 */
@Composable
fun ContenidoListaDuplicados(
    gruposFiltrados: List<GrupoDuplicado>,
    agruparPorSitio: Boolean,
    espaciadoFilas: Dp,
    modoSeleccion: Boolean,
    totalSobrantesIdenticas: Int,
    ajustes: AjustesApp,
    gruposExpandidos: Set<String>,
    seleccionados: Set<String>,
    haptica: Haptica,
    alAlternarGrupo: (String) -> Unit,
    alConservarCopia: (Entrada, GrupoDuplicado) -> Unit,
    alVerDetalle: (String, List<String>) -> Unit,
    alAlternarSeleccion: (String) -> Unit,
    alPulsarLargo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(espaciadoFilas),
        contentPadding = PaddingValues(bottom = if (modoSeleccion) 96.dp else if (totalSobrantesIdenticas > 0) 88.dp else 16.dp)
    ) {
        if (!agruparPorSitio) {
            val todasEntradas = gruposFiltrados.flatMap { grupo ->
                grupo.entradas.map { entrada ->
                    Triple(entrada, entrada.id == grupo.sugeridaPrincipal.id, grupo)
                }
            }
            items(todasEntradas, key = { it.first.id }) { (entrada, esSugerida, grupo) ->
                FilaEntradaDuplicada(
                    entrada = entrada,
                    esSugerida = esSugerida,
                    alConservar = { alConservarCopia(entrada, grupo) },
                    alVerDetalle = { alVerDetalle(entrada.id, grupo.entradas.map { it.id }) },
                    ajustes = ajustes,
                    mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                    enGrupo = false,
                    seleccionActiva = modoSeleccion,
                    seleccionado = seleccionados.contains(entrada.id),
                    alAlternarSeleccion = { alAlternarSeleccion(entrada.id) },
                    alPulsarLargo = {
                        haptica.toque()
                        alPulsarLargo(entrada.id)
                    }
                )
            }
        } else {
            items(gruposFiltrados, key = { it.idGrupo }) { grupo ->
                TarjetaGrupoDuplicado(
                    grupo = grupo,
                    expandido = gruposExpandidos.contains(grupo.idGrupo),
                    alAlternar = { alAlternarGrupo(grupo.idGrupo) },
                    alConservar = { elegida -> alConservarCopia(elegida, grupo) },
                    alVerDetalle = { id -> alVerDetalle(id, grupo.entradas.map { it.id }) },
                    ajustes = ajustes,
                    mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                    seleccionActiva = modoSeleccion,
                    seleccionados = seleccionados,
                    alAlternarSeleccion = alAlternarSeleccion,
                    alPulsarLargo = { id ->
                        haptica.toque()
                        alPulsarLargo(id)
                    }
                )
            }
        }
    }
}
