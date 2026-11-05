/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion.notas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.VisualizadorMarkdownNativo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AplicadorFormatoMarkdown

/**
 * Microcomponente desacoplado que renderiza el campo de edición directa de Notas
 * para entradas de tipo [com.jlnavas3.bovedalocal.data.TipoEntrada.NOTA].
 * No utiliza acordeón colapsable, titula el campo como "Nota" e integra la barra de herramientas
 * Markdown y el visor de vista previa interactivo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoNotaEnriquecidaEdicion(
    notas: String,
    alCambiarNotas: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var enVistaPrevia by remember { mutableStateOf(false) }
    var tfv by remember {
        mutableStateOf(TextFieldValue(text = notas, selection = TextRange(notas.length)))
    }

    if (tfv.text != notas) {
        val nuevoCursor = tfv.selection.end.coerceIn(0, notas.length)
        tfv = tfv.copy(text = notas, selection = TextRange(nuevoCursor))
    }

    val colorBordeIzquierdo = Color(0xFFFFD54F)
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val grosorConfigurado = if (GrosorBorde > 0.dp) GrosorBorde else 1.dp
    val borderModifier = when {
        isFocused -> Modifier.border(grosorConfigurado, ColorAcento.copy(alpha = 0.65f), FormaCampo)
        GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent -> Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
        else -> Modifier
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Description,
                    contentDescription = null,
                    tint = colorBordeIzquierdo,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Nota",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    ),
                    color = TextoSecundario
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        BarraHerramientasFormatoNota(
            enVistaPrevia = enVistaPrevia,
            alAlternarVistaPrevia = { enVistaPrevia = !enVistaPrevia },
            alAplicarFormato = { accion ->
                val resultado = AplicadorFormatoMarkdown.aplicar(tfv, accion)
                tfv = resultado
                alCambiarNotas(resultado.text)
            }
        )

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(FormaCampo)
                .background(ColorCampoAjustes)
                .then(borderModifier)
        ) {
            if (enVistaPrevia) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 140.dp)
                        .padding(start = 16.dp, end = 14.dp, top = 12.dp, bottom = 12.dp)
                ) {
                    VisualizadorMarkdownNativo(
                        contenido = tfv.text,
                        colorTextoPorDefecto = TextoPrincipal
                    )
                }
            } else {
                val coloresSinBordes = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    cursorColor = ColorAcento,
                    focusedTextColor = TextoPrincipal,
                    unfocusedTextColor = TextoPrincipal
                )

                BasicTextField(
                    value = tfv,
                    onValueChange = { nuevo ->
                        tfv = nuevo
                        alCambiarNotas(nuevo.text)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 140.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = TextoPrincipal,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    ),
                    cursorBrush = SolidColor(ColorAcento),
                    singleLine = false,
                    minLines = 6,
                    interactionSource = interactionSource,
                    decorationBox = @Composable { innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            value = tfv.text,
                            innerTextField = innerTextField,
                            enabled = true,
                            singleLine = false,
                            visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                            interactionSource = interactionSource,
                            placeholder = {
                                Text(
                                    text = "Escribe tu nota aquí... (admite formato Markdown)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextoSecundario.copy(alpha = 0.6f)
                                )
                            },
                            shape = FormaCampo,
                            colors = coloresSinBordes,
                            contentPadding = PaddingValues(start = 16.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
                            container = {}
                        )
                    }
                )
            }

            Box(
                modifier = Modifier.matchParentSize()
            ) {
                Box(
                    modifier = Modifier
                        .width(4.5.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .background(colorBordeIzquierdo)
                )
            }
        }
    }
}
