package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.passkey.WebAuthn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigInteger
import java.security.AlgorithmParameters
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECParameterSpec
import java.security.spec.ECPoint
import java.security.spec.ECPublicKeySpec

class WebAuthnCryptoTest {

    @Test
    fun `generarPar crea claves EC con coordenadas de exactamente 32 bytes`() {
        val par = WebAuthn.generarPar()
        assertNotNull(par.privadaPkcs8)
        assertTrue(par.privadaPkcs8.isNotEmpty())
        assertEquals("Coordenada X debe ser de 32 bytes", 32, par.x.size)
        assertEquals("Coordenada Y debe ser de 32 bytes", 32, par.y.size)
    }

    @Test
    fun `firmar produce una firma ECDSA verificable con la clave publica generada`() {
        val par = WebAuthn.generarPar()
        val datos = "Mensaje de autenticacion WebAuthn".toByteArray(Charsets.UTF_8)

        val firma = WebAuthn.firmar(par.privadaPkcs8, datos)
        assertTrue(firma.isNotEmpty())

        // Reconstruir la clave pública a partir de las coordenadas x e y
        val params = AlgorithmParameters.getInstance("EC").apply {
            init(ECGenParameterSpec("secp256r1"))
        }.getParameterSpec(ECParameterSpec::class.java)

        val punto = ECPoint(BigInteger(1, par.x), BigInteger(1, par.y))
        val pubSpec = ECPublicKeySpec(punto, params)
        val pubKey = KeyFactory.getInstance("EC").generatePublic(pubSpec)

        val verificador = Signature.getInstance("SHA256withECDSA").apply {
            initVerify(pubKey)
            update(datos)
        }
        assertTrue("La firma generada debe ser válida", verificador.verify(firma))
    }

    @Test
    fun `nuevoCredId genera identificadores de 16 bytes no nulos`() {
        val id1 = WebAuthn.nuevoCredId()
        val id2 = WebAuthn.nuevoCredId()

        assertEquals(16, id1.size)
        assertEquals(16, id2.size)
        assertTrue(WebAuthn.credIdValido(id1))
        assertTrue(WebAuthn.credIdValido(id2))
        assertFalse("Dos credIds aleatorios no deberían ser idénticos", id1.contentEquals(id2))
    }

    @Test
    fun `credIdValido detecta arrays vacios o de solo ceros`() {
        assertFalse(WebAuthn.credIdValido(byteArrayOf()))
        assertFalse(WebAuthn.credIdValido(ByteArray(16)))
        assertTrue(WebAuthn.credIdValido(byteArrayOf(0, 0, 0, 1)))
    }

    @Test
    fun `sha256 produce el hash criptografico correcto`() {
        val entrada = "hello world".toByteArray(Charsets.UTF_8)
        val hash = WebAuthn.sha256(entrada)
        assertEquals(32, hash.size)

        val hexEsperado = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"
        val hexObtenido = hash.joinToString("") { "%02x".format(it) }
        assertEquals(hexEsperado, hexObtenido)
    }
}
