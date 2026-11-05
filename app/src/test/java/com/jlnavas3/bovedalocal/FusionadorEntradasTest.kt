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
    fun fusionar_distintosTotp_preservaSegundoComoTotpSecundarioEstructurado() {
        val principal = crearEntrada("1", "X (Twitter)", "usuario_demo", "Clave123").copy(
            secretoTotp = "JBSWY3DPEHPK3PXP",
            totpEmisor = "Twitter"
        )
        val secundaria = crearEntrada("2", "X (Twitter)", "usuario_demo", "Clave123").copy(
            secretoTotp = "KRSXG5CTMVRXEZLU",
            totpEmisor = "X"
        )

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        assertEquals("JBSWY3DPEHPK3PXP", fusionada.secretoTotp)
        assertEquals(1, fusionada.totpsSecundarios.size)
        assertEquals("KRSXG5CTMVRXEZLU", fusionada.totpsSecundarios.first().secreto)
        assertEquals("X", fusionada.totpsSecundarios.first().emisor)
        assertTrue("No debe degradarse a notas si se preserva como objeto estructurado", !fusionada.notas.contains("TOTP alternativo"))
    }

    @Test
    fun fusionar_distintasPasskeys_preservaSegundaComoPasskeySecundaria() {
        val p1 = com.jlnavas3.bovedalocal.data.DatosPasskey(
            rpId = "google.com",
            rpName = "Google",
            userHandle = "h1",
            credId = "cred-1",
            clavePrivada = "priv1",
            usuario = "user1@gmail.com"
        )
        val p2 = com.jlnavas3.bovedalocal.data.DatosPasskey(
            rpId = "google.com",
            rpName = "Google",
            userHandle = "h2",
            credId = "cred-2",
            clavePrivada = "priv2",
            usuario = "user2@gmail.com"
        )
        val principal = crearEntrada("1", "Google").copy(passkey = p1)
        val secundaria = crearEntrada("2", "Google").copy(passkey = p2)

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        assertEquals("cred-1", fusionada.passkey?.credId)
        assertEquals(1, fusionada.passkeysSecundarias.size)
        assertEquals("cred-2", fusionada.passkeysSecundarias.first().credId)
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

    @Test
    fun fusionar_entradaContrasenaConEntradaPasskey_conservaAmbasComoPrincipalSinNotas() {
        val entradaPassword = crearEntrada("1", "eBay", "jolunavi@gmail.com", "qt(_M2w5SvQK78N")
        val entradaPasskey = crearEntrada("2", "eBay", "jolunavi@gmail.com", "").copy(
            passkey = com.jlnavas3.bovedalocal.data.DatosPasskey(
                rpId = "ebay.com",
                rpName = "eBay",
                userHandle = "uh123",
                credId = "cred-ebay-1",
                clavePrivada = "priv-key",
                usuario = "jolunavi@gmail.com"
            )
        )

        val fusionada = FusionadorEntradas.fusionar(entradaPassword, listOf(entradaPasskey))

        assertEquals("qt(_M2w5SvQK78N", fusionada.contrasena)
        org.junit.Assert.assertNotNull(fusionada.passkey)
        assertEquals("cred-ebay-1", fusionada.passkey?.credId)
        assertTrue("No debe haber texto autogenerado en notas", fusionada.notas.isBlank())
    }

    @Test
    fun fusionar_conNotasResidualesAntiguas_limpiaNotasAutogeneradas() {
        val principal = crearEntrada("1", "eBay", "user@test.com", "Clave123").copy(
            notas = "[Passkey secundario respaldado de 'eBay': Usuario=user@test.com, CredId=abc123xyz]\n\n---\nNota legítima del usuario"
        )
        val secundaria = crearEntrada("2", "eBay", "user@test.com", "Clave123").copy(
            notas = "Otra nota legítima"
        )

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        assertTrue("La nota de passkey residual debe ser eliminada", !fusionada.notas.contains("Passkey secundario respaldado"))
        assertTrue("La nota legítima debe conservarse", fusionada.notas.contains("Nota legítima del usuario"))
        assertTrue("La nota de la secundaria debe conservarse", fusionada.notas.contains("Otra nota legítima"))
    }

    @Test
    fun fusionar_mismasPasskeys_descartaDuplicadoYConsolidaDatosEnPrincipal() {
        val p1 = com.jlnavas3.bovedalocal.data.DatosPasskey(
            rpId = "whatsapp.com",
            rpName = "whatsapp.com",
            userHandle = "h1",
            credId = "cred-wa-123",
            clavePrivada = "priv-wa",
            usuario = "",
            creadaEn = 1724247720000L
        )
        val p2 = com.jlnavas3.bovedalocal.data.DatosPasskey(
            rpId = "whatsapp.com",
            rpName = "WhatsApp",
            userHandle = "h1",
            credId = "cred-wa-123",
            clavePrivada = "priv-wa",
            usuario = "********9258",
            creadaEn = 1724247720000L
        )
        val principal = crearEntrada("1", "WhatsApp").copy(passkey = p1)
        val secundaria = crearEntrada("2", "WhatsApp").copy(passkey = p2)

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        // La principal debe conservar la credencial única
        org.junit.Assert.assertNotNull(fusionada.passkey)
        assertEquals("cred-wa-123", fusionada.passkey?.credId)
        // Debe consolidar el nombre de usuario y nombre amigable que venían en la secundaria
        assertEquals("********9258", fusionada.passkey?.usuario)
        assertEquals("WhatsApp", fusionada.passkey?.rpName)
        // La passkey secundaria duplicada DEBE ser descartada
        assertTrue("No debe haber passkeys secundarias si ambas entradas tenían la misma passkey", fusionada.passkeysSecundarias.isEmpty())
    }

    @Test
    fun fusionar_mismoTotp_descartaDuplicadoYConsolidaEmisor() {
        val principal = crearEntrada("1", "Amazon").copy(
            secretoTotp = "JBSWY3DPEHPK3PXP",
            totpEmisor = ""
        )
        val secundaria = crearEntrada("2", "Amazon").copy(
            secretoTotp = "JBSWY3DPEHPK3PXP",
            totpEmisor = "Amazon Prime"
        )

        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))

        assertEquals("JBSWY3DPEHPK3PXP", fusionada.secretoTotp)
        assertEquals("Amazon Prime", fusionada.totpEmisor)
        assertTrue("No debe haber TOTP secundario si el secreto es el mismo", fusionada.totpsSecundarios.isEmpty())
    }
}
