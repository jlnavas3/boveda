/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class IdentidadDispositivoLocalTest {

    @Test
    fun formatearNombre_combina_fabricante_y_modelo() {
        assertEquals("HONOR DNP-NX9", IdentidadDispositivoLocal.formatearNombre("HONOR", "DNP-NX9"))
        assertEquals("Xiaomi 22101320G", IdentidadDispositivoLocal.formatearNombre("Xiaomi", "22101320G"))
        assertEquals("Samsung SM-S918B", IdentidadDispositivoLocal.formatearNombre("samsung", "SM-S918B"))
    }

    @Test
    fun formatearNombre_no_duplica_marca_si_modelo_ya_la_contiene() {
        assertEquals("Google Pixel 8 Pro", IdentidadDispositivoLocal.formatearNombre("Google", "Google Pixel 8 Pro"))
        assertEquals("Xiaomi Mi 11", IdentidadDispositivoLocal.formatearNombre("Xiaomi", "Xiaomi Mi 11"))
    }

    @Test
    fun formatearNombre_casos_limite_vacios() {
        assertEquals("Dispositivo Android", IdentidadDispositivoLocal.formatearNombre("", ""))
        assertEquals("DNP-NX9", IdentidadDispositivoLocal.formatearNombre("", "DNP-NX9"))
        assertEquals("HONOR", IdentidadDispositivoLocal.formatearNombre("HONOR", ""))
    }

    @Test
    fun obtenerNombre_no_falla_en_entorno_jvm() {
        val nombre = IdentidadDispositivoLocal.obtenerNombre()
        assertNotNull(nombre)
        assertTrue(nombre.isNotBlank())
    }
}
