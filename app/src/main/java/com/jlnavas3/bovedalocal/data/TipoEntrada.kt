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
    PASSKEY;

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
        }
}
