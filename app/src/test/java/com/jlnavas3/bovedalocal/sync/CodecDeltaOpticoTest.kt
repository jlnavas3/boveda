/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class CodecDeltaOpticoTest {

    private fun crearBovedaPrueba(numEntradas: Int = 3): ContenidoBoveda {
        val cat = Categoria(id = "cat-personal", nombre = "Personal")
        val entradas = (1..numEntradas).map { i ->
            Entrada(
                id = "id-$i",
                titulo = "Cuenta $i",
                usuario = "usuario$i@correo.com",
                contrasena = "claveSegura$i",
                categorias = listOf(cat.id),
                modificadaEn = System.currentTimeMillis()
            )
        }
        return ContenidoBoveda(
            entradas = entradas,
            categorias = listOf(cat)
        )
    }

    @Test
    fun empaquetar_y_desempaquetar_delta_estatico_1_qr() {
        val boveda = crearBovedaPrueba(2)
        val paquete = CodecDeltaOptico.empaquetar(boveda, soloDelta = true)

        assertEquals(1, paquete.totalTramas)
        assertEquals(1, paquete.tramas.size)
        assertEquals(6, paquete.pinEfimero.length)
        assertEquals(8, paquete.idSesionHex.length)

        val trama = paquete.tramas.first()
        assertEquals(1, trama.indice)
        assertEquals(1, trama.total)

        // Verificar serialización y parseo de URI óptico
        val uri = trama.serializarUri()
        assertTrue(uri.startsWith("bvda-opt://v1/1/1/"))

        val tramaParseada = TramaOpticaQr.parsearUri(uri)
        assertNotNull(tramaParseada)
        assertEquals(trama, tramaParseada)

        // Ensamblar y desempaquetar con EnsambladorRafagaOptica
        val ensamblador = EnsambladorRafagaOptica()
        val agregada = ensamblador.registrarTrama(tramaParseada!!)
        assertTrue(agregada)
        assertTrue(ensamblador.estaCompleto)
        assertEquals(trama.aliasEmisor, ensamblador.aliasEmisor)

        val recuperada = ensamblador.descifrarBoveda(paquete.pinEfimero)
        assertEquals(2, recuperada.entradas.size)
        assertEquals("Cuenta 1", recuperada.entradas[0].titulo)
        assertEquals("usuario1@correo.com", recuperada.entradas[0].usuario)
        assertEquals("claveSegura1", recuperada.entradas[0].contrasena)
    }

    @Test
    fun empaquetar_rafaga_fragmentada_y_reensamblar_desordenada() {
        val boveda = crearBovedaPrueba(10)
        // Forzar fragmentación con tamaño pequeño de chunk (120 bytes)
        val paquete = CodecDeltaOptico.empaquetar(boveda, soloDelta = false, maxBytesPorChunk = 120)

        assertTrue("Debe tener más de 2 tramas", paquete.totalTramas >= 3)
        assertEquals(paquete.totalTramas, paquete.tramas.size)

        val ensamblador = EnsambladorRafagaOptica()

        // Insertar en orden aleatorio / desordenado simulando cámara escaneando ráfaga
        val tramasDesordenadas = paquete.tramas.shuffled()
        for (trama in tramasDesordenadas) {
            val uri = trama.serializarUri()
            val parseada = TramaOpticaQr.parsearUri(uri)
            assertNotNull(parseada)
            ensamblador.registrarTrama(parseada!!)
        }

        assertTrue(ensamblador.estaCompleto)
        assertEquals(1f, ensamblador.porcentaje, 0.001f)

        val recuperada = ensamblador.descifrarBoveda(paquete.pinEfimero)
        assertEquals(10, recuperada.entradas.size)
    }

    @Test
    fun rechazo_con_pin_incorrecto() {
        val boveda = crearBovedaPrueba(1)
        val paquete = CodecDeltaOptico.empaquetar(boveda, soloDelta = true)

        val pinIncorrecto = if (paquete.pinEfimero == "123456") "654321" else "123456"

        val ensamblador = EnsambladorRafagaOptica()
        ensamblador.registrarTrama(paquete.tramas[0])

        try {
            ensamblador.descifrarBoveda(pinIncorrecto)
            fail("Debió fallar con PIN incorrecto")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("PIN óptico de 6 dígitos incorrecto"))
        }
    }

    @Test
    fun rechazo_con_crc32_corrupto() {
        val boveda = crearBovedaPrueba(1)
        val paquete = CodecDeltaOptico.empaquetar(boveda, soloDelta = true)
        val tramaOriginal = paquete.tramas[0]

        val datosCorruptos = tramaOriginal.datosChunk.clone()
        datosCorruptos[0] = (datosCorruptos[0].toInt() xor 0xFF).toByte()

        val tramaCorrupta = tramaOriginal.copy(datosChunk = datosCorruptos)

        val ensamblador = EnsambladorRafagaOptica()
        ensamblador.registrarTrama(tramaCorrupta)

        try {
            ensamblador.descifrarBoveda(paquete.pinEfimero)
            fail("Debió fallar por CRC32 inválido")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Fallo de integridad CRC32"))
        }
    }

    @Test
    fun delta_reciente_limita_a_5_entradas_y_garantiza_1_solo_qr() {
        // Simular bóveda con 50 entradas modificadas (sin lastSyncHash, todas pendientes)
        val boveda = crearBovedaPrueba(50)
        val paquete = CodecDeltaOptico.empaquetar(boveda, soloDelta = true)

        assertEquals("Delta reciente debe generar exactamente 1 QR estático", 1, paquete.totalTramas)
        assertTrue(paquete.bytesCifradosTotales <= CodecDeltaOptico.MAX_BYTES_POR_CHUNK_DEFECTO)

        val ensamblador = EnsambladorRafagaOptica()
        ensamblador.registrarTrama(paquete.tramas[0])
        val recuperada = ensamblador.descifrarBoveda(paquete.pinEfimero)

        assertEquals(5, recuperada.entradas.size)
    }

    @Test
    fun boveda_con_78_activas_y_25_papelera_al_dia_genera_exactamente_1_solo_qr() {
        val cat = Categoria(id = "cat-general", nombre = "General")
        val activas = (1..78).map { i ->
            val e = Entrada(
                id = "activa-$i",
                titulo = "Entrada Activa $i",
                usuario = "usuario$i@prueba.com",
                contrasena = "claveSegura$i",
                categorias = listOf(cat.id),
                modificadaEn = System.currentTimeMillis() - i * 1000L
            )
            val hash = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(e))
            e.copy(lastSyncHash = hash)
        }

        val papelera = (1..25).map { i ->
            val e = Entrada(
                id = "papelera-$i",
                titulo = "Entrada Papelera $i",
                usuario = "borrado$i@prueba.com",
                contrasena = "claveVieja$i",
                categorias = listOf(cat.id),
                eliminadaEn = System.currentTimeMillis() - i * 2000L,
                modificadaEn = System.currentTimeMillis() - i * 2000L
            )
            val hash = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(e))
            e.copy(lastSyncHash = hash)
        }

        val boveda = ContenidoBoveda(
            entradas = activas,
            papelera = papelera,
            categorias = listOf(cat)
        )

        // 1. Extraer delta reciente
        val delta = CodecDeltaOptico.extraerDeltaReciente(boveda)

        // Debe contener ÚNICAMENTE 1 entrada testigo y 0 en papelera
        assertEquals(1, delta.entradas.size)
        assertEquals(0, delta.papelera.size)
        assertEquals(1, delta.categorias.size)

        // 2. Empaquetar
        val paquete = CodecDeltaOptico.empaquetar(boveda, soloDelta = true)

        assertEquals("Debe generar exactamente 1 fotograma QR (1/1)", 1, paquete.totalTramas)
        assertEquals(1, paquete.tramas.size)
        assertTrue(
            "Carga útil debe ser menor a 300 bytes: ${paquete.bytesCifradosTotales}",
            paquete.bytesCifradosTotales <= CodecDeltaOptico.MAX_BYTES_POR_CHUNK_DEFECTO
        )

        // 3. Verificar que el receptor puede desempaquetar y fusionar sin problemas
        val ensamblador = EnsambladorRafagaOptica()
        ensamblador.registrarTrama(paquete.tramas[0])
        val recuperada = ensamblador.descifrarBoveda(paquete.pinEfimero)

        assertEquals(1, recuperada.entradas.size)
        assertEquals(0, recuperada.papelera.size)
    }

    @Test
    fun boveda_con_entradas_pendientes_no_arrastra_papelera_y_genera_1_solo_qr() {
        val cat = Categoria(id = "cat-general", nombre = "General")
        // 75 entradas al día, 3 entradas con cambios pendientes
        val activas = (1..78).map { i ->
            val e = Entrada(
                id = "activa-$i",
                titulo = "Entrada Activa $i",
                usuario = "usuario$i@prueba.com",
                contrasena = "claveSegura$i",
                categorias = listOf(cat.id),
                modificadaEn = System.currentTimeMillis() - i * 1000L
            )
            if (i <= 3) {
                // Modificada: lastSyncHash es null o antiguo
                e.copy(lastSyncHash = null)
            } else {
                val hash = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(e))
                e.copy(lastSyncHash = hash)
            }
        }

        val papelera = (1..25).map { i ->
            val e = Entrada(
                id = "papelera-$i",
                titulo = "Entrada Papelera $i",
                eliminadaEn = System.currentTimeMillis()
            )
            val hash = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(e))
            e.copy(lastSyncHash = hash)
        }

        val boveda = ContenidoBoveda(
            entradas = activas,
            papelera = papelera,
            categorias = listOf(cat)
        )

        val delta = CodecDeltaOptico.extraerDeltaReciente(boveda)
        assertEquals(3, delta.entradas.size)
        assertEquals(0, delta.papelera.size)

        val paquete = CodecDeltaOptico.empaquetar(boveda, soloDelta = true)
        assertEquals(1, paquete.totalTramas)
        assertEquals(1, paquete.tramas.size)
    }
}
