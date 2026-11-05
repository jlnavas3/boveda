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
import com.jlnavas3.bovedalocal.data.Perfil

/**
 * Constructor de Datasets de Android para el autocompletado de documentos oficiales de identidad.
 */
object CreadorDatasetDocumentoAutofill {

    @Suppress("DEPRECATION")
    fun crear(
        contexto: Context,
        entrada: Entrada,
        campos: CamposDetectadosDocumento,
        inlineSpec: InlinePresentationSpec? = null
    ): Dataset? {
        val numero = entrada.datosIdentidad?.numero?.ifBlank { null } ?: entrada.usuario.ifBlank { null } ?: return null
        val tipo = entrada.datosIdentidad?.tipoDocumento?.ifBlank { "Documento" } ?: "Documento"
        val nombre = entrada.datosIdentidad?.nombreCompleto?.ifBlank { entrada.titulo } ?: entrada.titulo

        return construirDataset(
            contexto = contexto,
            numero = numero,
            tipo = tipo,
            nombre = nombre,
            campos = campos,
            inlineSpec = inlineSpec
        )
    }

    fun crearDesdePerfil(
        contexto: Context,
        perfil: Perfil,
        campos: CamposDetectadosDocumento,
        inlineSpec: InlinePresentationSpec? = null
    ): Dataset? {
        val numero = perfil.numeroDocumento?.ifBlank { null } ?: return null
        val tipo = perfil.tipoDocumento?.ifBlank { "Documento" } ?: "Documento"
        val nombre = perfil.nombre

        return construirDataset(
            contexto = contexto,
            numero = numero,
            tipo = tipo,
            nombre = nombre,
            campos = campos,
            inlineSpec = inlineSpec
        )
    }

    @Suppress("DEPRECATION")
    private fun construirDataset(
        contexto: Context,
        numero: String,
        tipo: String,
        nombre: String,
        campos: CamposDetectadosDocumento,
        inlineSpec: InlinePresentationSpec?
    ): Dataset? {
        if (!campos.hayAlgo) return null

        val tituloMostrar = "$tipo: $numero"
        val subtituloMostrar = nombre.ifBlank { "Documento de identidad" }

        val vista = AutofillUtiles.presentacion(
            contexto = contexto,
            titulo = tituloMostrar,
            subtitulo = subtituloMostrar
        )

        val inlinePres = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && inlineSpec != null) {
            CreadorInlineSuggestion.crear(
                contexto = contexto,
                spec = inlineSpec,
                titulo = numero,
                subtitulo = tipo
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

        asignarCampo(campos.numero, numero)
        asignarCampo(campos.nombreCompleto, nombre)

        if (camposAsignados == 0) return null

        return try {
            constructor.build()
        } catch (_: Exception) {
            null
        }
    }
}
