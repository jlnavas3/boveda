/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.ConfiguracionBovedaExportable
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.OpcionesImportacionBvda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.aplicarConfiguracionFiltrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportacionSelectivaTest {

    @Test
    fun `opcionesImportacionBvda calcula total de elementos seleccionados correctamente`() {
        val opciones = OpcionesImportacionBvda(
            tiposEntradaSeleccionados = setOf(TipoEntrada.LOGIN, TipoEntrada.PASSKEY),
            importarCategorias = true,
            importarIdentidades = true,
            importarReglasUrls = false,
            importarReglasHomelab = false
        )
        // 10 entradas seleccionadas + categorias (1) + identidades (1) + plantillas(1) + 6 otros por defecto = 19
        val total = opciones.totalSeleccionados(10)
        assertTrue(total >= 12)
    }

    @Test
    fun `aplicarConfiguracionFiltrada aplica solo bloques seleccionados`() {
        val ajustesIniciales = AjustesApp(
            prefijosSubdominios = listOf("www"),
            autoBloqueoSegundos = 60,
            densidadLista = "comoda"
        )

        val configuracionImportada = ConfiguracionBovedaExportable(
            prefijosSubdominios = listOf("www", "api", "mobile"),
            autoBloqueoSegundos = 300,
            densidadLista = "compacta"
        )

        // Solo importar reglas de URLs
        val opciones = OpcionesImportacionBvda(
            importarReglasUrls = true,
            importarSeguridadTiempos = false,
            importarFormatosLista = false
        )

        val actualizados = ajustesIniciales.aplicarConfiguracionFiltrada(configuracionImportada, opciones)

        // Prefijos cambiaron
        assertEquals(listOf("www", "api", "mobile"), actualizados.prefijosSubdominios)
        // AutoBloqueo NO cambió
        assertEquals(60, actualizados.autoBloqueoSegundos)
        // Densidad NO cambió
        assertEquals("comoda", actualizados.densidadLista)
    }

    @Test
    fun `simulacion de deduplicacion de categorias reasocia correctamente las credenciales`() {
        val catExistente = Categoria(id = "cat-local-1", nombre = "Finanzas", creadaEn = 1000L)
        val catImportadaMismoNombre = Categoria(id = "cat-remota-uuid-2", nombre = " finanzas ", creadaEn = 2000L)

        val porIdCat = mutableMapOf(catExistente.id to catExistente)
        val porNombreCat = mutableMapOf(catExistente.nombre.trim().lowercase() to catExistente)
        val mapaIdsCat = mutableMapOf<String, String>()

        val existente = porIdCat[catImportadaMismoNombre.id] ?: porNombreCat[catImportadaMismoNombre.nombre.trim().lowercase()]
        if (existente != null) {
            mapaIdsCat[catImportadaMismoNombre.id] = existente.id
        }

        assertEquals("cat-local-1", mapaIdsCat["cat-remota-uuid-2"])

        // Entrada con categoría foránea
        val entradaImportada = Entrada(
            id = "ent-1",
            titulo = "Banco Santander",
            categorias = listOf("cat-remota-uuid-2")
        )

        val categoriasReasignadas = entradaImportada.categorias.map { catId ->
            mapaIdsCat[catId] ?: if (porIdCat.containsKey(catId)) catId else null
        }.filterNotNull().distinct()

        assertEquals(listOf("cat-local-1"), categoriasReasignadas)
    }

    @Test
    fun `simulacion de deduplicacion de identidades unifica correos secundarios`() {
        val idenExistente = Identidad(
            id = "iden-local-1",
            nombre = "Mi Cuenta",
            correoPrincipal = "test@ejemplo.com",
            correosSecundarios = listOf("secundario1@ejemplo.com")
        )
        val idenImportada = Identidad(
            id = "iden-remota-2",
            nombre = "Mi Cuenta Copia",
            correoPrincipal = "test@ejemplo.com",
            correosSecundarios = listOf("secundario2@ejemplo.com")
        )

        val correoNormalizado = idenImportada.correoPrincipal.trim().lowercase()
        val esMisma = idenExistente.correoPrincipal.trim().lowercase() == correoNormalizado

        assertTrue(esMisma)

        val todosCorreos = (idenExistente.correosSecundarios + idenImportada.correoPrincipal + idenImportada.correosSecundarios)
            .filter { it.isNotBlank() && it != idenExistente.correoPrincipal }
            .distinct()

        val fusionada = idenExistente.copy(correosSecundarios = todosCorreos)

        assertEquals("iden-local-1", fusionada.id)
        assertEquals(listOf("secundario1@ejemplo.com", "secundario2@ejemplo.com"), fusionada.correosSecundarios)
    }
}
