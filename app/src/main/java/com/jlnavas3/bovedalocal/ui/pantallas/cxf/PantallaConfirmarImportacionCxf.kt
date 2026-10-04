package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.cxf.CxfConvertidor
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla que permite al usuario revisar y seleccionar qué credenciales transferidas
 * vía CXF desea incorporar a su bóveda local.
 */
@Composable
fun PantallaConfirmarImportacionCxf(
    vm: VaultViewModel,
    jsonCxf: String
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()

    val entradasExistentes = (estadoBoveda as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    val resultadoCxf = remember(jsonCxf) {
        CxfConvertidor.convertir(jsonCxf)
    }

    val items = remember(resultadoCxf, entradasExistentes) {
        mutableStateListOf<ItemSeleccionableCxf>().apply {
            val transformados = resultadoCxf.entradas.map { ent ->
                val yaExiste = entradasExistentes.any { existente ->
                    (existente.usuario.equals(ent.usuario, ignoreCase = true) || ent.usuario.isBlank()) &&
                        (
                            (ent.passkey != null && existente.passkey?.rpId.equals(ent.passkey?.rpId, ignoreCase = true)) ||
                            ent.urls.any { u -> existente.urls.any { eu -> Dominios.coincide(u, eu) } } ||
                            (ent.titulo.isNotBlank() && existente.titulo.equals(ent.titulo, ignoreCase = true))
                        )
                }
                ItemSeleccionableCxf(
                    entrada = ent,
                    seleccionada = !yaExiste,
                    yaExisteEnBoveda = yaExiste
                )
            }
            addAll(transformados)
        }
    }

    val cuantasSeleccionadas = items.count { it.seleccionada }
    val todasSeleccionadas = items.isNotEmpty() && items.all { it.seleccionada }
    val ningunaSeleccionada = items.none { it.seleccionada }
    val soloNuevasSeleccionadas = items.isNotEmpty() && !todasSeleccionadas && !ningunaSeleccionada && items.all { it.seleccionada == !it.yaExisteEnBoveda }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BarraSuperiorPantalla(
                titulo = "Importar credenciales",
                idEtiqueta = "05-COP-CXF-IMP",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val exportadorNombre = resultadoCxf.exportador?.ifBlank { "Proveedor del sistema" } ?: "Proveedor del sistema"
                TarjetaResumenTransferenciaCxf(
                    exportadorNombre = exportadorNombre,
                    totalItems = items.size,
                    resultadoCxf = resultadoCxf,
                    soloNuevasSeleccionadas = soloNuevasSeleccionadas,
                    todasSeleccionadas = todasSeleccionadas,
                    ningunaSeleccionada = ningunaSeleccionada,
                    alSeleccionarSoloNuevas = {
                        haptica.tic()
                        for (i in items.indices) {
                            items[i] = items[i].copy(seleccionada = !items[i].yaExisteEnBoveda)
                        }
                    },
                    alSeleccionarTodas = {
                        haptica.tic()
                        for (i in items.indices) {
                            items[i] = items[i].copy(seleccionada = true)
                        }
                    },
                    alSeleccionarNinguna = {
                        haptica.tic()
                        for (i in items.indices) {
                            items[i] = items[i].copy(seleccionada = false)
                        }
                    }
                )

                Spacer(Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    itemsIndexed(items, key = { _, item -> item.entrada.id }) { index, item ->
                        FilaConfirmacionCxf(
                            item = item,
                            alAlternar = {
                                haptica.tic()
                                items[index] = item.copy(seleccionada = !item.seleccionada)
                            },
                            onCheckedChange = { checked ->
                                items[index] = item.copy(seleccionada = checked)
                            }
                        )
                    }
                }
            }
        }

        BotonImportarFabCxf(
            cuantasSeleccionadas = cuantasSeleccionadas,
            alImportar = {
                val seleccionadas = items.filter { it.seleccionada }.map { it.entrada }
                if (seleccionadas.isNotEmpty()) {
                    haptica.exito()
                    vm.importarEntradasCxf(seleccionadas) {
                        vm.irRaiz(Pantalla.Lista)
                    }
                } else {
                    haptica.error()
                    vm.mostrarAviso("Selecciona al menos una credencial para importar")
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}
