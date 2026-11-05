/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.PreferenciaIdentificadorIdentidad
import com.jlnavas3.bovedalocal.data.TipoDocumentoIdentidad
import com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentoIdentidadTest {

    @Test
    fun `identificadorPreferido devuelve documento si la preferencia es documento y existe`() {
        val iden = Identidad(
            nombre = "Personal",
            correoPrincipal = "personal@correo.com",
            tipoDocumento = "DNI",
            numeroDocumento = "12345678-A",
            preferenciaIdentificador = PreferenciaIdentificadorIdentidad.DOCUMENTO
        )

        assertEquals("12345678-A", iden.identificadorPreferido)
        assertTrue(iden.tieneDocumento)
        assertEquals("DNI: 12345678-A", iden.etiquetaDocumentoCompleta)
    }

    @Test
    fun `identificadorPreferido devuelve correo si la preferencia es correo`() {
        val iden = Identidad(
            nombre = "Personal",
            correoPrincipal = "personal@correo.com",
            tipoDocumento = "CI",
            numeroDocumento = "87654321",
            preferenciaIdentificador = PreferenciaIdentificadorIdentidad.CORREO
        )

        assertEquals("personal@correo.com", iden.identificadorPreferido)
    }

    @Test
    fun `identificadorPreferido devuelve documento si correo esta vacio`() {
        val iden = Identidad(
            nombre = "Trámites Gobierno",
            correoPrincipal = "",
            tipoDocumento = "CC",
            numeroDocumento = "10203040",
            preferenciaIdentificador = PreferenciaIdentificadorIdentidad.CORREO
        )

        assertEquals("10203040", iden.identificadorPreferido)
    }

    @Test
    fun `resolverIdentidadParaEntrada vincula por numero de documento normalizado`() {
        val iden = Identidad(
            id = "iden-gob-1",
            nombre = "Ciudadano",
            correoPrincipal = "ciudadano@correo.com",
            tipoDocumento = "CI",
            numeroDocumento = "12.345.678"
        )

        // Entrada donde el usuario escribió el documento sin puntos
        val entrada1 = Entrada(
            id = "ent-1",
            titulo = "Portal de Impuestos",
            usuario = "12345678"
        )

        val resuelto1 = resolverIdentidadParaEntrada(entrada1, listOf(iden))
        assertNotNull(resuelto1)
        assertEquals("iden-gob-1", resuelto1?.id)

        // Entrada donde el usuario escribió con puntos
        val entrada2 = Entrada(
            id = "ent-2",
            titulo = "Seguridad Social",
            usuario = "12.345.678"
        )

        val resuelto2 = resolverIdentidadParaEntrada(entrada2, listOf(iden))
        assertNotNull(resuelto2)
        assertEquals("iden-gob-1", resuelto2?.id)
    }

    @Test
    fun `catalogo TipoDocumentoIdentidad resuelve siglas comunes`() {
        assertEquals(TipoDocumentoIdentidad.DNI, TipoDocumentoIdentidad.desdeSigla("dni"))
        assertEquals(TipoDocumentoIdentidad.CI, TipoDocumentoIdentidad.desdeSigla("CI"))
        assertEquals(TipoDocumentoIdentidad.CC, TipoDocumentoIdentidad.desdeSigla("cc"))
        assertEquals(TipoDocumentoIdentidad.RUC, TipoDocumentoIdentidad.desdeSigla("ruc"))
        assertEquals(TipoDocumentoIdentidad.RUC, TipoDocumentoIdentidad.desdeSigla("RUC"))
        assertEquals(TipoDocumentoIdentidad.PASAPORTE, TipoDocumentoIdentidad.desdeSigla("pasaporte"))
        assertEquals(TipoDocumentoIdentidad.OTRO, TipoDocumentoIdentidad.desdeSigla("algo-raro"))
        assertEquals(TipoDocumentoIdentidad.CI, TipoDocumentoIdentidad.desdeSigla(null))
        assertEquals(TipoDocumentoIdentidad.CI, TipoDocumentoIdentidad.desdeSigla(""))
        assertEquals(TipoDocumentoIdentidad.CI, TipoDocumentoIdentidad.desdeSigla("   "))
    }
}
