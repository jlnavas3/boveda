/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.CerrarTecladoAlHacerScroll
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import com.jlnavas3.bovedalocal.ui.pantallas.lista.FilaEntrada
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Contenido principal scrolleable para la lista de entradas de un tipo con reciclaje de vistas mediante LazyColumn.
 */
@Composable
fun ContenidoListaEntradasTipo(
    infoTipo: InfoTipoEntrada,
    lazyListState: LazyListState,
    entradasFiltradas: List<Entrada>,
    todasLasEntradas: List<Entrada>,
    espaciadoFilas: Dp,
    haptica: Haptica,
    modoSeleccion: Boolean,
    seleccionados: Set<String>,
    ajustes: AjustesApp,
    vm: VaultViewModel,
    alCrearEntrada: () -> Unit,
    alPulsarLargo: (String) -> Unit,
    alAlternarSeleccion: (String) -> Unit,
    alPulsarEntrada: (Entrada) -> Unit,
    alAlternarFavorito: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    CerrarTecladoAlHacerScroll(lazyListState.isScrollInProgress)

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxWidth()
            .cerrarTecladoAlTocarFuera(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
    ) {
        item(key = "cabecera_descripcion") {
            DescripcionPantalla(
                subtitulo = if (entradasFiltradas.isEmpty()) {
                    "Sin elementos"
                } else {
                    "${entradasFiltradas.size} ${if (entradasFiltradas.size == 1) infoTipo.tituloSingular.lowercase() else infoTipo.tituloPlural.lowercase()} almacenada${if (entradasFiltradas.size == 1) "" else "s"}"
                }
            )
        }

        if (entradasFiltradas.isEmpty()) {
            item(key = "estado_vacio") {
                Spacer(Modifier.height(10.dp))
                EstadoVacioEntradasTipo(
                    infoTipo = infoTipo,
                    sinEntradasEnTotal = todasLasEntradas.isEmpty()
                )
            }
        } else {
            items(
                items = entradasFiltradas,
                key = { it.id }
            ) { entrada ->
                FilaEntrada(
                    entrada = entrada,
                    seleccionActiva = modoSeleccion,
                    seleccionado = seleccionados.contains(entrada.id),
                    alAbrir = { alPulsarEntrada(entrada) },
                    alCopiarUsuario = { vm.copiar("Usuario", entrada.usuario, sensible = false) },
                    alCopiarContrasena = { vm.copiar("Contraseña", entrada.contrasena, sensible = true) },
                    alFavorito = { alAlternarFavorito(entrada.id) },
                    alCopiarCodigo = { vm.copiar("Código 2FA", it, sensible = true) },
                    alPulsarLargo = { alPulsarLargo(entrada.id) },
                    alAlternarSeleccion = { alAlternarSeleccion(entrada.id) },
                    mostrarIndicadores = ajustes.mostrarIndicadoresContenido
                )
            }
        }
    }
}
