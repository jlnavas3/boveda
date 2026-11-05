/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidadorRfc1918Test {

    @Test
    fun testRangoClaseA10() {
        assertTrue(ValidadorRfc1918.esIpPrivadaRfc1918("10.0.0.1"))
        assertTrue(ValidadorRfc1918.esIpPrivadaRfc1918("10.255.255.254"))
        assertTrue(ValidadorRfc1918.esIpPrivadaRfc1918("10.14.200.55"))

        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("9.255.255.255"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("11.0.0.1"))
    }

    @Test
    fun testRangoClaseB172() {
        assertTrue(ValidadorRfc1918.esIpPrivadaRfc1918("172.16.0.1"))
        assertTrue(ValidadorRfc1918.esIpPrivadaRfc1918("172.24.100.20"))
        assertTrue(ValidadorRfc1918.esIpPrivadaRfc1918("172.31.255.254"))

        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("172.15.255.255"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("172.32.0.1"))
    }

    @Test
    fun testRangoClaseC192() {
        assertTrue(ValidadorRfc1918.esIpPrivadaRfc1918("192.168.0.1"))
        assertTrue(ValidadorRfc1918.esIpPrivadaRfc1918("192.168.1.50"))
        assertTrue(ValidadorRfc1918.esIpPrivadaRfc1918("192.168.254.254"))

        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("192.167.1.1"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("192.169.1.1"))
    }

    @Test
    fun testRechazoIpPublicasYReservadas() {
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("8.8.8.8"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("1.1.1.1"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("127.0.0.1")) // Loopback
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("169.254.1.1")) // Link-local
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("224.0.0.1")) // Multicast
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("255.255.255.255")) // Broadcast
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("0.0.0.0"))
    }

    @Test
    fun testRechazoHostnamesYDominios() {
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("localhost"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("desktop.lan"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("boveda.internal"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("google.com"))
    }

    @Test
    fun testRechazoNotacionOctalYCerosALaIzquierda() {
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("010.0.0.1"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("192.168.01.1"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("172.016.1.1"))
    }

    @Test
    fun testRechazoFormatoInvalido() {
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918(""))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("192.168.1"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("192.168.1.1.1"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("192.168.1.abc"))
        assertFalse(ValidadorRfc1918.esIpPrivadaRfc1918("192.168.1.256"))
    }
}
