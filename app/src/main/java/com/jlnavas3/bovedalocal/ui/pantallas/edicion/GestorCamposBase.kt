package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Gestor utilitario para campos base de tipos de entrada predefinidos
 * (Tarjeta, Wi-Fi, Cuenta Bancaria, Identidad, Servidor, Wallet).
 */
object GestorCamposBase {
    fun etiquetasBaseParaTipo(tipo: TipoEntrada): Set<String> = when (tipo) {
        TipoEntrada.TARJETA -> setOf("Titular", "Número de tarjeta", "Vencimiento", "CVV", "PIN de tarjeta")
        TipoEntrada.WIFI -> setOf("Nombre de red (SSID)", "Contraseña Wi-Fi", "Tipo de seguridad")
        TipoEntrada.CUENTA_BANCARIA -> setOf("Banco / Entidad", "Titular de la cuenta", "Número de cuenta / IBAN", "SWIFT / CBU / CLABE")
        TipoEntrada.IDENTIDAD -> setOf("Tipo de documento", "Número de documento", "Nombre completo", "Expedición", "Caducidad", "País emisor")
        TipoEntrada.SERVIDOR -> setOf("Host o IP", "Puerto", "Usuario SSH", "Clave privada / Password")
        TipoEntrada.WALLET -> setOf("Red / Blockchain", "Dirección pública", "Frase semilla (Seed phrase)", "Clave privada")
        else -> emptySet()
    }

    fun valorDeCampo(campos: List<CampoPersonalizado>, clave: String): String {
        return campos.firstOrNull { it.etiqueta.equals(clave, ignoreCase = true) }?.valor
            ?: campos.firstOrNull { it.etiqueta.startsWith(clave, ignoreCase = true) }?.valor
            ?: campos.firstOrNull {
                val c = clave.lowercase()
                val e = it.etiqueta.lowercase()
                (c.contains("seguridad") && e.contains("seguridad")) ||
                (c.contains("ssid") && e.contains("ssid")) ||
                (c.contains("red") && e.contains("red"))
            }?.valor
            ?: ""
    }

    fun actualizarValor(
        campos: List<CampoPersonalizado>,
        etiqueta: String,
        nuevoValor: String,
        tipo: TipoCampo,
        sensible: Boolean = false,
        formato: String? = null
    ): List<CampoPersonalizado> {
        val lista = campos.toMutableList()
        val index = lista.indexOfFirst {
            it.etiqueta.equals(etiqueta, ignoreCase = true) ||
            it.etiqueta.startsWith(etiqueta, ignoreCase = true) ||
            (etiqueta.contains("seguridad", ignoreCase = true) && it.etiqueta.contains("seguridad", ignoreCase = true)) ||
            (etiqueta.contains("ssid", ignoreCase = true) && it.etiqueta.contains("ssid", ignoreCase = true))
        }
        if (index >= 0) {
            lista[index] = lista[index].copy(etiqueta = etiqueta, valor = nuevoValor, tipo = tipo, esSensible = sensible, formato = formato)
        } else {
            lista.add(
                CampoPersonalizado(
                    etiqueta = etiqueta,
                    valor = nuevoValor,
                    tipo = tipo,
                    esSensible = sensible,
                    formato = formato
                )
            )
        }
        return lista
    }
}
