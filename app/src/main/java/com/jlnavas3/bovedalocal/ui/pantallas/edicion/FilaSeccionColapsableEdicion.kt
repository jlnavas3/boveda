package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente que representa una sección desplegable de 1 sola línea para el formulario de edición,
 * con insignia de icono, tipografía consistente, resumen de estado y transición animada.
 */
@Composable
fun FilaSeccionColapsableEdicion(
    icono: ImageVector,
    colorIcono: Color,
    titulo: String,
    resumen: String? = null,
    expandido: Boolean,
    alAlternarExpandido: () -> Unit,
    modifier: Modifier = Modifier,
    insigniaTexto: String? = null,
    contenido: @Composable () -> Unit
) {
    val forma = FormaCampo
    val colorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        ColorBordeActual
    } else {
        ColorSeparadorAjustes.copy(alpha = 0.4f)
    }
    val grosor = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 0.8.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorCampoAjustes)
            .border(grosor, colorBorde, forma)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { alAlternarExpandido() }
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(colorIcono.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(Modifier.width(11.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!resumen.isNullOrBlank()) {
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = resumen,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                        color = TextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (!insigniaTexto.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colorIcono.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = insigniaTexto,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = colorIcono
                    )
                }
                Spacer(Modifier.width(6.dp))
            }

            Icon(
                imageVector = if (expandido) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (expandido) "Contraer" else "Expandir",
                tint = TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }

        AnimatedVisibility(
            visible = expandido,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.6.dp)
                        .background(ColorSeparadorAjustes.copy(alpha = 0.45f))
                )
                Spacer(Modifier.height(10.dp))
                contenido()
            }
        }
    }
}
