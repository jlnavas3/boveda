/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles

@Composable
fun PantallaLicencia(
    alVolver: () -> Unit,
    mostrarIdsAjustes: Boolean = false
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val scrollState = rememberScrollState()

    val copiarLicencia = {
        Portapapeles.copiar(contexto, "Licencia GPLv3 - Bóveda Local", TextoLicenciaGplv3.TEXTO_COMPLETO)
        haptica.exito()
        Toast.makeText(contexto, "Texto de la licencia copiado", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Licencia de software",
            idEtiqueta = "06-SIS-LIC",
            mostrarId = mostrarIdsAjustes,
            alVolver = alVolver,
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo,
            acciones = {
                BotonIconoCabecera(
                    onClick = copiarLicencia,
                    icono = Icons.Filled.ContentCopy,
                    descripcion = "Copiar licencia",
                    tint = ColorIconosInternos
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaResumenLicencia()

            // Contenedor del texto legal oficial completo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(16.dp)))
                    .background(ColorTarjetaAjustes)
                    .border(1.dp, ColorBordeActual.copy(alpha = 0.5f), RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(16.dp)))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Texto Oficial de la Licencia",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextoPrincipal
                )

                SelectionContainer {
                    Text(
                        text = TextoLicenciaGplv3.TEXTO_COMPLETO,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        ),
                        color = TextoSecundario
                    )
                }
            }

            Button(
                onClick = copiarLicencia,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorAcento
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Copiar licencia completa",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
