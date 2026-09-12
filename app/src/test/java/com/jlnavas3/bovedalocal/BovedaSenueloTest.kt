package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.BovedaSenuelo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.security.SecureRandom

class BovedaSenueloTest {

    @Test
    fun `verificarPin valida correctamente el hash y rechaza pines incorrectos`() {
        val pinReal = "9988"
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val hash = BovedaSenuelo.sha256(pinReal, salt)

        val saltHex = salt.joinToString("") { "%02x".format(it) }
        val hashHex = hash.joinToString("") { "%02x".format(it) }

        assertTrue(BovedaSenuelo.verificarPin("9988", saltHex, hashHex))
        assertFalse(BovedaSenuelo.verificarPin("1234", saltHex, hashHex))
        assertFalse(BovedaSenuelo.verificarPin("9989", saltHex, hashHex))
        assertFalse(BovedaSenuelo.verificarPin("", saltHex, hashHex))
    }

    @Test
    fun `cuentasEjemplo devuelve entradas simuladas creibles`() {
        val ejemplos = BovedaSenuelo.cuentasEjemplo()
        assertTrue(ejemplos.isNotEmpty())
        assertEquals(3, ejemplos.size)
        assertTrue(ejemplos.any { it.titulo.contains("Wi-Fi") })
        assertTrue(ejemplos.any { it.titulo.contains("Correo") })
        assertTrue(ejemplos.any { it.titulo.contains("Spotify") })
    }
}
