/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoConfirmacionIconoLauncher(
    variante: VarianteIconoLauncher,
    alConfirmar: (VarianteIconoLauncher) -> Unit,
    alDescartar: () -> Unit
) {
    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Cambiar ícono del launcher",
        descripcion = "Variante: ${variante.etiqueta}",
        mostrarBotonCerrar = false
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(4.dp))
            MiniaturaIconoLauncher(
                variante = variante,
                seleccionado = true,
                tamano = 64.dp,
                mostrarBadgeCheck = false
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = variante.etiqueta,
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Android cerrará la aplicación por un instante para que el launcher actualice su ícono. Ya quedará guardado antes de cerrarse.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonBoveda(
                    texto = "Cancelar",
                    variante = VarianteBoton.SECUNDARIO,
                    modifier = Modifier.weight(1f),
                    alPulsar = alDescartar
                )
                BotonBoveda(
                    texto = "Cambiar y cerrar",
                    variante = VarianteBoton.PRIMARIO,
                    modifier = Modifier.weight(1f),
                    alPulsar = { alConfirmar(variante) }
                )
            }
        }
    }
}
