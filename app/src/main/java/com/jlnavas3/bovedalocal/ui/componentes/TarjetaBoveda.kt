package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorEncabezadoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

@Composable
fun TarjetaBoveda(
    modifier: Modifier = Modifier,
    alPulsar: (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = FormaTarjeta
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetas)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
            .then(if (alPulsar != null) Modifier.clickable { alPulsar() } else Modifier)
            .padding(16.dp)
    ) {
        Column(content = contenido)
    }
}

/**
 * Tarjeta desplegable con encabezado distinguido (tono más oscuro, icono grande a la izquierda,
 * textos verticalmente centrados, borde separador inferior y chevron a la derecha).
 * Completamente plana: sin sombras ni blur, gobernada por el borde y curvatura configurados.
 */
@Composable
fun TarjetaBovedaDesplegable(
    titulo: String,
    icono: ImageVector,
    descripcion: String = "",
    inicialmenteAbierta: Boolean = false,
    modifier: Modifier = Modifier,
    colorIcono: Color = ColorIconosInternos,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    abiertaControlada: Boolean? = null,
    alAlternarAbierta: ((Boolean) -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    var abiertaLocal by remember(inicialmenteAbierta) { mutableStateOf(inicialmenteAbierta) }
    val abierta = abiertaControlada ?: abiertaLocal
    val forma = FormaTarjeta

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(if (abierta) ColorTarjetas else ColorEncabezadoTarjeta)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
    ) {
        // Encabezado holgado con tono sutilmente más oscuro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorEncabezadoTarjeta)
                .clickable {
                    if (alAlternarAbierta != null) {
                        alAlternarAbierta(!abierta)
                    } else {
                        abiertaLocal = !abiertaLocal
                    }
                }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono distinguido con contenedor suave estilo barra lateral y etiqueta ID opcional
            val colorLegible = colorLegibleParaTema(colorIcono)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(FormaPequena)
                        .background(fondoBadgeParaTema(colorIcono)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorLegible,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                    Spacer(Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(fondoBadgeParaTema(colorIcono))
                            .padding(horizontal = 4.dp, vertical = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = idEtiqueta,
                            style = EstiloMono.copy(fontSize = 8.sp, fontWeight = FontWeight.Bold),
                            color = colorLegibleParaTema(colorIcono)
                        )
                    }
                }
            }

            Spacer(Modifier.width(14.dp))

            // Textos centrados verticalmente
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = titulo,
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                if (descripcion.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = descripcion,
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            // Indicador de desplegado a la derecha
            Icon(
                imageVector = if (abierta) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (abierta) "Contraer" else "Expandir",
                tint = TextoSecundario,
                modifier = Modifier.size(24.dp)
            )
        }

        // Borde separador entre encabezado y cuerpo cuando está desplegada
        if (abierta) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (GrosorBorde > 0.dp) GrosorBorde else 1.dp)
                    .background(if (ColorBordeActual != Color.Transparent) ColorBordeActual else Borde)
            )

            // Cuerpo de la tarjeta con padding holgado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorTarjetas)
                    .padding(16.dp),
                content = contenido
            )
        }
    }
}
