package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion
import com.jlnavas3.bovedalocal.util.filtrarColeccionesPorIdentidad
import com.jlnavas3.bovedalocal.util.filtrarIdentidadesPorColeccion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JerarquiaOrganizacionTest {

    private val idenPersonal = Identidad(id = "iden-1", nombre = "Personal", correoPrincipal = "personal@test.com")
    private val idenTrabajo = Identidad(id = "iden-2", nombre = "Trabajo", correoPrincipal = "trabajo@test.com")
    private val identidades = listOf(idenPersonal, idenTrabajo)

    private val colFinanzas = Coleccion(id = "col-fin", nombre = "Finanzas")
    private val colDev = Coleccion(id = "col-dev", nombre = "Desarrollo")
    private val colGeneral = Coleccion(id = "col-gen", nombre = "General")
    private val colecciones = listOf(colFinanzas, colDev, colGeneral)

    private val e1 = Entrada(id = "1", titulo = "Banco", usuario = "personal@test.com", colecciones = listOf("col-fin"))
    private val e2 = Entrada(id = "2", titulo = "GitHub Trabajo", usuario = "trabajo@test.com", colecciones = listOf("col-dev"))
    private val e3 = Entrada(id = "3", titulo = "Server AWS", usuario = "trabajo@test.com", colecciones = listOf("col-dev"))
    private val e4 = Entrada(id = "4", titulo = "Router Casa", usuario = "admin", colecciones = listOf("col-gen")) // sin identidad

    private val entradas = listOf(e1, e2, e3, e4)

    @Test
    fun `cuando identidad es nivel superior y se selecciona Trabajo solo aparecen colecciones de Trabajo`() {
        val (cols, conteos) = filtrarColeccionesPorIdentidad(
            colecciones = colecciones,
            entradas = entradas,
            identidades = identidades,
            identidadSeleccionadaId = idenTrabajo.id
        )

        assertEquals(1, cols.size)
        assertEquals("col-dev", cols.first().id)
        assertEquals(2, conteos["col-dev"])
        assertEquals(0, conteos["col-fin"])
    }

    @Test
    fun `cuando identidad es nivel superior y se selecciona Todas aparecen todas las colecciones con conteo total`() {
        val (cols, conteos) = filtrarColeccionesPorIdentidad(
            colecciones = colecciones,
            entradas = entradas,
            identidades = identidades,
            identidadSeleccionadaId = null
        )

        assertEquals(3, cols.size)
        assertEquals(1, conteos["col-fin"])
        assertEquals(2, conteos["col-dev"])
        assertEquals(1, conteos["col-gen"])
    }

    @Test
    fun `cuando coleccion es nivel superior y se selecciona Desarrollo solo aparecen identidades presentes en Desarrollo`() {
        val (idens, conteos, sinIden) = filtrarIdentidadesPorColeccion(
            identidades = identidades,
            entradas = entradas,
            coleccionSeleccionadaId = colDev.id
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
            JerarquiaOrganizacion.IDENTIDAD_SOBRE_COLECCION,
            JerarquiaOrganizacion.desdeClave("identidad_sobre_coleccion")
        )
        assertEquals(
            JerarquiaOrganizacion.COLECCION_SOBRE_IDENTIDAD,
            JerarquiaOrganizacion.desdeClave("coleccion_sobre_identidad")
        )
        assertEquals(
            JerarquiaOrganizacion.IDENTIDAD_SOBRE_COLECCION,
            JerarquiaOrganizacion.desdeClave("clave_desconocida")
        )
    }
}
