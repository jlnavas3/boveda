package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.colorParaGrupoId
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

val LocalColorGrupo = compositionLocalOf<Color?> { null }

/**
 * Contenedor de tarjeta agrupada para Ajustes estilo Samsung One UI / Honor MagicOS.
 *
 * Incluye:
 * - Etiqueta de encabezado en mayúsculas discretas.
 * - ID de grupo opcional (solo visible si mostrarId es true).
 * - Tarjeta con bordes redondeados (18.dp) y fondo de superficie limpio.
 * - Descripción o subtítulo opcional a nivel de grupo (por defecto como pie de tarjeta estilo One UI),
 *   lo que evita texto innecesario en cada fila.
 */
@Composable
fun ComponenteGrupo(
    modifier: Modifier = Modifier,
    etiqueta: String? = null,
    idGrupo: String? = null,
    mostrarId: Boolean = false,
    descripcion: String? = null,
    descripcionComoPie: Boolean = true,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val tieneBadgeGrupo = mostrarId && !idGrupo.isNullOrBlank()
    val colorBadgeGrupo = colorParaGrupoId(idGrupo)

    Column(modifier = modifier.fillMaxWidth()) {
        // Encabezado del grupo
        if (!etiqueta.isNullOrBlank() || tieneBadgeGrupo) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!etiqueta.isNullOrBlank()) {
                    Text(
                        text = etiqueta.uppercase(),
                        color = ColorAjusteGris,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                if (tieneBadgeGrupo) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(fondoBadgeParaTema(colorBadgeGrupo))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = idGrupo!!,
                            color = colorLegibleParaTema(colorBadgeGrupo),
                            style = EstiloMono.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // Descripción antes de la tarjeta si descripcionComoPie es false
        if (!descripcionComoPie && !descripcion.isNullOrBlank()) {
            Text(
                text = descripcion,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }

        // Tarjeta agrupada con esquinas redondeadas
        CompositionLocalProvider(LocalColorGrupo provides colorBadgeGrupo) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(ColorTarjetaAjustes)
            ) {
                contenido()
            }
        }

        // Descripción como pie sutil debajo de la tarjeta (estilo nativo Samsung One UI)
        if (descripcionComoPie && !descripcion.isNullOrBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = descripcion,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
