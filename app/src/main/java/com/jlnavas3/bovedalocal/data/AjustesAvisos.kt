/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Agrupa la configuración del Centro de Asistencia y Avisos Internos (In-App).
 * Encapsulado para mantener los slots del constructor de [AjustesApp] dentro de los límites del JVM.
 */
data class AjustesAvisos(
    val mostrarCampanaCabecera: Boolean = AjustesDefaults.Avisos.MOSTRAR_CAMPANA_CABECERA,
    val mostrarAvisosPantallaBloqueo: Boolean = AjustesDefaults.Avisos.MOSTRAR_AVISOS_PANTALLA_BLOQUEO,
    val sugerirNormalizarTrasImportar: Boolean = AjustesDefaults.Avisos.SUGERIR_NORMALIZAR_TRAS_IMPORTAR,
    val avisarDuplicados: Boolean = AjustesDefaults.Avisos.AVISAR_DUPLICADOS,
    val avisarTitulosSinNormalizar: Boolean = AjustesDefaults.Avisos.AVISAR_TITULOS_SIN_NORMALIZAR,
    val avisarSaludContrasenas: Boolean = AjustesDefaults.Avisos.AVISAR_SALUD_CONTRASENAS,
    val avisarRespaldoPendiente: Boolean = AjustesDefaults.Avisos.AVISAR_RESPALDO_PENDIENTE,
    val fechaCreacionBovedaLocal: Long = 0L,
    val avisosIgnorados: Set<String> = emptySet(),
    val conteoAvisosEnBloqueo: Int = 0
)
