/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraInferiorSeleccion
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Gestiona las acciones flotantes inferiores de PantallaLista:
 * - Botones flotantes normales (Bloquear / Nueva entrada) cuando no hay selección múltiple activa.
 * - Barra inferior de selección múltiple cuando hay elementos seleccionados.
 */
@Composable
fun BoxScope.CapaInferiorAccionesLista(
    estadoSeleccion: EstadoSeleccionLista,
    entradas: List<Entrada>,
    vm: VaultViewModel,
    haptica: Haptica,
    tieneAlertaAccesibilidad: Boolean = false,
    alAbrirAuditoriaAccesibilidad: (() -> Unit)? = null,
    alTransferirCxf: (List<Entrada>) -> Unit,
    alRenombrar: (String) -> Unit,
    alAsignarCategoria: () -> Unit,
    alBorrar: () -> Unit
) {
    if (!estadoSeleccion.modoSeleccion) {
        ColumnaAccionesFlotantesLista(
            alBloquear = {
                haptica.toque()
                vm.bloquear()
            },
            alNuevaEntrada = {
                haptica.toque()
                vm.ir(Pantalla.Editar(null))
            },
            tieneAlertaAccesibilidad = tieneAlertaAccesibilidad,
            alAbrirAuditoriaAccesibilidad = alAbrirAuditoriaAccesibilidad,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    } else {
        val itemsSeleccionados = remember(entradas, estadoSeleccion.seleccionados) {
            entradas.filter { estadoSeleccion.seleccionados.contains(it.id) }
        }
        val todosSonFavoritos = remember(itemsSeleccionados) {
            itemsSeleccionados.isNotEmpty() && itemsSeleccionados.all { it.favorito }
        }

        BarraInferiorSeleccion(
            cantidad = estadoSeleccion.seleccionados.size,
            todosSonFavoritos = todosSonFavoritos,
            alAlternarFavoritos = {
                val ids = estadoSeleccion.seleccionados.toSet()
                estadoSeleccion.salirDeSeleccion()
                vm.alternarFavoritosVarias(ids)
            },
            alComparar = {
                val listaComparar = estadoSeleccion.seleccionados.toList()
                if (listaComparar.size >= 2) {
                    estadoSeleccion.salirDeSeleccion()
                    vm.ir(
                        Pantalla.Detalle(
                            id = listaComparar.first(),
                            idsContexto = listaComparar,
                            modoComparacion = true
                        )
                    )
                }
            },
            alRespaldar = {
                val ids = estadoSeleccion.seleccionados.joinToString(",")
                estadoSeleccion.salirDeSeleccion()
                vm.ir(Pantalla.ExportarSelectivo("ids:$ids"))
            },
            alTransferirCxf = {
                val copia = itemsSeleccionados.toList()
                estadoSeleccion.salirDeSeleccion()
                alTransferirCxf(copia)
            },
            alRenombrar = {
                val ids = estadoSeleccion.seleccionados.toList()
                estadoSeleccion.salirDeSeleccion()
                vm.ir(Pantalla.NormalizadorTitulos(idsEspecificos = ids))
            },
            alAsignarCategoria = alAsignarCategoria,
            alUnificar = {
                val copia = itemsSeleccionados.toList()
                if (copia.size >= 2) {
                    estadoSeleccion.salirDeSeleccion()
                    val principal = com.jlnavas3.bovedalocal.data.AnalizadorDuplicados.seleccionarMejorEntrada(copia)
                    val secundarias = copia.filterNot { it.id == principal.id }
                    vm.unificarEntradas(principal = principal, secundarias = secundarias)
                }
            },
            alBorrar = alBorrar,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
