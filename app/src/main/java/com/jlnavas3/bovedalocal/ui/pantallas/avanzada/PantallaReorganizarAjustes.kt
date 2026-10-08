package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.InsigniaIdAjuste
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.NodoAjuste
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

/**
 * Representación plana de un elemento dentro del árbol jerárquico multinivel para renderizado en lista.
 */
data class ElementoArbolVisible(
    val nodo: NodoAjuste,
    val profundidad: Int,
    val padreId: String?,
    val tieneHijos: Boolean,
    val totalHijos: Int,
    val expandido: Boolean,
    val hermanos: List<String>,
    val indiceEnHermanos: Int
)

/**
 * Pantalla interactiva en árbol para reorganizar todas las opciones y subniveles de Ajustes.
 * Permite desplegar subitems mediante acordeón, reordenar entre hermanos con el asa ≡
 * y realizar Reparenting / Sangría desde el menú de 3 puntos ⋮.
 */
@Composable
fun PantallaReorganizarAjustes(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var nodosExpandidos by remember { mutableStateOf(setOf<String>()) }
    var nodoSeleccionadoParaMover by remember { mutableStateOf<NodoAjuste?>(null) }
    var menuAbiertoParaId by remember { mutableStateOf<String?>(null) }

    var draggingNodeId by remember { mutableStateOf<String?>(null) }
    var startDragIndex by remember { mutableIntStateOf(-1) }
    var targetVisualIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var dragOffsetX by remember { mutableFloatStateOf(0f) }

    // Función recursiva para aplanar el árbol jerárquico según el estado de expansión y reparenting
    fun construirArbolVisible(): List<ElementoArbolVisible> {
        val resultado = mutableListOf<ElementoArbolVisible>()

        fun agregarNodos(padreId: String?, profundidad: Int) {
            val hijos = if (padreId == null) {
                MapaAjustes.obtenerNodosRaiz(
                    ordenPersonalizado = ajustes.ordenAjustesPersonalizado,
                    reparenting = ajustes.reparentingPersonalizado
                )
            } else {
                MapaAjustes.obtenerHijosDe(
                    padreId = padreId,
                    ordenPersonalizado = ajustes.ordenJerarquiaPersonalizado,
                    reparenting = ajustes.reparentingPersonalizado
                )
            }

            val hermanosIds = hijos.map { it.id }

            hijos.forEachIndexed { index, nodo ->
                val subHijos = MapaAjustes.obtenerHijosDe(
                    padreId = nodo.id,
                    ordenPersonalizado = ajustes.ordenJerarquiaPersonalizado,
                    reparenting = ajustes.reparentingPersonalizado
                )
                val tieneHijos = subHijos.isNotEmpty()
                val estaExpandido = nodosExpandidos.contains(nodo.id)

                resultado.add(
                    ElementoArbolVisible(
                        nodo = nodo,
                        profundidad = profundidad,
                        padreId = padreId,
                        tieneHijos = tieneHijos,
                        totalHijos = subHijos.size,
                        expandido = estaExpandido,
                        hermanos = hermanosIds,
                        indiceEnHermanos = index
                    )
                )

                if (tieneHijos && estaExpandido) {
                    agregarNodos(nodo.id, profundidad + 1)
                }
            }
        }

        agregarNodos(null, 0)
        return resultado
    }

    val arbolVisible = remember(ajustes.ordenAjustesPersonalizado, ajustes.ordenJerarquiaPersonalizado, ajustes.reparentingPersonalizado, nodosExpandidos) {
        construirArbolVisible()
    }

    fun moverNodoDestino(
        nodoId: String,
        nuevoPadreId: String?,
        indiceDestinoEnHermanos: Int? = null
    ) {
        val reparenting = ajustes.reparentingPersonalizado.toMutableMap()
        val jerarquia = ajustes.ordenJerarquiaPersonalizado.toMutableMap()
        var ordenRaiz = ajustes.ordenAjustesPersonalizado.toMutableList()

        val nodoDef = MapaAjustes.buscarPorId(nodoId)
        val padreFabrica = nodoDef?.padreId // null si de fábrica era raíz
        val esDestinoRaiz = (nuevoPadreId == null || nuevoPadreId == "00-AJU")

        // 1. Quitar de listas previas de orden personalizado
        ordenRaiz.remove(nodoId)
        jerarquia.forEach { (pid, lista) ->
            if (lista.contains(nodoId)) {
                jerarquia[pid] = lista.filter { it != nodoId }
            }
        }

        // 2. Aplicar reparenting y orden en el nuevo contenedor
        if (esDestinoRaiz) {
            if (padreFabrica == null) {
                reparenting.remove(nodoId)
            } else {
                reparenting[nodoId] = "00-AJU"
            }
            val raicesActuales = MapaAjustes.obtenerNodosRaiz(ordenRaiz, reparenting)
                .map { it.id }.filter { it != nodoId }.toMutableList()
            val targetIdx = (indiceDestinoEnHermanos ?: raicesActuales.size).coerceIn(0, raicesActuales.size)
            raicesActuales.add(targetIdx, nodoId)
            ordenRaiz = raicesActuales
        } else {
            val destinoId = nuevoPadreId!!
            if (padreFabrica == destinoId) {
                reparenting.remove(nodoId)
            } else {
                reparenting[nodoId] = destinoId
            }
            val hijosActuales = MapaAjustes.obtenerHijosDe(destinoId, jerarquia, reparenting)
                .map { it.id }.filter { it != nodoId }.toMutableList()
            val targetIdx = (indiceDestinoEnHermanos ?: hijosActuales.size).coerceIn(0, hijosActuales.size)
            hijosActuales.add(targetIdx, nodoId)
            jerarquia[destinoId] = hijosActuales

            // Asegurar que el nuevo contenedor esté desplegado
            nodosExpandidos = nodosExpandidos + destinoId
        }

        vm.ajustarReparentingPersonalizado(reparenting)
        vm.ajustarOrdenAjustesPersonalizado(ordenRaiz)
        vm.ajustarOrdenJerarquiaPersonalizado(jerarquia)
    }

    fun reparentarNodo(nodo: NodoAjuste, nuevoPadre: NodoAjuste?) {
        haptica.exito()
        moverNodoDestino(nodo.id, nuevoPadre?.id)
        vm.avisar("Movido a ${nuevoPadre?.titulo ?: "Ajustes (Raíz)"}")
    }

    fun indentarNodo(elem: ElementoArbolVisible) {
        if (elem.indiceEnHermanos > 0) {
            haptica.tic()
            val hermanoAnteriorId = elem.hermanos[elem.indiceEnHermanos - 1]
            moverNodoDestino(elem.nodo.id, hermanoAnteriorId)
            val tituloHermano = MapaAjustes.buscarPorId(hermanoAnteriorId)?.titulo ?: "subnivel"
            vm.avisar("Convertido en subnivel de $tituloHermano")
        }
    }

    fun outdentarNodo(elem: ElementoArbolVisible) {
        if (elem.padreId != null) {
            haptica.tic()
            val abueloId = MapaAjustes.buscarPorId(elem.padreId)?.padreId
            moverNodoDestino(elem.nodo.id, abueloId)
            vm.avisar("Subido de nivel")
        }
    }

    fun restablecerNodo(nodo: NodoAjuste, padreId: String?) {
        haptica.tic()
        val reparenting = ajustes.reparentingPersonalizado.toMutableMap()
        reparenting.remove(nodo.id)
        vm.ajustarReparentingPersonalizado(reparenting)

        if (padreId != null) {
            val orden = ajustes.ordenJerarquiaPersonalizado.toMutableMap()
            orden.remove(padreId)
            vm.ajustarOrdenJerarquiaPersonalizado(orden)
        } else {
            val orden = ajustes.ordenAjustesPersonalizado.filter { it != nodo.id }
            vm.ajustarOrdenAjustesPersonalizado(orden)
        }
        vm.avisar("Restablecido a posición original")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Reorganizar ajustes",
            idEtiqueta = "06-SIS-AVZ-ORG",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                DescripcionPantalla(
                    subtitulo = "Despliega cualquier sección para ver sus subajustes. Arrastra ≡ para mover libremente entre niveles o usa ⋮ para opciones avanzadas."
                )
                Spacer(Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            haptica.tic()
                            vm.restablecerTodoArbolAjustes()
                            nodosExpandidos = emptySet()
                            vm.avisar("Árbol de ajustes restablecido al orden de fábrica")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Restablecer todo",
                            modifier = Modifier.size(15.dp),
                            tint = ColorAcento
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Restablecer todo",
                            color = ColorAcento,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    TextButton(
                        onClick = {
                            val todosConHijos = MapaAjustes.TODOS_LOS_NODOS.filter { nodo ->
                                MapaAjustes.obtenerHijosDe(nodo.id).isNotEmpty()
                            }.map { it.id }.toSet()
                            nodosExpandidos = if (nodosExpandidos.isEmpty()) todosConHijos else emptySet()
                        }
                    ) {
                        Icon(
                            imageVector = if (nodosExpandidos.isEmpty()) Icons.Default.UnfoldMore else Icons.Default.UnfoldLess,
                            contentDescription = "Expandir/Colapsar",
                            modifier = Modifier.size(15.dp),
                            tint = ColorAcento
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (nodosExpandidos.isEmpty()) "Expandir todo" else "Colapsar todo",
                            color = ColorAcento,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            itemsIndexed(
                items = arbolVisible,
                key = { _, item -> item.nodo.id }
            ) { itemIndex, item ->
                val isDragging = item.nodo.id == draggingNodeId
                val elevation = if (isDragging) 16.dp else 0.dp
                val sangriaDp = (item.profundidad * 12).dp

                val itemActual by rememberUpdatedState(item)
                val arbolActual by rememberUpdatedState(arbolVisible)

                val filaHeightPx = with(LocalDensity.current) { 34.dp.toPx() }
                val targetShiftY = when {
                    isDragging -> 0f
                    startDragIndex == -1 || targetVisualIndex == -1 -> 0f
                    startDragIndex < targetVisualIndex && itemIndex in (startDragIndex + 1)..targetVisualIndex -> -filaHeightPx
                    startDragIndex > targetVisualIndex && itemIndex in targetVisualIndex until startDragIndex -> filaHeightPx
                    else -> 0f
                }

                val animatedShiftY by animateFloatAsState(
                    targetValue = targetShiftY,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    ),
                    label = "reactiveSlot"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(if (isDragging) 999f else 1f)
                        .padding(start = sangriaDp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset {
                                if (isDragging) {
                                    IntOffset(dragOffsetX.roundToInt().coerceIn(-40, 60), dragOffsetY.roundToInt())
                                } else {
                                    IntOffset(0, animatedShiftY.roundToInt())
                                }
                            }
                            .shadow(elevation, RoundedCornerShape(8.dp))
                            .then(
                                if (isDragging) Modifier.border(BorderStroke(1.5.dp, ColorAcento.copy(alpha = 0.6f)), RoundedCornerShape(8.dp))
                                else Modifier
                            ),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorTarjetaAjustes)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Asa de arrastre táctil libre entre niveles
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .pointerInput(item.nodo.id) {
                                        detectDragGestures(
                                            onDragStart = {
                                                draggingNodeId = itemActual.nodo.id
                                                val sIdx = arbolActual.indexOfFirst { it.nodo.id == itemActual.nodo.id }
                                                startDragIndex = sIdx
                                                targetVisualIndex = sIdx
                                                dragOffsetY = 0f
                                                dragOffsetX = 0f
                                                haptica.tic()
                                            },
                                            onDragEnd = {
                                                val startIdx = arbolActual.indexOfFirst { it.nodo.id == itemActual.nodo.id }
                                                if (startIdx != -1) {
                                                    val deltaFilas = (dragOffsetY / filaHeightPx).roundToInt()
                                                    val targetIdx = (startIdx + deltaFilas).coerceIn(0, arbolActual.size - 1)

                                                    if (deltaFilas == 0 && dragOffsetX > 28.dp.toPx()) {
                                                        indentarNodo(itemActual)
                                                    } else if (deltaFilas == 0 && dragOffsetX < -28.dp.toPx()) {
                                                        outdentarNodo(itemActual)
                                                    } else if (targetIdx != startIdx) {
                                                        val destino = arbolActual[targetIdx]
                                                        haptica.exito()
                                                        if (destino.padreId == itemActual.padreId) {
                                                            moverNodoDestino(itemActual.nodo.id, itemActual.padreId, destino.indiceEnHermanos)
                                                            vm.avisar("Reordenado")
                                                        } else {
                                                            if (destino.tieneHijos && destino.expandido && targetIdx > startIdx) {
                                                                moverNodoDestino(itemActual.nodo.id, destino.nodo.id, 0)
                                                                vm.avisar("Movido dentro de ${destino.nodo.titulo}")
                                                            } else {
                                                                moverNodoDestino(itemActual.nodo.id, destino.padreId, destino.indiceEnHermanos)
                                                                val nombrePadre = if (destino.padreId != null) MapaAjustes.buscarPorId(destino.padreId)?.titulo ?: "Subnivel" else "Ajustes (Raíz)"
                                                                vm.avisar("Ubicado en $nombrePadre")
                                                            }
                                                        }
                                                    }
                                                }
                                                draggingNodeId = null
                                                startDragIndex = -1
                                                targetVisualIndex = -1
                                                dragOffsetY = 0f
                                                dragOffsetX = 0f
                                            },
                                            onDragCancel = {
                                                draggingNodeId = null
                                                startDragIndex = -1
                                                targetVisualIndex = -1
                                                dragOffsetY = 0f
                                                dragOffsetX = 0f
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                dragOffsetY += dragAmount.y
                                                dragOffsetX += dragAmount.x
                                                if (startDragIndex != -1) {
                                                    val deltaFilas = (dragOffsetY / filaHeightPx).roundToInt()
                                                    val nuevoTarget = (startDragIndex + deltaFilas).coerceIn(0, arbolActual.size - 1)
                                                    if (nuevoTarget != targetVisualIndex) {
                                                        targetVisualIndex = nuevoTarget
                                                        haptica.tic()
                                                    }
                                                }
                                            }
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DragHandle,
                                    contentDescription = "Arrastrar para reordenar",
                                    tint = if (isDragging) ColorAcento else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Chevron de expansión si tiene hijos
                            if (item.tieneHijos) {
                                IconButton(
                                    onClick = {
                                        nodosExpandidos = if (item.expandido) {
                                            nodosExpandidos - item.nodo.id
                                        } else {
                                            nodosExpandidos + item.nodo.id
                                        }
                                        haptica.tic()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = if (item.expandido) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = if (item.expandido) "Colapsar" else "Expandir",
                                        tint = ColorAcento,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else {
                                Spacer(Modifier.width(6.dp))
                            }

                            Spacer(Modifier.width(4.dp))

                            // Contenido textual: Título arriba, ID abajo si está activo
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (item.tieneHijos) {
                                            nodosExpandidos = if (item.expandido) nodosExpandidos - item.nodo.id else nodosExpandidos + item.nodo.id
                                            haptica.tic()
                                        }
                                    }
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.nodo.titulo,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 16.sp),
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    if (item.tieneHijos && !item.expandido) {
                                        Spacer(Modifier.width(5.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(ColorAcento.copy(alpha = 0.15f), RoundedCornerShape(3.dp))
                                                .padding(horizontal = 4.dp, vertical = 0.dp)
                                        ) {
                                            Text(
                                                text = "${item.totalHijos}",
                                                color = ColorAcento,
                                                fontSize = 9.sp,
                                                lineHeight = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                                if (ajustes.mostrarIdsAjustes) {
                                    Spacer(Modifier.height(1.dp))
                                    InsigniaIdAjuste(id = item.nodo.id, ajustes = ajustes)
                                }
                            }

                            // Menú contextual de 3 puntos ⋮
                            Box {
                                IconButton(
                                    onClick = { menuAbiertoParaId = item.nodo.id },
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Opciones",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuAbiertoParaId == item.nodo.id,
                                    onDismissRequest = { menuAbiertoParaId = null }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Mover a...") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.DriveFileMove,
                                                contentDescription = null,
                                                tint = ColorAcento
                                            )
                                        },
                                        onClick = {
                                            menuAbiertoParaId = null
                                            nodoSeleccionadoParaMover = item.nodo
                                        }
                                    )

                                    HorizontalDivider()

                                    DropdownMenuItem(
                                        text = { Text("Hacer subnivel (Sangría)") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                contentDescription = null
                                            )
                                        },
                                        enabled = item.indiceEnHermanos > 0,
                                        onClick = {
                                            menuAbiertoParaId = null
                                            indentarNodo(item)
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Subir de nivel (Desanidar)") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                                contentDescription = null
                                            )
                                        },
                                        enabled = item.padreId != null,
                                        onClick = {
                                            menuAbiertoParaId = null
                                            outdentarNodo(item)
                                        }
                                    )

                                    HorizontalDivider()

                                    DropdownMenuItem(
                                        text = { Text("Subir") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowUp,
                                                contentDescription = null
                                            )
                                        },
                                        enabled = item.indiceEnHermanos > 0,
                                        onClick = {
                                            menuAbiertoParaId = null
                                            moverNodoDestino(item.nodo.id, item.padreId, item.indiceEnHermanos - 1)
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Bajar") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = null
                                            )
                                        },
                                        enabled = item.indiceEnHermanos < item.hermanos.size - 1,
                                        onClick = {
                                            menuAbiertoParaId = null
                                            moverNodoDestino(item.nodo.id, item.padreId, item.indiceEnHermanos + 1)
                                        }
                                    )

                                    HorizontalDivider()

                                    DropdownMenuItem(
                                        text = { Text("Restablecer a fábrica") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = null
                                            )
                                        },
                                        onClick = {
                                            menuAbiertoParaId = null
                                            restablecerNodo(item.nodo, item.padreId)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(36.dp))
            }
        }
    }

    nodoSeleccionadoParaMover?.let { nodo ->
        val contenedores = remember(nodo) {
            MapaAjustes.obtenerNodosContenedores().filter { it.id != nodo.id }
        }
        DialogoMoverPantallaDestino(
            nodoOrigen = nodo,
            destinosDisponibles = contenedores,
            mostrarIds = ajustes.mostrarIdsAjustes,
            alSeleccionarDestino = { destino ->
                reparentarNodo(nodo, destino)
                nodoSeleccionadoParaMover = null
            },
            alDescartar = { nodoSeleccionadoParaMover = null }
        )
    }
}

@Preview(name = "Pantalla Reorganizar Ajustes Árbol", showBackground = true)
@Composable
private fun PantallaReorganizarAjustesPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
                .padding(16.dp)
        ) {
            DescripcionPantalla(subtitulo = "Reorganización jerárquica fractal en árbol interactivo")
        }
    }
}
