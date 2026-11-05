/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo modal desacoplado para elegir entre llamada telefónica directa o mediante prefijo de número oculto.
 */
@Composable
fun DialogoOpcionesLlamada(
    abierto: Boolean,
    alCerrar: () -> Unit,
    numero: String,
    prefijoOculto: String,
    alLlamarDirecto: () -> Unit,
    alLlamarOculto: () -> Unit
) {
    if (!abierto) return

    val formaItem = RoundedCornerShape(CurvaturaEsquinas)
    val modBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        Modifier.border(GrosorBorde, ColorBordeActual, formaItem)
    } else Modifier

    DialogoBoveda(
        abierto = abierto,
        alCerrar = alCerrar,
        titulo = "Llamada telefónica",
        icono = Icons.Filled.Call,
        colorIcono = ColorAcento,
        botonDescartar = {
            BotonTextoBoveda(texto = "Cancelar", alPulsar = alCerrar, tipo = TipoBotonTexto.SECUNDARIO)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = numero,
                style = EstiloMono.copy(fontWeight = FontWeight.SemiBold),
                color = TextoPrincipal
            )

            Spacer(Modifier.height(16.dp))

            // Opción 1: Llamada estándar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaItem)
                    .then(modBorde)
                    .background(ColorCampoAjustes)
                    .clickable {
                        alCerrar()
                        alLlamarDirecto()
                    }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Llamada estándar",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Marca el número habitual mostrando tu ID",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Opción 2: Llamada con número oculto
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaItem)
                    .then(modBorde)
                    .background(ColorCampoAjustes)
                    .clickable {
                        alCerrar()
                        alLlamarOculto()
                    }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Llamada oculta (${prefijoOculto})",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Añade el prefijo de privacidad del operador",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}
