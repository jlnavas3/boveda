/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.seleccion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.jlnavas3.bovedalocal.ui.componentes.hud.accionConHud
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Cabecera minimalista para el modo de selección múltiple (estilo Solid Explorer / Google).
 * Contiene el botón cerrar, el contador de elementos seleccionados y un único botón interactivo
 * toggle a la derecha que alterna entre "Seleccionar todo" y "Deseleccionar todo".
 */
@Composable
fun BarraSuperiorSeleccion(
    cantidad: Int,
    todoSeleccionado: Boolean,
    alCancelar: () -> Unit,
    alSeleccionarTodo: () -> Unit,
    alDeseleccionarTodo: () -> Unit,
    modifier: Modifier = Modifier,
    subtitulo: String? = null
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else Modifier
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .accionConHud(
                    etiqueta = "Cancelar selección",
                    icono = Icons.Filled.Close,
                    radioRipple = 19.dp,
                    alPulsar = alCancelar
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Cancelar selección",
                tint = TextoPrincipal,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(6.dp))

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$cantidad ${if (cantidad == 1) "seleccionada" else "seleccionadas"}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = ColorTitulos
            )

            if (!subtitulo.isNullOrBlank()) {
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "· $subtitulo",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }
        }

        // Botón interactivo toggle único: Seleccionar Todo <-> Deseleccionar Todo
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .accionConHud(
                    etiqueta = if (todoSeleccionado) "Deseleccionar todo" else "Seleccionar todo",
                    icono = if (todoSeleccionado) Icons.Filled.Deselect else Icons.Filled.SelectAll,
                    radioRipple = 19.dp,
                    alPulsar = {
                        if (todoSeleccionado) {
                            alDeseleccionarTodo()
                        } else {
                            alSeleccionarTodo()
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = todoSeleccionado,
                transitionSpec = {
                    (scaleIn(initialScale = 0.8f) + fadeIn()) togetherWith (scaleOut(targetScale = 0.8f) + fadeOut())
                },
                label = "ToggleSeleccionarTodoAnim"
            ) { estaTodoSeleccionado ->
                if (estaTodoSeleccionado) {
                    Icon(
                        imageVector = Icons.Filled.Deselect,
                        contentDescription = "Deseleccionar todo",
                        tint = ColorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.SelectAll,
                        contentDescription = "Seleccionar todo",
                        tint = TextoPrincipal,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
