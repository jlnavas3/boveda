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
 * Constructor de Datasets de Android para el autocompletado de tarjetas bancarias.
 */
object CreadorDatasetTarjetaAutofill {

    @Suppress("DEPRECATION")
    fun crear(
        contexto: Context,
        entrada: Entrada,
        campos: CamposDetectadosTarjeta,
        inlineSpec: InlinePresentationSpec? = null
    ): Dataset? {
        val datos = entrada.datosTarjeta ?: return null
        if (!campos.hayAlgo) return null

        val numLimpio = datos.numero.filter { it.isDigit() }
        val ultimos4 = if (numLimpio.length >= 4) numLimpio.takeLast(4) else numLimpio
        val marcaOIdentificador = datos.marca.ifBlank { datos.tipo }.ifBlank { "Tarjeta" }
        val tituloMostrar = if (entrada.titulo.isNotBlank() && entrada.titulo != "Nueva entrada") {
            "${entrada.titulo} (•••• $ultimos4)"
        } else {
            "$marcaOIdentificador •••• $ultimos4"
        }
        val subtituloMostrar = if (datos.vencimiento.isNotBlank()) {
            "Vence ${datos.vencimiento}"
        } else {
            datos.titular.ifBlank { "Tarjeta bancaria" }
        }

        val vista = AutofillUtiles.presentacion(
            contexto = contexto,
            titulo = tituloMostrar,
            subtitulo = subtituloMostrar
        )

        val inlinePres = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && inlineSpec != null) {
            CreadorInlineSuggestion.crear(
                contexto = contexto,
                spec = inlineSpec,
                titulo = "•••• $ultimos4",
                subtitulo = marcaOIdentificador
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

        // Número de tarjeta
        asignarCampo(campos.numeroTarjeta, datos.numero)

        // CVV
        asignarCampo(campos.cvv, datos.cvv)

        // Titular
        asignarCampo(campos.titular, datos.titular)

        // Vencimiento completo
        asignarCampo(campos.vencimiento, datos.vencimiento)

        // Vencimiento separado (mes y año)
        if (datos.vencimiento.contains("/")) {
            val partes = datos.vencimiento.split("/")
            val mes = partes.getOrNull(0)?.trim().orEmpty()
            val ano = partes.getOrNull(1)?.trim().orEmpty()
            asignarCampo(campos.vencimientoMes, mes)
            asignarCampo(campos.vencimientoAno, ano)
        }

        if (camposAsignados == 0) return null

        return try {
            constructor.build()
        } catch (_: Exception) {
            null
        }
    }
}
