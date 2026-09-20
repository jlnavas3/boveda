package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.GestorBackupAutomatico
import com.jlnavas3.bovedalocal.data.RegistroClaveGenerada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class HistorialGeneradorYBackupTest {

    @Test
    fun `purga de historial por autodestruccion elimina claves caducadas`() {
        val ahora = 100_000L
        val tiempoVida = 30_000L // 30s

        val lista = listOf(
            RegistroClaveGenerada(id = "1", clave = "antigua", generadaEn = ahora - 40_000L, origen = "Tile"),
            RegistroClaveGenerada(id = "2", clave = "reciente", generadaEn = ahora - 10_000L, origen = "App")
        )

        // Simula purga por autodestrucción
        val vigentes = lista.filter { (ahora - it.generadaEn) <= tiempoVida }

        assertEquals(1, vigentes.size)
        assertEquals("reciente", vigentes[0].clave)
    }

    @Test
    fun `limite maximo de historial recorta las mas antiguas`() {
        val max = 3
        val claves = (1..5).map {
            RegistroClaveGenerada(id = "$it", clave = "clave$it", generadaEn = it * 1000L, origen = "Tile")
        }

        // Nueva clave insertada al inicio y recortada a max
        val nueva = RegistroClaveGenerada(id = "6", clave = "clave6", generadaEn = 6000L, origen = "Tile")
        val actualizada = (listOf(nueva) + claves).take(max)

        assertEquals(3, actualizada.size)
        assertEquals("clave6", actualizada[0].clave)
        assertEquals("clave1", actualizada[1].clave)
        assertEquals("clave2", actualizada[2].clave)
    }

    @Test
    fun `frecuencia de backup evalua correctamente si debe ejecutarse`() {
        val ahora = System.currentTimeMillis()
        val unDiaMs = 86_400_000L
        val pass = "ContraseñaSegura123!"

        // Frecuencia 0 (desactivado) nunca debe ejecutar
        assertFalse(GestorBackupAutomatico.debeEjecutar(0, pass, 0L, ahora))

        // Contraseña vacía -> nunca debe ejecutar
        assertFalse(GestorBackupAutomatico.debeEjecutar(1, "", 0L, ahora))

        // Frecuencia 1 día, nunca ejecutado antes -> debe ejecutar
        assertTrue(GestorBackupAutomatico.debeEjecutar(1, pass, 0L, ahora))

        // Frecuencia 1 día, ejecutado hace medio día -> NO debe ejecutar
        assertFalse(GestorBackupAutomatico.debeEjecutar(1, pass, ahora - (unDiaMs / 2), ahora))

        // Frecuencia 1 día, ejecutado hace 2 días -> SÍ debe ejecutar
        assertTrue(GestorBackupAutomatico.debeEjecutar(1, pass, ahora - (2 * unDiaMs), ahora))

        // Frecuencia 7 días, ejecutado hace 6 días -> NO debe ejecutar
        assertFalse(GestorBackupAutomatico.debeEjecutar(7, pass, ahora - (6 * unDiaMs), ahora))

        // Frecuencia 7 días, ejecutado hace 8 días -> SÍ debe ejecutar
        assertTrue(GestorBackupAutomatico.debeEjecutar(7, pass, ahora - (8 * unDiaMs), ahora))
    }

    @Test
    fun `rotacion de backups elimina los mas antiguos manteniendo maximo especificado`() {
        val tempDir = File.createTempFile("test_backups", "").apply {
            delete()
            mkdirs()
        }
        try {
            val archivos = (1..7).map { i ->
                File(tempDir, "boveda-auto-2026091${i}-120000.boveda").apply {
                    writeText("contenido-$i")
                    setLastModified(1000L * i)
                }
            }

            assertEquals(7, tempDir.listFiles()?.size)

            // Rotar a max 4 copias
            GestorBackupAutomatico.rotarBackups(tempDir, maxCopias = 4)

            val restantes = tempDir.listFiles()?.sortedBy { it.lastModified() } ?: emptyList()
            assertEquals(4, restantes.size)
            // Deben quedar los 4 más nuevos (4, 5, 6, 7)
            assertEquals("boveda-auto-20260914-120000.boveda", restantes[0].name)
            assertEquals("boveda-auto-20260917-120000.boveda", restantes[3].name)
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun `resolverNombreArchivo aplica patron y asegura extension bvda`() {
        val fechaFija = java.util.Date(1758067200000L) // fecha fija

        val res1 = GestorBackupAutomatico.resolverNombreArchivo("boveda-auto-{FECHA}", secuencia = 1, fecha = fechaFija)
        assertTrue(res1.startsWith("boveda-auto-"))
        assertTrue(res1.endsWith(".bvda"))

        val res2 = GestorBackupAutomatico.resolverNombreArchivo("copia_personalizada", secuencia = 1, fecha = fechaFija)
        assertTrue(res2.startsWith("copia_personalizada_"))
        assertTrue(res2.endsWith(".bvda"))

        val res3 = GestorBackupAutomatico.resolverNombreArchivo("mi_archivo_{FECHA}.bvda", secuencia = 1, fecha = fechaFija)
        assertTrue(res3.startsWith("mi_archivo_"))
        assertTrue(res3.endsWith(".bvda"))
        assertFalse(res3.endsWith(".bvda.bvda"))

        val res4 = GestorBackupAutomatico.resolverNombreArchivo("{99}-boveda-auto-{FECHA}", secuencia = 3, fecha = fechaFija)
        assertTrue(res4.startsWith("03-boveda-auto-"))
        assertTrue(res4.endsWith(".bvda"))

        val res5 = GestorBackupAutomatico.resolverNombreArchivo("{999}-backup", secuencia = 7, fecha = fechaFija)
        assertTrue(res5.startsWith("007-backup_"))
        assertTrue(res5.endsWith(".bvda"))
    }

    @Test
    fun `calcularSiguienteSecuencia incrementa correctamente segun archivos existentes`() {
        val tempDir = File.createTempFile("test_seq", "").apply {
            delete()
            mkdirs()
        }
        try {
            assertEquals(1, GestorBackupAutomatico.calcularSiguienteSecuencia(tempDir, 0))

            File(tempDir, "01-boveda.bvda").writeText("1")
            File(tempDir, "02-boveda.bvda").writeText("2")
            assertEquals(3, GestorBackupAutomatico.calcularSiguienteSecuencia(tempDir, 0))

            // Si secuenciaActual en ajustes es mayor
            assertEquals(6, GestorBackupAutomatico.calcularSiguienteSecuencia(tempDir, 5))
        } finally {
            tempDir.deleteRecursively()
        }
    }
}
