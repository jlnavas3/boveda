/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.data.TipoEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalizadorDuplicadosTest {

    @Test
    fun `detecta copias exactas resultantes de importar varias veces un CSV`() {
        val entradaOriginal = Entrada(
            id = "id-1",
            titulo = "Google",
            usuario = "pepe@gmail.com",
            contrasena = "ClaveUltraSegura123!",
            urls = listOf("https://accounts.google.com"),
            notas = "Mi cuenta personal",
            creadaEn = 1000L,
            modificadaEn = 1000L
        )

        // Copia exacta producida por una segunda importación del CSV
        val copia1 = entradaOriginal.copy(
            id = "id-2",
            creadaEn = 2000L,
            modificadaEn = 2000L
        )

        // Copia exacta producida por una tercera importación del CSV
        val copia2 = entradaOriginal.copy(
            id = "id-3",
            creadaEn = 3000L,
            modificadaEn = 3000L
        )

        val entradas = listOf(entradaOriginal, copia1, copia2)
        val grupos = AnalizadorDuplicados.analizar(entradas)

        assertEquals(1, grupos.size)
        val grupo = grupos.first()
        assertEquals(TipoDuplicado.IDENTICO, grupo.tipo)
        assertEquals(3, grupo.entradas.size)
        assertEquals(2, grupo.entradasSecundarias.size)
        assertEquals("id-3", grupo.sugeridaPrincipal.id)
        assertFalse(grupo.esAppAndroid)
    }

    @Test
    fun `apps de Android diferentes con el mismo correo no se agrupan juntas como misma cuenta`() {
        val email = "usuario.ejemplo@gmail.com"

        val xiaomi = Entrada(
            id = "1",
            titulo = "account.xiaomi.com",
            usuario = email,
            contrasena = "claveXiaomi1",
            urls = listOf("android://g7zGEnZipieE6ZyBA4Bz00kxU8h0F9QTwPLL-dnRVwngI0FJWJiD3rCEJsIQ_VK064SNdxzqggnnnAqT8LI89A==@com.xiaomi.account/")
        )

        val disney = Entrada(
            id = "2",
            titulo = "disneyplus.com",
            usuario = email,
            contrasena = "claveDisney2",
            urls = listOf("android://0mxF7gRbL1Erv-6PlnR82jAP6lGaonT58okn_6fDdiqJuSFgX4aVoIyNFwhPQzm2smYltWO3qwuGjil466nBIQ==@com.disney.disneyplus/")
        )

        val netflix = Entrada(
            id = "3",
            titulo = "netflix.com",
            usuario = email,
            contrasena = "claveNetflix3",
            urls = listOf("android://Jzj5T2E45Hb33D-lk-EHZVCrb7a064dEicTwrTYQYGXO99JqE2YERhbMP1qLogwJiy87OsBzC09Gk094Z-U_hg==@com.netflix.mediaclient/")
        )

        val oralB = Entrada(
            id = "4",
            titulo = "Oral-B",
            usuario = email,
            contrasena = "claveOralB4",
            urls = listOf("android://GzD0MQtKbv-iD5-OIEB6Y-Wdi2lmHzSY4nPkWMJDPIRrowvNKisPtXENQH1tUE8GK1m0geNfpnpWwMv1U-Qkog==@com.pg.oralb.oralbapp/")
        )

        val zoom = Entrada(
            id = "5",
            titulo = "zoom.us",
            usuario = email,
            contrasena = "claveZoom5",
            urls = listOf("android://OEZ1gOxO71N06mbjKEqueuofBfcVskLfNATBY7p1RfoGoqBtYzsBCQD77iE7P8eRno3iIeED7Wd8qYHs2bmimw==@us.zoom.videomeetings/")
        )

        val entradas = listOf(xiaomi, disney, netflix, oralB, zoom)
        val grupos = AnalizadorDuplicados.analizar(entradas)

        // Ninguna debe agruparse con otra porque son servicios diferentes
        assertTrue("No deben agruparse aplicaciones de servicios diferentes", grupos.isEmpty())
    }

    @Test
    fun `copias repetidas de la misma app de Android se detectan como identicas y con esAppAndroid true`() {
        val email = "usuario.ejemplo@gmail.com"
        val solid1 = Entrada(
            id = "s1",
            titulo = "Solid Explorer File Manager",
            usuario = email,
            contrasena = "claveSolid123",
            urls = listOf("android://k2RotK_2DKoTYT4KW2ocyHl7sgK5FEQ3q7U1hLZVJpt72L_GaODrjKuyDeerPI0bHDKSQK-5vs5HyDO3ul7LDw==@pl.solidexplorer2/")
        )
        val solid2 = solid1.copy(id = "s2")

        val grupos = AnalizadorDuplicados.analizar(listOf(solid1, solid2))
        assertEquals(1, grupos.size)
        val grupo = grupos.first()
        assertEquals(TipoDuplicado.IDENTICO, grupo.tipo)
        assertTrue(grupo.esAppAndroid)
        assertTrue(grupo.claveVisual.contains("Solid Explorer"))
        assertTrue(grupo.claveVisual.contains("App Android"))
    }

    @Test
    fun `detecta misma cuenta con diferente clave`() {
        val entradaVieja = Entrada(
            id = "id-a",
            titulo = "GitHub",
            usuario = "dev123",
            contrasena = "claveAntigua1",
            urls = listOf("https://github.com/login"),
            modificadaEn = 1000L
        )

        val entradaNueva = Entrada(
            id = "id-b",
            titulo = "GitHub",
            usuario = "dev123",
            contrasena = "claveNueva2!@",
            urls = listOf("https://github.com"),
            modificadaEn = 5000L
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(entradaVieja, entradaNueva))
        assertEquals(1, grupos.size)
        assertEquals(TipoDuplicado.MISMA_CUENTA_DISTINTA_CLAVE, grupos.first().tipo)
        assertEquals("id-b", grupos.first().sugeridaPrincipal.id)
    }

    @Test
    fun `boveda sin duplicados devuelve lista vacia`() {
        val e1 = Entrada(id = "1", titulo = "Twitter", usuario = "a", contrasena = "p1", urls = listOf("https://x.com"))
        val e2 = Entrada(id = "2", titulo = "Reddit", usuario = "b", contrasena = "p2", urls = listOf("https://reddit.com"))
        val e3 = Entrada(id = "3", titulo = "Spotify", usuario = "c", contrasena = "p3", urls = listOf("https://spotify.com"))

        val grupos = AnalizadorDuplicados.analizar(listOf(e1, e2, e3))
        assertTrue(grupos.isEmpty())
    }

    @Test
    fun `fusion de entradas combina notas y URLs sin perder informacion`() {
        val principal = Entrada(
            id = "p",
            titulo = "Netflix",
            usuario = "familia@gmail.com",
            contrasena = "secreta123",
            urls = listOf("https://netflix.com"),
            notas = "Perfil 1 es de Juan"
        )

        val secundaria = Entrada(
            id = "s",
            titulo = "Netflix",
            usuario = "familia@gmail.com",
            contrasena = "secreta123",
            urls = listOf("https://netflix.com/login", "https://help.netflix.com"),
            notas = "Plan 4K UHD"
        )

        val fusionada = AnalizadorDuplicados.fusionar(principal, listOf(secundaria))

        assertTrue(fusionada.urls.contains("https://netflix.com"))
        assertTrue(fusionada.urls.contains("https://netflix.com/login"))
        assertTrue(fusionada.urls.contains("https://help.netflix.com"))
        assertTrue(fusionada.notas.contains("Perfil 1 es de Juan"))
        assertTrue(fusionada.notas.contains("Plan 4K UHD"))
    }

    @Test
    fun `fusion de entradas con contrasenas distintas guarda contrasenas alternativas en campos personalizados e historial`() {
        val principal = Entrada(
            id = "p",
            titulo = "Google",
            usuario = "user@gmail.com",
            contrasena = "claveActual",
            urls = listOf("https://google.com")
        )

        val secundaria = Entrada(
            id = "s",
            titulo = "Google (Viejo)",
            usuario = "user@gmail.com",
            contrasena = "claveAntigua999",
            urls = listOf("https://accounts.google.com")
        )

        val fusionada = AnalizadorDuplicados.fusionar(principal, listOf(secundaria))

        assertEquals("claveActual", fusionada.contrasena)
        assertTrue("No debe haber campo personalizado redundante de la clave", fusionada.camposPersonalizados.none { it.valor == "claveAntigua999" })
        assertTrue("La clave alternativa debe estar en el historial", fusionada.historialContrasenas.any { it.contrasena == "claveAntigua999" })
        assertTrue(fusionada.notas.contains("clave(s) alternativa(s) respaldada(s)"))
    }

    @Test
    fun `cuentas de google con y sin arroba gmail se agrupan pero correos institucionales no`() {
        val googleSinDominio = Entrada(
            id = "g1",
            titulo = "accounts.google.com",
            usuario = "estudiante.demo",
            contrasena = "claveGoogle123",
            urls = listOf("https://accounts.google.com")
        )
        val googleConGmail = Entrada(
            id = "g2",
            titulo = "accounts.google.com",
            usuario = "estudiante.demo@gmail.com",
            contrasena = "claveGoogle123",
            urls = listOf("https://accounts.google.com")
        )
        val googleInstitucional = Entrada(
            id = "g3",
            titulo = "accounts.google.com",
            usuario = "estudiante.demo@universidad.edu",
            contrasena = "claveGoogle123", // Misma clave que las otras
            urls = listOf("https://accounts.google.com")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(googleSinDominio, googleConGmail, googleInstitucional))

        // Solo g1 y g2 deben agruparse. g3 es un correo institucional distinto y debe quedar fuera
        assertEquals(1, grupos.size)
        val grupo = grupos.first()
        assertEquals(2, grupo.entradas.size)
        assertTrue(grupo.entradas.any { it.id == "g1" })
        assertTrue(grupo.entradas.any { it.id == "g2" })
        assertFalse("El correo institucional no debe mezclarse con la cuenta de gmail", grupo.entradas.any { it.id == "g3" })
        // Sugerida principal debe ser la que tiene el correo completo con @
        assertEquals("g2", grupo.sugeridaPrincipal.id)
    }

    @Test
    fun `cuentas con usuarios diferentes en instagram no se agrupan aunque tengan la misma contrasena`() {
        val u1 = Entrada(
            id = "ig1",
            titulo = "instagram.com",
            usuario = "usuario_uno",
            contrasena = "mismaClaveParaTodos",
            urls = listOf("https://instagram.com")
        )
        val u2 = Entrada(
            id = "ig2",
            titulo = "instagram.com",
            usuario = "usuario_dos",
            contrasena = "mismaClaveParaTodos",
            urls = listOf("https://instagram.com")
        )
        val u3 = Entrada(
            id = "ig3",
            titulo = "instagram.com",
            usuario = "usuario_tres",
            contrasena = "mismaClaveParaTodos",
            urls = listOf("https://instagram.com")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(u1, u2, u3))

        // No son duplicados, son 3 cuentas independientes que comparten contraseña (salud de la bóveda)
        assertTrue("No deben considerarse duplicados cuentas con nombres de usuario diferentes", grupos.isEmpty())
    }

    @Test
    fun `fusion de entrada passkey con entrada contrasena conserva ambas credenciales`() {
        val entradaPasskey = Entrada(
            id = "pk1",
            tipo = TipoEntrada.PASSKEY,
            titulo = "PayPal",
            usuario = "usuario.ejemplo@gmail.com",
            contrasena = "",
            urls = listOf("paypal.com"),
            passkey = DatosPasskey(
                rpId = "paypal.com",
                rpName = "PayPal",
                userHandle = "user123",
                credId = "cred-abc",
                clavePrivada = "priv-xyz"
            )
        )

        val entradaPassword = Entrada(
            id = "pwd1",
            tipo = TipoEntrada.LOGIN,
            titulo = "paypal.com",
            usuario = "usuario.ejemplo@gmail.com",
            contrasena = "SuperSecretaPayPal2026!",
            urls = listOf("https://www.paypal.com")
        )

        val fusionada = AnalizadorDuplicados.fusionar(entradaPasskey, listOf(entradaPassword))

        assertEquals("SuperSecretaPayPal2026!", fusionada.contrasena)
        assertNotNull(fusionada.passkey)
        assertEquals("cred-abc", fusionada.passkey?.credId)
        assertEquals(1, fusionada.urls.size)
        assertEquals("https://www.paypal.com", fusionada.urls.first())
    }

    @Test
    fun `entradas con contrasena igual o vacia pero distinto TOTP no son identicas`() {
        val e1 = Entrada(
            id = "1",
            titulo = "X (Twitter)",
            usuario = "usuario_ejemplo",
            contrasena = "",
            secretoTotp = "JBSWY3DPEHPK3PXP"
        )
        val e2 = Entrada(
            id = "2",
            titulo = "X (Twitter)",
            usuario = "usuario_ejemplo",
            contrasena = "",
            secretoTotp = "KRSXG5CTMVRXEZLU"
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(e1, e2))
        assertFalse(
            "Entradas con TOTP distinto nunca deben agruparse como IDENTICO",
            grupos.any { it.tipo == TipoDuplicado.IDENTICO }
        )
    }

    @Test
    fun `entradas con contrasena igual pero distinto Passkey credId no son identicas`() {
        val e1 = Entrada(
            id = "1",
            titulo = "Google",
            usuario = "test@gmail.com",
            contrasena = "clave123",
            passkey = DatosPasskey("google.com", "Google", "user1", "cred-1", "priv-1")
        )
        val e2 = Entrada(
            id = "2",
            titulo = "Google",
            usuario = "test@gmail.com",
            contrasena = "clave123",
            passkey = DatosPasskey("google.com", "Google", "user1", "cred-2", "priv-2")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(e1, e2))
        assertFalse(
            "Entradas con Passkey distinto nunca deben agruparse como IDENTICO",
            grupos.any { it.tipo == TipoDuplicado.IDENTICO }
        )
    }

    @Test
    fun `entradas de Aldiko con app Android, portal SSO de Google y web se detectan como duplicadas`() {
        val email = "usuario.ejemplo@gmail.com"
        val entradaApp = Entrada(
            id = "a1",
            titulo = "Aldiko",
            usuario = email,
            contrasena = "claveAldiko",
            urls = listOf("android://hash@com.aldiko.android/")
        )
        val entradaSso = Entrada(
            id = "a2",
            titulo = "Aldiko",
            usuario = email,
            contrasena = "claveAldiko",
            urls = listOf("https://accounts.google.com")
        )
        val entradaWeb = Entrada(
            id = "a3",
            titulo = "Aldiko",
            usuario = email,
            contrasena = "claveAldiko",
            urls = listOf("https://aldiko.com")
        )
        val entradaSimple = Entrada(
            id = "a4",
            titulo = "Aldiko",
            usuario = email,
            contrasena = "claveAldiko",
            urls = emptyList()
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(entradaApp, entradaSso, entradaWeb, entradaSimple))
        assertEquals(1, grupos.size)
        val grupo = grupos.first()
        assertEquals(4, grupo.entradas.size)
        assertTrue(grupo.claveVisual.contains("Aldiko"))
    }

    @Test
    fun `cuentas de microsoft con dominios de correo distintos como outlook y hotmail no se consideran duplicados`() {
        val outlook = Entrada(
            id = "m1",
            titulo = "Microsoft",
            usuario = "cuenta.demo@outlook.com",
            contrasena = "",
            urls = listOf("https://login.microsoft.com"),
            passkey = com.jlnavas3.bovedalocal.data.DatosPasskey("login.microsoft.com", "Microsoft", "cuenta.demo@outlook.com", "cred-1", "priv-1")
        )
        val hotmail = Entrada(
            id = "m2",
            titulo = "Microsoft",
            usuario = "cuenta.demo@hotmail.com",
            contrasena = "claveHotmail123",
            urls = listOf("https://signup.live.com/signup")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(outlook, hotmail))
        assertTrue("Cuentas con dominios de correo diferentes (@outlook.com vs @hotmail.com) nunca deben agruparse", grupos.isEmpty())
    }

    @Test
    fun `detecta como duplicados de variante de usuario entradas con misma clave y servicio donde una no tiene usuario`() {
        val entradaConUsuario = Entrada(
            id = "claro-1",
            titulo = "Claro Video",
            usuario = "usuario.ejemplo@gmail.com",
            contrasena = "ClaveSecreta123*",
            urls = listOf("https://www.clarovideo.com/")
        )
        val entradaSinUsuario = Entrada(
            id = "claro-2",
            titulo = "Claro Video",
            usuario = "",
            contrasena = "ClaveSecreta123*",
            urls = listOf("https://www.clarovideo.com/ecuador/login")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(entradaConUsuario, entradaSinUsuario))
        assertEquals(1, grupos.size)
        val grupo = grupos.first()
        assertEquals(TipoDuplicado.VARIANTE_USUARIO, grupo.tipo)
        assertEquals(2, grupo.entradas.size)
        assertEquals("claro-1", grupo.sugeridaPrincipal.id)
        assertEquals("usuario.ejemplo@gmail.com", grupo.sugeridaPrincipal.usuario)
    }
}



