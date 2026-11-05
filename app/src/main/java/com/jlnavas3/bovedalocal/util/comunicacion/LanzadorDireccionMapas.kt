/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util.comunicacion

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Microlanzador desacoplado para abrir direcciones físicas en aplicaciones de mapas o navegación.
 */
object LanzadorDireccionMapas {

    fun abrirMapa(context: Context, direccion: String): Boolean {
        if (direccion.isBlank()) return false

        val direccionCodificada = URLEncoder.encode(direccion, StandardCharsets.UTF_8.name())
        val uriGeo = Uri.parse("geo:0,0?q=$direccionCodificada")

        val intent = Intent(Intent.ACTION_VIEW, uriGeo).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        return try {
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            // Si no hay app de mapas nativa que soporte geo:, fallback a https://maps.google.com/?q=
            try {
                val fallbackIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://maps.google.com/?q=$direccionCodificada")
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
