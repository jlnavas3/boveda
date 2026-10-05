package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeccionCategoriasEdicion(
    categoriasDisponibles: List<Categoria>,
    categoriasSeleccionadas: List<String>,
    alCambiarCategorias: (List<String>) -> Unit,
    alCrearNuevaCategoria: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TextoTitulo(texto = "Categorías")
        TextoSubtitulo(texto = "Organiza esta entrada en categorías personalizadas")

        Spacer(Modifier.height(8.dp))

        val formaChip = RoundedCornerShape(10.dp)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoriasDisponibles.forEach { cat ->
                val estaSeleccionada = categoriasSeleccionadas.contains(cat.id)
                val colorPropio = IconosCategorias.parsearColorHex(cat.colorHex) ?: ColorAcento
                val icono = IconosCategorias.obtenerIcono(cat.icono)

                Box(
                    modifier = Modifier
                        .clip(formaChip)
                        .background(if (estaSeleccionada) colorPropio else ColorCampoAjustes)
                        .border(
                            width = if (estaSeleccionada) 1.dp else 0.8.dp,
                            color = if (estaSeleccionada) colorPropio else ColorSeparadorAjustes,
                            shape = formaChip
                        )
                        .clickable {
                            val nuevaLista = if (estaSeleccionada) {
                                categoriasSeleccionadas - cat.id
                            } else {
                                categoriasSeleccionadas + cat.id
                            }
                            alCambiarCategorias(nuevaLista)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (estaSeleccionada) Icons.Filled.Check else icono,
                            contentDescription = null,
                            tint = if (estaSeleccionada) Color.White else colorPropio,
                            modifier = Modifier.size(15.dp)
                        )
                        TextoCuerpo(
                            texto = cat.nombre,
                            color = if (estaSeleccionada) Color.White else TextoPrincipal
                        )
                    }
                }
            }

            // Chip para crear nueva categoría directamente
            Box(
                modifier = Modifier
                    .clip(formaChip)
                    .background(ColorTarjetaAjustes)
                    .border(0.8.dp, ColorSeparadorAjustes, formaChip)
                    .clickable { alCrearNuevaCategoria() }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Nueva",
                        tint = ColorAcento,
                        modifier = Modifier.size(15.dp)
                    )
                    TextoCuerpo(
                        texto = "Nueva",
                        color = ColorAcento
                    )
                }
            }
        }
    }
}
