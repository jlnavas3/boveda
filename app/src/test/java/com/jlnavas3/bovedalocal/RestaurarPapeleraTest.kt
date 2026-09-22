package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class RestaurarPapeleraTest {

    private fun sanearDuplicados(entradas: List<Entrada>): List<Entrada> {
        val idsVistos = mutableSetOf<String>()
        val saneadas = mutableListOf<Entrada>()

        for (ent in entradas) {
            if (idsVistos.contains(ent.id)) {
                val nuevoTitulo = if (ent.titulo.startsWith("Copia de ", ignoreCase = true)) {
                    ent.titulo
                } else {
                    "Copia de ${ent.titulo}"
                }
                val reparada = ent.copy(
                    id = UUID.randomUUID().toString(),
                    titulo = nuevoTitulo,
                    modificadaEn = System.currentTimeMillis()
                )
                idsVistos.add(reparada.id)
                saneadas.add(reparada)
            } else {
                idsVistos.add(ent.id)
                saneadas.add(ent)
            }
        }
        return saneadas
    }

    private fun simularRestaurar(
        activas: List<Entrada>,
        papelera: List<Entrada>,
        idRestaurar: String,
        sustituir: Boolean
    ): Pair<List<Entrada>, List<Entrada>> {
        val entrada = papelera.firstOrNull { it.id == idRestaurar } ?: return Pair(activas, papelera)
        val existeMismoId = activas.any { it.id == idRestaurar }
        val existeMismoTituloYUsuario = activas.any {
            it.titulo.trim().equals(entrada.titulo.trim(), ignoreCase = true) &&
            it.usuario.trim() == entrada.usuario.trim()
        }

        val entradaParaActiva = if (existeMismoId || existeMismoTituloYUsuario) {
            if (sustituir) {
                entrada.copy(eliminadaEn = 0L, modificadaEn = System.currentTimeMillis())
            } else {
                val nuevoTitulo = if (entrada.titulo.startsWith("Copia de ", ignoreCase = true)) {
                    entrada.titulo
                } else {
                    "Copia de ${entrada.titulo}"
                }
                entrada.copy(
                    id = UUID.randomUUID().toString(),
                    titulo = nuevoTitulo,
                    eliminadaEn = 0L,
                    modificadaEn = System.currentTimeMillis()
                )
            }
        } else {
            entrada.copy(eliminadaEn = 0L)
        }

        val nuevasEntradas = if (sustituir && (existeMismoId || existeMismoTituloYUsuario)) {
            activas.map { ent ->
                if (ent.id == idRestaurar || (ent.titulo.trim().equals(entrada.titulo.trim(), ignoreCase = true) && ent.usuario.trim() == entrada.usuario.trim())) {
                    entradaParaActiva.copy(id = ent.id)
                } else {
                    ent
                }
            }
        } else {
            activas + entradaParaActiva
        }

        val filtradasSinDuplicados = mutableListOf<Entrada>()
        val idsVistos = mutableSetOf<String>()
        for (ent in nuevasEntradas) {
            if (idsVistos.add(ent.id)) {
                filtradasSinDuplicados.add(ent)
            } else {
                val sana = ent.copy(id = UUID.randomUUID().toString())
                idsVistos.add(sana.id)
                filtradasSinDuplicados.add(sana)
            }
        }

        return Pair(filtradasSinDuplicados, papelera.filterNot { it.id == idRestaurar })
    }

    @Test
    fun sanearDuplicados_reasignaIdUnicoYRenombra() {
        val ent1 = Entrada(id = "id-123", titulo = "Twitter", usuario = "user1")
        val ent2 = Entrada(id = "id-123", titulo = "Twitter", usuario = "user1_editado")

        val saneadas = sanearDuplicados(listOf(ent1, ent2))
        assertEquals(2, saneadas.size)
        assertEquals("id-123", saneadas[0].id)
        assertNotEquals("id-123", saneadas[1].id)
        assertEquals("Twitter", saneadas[0].titulo)
        assertEquals("Copia de Twitter", saneadas[1].titulo)

        // Comprobar que todos los IDs son únicos
        assertEquals(2, saneadas.map { it.id }.toSet().size)
    }

    @Test
    fun restaurar_conSustituirReemplazaEntradaActiva() {
        val activa = Entrada(id = "id-100", titulo = "Netflix", contrasena = "ClaveVieja")
        val enPapelera = Entrada(id = "id-100", titulo = "Netflix", contrasena = "ClaveRestaurada")

        val (nuevasActivas, nuevaPapelera) = simularRestaurar(
            activas = listOf(activa),
            papelera = listOf(enPapelera),
            idRestaurar = "id-100",
            sustituir = true
        )

        assertEquals(1, nuevasActivas.size)
        assertEquals("ClaveRestaurada", nuevasActivas[0].contrasena)
        assertTrue(nuevaPapelera.isEmpty())
        assertEquals(1, nuevasActivas.map { it.id }.toSet().size)
    }

    @Test
    fun restaurar_conDuplicarCreaCopiaConNuevoId() {
        val activa = Entrada(id = "id-100", titulo = "Netflix", contrasena = "ClaveVieja")
        val enPapelera = Entrada(id = "id-100", titulo = "Netflix", contrasena = "ClaveRestaurada")

        val (nuevasActivas, nuevaPapelera) = simularRestaurar(
            activas = listOf(activa),
            papelera = listOf(enPapelera),
            idRestaurar = "id-100",
            sustituir = false
        )

        assertEquals(2, nuevasActivas.size)
        assertEquals(2, nuevasActivas.map { it.id }.toSet().size)
        assertTrue(nuevaPapelera.isEmpty())

        val copia = nuevasActivas.find { it.titulo.startsWith("Copia de ") }
        assertNotEquals(null, copia)
        assertEquals("Copia de Netflix", copia?.titulo)
        assertEquals("ClaveRestaurada", copia?.contrasena)
        assertNotEquals("id-100", copia?.id)
    }
}
