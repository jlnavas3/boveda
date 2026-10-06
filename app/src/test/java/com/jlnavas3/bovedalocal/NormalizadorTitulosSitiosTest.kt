package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.ModoFormatoTitulos
import com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NormalizadorTitulosSitiosTest {

    private val prefijos = AjustesDefaults.NormalizacionTitulos.PREFIJOS_SUBDOMINIOS

    @Test
    fun recortarPrefijosTecnicos_serviciosReales() {
        assertEquals("Lenovo", NormalizadorTitulosSitios.extraerNombreBase("https://account.lenovo.com/ec/es/signin", "account.lenovo.com", prefijos))
        assertEquals("Clarivate", NormalizadorTitulosSitios.extraerNombreBase("https://access.clarivate.com/register", "access.clarivate.com", prefijos))
        assertEquals("Envato", NormalizadorTitulosSitios.extraerNombreBase("https://account.envato.com/sign_in", "account.envato.com", prefijos))
        assertEquals("JetBrains", NormalizadorTitulosSitios.extraerNombreBase("https://account.jetbrains.com/login", "account.jetbrains.com", prefijos))
        assertEquals("Microsoft", NormalizadorTitulosSitios.extraerNombreBase("https://account.live.com/recover", "account.live.com", prefijos))
        assertEquals("Google", NormalizadorTitulosSitios.extraerNombreBase("https://accounts.google.com/signin/v2/sl/pwd", "accounts.google.com", prefijos))
        assertEquals("Crunchyroll", NormalizadorTitulosSitios.extraerNombreBase("https://sso-v2.crunchyroll.com/es/login", "crunchyroll.com", prefijos))
        assertEquals("SRI Ecuador", NormalizadorTitulosSitios.extraerNombreBase("https://srienlinea.sri.gob.ec/tuportal-internet/", "sri.gob.ec", prefijos))
    }

    @Test
    fun redesLocales_routersYServidoresRFC1918() {
        assertEquals("Router (192.168.1.1)", NormalizadorTitulosSitios.extraerNombreBase("http://192.168.1.1/", "http://192.168.1.1", prefijos))
        assertEquals("Router (192.168.0.1)", NormalizadorTitulosSitios.extraerNombreBase("http://192.168.0.1/", "http://192.168.0.1", prefijos))
        assertEquals("Router (192.168.100.1)", NormalizadorTitulosSitios.extraerNombreBase("http://192.168.100.1/", "http://192.168.100.1", prefijos))
        assertEquals("phpMyAdmin (192.168.0.200)", NormalizadorTitulosSitios.extraerNombreBase("http://192.168.0.200/phpmyadmin/", "http://192.168.0.200", prefijos))
        assertEquals("Servidor (192.168.1.4:8001)", NormalizadorTitulosSitios.extraerNombreBase("http://192.168.1.4:8001/server_privileges.php", "http://192.168.1.4:8001", prefijos))
        assertEquals("phpMyAdmin", NormalizadorTitulosSitios.extraerNombreBase("http://localhost/phpmyadmin/", "localhost", prefijos))
        // IP Pública: nunca debe truncarse al primer octeto
        assertEquals("Servidor (186.4.146.197:9904)", NormalizadorTitulosSitios.extraerNombreBase("http://186.4.146.197:9904/", "http://186.4.146.197:9904", prefijos))
    }

    @Test
    fun reversibilidad_titulosGeneradosSonModificables() {
        val base = "Google"
        val usuario = "central.paramotos"
        // Formato original o técnico
        assertTrue(NormalizadorTitulosSitios.esTituloGeneradoOModificable("accounts.google.com", base, usuario))
        // Formato ya normalizado en explícito
        assertTrue(NormalizadorTitulosSitios.esTituloGeneradoOModificable("Google (central.paramotos)", base, usuario))
        // Formato ya normalizado en minimalista
        assertTrue(NormalizadorTitulosSitios.esTituloGeneradoOModificable("Google", base, usuario))
        // Título personalizado por el usuario no debe considerarse generado
        assertTrue(!NormalizadorTitulosSitios.esTituloGeneradoOModificable("Mi Cuenta de Trabajo", base, usuario))
    }

    @Test
    fun prefijosDinamicos_agregarNuevoPrefijoSinHardcoding() {
        val prefijosPersonalizados = prefijos + listOf("myportal", "securezone")
        val resultado = NormalizadorTitulosSitios.extraerNombreBase(
            urlODominio = "https://myportal.empresa.com/login",
            rawTitulo = "myportal.empresa.com",
            prefijosConfigurados = prefijosPersonalizados
        )
        assertEquals("Empresa", resultado)
    }

    @Test
    fun formateoColisiones_modosMinimalistaYExplicitos() {
        val base = "Instagram"
        val usuario = "jolunavi"

        // Sin colisión (cuenta única)
        assertEquals("Instagram", NormalizadorTitulosSitios.generarTituloFinal(base, usuario, ModoFormatoTitulos.EXPLICITO_PARENTESIS, tieneColision = false))

        // Con colisión
        assertEquals("Instagram", NormalizadorTitulosSitios.generarTituloFinal(base, usuario, ModoFormatoTitulos.MINIMALISTA, tieneColision = true))
        assertEquals("Instagram (jolunavi)", NormalizadorTitulosSitios.generarTituloFinal(base, usuario, ModoFormatoTitulos.EXPLICITO_PARENTESIS, tieneColision = true))
        assertEquals("Instagram · jolunavi", NormalizadorTitulosSitios.generarTituloFinal(base, usuario, ModoFormatoTitulos.EXPLICITO_SEPARADOR, tieneColision = true))
    }

    @Test
    fun deteccionTitulosTecnicos() {
        assertTrue(NormalizadorTitulosSitios.esTituloTecnico("account.lenovo.com"))
        assertTrue(NormalizadorTitulosSitios.esTituloTecnico("http://192.168.1.1"))
        assertTrue(NormalizadorTitulosSitios.esTituloTecnico("http://186.4.146.197:9904"))
        assertTrue(NormalizadorTitulosSitios.esTituloTecnico("https://accounts.google.com"))
        assertTrue(!NormalizadorTitulosSitios.esTituloTecnico("Mi Banco Personal Ahorros"))
        assertTrue(!NormalizadorTitulosSitios.esTituloTecnico("Servidor Principal Casa"))
    }

    @Test
    fun agrupacionesListadoPrincipal_descartanSubdominiosConfigurados() {
        val entradaGoogle = com.jlnavas3.bovedalocal.data.Entrada(
            id = "g1",
            titulo = "accounts.google.com",
            urls = listOf("https://accounts.google.com/signin")
        )
        val entradaLenovo = com.jlnavas3.bovedalocal.data.Entrada(
            id = "l1",
            titulo = "account.lenovo",
            urls = listOf("https://account.lenovo.com/ec/es/signin")
        )
        val entradaRouter = com.jlnavas3.bovedalocal.data.Entrada(
            id = "r1",
            titulo = "192.168.1.1",
            urls = listOf("http://192.168.1.1/")
        )
        val claveGoogle = com.jlnavas3.bovedalocal.util.claveAgrupacionSitio(entradaGoogle, prefijos)
        val claveLenovo = com.jlnavas3.bovedalocal.util.claveAgrupacionSitio(entradaLenovo, prefijos)
        val claveRouter = com.jlnavas3.bovedalocal.util.claveAgrupacionSitio(entradaRouter, prefijos)

        assertEquals("Google", claveGoogle)
        assertEquals("Lenovo", claveLenovo)
        assertEquals("Router (192.168.1.1)", claveRouter)
    }

    @Test
    fun tldsDinamicos_seRecortanYDetectanCorrectamente() {
        val tlds = listOf("lan", "tech", "local", "co")

        // 1. Podar TLD
        assertEquals("servidor", NormalizadorTitulosSitios.podarTlds("servidor.lan", tlds))
        assertEquals("mi-empresa", NormalizadorTitulosSitios.podarTlds("mi-empresa.tech", tlds))
        assertEquals("intranet", NormalizadorTitulosSitios.podarTlds("intranet.local", tlds))

        // 2. Extraer nombre base limpio
        val nombreTech = NormalizadorTitulosSitios.extraerNombreBase(
            urlODominio = "https://innovacion.tech",
            tldsConfigurados = tlds
        )
        assertEquals("Innovacion", nombreTech)

        val nombreLan = NormalizadorTitulosSitios.extraerNombreBase(
            urlODominio = "http://nas-server.lan",
            tldsConfigurados = tlds
        )
        assertEquals("Nas Server", nombreLan)

        // 3. Título técnico con TLD personalizado
        assertTrue(NormalizadorTitulosSitios.esTituloTecnico("empresa.tech", tldsConfigurados = tlds))
        assertTrue(NormalizadorTitulosSitios.esTituloTecnico("portal.lan", tldsConfigurados = tlds))

        // 4. esTituloGeneradoOModificable con TLD personalizado
        assertTrue(NormalizadorTitulosSitios.esTituloGeneradoOModificable("empresa.tech", "Empresa", "admin", tldsConfigurados = tlds))
    }

    @Test
    fun grupoTitulosSitio_idUnicoPrevieneColisiones() {
        val grupo1 = com.jlnavas3.bovedalocal.data.GrupoTitulosSitio(
            id = "localhost_3000",
            dominioClave = "localhost:3000",
            nombreSugerido = "Localhost :3000"
        )
        val grupo2 = com.jlnavas3.bovedalocal.data.GrupoTitulosSitio(
            id = "localhost_8080",
            dominioClave = "localhost:8080",
            nombreSugerido = "Localhost :8080"
        )
        org.junit.Assert.assertNotEquals(grupo1.id, grupo2.id)
    }
}
