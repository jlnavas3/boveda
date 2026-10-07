package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.ConfiguracionBovedaExportable
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.aConfiguracionExportable
import com.jlnavas3.bovedalocal.data.aplicarConfiguracionExportable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ConfiguracionBovedaExportableTest {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @Test
    fun `aConfiguracionExportable mapea correctamente reglas y configuraciones funcionales`() {
        val plantilla = PlantillaCamposPersonalizada(
            id = "plantilla-vpn",
            titulo = "Configuración VPN",
            descripcion = "Credenciales y llaves VPN",
            campos = listOf(
                CampoPersonalizado(id = "c1", etiqueta = "Servidor IP", valor = "10.0.0.1", tipo = TipoCampo.TEXTO)
            )
        )

        val ajustes = AjustesApp(
            prefijosSubdominios = listOf("api", "auth", "vpn"),
            tldsDescartables = listOf("local", "internal", "lan"),
            marcasPersonalizadas = mapOf("proxmox.home" to "Proxmox VE"),
            puertosServiciosLocales = mapOf("8006" to "Proxmox", "8123" to "Home Assistant"),
            octetosRouter = listOf(1, 254),
            plantillaRouterIp = "Gateway Router ({IP})",
            plantillaServidorIp = "Servidor Homelab ({IP})",
            formatoColisionTitulos = "{TITULO} ({CONTADOR})",
            respetarTitulosPersonalizados = false,
            autofillSugerenciasTeclado = true,
            autofillPistasUsuario = listOf("username", "login", "user"),
            autofillPistasContrasena = listOf("password", "pass"),
            autofillPistasOtp = listOf("totp", "otp"),
            mapeoPaquetesPersonalizados = mapOf("com.example.bank" to "Banco Ejemplo"),
            navegadoresPersonalizados = listOf("com.custom.browser"),
            maxSugerenciasAutofill = 7,
            plantillasCamposPersonalizadas = listOf(plantilla),
            autoBloqueoSegundos = 600,
            portapapelesSegundos = 45,
            frenoIntentosGratis = 3,
            frenoSegundosMax = 300L,
            autodestruccionIntentosFallidosMax = 10,
            // Temas (que deben ser ignorados)
            colorAcento = "#FF5500",
            temaApp = "oscuro"
        )

        val exportable = ajustes.aConfiguracionExportable()

        assertEquals(listOf("api", "auth", "vpn"), exportable.prefijosSubdominios)
        assertEquals(listOf("local", "internal", "lan"), exportable.tldsDescartables)
        assertEquals(mapOf("proxmox.home" to "Proxmox VE"), exportable.marcasPersonalizadas)
        assertEquals(mapOf("8006" to "Proxmox", "8123" to "Home Assistant"), exportable.puertosServiciosLocales)
        assertEquals(listOf(1, 254), exportable.octetosRouter)
        assertEquals("Gateway Router ({IP})", exportable.plantillaRouterIp)
        assertEquals("Servidor Homelab ({IP})", exportable.plantillaServidorIp)
        assertEquals("{TITULO} ({CONTADOR})", exportable.formatoColisionTitulos)
        assertEquals(false, exportable.respetarTitulosPersonalizados)
        assertEquals(listOf(plantilla), exportable.plantillasCamposPersonalizadas)
        assertEquals(600, exportable.autoBloqueoSegundos)
        assertEquals(45, exportable.portapapelesSegundos)
        assertEquals(3, exportable.frenoIntentosGratis)
        assertEquals(300L, exportable.frenoSegundosMax)
        assertEquals(10, exportable.autodestruccionIntentosFallidosMax)
    }

    @Test
    fun `serializacion y deserializacion en ContenidoBoveda preserva configuracion`() {
        val contenidoOriginal = ContenidoBoveda(
            entradas = listOf(Entrada(id = "e1", titulo = "Test Entry")),
            configuracion = ConfiguracionBovedaExportable(
                prefijosSubdominios = listOf("homelab", "vault"),
                puertosServiciosLocales = mapOf("9000" to "Portainer"),
                autoBloqueoSegundos = 1200
            )
        )

        val jsonString = json.encodeToString(contenidoOriginal)
        val deserializado = json.decodeFromString<ContenidoBoveda>(jsonString)

        assertNotNull(deserializado.configuracion)
        assertEquals(listOf("homelab", "vault"), deserializado.configuracion?.prefijosSubdominios)
        assertEquals(mapOf("9000" to "Portainer"), deserializado.configuracion?.puertosServiciosLocales)
        assertEquals(1200, deserializado.configuracion?.autoBloqueoSegundos)
    }

    @Test
    fun `compatibilidad retroactiva con bovedas antiguas sin campo configuracion`() {
        val jsonAntiguo = """
            {
                "version": 1,
                "entradas": [
                    {
                        "id": "e1",
                        "titulo": "Vieja entrada"
                    }
                ],
                "papelera": [],
                "colecciones": [],
                "identidades": []
            }
        """.trimIndent()

        val deserializado = json.decodeFromString<ContenidoBoveda>(jsonAntiguo)
        assertEquals(1, deserializado.entradas.size)
        assertEquals("Vieja entrada", deserializado.entradas[0].titulo)
        assertNull(deserializado.configuracion)
    }

    @Test
    fun `aplicarConfiguracionExportable actualiza ajustes respetando temas locales existentes`() {
        val base = AjustesApp(
            colorAcento = "#123456",
            temaApp = "claro",
            colorTitulos = "#FFFFFF",
            prefijosSubdominios = listOf("antiguo"),
            autoBloqueoSegundos = 60
        )

        val exportable = ConfiguracionBovedaExportable(
            prefijosSubdominios = listOf("nuevo1", "nuevo2"),
            autoBloqueoSegundos = 600,
            puertosServiciosLocales = mapOf("8080" to "Http-Alt")
        )

        val actualizado = base.aplicarConfiguracionExportable(exportable)

        // Verificamos que las configuraciones funcionales se actualizaron
        assertEquals(listOf("nuevo1", "nuevo2"), actualizado.prefijosSubdominios)
        assertEquals(600, actualizado.autoBloqueoSegundos)
        assertEquals(mapOf("8080" to "Http-Alt"), actualizado.puertosServiciosLocales)

        // Verificamos que los temas visuales del dispositivo se preservaron intactos
        assertEquals("#123456", actualizado.colorAcento)
        assertEquals("claro", actualizado.temaApp)
        assertEquals("#FFFFFF", actualizado.colorTitulos)
    }
}
