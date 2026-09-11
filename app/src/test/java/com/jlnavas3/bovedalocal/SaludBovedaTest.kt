package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.MedidorFuerza
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class SaludBovedaTest {

    private fun calcularEstadisticas(entradas: List<Entrada>, ahora: Long = System.currentTimeMillis()): DiagnosticoSalud {
        val claves = entradas.filter { it.tipo == TipoEntrada.LOGIN && it.contrasena.isNotBlank() }
        val duplicadas = claves.groupBy { it.contrasena }.values.filter { it.size > 1 }
        val debiles = claves.filter { MedidorFuerza.medir(it.contrasena).puntuacion <= 1 }
        val antiguas = claves.filter {
            it.modificadaEn > 0 && TimeUnit.MILLISECONDS.toDays((ahora - it.modificadaEn).coerceAtLeast(0)) >= 90
        }
        return DiagnosticoSalud(
            totalClaves = claves.size,
            duplicadasGrupos = duplicadas.size,
            duplicadasTotalEntradas = duplicadas.sumOf { it.size },
            debilesTotal = debiles.size,
            antiguasTotal = antiguas.size
        )
    }

    private data class DiagnosticoSalud(
        val totalClaves: Int,
        val duplicadasGrupos: Int,
        val duplicadasTotalEntradas: Int,
        val debilesTotal: Int,
        val antiguasTotal: Int
    )

    @Test
    fun `detecta contrasenas duplicadas correctamente`() {
        val e1 = Entrada("1", titulo = "Sitio 1", contrasena = "claveCompartida123!")
        val e2 = Entrada("2", titulo = "Sitio 2", contrasena = "claveCompartida123!")
        val e3 = Entrada("3", titulo = "Sitio 3", contrasena = "claveUnica456*")

        val diag = calcularEstadisticas(listOf(e1, e2, e3))
        assertEquals(3, diag.totalClaves)
        assertEquals(1, diag.duplicadasGrupos)
        assertEquals(2, diag.duplicadasTotalEntradas)
    }

    @Test
    fun `detecta contrasenas debiles y las contabiliza`() {
        val debil1 = Entrada("1", titulo = "Debil 1", contrasena = "123456")
        val debil2 = Entrada("2", titulo = "Debil 2", contrasena = "password")
        val fuerte = Entrada("3", titulo = "Fuerte", contrasena = "X8#qL!9zP$2@mK5")

        val diag = calcularEstadisticas(listOf(debil1, debil2, fuerte))
        assertEquals(2, diag.debilesTotal)
    }

    @Test
    fun `detecta contrasenas con mas de 90 dias de antiguedad`() {
        val ahora = 1700000000000L
        val noventaYTresDiasAtras = ahora - TimeUnit.DAYS.toMillis(93)
        val diezDiasAtras = ahora - TimeUnit.DAYS.toMillis(10)

        val eAntigua = Entrada("1", titulo = "Antigua", contrasena = "passAntigua123!", modificadaEn = noventaYTresDiasAtras)
        val eReciente = Entrada("2", titulo = "Reciente", contrasena = "passReciente456!", modificadaEn = diezDiasAtras)

        val diag = calcularEstadisticas(listOf(eAntigua, eReciente), ahora = ahora)
        assertEquals(1, diag.antiguasTotal)
    }

    @Test
    fun `las notas y passkeys sin contrasena se excluyen de las estadisticas`() {
        val nota = Entrada("1", titulo = "Mi Nota", contrasena = "")
        val diag = calcularEstadisticas(listOf(nota))
        assertEquals(0, diag.totalClaves)
        assertEquals(0, diag.debilesTotal)
        assertEquals(0, diag.duplicadasTotalEntradas)
    }
}
