/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.ModoFormatoTitulos
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme

@Composable
fun SelectorModoFormatoTitulos(
    modoActual: ModoFormatoTitulos,
    respetarManuales: Boolean,
    alCambiarModo: (ModoFormatoTitulos) -> Unit,
    alCambiarRespetarManuales: (Boolean) -> Unit,
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    var dialogoModo by remember { mutableStateOf<ModoFormatoTitulos?>(null) }

    dialogoModo?.let { modo ->
        DialogoEjemploFormatoTitulo(
            modo = modo,
            alCerrar = { dialogoModo = null }
        )
    }

    ComponenteGrupo(
        etiqueta = null,
        icono = null,
        idGrupo = "03-LST-TIT-G01",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        FilaOpcionFormatoTitulo(
            titulo = "Servicio (usuario)",
            seleccionado = modoActual == ModoFormatoTitulos.EXPLICITO_PARENTESIS,
            alSeleccionar = { alCambiarModo(ModoFormatoTitulos.EXPLICITO_PARENTESIS) },
            alPulsarInfo = { dialogoModo = ModoFormatoTitulos.EXPLICITO_PARENTESIS }
        )
        ComponenteSeparador(sangriaInicio = 16.dp)

        FilaOpcionFormatoTitulo(
            titulo = "Servicio · usuario",
            seleccionado = modoActual == ModoFormatoTitulos.EXPLICITO_SEPARADOR,
            alSeleccionar = { alCambiarModo(ModoFormatoTitulos.EXPLICITO_SEPARADOR) },
            alPulsarInfo = { dialogoModo = ModoFormatoTitulos.EXPLICITO_SEPARADOR }
        )
        ComponenteSeparador(sangriaInicio = 16.dp)

        FilaOpcionFormatoTitulo(
            titulo = "Solo servicio (Minimalista)",
            seleccionado = modoActual == ModoFormatoTitulos.MINIMALISTA,
            alSeleccionar = { alCambiarModo(ModoFormatoTitulos.MINIMALISTA) },
            alPulsarInfo = { dialogoModo = ModoFormatoTitulos.MINIMALISTA }
        )
        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSwitch(
            titulo = "Respetar títulos personalizados",
            subtitulo = "No sobrescribe nombres editados a mano",
            icono = null,
            activo = respetarManuales,
            idFila = "03-LST-TIT-MAN",
            mostrarId = mostrarId,
            alCambiar = alCambiarRespetarManuales
        )
    }
}

@BovedaPreview
@Composable
private fun PreviaSelectorModoFormatoTitulos() {
    BovedaTheme {
        SelectorModoFormatoTitulos(
            modoActual = ModoFormatoTitulos.EXPLICITO_PARENTESIS,
            respetarManuales = true,
            alCambiarModo = {},
            alCambiarRespetarManuales = {}
        )
    }
}
