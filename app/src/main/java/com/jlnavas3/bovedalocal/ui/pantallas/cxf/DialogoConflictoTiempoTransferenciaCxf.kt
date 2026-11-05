/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo modal limpio y centrado para resolver el conflicto cuando el tiempo de ventana
 * de transferencia manual es mayor al tiempo de bloqueo por inactividad de la bóveda.
 */
@Composable
fun DialogoConflictoTiempoTransferenciaCxf(
    tiempoTransferenciaTexto: String,
    tiempoBloqueoActualTexto: String,
    alAjustarBloqueo: () -> Unit,
    alMantener: () -> Unit,
    alCancelar: () -> Unit
) {
    DialogoBoveda(
        onDismissRequest = alCancelar,
        icon = {
            ContenedorIconoInsignia(
                icono = Icons.Filled.HourglassTop,
                color = Color(0xFFFFB300),
                tamano = TamanoInsignia.HERO
            )
        },
        title = {
            TextoTitulo(
                texto = "Tiempo superior al auto-bloqueo",
                estilo = EstiloTitulo.MEDIANO
            )
        },
        text = {
            androidx.compose.foundation.layout.Column {
                Text(
                    text = "Has seleccionado una disponibilidad de $tiempoTransferenciaTexto, pero el bloqueo por inactividad de la bóveda está fijado en $tiempoBloqueoActualTexto.",
                    color = TextoSecundario,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Si la bóveda se bloquea por inactividad antes de completar la transferencia, el registro con Android se revocará inmediatamente por seguridad.",
                    color = TextoSecundario,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "¿Deseas ampliar el tiempo de bloqueo por inactividad para que coincida con la disponibilidad seleccionada?",
                    color = TextoSecundario,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            BotonBoveda(
                texto = "Ajustar bloqueo a $tiempoTransferenciaTexto",
                variante = VarianteBoton.PRIMARIO,
                alPulsar = alAjustarBloqueo
            )
        },
        dismissButton = {
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                BotonTextoBoveda(
                    texto = "Cancelar",
                    alPulsar = alCancelar
                )
                BotonTextoBoveda(
                    texto = "Mantener en $tiempoBloqueoActualTexto",
                    colorPersonalizado = ColorAcento,
                    alPulsar = alMantener
                )
            }
        }
    )
}
