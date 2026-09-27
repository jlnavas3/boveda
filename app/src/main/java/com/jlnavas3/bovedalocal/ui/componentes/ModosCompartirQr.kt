package com.jlnavas3.bovedalocal.ui.componentes

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase
import com.jlnavas3.bovedalocal.util.GeneradorQr

/**
 * Modos de compartición disponibles para generación de códigos QR.
 */
enum class ModoCompartirQr(val etiqueta: String, val descripcionInformativa: String) {
    WIFI(
        "Conectar Wi-Fi",
        "Escanea este código con la cámara de otro teléfono para conectarte automáticamente a la red Wi-Fi."
    ),
    TOTP(
        "2FA / TOTP",
        "Escanea con tu aplicación de autenticación para vincular este token 2FA."
    ),
    CONTRASENA(
        "Contraseña",
        "Escanea pantalla a pantalla para transferir únicamente la contraseña."
    ),
    CREDENCIAL(
        "Completa",
        "Transfiere los datos de la cuenta de forma segura y 100% offline."
    )
}

/**
 * Estado extraído de una credencial para la orquestación del diálogo QR.
 */
data class EstadoDatosQr(
    val ssidWifi: String,
    val claveWifi: String,
    val esWifi: Boolean,
    val tieneTotp: Boolean,
    val tieneContrasena: Boolean,
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

        val tieneTotp = !entrada.secretoTotp.isNullOrBlank()
        val tieneContrasena = entrada.contrasena.isNotBlank() || (esWifi && claveWifi.isNotBlank())

        val modosDisponibles = buildList {
            if (esWifi) add(ModoCompartirQr.WIFI)
            if (tieneTotp) add(ModoCompartirQr.TOTP)
            if (tieneContrasena) add(ModoCompartirQr.CONTRASENA)
            add(ModoCompartirQr.CREDENCIAL)
        }

        val modoInicial = when {
            esWifi -> ModoCompartirQr.WIFI
            tieneTotp -> ModoCompartirQr.TOTP
            tieneContrasena -> ModoCompartirQr.CONTRASENA
            else -> ModoCompartirQr.CREDENCIAL
        }

        return EstadoDatosQr(
            ssidWifi = ssidWifi,
            claveWifi = claveWifi,
            esWifi = esWifi,
            tieneTotp = tieneTotp,
            tieneContrasena = tieneContrasena,
            modosDisponibles = modosDisponibles,
            modoInicial = modoInicial
        )
    }

    fun generarTextoQr(entrada: Entrada, modo: ModoCompartirQr, estado: EstadoDatosQr): String {
        return when (modo) {
            ModoCompartirQr.WIFI -> GeneradorQr.textoWifiDesdeEntrada(entrada)
            ModoCompartirQr.TOTP -> GeneradorQr.uriTotp(entrada) ?: entrada.titulo
            ModoCompartirQr.CONTRASENA -> if (estado.esWifi) estado.claveWifi else entrada.contrasena
            ModoCompartirQr.CREDENCIAL -> GeneradorQr.textoCredencialCompleta(entrada)
        }
    }
}
