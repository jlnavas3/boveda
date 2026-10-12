/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.graphics.Bitmap

/**
 * Representa los datos sugeridos detectados para la creación automática de una nueva entrada.
 */
data class DatosSugeridosAutofill(
    val titulo: String,
    val url: String,
    val iconoBitmap: Bitmap? = null
)
