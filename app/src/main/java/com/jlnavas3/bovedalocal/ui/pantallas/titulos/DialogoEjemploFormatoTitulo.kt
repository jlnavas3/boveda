/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.ModoFormatoTitulos
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoEjemploFormatoTitulo(
    modo: ModoFormatoTitulos,
    alCerrar: () -> Unit
) {
    val (titulo, descripcion, ejemplos) = when (modo) {
        ModoFormatoTitulos.EXPLICITO_PARENTESIS -> Triple(
            "Servicio (usuario)",
            "Añade el usuario o correo entre paréntesis al final del nombre.",
            listOf("Instagram (jolunavi)", "Google (cuenta@gmail.com)", "GitHub (dev_jose)")
        )
        ModoFormatoTitulos.EXPLICITO_SEPARADOR -> Triple(
            "Servicio · usuario",
            "Separa el nombre del servicio y el usuario con un punto medio.",
            listOf("Instagram · jolunavi", "Google · cuenta@gmail.com", "GitHub · dev_jose")
        )
        ModoFormatoTitulos.MINIMALISTA -> Triple(
            "Solo servicio (Minimalista)",
            "Mantiene únicamente el nombre del servicio en el título. El usuario se consulta en el subtítulo.",
            listOf("Instagram", "Google", "GitHub")
        )
    }

    BasicAlertDialog(onDismissRequest = alCerrar) {
        val tieneBorde = GrosorBorde > 0.dp && EstiloBorde != "ninguno"
        Surface(
            shape = FormaTarjeta,
            color = ColorTarjetaAjustes,
            tonalElevation = 6.dp,
            border = if (tieneBorde) BorderStroke(GrosorBorde, ColorBordeActual) else null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = ColorAcento
                    )
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ColorTextoAjustes
                    )
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario,
                    lineHeight = 18.sp
                )

                Spacer(Modifier.height(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaPequena)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Ejemplos:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = ColorAcento
                    )
                    ejemplos.forEach { ej ->
                        Text(
                            text = "• $ej",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                            color = ColorTextoAjustes
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = alCerrar) {
                        Text("Entendido", color = ColorAcento, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
