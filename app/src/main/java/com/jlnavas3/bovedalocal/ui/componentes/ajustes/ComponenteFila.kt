@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
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
 * - Título limpio y directo sin subtítulos redundantes.
 * - Badge de ID de fila discreto en formato monoespaciado (solo si mostrarId es true).
 * - Soporte nativo para auto-scroll y animación de destello de alumbrado ("glow")
 *   cuando es el destino objetivo.
 */
@Composable
fun ComponenteFila(
    titulo: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    valorTexto: String? = null,
    habilitado: Boolean = true,
    alPulsar: (() -> Unit)? = null,
    contenidoFinal: (@Composable () -> Unit)? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estadoAlumbrado = recordarEstadoAlumbrado(idFila)
    val tieneBadgeId = mostrarId && !idFila.isNullOrBlank()

    val modifierClick = if (alPulsar != null && habilitado) {
        Modifier.clickable {
            haptica.tic()
            alPulsar()
        }
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .bringIntoViewRequester(estadoAlumbrado.bringIntoViewRequester)
            .background(estadoAlumbrado.colorFondoAnimado.value)
            .then(modifierClick)
            .padding(
                horizontal = 16.dp,
                vertical = if (tieneBadgeId) 10.dp else 13.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono en contenedor squircle redondeado (estilo MagicOS / One UI nativo)
        if (icono != null) {
            val fondoIcono = colorIcono ?: ColorAcento
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(fondoIcono),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorTinteIcono,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
        }

        // Título + ID de fila (ocupa el espacio disponible empujando el valor al extremo derecho)
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (tieneBadgeId) {
                Spacer(Modifier.height(3.dp))
                val badgeColor = if (habilitado) (colorIcono ?: LocalColorGrupo.current ?: colorParaGrupoId(idFila)) else ColorAjusteGris
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(fondoBadgeParaTema(badgeColor))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = idFila!!,
                        color = colorLegibleParaTema(badgeColor),
                        style = EstiloMono.copy(
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Bloque derecho: Valor textual + Slot final (ambos pegados al extremo derecho)
        Row(
            modifier = Modifier.widthIn(max = 160.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if (!valorTexto.isNullOrBlank()) {
                Text(
                    text = valorTexto,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
