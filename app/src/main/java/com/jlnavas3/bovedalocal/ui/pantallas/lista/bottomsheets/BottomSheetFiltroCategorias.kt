package com.jlnavas3.bovedalocal.ui.pantallas.lista.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import com.jlnavas3.bovedalocal.ui.componentes.CerrarTecladoAlHacerScroll
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.IconosCategorias
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Panel inferior deslizante para seleccionar y filtrar por Categoría con buscador rápido y creación directa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetFiltroCategorias(
    visible: Boolean,
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    conteoPorCategoria: Map<String, Int>,
    totalEntradas: Int,
    alSeleccionarCategoria: (String?) -> Unit,
    alCrearCategoria: () -> Unit,
    alGestionarCategorias: () -> Unit,
    alCerrar: () -> Unit
) {
    if (!visible) return

    var busqueda by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val categoriasFiltradas = remember(categorias, busqueda) {
        if (busqueda.isBlank()) categorias
        else categorias.filter { it.nombre.contains(busqueda, ignoreCase = true) }
    }

    val lazyListState = rememberLazyListState()
    CerrarTecladoAlHacerScroll(lazyListState.isScrollInProgress)

    ModalBottomSheet(
        onDismissRequest = alCerrar,
        sheetState = sheetState,
        containerColor = ColorAjustesFondo,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .cerrarTecladoAlTocarFuera()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            // Cabecera
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Folder,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Filtrar por Categoría",
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            alCerrar()
                            alCrearCategoria()
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Crear categoría",
                            tint = ColorAcento,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    IconButton(onClick = alCerrar, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cerrar",
                            tint = TextoSecundario,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Buscador
            CampoBusquedaBottomSheet(
                valor = busqueda,
                alCambiar = { busqueda = it },
                pista = "Buscar entre ${categorias.size} categorías..."
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Opción: Todas las categorías
                item {
                    val seleccionado = categoriaSeleccionadaId == null
                    FilaOpcionCategoria(
                        titulo = "Todas las categorías",
                        subtitulo = "$totalEntradas entradas en total",
                        icono = Icons.Filled.Layers,
                        colorIcono = ColorAcento,
                        seleccionado = seleccionado,
                        alPulsar = {
                            alSeleccionarCategoria(null)
                            alCerrar()
                        }
                    )
                }

                // Lista de categorías
                items(categoriasFiltradas, key = { it.id }) { cat ->
                    val colorBase = IconosCategorias.parsearColorHex(cat.colorHex) ?: ColorAcento
                    val icono = IconosCategorias.obtenerIcono(cat.icono)
                    val conteo = conteoPorCategoria[cat.id] ?: 0
                    val seleccionado = categoriaSeleccionadaId == cat.id

                    FilaOpcionCategoria(
                        titulo = cat.nombre,
                        subtitulo = "$conteo entradas",
                        conteo = conteo,
                        icono = icono,
                        colorIcono = colorBase,
                        seleccionado = seleccionado,
                        alPulsar = {
                            alSeleccionarCategoria(cat.id)
                            alCerrar()
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botón Administrar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        alCerrar()
                        alGestionarCategorias()
                    }
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Administrar categorías...",
                    color = ColorAcento,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
private fun FilaOpcionCategoria(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    colorIcono: Color,
    seleccionado: Boolean,
    conteo: Int? = null,
    alPulsar: () -> Unit
) {
    val forma = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(if (seleccionado) colorIcono.copy(alpha = 0.14f) else ColorTarjetaAjustes)
            .border(
                width = if (seleccionado) 1.dp else 0.5.dp,
                color = if (seleccionado) colorIcono.copy(alpha = 0.8f) else ColorSeparadorAjustes.copy(alpha = 0.4f),
                shape = forma
            )
            .clickable { alPulsar() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(colorIcono.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.SemiBold
                )
            )
            Text(
                text = subtitulo,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
            )
        }

        if (conteo != null) {
            Text(
                text = "$conteo",
                color = if (seleccionado) colorIcono else TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }

        if (seleccionado) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Seleccionado",
                tint = colorIcono,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
