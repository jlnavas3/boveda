package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.LETRAS_INDICE
import com.jlnavas3.bovedalocal.ui.componentes.encontrarIndiceParaLetra
import com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice
import com.jlnavas3.bovedalocal.ui.componentes.normalizarCaracterIndice
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IndiceAlfabeticoTest {

    @Test
    fun `el abecedario contiene hash numeral y la letra eñe en la posicion correcta`() {
        assertEquals('#', LETRAS_INDICE.first())
        assertEquals('Z', LETRAS_INDICE.last())
        assertTrue(LETRAS_INDICE.contains('Ñ'))
        val idxN = LETRAS_INDICE.indexOf('N')
        val idxEne = LETRAS_INDICE.indexOf('Ñ')
        val idxO = LETRAS_INDICE.indexOf('O')
        assertEquals(idxN + 1, idxEne)
        assertEquals(idxEne + 1, idxO)
        assertEquals(28, LETRAS_INDICE.size)
    }

    @Test
    fun `normaliza vocales acentuadas a su letra base y conserva la eñe`() {
        assertEquals('A', normalizarCaracterIndice('Á'))
        assertEquals('A', normalizarCaracterIndice('à'))
        assertEquals('E', normalizarCaracterIndice('é'))
        assertEquals('I', normalizarCaracterIndice('í'))
        assertEquals('O', normalizarCaracterIndice('ó'))
        assertEquals('U', normalizarCaracterIndice('ú'))
        assertEquals('U', normalizarCaracterIndice('ü'))
        assertEquals('Ñ', normalizarCaracterIndice('ñ'))
        assertEquals('Ñ', normalizarCaracterIndice('Ñ'))
        assertEquals('#', normalizarCaracterIndice('1'))
        assertEquals('#', normalizarCaracterIndice('@'))
        assertEquals('B', normalizarCaracterIndice('b'))
    }

    @Test
    fun `extrae letra inicial ignorando espacios y acentos`() {
        assertEquals('A', letraInicialIndice("  Álbum de fotos"))
        assertEquals('Ñ', letraInicialIndice("Ñandú"))
        assertEquals('#', letraInicialIndice("1Password"))
        assertEquals('#', letraInicialIndice(""))
        assertEquals('G', letraInicialIndice("Google"))
    }

    @Test
    fun `encontrarIndiceParaLetra localiza el elemento exacto o el mas proximo alfabeticamente`() {
        val items = listOf(
            ItemAgrupado.Suelto(Entrada(id = "1", titulo = "1Password")),
            ItemAgrupado.Suelto(Entrada(id = "2", titulo = "Amazon")),
            ItemAgrupado.Suelto(Entrada(id = "3", titulo = "Apple")),
            ItemAgrupado.Suelto(Entrada(id = "4", titulo = "Dropbox")),
            ItemAgrupado.Suelto(Entrada(id = "5", titulo = "Google")),
            ItemAgrupado.Suelto(Entrada(id = "6", titulo = "Netflix")),
            ItemAgrupado.Suelto(Entrada(id = "7", titulo = "Ñandú Express")),
            ItemAgrupado.Suelto(Entrada(id = "8", titulo = "Spotify"))
        )

        // '#' -> 1Password en indice 0
        assertEquals(0, encontrarIndiceParaLetra(items, '#'))

        // 'A' -> Amazon en indice 1
        assertEquals(1, encontrarIndiceParaLetra(items, 'A'))

        // 'D' -> Dropbox en indice 3
        assertEquals(3, encontrarIndiceParaLetra(items, 'D'))

        // 'C' no existe, debe avanzar al proximo disponible ('D')
        assertEquals(3, encontrarIndiceParaLetra(items, 'C'))

        // 'G' -> Google en indice 4
        assertEquals(4, encontrarIndiceParaLetra(items, 'G'))

        // 'Ñ' -> Ñandú Express en indice 6
        assertEquals(6, encontrarIndiceParaLetra(items, 'Ñ'))

        // 'S' -> Spotify en indice 7
        assertEquals(7, encontrarIndiceParaLetra(items, 'S'))

        // 'Z' excede los elementos existentes, retorna el ultimo
        assertEquals(7, encontrarIndiceParaLetra(items, 'Z'))
    }
}
