/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import com.jlnavas3.bovedalocal.ui.componentes.hud.accionConHud
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import kotlinx.coroutines.delay

/**
 * Microcomponente de barra superior para la pantalla de exportación (.bvda):
 * - Título "Exportación" con subtítulo de conteo integrado.
 * - Despliegue animado opcional del buscador compacto con placeholder "Buscar...".
 * - Cabecera limpia y despejada cuando se exporta una selección previa.
 */
@Composable
fun BarraSuperiorExportar(
    subtitulo: String?,
    mostrarId: Boolean,
    esSeleccionPrevia: Boolean,
    busquedaVisible: Boolean,
    textoBusqueda: String,
    totalDisponibles: Int,
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alCambiarTextoBusqueda: (String) -> Unit,
    alCerrarBusqueda: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(busquedaVisible) {
        if (busquedaVisible) {
            delay(100)
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Exportación",
            subtitulo = subtitulo,
            idEtiqueta = "05-COP-EXP",
            mostrarId = mostrarId,
            alVolver = alVolver,
            colorFondo = ColorAjustesFondo,
            estiloTitulo = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            acciones = {
                if (!esSeleccionPrevia && totalDisponibles > 1) {
                    BotonIconoCabecera(
                        onClick = alAlternarBusqueda,
                        icono = if (busquedaVisible) Icons.Filled.Close else Icons.Filled.Search,
                        descripcion = if (busquedaVisible) "Cerrar búsqueda" else "Buscar entradas",
                        tint = if (busquedaVisible || textoBusqueda.isNotBlank()) ColorAcento else ColorIconosInternos
                    )
                }
            }
        )

        AnimatedVisibility(
            visible = busquedaVisible && !esSeleccionPrevia,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            val forma = RoundedCornerShape(CurvaturaEsquinas)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(forma)
                        .background(ColorTarjetaAjustes)
                        .then(
                            if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                            else Modifier.border(0.8.dp, ColorSeparadorAjustes, forma)
                        )
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = if (textoBusqueda.isBlank()) TextoSecundario else ColorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textoBusqueda.isEmpty()) {
                            Text(
                                text = "Buscar...",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        BasicTextField(
                            value = textoBusqueda,
                            onValueChange = alCambiarTextoBusqueda,
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextoPrincipal, fontSize = 15.sp),
                            cursorBrush = SolidColor(ColorAcento),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                        )
                    }

                    if (textoBusqueda.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .accionConHud(
                                    etiqueta = "Borrar texto",
                                    icono = Icons.Filled.Close,
                                    radioRipple = 14.dp,
                                    alPulsar = { alCambiarTextoBusqueda("") }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Limpiar texto",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .accionConHud(
                                    etiqueta = "Cerrar búsqueda",
                                    icono = Icons.Filled.Close,
                                    radioRipple = 14.dp,
                                    alPulsar = alCerrarBusqueda
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Cerrar búsqueda",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
