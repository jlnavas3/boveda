/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.autofill.ResolvedorElementosPerfilAutofill
import com.jlnavas3.bovedalocal.data.DatosContacto
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Perfil
import com.jlnavas3.bovedalocal.data.TipoEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ResolvedorElementosPerfilAutofillTest {

    @Test
    fun `prioriza tarjeta predeterminada del perfil seleccionado`() {
        val t1 = Entrada("t1", tipo = TipoEntrada.TARJETA, titulo = "Visa Oro", datosTarjeta = DatosTarjetaBancaria(numero = "4111111111111111"))
        val t2 = Entrada("t2", tipo = TipoEntrada.TARJETA, titulo = "Mastercard Predeterminada", datosTarjeta = DatosTarjetaBancaria(numero = "5555555555555555"))
        val t3 = Entrada("t3", tipo = TipoEntrada.TARJETA, titulo = "Amex", datosTarjeta = DatosTarjetaBancaria(numero = "378282246310005"))

        val perfil = Perfil(
            id = "perfil-personal",
            nombre = "Personal",
            tarjetasIds = listOf("t1", "t2"),
            tarjetaPredeterminadaId = "t2"
        )

        val resultado = ResolvedorElementosPerfilAutofill.resolver(
            entradas = listOf(t1, t2, t3),
            perfiles = listOf(perfil),
            paquete = "com.android.chrome",
            dominioWeb = "tienda.com"
        )

        assertNotNull(resultado.perfil)
        assertEquals("Personal", resultado.perfil?.nombre)
        assertEquals(2, resultado.tarjetas.size)
        // La primera debe ser la predeterminada (t2):
        assertEquals("t2", resultado.tarjetas.first().id)
        assertEquals("t1", resultado.tarjetas[1].id)
    }

    @Test
    fun `prioriza contacto predeterminado del perfil asociado al sitio web`() {
        val c1 = Entrada("c1", tipo = TipoEntrada.CONTACTO, titulo = "Casa", datosContacto = DatosContacto(direccion = "Calle 10", ciudad = "Quito"))
        val c2 = Entrada("c2", tipo = TipoEntrada.CONTACTO, titulo = "Oficina", datosContacto = DatosContacto(direccion = "Av. Amazonas", ciudad = "Quito"))

        val perfilTrabajo = Perfil(
            id = "perfil-trabajo",
            nombre = "Trabajo",
            contactosIds = listOf("c1", "c2"),
            contactoPredeterminadoId = "c2"
        )

        val loginTrabajo = Entrada(
            id = "login-1",
            titulo = "Web Corporativa",
            contrasena = "secret",
            urls = listOf("https://corporativo.com"),
            perfilId = "perfil-trabajo"
        )

        val resultado = ResolvedorElementosPerfilAutofill.resolver(
            entradas = listOf(c1, c2, loginTrabajo),
            perfiles = listOf(perfilTrabajo),
            paquete = "com.android.chrome",
            dominioWeb = "corporativo.com"
        )

        assertEquals("Trabajo", resultado.perfil?.nombre)
        assertEquals(2, resultado.contactos.size)
        // La primera debe ser la oficina (c2 predeterminada):
        assertEquals("c2", resultado.contactos.first().id)
    }

    @Test
    fun `hace fallback a tarjetas de la boveda si el perfil no tiene tarjetas asociadas`() {
        val t1 = Entrada("t1", tipo = TipoEntrada.TARJETA, titulo = "Tarjeta Suelta", datosTarjeta = DatosTarjetaBancaria(numero = "4000000000000000"))
        val perfilVacio = Perfil(id = "p-vacio", nombre = "Vacío")

        val resultado = ResolvedorElementosPerfilAutofill.resolver(
            entradas = listOf(t1),
            perfiles = listOf(perfilVacio),
            paquete = "com.tienda",
            dominioWeb = null
        )

        assertEquals(1, resultado.tarjetas.size)
        assertEquals("t1", resultado.tarjetas.first().id)
    }
}
