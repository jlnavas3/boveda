/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupoColapsable
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
        ComponenteGrupoColapsable(
            etiqueta = "Acento esencial",
            subtitulo = "(10% interfaz)",
            icono = Icons.Filled.Security,
            expandidoInicial = false,
            modifier = modifier
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Elige el acento protagonista para acciones primarias, botones y estados activos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorTextoSecundario
                )

                Spacer(Modifier.height(14.dp))

                // Sección 1: Acentos Cromáticos (2 filas de 6)
                Text(
                    text = "Acentos Cromáticos",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorAjusteGris
                )
                Spacer(Modifier.height(8.dp))

                val cromFila1 = acentosPredefinidos.take(6)
                val cromFila2 = acentosPredefinidos.drop(6)

                // Fila 1 de Cromáticos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    cromFila1.forEach { (color, nombre) ->
                        val seleccionado = !esPersonalizadoActivo &&
                            kotlin.math.abs(colorAcentoActual.red - color.red) < 0.02f &&
                            kotlin.math.abs(colorAcentoActual.green - color.green) < 0.02f &&
                            kotlin.math.abs(colorAcentoActual.blue - color.blue) < 0.02f
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

                if (cromFila2.isNotEmpty()) {
                    Spacer(Modifier.height(10.dp))
                    // Fila 2 de Cromáticos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        cromFila2.forEach { (color, nombre) ->
                            val seleccionado = !esPersonalizadoActivo &&
                                kotlin.math.abs(colorAcentoActual.red - color.red) < 0.02f &&
                                kotlin.math.abs(colorAcentoActual.green - color.green) < 0.02f &&
                                kotlin.math.abs(colorAcentoActual.blue - color.blue) < 0.02f
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
                }

                Spacer(Modifier.height(16.dp))

                // Sección 2: Matices Neutros y Personalizado (2 filas de 6)
                Text(
                    text = "Matices Neutros Sofisticados y Personalizado",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorAjusteGris
                )
                Spacer(Modifier.height(8.dp))

                val neutrosFila1 = acentosGrisesNeutros.take(5)
                val neutrosFila2 = acentosGrisesNeutros.drop(5)

                // Fila 1 de Neutros (5 elementos)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    neutrosFila1.forEach { (color, nombre) ->
                        val seleccionado = !esPersonalizadoActivo &&
                            kotlin.math.abs(colorAcentoActual.red - color.red) < 0.02f &&
                            kotlin.math.abs(colorAcentoActual.green - color.green) < 0.02f &&
                            kotlin.math.abs(colorAcentoActual.blue - color.blue) < 0.02f
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
                    // Espaciador equivalente para alinear visualmente a 6 columnas
                    Spacer(modifier = Modifier.size(42.dp))
                }

                Spacer(Modifier.height(10.dp))

                // Fila 2 de Neutros (5 elementos + Botón Personalizado = 6)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    neutrosFila2.forEach { (color, nombre) ->
                        val seleccionado = !esPersonalizadoActivo &&
                            kotlin.math.abs(colorAcentoActual.red - color.red) < 0.02f &&
                            kotlin.math.abs(colorAcentoActual.green - color.green) < 0.02f &&
                            kotlin.math.abs(colorAcentoActual.blue - color.blue) < 0.02f
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

                    // Botón Personalizado en el 6º slot
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
                    ControlesSlidersAcentoLab(
                        estadoLab = estadoLab,
                        colorCampo = colorCampo,
                        colorBorde = colorBorde,
                        colorTextoPrincipal = colorTextoPrincipal,
                        colorTextoSecundario = colorTextoSecundario,
                        marcarModificado = marcarModificado
                    )
                }
            }
        }
    }
}
