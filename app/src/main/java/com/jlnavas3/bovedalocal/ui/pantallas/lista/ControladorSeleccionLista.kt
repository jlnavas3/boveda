package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Gestor de estado para la selección múltiple en PantallaLista.
 */
@Stable
class EstadoSeleccionLista(
    private val haptica: Haptica
) {
    var modoSeleccion by mutableStateOf(false)
        private set

    var seleccionados by mutableStateOf(setOf<String>())
        private set

    fun salirDeSeleccion() {
        modoSeleccion = false
        seleccionados = emptySet()
    }

    fun entrarEnSeleccion(id: String) {
        modoSeleccion = true
        seleccionados = setOf(id)
        haptica.tic()
    }

    fun alternarSeleccion(id: String) {
        seleccionados = if (seleccionados.contains(id)) seleccionados - id else seleccionados + id
        if (seleccionados.isEmpty()) modoSeleccion = false
    }

    fun entrarEnSeleccionLote(ids: Set<String>) {
        if (ids.isEmpty()) return
        haptica.tic()
        modoSeleccion = true
        seleccionados = seleccionados + ids
    }

    fun alternarSeleccionLote(ids: Set<String>) {
        if (ids.isEmpty()) return
        haptica.tic()
        val todosEstanSeleccionados = seleccionados.containsAll(ids)
        seleccionados = if (todosEstanSeleccionados) seleccionados - ids else seleccionados + ids
        modoSeleccion = seleccionados.isNotEmpty()
    }

    fun alternarSeleccionarTodo(visiblesIds: List<String>) {
        haptica.tic()
        val todoSeleccionado = visiblesIds.isNotEmpty() && seleccionados.containsAll(visiblesIds)
        if (todoSeleccionado) {
            seleccionados = emptySet()
            modoSeleccion = false
        } else {
            modoSeleccion = true
            seleccionados = visiblesIds.toSet()
        }
    }

    fun sincronizarConEntradasVivas(entradas: List<Entrada>) {
        if (modoSeleccion) {
            val vivos = entradas.map { it.id }.toSet()
            seleccionados = seleccionados.intersect(vivos)
            if (seleccionados.isEmpty()) modoSeleccion = false
        }
    }
}

@Composable
fun rememberEstadoSeleccionLista(
    entradas: List<Entrada>,
    haptica: Haptica
): EstadoSeleccionLista {
    val estado = remember { EstadoSeleccionLista(haptica) }

    LaunchedEffect(entradas) {
        estado.sincronizarConEntradasVivas(entradas)
    }

    return estado
}
