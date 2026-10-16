/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.crypto.AlgoritmoSsh
import com.jlnavas3.bovedalocal.crypto.GeneradorClavesSsh
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GeneradorClavesSshTest {

    @Test
    fun `generar clave RSA 2048 produce formato OpenSSH y PEM PKCS8 valido`() {
        val par = GeneradorClavesSsh.generar(AlgoritmoSsh.RSA_2048, "test@servidor")
        assertNotNull(par)
        assertTrue(par.clavePublicaSsh.startsWith("ssh-rsa "))
        assertTrue(par.clavePublicaSsh.endsWith(" test@servidor"))
        assertTrue(par.clavePrivadaPem.startsWith("-----BEGIN PRIVATE KEY-----"))
        assertTrue(par.clavePrivadaPem.endsWith("-----END PRIVATE KEY-----"))
        assertTrue(par.huellaSha256.startsWith("SHA256:"))
    }

    @Test
    fun `generar clave ECDSA produce formato OpenSSH ecdsa-sha2-nistp256`() {
        val par = GeneradorClavesSsh.generar(AlgoritmoSsh.ECDSA_P256, "ec@servidor")
        assertNotNull(par)
        assertTrue(par.clavePublicaSsh.startsWith("ecdsa-sha2-nistp256 "))
        assertTrue(par.clavePublicaSsh.endsWith(" ec@servidor"))
        assertTrue(par.clavePrivadaPem.contains("BEGIN PRIVATE KEY"))
        assertTrue(par.huellaSha256.startsWith("SHA256:"))
    }

    @Test
    fun `generar clave Ed25519 funciona o recurre a fallback seguro sin excepciones`() {
        val par = GeneradorClavesSsh.generar(AlgoritmoSsh.ED25519, "ed@servidor")
        assertNotNull(par)
        assertTrue(
            par.clavePublicaSsh.startsWith("ssh-ed25519 ") ||
            par.clavePublicaSsh.startsWith("ssh-rsa ") ||
            par.clavePublicaSsh.startsWith("ecdsa-sha2-nistp256 ")
        )
        assertTrue(par.clavePrivadaPem.isNotBlank())
        assertTrue(par.huellaSha256.startsWith("SHA256:"))
    }
}
