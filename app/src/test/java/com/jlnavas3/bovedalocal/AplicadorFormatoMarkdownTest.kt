/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.jlnavas3.bovedalocal.util.AplicadorFormatoMarkdown
import com.jlnavas3.bovedalocal.util.FormatoMarkdownAccion
import org.junit.Assert.assertEquals
import org.junit.Test

class AplicadorFormatoMarkdownTest {

    @Test
    fun testEnvolverTextoSeleccionadoNegrita() {
        val original = TextFieldValue("Hola mundo", TextRange(5, 10))
        val res = AplicadorFormatoMarkdown.aplicar(original, FormatoMarkdownAccion.NEGRITA)
        assertEquals("Hola **mundo**", res.text)
        assertEquals(TextRange(7, 12), res.selection)
    }

    @Test
    fun testEnvolverCursorVacioNegrita() {
        val original = TextFieldValue("Hola ", TextRange(5))
        val res = AplicadorFormatoMarkdown.aplicar(original, FormatoMarkdownAccion.NEGRITA)
        assertEquals("Hola ****", res.text)
        assertEquals(TextRange(7), res.selection)
    }

    @Test
    fun testPrefijoTituloLinea() {
        val original = TextFieldValue("Mi nota importante", TextRange(8))
        val res = AplicadorFormatoMarkdown.aplicar(original, FormatoMarkdownAccion.TITULO_1)
        assertEquals("# Mi nota importante", res.text)
        assertEquals(TextRange(10), res.selection)
    }

    @Test
    fun testAlternarPrefijoQuitarSiYaExiste() {
        val original = TextFieldValue("# Mi nota importante", TextRange(10))
        val res = AplicadorFormatoMarkdown.aplicar(original, FormatoMarkdownAccion.TITULO_1)
        assertEquals("Mi nota importante", res.text)
        assertEquals(TextRange(8), res.selection)
    }

    @Test
    fun testPrefijoCheckbox() {
        val original = TextFieldValue("Comprar leche", TextRange(0))
        val res = AplicadorFormatoMarkdown.aplicar(original, FormatoMarkdownAccion.CHECKBOX)
        assertEquals("- [ ] Comprar leche", res.text)
    }

    @Test
    fun testInsertarTablaPlantilla() {
        val original = TextFieldValue("", TextRange(0))
        val res = AplicadorFormatoMarkdown.aplicar(original, FormatoMarkdownAccion.TABLA)
        assert(res.text.contains("| Columna 1 | Columna 2 |"))
        assert(res.text.contains("| :--- | ---: |"))
    }

    @Test
    fun testCentrarTextoConSeleccion() {
        val original = TextFieldValue("Título centrado", TextRange(0, 15))
        val res = AplicadorFormatoMarkdown.aplicar(original, FormatoMarkdownAccion.CENTRAR)
        assertEquals("-> Título centrado <-", res.text)
    }

    @Test
    fun testAlinearDerechaConSeleccion() {
        val original = TextFieldValue("Firma a la derecha", TextRange(0, 18))
        val res = AplicadorFormatoMarkdown.aplicar(original, FormatoMarkdownAccion.ALINEAR_DERECHA)
        assertEquals("-> Firma a la derecha", res.text)
    }
}
