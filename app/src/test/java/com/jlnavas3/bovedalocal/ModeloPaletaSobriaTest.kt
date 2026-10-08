package com.jlnavas3.bovedalocal

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.crearGris
import com.jlnavas3.bovedalocal.ui.theme.restringirLuminancia
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ModeloPaletaSobriaTest {

    @Test
    fun restriccionLuminancia_modoOscuro_mantieneLimitesSeguros() {
        // En modo oscuro, superficies deben ser oscuras y permitir negro puro AMOLED (0.00 .. 0.38)
        val superfBaja = restringirLuminancia(-0.05f, esOscuro = true, esSuperficie = true)
        val superfAlta = restringirLuminancia(0.99f, esOscuro = true, esSuperficie = true)
        assertEquals(0.00f, superfBaja, 0.001f)
        assertEquals(0.38f, superfAlta, 0.001f)

        // En modo oscuro, textos deben ser claros (0.48 .. 1.00)
        val textoBajo = restringirLuminancia(0.10f, esOscuro = true, esSuperficie = false)
        val textoAlto = restringirLuminancia(1.05f, esOscuro = true, esSuperficie = false)
        assertEquals(0.48f, textoBajo, 0.001f)
        assertEquals(1.00f, textoAlto, 0.001f)
    }

    @Test
    fun restriccionLuminancia_modoClaro_mantieneLimitesSeguros() {
        // En modo claro, superficies deben ser claras (0.75 .. 1.00)
        val superfBaja = restringirLuminancia(0.10f, esOscuro = false, esSuperficie = true)
        val superfAlta = restringirLuminancia(1.05f, esOscuro = false, esSuperficie = true)
        assertEquals(0.75f, superfBaja, 0.001f)
        assertEquals(1.00f, superfAlta, 0.001f)

        // En modo claro, textos deben ser oscuros (0.00 .. 0.45)
        val textoBajo = restringirLuminancia(-0.10f, esOscuro = false, esSuperficie = false)
        val textoAlto = restringirLuminancia(0.90f, esOscuro = false, esSuperficie = false)
        assertEquals(0.00f, textoBajo, 0.001f)
        assertEquals(0.45f, textoAlto, 0.001f)
    }

    @Test
    fun exportarJson_contieneTodasLasClaves() {
        val paleta = PaletaSobriaDefaults.OSCURA
        val json = paleta.aJson()

        assertTrue(json.contains("\"modo\": \"oscuro\""))
        assertTrue(json.contains("\"fondo\":"))
        assertTrue(json.contains("\"tarjeta\":"))
        assertTrue(json.contains("\"campo\":"))
        assertTrue(json.contains("\"borde\":"))
        assertTrue(json.contains("\"textoPrincipal\":"))
        assertTrue(json.contains("\"textoSecundario\":"))
        assertTrue(json.contains("\"acento\":"))
    }

    @Test
    fun exportarCodigoKotlin_contieneDefinicionValida() {
        val paleta = PaletaSobriaDefaults.CLARA
        val codigo = paleta.aCodigoKotlin()

        assertTrue(codigo.contains("val paletaPersonalizadaClara = PaletaSobria("))
        assertTrue(codigo.contains("esOscuro = false"))
        assertTrue(codigo.contains("fondo = Color("))
        assertTrue(codigo.contains("tarjeta = Color("))
    }

    @Test
    fun crearGris_produceGrisNeutro() {
        val gris = crearGris(0.5f)
        assertEquals(gris.red, gris.green, 0.001f)
        assertEquals(gris.green, gris.blue, 0.001f)
        assertEquals(1.0f, gris.alpha, 0.001f)
    }
}
