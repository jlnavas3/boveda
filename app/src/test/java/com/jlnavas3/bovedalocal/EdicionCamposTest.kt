package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EdicionCamposTest {

    @Test
    fun normalizarEtiqueta_limpiaEspaciosYPrefijoHash() {
        assertEquals("BANCO", normalizarEtiqueta("  BANCO  "))
        assertEquals("redessociales", normalizarEtiqueta("redes sociales"))
        assertEquals("correo", normalizarEtiqueta("#correo"))
        assertEquals("", normalizarEtiqueta("   "))
    }

    @Test
    fun tipoCampo_poseeEtiquetasCorrectas() {
        assertEquals("Texto", TipoCampo.TEXTO.etiqueta)
        assertEquals("Oculto", TipoCampo.OCULTO.etiqueta)
        assertEquals("PIN", TipoCampo.PIN.etiqueta)
    }

    @Test
    fun campoPersonalizado_creacionYModificacion() {
        val campo = CampoPersonalizado(etiqueta = "PIN Cajero", valor = "1234", tipo = TipoCampo.PIN)
        assertEquals("PIN Cajero", campo.etiqueta)
        assertEquals("1234", campo.valor)
        assertEquals(TipoCampo.PIN, campo.tipo)

        val copia = campo.copy(valor = "5678")
        assertEquals("5678", copia.valor)
    }

    @Test
    fun tipoEntrada_contieneTiposBasicos() {
        val tipos = TipoEntrada.entries
        assertTrue(tipos.contains(TipoEntrada.LOGIN))
        assertTrue(tipos.contains(TipoEntrada.NOTA))
        assertTrue(tipos.contains(TipoEntrada.PASSKEY))
    }

    @Test
    fun base32_validacionCorrecta() {
        assertTrue(Base32.esValido("JBSWY3DPEHPK3PXP"))
        assertTrue(Base32.esValido("MFRGGZDFMZTWQ2LK"))
        // Base32 no admite 1, 8, 9, 0
        assertFalse(Base32.esValido("INVALID1890!@#"))
    }

    @Test
    fun opcionesGenerador_valoresPorDefecto() {
        val opciones = OpcionesGenerador()
        assertTrue(opciones.longitud in 8..64)
        assertTrue(opciones.mayusculas)
        assertTrue(opciones.minusculas)
        assertTrue(opciones.digitos)
    }
}
