/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.app.assist.AssistStructure
import android.view.View

/**
 * Detector especializado en identificar campos de pago y tarjetas de crédito en la jerarquía de vistas de Android.
 */
object DetectorCamposTarjetaAutofill {

    fun esNumeroTarjeta(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "cc-number" ||
            pistasSistema.any { it.contains("creditcardnumber") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p.contains("cardnumber") || p.contains("card-number") || p.contains("card_number") ||
                    p.contains("cc-num") || p.contains("ccnum") || p.contains("cc_num") ||
                    p.contains("creditcard") || p.contains("credit-card") || p.contains("numerotarjeta")
            }
    }

    fun esCvv(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "cc-csc" ||
            pistasSistema.any { it.contains("creditcardsecuritycode") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) || p == pista } ||
                    p == "cvv" || p == "cvc" || p == "csc" ||
                    p.contains("cvv") || p.contains("cvc") || p.contains("csc") ||
                    p.contains("securitycode") || p.contains("security-code") || p.contains("security_code") ||
                    p.contains("codigoseguridad") || p.contains("codigo-seguridad")
            }
    }

    fun esExpiracionMes(pistasSistema: List<String>, textoPistas: List<String>, htmlAutocomplete: String?): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "cc-exp-month" ||
            pistasSistema.any { it.contains("creditcardexpirationmonth") } ||
            textoPistas.any { p ->
                p.contains("expmonth") || p.contains("exp-month") || p.contains("exp_month") ||
                    p.contains("card-expiry-month") || p.contains("expiry-month") || p.contains("mes-exp")
            }
    }

    fun esExpiracionAno(pistasSistema: List<String>, textoPistas: List<String>, htmlAutocomplete: String?): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "cc-exp-year" ||
            pistasSistema.any { it.contains("creditcardexpirationyear") } ||
            textoPistas.any { p ->
                p.contains("expyear") || p.contains("exp-year") || p.contains("exp_year") ||
                p.contains("card-expiry-year") || p.contains("expiry-year") || p.contains("ano-exp") || p.contains("anio-exp")
            }
    }

    fun esExpiracion(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "cc-exp" ||
            pistasSistema.any { it.contains("creditcardexpirationdate") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p.contains("expdate") || p.contains("exp-date") || p.contains("exp_date") ||
                    p.contains("ccexp") || p.contains("cc-exp") || p.contains("card-expiry") ||
                    p.contains("card_expiry") || p.contains("vencimiento") || (p.contains("expir") && !p.contains("pass"))
            }
    }

    fun esTitular(
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "cc-name" ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p.contains("cardname") || p.contains("card-name") || p.contains("card_name") ||
                    p.contains("cname") || p.contains("ccname") || p.contains("cardholder") ||
                    p.contains("titulartarjeta") || p.contains("titular-tarjeta")
            }
    }

    fun inspeccionarNodo(
        nodo: AssistStructure.ViewNode,
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        acumulador: AcumuladorCamposTarjeta,
        pistasNumero: List<String> = emptyList(),
        pistasCvv: List<String> = emptyList(),
        pistasExpiracion: List<String> = emptyList(),
        pistasTitular: List<String> = emptyList()
    ) {
        val id = nodo.autofillId ?: return
        val tipoValido = nodo.autofillType == View.AUTOFILL_TYPE_TEXT || nodo.autofillType == View.AUTOFILL_TYPE_NONE
        if (!tipoValido) return

        if (esNumeroTarjeta(pistasSistema, textoPistas, htmlAutocomplete, pistasNumero) && acumulador.numeroTarjeta == null) {
            acumulador.numeroTarjeta = id
            return
        }

        if (esCvv(pistasSistema, textoPistas, htmlAutocomplete, pistasCvv) && acumulador.cvv == null) {
            acumulador.cvv = id
            return
        }

        if (esExpiracionMes(pistasSistema, textoPistas, htmlAutocomplete) && acumulador.vencimientoMes == null) {
            acumulador.vencimientoMes = id
            return
        }

        if (esExpiracionAno(pistasSistema, textoPistas, htmlAutocomplete) && acumulador.vencimientoAno == null) {
            acumulador.vencimientoAno = id
            return
        }

        if (esExpiracion(pistasSistema, textoPistas, htmlAutocomplete, pistasExpiracion) && acumulador.vencimiento == null) {
            acumulador.vencimiento = id
            return
        }

        if (esTitular(textoPistas, htmlAutocomplete, pistasTitular) && acumulador.titular == null) {
            acumulador.titular = id
            return
        }
    }
}
