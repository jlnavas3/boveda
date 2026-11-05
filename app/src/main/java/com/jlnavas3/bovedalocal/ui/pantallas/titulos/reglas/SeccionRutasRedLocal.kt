/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.titulos.reglas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeccionRutasRedLocal(
    rutasServicios: Map<String, String>,
    alAgregarClick: () -> Unit,
    alEliminarRuta: (String) -> Unit,
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Rutas y Aplicaciones Web Locales",
        icono = Icons.AutoMirrored.Filled.AltRoute,
        colorIcono = ColorAcento,
        idGrupo = "03-LST-RGL-G05",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Identificación de servicios por fragmentos de ruta en redes privadas y localhost (ej. /phpmyadmin → phpMyAdmin, /portal → Portal Web, /nextcloud → Nextcloud).",
                color = TextoSecundario,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rutasServicios.forEach { (ruta, servicio) ->
                    ChipRutaServicio(
                        ruta = ruta,
                        servicio = servicio,
                        alEliminar = alEliminarRuta
                    )
                }

                BotonAgregarChipRegla(
                    descripcion = "Agregar ruta de servicio",
                    alPulsar = alAgregarClick
                )
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaSeccionRutasRedLocal() {
    BovedaTheme {
        SeccionRutasRedLocal(
            rutasServicios = mapOf("phpmyadmin" to "phpMyAdmin", "portal" to "Portal Web"),
            alAgregarClick = {},
            alEliminarRuta = {}
        )
    }
}
