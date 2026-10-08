package com.jlnavas3.bovedalocal.ui.pantallas.menulateral

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.NodoAjuste
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono

/**
 * Fila reordenable e interactiva para un elemento de la barra lateral en la pantalla de personalización.
 */
@Composable
fun FilaItemMenuLateralPersonalizable(
    nodo: NodoAjuste,
    mostrarIds: Boolean,
    estaArrastrando: Boolean,
    offsetY: Float,
    alEliminar: () -> Unit,
    modifierAsa: Modifier = Modifier,
    modifier: Modifier = Modifier
) {
    val (icono, color) = MapaAjustes.resolverIconoYColor(nodo)
    val forma = RoundedCornerShape(12.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = offsetY
            }
            .then(
                if (estaArrastrando) {
                    Modifier
                        .shadow(12.dp, forma)
                        .border(1.5.dp, ColorAcento, forma)
                } else Modifier
            ),
        shape = forma,
        color = ColorTarjetaAjustes
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Asa táctil para arrastrar
            Box(
                modifier = modifierAsa
                    .size(36.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    contentDescription = "Reordenar",
                    tint = if (estaArrastrando) ColorAcento else ColorAjusteGris.copy(alpha = 0.8f),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(8.dp))

            // Squircle con icono
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            // Título, subtítulo e ID
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nodo.titulo,
                    color = ColorTextoAjustes,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (nodo.subtitulo.isNotBlank()) {
                    Text(
                        text = nodo.subtitulo,
                        color = ColorAjusteGris,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (mostrarIds) {
                    Text(
                        text = nodo.id,
                        color = ColorAcento,
                        style = EstiloMono.copy(fontSize = 10.sp)
                    )
                }
            }

            // Botón eliminar / quitar de barra lateral
            IconButton(
                onClick = alEliminar,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Quitar",
                    tint = ColorAjusteGris.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
