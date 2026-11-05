/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.content.Context
import android.os.Build
import android.service.autofill.Dataset
import android.view.autofill.AutofillValue
import android.widget.inline.InlinePresentationSpec
import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Constructor de Datasets de Android para el autocompletado de direcciones postales y contactos.
 */
object CreadorDatasetDireccionAutofill {

    @Suppress("DEPRECATION")
    fun crear(
        contexto: Context,
        entrada: Entrada,
        campos: CamposDetectadosDireccion,
        inlineSpec: InlinePresentationSpec? = null
    ): Dataset? {
        val datos = entrada.datosContacto ?: return null
        if (!campos.hayAlgo) return null

        val tituloMostrar = if (entrada.titulo.isNotBlank() && entrada.titulo != "Nueva entrada") {
            entrada.titulo
        } else {
            datos.direccion.ifBlank { "Dirección guardada" }
        }
        val subtituloMostrar = datos.direccionFormateada.ifBlank { "Dirección de envío" }

        val vista = AutofillUtiles.presentacion(
            contexto = contexto,
            titulo = tituloMostrar,
            subtitulo = subtituloMostrar
        )

        val inlineTitulo = datos.direccion.ifBlank { entrada.titulo }
        val inlineSubtitulo = listOf(datos.ciudad, datos.codigoPostal).filter { it.isNotBlank() }.joinToString(" ").ifBlank { "Dirección" }

        val inlinePres = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && inlineSpec != null) {
            CreadorInlineSuggestion.crear(
                contexto = contexto,
                spec = inlineSpec,
                titulo = inlineTitulo,
                subtitulo = inlineSubtitulo
            )
        } else null

        val constructor = Dataset.Builder(vista)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && inlinePres != null) {
            constructor.setInlinePresentation(inlinePres)
        }

        var camposAsignados = 0

        fun asignarCampo(id: android.view.autofill.AutofillId?, valor: String) {
            if (id != null && valor.isNotBlank()) {
                val autoVal = AutofillValue.forText(valor)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && inlinePres != null) {
                    constructor.setValue(id, autoVal, vista, inlinePres)
                } else {
                    constructor.setValue(id, autoVal)
                }
                camposAsignados++
            }
        }

        asignarCampo(campos.calle, datos.direccion)
        asignarCampo(campos.ciudad, datos.ciudad)
        asignarCampo(campos.provincia, datos.provincia)
        asignarCampo(campos.codigoPostal, datos.codigoPostal)
        asignarCampo(campos.pais, datos.pais)
        asignarCampo(campos.telefono, entrada.usuario.ifBlank { datos.telefonoSecundario })
        asignarCampo(campos.correo, datos.correo)
        asignarCampo(campos.nombreCompleto, entrada.titulo)

        if (camposAsignados == 0) return null

        return try {
            constructor.build()
        } catch (_: Exception) {
            null
        }
    }
}
