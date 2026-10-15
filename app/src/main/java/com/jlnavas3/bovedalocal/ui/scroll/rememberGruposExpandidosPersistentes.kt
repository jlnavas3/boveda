/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.scroll

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Composable desacoplado que crea y vincula un estado mutable de [Set] de cadenas con
 * [GestorGruposExpandidos] bajo una [clave] única, permitiendo que las secciones o grupos
 * desplegados se recuerden al salir y volver a la pantalla.
 */
@Composable
fun rememberGruposExpandidosPersistentes(
    clave: String,
    gestor: GestorGruposExpandidos?
): MutableState<Set<String>> {
    if (gestor == null) {
        return remember { mutableStateOf(emptySet()) }
    }
    val estadoGrupos = remember(clave) {
        mutableStateOf(gestor.obtenerGrupos(clave))
    }

    DisposableEffect(clave, estadoGrupos.value) {
        gestor.guardarGrupos(clave, estadoGrupos.value)
        onDispose {
            gestor.guardarGrupos(clave, estadoGrupos.value)
        }
    }

    return estadoGrupos
}
