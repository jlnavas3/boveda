/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * CompositionLocal que provee un [State] estable con la elevación vertical recomendada (en Dp)
 * para los botones flotantes (FAB) cuando un aviso emergente está visible.
 * Al proveer un State<Dp> estable, los cambios de elevación no provocan recomposiciones
 * del árbol de pantallas, aislando el renderizado de forma ultra-rápida.
 */
val LocalDesplazamientoFabPorAviso = staticCompositionLocalOf<State<Dp>> { mutableStateOf(0.dp) }
