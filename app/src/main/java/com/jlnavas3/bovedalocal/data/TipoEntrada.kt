/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

@Serializable
enum class TipoEntrada {
    LOGIN,
    NOTA,
    TARJETA,
    WIFI,
    CUENTA_BANCARIA,
    IDENTIDAD,
    SERVIDOR,
    WALLET,
    PASSKEY,
    CONTACTO;

    val etiqueta: String
        get() = when (this) {
            LOGIN -> "Contraseña"
            NOTA -> "Nota segura"
            TARJETA -> "Tarjeta bancaria"
            WIFI -> "Red Wi-Fi"
            CUENTA_BANCARIA -> "Cuenta bancaria"
            IDENTIDAD -> "Documento de identidad"
            SERVIDOR -> "Servidor / SSH"
            WALLET -> "Cripto Wallet"
            PASSKEY -> "Llave de paso"
            CONTACTO -> "Contacto seguro"
        }
}
