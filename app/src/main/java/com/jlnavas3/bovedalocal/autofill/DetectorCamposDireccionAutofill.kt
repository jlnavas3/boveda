/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.app.assist.AssistStructure
import android.view.View

/**
 * Detector especializado en identificar campos de direcciones postales, envíos y contactos en la jerarquía de vistas de Android.
 */
object DetectorCamposDireccionAutofill {

    fun esCalle(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "street-address" || auto == "address-line1" ||
            pistasSistema.any { it.contains("postaladdressstreetaddress") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p == "address" || p == "adr" || p.contains("street") || p.contains("calle") ||
                    p.contains("direccion") || p.contains("dirección") || p.contains("address1") ||
                    p.contains("address-line1") || p.contains("domicilio")
            }
    }

    fun esCodigoPostal(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "postal-code" ||
            pistasSistema.any { it.contains("postalcode") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p == "zip" || p == "zipcode" || p.contains("postal-code") || p.contains("postalcode") ||
                    p.contains("codigopostal") || p.contains("codigo-postal") || p.contains("codigo_postal")
            }
    }

    fun esCiudad(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "address-level2" ||
            pistasSistema.any { it.contains("postaladdresslocality") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p == "city" || p.contains("ciudad") || p.contains("localidad") ||
                    p.contains("municipio") || p.contains("town")
            }
    }

    fun esProvincia(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "address-level1" ||
            pistasSistema.any { it.contains("postaladdressregion") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p == "state" || p.contains("provincia") || p.contains("departamento") ||
                    p.contains("estado") || p.contains("region") || p.contains("región")
            }
    }

    fun esPais(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "country" || auto == "country-name" ||
            pistasSistema.any { it.contains("postaladdresscountry") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p == "country" || p.contains("pais") || p.contains("país") || p.contains("nacion")
            }
    }

    fun esTelefono(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "tel" || auto == "tel-national" ||
            pistasSistema.any { it.contains("phone") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p == "phone" || p == "tel" || p.contains("telefono") || p.contains("teléfono") ||
                    p.contains("celular") || p.contains("mobile")
            }
    }

    fun esCorreo(pistasSistema: List<String>, textoPistas: List<String>, htmlAutocomplete: String?): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "email" ||
            pistasSistema.any { it.contains("emailaddress") } ||
            textoPistas.any { p -> p == "email" || p.contains("correo") || p.contains("mail") }
    }

    fun esNombreCompleto(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        pistasPersonalizadas: List<String> = emptyList()
    ): Boolean {
        val auto = htmlAutocomplete?.lowercase().orEmpty()
        return auto == "name" || auto == "given-name" ||
            pistasSistema.any { it.contains("name") && !it.contains("user") } ||
            textoPistas.any { p ->
                pistasPersonalizadas.any { pista -> p.contains(pista) } ||
                    p == "firstname" || p == "fname" || p == "fullname" || p.contains("destinatario") ||
                    (p.contains("nombre") && !p.contains("usuario"))
            }
    }

    fun inspeccionarNodo(
        nodo: AssistStructure.ViewNode,
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String?,
        acumulador: AcumuladorCamposDireccion,
        pistasCalle: List<String> = emptyList(),
        pistasCp: List<String> = emptyList(),
        pistasCiudad: List<String> = emptyList(),
        pistasProvincia: List<String> = emptyList(),
        pistasPais: List<String> = emptyList(),
        pistasTelefono: List<String> = emptyList(),
        pistasNombre: List<String> = emptyList()
    ) {
        val id = nodo.autofillId ?: return
        val tipoValido = nodo.autofillType == View.AUTOFILL_TYPE_TEXT || nodo.autofillType == View.AUTOFILL_TYPE_NONE
        if (!tipoValido) return

        if (esCalle(pistasSistema, textoPistas, htmlAutocomplete, pistasCalle) && acumulador.calle == null) {
            acumulador.calle = id
            return
        }

        if (esCodigoPostal(pistasSistema, textoPistas, htmlAutocomplete, pistasCp) && acumulador.codigoPostal == null) {
            acumulador.codigoPostal = id
            return
        }

        if (esCiudad(pistasSistema, textoPistas, htmlAutocomplete, pistasCiudad) && acumulador.ciudad == null) {
            acumulador.ciudad = id
            return
        }

        if (esProvincia(pistasSistema, textoPistas, htmlAutocomplete, pistasProvincia) && acumulador.provincia == null) {
            acumulador.provincia = id
            return
        }

        if (esPais(pistasSistema, textoPistas, htmlAutocomplete, pistasPais) && acumulador.pais == null) {
            acumulador.pais = id
            return
        }

        if (esTelefono(pistasSistema, textoPistas, htmlAutocomplete, pistasTelefono) && acumulador.telefono == null) {
            acumulador.telefono = id
            return
        }

        if (esCorreo(pistasSistema, textoPistas, htmlAutocomplete) && acumulador.correo == null) {
            acumulador.correo = id
            return
        }

        if (esNombreCompleto(pistasSistema, textoPistas, htmlAutocomplete, pistasNombre) && acumulador.nombreCompleto == null) {
            acumulador.nombreCompleto = id
            return
        }
    }
}
