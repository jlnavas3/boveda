package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.CampoPlantilla
import com.jlnavas3.bovedalocal.data.GestorPlantillasCampos
import com.jlnavas3.bovedalocal.data.PlantillaCampos
import com.jlnavas3.bovedalocal.data.TipoCampo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlantillasCamposTest {

    @Test
    fun `las plantillas predeterminadas estan bien configuradas`() {
        val predeterminadas = GestorPlantillasCampos.PREDETERMINADAS
        assertTrue("Debe haber al menos 6 plantillas predeterminadas", predeterminadas.size >= 6)

        // Verificar que cada plantilla tiene ID único y al menos un campo
        val ids = predeterminadas.map { it.id }
        assertEquals("Los IDs deben ser únicos", ids.distinct().size, ids.size)

        predeterminadas.forEach { plantilla ->
            assertTrue("El nombre no debe estar en blanco", plantilla.nombre.isNotBlank())
            assertTrue("Debe tener al menos un campo", plantilla.campos.isNotEmpty())
            assertTrue("Debe marcarse como predeterminada", plantilla.esPredeterminada)
        }

        // Verificar plantilla de Tarjeta Bancaria
        val tarjeta = predeterminadas.find { it.id == "pred_tarjeta" }
        assertNotNull(tarjeta)
        assertTrue(tarjeta!!.campos.any { it.etiqueta.contains("CVV") && it.tipo == TipoCampo.OCULTO })
        assertTrue(tarjeta.campos.any { it.etiqueta.contains("PIN") && it.tipo == TipoCampo.PIN })

        // Verificar plantilla de Wi-Fi
        val wifi = predeterminadas.find { it.id == "pred_wifi" }
        assertNotNull(wifi)
        assertTrue(wifi!!.campos.any { it.etiqueta.contains("Contraseña") && it.tipo == TipoCampo.OCULTO })
    }

    @Test
    fun `conversion de plantilla a campos personalizados`() {
        val plantilla = PlantillaCampos(
            id = "test_custom",
            nombre = "Streaming",
            campos = listOf(
                CampoPlantilla("Perfil", TipoCampo.TEXTO),
                CampoPlantilla("PIN Infantil", TipoCampo.PIN)
            )
        )

        val camposPersonalizados = plantilla.aCamposPersonalizados()
        assertEquals(2, camposPersonalizados.size)

        assertEquals("Perfil", camposPersonalizados[0].etiqueta)
        assertEquals(TipoCampo.TEXTO, camposPersonalizados[0].tipo)
        assertEquals("", camposPersonalizados[0].valor)

        assertEquals("PIN Infantil", camposPersonalizados[1].etiqueta)
        assertEquals(TipoCampo.PIN, camposPersonalizados[1].tipo)
        assertEquals("", camposPersonalizados[1].valor)

        // Los IDs generados deben ser únicos
        assertNotEquals(camposPersonalizados[0].id, camposPersonalizados[1].id)
    }

    @Test
    fun `serializacion y deserializacion de plantillas personalizadas`() {
        val original = listOf(
            PlantillaCampos(
                id = "p1",
                nombre = "Servicio Cloud",
                descripcion = "Cuentas y tokens cloud",
                esPredeterminada = false,
                campos = listOf(
                    CampoPlantilla("Access Key", TipoCampo.TEXTO),
                    CampoPlantilla("Secret Key", TipoCampo.OCULTO)
                )
            ),
            PlantillaCampos(
                id = "p2",
                nombre = "Membresía Gimnasio",
                esPredeterminada = false,
                campos = listOf(
                    CampoPlantilla("Socio #", TipoCampo.TEXTO),
                    CampoPlantilla("Locker PIN", TipoCampo.PIN)
                )
            )
        )

        val json = GestorPlantillasCampos.codificarPersonalizadas(original)
        assertTrue(json.isNotBlank())
        assertTrue(json.contains("Servicio Cloud"))
        assertTrue(json.contains("Locker PIN"))

        val decodificado = GestorPlantillasCampos.decodificarPersonalizadas(json)
        assertEquals(2, decodificado.size)
        assertEquals("Servicio Cloud", decodificado[0].nombre)
        assertEquals(2, decodificado[0].campos.size)
        assertEquals(TipoCampo.OCULTO, decodificado[0].campos[1].tipo)

        assertEquals("Membresía Gimnasio", decodificado[1].nombre)
        assertEquals(TipoCampo.PIN, decodificado[1].campos[1].tipo)
    }

    @Test
    fun `decodificacion maneja strings invalidos o vacios sin fallar`() {
        val vacio = GestorPlantillasCampos.decodificarPersonalizadas("")
        assertEquals(emptyList<PlantillaCampos>(), vacio)

        val espacios = GestorPlantillasCampos.decodificarPersonalizadas("   ")
        assertEquals(emptyList<PlantillaCampos>(), espacios)

        val invalido = GestorPlantillasCampos.decodificarPersonalizadas("{ json roto }")
        assertEquals(emptyList<PlantillaCampos>(), invalido)
    }
}
