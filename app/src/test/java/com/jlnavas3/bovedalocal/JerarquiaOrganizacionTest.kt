package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion
import com.jlnavas3.bovedalocal.util.filtrarCategoriasPorIdentidad
import com.jlnavas3.bovedalocal.util.filtrarIdentidadesPorCategoria
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JerarquiaOrganizacionTest {

    private val idenPersonal = Identidad(id = "iden-1", nombre = "Personal", correoPrincipal = "personal@test.com")
    private val idenTrabajo = Identidad(id = "iden-2", nombre = "Trabajo", correoPrincipal = "trabajo@test.com")
    private val identidades = listOf(idenPersonal, idenTrabajo)

    private val catFinanzas = Categoria(id = "cat-fin", nombre = "Finanzas")
    private val catDev = Categoria(id = "cat-dev", nombre = "Desarrollo")
    private val catGeneral = Categoria(id = "cat-gen", nombre = "General")
    private val categorias = listOf(catFinanzas, catDev, catGeneral)

    private val e1 = Entrada(id = "1", titulo = "Banco", usuario = "personal@test.com", categorias = listOf("cat-fin"))
    private val e2 = Entrada(id = "2", titulo = "GitHub Trabajo", usuario = "trabajo@test.com", categorias = listOf("cat-dev"))
    private val e3 = Entrada(id = "3", titulo = "Server AWS", usuario = "trabajo@test.com", categorias = listOf("cat-dev"))
    private val e4 = Entrada(id = "4", titulo = "Router Casa", usuario = "admin", categorias = listOf("cat-gen")) // sin identidad

    private val entradas = listOf(e1, e2, e3, e4)

    @Test
    fun `cuando identidad es nivel superior y se selecciona Trabajo solo aparecen categorias de Trabajo`() {
        val (cats, conteos) = filtrarCategoriasPorIdentidad(
            categorias = categorias,
            entradas = entradas,
            identidades = identidades,
            identidadSeleccionadaId = idenTrabajo.id
        )

        assertEquals(1, cats.size)
        assertEquals("cat-dev", cats.first().id)
        assertEquals(2, conteos["cat-dev"])
        assertEquals(0, conteos["cat-fin"])
    }

    @Test
    fun `cuando identidad es nivel superior y se selecciona Todas aparecen todas las categorias con conteo total`() {
        val (cats, conteos) = filtrarCategoriasPorIdentidad(
            categorias = categorias,
            entradas = entradas,
            identidades = identidades,
            identidadSeleccionadaId = null
        )

        assertEquals(3, cats.size)
        assertEquals(1, conteos["cat-fin"])
        assertEquals(2, conteos["cat-dev"])
        assertEquals(1, conteos["cat-gen"])
    }

    @Test
    fun `cuando categoria es nivel superior y se selecciona Desarrollo solo aparecen identidades presentes en Desarrollo`() {
        val (idens, conteos, sinIden) = filtrarIdentidadesPorCategoria(
            identidades = identidades,
            entradas = entradas,
            categoriaSeleccionadaId = catDev.id
        )

        assertEquals(1, idens.size)
        assertEquals("iden-2", idens.first().id)
        assertEquals(2, conteos["iden-2"])
        assertEquals(0, conteos["iden-1"])
        assertEquals(0, sinIden)
    }

    @Test
    fun `enum deserializa correctamente claves y defaults`() {
        assertEquals(
            JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA,
            JerarquiaOrganizacion.desdeClave("identidad_sobre_categoria")
        )
        assertEquals(
            JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA,
            JerarquiaOrganizacion.desdeClave("identidad_sobre_coleccion")
        )
        assertEquals(
            JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD,
            JerarquiaOrganizacion.desdeClave("categoria_sobre_identidad")
        )
        assertEquals(
            JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD,
            JerarquiaOrganizacion.desdeClave("coleccion_sobre_identidad")
        )
        assertEquals(
            JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA,
            JerarquiaOrganizacion.desdeClave("clave_desconocida")
        )
    }
}
