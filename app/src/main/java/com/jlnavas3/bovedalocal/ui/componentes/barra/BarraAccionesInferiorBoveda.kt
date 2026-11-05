/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.barra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

import androidx.compose.foundation.layout.RowScope

/**
 * Contenedor genérico para la barra inferior fija de acciones (Bottom Action Bar).
 * Permanece anclada en la base de la pantalla, respeta la barra de navegación del sistema,
 * bordes y colores temáticos, y permite contenido personalizado en su interior.
 */
@Composable
fun BarraAccionesInferiorBoveda(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = ColorAjustesFondo,
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(
                        width = GrosorBorde,
                        color = ColorBordeActual.copy(alpha = 0.5f)
                    )
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

/**
 * Sobrecarga de conveniencia que distribuye uniformemente una lista de [ItemAccionBarraInferior].
 */
@Composable
fun BarraAccionesInferiorBoveda(
    items: List<ItemAccionBarraInferior>,
    modifier: Modifier = Modifier
) {
    BarraAccionesInferiorBoveda(modifier = modifier) {
        items.forEach { item ->
            BotonAccionBarraInferior(item = item)
        }
    }
}
