/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.quicksettings

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.graphics.drawable.IconCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import com.jlnavas3.bovedalocal.R

object GeneradorRapidoShortcutHelper {

    fun sePuedeAnclarAlEscritorio(context: Context): Boolean {
        return ShortcutManagerCompat.isRequestPinShortcutSupported(context)
    }

    fun anclarAlEscritorio(context: Context): Boolean {
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            Toast.makeText(context, "El lanzador del dispositivo no soporta anclar accesos directos", Toast.LENGTH_SHORT).show()
            return false
        }

        val shortcutInfo = ShortcutInfoCompat.Builder(context, "generador_rapido")
            .setShortLabel(context.getString(R.string.shortcut_generador))
            .setLongLabel(context.getString(R.string.shortcut_generador))
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_generador))
            .setIntent(Intent(context, GeneradorRapidoShortcutActivity::class.java).apply {
                action = Intent.ACTION_VIEW
            })
            .build()

        return ShortcutManagerCompat.requestPinShortcut(context, shortcutInfo, null)
    }
}
