/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable

/**
 * Agrupa la configuración de Autocompletado, Inline Suggestions y Llaves de acceso (Passkeys).
 * Encapsulado para mantener los slots del constructor de [AjustesApp] dentro de los límites del JVM.
 */
@Immutable
data class AjustesAutocompletado(
    val sugerenciasTeclado: Boolean = AjustesDefaults.Autocompletado.SUGERENCIAS_TECLADO,
    val copiarTotpAutomatico: Boolean = AjustesDefaults.Autocompletado.COPIAR_TOTP_AUTOMATICO,
    val pistasUsuario: List<String> = AjustesDefaults.Autocompletado.PISTAS_USUARIO,
    val pistasContrasena: List<String> = AjustesDefaults.Autocompletado.PISTAS_CONTRASENA,
    val pistasOtp: List<String> = AjustesDefaults.Autocompletado.PISTAS_OTP,
    val pistasDocumento: List<String> = AjustesDefaults.Autocompletado.PISTAS_DOCUMENTO,
    val paquetesComunicacion: Set<String> = AjustesDefaults.Autocompletado.PAQUETES_COMUNICACION,
    val pistasContactoTelefono: List<String> = AjustesDefaults.Autocompletado.PISTAS_CONTACTO_TELEFONO,
    val pistasContactoNombre: List<String> = AjustesDefaults.Autocompletado.PISTAS_CONTACTO_NOMBRE,
    val mapeoPaquetesPersonalizados: Map<String, String> = AjustesDefaults.Autocompletado.MAPEO_PAQUETES_PERSONALIZADOS,
    val navegadoresPersonalizados: List<String> = AjustesDefaults.Autocompletado.NAVEGADORES_PERSONALIZADOS,
    val maxSugerenciasAutofill: Int = AjustesDefaults.Autocompletado.MAX_SUGERENCIAS_AUTOFILL,
    val mostrarDatosTecnicosPasskey: Boolean = AjustesDefaults.Autocompletado.MOSTRAR_DATOS_TECNICOS_PASSKEY,
    val pistasTarjetaNumero: List<String> = AjustesDefaults.Autocompletado.PISTAS_TARJETA_NUMERO,
    val pistasTarjetaCvv: List<String> = AjustesDefaults.Autocompletado.PISTAS_TARJETA_CVV,
    val pistasTarjetaExpiracion: List<String> = AjustesDefaults.Autocompletado.PISTAS_TARJETA_EXPIRACION,
    val pistasTarjetaTitular: List<String> = AjustesDefaults.Autocompletado.PISTAS_TARJETA_TITULAR,
    val pistasDireccionCalle: List<String> = AjustesDefaults.Autocompletado.PISTAS_DIRECCION_CALLE,
    val pistasDireccionCp: List<String> = AjustesDefaults.Autocompletado.PISTAS_DIRECCION_CP,
    val pistasDireccionCiudad: List<String> = AjustesDefaults.Autocompletado.PISTAS_DIRECCION_CIUDAD,
    val pistasDireccionProvincia: List<String> = AjustesDefaults.Autocompletado.PISTAS_DIRECCION_PROVINCIA,
    val pistasDireccionPais: List<String> = AjustesDefaults.Autocompletado.PISTAS_DIRECCION_PAIS,
    val tiposDocumentoPersonalizados: List<String> = AjustesDefaults.Autocompletado.TIPOS_DOCUMENTO_PERSONALIZADOS
)
