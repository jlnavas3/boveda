/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.avisos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AvisoInterno
import com.jlnavas3.bovedalocal.data.TipoAvisoInterno
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalAvisosInternos(
    avisos: List<AvisoInterno>,
    alCerrar: () -> Unit,
    alEjecutarAviso: (AvisoInterno) -> Unit,
    alIgnorarAviso: (TipoAvisoInterno) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val configuracion = LocalConfiguration.current
    val alturaMaxima = (configuracion.screenHeightDp * 0.60f).dp

    ModalBottomSheet(
        onDismissRequest = alCerrar,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = CurvaturaEsquinas, topEnd = CurvaturaEsquinas),
        containerColor = ColorAjustesFondo,
        scrimColor = Color.Black.copy(alpha = 0.55f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = alturaMaxima)
                .navigationBarsPadding()
        ) {
            CabeceraAvisosInternos(
                totalAvisos = avisos.size,
                alCerrar = alCerrar
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(avisos, key = { it.tipo.name }) { aviso ->
                    TarjetaAvisoInterno(
                        aviso = aviso,
                        alEjecutar = {
                            alCerrar()
                            alEjecutarAviso(aviso)
                        },
                        alIgnorar = {
                            alIgnorarAviso(aviso.tipo)
                        }
                    )
                }
            }
        }
    }
}
