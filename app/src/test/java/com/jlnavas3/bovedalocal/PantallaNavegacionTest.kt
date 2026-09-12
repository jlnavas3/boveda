package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PantallaNavegacionTest {

    @Test
    fun `verificar igualdad y distincion de rutas Pantalla`() {
        assertEquals(Pantalla.Lista, Pantalla.Lista)
        assertEquals(Pantalla.Generador, Pantalla.Generador)
        assertEquals(Pantalla.SaludBoveda, Pantalla.SaludBoveda)
        assertEquals(Pantalla.Ajustes, Pantalla.Ajustes)
        assertEquals(Pantalla.KitEmergencia, Pantalla.KitEmergencia)
        assertEquals(Pantalla.Escaner(), Pantalla.Escaner())
        assertEquals(Pantalla.Passkeys, Pantalla.Passkeys)
        assertEquals(Pantalla.Autenticador, Pantalla.Autenticador)
        assertEquals(Pantalla.Papelera, Pantalla.Papelera)
        assertEquals(Pantalla.AjustesSenuelo, Pantalla.AjustesSenuelo)

        val edicion1 = Pantalla.Editar("123", "abc")
        val edicion1Clon = Pantalla.Editar("123", "abc")
        val edicion2 = Pantalla.Editar("456", "")
        val edicionNueva = Pantalla.Editar(null)

        assertEquals(edicion1, edicion1Clon)
        assertNotEquals(edicion1, edicion2)
        assertNotEquals(edicion1, edicionNueva)
        assertEquals(null, edicionNueva.id)

        val detalle1 = Pantalla.Detalle("123")
        val detalle2 = Pantalla.Detalle("456")
        assertNotEquals(detalle1, detalle2)
    }

    @Test
    fun `simulacion de pila de navegacion para ViewModel`() {
        val pila = mutableListOf<Pantalla>()
        var pantallaActual: Pantalla = Pantalla.Lista

        fun ir(nuevaPantalla: Pantalla) {
            pila.add(pantallaActual)
            pantallaActual = nuevaPantalla
        }

        fun retroceder(): Boolean {
            if (pila.isNotEmpty()) {
                pantallaActual = pila.removeAt(pila.size - 1)
                return true
            }
            return false
        }

        // Flujo: Lista -> Ajustes -> SaludBoveda
        assertEquals(Pantalla.Lista, pantallaActual)
        ir(Pantalla.Ajustes)
        assertEquals(Pantalla.Ajustes, pantallaActual)
        assertEquals(1, pila.size)

        ir(Pantalla.SaludBoveda)
        assertEquals(Pantalla.SaludBoveda, pantallaActual)
        assertEquals(2, pila.size)

        // Retroceder de SaludBoveda -> Ajustes
        assertTrue(retroceder())
        assertEquals(Pantalla.Ajustes, pantallaActual)
        assertEquals(1, pila.size)

        // Retroceder de Ajustes -> Lista
        assertTrue(retroceder())
        assertEquals(Pantalla.Lista, pantallaActual)
        assertEquals(0, pila.size)

        // No hay más en la pila
        val pudoRetroceder = retroceder()
        assertEquals(false, pudoRetroceder)
        assertEquals(Pantalla.Lista, pantallaActual)
    }

    @Test
    fun `verificar valores y orden de CriterioOrdenacion`() {
        val criterios = CriterioOrdenacion.values()
        assertEquals(5, criterios.size)
        assertTrue(criterios.contains(CriterioOrdenacion.NOMBRE_AZ))
        assertTrue(criterios.contains(CriterioOrdenacion.NOMBRE_ZA))
        assertTrue(criterios.contains(CriterioOrdenacion.MODIFICACION_RECIENTE))
        assertTrue(criterios.contains(CriterioOrdenacion.CREACION_RECIENTE))
        assertTrue(criterios.contains(CriterioOrdenacion.ANTIGUEDAD))
    }
}
