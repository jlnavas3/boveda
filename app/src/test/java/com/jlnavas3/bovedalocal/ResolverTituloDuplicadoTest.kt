package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.ResolverTituloDuplicado
import org.junit.Assert.assertEquals
import org.junit.Test

class ResolverTituloDuplicadoTest {

    @Test
    fun cuandoNoExisteDuplicado_mantieneTituloOriginal() {
        val existentes = listOf("Google", "Twitter", "Amazon")
        val resultado = ResolverTituloDuplicado.resolver("Netflix", existentes)
        assertEquals("Netflix", resultado)
    }

    @Test
    fun cuandoExisteMismoTitulo_agregaPrefijoCopia() {
        val existentes = listOf("Netflix", "Google")
        val resultado = ResolverTituloDuplicado.resolver("Netflix", existentes)
        assertEquals("Copia Netflix", resultado)
    }

    @Test
    fun cuandoExisteCopia_agregaNumeroDos() {
        val existentes = listOf("Netflix", "Copia Netflix")
        val resultado = ResolverTituloDuplicado.resolver("Netflix", existentes)
        assertEquals("Copia (2) Netflix", resultado)
    }

    @Test
    fun cuandoExistenMultiplesCopias_incrementaNumero() {
        val existentes = listOf("Netflix", "Copia Netflix", "Copia (2) Netflix", "Copia (3) Netflix")
        val resultado = ResolverTituloDuplicado.resolver("Netflix", existentes)
        assertEquals("Copia (4) Netflix", resultado)
    }

    @Test
    fun cuandoTituloYaEsCopiaYExiste_agregaNumeroDos() {
        val existentes = listOf("Copia Netflix")
        val resultado = ResolverTituloDuplicado.resolver("Copia Netflix", existentes)
        assertEquals("Copia (2) Netflix", resultado)
    }

    @Test
    fun cuandoTituloYaTieneNumeroYExiste_incrementaNumero() {
        val existentes = listOf("Copia (2) Netflix")
        val resultado = ResolverTituloDuplicado.resolver("Copia (2) Netflix", existentes)
        assertEquals("Copia (3) Netflix", resultado)
    }

    @Test
    fun comparacionEsInsensibleAMayusculasYEspacios() {
        val existentes = listOf("  netflix  ")
        val resultado = ResolverTituloDuplicado.resolver("NETFLIX", existentes)
        assertEquals("Copia NETFLIX", resultado)
    }

    @Test
    fun resolverEntrada_actualizaElTituloEnLaEntrada() {
        val entrada = Entrada(
            id = "123",
            titulo = "Juan Pérez",
            usuario = "+34 600 000 000",
            tipo = TipoEntrada.CONTACTO
        )
        val existentes = listOf("Juan Pérez")
        val resuelta = ResolverTituloDuplicado.resolverEntrada(entrada, existentes)
        assertEquals("Copia Juan Pérez", resuelta.titulo)
        assertEquals("123", resuelta.id)
        assertEquals(TipoEntrada.CONTACTO, resuelta.tipo)
    }

    @Test
    fun loteDeImportacionesConMismoTitulo_resuelveSecuencialmente() {
        val titulosAcumulados = mutableListOf("Contacto X")
        val item1 = ResolverTituloDuplicado.resolver("Contacto X", titulosAcumulados)
        titulosAcumulados.add(item1)
        val item2 = ResolverTituloDuplicado.resolver("Contacto X", titulosAcumulados)
        titulosAcumulados.add(item2)

        assertEquals("Copia Contacto X", item1)
        assertEquals("Copia (2) Contacto X", item2)
    }
}
