package com.jlnavas3.bovedalocal.data

import java.util.UUID

data class PresetRapido(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tipoEntradaSugerido: TipoEntrada,
    val generarCampos: () -> List<CampoPersonalizado>
)

object PresetsCampos {

    private fun campo(
        etiqueta: String,
        tipo: TipoCampo,
        sensible: Boolean = false,
        formato: String? = null
    ) = CampoPersonalizado(
        id = UUID.randomUUID().toString(),
        etiqueta = etiqueta,
        valor = "",
        tipo = tipo,
        esSensible = sensible,
        formato = formato
    )

    fun tarjeta(): List<CampoPersonalizado> = listOf(
        campo("Titular", TipoCampo.TEXTO),
        campo("Número de tarjeta", TipoCampo.TEXTO, sensible = true, formato = "TARJETA_CREDITO"),
        campo("Vencimiento", TipoCampo.FECHA, formato = "MM/AA"),
        campo("CVV", TipoCampo.NUMERO, sensible = true),
        campo("PIN de tarjeta", TipoCampo.PIN, sensible = true)
    )

    fun wifi(): List<CampoPersonalizado> = listOf(
        campo("Nombre de red (SSID)", TipoCampo.TEXTO),
        campo("Contraseña Wi-Fi", TipoCampo.TEXTO, sensible = true),
        campo("Tipo de seguridad", TipoCampo.TEXTO)
    )

    fun cuentaBancaria(): List<CampoPersonalizado> = listOf(
        campo("Banco / Entidad", TipoCampo.TEXTO),
        campo("Titular", TipoCampo.TEXTO),
        campo("Tipo de cuenta", TipoCampo.TEXTO),
        campo("Número de cuenta / IBAN", TipoCampo.TEXTO, sensible = true),
        campo("CBU / CLABE / SWIFT", TipoCampo.TEXTO)
    )

    fun identidad(): List<CampoPersonalizado> = listOf(
        campo("Tipo de documento (DNI/Pasaporte)", TipoCampo.TEXTO),
        campo("Número de documento", TipoCampo.TEXTO, sensible = true),
        campo("Fecha de expedición", TipoCampo.FECHA),
        campo("Fecha de caducidad", TipoCampo.FECHA),
        campo("País emisor", TipoCampo.TEXTO)
    )

    fun servidorSsh(): List<CampoPersonalizado> = listOf(
        campo("Host o IP", TipoCampo.TEXTO),
        campo("Puerto", TipoCampo.NUMERO),
        campo("Usuario SSH", TipoCampo.TEXTO),
        campo("Clave privada / Contraseña", TipoCampo.NOTAS, sensible = true)
    )

    fun criptoWallet(): List<CampoPersonalizado> = listOf(
        campo("Red / Blockchain", TipoCampo.TEXTO),
        campo("Dirección pública", TipoCampo.TEXTO),
        campo("Frase semilla (Seed phrase)", TipoCampo.NOTAS, sensible = true),
        campo("Clave privada", TipoCampo.NOTAS, sensible = true)
    )

    fun paraTipoEntrada(tipo: TipoEntrada): List<CampoPersonalizado> = when (tipo) {
        TipoEntrada.TARJETA -> tarjeta()
        TipoEntrada.WIFI -> wifi()
        TipoEntrada.CUENTA_BANCARIA -> cuentaBancaria()
        TipoEntrada.IDENTIDAD -> identidad()
        TipoEntrada.SERVIDOR -> servidorSsh()
        TipoEntrada.WALLET -> criptoWallet()
        else -> emptyList()
    }

    val todos: List<PresetRapido> = listOf(
        PresetRapido(
            id = "tarjeta",
            titulo = "Tarjeta bancaria",
            descripcion = "Titular, número, vencimiento, CVV y PIN",
            tipoEntradaSugerido = TipoEntrada.TARJETA,
            generarCampos = ::tarjeta
        ),
        PresetRapido(
            id = "wifi",
            titulo = "Red Wi-Fi",
            descripcion = "Nombre SSID, contraseña y tipo de seguridad",
            tipoEntradaSugerido = TipoEntrada.WIFI,
            generarCampos = ::wifi
        ),
        PresetRapido(
            id = "cuenta_bancaria",
            titulo = "Cuenta bancaria",
            descripcion = "Banco, titular, IBAN/cuenta y claves de transferencia",
            tipoEntradaSugerido = TipoEntrada.CUENTA_BANCARIA,
            generarCampos = ::cuentaBancaria
        ),
        PresetRapido(
            id = "identidad",
            titulo = "Documento de identidad",
            descripcion = "Tipo, número oficial, emisión y caducidad",
            tipoEntradaSugerido = TipoEntrada.IDENTIDAD,
            generarCampos = ::identidad
        ),
        PresetRapido(
            id = "servidor",
            titulo = "Servidor / SSH",
            descripcion = "Host/IP, puerto, usuario y clave SSH",
            tipoEntradaSugerido = TipoEntrada.SERVIDOR,
            generarCampos = ::servidorSsh
        ),
        PresetRapido(
            id = "wallet",
            titulo = "Cripto Wallet",
            descripcion = "Red, dirección pública, semilla mnemónica y clave privada",
            tipoEntradaSugerido = TipoEntrada.WALLET,
            generarCampos = ::criptoWallet
        )
    )
}
