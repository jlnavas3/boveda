/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Remove
import com.jlnavas3.bovedalocal.util.CLAVE_GRUPO_ANCLADOS
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewMocks
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAnclado
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Contenedor de tarjeta agrupada para el Listado Principal estilo Samsung One UI / Honor MagicOS.
 *
 * Encapsula la cabecera del grupo (dominio, total de cuentas, icono y chevron)
 * y sus entradas hijas dentro de una única tarjeta con esquinas redondeadas (18.dp),
 * separadas por divisores interiores discretos con sangría interior.
 */
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ComponenteGrupoLista(
    clave: String,
    entradas: List<Entrada>,
    expandido: Boolean,
    alAlternar: () -> Unit,
    modifier: Modifier = Modifier,
    alturaFila: Dp = 74.dp,
    tamanoMonograma: Int = 46,
    resaltado: Boolean = false,
    seleccionActiva: Boolean = false,
    seleccionado: Boolean = false,
    parcialmenteSeleccionado: Boolean = false,
    alPulsarLargo: (() -> Unit)? = null,
    alAlternarExpansion: (() -> Unit)? = null,
    alAlternarSeleccionGrupo: (() -> Unit)? = null,
    contenidoEntrada: @Composable (entrada: Entrada, indice: Int, total: Int) -> Unit
) {
    val compacta = alturaFila.value <= 48f
    val formaGrupo = RoundedCornerShape(CurvaturaEsquinas)
    val tamanoIcono = if (compacta) 28 else if (alturaFila.value <= 64f) 34 else 38
    val sangriaSeparador = (14 + tamanoIcono + 12).dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(formaGrupo)
            .background(if (resaltado) ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, formaGrupo)
                else Modifier
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Cabecera del grupo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(alturaFila)
                    .combinedClickable(
                        onClick = alAlternar,
                        onLongClick = alPulsarLargo
                    )
                    .padding(horizontal = 14.dp, vertical = if (compacta) 3.dp else 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val esAnclados = clave == CLAVE_GRUPO_ANCLADOS
                val tituloVisual = if (esAnclados) "Anclados" else clave
                val vectorIcono = if (esAnclados) Icons.Filled.PushPin else Icons.Filled.Dns
                val colorIcono = if (esAnclados) ColorAnclado else ColorIconosInternos
                val textoConteo = if (esAnclados) {
                    "${entradas.size} ${if (entradas.size == 1) "anclada" else "ancladas"}"
                } else {
                    "${entradas.size} ${if (entradas.size == 1) "cuenta" else "cuentas"}"
                }

                AvatarSeleccionableGrupo(
                    vectorIcono = vectorIcono,
                    colorIcono = colorIcono,
                    tamanoIcono = tamanoIcono,
                    seleccionado = seleccionado,
                    parcialmenteSeleccionado = parcialmenteSeleccionado,
                    alAlternarSeleccion = {
                        if (alAlternarSeleccionGrupo != null) {
                            alAlternarSeleccionGrupo()
                        } else {
                            alAlternar()
                        }
                    }
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = tituloVisual,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (resaltado) ColorAcento else TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = textoConteo,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextoSecundario,
                    maxLines = 1
                )

                Spacer(Modifier.width(16.dp))

                IconButton(
                    onClick = {
                        if (alAlternarExpansion != null) alAlternarExpansion() else alAlternar()
                    },
                    modifier = Modifier.size(if (compacta) 28.dp else 32.dp)
                ) {
                    Icon(
                        imageVector = if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expandido) "Contraer" else "Expandir",
                        tint = if (resaltado) ColorAcento else ColorIconosInternos
                    )
                }
            }

            // Entradas hijas integradas dentro de la misma tarjeta
            AnimatedVisibility(
                visible = expandido,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    entradas.forEachIndexed { indice, entrada ->
                        HorizontalDivider(
                            color = ColorSeparadorAjustes.copy(alpha = 0.65f),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(
                                start = if (indice == 0) 14.dp else sangriaSeparador,
                                end = 14.dp
                            )
                        )
                        contenidoEntrada(entrada, indice, entradas.size)
                    }
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviewComponenteGrupoLista() {
    PreviewTemaBoveda {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponenteGrupoLista(
                clave = "google.com",
                entradas = listOf(PreviewMocks.entradaEjemplo, PreviewMocks.entradaBancaria),
                expandido = true,
                alAlternar = {},
                contenidoEntrada = { entrada, _, _ ->
                    FilaEntrada(
                        entrada = entrada,
                        seleccionActiva = false,
                        seleccionado = false,
                        alAbrir = {},
                        alCopiarUsuario = {},
                        alCopiarContrasena = {},
                        alFavorito = {},
                        alPulsarLargo = {},
                        alAlternarSeleccion = {}
                    )
                }
            )
        }
    }
}

