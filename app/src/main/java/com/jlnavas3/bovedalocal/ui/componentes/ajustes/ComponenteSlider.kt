@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.background
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
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.colorParaGrupoId
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/**
 * Fila con control deslizante (Slider) estilo Samsung One UI / Honor MagicOS.
 *
 * Presenta:
 * - Icono squircle opcional.
 * - Título del parámetro y badge de ID opcional.
 * - Indicador en píldora con el valor formateado actual.
 * - Deslizador suave y etiquetas de mínimo/máximo opcionales.
 * - Soporte nativo para auto-scroll y alumbrado animado.
 */
@Composable
fun ComponenteSlider(
    titulo: String,
    valor: Float,
    valorTexto: String,
    alCambiar: (Float) -> Unit,
    modifier: Modifier = Modifier,
    rango: ClosedFloatingPointRange<Float> = 0f..100f,
    pasos: Int = 0,
    etiquetaMin: String? = null,
    etiquetaMax: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    habilitado: Boolean = true,
    colorAcento: Color = ColorAcento
) {
    val estadoAlumbrado = recordarEstadoAlumbrado(idFila)
    val coordinador = LocalCoordinadorResaltado.current
    val tieneBadgeId = mostrarId && !idFila.isNullOrBlank()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .bringIntoViewRequester(estadoAlumbrado.bringIntoViewRequester)
            .background(estadoAlumbrado.colorFondoAnimado.value)
            .onGloballyPositioned { coords ->
                coordinador?.registrarYEjecutarSiCoincide(
                    id = idFila,
                    itemCoordinates = coords,
                    estadoAlumbrado = estadoAlumbrado,
                    colorAcento = colorAcento
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Cabecera: Icono + Título/ID + Píldora de Valor
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icono != null) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colorIcono ?: colorAcento),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorTinteIcono,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
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

            Spacer(Modifier.width(8.dp))

            // Indicador en píldora del valor numérico
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorAcento.copy(alpha = 0.16f))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = valorTexto,
                    color = colorAcento,
                    style = EstiloMono.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // Barra deslizadora
        Slider(
            value = valor,
            onValueChange = alCambiar,
            valueRange = rango,
            steps = pasos,
            enabled = habilitado,
            colors = SliderDefaults.colors(
                thumbColor = colorAcento,
                activeTrackColor = colorAcento,
                inactiveTrackColor = ColorBordeActual.copy(alpha = 0.3f),
                disabledThumbColor = ColorAjusteGris,
                disabledActiveTrackColor = ColorAjusteGris.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Rótulos de mínimo y máximo si están presentes
        if (!etiquetaMin.isNullOrBlank() || !etiquetaMax.isNullOrBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = etiquetaMin ?: "",
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                )
                Text(
                    text = etiquetaMax ?: "",
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                )
            }
        }
    }
}
