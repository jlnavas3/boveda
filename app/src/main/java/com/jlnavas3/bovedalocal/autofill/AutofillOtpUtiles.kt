package com.jlnavas3.bovedalocal.autofill

import android.content.Context
import android.service.autofill.Dataset
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import com.jlnavas3.bovedalocal.util.MapeadorPaquetesPopulares

object AutofillOtpUtiles {

    private val PISTAS_OTP = listOf(
        "otp",
        "totp",
        "2fa",
        "mfa",
        "one-time-code",
        "verification",
        "verificacion",
        "verificación",
        "codigo",
        "código",
        "security_code",
        "auth_code",
        "token"
    )

    /**
     * Determina si un campo de texto de la estructura corresponde a un código de un solo uso (OTP/2FA).
     */
    fun esCampoOtp(
        pistasSistema: List<String>,
        textoPistas: List<String>,
        htmlAutocomplete: String? = null,
        pistasOtp: List<String> = emptyList()
    ): Boolean {
        if (pistasSistema.any { it.contains("otp") || it.contains("one-time-code") || it.contains("sms_otp") }) {
            return true
        }
        if (htmlAutocomplete?.contains("one-time-code", ignoreCase = true) == true) {
            return true
        }
        val listaOtp = pistasOtp.ifEmpty { com.jlnavas3.bovedalocal.data.AjustesDefaults.Autocompletado.PISTAS_OTP }
        return textoPistas.any { pista ->
            listaOtp.any { pista.contains(it, ignoreCase = true) }
        }
    }

    /**
     * Obtiene el código TOTP actual en tiempo real para una entrada si tiene secreto configurado.
     */
    fun obtenerCodigoTotp(entrada: Entrada, momentoMs: Long = System.currentTimeMillis()): String? {
        val secreto = entrada.secretoTotp?.takeIf { it.isNotBlank() } ?: return null
        return try {
            Totp.codigo(
                secreto = Base32.decodificar(secreto),
                segundosUnix = momentoMs / 1000L,
                digitos = entrada.totpDigitos,
                periodo = entrada.totpPeriodo.toLong(),
                algoritmo = entrada.totpAlgoritmo
            )
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Construye un Dataset específico de 2FA para rellenar campos de código de un solo uso.
     */
    @Suppress("DEPRECATION")
    fun datasetTotp(
        contexto: Context,
        entrada: Entrada,
        idCampoOtp: AutofillId,
        inlineSpec: android.widget.inline.InlinePresentationSpec? = null,
        paqueteSolicitante: String? = null
    ): Dataset? {
        val codigo = obtenerCodigoTotp(entrada) ?: return null
        val dom = entrada.urls.firstNotNullOfOrNull { Dominios.host(it) }?.let { Dominios.raiz(it) }
        val paqueteDeDominio = dom?.let { MapeadorPaquetesPopulares.obtenerPaquete(it) }
        val paquete = entrada.urls.firstNotNullOfOrNull { LanzadorEnlaces.extraerPaquete(it) }
            ?: entrada.passkey?.rpId?.let { LanzadorEnlaces.extraerPaquete(it) }
            ?: (if (paqueteDeDominio != null && LanzadorEnlaces.estaInstalada(contexto, paqueteDeDominio)) paqueteDeDominio else null)
            ?: GestorAppsInstaladas.resolverPaqueteApp(contexto, entrada)
            ?: (if (!paqueteSolicitante.isNullOrBlank() && LanzadorEnlaces.estaInstalada(contexto, paqueteSolicitante)) paqueteSolicitante else null)
        val iconoBitmapCirculo = if (paquete != null && LanzadorEnlaces.estaInstalada(contexto, paquete)) {
            AutofillUtiles.obtenerBitmapIconoCircular(contexto, paquete)
        } else null
        val iconoGlifoTeclado = ResolverIconoGlifoTeclado.resolver(
            contexto = contexto,
            entrada = entrada,
            paquete = paquete,
            dominioWeb = entrada.urls.firstNotNullOfOrNull { Dominios.host(it) }
        )
        val nombreApp = if (paquete != null && LanzadorEnlaces.estaInstalada(contexto, paquete)) {
            LanzadorEnlaces.obtenerNombreApp(contexto, paquete)
        } else null
        val tituloBase = when {
            !nombreApp.isNullOrBlank() && (entrada.titulo.isBlank() ||
                entrada.titulo == "Nueva entrada" ||
                entrada.titulo.equals(paquete, ignoreCase = true)) -> nombreApp
            entrada.titulo.isNotBlank() -> entrada.titulo
            else -> "Cuenta"
        }
        val vista = AutofillUtiles.presentacion(
            contexto = contexto,
            titulo = "$tituloBase (Código de verificación)",
            subtitulo = "Código actual: $codigo",
            iconoBitmap = iconoBitmapCirculo
        )
        val inlinePres = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && inlineSpec != null) {
            CreadorInlineSuggestion.crear(
                contexto = contexto,
                spec = inlineSpec,
                titulo = "$tituloBase: $codigo",
                subtitulo = "Código de verificación",
                iconoBitmap = iconoGlifoTeclado
            )
        } else null

        val constructor = Dataset.Builder(vista)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && inlinePres != null) {
            constructor.setInlinePresentation(inlinePres)
            @Suppress("DEPRECATION")
            constructor.setValue(idCampoOtp, AutofillValue.forText(codigo), vista, inlinePres)
        } else {
            constructor.setValue(idCampoOtp, AutofillValue.forText(codigo))
        }
        return try {
            constructor.build()
        } catch (_: Exception) {
            null
        }
    }
}
