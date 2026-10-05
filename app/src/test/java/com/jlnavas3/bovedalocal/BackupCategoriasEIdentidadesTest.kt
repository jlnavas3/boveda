package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.crypto.KdfParams
import com.jlnavas3.bovedalocal.crypto.VaultCrypto
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupCategoriasEIdentidadesTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `copia de seguridad bvda preserva categorias e identidades completas con sus enlaces`() {
        val catPersonal = Categoria(
            id = "cat-personal",
            nombre = "Personal",
            icono = "casa",
            colorHex = "#3B82F6",
            creadaEn = 1000L,
            modificadaEn = 1000L
        )
        val catTrabajo = Categoria(
            id = "cat-trabajo",
            nombre = "Trabajo",
            icono = "maletin",
            colorHex = "#10B981",
            creadaEn = 2000L,
            modificadaEn = 2000L
        )

        val identidadPrincipal = Identidad(
            id = "iden-1",
            nombre = "Personal Gmail",
            correoPrincipal = "juan@gmail.com",
            correosSecundarios = listOf("alias1@gmail.com", "alias2@gmail.com"),
            colorHex = "#EF4444",
            icono = "person",
            creadaEn = 1000L,
            modificadaEn = 1000L
        )
        val identidadTrabajo = Identidad(
            id = "iden-2",
            nombre = "Empresa",
            correoPrincipal = "juan@corporacion.com",
            correosSecundarios = emptyList(),
            colorHex = "#8B5CF6",
            icono = "work",
            creadaEn = 2000L,
            modificadaEn = 2000L
        )

        val entrada1 = Entrada(
            id = "e1",
            titulo = "Google Drive",
            usuario = "juan@gmail.com",
            contrasena = "pass123",
            categorias = listOf("cat-personal"),
            identidadId = "iden-1"
        )
        val entrada2 = Entrada(
            id = "e2",
            titulo = "Slack Corporativo",
            usuario = "juan@corporacion.com",
            contrasena = "corp456",
            categorias = listOf("cat-trabajo"),
            identidadId = "iden-2"
        )

        val bovedaOriginal = ContenidoBoveda(
            version = 1,
            entradas = listOf(entrada1, entrada2),
            categorias = listOf(catPersonal, catTrabajo),
            identidades = listOf(identidadPrincipal, identidadTrabajo)
        )

        // 1. Simular exportación: Cifrado con contraseña simulando un archivo .bvda
        val kdfTest = object : com.jlnavas3.bovedalocal.crypto.Kdf {
            override fun derivar(password: CharArray, salt: ByteArray, params: KdfParams): ByteArray {
                val digest = java.security.MessageDigest.getInstance("SHA-256")
                digest.update(String(password).toByteArray(Charsets.UTF_8))
                digest.update(salt)
                return digest.digest().copyOf(params.hashLength)
            }
        }
        val password = "clave-super-segura".toCharArray()
        val salt = VaultCrypto.nuevoSalt()
        val clave = VaultCrypto.derivarClave(password, salt, KdfParams.PREDETERMINADOS, kdfTest)
        val payloadPlano = json.encodeToString(bovedaOriginal).toByteArray(Charsets.UTF_8)
        val archivoBvda = VaultCrypto.cifrar(payloadPlano, clave, salt, KdfParams.PREDETERMINADOS)

        // 2. Simular importación en nuevo dispositivo
        val cabecera = VaultCrypto.leerCabecera(archivoBvda)
        val claveDescifrado = VaultCrypto.derivarClave(password, cabecera.salt, cabecera.params, kdfTest)
        val payloadDescifrado = VaultCrypto.descifrar(archivoBvda, claveDescifrado)
        val bovedaImportada = json.decodeFromString<ContenidoBoveda>(String(payloadDescifrado, Charsets.UTF_8))

        // 3. Verificaciones de Categorías
        assertEquals(2, bovedaImportada.categorias.size)
        val catImportada1 = bovedaImportada.categorias.find { it.id == "cat-personal" }
        assertNotNull(catImportada1)
        assertEquals("Personal", catImportada1?.nombre)
        assertEquals("casa", catImportada1?.icono)
        assertEquals("#3B82F6", catImportada1?.colorHex)

        // 4. Verificaciones de Identidades
        assertEquals(2, bovedaImportada.identidades.size)
        val idenImportada1 = bovedaImportada.identidades.find { it.id == "iden-1" }
        assertNotNull(idenImportada1)
        assertEquals("Personal Gmail", idenImportada1?.nombre)
        assertEquals("juan@gmail.com", idenImportada1?.correoPrincipal)
        assertEquals(listOf("alias1@gmail.com", "alias2@gmail.com"), idenImportada1?.correosSecundarios)
        assertEquals("#EF4444", idenImportada1?.colorHex)
        assertEquals("person", idenImportada1?.icono)

        // 5. Verificaciones de Entradas y sus enlaces intactos
        assertEquals(2, bovedaImportada.entradas.size)
        val eImportada1 = bovedaImportada.entradas.find { it.id == "e1" }
        assertNotNull(eImportada1)
        assertEquals("iden-1", eImportada1?.identidadId)
        assertTrue(eImportada1?.categorias?.contains("cat-personal") == true)

        val eImportada2 = bovedaImportada.entradas.find { it.id == "e2" }
        assertNotNull(eImportada2)
        assertEquals("iden-2", eImportada2?.identidadId)
        assertTrue(eImportada2?.categorias?.contains("cat-trabajo") == true)
    }
}
