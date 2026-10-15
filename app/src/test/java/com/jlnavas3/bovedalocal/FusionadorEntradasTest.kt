/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.FusionadorEntradas
import com.jlnavas3.bovedalocal.data.TipoEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FusionadorEntradasTest {

    private fun crearEntrada(
        id: String,
        titulo: String,
        usuario: String = "user@test.com",
        contrasena: String = "Clave123",
        urls: List<String> = listOf("https://test.com")
    ): Entrada = Entrada(
        id = id,
        titulo = titulo,
        usuario = usuario,
        contrasena = contrasena,
        urls = urls,
        tipo = TipoEntrada.LOGIN
    )

    @Test
    fun fusionar_mismasClavesYMismasUrls_reemplazaSinDuplicar() {
        val principal = crearEntrada("1", "Google", "jln@gmail.com", "ClaveSegura!", listOf("https://accounts.google.com"))
        val secundaria = crearEntrada("2", "Google", "jln@gmail.com", "ClaveSegura!", listOf("https://accounts.google.com"))

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        assertEquals("jln@gmail.com", fusionada.usuario)
        assertEquals("ClaveSegura!", fusionada.contrasena)
        assertEquals(listOf("https://accounts.google.com"), fusionada.urls)
        assertTrue("No debe haber historial si la contraseña era idéntica", fusionada.historialContrasenas.isEmpty())
    }

    @Test
    fun fusionar_distintasClaves_pasanAHistorialContrasenasAnteriores() {
        val principal = crearEntrada("1", "Google", "jln@gmail.com", "ClaveActual!", listOf("https://accounts.google.com"))
        val secundaria = crearEntrada("2", "Google", "jln@gmail.com", "ClaveVieja123", listOf("https://accounts.google.com"))

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        assertEquals("ClaveActual!", fusionada.contrasena)
        assertEquals(1, fusionada.historialContrasenas.size)
        assertEquals("ClaveVieja123", fusionada.historialContrasenas.first().contrasena)
        assertTrue("No debe crearse un campo personalizado redundante de clave alternativa", fusionada.camposPersonalizados.none { it.etiqueta.startsWith("Clave alternativa") })
    }

    @Test
    fun fusionar_distintosEnlaces_seAgreganComoCamposDeEnlaceAdicionales() {
        val principal = crearEntrada("1", "Google", "jln@gmail.com", "Clave123", listOf("https://myaccount.google.com/"))
        val secundaria1 = crearEntrada("2", "Google", "jln@gmail.com", "Clave123", listOf("https://accounts.google.com/"))
        val secundaria2 = crearEntrada("3", "Google", "jln@gmail.com", "Clave123", listOf("https://mail.google.com/"))

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria1, secundaria2))

        assertEquals(3, fusionada.urls.size)
        assertEquals("https://myaccount.google.com/", fusionada.urls[0])
        assertEquals("https://accounts.google.com/", fusionada.urls[1])
        assertEquals("https://mail.google.com/", fusionada.urls[2])
    }

    @Test
    fun fusionar_conservaUsuarioPrincipal() {
        val principal = crearEntrada("1", "Google", "usuario.principal@gmail.com", "Clave123")
        val secundaria = crearEntrada("2", "Google", "usuario.secundario@gmail.com", "Clave123")

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        assertEquals("usuario.principal@gmail.com", fusionada.usuario)
        assertTrue("El usuario alternativo debe quedar respaldado en notas", fusionada.notas.contains("usuario.secundario@gmail.com"))
    }

    @Test
    fun fusionar_conUsuarioElegido_estableceElegidoYRespaldaAlternativoEnNotas() {
        val principal = crearEntrada("1", "Google", "usuario.original@gmail.com", "Clave123")
        val secundaria = crearEntrada("2", "Google", "usuario.elegido@gmail.com", "Clave123")

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria), usuarioElegido = "usuario.elegido@gmail.com")

        assertEquals("usuario.elegido@gmail.com", fusionada.usuario)
        assertTrue("El usuario original no elegido debe quedar respaldado en notas", fusionada.notas.contains("usuario.original@gmail.com"))
    }

    @Test
    fun fusionar_distintosTotp_preservaSegundoComoTotpAlternativo() {
        val principal = crearEntrada("1", "X (Twitter)", "usuario_demo", "Clave123").copy(
            secretoTotp = "JBSWY3DPEHPK3PXP"
        )
        val secundaria = crearEntrada("2", "X (Twitter)", "usuario_demo", "Clave123").copy(
            secretoTotp = "KRSXG5CTMVRXEZLU"
        )

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        assertEquals("JBSWY3DPEHPK3PXP", fusionada.secretoTotp)
        val campoAlt = fusionada.camposPersonalizados.firstOrNull { it.etiqueta.contains("TOTP", ignoreCase = true) }
        org.junit.Assert.assertNotNull("Debe existir el campo personalizado TOTP alternativo", campoAlt)
        assertEquals("KRSXG5CTMVRXEZLU", campoAlt?.valor)
        assertTrue("Debe existir nota de respaldo de TOTP alternativo", fusionada.notas.contains("TOTP alternativo"))
    }

    @Test
    fun fusionar_urlsEquivalentesConYSinProtocolo_seDeduplicanConservandoLaCompleta() {
        val principal = crearEntrada(
            id = "1",
            titulo = "Disney+",
            urls = listOf(
                "disneyplus.disney.com",
                "com.disney.disneyplus",
                "starplus.disney.com",
                "com.disney.starplus"
            )
        )
        val secundaria = crearEntrada(
            id = "2",
            titulo = "Disney+",
            urls = listOf(
                "https://www.disneyplus.com/",
                "https://starplus.disney.com",
                "https://disneyplus.disney.com"
            )
        )

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        assertEquals(5, fusionada.urls.size)
        assertEquals("https://disneyplus.disney.com", fusionada.urls[0])
        assertEquals("com.disney.disneyplus", fusionada.urls[1])
        assertEquals("https://starplus.disney.com", fusionada.urls[2])
        assertEquals("com.disney.starplus", fusionada.urls[3])
        assertEquals("https://www.disneyplus.com/", fusionada.urls[4])
    }
}
