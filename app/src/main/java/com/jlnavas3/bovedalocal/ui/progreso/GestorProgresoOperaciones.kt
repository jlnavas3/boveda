/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.progreso

import com.jlnavas3.bovedalocal.data.EstadoProgresoOperacion
import com.jlnavas3.bovedalocal.data.ItemProgresoEntrada
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Gestor singleton y observable para coordinar el progreso de operaciones
 * masivas e importaciones en segundo plano.
 */
object GestorProgresoOperaciones {

    private const val MAX_ITEMS_VISIBLES = 4

    private val _estado = MutableStateFlow<EstadoProgresoOperacion?>(null)
    val estado: StateFlow<EstadoProgresoOperacion?> = _estado.asStateFlow()

    private val _minimizado = MutableStateFlow(false)
    val minimizado: StateFlow<Boolean> = _minimizado.asStateFlow()

    fun iniciar(
        id: String,
        titulo: String,
        faseInicial: String = "Preparando...",
        totalEstimado: Int = 0
    ) {
        _minimizado.value = false
        _estado.value = EstadoProgresoOperacion(
            idOperacion = id,
            titulo = titulo,
            fase = faseInicial,
            progresoGlobal = 0.05f,
            totalCuentas = totalEstimado,
            enEjecucion = true
        )
    }

    fun actualizarFase(fase: String, progresoGlobal: Float) {
        _estado.value = _estado.value?.copy(
            fase = fase,
            progresoGlobal = progresoGlobal.coerceIn(0f, 1f)
        )
    }

    private const val MAX_HISTORIAL_ITEMS = 30

    fun reportarItem(
        item: ItemProgresoEntrada,
        progresoGlobal: Float,
        procesadas: Int = 0
    ) {
        val actual = _estado.value ?: return
        val listaActual = actual.itemsActivos.toMutableList()

        val idx = listaActual.indexOfFirst { it.id == item.id }
        if (idx >= 0) {
            listaActual[idx] = item
        } else {
            listaActual.add(item)
            if (listaActual.size > MAX_HISTORIAL_ITEMS) {
                listaActual.removeAt(0)
            }
        }

        _estado.value = actual.copy(
            itemsActivos = listaActual,
            progresoGlobal = progresoGlobal.coerceIn(0f, 1f),
            cuentasProcesadas = if (procesadas > 0) procesadas else actual.cuentasProcesadas
        )
    }

    fun completar(
        mensajeExito: String,
        totalCuentas: Int,
        textoAccionResultado: String? = null,
        textoAccionSecundaria: String? = "Ver en la lista",
        accionResultado: (() -> Unit)? = null
    ) {
        val actual = _estado.value ?: return
        val itemsFinalizados = actual.itemsActivos.map {
            if (!it.completado || it.progreso < 1f) it.copy(progreso = 1f, completado = true) else it
        }
        _estado.value = actual.copy(
            fase = mensajeExito,
            progresoGlobal = 1.0f,
            itemsActivos = itemsFinalizados,
            cuentasProcesadas = totalCuentas,
            totalCuentas = totalCuentas,
            enEjecucion = false,
            completado = true,
            textoAccionResultado = textoAccionResultado,
            textoAccionSecundaria = textoAccionSecundaria,
            accionResultado = accionResultado
        )
        // Si estaba minimizado, restaurar para que el usuario vea el resultado
        _minimizado.value = false
    }

    fun fallar(mensajeError: String) {
        val actual = _estado.value ?: return
        _estado.value = actual.copy(
            fase = "Error: $mensajeError",
            enEjecucion = false,
            completado = false,
            error = mensajeError
        )
        _minimizado.value = false
    }

    fun fijarMinimizado(valor: Boolean) {
        _minimizado.value = valor
    }

    fun alternarMinimizado() {
        _minimizado.value = !_minimizado.value
    }

    fun descartar() {
        _estado.value = null
        _minimizado.value = false
    }
}
