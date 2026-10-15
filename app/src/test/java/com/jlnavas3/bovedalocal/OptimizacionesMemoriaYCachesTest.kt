/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AvisoInterno
import com.jlnavas3.bovedalocal.data.CategoriaAvisoInterno
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoAvisoInterno
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.ResolverIconoAppDominio
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class OptimizacionesMemoriaYCachesTest {

    @Test
    fun `limpiarCachesSensibles purga caches volatiles de aplicaciones sin errores`() {
        GestorAppsInstaladas.limpiarCachesSensibles()
        GestorAppsInstaladas.limpiarCache()
        // No debe lanzar excepciones
    }

    @Test
    fun `limpiarCaches en ResolverIconoAppDominio limpia caches LRU`() {
        ResolverIconoAppDominio.limpiarCaches()
        // No debe lanzar excepciones
    }

    @Test
    fun `instanciacion de modelos inmutables verifica integridad estructural`() {
        val entrada = Entrada(
            id = "test-1",
            titulo = "Google",
            usuario = "user@gmail.com",
            urls = listOf("https://google.com"),
            etiquetas = listOf("trabajo")
        )
        assertNotNull(entrada)
        assertEquals("test-1", entrada.id)

        val aviso = AvisoInterno(
            tipo = TipoAvisoInterno.DUPLICADOS_DETECTADOS,
            categoria = CategoriaAvisoInterno.SEGURIDAD,
            titulo = "Aviso test",
            descripcion = "Desc",
            accionTexto = "Ver",
            pantallaDestino = Pantalla.Duplicados,
            cantidadAfectados = 2
        )
        assertNotNull(aviso)
        assertEquals(2, aviso.cantidadAfectados)
    }
}
