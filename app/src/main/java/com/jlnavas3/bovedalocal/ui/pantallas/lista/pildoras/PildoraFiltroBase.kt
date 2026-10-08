package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Píldora de filtrado base con factor de forma ultra-compacto (~32 dp de altura).
 *
 * Soporta icono primario, etiqueta truncada en una línea, y acción final diferenciada
 * (flecha desplegable cuando está inactiva o botón '✕' táctil para desfiltrar cuando está activa).
 */
@Composable
fun PildoraFiltroBase(
    icono: ImageVector,
    texto: String?,
    activo: Boolean,
    modifier: Modifier = Modifier,
    colorAcento: Color = ColorAcento,
    alPulsar: () -> Unit,
    alLimpiar: (() -> Unit)? = null
) {
    val formaPildora = RoundedCornerShape(50)
    val colorFondo = if (activo) colorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes
    val colorBorde = if (activo) {
        colorAcento.copy(alpha = 0.85f)
    } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        ColorBordeActual
    } else {
        ColorSeparadorAjustes.copy(alpha = 0.7f)
    }

    Box(
        modifier = modifier
            .clip(formaPildora)
            .background(colorFondo)
            .border(
                width = if (activo) 1.dp else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 0.8.dp,
                color = colorBorde,
                shape = formaPildora
            )
            .clickable { alPulsar() }
            .padding(start = 9.dp, end = 7.dp, top = 5.dp, bottom = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (activo) colorAcento else TextoSecundario,
                modifier = Modifier.size(15.dp)
            )

            if (!texto.isNullOrBlank()) {
                Text(
                    text = texto,
                    color = if (activo) colorAcento else TextoPrincipal,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (activo) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (activo && alLimpiar != null) {
                Box(
                    modifier = Modifier
                        .size(17.dp)
                        .clip(CircleShape)
                        .background(colorAcento.copy(alpha = 0.22f))
                        .clickable { alLimpiar() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Limpiar filtro",
                        tint = colorAcento,
                        modifier = Modifier.size(11.dp)
                    )
                }
            } else if (!activo) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = TextoSecundario.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
