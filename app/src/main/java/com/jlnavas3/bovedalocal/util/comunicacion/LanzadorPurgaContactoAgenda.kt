/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util.comunicacion

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract

/**
 * Microlanzador desacoplado para facilitar la purga o eliminación de un contacto
 * de la agenda pública del sistema, abriendo su ficha oficial en el gestor de contactos
 * sin requerir el permiso invasivo WRITE_CONTACTS.
 */
object LanzadorPurgaContactoAgenda {

    fun abrirFichaParaEliminar(context: Context, contactId: String): Boolean {
        if (contactId.isBlank()) return false

        return try {
            val idLong = contactId.toLongOrNull() ?: return false
            val uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idLong)
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}
