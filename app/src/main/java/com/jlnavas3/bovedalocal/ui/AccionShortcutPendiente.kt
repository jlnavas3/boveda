package com.jlnavas3.bovedalocal.ui

/**
 * Representa una acción de acceso directo o autocompletado pendiente por ejecutar tras desbloquear la bóveda.
 */
data class AccionShortcutPendiente(
    val accion: String,
    val tituloInicial: String = "",
    val urlInicial: String = ""
)
