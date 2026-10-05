package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion
import com.jlnavas3.bovedalocal.util.ItemAgrupadoJerarquico
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosJerarquicos
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AgrupadorJerarquicoTest {

    private val idenPersonal = Identidad(id = "iden-1", nombre = "Personal", correoPrincipal = "personal@test.com")
    private val idenTrabajo = Identidad(id = "iden-2", nombre = "Trabajo", correoPrincipal = "trabajo@test.com")
    private val identidades = listOf(idenPersonal, idenTrabajo)

    private val catFinanzas = Categoria(id = "cat-fin", nombre = "Finanzas")
    private val catDev = Categoria(id = "cat-dev", nombre = "Desarrollo")
    private val categorias = listOf(catFinanzas, catDev)

    private val e1 = Entrada(id = "1", titulo = "Banco", usuario = "personal@test.com", categorias = listOf("cat-fin"))
    private val e2 = Entrada(id = "2", titulo = "GitHub", usuario = "trabajo@test.com", categorias = listOf("cat-dev"))
    private val e3 = Entrada(id = "3", titulo = "AWS", usuario = "trabajo@test.com", categorias = listOf("cat-dev"))
    private val e4 = Entrada(id = "4", titulo = "Router Casa", usuario = "admin", categorias = emptyList())

    private val entradas = listOf(e1, e2, e3, e4)

    @Test
    fun jerarquiaIdentidadSobreCategoriaGeneraCabecerasDeIdentidadYSubcabecerasDeCategoria() {
        val items = construirItemsAgrupadosJerarquicos(
            entradas = entradas,
            identidades = identidades,
            categorias = categorias,
            jerarquia = JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA,
            identidadesActivas = true,
            estaExpandido = { true }
        )

        val principales = items.filterIsInstance<ItemAgrupadoJerarquico.CabeceraPrincipal>()
        assertEquals(3, principales.size)
        assertEquals("identidad-iden-1", principales[0].claveGrupo)
        assertEquals(setOf("1"), principales[0].idsEntradas)
        assertEquals("identidad-iden-2", principales[1].claveGrupo)
        assertEquals(setOf("2", "3"), principales[1].idsEntradas)
        assertEquals("identidad-__SIN_IDENTIDAD__", principales[2].claveGrupo)
        assertEquals(setOf("4"), principales[2].idsEntradas)

        val subcabeceras = items.filterIsInstance<ItemAgrupadoJerarquico.Subcabecera>()
        assertTrue(subcabeceras.any { it.claveGrupo == "subcat-iden-1-cat-fin" && it.idsEntradas == setOf("1") })
        assertTrue(subcabeceras.any { it.claveGrupo == "subcat-iden-2-cat-dev" && it.idsEntradas == setOf("2", "3") })
        assertTrue(subcabeceras.any { it.claveGrupo == "subcat-sin-iden-__SIN_CAT__" && it.idsEntradas == setOf("4") })
    }

    @Test
    fun jerarquiaCategoriaSobreIdentidadGeneraCabecerasDeCategoriaYSubcabecerasDeIdentidad() {
        val items = construirItemsAgrupadosJerarquicos(
            entradas = entradas,
            identidades = identidades,
            categorias = categorias,
            jerarquia = JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD,
            identidadesActivas = true,
            estaExpandido = { true }
        )

        val principales = items.filterIsInstance<ItemAgrupadoJerarquico.CabeceraPrincipal>()
        assertEquals(3, principales.size)
        assertEquals("categoria-cat-fin", principales[0].claveGrupo)
        assertEquals("categoria-cat-dev", principales[1].claveGrupo)
        assertEquals(setOf("2", "3"), principales[1].idsEntradas)
        assertEquals("categoria-__SIN_CATEGORIA__", principales[2].claveGrupo)
        assertEquals(setOf("4"), principales[2].idsEntradas)

        val subcabeceras = items.filterIsInstance<ItemAgrupadoJerarquico.Subcabecera>()
        assertTrue(subcabeceras.any { it.claveGrupo == "subiden-cat-dev-iden-2" && it.idsEntradas == setOf("2", "3") })
    }

    @Test
    fun cuandoIdentidadesEstanDesactivadasAgrupaSoloPorCategoriasSinSubcabeceras() {
        val items = construirItemsAgrupadosJerarquicos(
            entradas = entradas,
            identidades = identidades,
            categorias = categorias,
            jerarquia = JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA,
            identidadesActivas = false,
            estaExpandido = { true }
        )

        val principales = items.filterIsInstance<ItemAgrupadoJerarquico.CabeceraPrincipal>()
        assertEquals(3, principales.size)
        val subcabeceras = items.filterIsInstance<ItemAgrupadoJerarquico.Subcabecera>()
        assertTrue(subcabeceras.isEmpty())

        val hojas = items.filterIsInstance<ItemAgrupadoJerarquico.EntradaHoja>()
        assertEquals(4, hojas.size)
    }

    @Test
    fun cuandoSeccionEstaPlegadaNoIncluyeSubcabecerasNiHojas() {
        val items = construirItemsAgrupadosJerarquicos(
            entradas = entradas,
            identidades = identidades,
            categorias = categorias,
            jerarquia = JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA,
            identidadesActivas = true,
            estaExpandido = { false }
        )

        val principales = items.filterIsInstance<ItemAgrupadoJerarquico.CabeceraPrincipal>()
        assertEquals(3, principales.size)
        val subcabeceras = items.filterIsInstance<ItemAgrupadoJerarquico.Subcabecera>()
        assertEquals(0, subcabeceras.size)
        val hojas = items.filterIsInstance<ItemAgrupadoJerarquico.EntradaHoja>()
        assertEquals(0, hojas.size)
    }
}
