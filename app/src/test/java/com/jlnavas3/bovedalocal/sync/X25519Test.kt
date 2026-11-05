/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import org.junit.Assert.assertEquals
import org.junit.Test

class X25519Test {

    @Test
    fun testVectorOroX25519() {
        val skDesktop = ByteArray(32) { 0x11 }
        val skAndroid = ByteArray(32) { 0x22 }

        val pkDesktopEsperada = "7b4e909bbe7ffe44c465a220037d608ee35897d31ef972f07f74892cb0f73f13"
        val pkAndroidEsperada = "0faa684ed28867b97f4a6a2dee5df8ce974e76b7018e3f22a1c4cf2678570f20"
        val dhSharedEsperada = "9e004098efc091d4ec2663b4e9f5cfd4d7064571690b4bea97ab146ab9f35056"

        val pkDesktop = X25519.derivarClavePublica(skDesktop)
        val pkAndroid = X25519.derivarClavePublica(skAndroid)

        assertEquals(pkDesktopEsperada, CalculadorHashEntrada.bytesAHex(pkDesktop))
        assertEquals(pkAndroidEsperada, CalculadorHashEntrada.bytesAHex(pkAndroid))

        val dhDesktop = X25519.calcularDiffieHellman(skDesktop, pkAndroid)
        val dhAndroid = X25519.calcularDiffieHellman(skAndroid, pkDesktop)

        assertEquals(dhSharedEsperada, CalculadorHashEntrada.bytesAHex(dhDesktop))
        assertEquals(dhSharedEsperada, CalculadorHashEntrada.bytesAHex(dhAndroid))
        assertEquals(CalculadorHashEntrada.bytesAHex(dhDesktop), CalculadorHashEntrada.bytesAHex(dhAndroid))
    }
}
