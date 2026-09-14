package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.GeneradorKitEmergencia
import com.jlnavas3.bovedalocal.util.GeneradorKitEmergencia.OpcionesKit
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KitEmergenciaTest {

    private val entradas = listOf(
        Entrada(
            id = "1",
            titulo = "Banco Santander",
            usuario = "es12345",
            contrasena = "ClaveSecreta9!",
            favorito = true
        ),
        Entrada(
            id = "2",
            titulo = "Netflix",
            usuario = "familia@example.com",
            contrasena = "Peliculas2026",
            favorito = false
        )
    )

    @Test
    fun `generarTexto respeta opcion de ocultar contrasenas`() {
        val texto = GeneradorKitEmergencia.generarTexto(entradas, OpcionesKit(incluirContrasenas = false))
        assertTrue(texto.contains("Banco Santander"))
        assertTrue(texto.contains("Netflix"))
        assertFalse(texto.contains("ClaveSecreta9!"))
        assertFalse(texto.contains("Peliculas2026"))
        assertTrue(texto.contains("Clave   :"))
        assertFalse(texto.contains("ANOTAR MANUALMENTE"))
    }

    @Test
    fun `generarTexto incluye contrasenas cuando se solicita`() {
        val texto = GeneradorKitEmergencia.generarTexto(entradas, OpcionesKit(incluirContrasenas = true))
        assertTrue(texto.contains("ClaveSecreta9!"))
        assertTrue(texto.contains("Peliculas2026"))
    }

    @Test
    fun `filtrarEntradas soloFavoritos filtra correctamente`() {
        val filtradas = GeneradorKitEmergencia.filtrarEntradas(entradas, OpcionesKit(soloFavoritos = true))
        assertTrue(filtradas.size == 1)
        assertTrue(filtradas.first().titulo == "Banco Santander")
    }

    @Test
    fun `generarHtml produce estructura valida`() {
        val html = GeneradorKitEmergencia.generarHtml(entradas, OpcionesKit())
        assertTrue(html.contains("<!DOCTYPE html>"))
        assertTrue(html.contains("Banco Santander"))
        assertTrue(html.contains("Netflix"))
        assertFalse(html.contains("[Anote manualmente]"))
        assertTrue(html.contains("<td>&nbsp;</td>"))
        assertFalse(html.contains("ClaveSecreta9!"))
    }

    @Test
    fun `generarHtml incluye contrasenas visibles cuando se solicita`() {
        val html = GeneradorKitEmergencia.generarHtml(entradas, OpcionesKit(incluirContrasenas = true))
        assertTrue(html.contains("<code>ClaveSecreta9!</code>"))
        assertTrue(html.contains("<code>Peliculas2026</code>"))
        assertFalse(html.contains("[Anote manualmente]"))
    }

    @Test
    fun `generarHtml escapa correctamente caracteres especiales HTML en contrasenas y titulos`() {
        val entradasEspeciales = listOf(
            Entrada(
                id = "3",
                titulo = "Sitio <Seguro> & Co",
                usuario = "user\"1\"@mail.com",
                contrasena = "P@ss<w0rd>&99'!",
                notas = "Nota con <etiqueta> & detalle"
            )
        )
        val html = GeneradorKitEmergencia.generarHtml(entradasEspeciales, OpcionesKit(incluirContrasenas = true, incluirNotas = true))
        assertTrue(html.contains("Sitio &lt;Seguro&gt; &amp; Co"))
        assertTrue(html.contains("user&quot;1&quot;@mail.com"))
        assertTrue(html.contains("<code>P@ss&lt;w0rd&gt;&amp;99&#39;!</code>"))
        assertTrue(html.contains("Nota con &lt;etiqueta&gt; &amp; detalle"))
        assertFalse(html.contains("<w0rd>"))
    }

    @Test
    fun `extrae y muestra datos completos de Red Wi-Fi en HTML y Texto`() {
        val wifi = Entrada(
            id = "wifi-1",
            tipo = com.jlnavas3.bovedalocal.data.TipoEntrada.WIFI,
            titulo = "Wi-Fi Oficina",
            camposPersonalizados = listOf(
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "Nombre de red (SSID)", valor = "Oficina_Fibra_5G"),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "Contraseña Wi-Fi", valor = "W1f1_Clav3_S3cr3ta!", esSensible = true),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "Tipo de seguridad", valor = "WPA2 / WPA3")
            )
        )

        // Modo con contraseñas visibles
        val htmlVisible = GeneradorKitEmergencia.generarHtml(listOf(wifi), OpcionesKit(incluirContrasenas = true))
        assertTrue(htmlVisible.contains("Wi-Fi Oficina"))
        assertTrue(htmlVisible.contains("Red Wi-Fi"))
        assertTrue(htmlVisible.contains("SSID: Oficina_Fibra_5G (WPA2 / WPA3)"))
        assertTrue(htmlVisible.contains("<code>W1f1_Clav3_S3cr3ta!</code>"))

        val textoVisible = GeneradorKitEmergencia.generarTexto(listOf(wifi), OpcionesKit(incluirContrasenas = true))
        assertTrue(textoVisible.contains("Wi-Fi Oficina (Red Wi-Fi)"))
        assertTrue(textoVisible.contains("SSID: Oficina_Fibra_5G (WPA2 / WPA3)"))
        assertTrue(textoVisible.contains("Clave   : W1f1_Clav3_S3cr3ta!"))

        // Modo seguro sin contraseñas
        val htmlOculto = GeneradorKitEmergencia.generarHtml(listOf(wifi), OpcionesKit(incluirContrasenas = false))
        assertTrue(htmlOculto.contains("SSID: Oficina_Fibra_5G"))
        assertFalse(htmlOculto.contains("W1f1_Clav3_S3cr3ta!"))
        assertFalse(htmlOculto.contains("[Anote manualmente]"))
        assertTrue(htmlOculto.contains("<td>&nbsp;</td>"))
    }

    @Test
    fun `extrae y formatea Tarjeta Bancaria y Cuenta en Kit de Emergencia`() {
        val tarjeta = Entrada(
            id = "card-1",
            tipo = com.jlnavas3.bovedalocal.data.TipoEntrada.TARJETA,
            titulo = "Visa Débito",
            camposPersonalizados = listOf(
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "Titular", valor = "Carlos Mendoza"),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "Número de tarjeta", valor = "4532 9876 5432 1098"),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "Vencimiento", valor = "11/29"),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "CVV", valor = "889", esSensible = true),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "PIN de tarjeta", valor = "4321", esSensible = true)
            )
        )

        val cuenta = Entrada(
            id = "bank-1",
            tipo = com.jlnavas3.bovedalocal.data.TipoEntrada.CUENTA_BANCARIA,
            titulo = "Ahorros Banco Pichincha",
            camposPersonalizados = listOf(
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "Banco / Entidad", valor = "Banco Pichincha"),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "Titular de la cuenta", valor = "Carlos Mendoza"),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "Número de cuenta / IBAN", valor = "2201994821"),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(etiqueta = "SWIFT / CBU / CLABE", valor = "PICHNECQXXX")
            )
        )

        val html = GeneradorKitEmergencia.generarHtml(listOf(tarjeta, cuenta), OpcionesKit(incluirContrasenas = true))
        assertTrue(html.contains("Carlos Mendoza"))
        assertTrue(html.contains("4532 9876 5432 1098"))
        assertTrue(html.contains("Venc: 11/29 • CVV: 889 • PIN: 4321"))
        assertTrue(html.contains("Banco Pichincha"))
        assertTrue(html.contains("Cuenta/IBAN: 2201994821 • SWIFT: PICHNECQXXX"))
    }
}
