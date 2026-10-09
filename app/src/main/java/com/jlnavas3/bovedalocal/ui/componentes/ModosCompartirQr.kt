package com.jlnavas3.bovedalocal.ui.componentes

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase
import com.jlnavas3.bovedalocal.util.GeneradorQr

/**
 * Modos de compartición disponibles para generación de códigos QR.
 */
enum class ModoCompartirQr(val etiqueta: String, val descripcionInformativa: String) {
    CONTACTO(
        "Contacto",
        "Escanea con la cámara de cualquier teléfono para guardar este contacto directamente en la agenda."
    ),
    WIFI(
        "Wi-Fi",
        "Escanea este código con la cámara de otro teléfono para conectarte automáticamente a la red Wi-Fi."
    ),
    TOTP(
        "2FA / TOTP",
        "Escanea con tu aplicación de autenticación para vincular este token 2FA."
    ),
    TRANSFERIR(
        "Transferir",
        "Escanea desde otra Bóveda Local para importar esta entrada de forma íntegra y 100% offline."
    )
}

/**
 * Estado extraído de una credencial para la orquestación del diálogo QR.
 */
data class EstadoDatosQr(
    val ssidWifi: String,
    val claveWifi: String,
    val esWifi: Boolean,
    val esContacto: Boolean,
    val tieneTotp: Boolean,
    val modosDisponibles: List<ModoCompartirQr>,
    val modoInicial: ModoCompartirQr
)

/**
 * Lógica pura de extracción de datos y formateo de texto QR a partir de una Entrada.
 */
object ExtractorModosQr {

    fun extraerEstado(entrada: Entrada): EstadoDatosQr {
        val ssidWifi = GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Nombre de red (SSID)")
            .ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "SSID") }
            .ifBlank { entrada.titulo }
            .trim()

        val claveWifi = GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Contraseña Wi-Fi")
            .ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Contraseña") }
            .ifBlank { entrada.contrasena }

        val esWifi = entrada.tipo == TipoEntrada.WIFI ||
            GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Nombre de red (SSID)").isNotBlank()

        val esContacto = entrada.tipo == TipoEntrada.CONTACTO

        val tieneTotp = !entrada.secretoTotp.isNullOrBlank()

        val modosDisponibles = buildList {
            if (esContacto) add(ModoCompartirQr.CONTACTO)
            if (esWifi) add(ModoCompartirQr.WIFI)
            if (tieneTotp) add(ModoCompartirQr.TOTP)
            add(ModoCompartirQr.TRANSFERIR)
        }

        val modoInicial = when {
            esContacto -> ModoCompartirQr.CONTACTO
            esWifi -> ModoCompartirQr.WIFI
            tieneTotp -> ModoCompartirQr.TOTP
            else -> ModoCompartirQr.TRANSFERIR
        }

        return EstadoDatosQr(
            ssidWifi = ssidWifi,
            claveWifi = claveWifi,
            esWifi = esWifi,
            esContacto = esContacto,
            tieneTotp = tieneTotp,
            modosDisponibles = modosDisponibles,
            modoInicial = modoInicial
        )
    }

    fun generarTextoQr(entrada: Entrada, modo: ModoCompartirQr, estado: EstadoDatosQr): String {
        return when (modo) {
            ModoCompartirQr.CONTACTO -> GeneradorQr.textoVCardDesdeEntrada(entrada)
            ModoCompartirQr.WIFI -> GeneradorQr.textoWifiDesdeEntrada(entrada)
            ModoCompartirQr.TOTP -> GeneradorQr.uriTotp(entrada) ?: entrada.titulo
            ModoCompartirQr.TRANSFERIR -> GeneradorQr.textoTransferenciaBoveda(entrada)
        }
    }
}
