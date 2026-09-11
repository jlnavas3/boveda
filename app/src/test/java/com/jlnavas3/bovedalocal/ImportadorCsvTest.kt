package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.ImportadorCsv
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportadorCsvTest {

    @Test
    fun `parsea CSV de Google Password Manager y Chrome`() {
        val csv = """
            name,url,username,password,note
            Google,https://accounts.google.com,pepo@gmail.com,ClaveSecreta123!,Nota de Google
            GitHub,https://github.com/login,pepotech,SuperP@ssw0rd,
        """.trimIndent()

        val entradas = ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
        assertEquals(2, entradas.size)

        val e1 = entradas[0]
        assertEquals("Google", e1.titulo)
        assertEquals("https://accounts.google.com", e1.urls.first())
        assertEquals("pepo@gmail.com", e1.usuario)
        assertEquals("ClaveSecreta123!", e1.contrasena)
        assertEquals("Nota de Google", e1.notas)

        val e2 = entradas[1]
        assertEquals("GitHub", e2.titulo)
        assertEquals("https://github.com/login", e2.urls.first())
        assertEquals("pepotech", e2.usuario)
        assertEquals("SuperP@ssw0rd", e2.contrasena)
        assertEquals("", e2.notas)
    }

    @Test
    fun `parsea CSV de Bitwarden`() {
        val csv = """
            folder,favorite,type,name,notes,fields,reprompt,login_uri,login_username,login_password,login_totp
            Personal,0,login,Amazon,Nota sobre compras,,0,https://www.amazon.es,comprador@amazon.com,MiClaveSegura2026,
            Trabajo,1,login,Slack,,,0,https://slack.com,trabajador@empresa.com,TokenSlack999,
        """.trimIndent()

        val entradas = ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
        assertEquals(2, entradas.size)

        assertEquals("Amazon", entradas[0].titulo)
        assertEquals("comprador@amazon.com", entradas[0].usuario)
        assertEquals("MiClaveSegura2026", entradas[0].contrasena)
        assertEquals("https://www.amazon.es", entradas[0].urls.first())
        assertEquals("Nota sobre compras", entradas[0].notas)

        assertEquals("Slack", entradas[1].titulo)
        assertEquals("trabajador@empresa.com", entradas[1].usuario)
        assertEquals("TokenSlack999", entradas[1].contrasena)
    }

    @Test
    fun `parsea CSV de LastPass`() {
        val csv = """
            url,username,password,extra,name,grouping,fav
            https://twitter.com,tuitero,PassTwitter!1,Mi nota de Twitter,X Corp,Social,0
            https://netflix.com,series@tv.com,Peliculas4K,Nota TV,Netflix,Streaming,1
        """.trimIndent()

        val entradas = ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
        assertEquals(2, entradas.size)

        assertEquals("X Corp", entradas[0].titulo)
        assertEquals("tuitero", entradas[0].usuario)
        assertEquals("PassTwitter!1", entradas[0].contrasena)
        assertEquals("Mi nota de Twitter", entradas[0].notas)

        assertEquals("Netflix", entradas[1].titulo)
        assertEquals("series@tv.com", entradas[1].usuario)
        assertEquals("Peliculas4K", entradas[1].contrasena)
    }

    @Test
    fun `soporta alias en espanol`() {
        val csv = """
            nombre,sitio,usuario,clave,notas
            Banco Nacional,https://banco.es,cliente123,PinBancario88,PIN del cajero
        """.trimIndent()

        val entradas = ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
        assertEquals(1, entradas.size)
        assertEquals("Banco Nacional", entradas[0].titulo)
        assertEquals("https://banco.es", entradas[0].urls.first())
        assertEquals("cliente123", entradas[0].usuario)
        assertEquals("PinBancario88", entradas[0].contrasena)
        assertEquals("PIN del cajero", entradas[0].notas)
    }

    @Test
    fun `tolera el marcador BOM UTF-8 al inicio`() {
        val csv = "\uFEFFname,url,username,password\nSpotify,https://spotify.com,musico,Rock2026!"
        val entradas = ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
        assertEquals(1, entradas.size)
        assertEquals("Spotify", entradas[0].titulo)
        assertEquals("musico", entradas[0].usuario)
    }

    @Test
    fun `maneja comas dentro de comillas y comillas escapadas`() {
        val csv = """
            name,url,username,password,notes
            "Empresa, S.A.",https://empresa.com,admin,"clave,con,comas","Nota con ""comillas"" dobles"
        """.trimIndent()

        val entradas = ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
        assertEquals(1, entradas.size)
        assertEquals("Empresa, S.A.", entradas[0].titulo)
        assertEquals("clave,con,comas", entradas[0].contrasena)
        assertEquals("Nota con \"comillas\" dobles", entradas[0].notas)
    }

    @Test
    fun `maneja saltos de linea dentro de campos entrecomillados`() {
        val csv = """
            name,url,username,password,notes
            Servidor,https://ssh.server.com,root,secret,"Linea 1
            Linea 2
            Linea 3"
        """.trimIndent()

        val entradas = ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
        assertEquals(1, entradas.size)
        assertTrue(entradas[0].notas.contains("Linea 1"))
        assertTrue(entradas[0].notas.contains("Linea 2"))
        assertTrue(entradas[0].notas.contains("Linea 3"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lanza excepcion si el CSV no tiene filas de datos`() {
        val csv = "name,url,username,password\n"
        ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lanza excepcion si las columnas no son reconocibles`() {
        val csv = """
            columnaA,columnaB,columnaC
            val1,val2,val3
        """.trimIndent()
        ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
    }

    @Test
    fun `ignora filas donde usuario contrasena y url estan en blanco`() {
        val csv = """
            name,url,username,password,notes
            Vacia,,,,Solo notas sin login
            Valida,https://ejemplo.com,user,pass,
        """.trimIndent()

        val entradas = ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
        assertEquals(1, entradas.size)
        assertEquals("Valida", entradas[0].titulo)
    }

    @Test
    fun `si el titulo esta en blanco usa url o usuario como fallback`() {
        val csv = """
            name,url,username,password
            ,https://sin-titulo.com,usuario1,pass1
            ,,usuario2,pass2
        """.trimIndent()

        val entradas = ImportadorCsv.parsear(csv.toByteArray(Charsets.UTF_8))
        assertEquals(2, entradas.size)
        assertEquals("https://sin-titulo.com", entradas[0].titulo)
        assertEquals("usuario2", entradas[1].titulo)
    }
}
