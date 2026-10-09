package com.jlnavas3.bovedalocal.data.contactos

import android.content.Context
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Microcomponente para consultar y extraer los contactos almacenados localmente
 * en el dispositivo Android mediante ContentResolver y ContactsContract.
 */
object LectorContactosAndroid {

    suspend fun leerContactos(context: Context): List<ContactoDispositivo> = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        val contactosMap = linkedMapOf<String, MutableContacto>()

        // 1. Obtener nombres y números de teléfono
        val proyeccionTelefonos = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        try {
            resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                proyeccionTelefonos,
                null,
                null,
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
            )?.use { cursor ->
                val idIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val nombreIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (cursor.moveToNext()) {
                    val id = if (idIdx >= 0) cursor.getString(idIdx) ?: "" else ""
                    val nombre = if (nombreIdx >= 0) cursor.getString(nombreIdx) ?: "" else ""
                    val numero = if (numIdx >= 0) cursor.getString(numIdx) ?: "" else ""

                    if (id.isNotBlank() && nombre.isNotBlank()) {
                        val contacto = contactosMap.getOrPut(id) {
                            MutableContacto(id = id, nombre = nombre.trim())
                        }
                        val numLimpio = numero.trim()
                        if (numLimpio.isNotBlank() && !contacto.telefonos.contains(numLimpio)) {
                            contacto.telefonos.add(numLimpio)
                        }
                    }
                }
            }
        } catch (_: SecurityException) {
            return@withContext emptyList()
        } catch (_: Exception) {
            // Manejo tolerante a fallos
        }

        // 2. Obtener correos electrónicos asociados
        val proyeccionEmails = arrayOf(
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.CommonDataKinds.Email.ADDRESS
        )

        try {
            resolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                proyeccionEmails,
                null,
                null,
                null
            )?.use { cursor ->
                val idIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
                val emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)

                while (cursor.moveToNext()) {
                    val id = if (idIdx >= 0) cursor.getString(idIdx) ?: "" else ""
                    val email = if (emailIdx >= 0) cursor.getString(emailIdx) ?: "" else ""

                    val contacto = contactosMap[id]
                    if (contacto != null && email.isNotBlank()) {
                        val emailLimpio = email.trim()
                        if (!contacto.correos.contains(emailLimpio)) {
                            contacto.correos.add(emailLimpio)
                        }
                    }
                }
            }
        } catch (_: Exception) {
            // Tolerar omisiones de emails
        }

        contactosMap.values.map {
            ContactoDispositivo(
                id = it.id,
                nombre = it.nombre,
                telefonos = it.telefonos.toList(),
                correos = it.correos.toList(),
                organizacion = it.organizacion,
                cargo = it.cargo,
                notas = it.notas
            )
        }.sortedBy { it.nombre.lowercase() }
    }

    private class MutableContacto(
        val id: String,
        val nombre: String,
        val telefonos: MutableList<String> = mutableListOf(),
        val correos: MutableList<String> = mutableListOf(),
        var organizacion: String? = null,
        var cargo: String? = null,
        var notas: String? = null
    )
}
