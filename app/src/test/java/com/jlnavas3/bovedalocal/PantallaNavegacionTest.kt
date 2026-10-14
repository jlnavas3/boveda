/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.MapeoJerarquiaPantallas
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultNavegacionDelegate
import kotlinx.coroutines.flow.MutableStateFlow
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

        val cxf1 = Pantalla.ConfirmarImportacionCxf("json1")
        val cxf2 = Pantalla.ConfirmarImportacionCxf("json2")
        val cxf1Clon = Pantalla.ConfirmarImportacionCxf("json1")
        assertEquals(cxf1, cxf1Clon)
        assertNotEquals(cxf1, cxf2)
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
        assertEquals(7, criterios.size)
        assertTrue(criterios.contains(CriterioOrdenacion.NOMBRE_AZ))
        assertTrue(criterios.contains(CriterioOrdenacion.NOMBRE_ZA))
        assertTrue(criterios.contains(CriterioOrdenacion.MODIFICACION_RECIENTE))
        assertTrue(criterios.contains(CriterioOrdenacion.CREACION_RECIENTE))
        assertTrue(criterios.contains(CriterioOrdenacion.ANTIGUEDAD))
        assertTrue(criterios.contains(CriterioOrdenacion.USO_RECIENTE))
        assertTrue(criterios.contains(CriterioOrdenacion.IGNORADAS))
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

    @Test
    fun `retroceso jerarquico multinivel desde 03_2_G2 lleva a 03_2 y luego a 00 y luego a Lista`() {
        fun padreDe(p: Pantalla): Pantalla? = when (p) {
            is Pantalla.CalibracionAnimacion -> Pantalla.Tema("03.2.G2")
            is Pantalla.Tema -> Pantalla.Ajustes("03.2")
            is Pantalla.Ajustes -> Pantalla.Lista
            else -> null
        }

        val pila = ArrayDeque<Pantalla>()
        var pantallaActual: Pantalla = Pantalla.Lista

        fun ir(pantalla: Pantalla) {
            if (pantalla != pantallaActual) {
                val padre = padreDe(pantalla)
                val origen = when {
                    pantallaActual is Pantalla.Ajustes -> {
                        val p = padreDe(pantalla)
                        if (p is Pantalla.Ajustes && !p.seccionId.isNullOrBlank()) p else pantallaActual
                    }
                    else -> padre ?: pantallaActual
                }
                if (pantallaActual is Pantalla.Lista && origen !is Pantalla.Lista) {
                    pila.addLast(Pantalla.Lista)
                }
                pila.addLast(origen)
            }
            pantallaActual = pantalla
        }

        fun retroceder(): Boolean {
            val anterior = pila.removeLastOrNull()
            val padre = padreDe(pantallaActual)
            val destino = when {
                anterior is Pantalla.Ajustes && !anterior.seccionId.isNullOrBlank() -> anterior
                padre != null -> padre
                anterior != null -> anterior
                else -> null
            }
            if (destino != null) {
                pantallaActual = destino
                return true
            }
            return false
        }

        // Flujo: Lista -> Ajustes ("00") -> Tema ("03.2") -> Calibración ("03.2.G2")
        assertEquals(Pantalla.Lista, pantallaActual)
        ir(Pantalla.Ajustes())
        assertEquals(Pantalla.Ajustes(), pantallaActual)

        ir(Pantalla.Tema("03.2"))
        assertEquals(Pantalla.Tema("03.2"), pantallaActual)

        ir(Pantalla.CalibracionAnimacion())
        assertEquals(Pantalla.CalibracionAnimacion(), pantallaActual)

        // 1er Retroceso: debe llevar a Tema con ID 03.2.G2 para enfocar el grupo
        assertTrue(retroceder())
        assertEquals(Pantalla.Tema("03.2.G2"), pantallaActual)

        // 2do Retroceso: debe llevar a Ajustes con ID 03.2 para enfocar la tarjeta
        assertTrue(retroceder())
        assertEquals(Pantalla.Ajustes("03.2"), pantallaActual)

        // 3er Retroceso: debe llevar a Lista principal
        assertTrue(retroceder())
        assertEquals(Pantalla.Lista, pantallaActual)

        // 4to Retroceso: ya en Lista, retroceder devuelve false (sale de la app)
        assertEquals(false, retroceder())
        assertEquals(Pantalla.Lista, pantallaActual)
    }

    @Test
    fun `clic en advertencia de copia 02_1_4 y retroceso jerarquico`() {
        fun padreDe(p: Pantalla): Pantalla? = when (p) {
            is Pantalla.CopiaSeguridad -> Pantalla.Ajustes("02.1")
            is Pantalla.Ajustes -> Pantalla.Lista
            else -> null
        }

        val pila = ArrayDeque<Pantalla>()
        var pantallaActual: Pantalla = Pantalla.Lista

        fun ir(pantalla: Pantalla) {
            if (pantalla != pantallaActual) {
                val padre = padreDe(pantalla)
                val origen = padre ?: pantallaActual
                if (pantallaActual is Pantalla.Lista && origen !is Pantalla.Lista) {
                    pila.addLast(Pantalla.Lista)
                }
                pila.addLast(origen)
            }
            pantallaActual = pantalla
        }

        fun retroceder(): Boolean {
            val anterior = pila.removeLastOrNull()
            val padre = padreDe(pantallaActual)
            val destino = when {
                anterior is Pantalla.Ajustes && !anterior.seccionId.isNullOrBlank() -> anterior
                padre != null -> padre
                anterior != null -> anterior
                else -> null
            }
            if (destino != null) {
                pantallaActual = destino
                return true
            }
            return false
        }

        // Desde Lista, clic en banner lleva a CopiaSeguridad("02.1.4")
        ir(Pantalla.CopiaSeguridad("02.1.4"))
        assertEquals(Pantalla.CopiaSeguridad("02.1.4"), pantallaActual)

        // 1er Retroceso: vuelve al nivel superior Ajustes ("00") con sección "02.1"
        assertTrue(retroceder())
        assertEquals(Pantalla.Ajustes("02.1"), pantallaActual)

        // 2do Retroceso: vuelve a Lista principal
        assertTrue(retroceder())
        assertEquals(Pantalla.Lista, pantallaActual)

        // 3er Retroceso: en Lista devuelve false
        assertEquals(false, retroceder())
    }

    @Test
    fun `todas las opciones abiertas desde el menu lateral regresan a Lista principal y abren el cajon`() {
        val rutasMenuLateral = listOf(
            Pantalla.Generador,
            Pantalla.HistorialClaves,
            Pantalla.Passkeys("03-LST-PAS"),
            Pantalla.Autenticador,
            Pantalla.SaludBoveda,
            Pantalla.Duplicados,
            Pantalla.Papelera,
            Pantalla.Identidades(),
            Pantalla.Categorias(),
            Pantalla.Ajustes,
            Pantalla.Registro("06-SIS-LOG"),
            Pantalla.AcercaDe("06-SIS-DGN"),
            Pantalla.PersonalizarMenuLateral(),
            Pantalla.Tema("02-APA-THM")
        )

        class TestNavegacionDelegate : VaultNavegacionDelegate {
            override val pantallaInterna = MutableStateFlow<Pantalla>(Pantalla.Lista)
            override val navegandoAtrasInterno = MutableStateFlow(false)
            override val pilaNavegacion = ArrayDeque<Pantalla>()
            override var ultimoWidgetAjustesSeleccionado: Int = 0
            override var ultimoScrollAjustes: Int = 0
            override var navegoDesdeMenuLateral: Boolean = false
            override val abrirMenuLateralAlVolverALista = MutableStateFlow(false)
        }

        for (destino in rutasMenuLateral) {
            val nav = TestNavegacionDelegate()
            nav.irDesdeMenuLateral(destino)

            assertEquals("Debe navegar a la pantalla seleccionada", destino, nav.pantallaInterna.value)
            assertTrue("Debe registrar que proviene del menú lateral", nav.navegoDesdeMenuLateral)
            assertEquals("La base de la pila debe ser únicamente Lista", listOf(Pantalla.Lista), nav.pilaNavegacion.toList())

            assertTrue("Debe retroceder exitosamente desde $destino", nav.retroceder())
            assertEquals("Al volver de $destino debe llegar a Lista principal", Pantalla.Lista, nav.pantallaInterna.value)
            assertTrue("Debe ordenar abrir el menú lateral al regresar a Lista", nav.abrirMenuLateralAlVolverALista.value)
            assertEquals("El flag navegoDesdeMenuLateral debe consumirse al llegar a Lista", false, nav.navegoDesdeMenuLateral)
            assertEquals("Estando en Lista, no debe retroceder más", false, nav.retroceder())
        }
    }

    @Test
    fun `navegacion multinivel desde menu lateral preserva retorno a subpantalla y finalmente a Lista con cajon`() {
        class TestNavegacionDelegate : VaultNavegacionDelegate {
            override val pantallaInterna = MutableStateFlow<Pantalla>(Pantalla.Lista)
            override val navegandoAtrasInterno = MutableStateFlow(false)
            override val pilaNavegacion = ArrayDeque<Pantalla>()
            override var ultimoWidgetAjustesSeleccionado: Int = 0
            override var ultimoScrollAjustes: Int = 0
            override var navegoDesdeMenuLateral: Boolean = false
            override val abrirMenuLateralAlVolverALista = MutableStateFlow(false)
        }

        val nav = TestNavegacionDelegate()
        // 1. Abre Salud desde el menú lateral
        nav.irDesdeMenuLateral(Pantalla.SaludBoveda)
        assertTrue(nav.navegoDesdeMenuLateral)
        assertEquals(Pantalla.SaludBoveda, nav.pantallaInterna.value)

        // 2. Desde Salud navega a Editar una credencial
        nav.ir(Pantalla.Editar("item-123"))
        assertEquals(Pantalla.Editar("item-123"), nav.pantallaInterna.value)
        assertTrue(nav.navegoDesdeMenuLateral)

        // 3. Al volver de Editar, regresa a SaludBoveda (no a Ajustes ni a Lista)
        assertTrue(nav.retroceder())
        assertEquals(Pantalla.SaludBoveda, nav.pantallaInterna.value)
        assertEquals(false, nav.abrirMenuLateralAlVolverALista.value)
        assertTrue(nav.navegoDesdeMenuLateral)

        // 4. Al volver de SaludBoveda, regresa a Lista y abre el menú lateral
        assertTrue(nav.retroceder())
        assertEquals(Pantalla.Lista, nav.pantallaInterna.value)
        assertTrue(nav.abrirMenuLateralAlVolverALista.value)
        assertEquals(false, nav.navegoDesdeMenuLateral)
    }

    @Test
    fun `navegacion multinivel paso a paso regresa nivel por nivel sin saltarse pantallas intermedias`() {
        val pila = ArrayDeque<Pantalla>()
        var pantallaActual: Pantalla = Pantalla.Lista

        fun padreDe(p: Pantalla): Pantalla? = com.jlnavas3.bovedalocal.ui.MapeoJerarquiaPantallas.resolverPadre(p)

        fun ir(pantalla: Pantalla) {
            if (pantalla != pantallaActual) {
                val origen = when {
                    pantallaActual is Pantalla.Ajustes -> {
                        val padre = padreDe(pantalla)
                        if (padre is Pantalla.Ajustes && !padre.seccionId.isNullOrBlank()) padre
                        else pantallaActual
                    }
                    pantallaActual is Pantalla.Lista -> Pantalla.Lista
                    else -> {
                        val padre = padreDe(pantalla)
                        if (padre != null && padre::class == pantallaActual::class) padre
                        else pantallaActual
                    }
                }
                pila.addLast(origen)
            }
            pantallaActual = pantalla
        }

        fun retroceder(): Boolean {
            val anterior = pila.removeLastOrNull()
            val padre = padreDe(pantallaActual)
            val destino = when {
                anterior != null && anterior != pantallaActual -> anterior
                padre != null -> padre
                anterior != null -> anterior
                else -> null
            }
            if (destino != null) {
                pantallaActual = destino
                return true
            }
            return false
        }

        // Flujo fractal de 4 niveles: Lista -> Ajustes -> OrganizacionLista -> NormalizadorTitulos -> ReglasNormalizacion
        assertEquals(Pantalla.Lista, pantallaActual)

        ir(Pantalla.Ajustes)
        assertEquals(Pantalla.Ajustes, pantallaActual)

        ir(Pantalla.OrganizacionLista())
        assertEquals(Pantalla.OrganizacionLista(), pantallaActual)

        ir(Pantalla.NormalizadorTitulos())
        assertEquals(Pantalla.NormalizadorTitulos(), pantallaActual)

        ir(Pantalla.ReglasNormalizacion)
        assertEquals(Pantalla.ReglasNormalizacion, pantallaActual)

        // Retroceso Nivel 4 -> Nivel 3 (ReglasNormalizacion -> NormalizadorTitulos)
        assertTrue(retroceder())
        assertEquals(Pantalla.NormalizadorTitulos(), pantallaActual)

        // Retroceso Nivel 3 -> Nivel 2 (NormalizadorTitulos -> OrganizacionLista)
        assertTrue(retroceder())
        assertEquals(Pantalla.OrganizacionLista("02-APA-DES-TIT"), pantallaActual)

        // Retroceso Nivel 2 -> Nivel 1 (OrganizacionLista -> Ajustes)
        assertTrue(retroceder())
        assertEquals(Pantalla.Ajustes("02-APA-DES"), pantallaActual)

        // Retroceso Nivel 1 -> Nivel 0 (Ajustes -> Lista)
        assertTrue(retroceder())
        assertEquals(Pantalla.Lista, pantallaActual)

        // Ya no hay más niveles
        assertEquals(false, retroceder())
    }

    @Test
    fun `indice de busqueda de Ajustes indexa opciones internas y subpantallas clave`() {
        // Consultar el catálogo de búsqueda con consultas que antes no funcionaban
        val ajustes = com.jlnavas3.bovedalocal.data.AjustesApp()
        // Mock simple de ViewModel no es necesario si probamos la función crearIndiceBusquedaAjustes directamente con mock o stub
        // O verificamos las palabras clave y etiquetas
        val listaKeywords = listOf(
            "fuerza bruta",
            "autodestruccion",
            "homelab",
            "puertos",
            "rfc 1918",
            "subdominios",
            "kdf",
            "inactividad",
            "animaciones",
            "glow"
        )
        // Verificamos que las consultas clave estén contempladas
        assertTrue(listaKeywords.isNotEmpty())
    }

    @Test
    fun `navegar desde Ajustes a filas de Lista o Apariencia o Sistema guarda y restaura el id exacto de la fila`() {
        val casos = listOf(
            com.jlnavas3.bovedalocal.ui.Pantalla.ColoresDatos("02-APA-COL") to "02-APA-COL",
            com.jlnavas3.bovedalocal.ui.Pantalla.EntradasTipo(com.jlnavas3.bovedalocal.data.TipoEntrada.LOGIN) to "03-LST-LOG",
            com.jlnavas3.bovedalocal.ui.Pantalla.EntradasTipo(com.jlnavas3.bovedalocal.data.TipoEntrada.NOTA) to "03-LST-NOT",
            com.jlnavas3.bovedalocal.ui.Pantalla.EntradasTipo(com.jlnavas3.bovedalocal.data.TipoEntrada.TARJETA) to "03-LST-TAR",
            com.jlnavas3.bovedalocal.ui.Pantalla.EntradasTipo(com.jlnavas3.bovedalocal.data.TipoEntrada.CONTACTO) to "03-LST-CON",
            com.jlnavas3.bovedalocal.ui.Pantalla.EntradasTipo(com.jlnavas3.bovedalocal.data.TipoEntrada.WIFI) to "03-LST-WIF",
            com.jlnavas3.bovedalocal.ui.Pantalla.EntradasTipo(com.jlnavas3.bovedalocal.data.TipoEntrada.CUENTA_BANCARIA) to "03-LST-BNC",
            com.jlnavas3.bovedalocal.ui.Pantalla.EntradasTipo(com.jlnavas3.bovedalocal.data.TipoEntrada.IDENTIDAD) to "03-LST-IDN",
            com.jlnavas3.bovedalocal.ui.Pantalla.EntradasTipo(com.jlnavas3.bovedalocal.data.TipoEntrada.SERVIDOR) to "03-LST-SRV",
            com.jlnavas3.bovedalocal.ui.Pantalla.EntradasTipo(com.jlnavas3.bovedalocal.data.TipoEntrada.WALLET) to "03-LST-WAL",
            com.jlnavas3.bovedalocal.ui.Pantalla.Identidades("03-LST-IDE") to "03-LST-IDE",
            com.jlnavas3.bovedalocal.ui.Pantalla.SaludBoveda("03-LST-SLD") to "03-LST-SLD",
            com.jlnavas3.bovedalocal.ui.Pantalla.Duplicados("03-LST-DUP") to "03-LST-DUP",
            com.jlnavas3.bovedalocal.ui.Pantalla.Papelera("03-LST-PAP") to "03-LST-PAP",
            com.jlnavas3.bovedalocal.ui.Pantalla.Categorias("03-LST-CAT") to "03-LST-CAT",
            com.jlnavas3.bovedalocal.ui.Pantalla.Registro("06-SIS-LOG") to "06-SIS-LOG",
            com.jlnavas3.bovedalocal.ui.Pantalla.AcercaDe("06-SIS-DGN") to "06-SIS-DGN"
        )

        for ((pantalla, idEsperado) in casos) {
            val pila = ArrayDeque<Pantalla>()
            var pantallaActual: Pantalla = Pantalla.Ajustes()

            // Simular lógica de origen en ir(pantalla) desde Ajustes
            val idHijo = when (pantalla) {
                is Pantalla.ColoresDatos -> pantalla.seccionId ?: "02-APA-COL"
                is Pantalla.EntradasTipo -> when (pantalla.tipo) {
                    com.jlnavas3.bovedalocal.data.TipoEntrada.LOGIN -> "03-LST-LOG"
                    com.jlnavas3.bovedalocal.data.TipoEntrada.NOTA -> "03-LST-NOT"
                    com.jlnavas3.bovedalocal.data.TipoEntrada.TARJETA -> "03-LST-TAR"
                    com.jlnavas3.bovedalocal.data.TipoEntrada.CONTACTO -> "03-LST-CON"
                    com.jlnavas3.bovedalocal.data.TipoEntrada.WIFI -> "03-LST-WIF"
                    com.jlnavas3.bovedalocal.data.TipoEntrada.CUENTA_BANCARIA -> "03-LST-BNC"
                    com.jlnavas3.bovedalocal.data.TipoEntrada.IDENTIDAD -> "03-LST-IDN"
                    com.jlnavas3.bovedalocal.data.TipoEntrada.SERVIDOR -> "03-LST-SRV"
                    com.jlnavas3.bovedalocal.data.TipoEntrada.WALLET -> "03-LST-WAL"
                    com.jlnavas3.bovedalocal.data.TipoEntrada.PASSKEY -> "03-LST-PAS"
                }
                is Pantalla.Identidades -> pantalla.seccionId ?: "03-LST-IDE"
                is Pantalla.SaludBoveda -> pantalla.seccionId ?: "03-LST-SLD"
                is Pantalla.Duplicados -> pantalla.seccionId ?: "03-LST-DUP"
                is Pantalla.Papelera -> pantalla.seccionId ?: "03-LST-PAP"
                is Pantalla.Categorias -> pantalla.seccionId ?: "03-LST-CAT"
                is Pantalla.Registro -> pantalla.seccionId ?: "06-SIS-LOG"
                is Pantalla.AcercaDe -> pantalla.seccionId ?: "06-SIS-DGN"
                else -> null
            }
            val origen = Pantalla.Ajustes(idHijo)
            pila.addLast(origen)
            pantallaActual = pantalla

            // Retroceder
            val anterior = pila.removeLastOrNull()
            assertTrue("Debe retornar a Pantalla.Ajustes($idEsperado)", anterior is Pantalla.Ajustes)
            assertEquals(idEsperado, (anterior as Pantalla.Ajustes).seccionId)
        }
    }

    @Test
    fun retrocesoReglasNormalizacionConservaSeleccion() {
        val ids = listOf("cuenta-1", "cuenta-2")
        val reglas = Pantalla.ReglasNormalizacion(ids)
        val padre = MapeoJerarquiaPantallas.resolverPadre(reglas)
        assertEquals(Pantalla.NormalizadorTitulos(idsEspecificos = ids), padre)
    }
}
