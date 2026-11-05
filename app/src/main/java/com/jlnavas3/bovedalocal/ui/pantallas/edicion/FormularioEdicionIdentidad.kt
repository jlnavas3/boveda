/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad
import com.jlnavas3.bovedalocal.data.TipoDocumentoIdentidad
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import java.util.Calendar

/**
 * Microcomponente desacoplado para edición directa y fuertemente tipada de documentos de identidad.
 */
@Composable
fun FormularioEdicionIdentidad(
    datos: DatosDocumentoIdentidad,
    alCambiar: (DatosDocumentoIdentidad) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    val contexto = LocalContext.current
    val ocultarPorDefecto = ajustes.seguridadVisualActiva && ajustes.ocultarUsuario
    var mostrarDoc by remember(ajustes.seguridadVisualActiva, ajustes.ocultarUsuario) { mutableStateOf(!ocultarPorDefecto) }

    val tipoDoc = datos.tipoDocumento.ifBlank { "CI" }

    fun abrirSelectorFecha(esExpedicion: Boolean, valorActual: String) {
        val parseado = FormateadorCampos.parsearFecha(valorActual, ajustes.formatoFecha)
        val cal = Calendar.getInstance()
        val y = parseado?.first ?: cal.get(Calendar.YEAR)
        val m = parseado?.second ?: cal.get(Calendar.MONTH)
        val d = parseado?.third ?: cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(contexto, { _, anio, mes, dia ->
            val fechaFormateada = FormateadorCampos.formatearFecha(anio, mes, dia, ajustes.formatoFecha)
            if (esExpedicion) {
                alCambiar(datos.copy(expedicion = fechaFormateada))
            } else {
                alCambiar(datos.copy(caducidad = fechaFormateada))
            }
        }, y, m, d).show()
    }

    val docEnum = TipoDocumentoIdentidad.desdeSigla(tipoDoc)
    val esSiglaEstandar = docEnum != TipoDocumentoIdentidad.OTRO
    val esPersonalizado = ajustes.tiposDocumentoPersonalizados.any { it.equals(tipoDoc.trim(), ignoreCase = true) }
    val esOtro = !esSiglaEstandar && !esPersonalizado

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SelectorTipoDocumentoDesplegable(
            tipoSeleccionado = tipoDoc,
            alSeleccionarTipo = { nuevoTipo ->
                alCambiar(datos.copy(tipoDocumento = nuevoTipo))
            },
            tiposPersonalizados = ajustes.tiposDocumentoPersonalizados
        )

        if (esOtro) {
            CampoBoveda(
                valor = if (tipoDoc.equals("Otro", ignoreCase = true)) "" else tipoDoc,
                etiqueta = "Especificar tipo de documento (ej. Licencia de conducir)",
                alCambiar = { nuevoValor ->
                    alCambiar(datos.copy(tipoDocumento = nuevoValor.ifBlank { "Otro" }))
                },
                colorBordeIzquierdo = ColorDatosWeb
            )
        }

        CampoBoveda(
            valor = datos.numero,
            etiqueta = "Número de documento",
            alCambiar = {
                alCambiar(datos.copy(numero = it))
            },
            esContrasena = true,
            mostrarContrasena = mostrarDoc,
            alAlternarMostrarContrasena = { mostrarDoc = !mostrarDoc },
            monoespaciada = true,
            colorBordeIzquierdo = ColorAcento
        )

        CampoBoveda(
            valor = datos.nombreCompleto,
            etiqueta = "Nombre completo del titular",
            alCambiar = {
                alCambiar(datos.copy(nombreCompleto = it))
            },
            colorBordeIzquierdo = ColorDatosUsuario
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = datos.expedicion,
                etiqueta = "Expedición (${ajustes.formatoFecha})",
                alCambiar = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { abrirSelectorFecha(esExpedicion = true, valorActual = datos.expedicion) }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Elegir fecha",
                            tint = ColorIconosInternos
                        )
                    }
                },
                alPulsar = { abrirSelectorFecha(esExpedicion = true, valorActual = datos.expedicion) },
                modifier = Modifier.weight(1f),
                colorBordeIzquierdo = ColorDatos2FA
            )

            CampoBoveda(
                valor = datos.caducidad,
                etiqueta = "Caducidad (${ajustes.formatoFecha})",
                alCambiar = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { abrirSelectorFecha(esExpedicion = false, valorActual = datos.caducidad) }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Elegir fecha",
                            tint = ColorIconosInternos
                        )
                    }
                },
                alPulsar = { abrirSelectorFecha(esExpedicion = false, valorActual = datos.caducidad) },
                modifier = Modifier.weight(1f),
                colorBordeIzquierdo = ColorDatos2FA
            )
        }

        CampoBoveda(
            valor = datos.paisEmisor,
            etiqueta = "País emisor",
            alCambiar = {
                alCambiar(datos.copy(paisEmisor = it))
            },
            colorBordeIzquierdo = ColorDatosWeb
        )
    }
}
