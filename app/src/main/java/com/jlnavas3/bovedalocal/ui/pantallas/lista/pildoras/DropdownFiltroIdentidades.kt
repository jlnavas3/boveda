/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Menú desplegable contextual anclado al chip de filtro de Identidades.
 */
@Composable
fun DropdownFiltroIdentidades(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    identidades: List<Identidad>,
    identidadSeleccionadaId: String?,
    conteoPorIdentidad: Map<String, Int>,
    conteoSinIdentidad: Int,
    totalEntradas: Int,
    alSeleccionarIdentidad: (String?) -> Unit,
    alGestionarIdentidades: () -> Unit,
    modifier: Modifier = Modifier
) {
    MenuDesplegableBoveda(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.widthIn(min = 210.dp, max = 280.dp)
    ) {
        Column(
            modifier = Modifier
                .heightIn(max = 300.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Todas
            val esTodas = identidadSeleccionadaId == null
            val textoTodas = if (totalEntradas > 0) "Todas ($totalEntradas)" else "Todas"
            ElementoMenuCompacto(
                texto = textoTodas,
                icono = Icons.Filled.Person,
                colorIcono = if (esTodas) ColorAcento else ColorIconosInternos,
                colorTexto = if (esTodas) ColorAcento else TextoPrincipal,
                iconoFinal = if (esTodas) Icons.Filled.Check else null,
                colorIconoFinal = ColorAcento,
                onClick = {
                    alSeleccionarIdentidad(null)
                    onDismissRequest()
                }
            )

            // 2. Sin identidad (si aplica)
            if (conteoSinIdentidad > 0) {
                val esSinIden = identidadSeleccionadaId == "__SIN_IDENTIDAD__"
                ElementoMenuCompacto(
                    texto = "Sin asignar ($conteoSinIdentidad)",
                    icono = Icons.Filled.PersonOutline,
                    colorIcono = if (esSinIden) ColorAcento else ColorIconosInternos,
                    colorTexto = if (esSinIden) ColorAcento else TextoPrincipal,
                    iconoFinal = if (esSinIden) Icons.Filled.Check else null,
                    colorIconoFinal = ColorAcento,
                    onClick = {
                        alSeleccionarIdentidad("__SIN_IDENTIDAD__")
                        onDismissRequest()
                    }
                )
            }

            // 3. Lista de Identidades
            identidades.forEach { iden ->
                val esActiva = identidadSeleccionadaId == iden.id
                val conteo = conteoPorIdentidad[iden.id] ?: 0
                val textoIden = if (conteo > 0) "${iden.nombre} ($conteo)" else iden.nombre

                ElementoMenuCompacto(
                    texto = textoIden,
                    icono = Icons.Filled.Person,
                    colorIcono = if (esActiva) ColorAcento else ColorIconosInternos,
                    colorTexto = if (esActiva) ColorAcento else TextoPrincipal,
                    iconoFinal = if (esActiva) Icons.Filled.Check else null,
                    colorIconoFinal = ColorAcento,
                    onClick = {
                        alSeleccionarIdentidad(iden.id)
                        onDismissRequest()
                    }
                )
            }

            // 4. Separador y Gestión
            SeparadorOpcionMenu()
            ElementoMenuCompacto(
                texto = "Gestionar identidades...",
                icono = Icons.Filled.Tune,
                colorIcono = ColorIconosInternos,
                colorTexto = TextoSecundario,
                onClick = {
                    onDismissRequest()
                    alGestionarIdentidades()
                }
            )
        }
    }
}
