/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun TarjetaNotasDetalle(
    notas: String,
    ultimaCopia: String?,
    alCopiarNotas: () -> Unit,
    ajustes: AjustesApp? = null,
    titulo: String = "Notas"
) {
    if (notas.isBlank()) return

    val colorBordeNota = Color(0xFFFFD54F)
    val protegerNotas = ajustes?.seguridadVisualActiva == true && ajustes.ocultarNotas
    var notasReveladas by remember(notas, protegerNotas) { mutableStateOf(!protegerNotas) }

    TemporizadorAutoOcultar(
        revelado = notasReveladas && protegerNotas,
        tiempoSegundos = if (ajustes?.seguridadVisualActiva == true) ajustes.tiempoAutoOcultarSegundos else 0,
        alAutoOcultar = { notasReveladas = false }
    )

    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estaOculto = protegerNotas && !notasReveladas

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = titulo)
        TarjetaDatoDetalle(colorBorde = colorBordeNota) {
            if (estaOculto) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextoSeguroVisual(
                        texto = notas,
                        oculto = true,
                        estilo = ajustes.estiloOcultamientoVisual,
                        estiloTexto = MaterialTheme.typography.bodyMedium,
                        colorTexto = TextoPrincipal,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BotonIconoDetalle(
                            icono = Icons.Filled.Visibility,
                            descripcion = "Mostrar notas",
                            alPulsar = {
                                haptica.toque()
                                notasReveladas = true
                            }
                        )
                        BotonCopiarDetalle(copiado = ultimaCopia == "notas", alPulsar = alCopiarNotas)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 10.dp)
                ) {
                    com.jlnavas3.bovedalocal.ui.componentes.VisualizadorMarkdownNativo(
                        contenido = notas,
                        colorTextoPorDefecto = TextoPrincipal
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (protegerNotas) {
                            BotonIconoDetalle(
                                icono = Icons.Filled.VisibilityOff,
                                descripcion = "Ocultar notas",
                                alPulsar = {
                                    haptica.toque()
                                    notasReveladas = false
                                }
                            )
                        }
                        BotonCopiarDetalle(copiado = ultimaCopia == "notas", alPulsar = alCopiarNotas)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaNotasDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        TarjetaNotasDetalle(
            notas = "Recuerda renovar la suscripción anual en noviembre.\nNúmero de contrato: CT-884920.",
            ultimaCopia = null,
            alCopiarNotas = {}
        )
    }
}

