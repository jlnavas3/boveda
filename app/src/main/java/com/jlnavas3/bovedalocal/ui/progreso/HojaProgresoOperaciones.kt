/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.progreso

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta

/**
 * Contenedor visual desacoplado que renderiza el panel flotante inferior de operaciones
 * o la píldora minimizada según el estado de `GestorProgresoOperaciones`.
 */
@Composable
fun HojaProgresoOperaciones(
    modifier: Modifier = Modifier
) {
    val estado by GestorProgresoOperaciones.estado.collectAsStateWithLifecycle()
    val minimizado by GestorProgresoOperaciones.minimizado.collectAsStateWithLifecycle()
    var detallesExpandidos by remember { mutableStateOf(false) }

    val operacion = estado ?: return

    val formaSheet = RoundedCornerShape(CurvaturaEsquinas)

    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Scrim exterior transparente: al tocar fuera del panel desplegado, se minimiza a la píldora
        if (!minimizado) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            GestorProgresoOperaciones.fijarMinimizado(true)
                        }
                    )
            )
        }

        // 1. Píldora minimizada (si el usuario la plegó)
        PildoraProgresoFlotante(
            estado = operacion,
            visible = minimizado,
            alReabrir = { GestorProgresoOperaciones.fijarMinimizado(false) },
            alCerrar = { GestorProgresoOperaciones.descartar() }
        )

        // 2. Panel desplegado completo
        AnimatedVisibility(
            visible = !minimizado,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .shadow(elevation = 16.dp, shape = formaSheet)
                    .clip(formaSheet)
                    .background(SuperficieAlta)
                    .border(
                        width = GrosorBorde.coerceAtMost(1.5.dp),
                        color = ColorBordeActual.copy(alpha = 0.45f),
                        shape = formaSheet
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /* Consume clicks inside sheet so it doesn't minimize */ }
                    )
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            when {
                                delta < -15 && operacion.itemsActivos.isNotEmpty() -> {
                                    // Arrastre hacia arriba: desplegar lista de entradas
                                    detallesExpandidos = true
                                }
                                delta > 20 -> {
                                    // Arrastre hacia abajo: si hay detalles expandidos se repliegan, si no se minimiza
                                    if (detallesExpandidos) {
                                        detallesExpandidos = false
                                    } else if (operacion.enEjecucion) {
                                        GestorProgresoOperaciones.fijarMinimizado(true)
                                    }
                                }
                            }
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Cabecera con barra general y controles (permite arrastre o toque para expandir/plegar)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (operacion.itemsActivos.isNotEmpty()) {
                                    detallesExpandidos = !detallesExpandidos
                                }
                            }
                        )
                ) {
                    CabeceraProgresoOperacion(
                        estado = operacion,
                        alMinimizar = { GestorProgresoOperaciones.fijarMinimizado(true) },
                        alCerrar = { GestorProgresoOperaciones.descartar() }
                    )
                }

                // Lista de entradas activa: oculta por defecto, solo visible al expandir
                AnimatedVisibility(
                    visible = detallesExpandidos && operacion.itemsActivos.isNotEmpty(),
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        ListaProgresoEntradasActivas(items = operacion.itemsActivos)
                    }
                }

                // Pie con botones de acción al completar
                if (operacion.completado) {
                    Spacer(modifier = Modifier.height(10.dp))
                    PieProgresoExitoAcciones(
                        textoPrincipal = operacion.textoAccionResultado,
                        textoSecundario = operacion.textoAccionSecundaria,
                        alPulsarPrincipal = {
                            val accion = operacion.accionResultado
                            GestorProgresoOperaciones.descartar()
                            accion?.invoke()
                        },
                        alPulsarSecundario = {
                            GestorProgresoOperaciones.descartar()
                        }
                    )
                }
            }
        }
    }
}
