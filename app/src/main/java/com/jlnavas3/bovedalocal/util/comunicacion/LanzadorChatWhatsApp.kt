/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util.comunicacion

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.jlnavas3.bovedalocal.data.contactos.FormateadorTelefonoInternacional
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Microlanzador desacoplado para iniciar chats o enviar archivos efímeros a contactos
 * directos en WhatsApp sin necesidad de guardarlos en la agenda pública de Android.
 */
object LanzadorChatWhatsApp {

    private val PAQUETES_WHATSAPP = listOf("com.whatsapp", "com.whatsapp.w4b")

    /**
     * Detecta el paquete de WhatsApp disponible en el dispositivo (estándar o Business).
     */
    fun obtenerPaqueteWhatsApp(context: Context): String? {
        val pm = context.packageManager
        for (pkg in PAQUETES_WHATSAPP) {
            try {
                pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES)
                return pkg
            } catch (_: PackageManager.NameNotFoundException) {
                // Siguiente paquete
            }
        }
        return null
    }

    /**
     * Abre la conversación directa de WhatsApp para el número especificado (formato internacional sin '+' ni ceros).
     */
    fun abrirChat(context: Context, numero: String, texto: String? = null): Boolean {
        val numLimpio = FormateadorTelefonoInternacional.limpiarParaWhatsApp(numero)
        if (numLimpio.isBlank()) return false

        val paquete = obtenerPaqueteWhatsApp(context)
        val textoCodificado = if (!texto.isNullOrBlank()) {
            "?text=" + URLEncoder.encode(texto, StandardCharsets.UTF_8.name())
        } else {
            ""
        }

        val url = "https://wa.me/$numLimpio$textoCodificado"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            if (paquete != null) {
                setPackage(paquete)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        return try {
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            // Reintento genérico sin restringir paquete si falló
            try {
                intent.setPackage(null)
                context.startActivity(intent)
                true
            } catch (_: Exception) {
                false
            }
        }
    }

    /**
     * Envía una imagen efímera (generada en caché segura) directamente al chat de WhatsApp
     * inyectando el número como JID ("{numero}@s.whatsapp.net") y otorgando permisos de lectura sobre la URI.
     */
    fun enviarImagenEfimera(context: Context, numero: String, imagenUri: Uri): Boolean {
        val numLimpio = FormateadorTelefonoInternacional.limpiarParaWhatsApp(numero)
        if (numLimpio.isBlank()) return false

        val paquete = obtenerPaqueteWhatsApp(context)
        val jid = "$numLimpio@s.whatsapp.net"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imagenUri)
            putExtra("jid", jid)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (paquete != null) {
                setPackage(paquete)
            }
        }

        return try {
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            // Si falla con jid directo, intentar compartir vía ACTION_SEND genérico con chooser
            try {
                val chooser = Intent.createChooser(intent, "Enviar imagen confidencial").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(chooser)
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
