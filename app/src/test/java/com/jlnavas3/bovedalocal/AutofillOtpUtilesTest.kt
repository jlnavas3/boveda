package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.autofill.AutofillOtpUtiles
import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AutofillOtpUtilesTest {

    @Test
    fun `esCampoOtp detecta campos de un solo uso por pistas`() {
        assertTrue(AutofillOtpUtiles.esCampoOtp(listOf("autofill_hint_sms_otp"), emptyList()))
        assertTrue(AutofillOtpUtiles.esCampoOtp(emptyList(), emptyList(), "one-time-code"))
        assertTrue(AutofillOtpUtiles.esCampoOtp(emptyList(), listOf("introduce tu código totp")))
        assertTrue(AutofillOtpUtiles.esCampoOtp(emptyList(), listOf("input_2fa_verification")))
        assertFalse(AutofillOtpUtiles.esCampoOtp(emptyList(), listOf("contraseña", "password")))
    }

    @Test
    fun `obtenerCodigoTotp genera codigo de 6 digitos si hay secreto`() {
        val entrada = Entrada(
            id = "totp-1",
            titulo = "Google",
            secretoTotp = "JBSWY3DPEHPK3PXP",
            totpDigitos = 6
        )
        val codigo = AutofillOtpUtiles.obtenerCodigoTotp(entrada, momentoMs = 1234567890000L)
        assertNotNull(codigo)
        assertEquals(6, codigo!!.length)
    }

    @Test
    fun `obtenerCodigoTotp devuelve null si no hay secreto`() {
        val entrada = Entrada(id = "sin-totp", titulo = "Twitter")
        assertNull(AutofillOtpUtiles.obtenerCodigoTotp(entrada))
    }
}
