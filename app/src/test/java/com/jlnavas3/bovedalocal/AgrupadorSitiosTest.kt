package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import com.jlnavas3.bovedalocal.util.claveAgrupacionSitio
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorSitio
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AgrupadorSitiosTest {

    @Test
    fun `las urls android usan el titulo importado y no el paquete com`() {
        val entradas = listOf(
            Entrada("1", titulo = "brakepak.com", urls = listOf("android://token@com.brakepak/")),
            Entrada("2", titulo = "gaia.com", urls = listOf("android://token@com.gaiamtv/")),
            Entrada("3", titulo = "mercadolibre.com", urls = listOf("android://token@com.mercadolibre/")),
            Entrada("4", titulo = "waze.com", urls = listOf("android://token@com.waze/"))
        )

        val items = construirItemsAgrupadosPorSitio(entradas) { false }

        assertEquals(entradas.size, items.size)
        assertEquals(true, items.all { it is ItemAgrupado.Suelto })
    }

    @Test
    fun `agrupa entradas que comparten el mismo sitio con distintos dominios o tlds`() {
        val e1 = Entrada("1", titulo = "Amazon US", urls = listOf("https://www.amazon.com/login"))
        val e2 = Entrada("2", titulo = "Amazon ES", urls = listOf("https://amazon.es/ap/signin"))
        val e3 = Entrada("3", titulo = "GitHub", urls = listOf("https://github.com"))

        val items = construirItemsAgrupadosPorSitio(listOf(e1, e2, e3)) { false }

        // Deben ser 2 items: 1 Grupo (Amazon con 2 entradas) y 1 Suelto (GitHub)
        assertEquals(2, items.size)
        val grupoAmazon = items[0] as ItemAgrupado.Grupo
        assertEquals("amazon", grupoAmazon.clave)
        assertEquals(2, grupoAmazon.entradas.size)

        val sueltoGithub = items[1] as ItemAgrupado.Suelto
        assertEquals("GitHub", sueltoGithub.entrada.titulo)
    }

    @Test
    fun `cuando un grupo esta expandido incluye a sus hijos en la lista`() {
        val e1 = Entrada("1", titulo = "Google Personal", urls = listOf("https://accounts.google.com/u/1"))
        val e2 = Entrada("2", titulo = "Google Trabajo", urls = listOf("https://accounts.google.com/u/2"))

        val items = construirItemsAgrupadosPorSitio(listOf(e1, e2)) { clave -> clave == "accounts.google" }

        // 1 Grupo + 2 Hijos
        assertEquals(3, items.size)
        assertTrue(items[0] is ItemAgrupado.Grupo)
        assertEquals("accounts.google", (items[0] as ItemAgrupado.Grupo).clave)
        assertTrue(items[1] is ItemAgrupado.Hijo)
        assertTrue(items[2] is ItemAgrupado.Hijo)
        assertEquals("1", (items[1] as ItemAgrupado.Hijo).entrada.id)
        assertEquals("2", (items[2] as ItemAgrupado.Hijo).entrada.id)
    }

    @Test
    fun `subdominios distintos como account xiaomi y xiaomi no se mezclan y respetan orden alfabetico`() {
        val eAccount1 = Entrada("1", titulo = "account.xiaomi.com (personal)", urls = listOf("https://account.xiaomi.com"))
        val eAccount2 = Entrada("2", titulo = "account.xiaomi.com (trabajo)", urls = listOf("https://account.xiaomi.com"))
        val eXiaomi1 = Entrada("3", titulo = "xiaomi.com (tienda)", urls = listOf("https://xiaomi.com"))
        val eXiaomi2 = Entrada("4", titulo = "xiaomi.com (comunidad)", urls = listOf("https://xiaomi.com"))

        val items = construirItemsAgrupadosPorSitio(
            listOf(eXiaomi1, eXiaomi2, eAccount1, eAccount2),
            criterio = CriterioOrdenacion.NOMBRE_AZ,
            agrupar = true
        ) { false }

        // Deben ser 2 grupos independientes: account.xiaomi y xiaomi
        assertEquals(2, items.size)
        val g1 = items[0] as ItemAgrupado.Grupo
        val g2 = items[1] as ItemAgrupado.Grupo

        assertEquals("account.xiaomi", g1.clave)
        assertEquals(2, g1.entradas.size)

        assertEquals("xiaomi", g2.clave)
        assertEquals(2, g2.entradas.size)
    }

    @Test
    fun `cuando agrupar es false devuelve lista plana de entradas sueltas`() {
        val e1 = Entrada("1", titulo = "account.xiaomi.com", urls = listOf("https://account.xiaomi.com"))
        val e2 = Entrada("2", titulo = "xiaomi.com", urls = listOf("https://xiaomi.com"))

        val items = construirItemsAgrupadosPorSitio(
            listOf(e1, e2),
            agrupar = false
        ) { false }

        assertEquals(2, items.size)
        assertTrue(items.all { it is ItemAgrupado.Suelto })
        assertEquals("account.xiaomi.com", (items[0] as ItemAgrupado.Suelto).entrada.titulo)
        assertEquals("xiaomi.com", (items[1] as ItemAgrupado.Suelto).entrada.titulo)
    }

    @Test
    fun `las passkeys se agrupan por su rpId`() {
        val p1 = DatosPasskey("paypal.com", "PayPal", "user1", "id1", "key1")
        val p2 = DatosPasskey("paypal.com", "PayPal", "user2", "id2", "key2")
        val e1 = Entrada("1", tipo = TipoEntrada.PASSKEY, titulo = "PayPal 1", passkey = p1)
        val e2 = Entrada("2", tipo = TipoEntrada.PASSKEY, titulo = "PayPal 2", passkey = p2)

        assertEquals("paypal", claveAgrupacionSitio(e1))
        assertEquals("paypal", claveAgrupacionSitio(e2))

        val items = construirItemsAgrupadosPorSitio(listOf(e1, e2)) { false }
        assertEquals(1, items.size)
        assertTrue(items.first() is ItemAgrupado.Grupo)
    }

    @Test
    fun `las notas seguras nunca se agrupan`() {
        val n1 = Entrada("1", tipo = TipoEntrada.NOTA, titulo = "Nota Secreta 1", contrasena = "")
        val n2 = Entrada("2", tipo = TipoEntrada.NOTA, titulo = "Nota Secreta 2", contrasena = "")

        assertNull(claveAgrupacionSitio(n1))
        assertNull(claveAgrupacionSitio(n2))

        val items = construirItemsAgrupadosPorSitio(listOf(n1, n2)) { false }
        assertEquals(2, items.size)
        assertTrue(items.all { it is ItemAgrupado.Suelto })
    }
}