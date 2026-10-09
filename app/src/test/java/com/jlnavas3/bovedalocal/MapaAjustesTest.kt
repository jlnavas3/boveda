package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.ui.MapeoJerarquiaPantallas
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MapaAjustesTest {

    @Test
    fun `raices tienen iconos y subnodos tienen icono estrictamente null`() {
        val raices = MapaAjustes.NODOS_RAIZ
        assertTrue("Debe haber nodos raíz", raices.isNotEmpty())

        raices.forEach { raiz ->
            assertNotNull("La opción raíz ${raiz.id} debe tener icono squircle", raiz.icono)
            assertNotNull("La opción raíz ${raiz.id} debe tener color de icono", raiz.colorIcono)
            assertTrue("La opción raíz ${raiz.id} debe estar marcada como esRaiz", raiz.esRaiz)
        }

        val subnodos = MapaAjustes.SUBNODOS
        assertTrue("Debe haber subnodos", subnodos.isNotEmpty())

        val subnodosN3 = subnodos.filter { it.padreId != null && !it.padreId!!.matches(Regex("^0[1-6]-[A-Z]{3}$")) }
        assertTrue("Debe haber subnodos N3+", subnodosN3.isNotEmpty())

        subnodosN3.forEach { subnodo ->
            assertNull("El subnodo N3+ ${subnodo.id} debe tener icono null (regla Pixel / iOS)", subnodo.icono)
            assertNull("El subnodo N3+ ${subnodo.id} debe tener colorIcono null", subnodo.colorIcono)
            assertEquals("El subnodo ${subnodo.id} no debe ser raíz", false, subnodo.esRaiz)
            assertNotNull("El subnodo ${subnodo.id} debe tener padreId", subnodo.padreId)
        }
    }

    @Test
    fun `obtenerNodosRaiz respeta el orden personalizado del usuario`() {
        val ordenPorDefecto = MapaAjustes.obtenerNodosRaiz()
        assertEquals(MapaAjustes.NODOS_RAIZ.size, ordenPorDefecto.size)

        // Supongamos que el usuario mueve Sistema (06-SIS) y Apariencia (02-APA) al inicio
        val ordenPersonalizado = listOf("06-SIS", "02-APA")
        val reordenados = MapaAjustes.obtenerNodosRaiz(ordenPersonalizado)

        assertEquals("06-SIS", reordenados[0].id)
        assertEquals("02-APA", reordenados[1].id)
        assertEquals(MapaAjustes.NODOS_RAIZ.size, reordenados.size)
    }

    @Test
    fun `buscarNodos encuentra opciones por titulo palabras clave y id`() {
        val resultadosBiometria = MapaAjustes.buscarNodos("biometria")
        assertTrue(resultadosBiometria.any { it.id == "01-SEG-BIO" })

        val resultadosReorganizar = MapaAjustes.buscarNodos("reorganizar")
        assertTrue(resultadosReorganizar.any { it.id == "06-SIS-AVZ-ORG" })

        val resultadosPorId = MapaAjustes.buscarNodos("01-SEG-BIO-BLO")
        assertTrue(resultadosPorId.any { it.id == "01-SEG-BIO-BLO" })
    }

    @Test
    fun `jerarquia de retorno LIFO para subpantallas de seguridad y reorganizacion`() {
        // BloqueoBiometria debe retornar a Seguridad con id de alumbrado
        val padreBloqueo = MapeoJerarquiaPantallas.resolverPadre(Pantalla.BloqueoBiometria())
        assertEquals(Pantalla.Seguridad("01-SEG-BIO-BLO"), padreBloqueo)

        // SeguridadVisualMemoria debe retornar a Seguridad con id de alumbrado
        val padreVisual = MapeoJerarquiaPantallas.resolverPadre(Pantalla.SeguridadVisualMemoria())
        assertEquals(Pantalla.Seguridad("01-SEG-BIO-VIS"), padreVisual)

        // ReorganizarAjustes debe retornar a Avanzada con id de alumbrado
        val padreReorganizar = MapeoJerarquiaPantallas.resolverPadre(Pantalla.ReorganizarAjustes)
        assertEquals(Pantalla.Avanzada("06-SIS-AVZ-ORG"), padreReorganizar)
    }

    @Test
    fun `asistente de titulos es hijo de 03-LST-DES y retorna a OrganizacionLista`() {
        val nodoTitulos = MapaAjustes.buscarPorId("03-LST-DES-TIT")
        assertNotNull("Debe existir el nodo 03-LST-DES-TIT", nodoTitulos)
        assertEquals("03-LST-DES", nodoTitulos?.padreId)

        val hijosDiseno = MapaAjustes.obtenerHijosDe("03-LST-DES")
        assertTrue("03-LST-DES-TIT debe ser hijo de 03-LST-DES", hijosDiseno.any { it.id == "03-LST-DES-TIT" })

        val padrePantalla = MapeoJerarquiaPantallas.resolverPadre(Pantalla.NormalizadorTitulos())
        assertEquals(Pantalla.OrganizacionLista("03-LST-DES-TIT"), padrePantalla)
    }
}
