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
