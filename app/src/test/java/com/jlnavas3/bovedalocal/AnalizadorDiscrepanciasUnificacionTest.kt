/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AnalizadorDiscrepanciasUnificacion
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalizadorDiscrepanciasUnificacionTest {

    @Test
    fun `entradas identicas no generan discrepancias`() {
        val entrada1 = Entrada(
            id = "1",
            titulo = "GitHub",
            usuario = "octocat",
            contrasena = "secret123",
            urls = listOf("https://github.com")
        )
        val entrada2 = entrada1.copy(id = "2")

        val res = AnalizadorDiscrepanciasUnificacion.analizar(listOf(entrada1, entrada2), entrada1)

        assertFalse(res.hayServiciosDiferentes)
        assertFalse(res.hayUsuariosDiferentes)
        assertFalse(res.hayContrasenasDiferentes)
        assertFalse(res.hayTotpsMultiples)
        assertFalse(res.hayPasskeysMultiples)
        assertFalse(res.tieneAlgunaAdvertencia)
        assertFalse(res.tieneAdvertenciasCriticas)
    }

    @Test
    fun `detecta servicios diferentes correctamente`() {
        val entrada1 = Entrada(
            id = "1",
            titulo = "Google",
            usuario = "user@gmail.com",
            urls = listOf("https://accounts.google.com")
        )
        val entrada2 = Entrada(
            id = "2",
            titulo = "Amazon",
            usuario = "user@gmail.com",
            urls = listOf("https://amazon.com")
        )

        val res = AnalizadorDiscrepanciasUnificacion.analizar(listOf(entrada1, entrada2), entrada1)

        assertTrue(res.hayServiciosDiferentes)
        assertTrue(res.tieneAdvertenciasCriticas)
        assertTrue(res.tieneAlgunaAdvertencia)
        assertEquals(2, res.servicios.size)
    }

    @Test
    fun `detecta usuarios y contrasenas diferentes`() {
        val entrada1 = Entrada(
            id = "1",
            titulo = "Servicio",
            usuario = "juan",
            contrasena = "pass1",
            urls = listOf("https://ejemplo.com")
        )
        val entrada2 = Entrada(
            id = "2",
            titulo = "Servicio",
            usuario = "pedro",
            contrasena = "pass2",
            urls = listOf("https://ejemplo.com")
        )

        val res = AnalizadorDiscrepanciasUnificacion.analizar(listOf(entrada1, entrada2), entrada1)

        assertFalse(res.hayServiciosDiferentes)
        assertTrue(res.hayUsuariosDiferentes)
        assertTrue(res.hayContrasenasDiferentes)
        assertEquals(2, res.usuarios.size)
        assertEquals(2, res.totalContrasenas)
    }

    @Test
    fun `detecta multiples 2FA y Passkeys`() {
        val entrada1 = Entrada(
            id = "1",
            titulo = "Servicio",
            usuario = "juan",
            secretoTotp = "JBSWY3DPEHPK3PXP",
            passkey = DatosPasskey(
                rpId = "ejemplo.com",
                rpName = "Ejemplo",
                userHandle = "handle1",
                credId = "cred-1",
                clavePrivada = "priv1",
                usuario = "juan"
            )
        )
        val entrada2 = Entrada(
            id = "2",
            titulo = "Servicio",
            usuario = "juan",
            secretoTotp = "HXDMVJECJJWSRB3H",
            passkey = DatosPasskey(
                rpId = "ejemplo.com",
                rpName = "Ejemplo",
                userHandle = "handle2",
                credId = "cred-2",
                clavePrivada = "priv2",
                usuario = "juan"
            )
        )

        val res = AnalizadorDiscrepanciasUnificacion.analizar(listOf(entrada1, entrada2), entrada1)

        assertTrue(res.hayTotpsMultiples)
        assertTrue(res.hayPasskeysMultiples)
    }
}
