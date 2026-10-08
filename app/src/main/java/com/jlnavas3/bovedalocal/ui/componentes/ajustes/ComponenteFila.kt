@file:OptIn(ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.colorParaGrupoId
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Componente base de fila para Ajustes, diseñado siguiendo la estética limpia de
 * Honor MagicOS y Samsung One UI.
 *
 * Características:
 * - Icono en contenedor redondeado tipo squircle con fondo de color distintivo.
 * - Título y subtítulo descriptivo opcional.
 * - Badge de ID de fila discreto en formato monoespaciado (solo si mostrarId es true).
 * - Soporte nativo para auto-scroll y animación de destello de alumbrado ("glow")
 *   cuando es el destino objetivo.
 */
@Composable
fun ComponenteFila(
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    valorTexto: String? = null,
    subvalorTexto: String? = null,
    maxSubtituloLines: Int = 3,
    habilitado: Boolean = true,
    alPulsar: (() -> Unit)? = null,
    ejecutarHapticaAlPulsar: Boolean = true,
    estadoAlumbrado: EstadoAlumbradoFila? = null,
    contenidoFinal: (@Composable () -> Unit)? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estadoAlumbradoEfectivo = estadoAlumbrado ?: recordarEstadoAlumbrado(idFila)
    val coordinador = LocalCoordinadorResaltado.current
    val colorAcento = ColorAcento
    val tieneBadgeId = mostrarId && !idFila.isNullOrBlank()

    val modifierClick = if (alPulsar != null && habilitado) {
        Modifier.clickable {
            if (ejecutarHapticaAlPulsar) {
                haptica.tic()
            }
            alPulsar()
        }
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .bringIntoViewRequester(estadoAlumbradoEfectivo.bringIntoViewRequester)
            .background(estadoAlumbradoEfectivo.colorFondoAnimado.value)
            .onGloballyPositioned { coords ->
                coordinador?.registrarYEjecutarSiCoincide(
                    id = idFila,
                    itemCoordinates = coords,
                    estadoAlumbrado = estadoAlumbradoEfectivo,
                    colorAcento = colorAcento
                )
            }
            .then(modifierClick)
            .padding(
                horizontal = 16.dp,
                vertical = if (tieneBadgeId || !subtitulo.isNullOrBlank()) 8.dp else 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono en contenedor squircle redondeado (estilo MagicOS / One UI nativo)
        if (icono != null) {
            val fondoIcono = colorIcono ?: ColorIconosInternos
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(fondoIcono),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorTinteIcono,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(10.dp))
        }

        // Título + Subtítulo + ID de fila
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titulo,
                color = if (habilitado) ColorTextoAjustes else ColorAjusteGris,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.5.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (!subtitulo.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    maxLines = maxSubtituloLines,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (tieneBadgeId) {
                Spacer(Modifier.height(3.dp))
                InsigniaIdAjuste(id = idFila!!)
            }
        }

        // Bloque derecho: Valor textual (opcionalmente doble línea) + Slot final
        Row(
            modifier = Modifier.widthIn(max = 160.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if (!valorTexto.isNullOrBlank()) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = valorTexto,
                        color = ColorAjusteGris,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!subvalorTexto.isNullOrBlank()) {
                        Text(
                            text = subvalorTexto,
                            color = ColorAjusteGris.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            if (contenidoFinal != null) {
                if (!valorTexto.isNullOrBlank()) {
                    Spacer(Modifier.width(6.dp))
                }
                contenidoFinal()
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun ComponenteFilaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ComponenteFila(
                titulo = "Bloqueo Automático",
                subtitulo = "Bloquear la bóveda al salir de la aplicación",
                idFila = "01-SEG-BLQ",
                mostrarId = true,
                valorTexto = "Inmediato"
            )
            ComponenteFila(
                titulo = "Tema de la Aplicación",
                subtitulo = "Seguir tema del sistema",
                idFila = "02-APA-TEM",
                mostrarId = false,
                valorTexto = "Sistema"
            )
        }
    }
}

