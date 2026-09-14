package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.PinAutodestruccion
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PinAutodestruccionTest {

    @Test
    fun `sha256 genera hashes consistentes con la misma sal`() {
        val salt = "1234567890abcdef".toByteArray()
        val hash1 = PinAutodestruccion.sha256("9988", salt)
        val hash2 = PinAutodestruccion.sha256("9988", salt)
        assertTrue(hash1.contentEquals(hash2))

        val hashDistinto = PinAutodestruccion.sha256("9989", salt)
        assertFalse(hash1.contentEquals(hashDistinto))
    }

    @Test
    fun `verificarPin valida correctamente el hash y salt hex`() {
        val salt = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val saltHex = salt.joinToString("") { "%02x".format(it) }
        val hash = PinAutodestruccion.sha256("4321", salt)
        val hashHex = hash.joinToString("") { "%02x".format(it) }

        assertTrue(PinAutodestruccion.verificarPin("4321", saltHex, hashHex))
        assertFalse(PinAutodestruccion.verificarPin("0000", saltHex, hashHex))
        assertFalse(PinAutodestruccion.verificarPin("", saltHex, hashHex))
        assertFalse(PinAutodestruccion.verificarPin("4321", "", hashHex))
        assertFalse(PinAutodestruccion.verificarPin("4321", saltHex, ""))
    }
}
