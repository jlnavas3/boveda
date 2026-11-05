/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ResolvedorAccionesDeslizamientoEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResolvedorAccionesDeslizamientoEntradaTest {

    @Test
    fun `deslizamiento en IDENTIDAD resuelve copiar documento y titular`() {
        val entrada = Entrada(
            id = "id-1",
            tipo = TipoEntrada.IDENTIDAD,
            titulo = "Cédula personal",
            camposPersonalizados = listOf(
                CampoPersonalizado(etiqueta = "Tipo de documento", valor = "CI"),
                CampoPersonalizado(etiqueta = "Número de documento", valor = "1712345678"),
                CampoPersonalizado(etiqueta = "Nombre completo", valor = "Juan Pérez")
            )
        )

        var copiadoEtiqueta = ""
        var copiadoValor = ""
        var copiadoSensible = false

        val (izq, der) = ResolvedorAccionesDeslizamientoEntrada.resolver(
            entrada = entrada,
            alCopiar = { etq, valr, sens ->
                copiadoEtiqueta = etq
                copiadoValor = valr
                copiadoSensible = sens
            }
        )

        assertNotNull(izq)
        assertNotNull(der)
        assertTrue(izq!!.texto.contains("CI"))
        assertTrue(der!!.texto.contains("Titular"))

        izq.alEjecutar()
        assertEquals("CI", copiadoEtiqueta)
        assertEquals("1712345678", copiadoValor)
        assertTrue(copiadoSensible)

        der.alEjecutar()
        assertEquals("Titular", copiadoEtiqueta)
        assertEquals("Juan Pérez", copiadoValor)
    }

    @Test
    fun `deslizamiento en TARJETA resuelve copiar numero y cvv`() {
        val entrada = Entrada(
            id = "id-2",
            tipo = TipoEntrada.TARJETA,
            titulo = "Visa Débito",
            camposPersonalizados = listOf(
                CampoPersonalizado(etiqueta = "Número de tarjeta", valor = "4532 0000 1111 2222"),
                CampoPersonalizado(etiqueta = "CVV", valor = "123")
            )
        )

        var copiadoEtiqueta = ""
        var copiadoValor = ""

        val (izq, der) = ResolvedorAccionesDeslizamientoEntrada.resolver(
            entrada = entrada,
            alCopiar = { etq, valr, _ ->
                copiadoEtiqueta = etq
                copiadoValor = valr
            }
        )

        assertNotNull(izq)
        assertNotNull(der)
        assertTrue(izq!!.texto.contains("Tarjeta"))
        assertTrue(der!!.texto.contains("CVV"))

        izq.alEjecutar()
        assertEquals("4532 0000 1111 2222", copiadoValor)

        der.alEjecutar()
        assertEquals("CVV", copiadoEtiqueta)
        assertEquals("123", copiadoValor)
    }

    @Test
    fun `deslizamiento en WIFI resuelve copiar clave y red ssid`() {
        val entrada = Entrada(
            id = "id-3",
            tipo = TipoEntrada.WIFI,
            titulo = "Wifi Casa",
            contrasena = "clave_wifi_secreta",
            camposPersonalizados = listOf(
                CampoPersonalizado(etiqueta = "Nombre de red (SSID)", valor = "MiFibra_5G")
            )
        )

        var copiadoEtiqueta = ""
        var copiadoValor = ""

        val (izq, der) = ResolvedorAccionesDeslizamientoEntrada.resolver(
            entrada = entrada,
            alCopiar = { etq, valr, _ ->
                copiadoEtiqueta = etq
                copiadoValor = valr
            }
        )

        assertNotNull(izq)
        assertNotNull(der)
        assertTrue(izq!!.texto.contains("Red"))
        assertTrue(der!!.texto.contains("Contraseña"))

        izq.alEjecutar()
        assertEquals("Red Wi-Fi", copiadoEtiqueta)
        assertEquals("MiFibra_5G", copiadoValor)

        der.alEjecutar()
        assertEquals("Contraseña Wi-Fi", copiadoEtiqueta)
        assertEquals("clave_wifi_secreta", copiadoValor)
    }

    @Test
    fun `deslizamiento en NOTA resuelve copiar contenido y null a la derecha`() {
        val entrada = Entrada(
            id = "id-4",
            tipo = TipoEntrada.NOTA,
            titulo = "Mis notas",
            notas = "Contenido de la nota ultra secreta"
        )

        var copiadoValor = ""

        val (izq, der) = ResolvedorAccionesDeslizamientoEntrada.resolver(
            entrada = entrada,
            alCopiar = { _, valr, _ ->
                copiadoValor = valr
            }
        )

        assertNotNull(izq)
        assertNull(der)
        assertTrue(izq!!.texto.contains("Nota"))

        izq.alEjecutar()
        assertEquals("Contenido de la nota ultra secreta", copiadoValor)
    }

    @Test
    fun `deslizamiento en LOGIN resuelve copiar usuario y copiar contrasena`() {
        val entrada = Entrada(
            id = "id-5",
            tipo = TipoEntrada.LOGIN,
            titulo = "GitHub",
            usuario = "usuario_test",
            contrasena = "pass123"
        )

        var copiadoEtiqueta = ""
        var copiadoValor = ""

        val (izq, der) = ResolvedorAccionesDeslizamientoEntrada.resolver(
            entrada = entrada,
            alCopiar = { etq, valr, _ ->
                copiadoEtiqueta = etq
                copiadoValor = valr
            }
        )

        assertNotNull(izq)
        assertNotNull(der)

        izq!!.alEjecutar()
        assertEquals("Usuario", copiadoEtiqueta)
        assertEquals("usuario_test", copiadoValor)

        der!!.alEjecutar()
        assertEquals("Contraseña", copiadoEtiqueta)
        assertEquals("pass123", copiadoValor)
    }

    @Test
    fun `deslizamiento avisa cuando el campo esta vacio`() {
        val entrada = Entrada(
            id = "id-6",
            tipo = TipoEntrada.LOGIN,
            titulo = "GitHub Sin Clave",
            usuario = "",
            contrasena = ""
        )

        var avisoRecibido = ""
        val (izq, der) = ResolvedorAccionesDeslizamientoEntrada.resolver(
            entrada = entrada,
            alCopiar = { _, _, _ -> },
            alAvisar = { avisoRecibido = it }
        )

        izq!!.alEjecutar()
        assertEquals("Sin usuario", avisoRecibido)

        der!!.alEjecutar()
        assertEquals("Sin contraseña", avisoRecibido)
    }
}
