package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MenuLateralTest {

    @Test
    fun `formateo de cantidad de entradas singular y plural`() {
        fun formatear(total: Int) = "$total ${if (total == 1) "entrada" else "entradas"}"

        assertEquals("0 entradas", formatear(0))
        assertEquals("1 entrada", formatear(1))
        assertEquals("2 entradas", formatear(2))
        assertEquals("42 entradas", formatear(42))
    }

    @Test
    fun `badge de papelera se muestra solo cuando hay elementos eliminados`() {
        fun badgePapelera(totalPapelera: Int): String? =
            if (totalPapelera > 0) totalPapelera.toString() else null

        assertNull(badgePapelera(0))
        assertEquals("1", badgePapelera(1))
        assertEquals("5", badgePapelera(5))
        assertEquals("12", badgePapelera(12))
    }

    @Test
    fun `las pantallas de navegacion del menu lateral son validas`() {
        val rutasMenu = listOf(
            Pantalla.Generador,
            Pantalla.Passkeys,
            Pantalla.Autenticador,
            Pantalla.SaludBoveda,
            Pantalla.Duplicados,
            Pantalla.Papelera,
            Pantalla.AcercaDe,
            Pantalla.Registro,
            Pantalla.Ajustes
        )

        assertEquals(9, rutasMenu.size)
        assertTrue(rutasMenu.contains(Pantalla.Generador))
        assertTrue(rutasMenu.contains(Pantalla.Duplicados))
        assertTrue(rutasMenu.contains(Pantalla.Ajustes))
        assertTrue(rutasMenu.contains(Pantalla.AcercaDe))
    }

    @Test
    fun `criterios de ordenacion tienen etiquetas descriptivas`() {
        assertEquals("Nombre (A-Z)", CriterioOrdenacion.NOMBRE_AZ.etiqueta)
        assertEquals("Nombre (Z-A)", CriterioOrdenacion.NOMBRE_ZA.etiqueta)
        assertEquals("Modificado recientemente", CriterioOrdenacion.MODIFICACION_RECIENTE.etiqueta)
        assertEquals("Añadido recientemente", CriterioOrdenacion.CREACION_RECIENTE.etiqueta)
        assertEquals("Más antiguos primero", CriterioOrdenacion.ANTIGUEDAD.etiqueta)
    }

    @Test
    fun `etiquetas de filtros segun tipo de entrada y favoritos`() {
        fun etiquetaFiltro(filtro: TipoEntrada?, soloFavoritos: Boolean): String = when {
            filtro == null && !soloFavoritos -> "Todo"
            filtro == TipoEntrada.LOGIN && soloFavoritos -> "Claves + ★"
            filtro == TipoEntrada.PASSKEY && soloFavoritos -> "Passkeys + ★"
            filtro == TipoEntrada.NOTA && soloFavoritos -> "Notas + ★"
            filtro == TipoEntrada.LOGIN -> "Claves"
            filtro == TipoEntrada.PASSKEY -> "Passkeys"
            filtro == TipoEntrada.NOTA -> "Notas"
            soloFavoritos -> "Favoritos"
            else -> "Filtros"
        }

        assertEquals("Todo", etiquetaFiltro(null, false))
        assertEquals("Claves", etiquetaFiltro(TipoEntrada.LOGIN, false))
        assertEquals("Passkeys", etiquetaFiltro(TipoEntrada.PASSKEY, false))
        assertEquals("Notas", etiquetaFiltro(TipoEntrada.NOTA, false))
        assertEquals("Favoritos", etiquetaFiltro(null, true))
        assertEquals("Claves + ★", etiquetaFiltro(TipoEntrada.LOGIN, true))
        assertEquals("Passkeys + ★", etiquetaFiltro(TipoEntrada.PASSKEY, true))
        assertEquals("Notas + ★", etiquetaFiltro(TipoEntrada.NOTA, true))
    }
}
