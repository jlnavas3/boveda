/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data.contactos

/**
 * Enrutador y discriminador unificado para la lectura de archivos de contactos.
 *
 * Determina automáticamente el formato del archivo:
 * - vCard (.vcf): Si el contenido contiene la etiqueta canónica "BEGIN:VCARD".
 * - CSV (.csv): En cualquier otro caso, parsea con el motor RFC 4180 de contactos.
 */
object LectorArchivoContactos {

    fun parsear(contenido: String): List<ContactoDispositivo> {
        val limpio = contenido.trim()
        if (limpio.isBlank()) return emptyList()

        return if (limpio.contains("BEGIN:VCARD", ignoreCase = true)) {
            ParserVcardContacto.parsear(limpio)
        } else {
            ParserCsvContacto.parsear(limpio)
        }
    }
}
