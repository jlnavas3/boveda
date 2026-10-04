package com.jlnavas3.bovedalocal.ui.pantallas.tema

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.componentes.colorAhsv
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.aHexConAlfa
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente para la Sección 2 del Laboratorio de Temas:
 * Acento Esencial (10% de color), paletas de matices neutros y ajustes cromáticos avanzados.
 */
@Composable
fun SeccionAcentoEsencialLab(
    estadoLab: EstadoLaboratorioTemas,
    colorCampo: Color,
    colorBorde: Color,
    colorTextoPrincipal: Color,
    colorTextoSecundario: Color,
    acentosPredefinidos: List<Pair<Color, String>>,
    acentosGrisesNeutros: List<Pair<Color, String>>,
    haptica: Haptica,
    marcarModificado: () -> Unit,
    modifier: Modifier = Modifier
) {
    with(estadoLab) {
        ComponenteGrupo(
            etiqueta = "Acento Esencial (10% Color)",
            icono = Icons.Filled.Security,
            modifier = modifier
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Elige el acento protagonista para acciones primarias, botones y estados activos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorTextoSecundario
                )

                Spacer(Modifier.height(14.dp))

                // Fila 1: Acentos Cromáticos
                Text(
                    text = "Acentos Cromáticos",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorAjusteGris
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    acentosPredefinidos.forEach { (color, nombre) ->
                        val seleccionado = !esPersonalizadoActivo && colorAcentoActual == color
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (seleccionado) 3.dp else 1.dp,
                                    color = if (seleccionado) ColorTextoAjustes else colorBorde,
                                    shape = CircleShape
                                )
                                .clickable {
                                    haptica.tic()
                                    colorAcentoActual = color
                                    esPersonalizadoActivo = false
                                    val (h, s, v) = colorAhsv(color)
                                    huePersonalizado = h
                                    satPersonalizado = s
                                    valPersonalizado = v
                                    alfaPersonalizado = color.alpha
                                    marcarModificado()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (seleccionado) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = nombre,
                                    tint = colorContraste(color),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Fila 2: Matices Neutros y Personalizado
                Text(
                    text = "Matices Neutros Sofisticados y Personalizado",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorAjusteGris
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    acentosGrisesNeutros.forEach { (color, nombre) ->
                        val seleccionado = !esPersonalizadoActivo && colorAcentoActual == color
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (seleccionado) 3.dp else 1.dp,
                                    color = if (seleccionado) ColorTextoAjustes else colorBorde,
                                    shape = CircleShape
                                )
                                .clickable {
                                    haptica.tic()
                                    colorAcentoActual = color
                                    esPersonalizadoActivo = false
                                    val (h, s, v) = colorAhsv(color)
                                    huePersonalizado = h
                                    satPersonalizado = s
                                    valPersonalizado = v
                                    alfaPersonalizado = color.alpha
                                    marcarModificado()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (seleccionado) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = nombre,
                                    tint = colorContraste(color),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Botón Personalizado
                    val seleccionadoPersonalizado = esPersonalizadoActivo
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                if (seleccionadoPersonalizado) colorAcentoActual else colorCampo
                            )
                            .border(
                                width = if (seleccionadoPersonalizado) 3.dp else 1.dp,
                                color = if (seleccionadoPersonalizado) ColorTextoAjustes else colorBorde,
                                shape = CircleShape
                            )
                            .clickable {
                                haptica.tic()
                                esPersonalizadoActivo = true
                                mostrarAjustePersonalizado = true
                                colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                marcarModificado()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = "Personalizado",
                            tint = if (seleccionadoPersonalizado) colorContraste(colorAcentoActual) else ColorTextoAjustes,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Panel expandible para controles personalizados con sliders y transparencias
                if (mostrarAjustePersonalizado || esPersonalizadoActivo) {
                    Spacer(Modifier.height(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                            .background(colorCampo)
                            .border(1.dp, colorBorde, RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(colorAcentoActual)
                                    .border(1.dp, colorBorde, CircleShape)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Acento Personalizado",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = colorTextoPrincipal,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = colorAcentoActual.aHexConAlfa(),
                                style = EstiloMono.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                                color = colorTextoPrincipal
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Control 1: Tono / Color
                        Text(
                            text = "Tono: ${huePersonalizado.toInt()}°",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = colorTextoSecundario
                        )
                        SliderBoveda(
                            value = huePersonalizado,
                            onValueChange = {
                                huePersonalizado = it
                                esPersonalizadoActivo = true
                                colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                marcarModificado()
                            },
                            valueRange = 0f..360f
                        )

                        Spacer(Modifier.height(8.dp))

                        // Control 2: Saturación (0% es Gris puro)
                        Text(
                            text = "Saturación: ${(satPersonalizado * 100).toInt()}% ${if (satPersonalizado < 0.05f) "(Gris Puro)" else ""}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = colorTextoSecundario
                        )
                        SliderBoveda(
                            value = satPersonalizado,
                            onValueChange = {
                                satPersonalizado = it
                                esPersonalizadoActivo = true
                                colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                marcarModificado()
                            },
                            valueRange = 0f..1f
                        )

                        Spacer(Modifier.height(8.dp))

                        // Control 3: Brillo / Luminosidad
                        Text(
                            text = "Brillo: ${(valPersonalizado * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = colorTextoSecundario
                        )
                        SliderBoveda(
                            value = valPersonalizado,
                            onValueChange = {
                                valPersonalizado = it
                                esPersonalizadoActivo = true
                                colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                marcarModificado()
                            },
                            valueRange = 0.10f..1f
                        )

                        Spacer(Modifier.height(8.dp))

                        // Control 4: Transparencia / Opacidad (Alfa)
                        Text(
                            text = "Transparencia / Opacidad: ${(alfaPersonalizado * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (alfaPersonalizado < 1.0f) colorAcentoActual else colorTextoSecundario
                            )
                        )
                        SliderBoveda(
                            value = alfaPersonalizado,
                            onValueChange = {
                                alfaPersonalizado = it
                                esPersonalizadoActivo = true
                                colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                marcarModificado()
                            },
                            valueRange = 0.10f..1f
                        )
                    }
                }
            }
        }
    }
}
