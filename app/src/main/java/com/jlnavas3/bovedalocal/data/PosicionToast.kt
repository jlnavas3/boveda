/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Define la posición y el comportamiento de visualización de los avisos emergentes (Toast)
 * en la interfaz de usuario de la aplicación.
 */
enum class PosicionToast(
    val clave: String,
    val etiqueta: String,
    val descripcion: String,
    val esPredeterminada: Boolean = false
) {
    INFERIOR(
        clave = "inferior",
        etiqueta = "Inferior (Predeterminado)",
        descripcion = "Aparece en la parte inferior, suspendido sobre la barra fija de acciones.",
        esPredeterminada = true
    ),
    SUPERIOR(
        clave = "superior",
        etiqueta = "Superior",
        descripcion = "Aparece debajo de la cabecera en la parte superior, dejando toda la zona inferior totalmente despejada."
    );

    companion object {
        fun desde(clave: String?): PosicionToast = when (clave?.lowercase()) {
            "superior" -> SUPERIOR
            else -> INFERIOR
        }
    }
}
