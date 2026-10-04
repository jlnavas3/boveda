package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IconoTipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Barra horizontal deslizable de chips de categorías con el modelo visual y dimensiones
 * de las colecciones (RoundedCornerShape(12.dp), icono representativo de 15.dp, colores propios y bordes configurables).
 */
@Composable
fun ChipsCategoriasExportacion(
    categorias: List<CategoriaExportacion>,
    categoriaActivaId: String,
    alSeleccionarCategoria: (String) -> Unit,
    totalPorCategoria: (CategoriaExportacion) -> Int,
    onTic: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (categorias.size <= 1) return

    val formaChip = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categorias.forEach { cat ->
            val seleccionada = cat.id == categoriaActivaId
            val cantCat = totalPorCategoria(cat)
            val colorPropio = cat.color
            val icono = cat.icono

            Box(
                modifier = Modifier
                    .clip(formaChip)
                    .background(if (seleccionada) colorPropio else ColorTarjetaAjustes)
                    .then(
                        if (seleccionada) {
                            Modifier.border(0.8.dp, colorPropio, formaChip)
                        } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                            Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                        } else {
                            Modifier.border(0.8.dp, ColorSeparadorAjustes, formaChip)
                        }
                    )
                    .clickable {
                        onTic()
                        alSeleccionarCategoria(cat.id)
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = if (seleccionada) {
                            if (colorPropio == ColorAcento) ColorSobreAcento else Color.White
                        } else colorPropio,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${cat.etiqueta} ($cantCat)",
                        color = if (seleccionada) {
                            if (colorPropio == ColorAcento) ColorSobreAcento else Color.White
                        } else TextoPrincipal,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

/**
 * Indicador de cantidad de elementos seleccionados.
 */
@Composable
fun BarraControlesSeleccionExportar(
    seleccionadas: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$seleccionadas de $total seleccionadas",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            ),
            color = ColorAcento
        )
    }
}

/**
 * Fila visual de cada credencial con las mismas dimensiones, avatar e indicador de selección circular
 * que en la pantalla principal.
 */
@Composable
fun FilaEntradaExportarSelectivo(
    entrada: Entrada,
    marcada: Boolean,
    alAlternarMarcado: (Boolean) -> Unit,
    alturaFila: Dp = 74.dp,
    tamanoMonograma: Int = 46,
    modifier: Modifier = Modifier
) {
    val compacta = alturaFila.value <= 48f
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val tamanoIcono = if (compacta) 32 else if (alturaFila.value <= 64f) 36 else 40

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(alturaFila)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else Modifier
            )
            .clickable { alAlternarMarcado(!marcada) }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador circular de selección estilo PantallaLista
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (marcada) ColorAcento else Borde),
                contentAlignment = Alignment.Center
            ) {
                if (marcada) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = ColorSobreAcento,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            // Icono representativo / Avatar
            IconoTipoEntrada(
                entrada = entrada,
                tamanoIcono = tamanoIcono
            )

            Spacer(Modifier.width(12.dp))

            // Información de la entrada
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = entrada.titulo.ifBlank { "Sin título" },
                    style = if (compacta) {
                        MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    } else {
                        MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    },
                    color = TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entrada.usuario.isNotBlank()) {
                    Text(
                        text = entrada.usuario,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
