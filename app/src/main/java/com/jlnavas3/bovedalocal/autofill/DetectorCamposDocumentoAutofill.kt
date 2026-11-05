/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.app.assist.AssistStructure
import android.view.View

/**
 * Detector especializado en identificar campos de documentos de identidad oficiales (Cédula, DNI, Pasaporte, RUC).
 */
object DetectorCamposDocumentoAutofill {

    fun esDocumentoIdentidad(
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        if (auto.contains("national-id") || auto.contains("identification")) return true
        val pistasAComprobar = pistasPersonalizadas.ifEmpty {
            com.jlnavas3.bovedalocal.data.AjustesDefaults.Autocompletado.PISTAS_DOCUMENTO
        }
        return textoPistas.any { p ->
            pistasAComprobar.any { docPista ->
                p == docPista || p.contains(docPista)
            }
        }
    }

    fun inspeccionarNodo(
        nodo: AssistStructure.ViewNode,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        acumulador: AcumuladorCamposDocumento,
        pistasPersonalizadas: List<String> = emptyList()
    ) {
        val id = nodo.autofillId ?: return
        val tipoValido = nodo.autofillType == View.AUTOFILL_TYPE_TEXT || nodo.autofillType == View.AUTOFILL_TYPE_NONE
        if (!tipoValido) return

        if (esDocumentoIdentidad(textoPistas, htmlAutocomplete, pistasPersonalizadas) && acumulador.numero == null) {
            acumulador.numero = id
        }
    }
}
