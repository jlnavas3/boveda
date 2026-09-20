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
        assertEquals(Pantalla.Seguridad, Pantalla.Seguridad)
        assertEquals(Pantalla.CopiaSeguridad, Pantalla.CopiaSeguridad)
        assertEquals(Pantalla.CsvGoogle, Pantalla.CsvGoogle)
        assertEquals(Pantalla.Argon2id, Pantalla.Argon2id)
        assertEquals(Pantalla.AjustesAutenticador, Pantalla.AjustesAutenticador)
        assertEquals(Pantalla.AjustesCamara, Pantalla.AjustesCamara)
        assertEquals(Pantalla.TileRapido, Pantalla.TileRapido)
        assertEquals(Pantalla.Avanzada, Pantalla.Avanzada)
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

    @Test
    fun `navegacion contextual por ID regresa a la seccion o tarjeta padre sin saltos`() {
        val pila = ArrayDeque<Pantalla>()
        var pantallaActual: Pantalla = Pantalla.Ajustes(null)

        fun irPorId(id: String) {
            val destino = when {
                id.startsWith("03.2") -> Pantalla.Tema(id)
                id.startsWith("01.2") -> Pantalla.AjustesSenuelo(id)
                else -> Pantalla.Ajustes(id)
            }
            val origen = if (pantallaActual is Pantalla.Ajustes) Pantalla.Ajustes(id) else pantallaActual
            pila.addLast(origen)
            pantallaActual = destino
        }

        fun retroceder(): Boolean {
            val anterior = pila.removeLastOrNull()
            if (anterior != null) {
                pantallaActual = anterior
                return true
            }
            return false
        }

        // Usuario en Ajustes abre "Personalizar colores" (ID 03.2)
        irPorId("03.2")
        assertEquals(Pantalla.Tema("03.2"), pantallaActual)
        assertEquals(1, pila.size)
        assertEquals(Pantalla.Ajustes("03.2"), pila.last())

        // Al retroceder, debe volver a Ajustes con la seccion 03.2 para abrirla y enfocarla
        assertTrue(retroceder())
        assertEquals(Pantalla.Ajustes("03.2"), pantallaActual)
        assertEquals(0, pila.size)

        // Usuario en Ajustes abre "Boveda Senuelo" (ID 01.2)
        irPorId("01.2")
        assertEquals(Pantalla.AjustesSenuelo("01.2"), pantallaActual)
        assertTrue(retroceder())
        assertEquals(Pantalla.Ajustes("01.2"), pantallaActual)
    }

    @Test
    fun `verificar resolucion completa del arbol de IDs 01 a 05 y barra lateral`() {
        fun resolverId(id: String): Pantalla {
            val limpio = id.trim()
            return when {
                // Grupo 01: Seguridad
                limpio == "01" || limpio.startsWith("01.1") || limpio.startsWith("01.0") -> Pantalla.Seguridad(limpio)
                limpio.startsWith("01.2") -> Pantalla.AjustesSenuelo(limpio)
                limpio.startsWith("01.3") -> Pantalla.AjustesAutodestruccion(limpio)
                limpio.startsWith("01.4") || limpio == "03" || limpio.startsWith("03.0") -> Pantalla.Argon2id(limpio)

                // Grupo 02: Cuentas y Datos
                limpio == "02" || limpio.startsWith("02.1") || limpio == "06" || limpio.startsWith("06.0") -> Pantalla.CopiaSeguridad(limpio)
                limpio.startsWith("02.2") || limpio == "08" || limpio.startsWith("08.0") -> Pantalla.CsvGoogle(limpio)
                limpio.startsWith("02.3") || limpio.startsWith("06.3") -> Pantalla.KitEmergencia(limpio)
                limpio.startsWith("02.4") || limpio.startsWith("06.1") -> Pantalla.SaludBoveda(limpio)
                limpio.startsWith("02.5") || limpio.startsWith("06.2") -> Pantalla.Duplicados(limpio)
                limpio.startsWith("02.6") || limpio.startsWith("06.4") -> Pantalla.Papelera(limpio)

                // Grupo 03: Personalización
                limpio.startsWith("03.1") || limpio.startsWith("09.1") -> Pantalla.Ajustes("03.1")
                limpio.startsWith("03.2") || limpio.startsWith("09.2") -> Pantalla.Tema(limpio)
                limpio.startsWith("03.3") || limpio.startsWith("09.4") -> Pantalla.AjustesWidget(limpio)
                limpio.startsWith("03.4") || limpio.startsWith("09.5") -> Pantalla.AjustesIndice(limpio)
                limpio.startsWith("03.5") || limpio.startsWith("09.6") || limpio.startsWith("09.7") -> Pantalla.OrganizacionLista(limpio)
                limpio.startsWith("03.6") || limpio.startsWith("10") -> Pantalla.FormatosCampos(limpio)

                // Grupo 04: Funciones y Herramientas
                limpio.startsWith("04.1") || limpio == "07" || limpio.startsWith("07.0") -> Pantalla.AjustesAutenticador(limpio)
                limpio.startsWith("04.2") || limpio == "05" || limpio.startsWith("05.0") -> Pantalla.AjustesCamara(limpio)
                limpio.startsWith("04.3") -> Pantalla.TileRapido(limpio)
                limpio.startsWith("04.4") -> Pantalla.Generador
                limpio.startsWith("04.5") || limpio == "04" || limpio.startsWith("04.0") -> Pantalla.HistorialClaves(limpio)
                limpio.startsWith("04.6") -> Pantalla.Passkeys
                limpio.startsWith("04.7") -> Pantalla.Autenticador

                // Grupo 05: Sistema
                limpio.startsWith("05.1") || limpio.startsWith("11.1") || limpio.startsWith("11.2") || limpio.startsWith("11.4") || limpio == "11" -> Pantalla.Avanzada(limpio)
                limpio.startsWith("05.2") || limpio.startsWith("11.3.2") -> Pantalla.Registro(limpio)
                limpio.startsWith("05.3") || limpio.startsWith("11.3.1") -> Pantalla.AcercaDe(limpio)

                else -> Pantalla.Ajustes(limpio)
            }
        }

        // Grupo 01
        assertEquals(Pantalla.Seguridad("01.1"), resolverId("01.1"))
        assertEquals(Pantalla.AjustesSenuelo("01.2"), resolverId("01.2"))
        assertEquals(Pantalla.AjustesAutodestruccion("01.3"), resolverId("01.3"))
        assertEquals(Pantalla.Argon2id("01.4"), resolverId("01.4"))

        // Grupo 02
        assertEquals(Pantalla.CopiaSeguridad("02.1"), resolverId("02.1"))
        assertEquals(Pantalla.CsvGoogle("02.2"), resolverId("02.2"))
        assertEquals(Pantalla.KitEmergencia("02.3"), resolverId("02.3"))
        assertEquals(Pantalla.SaludBoveda("02.4"), resolverId("02.4"))
        assertEquals(Pantalla.Duplicados("02.5"), resolverId("02.5"))
        assertEquals(Pantalla.Papelera("02.6"), resolverId("02.6"))

        // Grupo 03
        assertEquals(Pantalla.Ajustes("03.1"), resolverId("03.1"))
        assertEquals(Pantalla.Tema("03.2"), resolverId("03.2"))
        assertEquals(Pantalla.AjustesWidget("03.3"), resolverId("03.3"))
        assertEquals(Pantalla.AjustesIndice("03.4"), resolverId("03.4"))
        assertEquals(Pantalla.OrganizacionLista("03.5"), resolverId("03.5"))
        assertEquals(Pantalla.FormatosCampos("03.6"), resolverId("03.6"))

        // Grupo 04
        assertEquals(Pantalla.AjustesAutenticador("04.1"), resolverId("04.1"))
        assertEquals(Pantalla.AjustesCamara("04.2"), resolverId("04.2"))
        assertEquals(Pantalla.TileRapido("04.3"), resolverId("04.3"))
        assertEquals(Pantalla.Generador, resolverId("04.4"))
        assertEquals(Pantalla.HistorialClaves("04.5"), resolverId("04.5"))
        assertEquals(Pantalla.Passkeys, resolverId("04.6"))
        assertEquals(Pantalla.Autenticador, resolverId("04.7"))

        // Grupo 05
        assertEquals(Pantalla.Avanzada("05.1"), resolverId("05.1"))
        assertEquals(Pantalla.Registro("05.2"), resolverId("05.2"))
        assertEquals(Pantalla.AcercaDe("05.3"), resolverId("05.3"))
    }
}
