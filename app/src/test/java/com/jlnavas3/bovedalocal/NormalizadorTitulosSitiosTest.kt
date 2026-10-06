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
        assertTrue(NormalizadorTitulosSitios.esTituloTecnico("https://accounts.google.com"))
        assertTrue(!NormalizadorTitulosSitios.esTituloTecnico("Mi Banco Personal Ahorros"))
        assertTrue(!NormalizadorTitulosSitios.esTituloTecnico("Servidor Principal Casa"))
    }
}
