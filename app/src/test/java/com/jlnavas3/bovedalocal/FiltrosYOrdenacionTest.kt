package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FiltrosYOrdenacionTest {

    private fun filtrarYOrdenar(
        entradas: List<Entrada>,
        busqueda: String = "",
        filtroTipo: TipoEntrada? = null,
        soloFavoritos: Boolean = false,
        filtroEtiqueta: String? = null,
        criterio: CriterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ
    ): List<Entrada> {
        val texto = busqueda.trim().lowercase()
        val filtradas = entradas.filter { entrada ->
            (filtroTipo == null || entrada.tipo == filtroTipo) &&
                (!soloFavoritos || entrada.favorito) &&
                (filtroEtiqueta == null || entrada.etiquetas.contains(filtroEtiqueta)) &&
                (texto.isEmpty() ||
                    entrada.titulo.lowercase().contains(texto) ||
                    entrada.usuario.lowercase().contains(texto) ||
                    entrada.urls.any { it.lowercase().contains(texto) } ||
                    entrada.etiquetas.any { it.lowercase().contains(texto) })
        }
        val comparador: Comparator<Entrada> = when (criterio) {
            CriterioOrdenacion.NOMBRE_AZ -> compareByDescending<Entrada> { it.favorito }.thenBy { it.titulo.lowercase() }
            CriterioOrdenacion.NOMBRE_ZA -> compareByDescending<Entrada> { it.titulo.lowercase() }.thenByDescending { it.favorito }
            CriterioOrdenacion.MODIFICACION_RECIENTE -> compareByDescending<Entrada> { it.modificadaEn }.thenByDescending { it.favorito }
            CriterioOrdenacion.CREACION_RECIENTE -> compareByDescending<Entrada> { it.creadaEn }.thenByDescending { it.favorito }
            CriterioOrdenacion.ANTIGUEDAD -> compareBy<Entrada> { it.creadaEn }.thenByDescending { it.favorito }
        }
        return filtradas.sortedWith(comparador)
    }

    @Test
    fun `filtra por texto en titulo usuario url o etiqueta de forma insensible a mayusculas`() {
        val e1 = Entrada("1", titulo = "GitHub", usuario = "pepotech", etiquetas = listOf("#dev"))
        val e2 = Entrada("2", titulo = "Google Cloud", usuario = "admin@empresa.com", urls = listOf("https://console.cloud.google.com"))
        val e3 = Entrada("3", titulo = "Banco", usuario = "cliente", notas = "nota interna")

        val lista = listOf(e1, e2, e3)

        assertEquals(listOf(e1), filtrarYOrdenar(lista, busqueda = "github"))
        assertEquals(listOf(e1), filtrarYOrdenar(lista, busqueda = "PEPO"))
        assertEquals(listOf(e1), filtrarYOrdenar(lista, busqueda = "#dev"))
        assertEquals(listOf(e2), filtrarYOrdenar(lista, busqueda = "cloud.google"))
        assertEquals(listOf(e3), filtrarYOrdenar(lista, busqueda = "banco"))
    }

    @Test
    fun `filtra por tipo de entrada LOGIN PASSKEY NOTA`() {
        val eLogin = Entrada("1", tipo = TipoEntrada.LOGIN, titulo = "Login", contrasena = "123")
        val ePasskey = Entrada("2", tipo = TipoEntrada.PASSKEY, titulo = "Passkey", passkey = DatosPasskey("paypal.com", "PayPal", "user", "id", "key"))
        val eNota = Entrada("3", tipo = TipoEntrada.NOTA, titulo = "Nota", contrasena = "", passkey = null)

        val lista = listOf(eLogin, ePasskey, eNota)

        assertEquals(listOf(eLogin), filtrarYOrdenar(lista, filtroTipo = TipoEntrada.LOGIN))
        assertEquals(listOf(ePasskey), filtrarYOrdenar(lista, filtroTipo = TipoEntrada.PASSKEY))
        assertEquals(listOf(eNota), filtrarYOrdenar(lista, filtroTipo = TipoEntrada.NOTA))
    }

    @Test
    fun `filtra solo favoritos`() {
        val eFav = Entrada("1", titulo = "Favorito", favorito = true)
        val eNormal = Entrada("2", titulo = "Normal", favorito = false)

        val resultado = filtrarYOrdenar(listOf(eFav, eNormal), soloFavoritos = true)
        assertEquals(listOf(eFav), resultado)
    }

    @Test
    fun `los favoritos siempre van primero antes del orden secundario`() {
        val e1 = Entrada("1", titulo = "Zeta", favorito = true)
        val e2 = Entrada("2", titulo = "Alfa", favorito = false)
        val e3 = Entrada("3", titulo = "Beta", favorito = false)

        val resultado = filtrarYOrdenar(listOf(e2, e1, e3), criterio = CriterioOrdenacion.NOMBRE_AZ)
        // Zeta es favorito, así que va primero a pesar de que en orden alfabético Alfa iría antes
        assertEquals(listOf(e1, e2, e3), resultado)
    }

    @Test
    fun `ordena correctamente por los 5 criterios`() {
        val eA = Entrada("1", titulo = "Alfa", creadaEn = 1000L, modificadaEn = 5000L)
        val eB = Entrada("2", titulo = "Beta", creadaEn = 3000L, modificadaEn = 2000L)
        val eC = Entrada("3", titulo = "Gamma", creadaEn = 2000L, modificadaEn = 8000L)

        val lista = listOf(eB, eC, eA)

        // NOMBRE_AZ
        assertEquals(listOf(eA, eB, eC), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.NOMBRE_AZ))

        // NOMBRE_ZA
        assertEquals(listOf(eC, eB, eA), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.NOMBRE_ZA))

        // MODIFICACION_RECIENTE (mayor modificadaEn primero: Gamma=8000, Alfa=5000, Beta=2000)
        assertEquals(listOf(eC, eA, eB), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.MODIFICACION_RECIENTE))

        // CREACION_RECIENTE (mayor creadaEn primero: Beta=3000, Gamma=2000, Alfa=1000)
        assertEquals(listOf(eB, eC, eA), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.CREACION_RECIENTE))

        // ANTIGUEDAD (menor creadaEn primero: Alfa=1000, Gamma=2000, Beta=3000)
        assertEquals(listOf(eA, eC, eB), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.ANTIGUEDAD))
    }

    @Test
    fun `en criterios distintos de NOMBRE_AZ el criterio principal manda sobre favorito`() {
        // Alfa es favorito antiguo, Zeta es no-favorito nuevo
        val eFav = Entrada("1", titulo = "Alfa", creadaEn = 1000L, modificadaEn = 1000L, favorito = true)
        val eNuevo = Entrada("2", titulo = "Zeta", creadaEn = 9000L, modificadaEn = 9000L, favorito = false)

        val lista = listOf(eFav, eNuevo)

        // En NOMBRE_AZ, eFav va primero por ser favorito
        assertEquals(listOf(eFav, eNuevo), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.NOMBRE_AZ))

        // En NOMBRE_ZA, Zeta va primero alfabéticamente
        assertEquals(listOf(eNuevo, eFav), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.NOMBRE_ZA))

        // En MODIFICACION_RECIENTE, Zeta va primero por ser más reciente
        assertEquals(listOf(eNuevo, eFav), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.MODIFICACION_RECIENTE))

        // En CREACION_RECIENTE, Zeta va primero por ser más nuevo
        assertEquals(listOf(eNuevo, eFav), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.CREACION_RECIENTE))

        // En ANTIGUEDAD, Alfa va primero por tener menor timestamp
        assertEquals(listOf(eFav, eNuevo), filtrarYOrdenar(lista, criterio = CriterioOrdenacion.ANTIGUEDAD))
    }
}
