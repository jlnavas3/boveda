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
    fun `retorna nulo para dominios desconocidos y previene falsos positivos`() {
        assertNull(IconosMarcas.buscar("https://mi-banco-local-personal.es", "Mi Banco"))
        assertNull(IconosMarcas.buscar("intranet.empresa.local", "Servidor Local"))

        // Casos que antes causaban falsos positivos por contener "login" (Logitech) o "reset" (ESET):
        assertNull(IconosMarcas.buscar("https://micuenta.aeropost.com/en/login", "aeropost.com"))
        assertNull(IconosMarcas.buscar("http://192.168.1.1/login.html", "http://192.168.1.1"))
        assertNull(IconosMarcas.buscar("https://flemail.flipboard.com/reset_password", "flipboard.com"))
        assertNull(IconosMarcas.buscar("http://localhost/cms_3/login", "localhost"))
        assertNull(IconosMarcas.buscar("https://apps.registrocivil.gob.ec/portalCiudadano/login.jsf", "registrocivil.gob.ec"))
        assertNull(IconosMarcas.buscar("http://solofrenos.guru/wp-login.php", "solofrenos.guru"))
        assertNull(IconosMarcas.buscar("https://login.utpl.cedia.edu.ec/", "cedia.edu.ec"))

        // Subcadenas en títulos que no deben coincidir con marcas (ej: "claro" en "declaración", "live" en "delivery"):
        assertNull(IconosMarcas.buscar("https://empresa.com", "Declaración de bienes"))
        assertNull(IconosMarcas.buscar("https://restaurante.com", "Entrega a domicilio Delivery"))
        assertNull(IconosMarcas.buscar("android://hash@com.devahead.wifipcfileexplorerfree/", "WifiPCFileExplorerFree.x"))
    }

    @Test
    fun `resuelve correctamente paquetes Android y servicios locales e internacionales`() {
        assertEquals(R.drawable.ic_marca_adobe, IconosMarcas.buscar("https://adobeid.services.adobe.com/reset/es_LA/123", "adobe.com"))
        assertEquals(R.drawable.ic_marca_xiaomi, IconosMarcas.buscar("android://hash@com.xiaomi.account/", "account.xiaomi.com"))
        assertEquals(R.drawable.ic_marca_pichincha, IconosMarcas.buscar("android://hash@com.yellowpepper.pichincha/", "Banco Pichincha"))
        assertEquals(R.drawable.ic_marca_pichincha, IconosMarcas.buscar("https://bancaweb.pichincha.com", "pichincha.com"))
        assertEquals(R.drawable.ic_marca_claro, IconosMarcas.buscar("android://hash@com.claroecuador.miclaro/", "Mi Claro Ecuador"))
        assertEquals(R.drawable.ic_marca_sri, IconosMarcas.buscar("https://sri.gob.ec/declaraciones", "Declaración de Impuestos SRI"))
        assertEquals(R.drawable.ic_marca_cnt, IconosMarcas.buscar("https://micnt.com.ec/", "micnt.com.ec"))
        assertEquals(R.drawable.ic_marca_x, IconosMarcas.buscar("https://x.com", "x.com"))
        assertEquals(R.drawable.ic_marca_x, IconosMarcas.buscar("https://twitter.com", "twitter.com"))
        assertEquals(R.drawable.ic_marca_logitech, IconosMarcas.buscar("https://logitech.com", "Logitech"))
    }
}
