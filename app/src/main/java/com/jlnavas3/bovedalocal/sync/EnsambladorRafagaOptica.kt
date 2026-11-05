/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import java.io.ByteArrayOutputStream

/**
 * Microcomponente acumulador y reensamblador reactivo para tramas de códigos QR ópticos.
 *
 * Soporta la recepción de fragmentos 1..N en cualquier orden o con tramas duplicadas/repetidas,
 * permitiendo una lectura fluida a ráfaga (animated QR) o estática (1 solo QR).
 */
class EnsambladorRafagaOptica {

    private var sesionActualId: String? = null
    private var totalEsperado: Int = 0
    private var crc32EsperadoHex: String? = null
    var aliasEmisor: String? = null
        private set
    private val tramasRecibidas = mutableMapOf<Int, ByteArray>()

    val sesionId: String? get() = sesionActualId
    val progreso: Int get() = tramasRecibidas.size
    val total: Int get() = totalEsperado
    val estaCompleto: Boolean get() = totalEsperado > 0 && tramasRecibidas.size == totalEsperado
    val porcentaje: Float get() = if (totalEsperado > 0) tramasRecibidas.size.toFloat() / totalEsperado else 0f

    /**
     * Registra una trama capturada por la cámara.
     * Retorna `true` si es una nueva trama que incrementó el progreso de la sesión actual.
     * Si la trama pertenece a una sesión distinta, reinicia el estado automáticamente.
     */
    @Synchronized
    fun registrarTrama(trama: TramaOpticaQr): Boolean {
        if (sesionActualId != trama.idSesionHex) {
            sesionActualId = trama.idSesionHex
            totalEsperado = trama.total
            crc32EsperadoHex = trama.crc32TotalHex
            aliasEmisor = trama.aliasEmisor
            tramasRecibidas.clear()
        } else if (aliasEmisor == null && trama.aliasEmisor != null) {
            aliasEmisor = trama.aliasEmisor
        }

        if (trama.total != totalEsperado) return false
        val previa = tramasRecibidas.put(trama.indice, trama.datosChunk)
        return previa == null
    }

    /**
     * Reconstituye el bloque binario cifrado concatenando los fragmentos en orden estricto 1..N.
     */
    @Synchronized
    fun ensamblarBloqueCifrado(): ByteArray {
        check(estaCompleto) {
            "Ráfaga incompleta: recibidos ${tramasRecibidas.size} de $totalEsperado fragmentos."
        }
        val bos = ByteArrayOutputStream()
        for (i in 1..totalEsperado) {
            val chunk = tramasRecibidas[i] ?: error("Falta el fragmento $i de la sesión $sesionActualId")
            bos.write(chunk)
        }
        return bos.toByteArray()
    }

    /**
     * Descifra y descomprime la bóveda una vez que la ráfaga está completa.
     *
     * @param pin6Digitos PIN efímero de 6 dígitos mostrado en la pantalla del emisor.
     * @return Bóveda remota desempaquetada y autenticada con ChaCha20-Poly1305.
     */
    fun descifrarBoveda(pin6Digitos: String): ContenidoBoveda {
        val bloque = ensamblarBloqueCifrado()
        val sesion = sesionActualId ?: error("Sesión óptica no iniciada.")
        val crc = crc32EsperadoHex ?: error("CRC32 no disponible.")
        return CodecDeltaOptico.desempaquetar(
            bloqueCifradoCompleto = bloque,
            idSesionHex = sesion,
            pin6Digitos = pin6Digitos,
            crc32EsperadoHex = crc
        )
    }

    @Synchronized
    fun reiniciar() {
        sesionActualId = null
        totalEsperado = 0
        crc32EsperadoHex = null
        aliasEmisor = null
        tramasRecibidas.clear()
    }
}
