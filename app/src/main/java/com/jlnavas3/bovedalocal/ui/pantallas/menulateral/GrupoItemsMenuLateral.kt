package com.jlnavas3.bovedalocal.ui.pantallas.menulateral

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.NodoAjuste
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

/**
 * Contenedor interactivo reordenable para los accesos directos activos de la barra lateral.
 */
@Composable
fun GrupoItemsMenuLateral(
    itemsNodos: List<NodoAjuste>,
    mostrarIds: Boolean,
    alReordenar: (List<String>) -> Unit,
    alEliminar: (String) -> Unit,
    alAbrirSelector: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    var draggingId by remember { mutableStateOf<String?>(null) }
    var startDragIndex by remember { mutableIntStateOf(-1) }
    var targetVisualIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    val itemHeightPx = with(LocalDensity.current) { 54.dp.toPx() }
    val itemsActuales by rememberUpdatedState(itemsNodos)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ACCESOS DIRECTOS ACTIVOS",
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    fontSize = 11.sp
                )
            )

            Text(
                text = "${itemsNodos.size} elementos",
                color = ColorAjusteGris,
                fontSize = 11.sp
            )
        }

        Spacer(Modifier.height(4.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsNodos.forEachIndexed { itemIndex, nodo ->
                val isDragging = nodo.id == draggingId

                val targetShiftY = when {
                    isDragging -> 0f
                    startDragIndex == -1 || targetVisualIndex == -1 -> 0f
                    startDragIndex < targetVisualIndex && itemIndex in (startDragIndex + 1)..targetVisualIndex -> -itemHeightPx
                    startDragIndex > targetVisualIndex && itemIndex in targetVisualIndex until startDragIndex -> itemHeightPx
                    else -> 0f
                }

                val animatedShiftY by animateFloatAsState(
                    targetValue = targetShiftY,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    ),
                    label = "reactiveSlotSidebar"
                )

                val modifierAsa = Modifier.pointerInput(nodo.id) {
                    detectDragGestures(
                        onDragStart = {
                            draggingId = nodo.id
                            val sIdx = itemsActuales.indexOfFirst { it.id == nodo.id }
                            startDragIndex = sIdx
                            targetVisualIndex = sIdx
                            dragOffsetY = 0f
                            haptica.tic()
                        },
                        onDragEnd = {
                            val startIdx = itemsActuales.indexOfFirst { it.id == nodo.id }
                            if (startIdx != -1) {
                                val delta = (dragOffsetY / itemHeightPx).roundToInt()
                                val targetIdx = (startIdx + delta).coerceIn(0, itemsActuales.size - 1)
                                if (targetIdx != startIdx) {
                                    val mutable = itemsActuales.map { it.id }.toMutableList()
                                    val item = mutable.removeAt(startIdx)
                                    mutable.add(targetIdx, item)
                                    alReordenar(mutable)
                                    haptica.exito()
                                }
                            }
                            draggingId = null
                            startDragIndex = -1
                            targetVisualIndex = -1
                            dragOffsetY = 0f
                        },
                        onDragCancel = {
                            draggingId = null
                            startDragIndex = -1
                            targetVisualIndex = -1
                            dragOffsetY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragOffsetY += dragAmount.y
                            val delta = (dragOffsetY / itemHeightPx).roundToInt()
                            val newTarget = (startDragIndex + delta).coerceIn(0, itemsActuales.size - 1)
                            if (newTarget != targetVisualIndex) {
                                targetVisualIndex = newTarget
                                haptica.tic()
                            }
                        }
                    )
                }

                FilaItemMenuLateralPersonalizable(
                    nodo = nodo,
                    mostrarIds = mostrarIds,
                    estaArrastrando = isDragging,
                    offsetY = if (isDragging) dragOffsetY else animatedShiftY,
                    alEliminar = { alEliminar(nodo.id) },
                    modifierAsa = modifierAsa,
                    modifier = Modifier.zIndex(if (isDragging) 999f else 1f)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Botón añadir acceso directo
        OutlinedButton(
            onClick = alAbrirSelector,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorAcento)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Añadir acceso directo", fontWeight = FontWeight.Medium, fontSize = 13.sp)
        }
    }
}
