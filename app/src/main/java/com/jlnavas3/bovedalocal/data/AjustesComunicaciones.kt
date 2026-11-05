/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable

/**
 * Agrupa la configuración de llamadas telefónicas, WhatsApp directo y mensajes confidenciales.
 * Encapsulado para mantener los slots del constructor de [AjustesApp] dentro de los límites del JVM.
 */
@Immutable
data class AjustesComunicaciones(
    val prefijoNumeroOculto: String = AjustesDefaults.Comunicaciones.PREFIJO_NUMERO_OCULTO,
    val habilitarLlamadaOculta: Boolean = AjustesDefaults.Comunicaciones.HABILITAR_LLAMADA_OCULTA,
    val habilitarWhatsappDirecto: Boolean = AjustesDefaults.Comunicaciones.HABILITAR_WHATSAPP_DIRECTO,
    val paisPredeterminadoTelefono: String = AjustesDefaults.Comunicaciones.PAIS_PREDETERMINADO_TELEFONO,
    val modoEnvioMensajeSeguro: String = AjustesDefaults.Comunicaciones.MODO_ENVIO_MENSAJE_SEGURO,
    val preguntarPurgaAgenda: Boolean = AjustesDefaults.Comunicaciones.PREGUNTAR_PURGA_AGENDA
)
