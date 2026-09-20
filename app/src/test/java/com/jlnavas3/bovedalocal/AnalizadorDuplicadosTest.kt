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
        val email = "jolunavi@gmail.com"

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
        val email = "jolunavi@gmail.com"
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
        assertTrue(fusionada.camposPersonalizados.any { it.valor == "claveAntigua999" && it.esSensible })
        assertTrue(fusionada.historialContrasenas.any { it.contrasena == "claveAntigua999" })
        assertTrue(fusionada.notas.contains("clave(s) alternativa(s) guardada(s)"))
    }

    @Test
    fun `cuentas de google con y sin arroba gmail se agrupan pero correos institucionales no`() {
        val googleSinDominio = Entrada(
            id = "g1",
            titulo = "accounts.google.com",
            usuario = "jlnavas3.utpl.edu.ec",
            contrasena = "claveGoogle123",
            urls = listOf("https://accounts.google.com")
        )
        val googleConGmail = Entrada(
            id = "g2",
            titulo = "accounts.google.com",
            usuario = "jlnavas3.utpl.edu.ec@gmail.com",
            contrasena = "claveGoogle123",
            urls = listOf("https://accounts.google.com")
        )
        val googleInstitucional = Entrada(
            id = "g3",
            titulo = "accounts.google.com",
            usuario = "jlnavas3@utpl.edu.ec",
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
            usuario = "curtain.cross",
            contrasena = "mismaClaveParaTodos",
            urls = listOf("https://instagram.com")
        )
        val u2 = Entrada(
            id = "ig2",
            titulo = "instagram.com",
            usuario = "fregenavi",
            contrasena = "mismaClaveParaTodos",
            urls = listOf("https://instagram.com")
        )
        val u3 = Entrada(
            id = "ig3",
            titulo = "instagram.com",
            usuario = "its.spoky",
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
            usuario = "jolunavi@gmail.com",
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
            usuario = "jolunavi@gmail.com",
            contrasena = "SuperSecretaPayPal2026!",
            urls = listOf("https://www.paypal.com")
        )

        val fusionada = AnalizadorDuplicados.fusionar(entradaPasskey, listOf(entradaPassword))

        assertEquals("SuperSecretaPayPal2026!", fusionada.contrasena)
        assertNotNull(fusionada.passkey)
        assertEquals("cred-abc", fusionada.passkey?.credId)
        assertEquals(TipoEntrada.LOGIN, fusionada.tipo)
        assertTrue(fusionada.urls.contains("paypal.com"))
        assertTrue(fusionada.urls.contains("https://www.paypal.com"))
    }
}



