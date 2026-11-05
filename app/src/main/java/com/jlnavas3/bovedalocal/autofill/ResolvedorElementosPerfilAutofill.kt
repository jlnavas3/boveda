/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Perfil
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Resuelve y prioriza tarjetas bancarias, contactos de envío y documentos de identidad
 * asociados al perfil del usuario para el servicio de autocompletado de Android.
 */
object ResolvedorElementosPerfilAutofill {

    fun resolver(
        entradas: List<Entrada>,
        perfiles: List<Perfil>,
        paquete: String,
        dominioWeb: String?,
        mapeoPersonalizado: Map<String, String> = emptyMap()
    ): ResultadoElementosPerfilAutofill {
        val compatibles = AutofillUtiles.entradasCompatibles(entradas, paquete, dominioWeb, mapeoPersonalizado)
        val perfilCoincidente = compatibles.firstNotNullOfOrNull { c ->
            c.perfilId?.let { pid -> perfiles.firstOrNull { it.id == pid } }
        } ?: perfiles.firstOrNull()

        val tarjetas = if (perfilCoincidente != null) {
            val predet = perfilCoincidente.tarjetaPredeterminadaId?.let { id ->
                entradas.firstOrNull { it.id == id && it.tipo == TipoEntrada.TARJETA && it.datosTarjeta != null }
            }
            val asociadas = entradas.filter {
                it.id in perfilCoincidente.tarjetasIds &&
                    it.id != perfilCoincidente.tarjetaPredeterminadaId &&
                    it.tipo == TipoEntrada.TARJETA &&
                    it.datosTarjeta != null
            }
            val combinadas = listOfNotNull(predet) + asociadas
            if (combinadas.isNotEmpty()) combinadas else entradas.filter { it.tipo == TipoEntrada.TARJETA && it.datosTarjeta != null }
        } else {
            entradas.filter { it.tipo == TipoEntrada.TARJETA && it.datosTarjeta != null }
        }

        val contactos = if (perfilCoincidente != null) {
            val predet = perfilCoincidente.contactoPredeterminadoId?.let { id ->
                entradas.firstOrNull { it.id == id && it.tipo == TipoEntrada.CONTACTO && it.datosContacto != null }
            }
            val asociadas = entradas.filter {
                it.id in perfilCoincidente.contactosIds &&
                    it.id != perfilCoincidente.contactoPredeterminadoId &&
                    it.tipo == TipoEntrada.CONTACTO &&
                    it.datosContacto != null
            }
            val combinadas = listOfNotNull(predet) + asociadas
            if (combinadas.isNotEmpty()) combinadas else entradas.filter { it.tipo == TipoEntrada.CONTACTO && it.datosContacto != null }
        } else {
            entradas.filter { it.tipo == TipoEntrada.CONTACTO && it.datosContacto != null }
        }

        val documentos = if (perfilCoincidente != null) {
            val predet = perfilCoincidente.documentoPredeterminadoId?.let { id ->
                entradas.firstOrNull { it.id == id && it.tipo == TipoEntrada.IDENTIDAD && it.datosIdentidad != null }
            }
            val asociadas = entradas.filter {
                it.id in perfilCoincidente.documentosIds &&
                    it.id != perfilCoincidente.documentoPredeterminadoId &&
                    it.tipo == TipoEntrada.IDENTIDAD &&
                    it.datosIdentidad != null
            }
            val combinadas = listOfNotNull(predet) + asociadas
            if (combinadas.isNotEmpty()) combinadas else entradas.filter { it.tipo == TipoEntrada.IDENTIDAD && it.datosIdentidad != null }
        } else {
            entradas.filter { it.tipo == TipoEntrada.IDENTIDAD && it.datosIdentidad != null }
        }

        return ResultadoElementosPerfilAutofill(
            perfil = perfilCoincidente,
            tarjetas = tarjetas,
            contactos = contactos,
            documentos = documentos
        )
    }
}
