package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.componentes.colorAhsv
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaAjuste
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente para la Sección 1 del Laboratorio de Temas:
 * Ajustes de la escala de capas neutras y tonos (90% de la interfaz).
 */
@Composable
fun SeccionEscalaCapasLab(
    estadoLab: EstadoLaboratorioTemas,
    colorFondo: Color,
    colorTarjeta: Color,
    colorCampo: Color,
    colorBorde: Color,
    colorTextoPrincipal: Color,
    colorTextoSecundario: Color,
    colorAcentoActual: Color,
    haptica: Haptica,
    marcarModificado: () -> Unit,
    modifier: Modifier = Modifier
) {
    with(estadoLab) {
        ComponenteGrupo(
            etiqueta = "Escala de Capas y Tonos (90% Interfaz)",
            icono = Icons.Filled.Palette,
            modifier = modifier
        ) {
            // Control Maestro de Tono y Switch de Unificación
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Aplicar el mismo tono a todos",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = ColorTextoAjustes
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = if (unificarTonos) "Todas las capas comparten el mismo tono" else "Tono independiente por cada capa",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorAjusteGris
                        )
                    }
                    SwitchBoveda(
                        checked = unificarTonos,
                        colorActivo = colorAcentoActual,
                        colorInactivoTrack = colorCampo,
                        colorInactivoThumb = colorTextoSecundario,
                        onCheckedChange = {
                            haptica.tic()
                            unificarTonos = it
                            marcarModificado()
                        }
                    )
                }

                Spacer(Modifier.height(10.dp))

                if (unificarTonos) {
                    // Slider de Tono Unificado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color.hsv(tonoGlobal, 0.8f, 0.9f))
                                .border(1.dp, colorBorde, CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Tono cromático unificado: ${tonoGlobal.toInt()}°",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = ColorTextoAjustes,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    SliderBoveda(
                        value = tonoGlobal,
                        onValueChange = {
                            tonoGlobal = it
                            marcarModificado()
                            if (unificarTonos) {
                                val (_, s, v) = colorAhsv(estadoLab.colorAcentoActual)
                                estadoLab.colorAcentoActual = Color.hsv(it, if (s < 0.05f) 0.75f else s, v, estadoLab.colorAcentoActual.alpha)
                                huePersonalizado = it
                            }
                        },
                        valueRange = 0f..360f
                    )
                    Spacer(Modifier.height(8.dp))
                }

                // Slider de Saturación / Intensidad de tinte en grises
                Text(
                    text = "Intensidad de tinte en grises: ${(saturacionTinte * 100).toInt()}% ${if (saturacionTinte <= 0.001f) "(Gris puro)" else if (saturacionTinte <= 0.12f) "(Matiz Elegante)" else ""}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = ColorAjusteGris
                )
                SliderBoveda(
                    value = saturacionTinte,
                    onValueChange = {
                        saturacionTinte = it
                        marcarModificado()
                    },
                    valueRange = 0f..0.35f
                )
            }

            SeparadorFilaAjuste()

            // Capa 0: Fondo de Pantalla
            ControlCapaFila(
                etiqueta = "Fondo general (Capa 0)",
                colorActual = colorFondo,
                luminancia = lumFondo,
                alCambiarLuminancia = {
                    lumFondo = it
                    marcarModificado()
                },
                mostrarControlTono = !unificarTonos,
                tono = tonoFondo,
                alCambiarTono = {
                    tonoFondo = it
                    marcarModificado()
                }
            )
            SeparadorFilaAjuste()

            // Capa 1: Tarjetas y Grupos
            ControlCapaFila(
                etiqueta = "Tarjetas y grupos (Capa 1)",
                colorActual = colorTarjeta,
                luminancia = lumTarjeta,
                alCambiarLuminancia = {
                    lumTarjeta = it
                    marcarModificado()
                },
                mostrarControlTono = !unificarTonos,
                tono = tonoTarjeta,
                alCambiarTono = {
                    tonoTarjeta = it
                    marcarModificado()
                }
            )
            SeparadorFilaAjuste()

            // Capa 2: Campos de entrada y chips
            ControlCapaFila(
                etiqueta = "Campos y chips (Capa 2)",
                colorActual = colorCampo,
                luminancia = lumCampo,
                alCambiarLuminancia = {
                    lumCampo = it
                    marcarModificado()
                },
                mostrarControlTono = !unificarTonos,
                tono = tonoCampo,
                alCambiarTono = {
                    tonoCampo = it
                    marcarModificado()
                }
            )
            SeparadorFilaAjuste()

            // Bordes y separadores
            ControlCapaFila(
                etiqueta = "Bordes y líneas divisorias",
                colorActual = colorBorde,
                luminancia = lumBorde,
                alCambiarLuminancia = {
                    lumBorde = it
                    marcarModificado()
                },
                mostrarControlTono = !unificarTonos,
                tono = tonoBorde,
                alCambiarTono = {
                    tonoBorde = it
                    marcarModificado()
                }
            )
            SeparadorFilaAjuste()

            // Texto Principal
            ControlCapaFila(
                etiqueta = "Texto principal (Títulos y datos)",
                colorActual = colorTextoPrincipal,
                luminancia = lumTextoPrincipal,
                alCambiarLuminancia = {
                    lumTextoPrincipal = it
                    marcarModificado()
                },
                mostrarControlTono = !unificarTonos,
                tono = tonoTextoPrincipal,
                alCambiarTono = {
                    tonoTextoPrincipal = it
                    marcarModificado()
                }
            )
            SeparadorFilaAjuste()

            // Texto Secundario
            ControlCapaFila(
                etiqueta = "Texto secundario (Subtítulos)",
                colorActual = colorTextoSecundario,
                luminancia = lumTextoSecundario,
                alCambiarLuminancia = {
                    lumTextoSecundario = it
                    marcarModificado()
                },
                mostrarControlTono = !unificarTonos,
                tono = tonoTextoSecundario,
                alCambiarTono = {
                    tonoTextoSecundario = it
                    marcarModificado()
                }
            )
        }
    }
}
