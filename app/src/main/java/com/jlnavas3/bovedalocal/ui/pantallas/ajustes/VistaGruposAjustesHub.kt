/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes

@Composable
fun VistaGruposAjustesHub(
    todosLosElementos: List<ElementoMenuAjustes>,
    ajustes: AjustesApp,
    vm: VaultViewModel,
    seccionDestino: String? = null,
    alAbrirNombreBoveda: () -> Unit = {},
    alAbrirProveedorPasskeys: () -> Unit = {},
    alAbrirCambioMaestra: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 1. Obtener los grupos Nivel 1 en el orden personalizado dinámico
    val gruposN1 = remember(ajustes.ordenAjustesPersonalizado, ajustes.reparentingPersonalizado) {
        MapaAjustes.obtenerNodosRaiz(
            ordenPersonalizado = ajustes.ordenAjustesPersonalizado,
            reparenting = ajustes.reparentingPersonalizado
        )
    }

    // 2. Resolver los elementos del nivel raíz (grupos colapsables y ajustes directos)
    val elementosRaiz = remember(
        gruposN1,
        ajustes.ordenJerarquiaPersonalizado,
        ajustes.reparentingPersonalizado,
        todosLosElementos
    ) {
        val mapaElementos = todosLosElementos.associateBy { it.idEtiqueta }
        gruposN1.mapNotNull { nodo ->
            val hijos = MapaAjustes.obtenerHijosDe(
                padreId = nodo.id,
                ordenPersonalizado = ajustes.ordenJerarquiaPersonalizado,
                reparenting = ajustes.reparentingPersonalizado
            )

            if (hijos.isNotEmpty()) {
                val elementosDelGrupo = hijos.mapNotNull { hijoNodo ->
                    mapaElementos[hijoNodo.id] ?: run {
                        // Elemento adoptado o promovido dinámicamente desde subniveles
                        val (icono, color) = MapaAjustes.resolverIconoYColor(hijoNodo)
                        val accion: () -> Unit = when {
                            hijoNodo.accionEspecial == AccionEspecialAjuste.CAMBIAR_MAESTRA -> alAbrirCambioMaestra
                            hijoNodo.accionEspecial == AccionEspecialAjuste.NOMBRE_BOVEDA -> alAbrirNombreBoveda
                            hijoNodo.accionEspecial == AccionEspecialAjuste.PROVEEDOR_PASSKEYS -> alAbrirProveedorPasskeys
                            hijoNodo.pantallaDestino != null -> { { vm.ir(hijoNodo.pantallaDestino) } }
                            else -> { { vm.irPorId(hijoNodo.id) } }
                        }
                        ElementoMenuAjustes(
                            titulo = hijoNodo.titulo,
                            subtitulo = hijoNodo.subtitulo,
                            icono = icono,
                            colorIcono = color,
                            idEtiqueta = hijoNodo.id,
                            grupo = nodo.titulo,
                            palabrasClave = hijoNodo.palabrasClave.joinToString(" "),
                            valorTexto = null,
                            alPulsar = accion
                        )
                    }
                }

                if (elementosDelGrupo.isNotEmpty()) {
                    ElementoRaizHub.GrupoColapsable(grupo = nodo, elementos = elementosDelGrupo)
                } else null
            } else {
                // Ajuste directo individual en el nivel raíz (ej. promovido por el usuario)
                val accion: () -> Unit = when {
                    nodo.accionEspecial == AccionEspecialAjuste.CAMBIAR_MAESTRA -> alAbrirCambioMaestra
                    nodo.accionEspecial == AccionEspecialAjuste.NOMBRE_BOVEDA -> alAbrirNombreBoveda
                    nodo.accionEspecial == AccionEspecialAjuste.PROVEEDOR_PASSKEYS -> alAbrirProveedorPasskeys
                    nodo.pantallaDestino != null -> { { vm.ir(nodo.pantallaDestino) } }
                    else -> { { vm.irPorId(nodo.id) } }
                }
                ElementoRaizHub.AjusteDirecto(nodo = nodo, alPulsar = accion)
            }
        }
    }

    // Auto-expandir el grupo si hay un destino específico (deep linking / resaltado)
    val grupoParaDestino = remember(seccionDestino, elementosRaiz) {
        if (seccionDestino.isNullOrBlank()) null
        else {
            elementosRaiz.firstOrNull { elem ->
                when (elem) {
                    is ElementoRaizHub.GrupoColapsable -> elem.id == seccionDestino || elem.elementos.any { it.idEtiqueta == seccionDestino }
                    is ElementoRaizHub.AjusteDirecto -> elem.id == seccionDestino
                }
            }?.id
        }
    }

    var gruposExpandidos by rememberSaveable {
        mutableStateOf(if (grupoParaDestino != null) setOf(grupoParaDestino) else emptySet())
    }

    LaunchedEffect(grupoParaDestino) {
        if (grupoParaDestino != null && grupoParaDestino !in gruposExpandidos) {
            gruposExpandidos = gruposExpandidos + grupoParaDestino
        }
    }

    Column(modifier = modifier) {
        elementosRaiz.forEachIndexed { index, elem ->
            if (index > 0) Spacer(Modifier.height(EspaciadoComponentes))

            when (elem) {
                is ElementoRaizHub.GrupoColapsable -> {
                    val expandido = elem.grupo.id in gruposExpandidos

                    TarjetaGrupoColapsableHub(
                        grupo = elem.grupo,
                        elementos = elem.elementos,
                        expandido = expandido,
                        alAlternarExpandido = {
                            gruposExpandidos = if (expandido) {
                                gruposExpandidos - elem.grupo.id
                            } else {
                                gruposExpandidos + elem.grupo.id
                            }
                        },
                        mostrarId = ajustes.mostrarIdsAjustes
                    )
                }
                is ElementoRaizHub.AjusteDirecto -> {
                    TarjetaAjusteDirectoHub(
                        nodo = elem.nodo,
                        alPulsar = elem.alPulsar,
                        mostrarId = ajustes.mostrarIdsAjustes
                    )
                }
            }
        }
    }
}
