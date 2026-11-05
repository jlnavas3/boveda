/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import org.junit.Assert.assertEquals
import org.junit.Test

class ArbitroSincronizacionTest {

    @Test
    fun testSinCambios() {
        val hashComun = ByteArray(32) { 0x42.toByte() }
        val lastSync = ByteArray(32) { 0x11.toByte() }

        val decision = ArbitroSincronizacion.evaluarEstadoEntrada(
            hashLocal = hashComun,
            lastSyncLocal = lastSync,
            revLocal = 5L,
            hashRemoto = hashComun,
            lastSyncRemoto = lastSync,
            revRemoto = 5L
        )

        assertEquals(DecisionArbitro.SIN_CAMBIOS, decision)
    }

    @Test
    fun testFastForwardLocal() {
        val hashBase = ByteArray(32) { 0x01.toByte() }
        val hashLocal = ByteArray(32) { 0x02.toByte() }

        val decision = ArbitroSincronizacion.evaluarEstadoEntrada(
            hashLocal = hashLocal,
            lastSyncLocal = hashBase,
            revLocal = 5L,
            hashRemoto = hashBase,
            lastSyncRemoto = hashBase,
            revRemoto = 4L
        )

        assertEquals(DecisionArbitro.FAST_FORWARD_LOCAL, decision)
    }

    @Test
    fun testFastForwardRemoto() {
        val hashBase = ByteArray(32) { 0x01.toByte() }
        val hashRemoto = ByteArray(32) { 0x03.toByte() }

        val decision = ArbitroSincronizacion.evaluarEstadoEntrada(
            hashLocal = hashBase,
            lastSyncLocal = hashBase,
            revLocal = 4L,
            hashRemoto = hashRemoto,
            lastSyncRemoto = hashBase,
            revRemoto = 5L
        )

        assertEquals(DecisionArbitro.FAST_FORWARD_REMOTO, decision)
    }

    @Test
    fun testSplitBrainSimetrico() {
        val hashLocal = ByteArray(32) { 0xAA.toByte() }
        val hashRemoto = ByteArray(32) { 0xBB.toByte() }
        val baseComun = ByteArray(32) { 0x00.toByte() }

        val decision = ArbitroSincronizacion.evaluarEstadoEntrada(
            hashLocal = hashLocal,
            lastSyncLocal = baseComun,
            revLocal = 5L,
            hashRemoto = hashRemoto,
            lastSyncRemoto = baseComun,
            revRemoto = 5L
        )

        assertEquals(DecisionArbitro.SPLIT_BRAIN, decision)
    }

    @Test
    fun testSplitBrainAsimetrico() {
        // rev 6 vs rev 5 ambos partiendo de rev 4 (baseComun).
        // Ninguno tiene el hash del otro como lastSync.
        val baseComun = ByteArray(32) { 0x10.toByte() }
        val hashLocal = ByteArray(32) { 0x20.toByte() }
        val hashRemoto = ByteArray(32) { 0x30.toByte() }

        val decision = ArbitroSincronizacion.evaluarEstadoEntrada(
            hashLocal = hashLocal,
            lastSyncLocal = baseComun,
            revLocal = 6L,
            hashRemoto = hashRemoto,
            lastSyncRemoto = baseComun,
            revRemoto = 5L
        )

        assertEquals(DecisionArbitro.SPLIT_BRAIN, decision)
    }

    @Test
    fun testPrimerSyncSinAncestro() {
        val hashLocal = ByteArray(32) { 0x12.toByte() }
        val hashRemoto = ByteArray(32) { 0x34.toByte() }

        val decision = ArbitroSincronizacion.evaluarEstadoEntrada(
            hashLocal = hashLocal,
            lastSyncLocal = null,
            revLocal = 1L,
            hashRemoto = hashRemoto,
            lastSyncRemoto = null,
            revRemoto = 1L
        )

        assertEquals(DecisionArbitro.SPLIT_BRAIN, decision)
    }
}
