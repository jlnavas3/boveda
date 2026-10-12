/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AvisoInterno
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoAvisoInterno
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.CalculadorAvisosInternos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Delegado encargado de gestionar los avisos y recomendaciones internas de la bóveda.
 */
interface VaultAvisosDelegate {
    val repositorio: VaultRepository
    val avisosInternosFlow: MutableStateFlow<List<AvisoInterno>>
    val scopeAvisos: CoroutineScope
    fun obtenerApp(): android.app.Application

    val avisosInternos: StateFlow<List<AvisoInterno>> get() = avisosInternosFlow.asStateFlow()

    fun recalcularAvisosInternos() {
        val estado = repositorio.estado.value
        if (estado !is EstadoBoveda.Desbloqueada) {
            avisosInternosFlow.value = emptyList()
            return
        }
        scopeAvisos.launch(Dispatchers.Default) {
            val entradas = estado.entradas
            val ajustes = repositorio.ajustes.actual
            val modificado = repositorio.archivoBoveda.lastModified()
            val calculados = CalculadorAvisosInternos.calcular(
                entradas = entradas,
                ajustes = ajustes,
                archivoBovedaModificado = modificado,
                contexto = obtenerApp()
            )
            avisosInternosFlow.value = calculados

            if (ajustes.avisos.conteoAvisosEnBloqueo != calculados.size) {
                repositorio.ajustes.actualizar {
                    it.copy(avisos = it.avisos.copy(conteoAvisosEnBloqueo = calculados.size))
                }
            }
        }
    }

    fun ignorarAviso(tipo: TipoAvisoInterno) {
        val actualIgnorados = repositorio.ajustes.actual.avisos.avisosIgnorados
        repositorio.ajustes.actualizar {
            it.copy(avisos = it.avisos.copy(avisosIgnorados = actualIgnorados + tipo.name))
        }
        recalcularAvisosInternos()
    }

    fun restablecerAvisosIgnorados() {
        repositorio.ajustes.actualizar {
            it.copy(avisos = it.avisos.copy(avisosIgnorados = emptySet()))
        }
        recalcularAvisosInternos()
    }
}

