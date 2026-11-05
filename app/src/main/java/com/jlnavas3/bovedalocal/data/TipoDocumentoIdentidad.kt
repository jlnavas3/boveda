/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Catálogo de documentos oficiales de identidad nacional utilizados en países
 * hispanohablantes e internacionales para trámites gubernamentales, tributarios y civiles.
 */
enum class TipoDocumentoIdentidad(val sigla: String, val descripcion: String) {
    CI("CI", "Cédula de Identidad"),
    CC("CC", "Cédula de Ciudadanía"),
    CIP("CIP", "Cédula de Identidad Personal"),
    CIC("CIC", "Cédula de Identidad Civil"),
    DNI("DNI", "Documento Nacional de Identidad"),
    DUI("DUI", "Documento Único de Identidad"),
    DPI("DPI", "Documento Personal de Identificación"),
    CURP("CURP", "Clave Única de Registro de Población"),
    RUT("RUT", "Rol Único Tributario / RUN"),
    RUC("RUC", "Registro Único de Contribuyentes"),
    PASAPORTE("Pasaporte", "Pasaporte"),
    OTRO("Otro", "Otro documento oficial");

    companion object {
        val SIGLAS_PREDETERMINADAS: List<String> = entries.map { it.sigla }

        fun desdeSigla(sigla: String?): TipoDocumentoIdentidad {
            if (sigla.isNullOrBlank()) return CI
            return entries.firstOrNull { it.sigla.equals(sigla.trim(), ignoreCase = true) } ?: OTRO
        }

        fun todasLasSiglas(personalizadas: List<String> = emptyList()): List<String> {
            return (SIGLAS_PREDETERMINADAS + personalizadas).distinct()
        }
    }
}
