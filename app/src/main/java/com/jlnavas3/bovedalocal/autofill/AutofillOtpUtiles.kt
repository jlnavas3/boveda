package com.jlnavas3.bovedalocal.autofill

import android.content.Context
import android.service.autofill.Dataset
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.Entrada

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
        htmlAutocomplete: String? = null
    ): Boolean {
        if (pistasSistema.any { it.contains("otp") || it.contains("one-time-code") || it.contains("sms_otp") }) {
            return true
        }
        if (htmlAutocomplete?.contains("one-time-code", ignoreCase = true) == true) {
            return true
        }
        return textoPistas.any { pista ->
            PISTAS_OTP.any { pista.contains(it, ignoreCase = true) }
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
        idCampoOtp: AutofillId
    ): Dataset? {
        val codigo = obtenerCodigoTotp(entrada) ?: return null
        val vista = AutofillUtiles.presentacion(
            contexto = contexto,
            titulo = "${entrada.titulo.ifBlank { "Cuenta" }} (Código 2FA)",
            subtitulo = "Código actual: $codigo"
        )
        val constructor = Dataset.Builder(vista)
        constructor.setValue(idCampoOtp, AutofillValue.forText(codigo))
        return try {
            constructor.build()
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}
