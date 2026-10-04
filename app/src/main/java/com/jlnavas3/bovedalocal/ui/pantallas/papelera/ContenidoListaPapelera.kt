package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista
import com.jlnavas3.bovedalocal.ui.pantallas.salud.diasDesde
import com.jlnavas3.bovedalocal.util.ItemAgrupado

private const val DIAS_PAPELERA = 30L

/**
 * Muestra el estado vacío o la lista (individual o agrupada) de entradas en la papelera.
 */
@Composable
fun ContenidoListaPapelera(
    papelera: List<Entrada>,
    ordenadas: List<Entrada>,
    itemsAgrupados: List<ItemAgrupado>,
    gruposExpandidos: Set<String>,
    ajustes: AjustesApp,
    ahora: Long,
    espaciadoFilas: Dp,
    alAlternarGrupo: (String) -> Unit,
    alRestaurar: (Entrada) -> Unit,
    alBorrarDefinitivo: (Entrada) -> Unit,
    modifier: Modifier = Modifier
) {
    if (papelera.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IlustracionPapeleraVacia()
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
        ) {
            if (!ajustes.agruparPorSitio) {
                items(ordenadas, key = { it.id }) { entrada ->
                    val diasRestantes = (DIAS_PAPELERA - diasDesde(entrada.eliminadaEn, ahora)).coerceAtLeast(0)
                    FilaPapeleraNativa(
                        entrada = entrada,
                        diasRestantes = diasRestantes,
                        alRestaurar = { alRestaurar(entrada) },
                        alBorrarDefinitivo = { alBorrarDefinitivo(entrada) },
                        ajustes = ajustes,
                        mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                        enGrupo = false
                    )
                }
            } else {
                items(itemsAgrupados, key = { item ->
                    when (item) {
                        is ItemAgrupado.Grupo -> "grupo_${item.clave}"
                        is ItemAgrupado.Suelto -> "suelto_${item.entrada.id}"
                        is ItemAgrupado.Hijo -> "hijo_${item.entrada.id}"
                    }
                }) { item ->
                    when (item) {
                        is ItemAgrupado.Grupo -> {
                            ComponenteGrupoLista(
                                clave = item.clave,
                                entradas = item.entradas,
                                expandido = gruposExpandidos.contains(item.clave),
                                alAlternar = { alAlternarGrupo(item.clave) },
                                contenidoEntrada = { entradaHija, _, _ ->
                                    val diasRestantes = (DIAS_PAPELERA - diasDesde(entradaHija.eliminadaEn, ahora)).coerceAtLeast(0)
                                    FilaPapeleraNativa(
                                        entrada = entradaHija,
                                        diasRestantes = diasRestantes,
                                        alRestaurar = { alRestaurar(entradaHija) },
                                        alBorrarDefinitivo = { alBorrarDefinitivo(entradaHija) },
                                        ajustes = ajustes,
                                        mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                        enGrupo = true
                                    )
                                }
                            )
                        }
                        is ItemAgrupado.Suelto -> {
                            val diasRestantes = (DIAS_PAPELERA - diasDesde(item.entrada.eliminadaEn, ahora)).coerceAtLeast(0)
                            FilaPapeleraNativa(
                                entrada = item.entrada,
                                diasRestantes = diasRestantes,
                                alRestaurar = { alRestaurar(item.entrada) },
                                alBorrarDefinitivo = { alBorrarDefinitivo(item.entrada) },
                                ajustes = ajustes,
                                mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                enGrupo = false
                            )
                        }
                        is ItemAgrupado.Hijo -> Unit
                    }
                }
            }
        }
    }
}
