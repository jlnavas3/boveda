package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.crypto.OtpAuth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class OtpAuthTest {

    @Test
    fun `parsea URI estandar de Google Authenticator`() {
        val uri = "otpauth://totp/GitHub:pepotech?secret=JBSWY3DPEHPK3PXP&issuer=GitHub"
        val semilla = OtpAuth.leer(uri)
        assertNotNull(semilla)
        assertEquals("GitHub", semilla!!.emisor)
        assertEquals("pepotech", semilla.cuenta)
        assertEquals("JBSWY3DPEHPK3PXP", semilla.secreto)
        assertEquals(6, semilla.digitos)
        assertEquals(30, semilla.periodo)
        assertEquals("HmacSHA1", semilla.algoritmo)
        assertEquals("GitHub", semilla.titulo)
    }

    @Test
    fun `parsea parametros personalizados de digitos periodo y algoritmo`() {
        val uri = "otpauth://totp/MiServicio:admin?secret=JBSWY3DPEHPK3PXP&digits=8&period=60&algorithm=SHA256"
        val semilla = OtpAuth.leer(uri)
        assertNotNull(semilla)
        assertEquals("MiServicio", semilla!!.emisor)
        assertEquals("admin", semilla.cuenta)
        assertEquals(8, semilla.digitos)
        assertEquals(60, semilla.periodo)
        assertEquals("HmacSHA256", semilla.algoritmo)
    }

    @Test
    fun `soporta algoritmo SHA512`() {
        val uri = "otpauth://totp/Test:user?secret=JBSWY3DPEHPK3PXP&algorithm=SHA512"
        val semilla = OtpAuth.leer(uri)
        assertNotNull(semilla)
        assertEquals("HmacSHA512", semilla!!.algoritmo)
    }

    @Test
    fun `decodifica caracteres especiales en URL`() {
        val uri = "otpauth://totp/Amazon%20Web%20Services:juan%40empresa.com?secret=JBSWY3DPEHPK3PXP"
        val semilla = OtpAuth.leer(uri)
        assertNotNull(semilla)
        assertEquals("Amazon Web Services", semilla!!.emisor)
        assertEquals("juan@empresa.com", semilla.cuenta)
    }

    @Test
    fun `acepta secreto Base32 pegado a pelo sin esquema otpauth`() {
        val secretoCrudo = "JBSWY 3DPEH PK3PXP"
        val semilla = OtpAuth.leer(secretoCrudo, digitosManual = 6, periodoManual = 30)
        assertNotNull(semilla)
        assertEquals("JBSWY3DPEHPK3PXP", semilla!!.secreto)
        assertEquals(6, semilla.digitos)
        assertEquals(30, semilla.periodo)
        assertEquals("2FA", semilla.titulo)
    }

    @Test
    fun `devuelve null ante secreto base32 invalido`() {
        val uri = "otpauth://totp/Test:user?secret=CARACTERES_INVALIDOS_189"
        val semilla = OtpAuth.leer(uri)
        assertNull(semilla)
    }

    @Test
    fun `devuelve null si falta el parametro secret en la URI`() {
        val uri = "otpauth://totp/Test:user?issuer=Test"
        val semilla = OtpAuth.leer(uri)
        assertNull(semilla)
    }

    @Test
    fun `devuelve null para texto vacio o no reconocible`() {
        assertNull(OtpAuth.leer(""))
        assertNull(OtpAuth.leer("cadena_invalida_con_caracteres_8_9_0_1_!#$"))
        assertNull(OtpAuth.leer("otpauth://otra_cosa/sin_totp"))
    }
}
