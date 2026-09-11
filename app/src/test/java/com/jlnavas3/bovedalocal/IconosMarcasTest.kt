package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.util.IconosMarcas
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class IconosMarcasTest {

    @Test
    fun `resuelve iconos para URLs y dominios conocidos`() {
        assertEquals(R.drawable.ic_marca_google, IconosMarcas.buscar("https://accounts.google.com"))
        assertEquals(R.drawable.ic_marca_github, IconosMarcas.buscar("https://github.com/login"))
        assertEquals(R.drawable.ic_marca_spotify, IconosMarcas.buscar("https://open.spotify.com"))
        assertEquals(R.drawable.ic_marca_netflix, IconosMarcas.buscar("netflix.com"))
        assertEquals(R.drawable.ic_marca_steam, IconosMarcas.buscar("https://store.steampowered.com"))
        assertEquals(R.drawable.ic_marca_amazon, IconosMarcas.buscar("https://amazon.co.uk"))
        assertEquals(R.drawable.ic_marca_apple, IconosMarcas.buscar("https://appleid.apple.com"))
        assertEquals(R.drawable.ic_marca_paypal, IconosMarcas.buscar("https://www.paypal.com"))
    }

    @Test
    fun `resuelve iconos a partir del titulo si no hay url`() {
        assertEquals(R.drawable.ic_marca_discord, IconosMarcas.buscar("", "Discord Principal"))
        assertEquals(R.drawable.ic_marca_reddit, IconosMarcas.buscar("", "Cuenta de Reddit"))
        assertEquals(R.drawable.ic_marca_telegram, IconosMarcas.buscar("", "Telegram"))
        assertEquals(R.drawable.ic_marca_openai, IconosMarcas.buscar("", "ChatGPT"))
    }

    @Test
    fun `retorna nulo para dominios desconocidos`() {
        assertNull(IconosMarcas.buscar("https://mi-banco-local-personal.es", "Mi Banco"))
        assertNull(IconosMarcas.buscar("intranet.empresa.local", "Servidor Local"))
    }
}
