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
    avisos: List<com.jlnavas3.bovedalocal.data.AvisoInterno> = emptyList(),
    alNavegarAviso: ((Pantalla) -> Unit)? = null,
    alTransferirCxf: (List<Entrada>) -> Unit,
    alRenombrar: (String) -> Unit,
    alAsignarCategoria: () -> Unit,
    alSolicitarUnificar: (List<Entrada>) -> Unit,
    alBorrar: () -> Unit
) {
    if (!estadoSeleccion.modoSeleccion) {
        BarraAccionesListaPrincipal(
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
            avisos = avisos,
            alNavegarAviso = alNavegarAviso,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    } else {
        val itemsSeleccionados = remember(entradas, estadoSeleccion.seleccionados) {
            entradas.filter { estadoSeleccion.seleccionados.contains(it.id) }
        }
        val todosSonFavoritos = remember(itemsSeleccionados) {
            itemsSeleccionados.isNotEmpty() && itemsSeleccionados.all { it.favorito }
        }
        val todosSonAnclados = remember(itemsSeleccionados) {
            itemsSeleccionados.isNotEmpty() && itemsSeleccionados.all { it.anclado }
        }
        val todosIgnorados = remember(itemsSeleccionados) {
            itemsSeleccionados.isNotEmpty() && itemsSeleccionados.all { it.ignoradaEnSalud }
        }
        val tieneCredencialesCxf = remember(itemsSeleccionados) {
            itemsSeleccionados.any {
                it.tipo == com.jlnavas3.bovedalocal.data.TipoEntrada.LOGIN ||
                it.tipo == com.jlnavas3.bovedalocal.data.TipoEntrada.PASSKEY ||
                it.passkey != null ||
                it.contrasena.isNotBlank() ||
                !it.secretoTotp.isNullOrBlank()
            }
        }
        val puedeUnificar = remember(itemsSeleccionados) {
            itemsSeleccionados.size >= 2 && itemsSeleccionados.all {
                it.tipo == com.jlnavas3.bovedalocal.data.TipoEntrada.LOGIN || it.tipo == com.jlnavas3.bovedalocal.data.TipoEntrada.PASSKEY
            }
        }
        val tieneLoginAuditable = remember(itemsSeleccionados) {
            itemsSeleccionados.any {
                it.tipo == com.jlnavas3.bovedalocal.data.TipoEntrada.LOGIN && it.contrasena.isNotBlank()
            }
        }

        BarraInferiorSeleccion(
            cantidad = estadoSeleccion.seleccionados.size,
            todosSonFavoritos = todosSonFavoritos,
            alAlternarFavoritos = {
                val ids = estadoSeleccion.seleccionados.toSet()
                estadoSeleccion.salirDeSeleccion()
                vm.alternarFavoritosVarias(ids)
            },
            todosSonAnclados = todosSonAnclados,
            alAlternarAnclados = {
                val ids = estadoSeleccion.seleccionados.toSet()
                estadoSeleccion.salirDeSeleccion()
                vm.alternarAnclados(ids)
            },
            alAlternarIgnorarAuditoria = if (tieneLoginAuditable) {
                {
                    val ids = estadoSeleccion.seleccionados.toSet()
                    estadoSeleccion.salirDeSeleccion()
                    vm.alternarIgnorarSaludVarias(ids)
                }
            } else null,
            todosIgnorados = todosIgnorados,
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
            alTransferirCxf = if (tieneCredencialesCxf) {
                {
                    val copia = itemsSeleccionados.toList()
                    estadoSeleccion.salirDeSeleccion()
                    alTransferirCxf(copia)
                }
            } else null,
            alRenombrar = {
                val ids = estadoSeleccion.seleccionados.toList()
                estadoSeleccion.salirDeSeleccion()
                vm.ir(Pantalla.NormalizadorTitulos(idsEspecificos = ids))
            },
            alAsignarCategoria = alAsignarCategoria,
            alUnificar = if (puedeUnificar) {
                {
                    val copia = itemsSeleccionados.toList()
                    if (copia.size >= 2) {
                        alSolicitarUnificar(copia)
                    }
                }
            } else null,
            alBorrar = alBorrar,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
