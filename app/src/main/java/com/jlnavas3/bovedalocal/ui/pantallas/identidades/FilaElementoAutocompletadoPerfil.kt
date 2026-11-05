/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Fila interactiva desacoplada para representar un elemento vinculado (documento, tarjeta, cuenta o contacto)
 * dentro de la tarjeta unificada de autocompletado del perfil.
 */
@Composable
fun FilaElementoAutocompletadoPerfil(
    icono: ImageVector,
    tituloCategoria: String,
    tituloElemento: String?,
    detalleElemento: String?,
    cantidadTotal: Int = 0,
    colorAcento: Color,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tieneElemento = !tituloElemento.isNullOrBlank()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CurvaturaEsquinas))
            .clickable(onClick = alPulsar)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContenedorIconoInsignia(
            icono = icono,
            tamano = TamanoInsignia.PEQUENO,
            colorFondo = ColorCampoAjustes,
            colorIcono = if (tieneElemento) colorAcento else TextoSecundario
        )

        Spacer(Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tituloCategoria,
                fontSize = 11.sp,
                color = TextoSecundario,
                fontWeight = FontWeight.Normal
            )
            if (tieneElemento) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = tituloElemento,
                        fontSize = 13.5.sp,
                        color = TextoPrincipal,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (cantidadTotal > 1) {
                        Text(
                            text = "(+${cantidadTotal - 1})",
                            fontSize = 11.sp,
                            color = colorAcento,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                if (!detalleElemento.isNullOrBlank()) {
                    Text(
                        text = detalleElemento,
                        fontSize = 11.5.sp,
                        color = TextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Text(
                    text = "Sin vincular",
                    fontSize = 12.5.sp,
                    color = TextoSecundario.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        BotonTextoBoveda(
            texto = if (tieneElemento) "Cambiar" else "+ Asociar",
            tipo = if (tieneElemento) TipoBotonTexto.SECUNDARIO else TipoBotonTexto.PRIMARIO,
            colorPersonalizado = colorAcento,
            alPulsar = alPulsar
        )
    }
}
