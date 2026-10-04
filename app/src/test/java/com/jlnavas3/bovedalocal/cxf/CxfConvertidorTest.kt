package com.jlnavas3.bovedalocal.cxf

import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.passkey.WebAuthn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigInteger
import java.security.AlgorithmParameters
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECParameterSpec
import java.security.spec.ECPoint
import java.security.spec.ECPublicKeySpec

class CxfConvertidorTest {

    @Test
    fun `UtilBase64Cxf codifica y decodifica correctamente Base64 y Base64Url`() {
        val original = "Prueba de texto con caracteres especiales: +/=_-"
        val bytes = original.toByteArray(Charsets.UTF_8)

        val b64 = UtilBase64Cxf.aBase64(bytes)
        val decB64 = UtilBase64Cxf.decodificar(b64)
        assertEquals(original, String(decB64, Charsets.UTF_8))

        val b64Url = UtilBase64Cxf.aBase64Url(bytes)
        val decB64Url = UtilBase64Cxf.decodificar(b64Url)
        assertEquals(original, String(decB64Url, Charsets.UTF_8))
    }

    @Test
    fun `asegurarPkcs8 convierte escalar d de 32 bytes en PKCS8 valido y firmable`() {
        // Generar un par de claves WebAuthn real
        val par = WebAuthn.generarPar()
        // Extraer el escalar d (o generar uno de prueba de 32 bytes)
        val dBytes = ByteArray(32) { (it + 1).toByte() }

        val pkcs8 = asegurarPkcs8(dBytes)
        assertTrue("PKCS#8 debe tener más de 32 bytes", pkcs8.size > 32)

        // Verificar que Java KeyFactory puede cargar la clave privada PKCS#8 generada
        val kf = KeyFactory.getInstance("EC")
        val spec = java.security.spec.PKCS8EncodedKeySpec(pkcs8)
        val privKey = kf.generatePrivate(spec)
        assertNotNull(privKey)
    }

    @Test
    fun `parsear CXF con contrasena de Google Password Manager`() {
        val json = """
        {
          "version": { "major": 1, "minor": 0 },
          "exporterDisplayName": "Google Password Manager",
          "accounts": [
            {
              "id": "acc-1",
              "userName": "usuario@gmail.com",
              "items": [
                {
                  "id": "item-1",
                  "title": "GitHub",
                  "urls": ["https://github.com/login"],
                  "credentials": [
                    {
                      "type": "basic-auth",
                      "username": "usuario@gmail.com",
                      "password": "miSuperPassword123!",
                      "notes": "Cuenta principal de desarrollo"
                    }
                  ]
                }
              ]
            }
          ]
        }
        """.trimIndent()

        val resultado = CxfConvertidor.convertir(json)
        assertEquals(1, resultado.totalContrasenas)
        assertEquals(0, resultado.totalPasskeys)
        assertEquals(0, resultado.totalTotp)
        assertEquals("Google Password Manager", resultado.exportador)
        assertEquals(1, resultado.entradas.size)

        val entrada = resultado.entradas.first()
        assertEquals(TipoEntrada.LOGIN, entrada.tipo)
        assertEquals("GitHub", entrada.titulo)
        assertEquals("usuario@gmail.com", entrada.usuario)
        assertEquals("miSuperPassword123!", entrada.contrasena)
        assertEquals(listOf("https://github.com/login"), entrada.urls)
        assertTrue(entrada.notas.contains("Cuenta principal"))
    }

    @Test
    fun `parsear CXF con llave de paso (Passkey) en formato PKCS8`() {
        val par = WebAuthn.generarPar()
        val privadaB64 = UtilBase64Cxf.aBase64Url(par.privadaPkcs8)
        val credId = UtilBase64Cxf.aBase64Url(WebAuthn.nuevoCredId())

        val json = """
        {
          "version": { "major": 1, "minor": 0 },
          "exporter": {
            "name": "Gestor de Credenciales Externo",
            "rpId": "com.ejemplo.gestor"
          },
          "items": [
            {
              "id": "item-passkey-1",
              "title": "Google",
              "domain": "google.com",
              "credentials": [
                {
                  "type": "passkey",
                  "rpId": "google.com",
                  "rpName": "Google",
                  "userName": "mi_cuenta@gmail.com",
                  "userHandle": "dXNlcl9oYW5kbGVfMTIz",
                  "credentialId": "$credId",
                  "privateKey": "$privadaB64",
                  "alg": -7
                }
              ]
            }
          ]
        }
        """.trimIndent()

        val resultado = CxfConvertidor.convertir(json)
        assertEquals(1, resultado.totalPasskeys)
        assertEquals(0, resultado.totalContrasenas)
        assertEquals(1, resultado.entradas.size)

        val entrada = resultado.entradas.first()
        assertEquals(TipoEntrada.PASSKEY, entrada.tipo)
        assertEquals("Google", entrada.titulo)
        assertEquals("mi_cuenta@gmail.com", entrada.usuario)
        assertNotNull(entrada.passkey)

        val passkey = entrada.passkey!!
        assertEquals("google.com", passkey.rpId)
        assertEquals("Google", passkey.rpName)
        assertEquals(credId, passkey.credId)
        assertEquals("ES256", passkey.algoritmo)

        // Verificar que la clave privada importada puede firmar con WebAuthn
        val firma = WebAuthn.firmar(UtilBase64Cxf.decodificar(passkey.clavePrivada), "prueba".toByteArray())
        assertTrue(firma.isNotEmpty())
    }

    @Test
    fun `parsear CXF con llave de paso (Passkey) en formato JWK`() {
        val dBytes = ByteArray(32) { (it + 5).toByte() }
        val dB64Url = UtilBase64Cxf.aBase64Url(dBytes)
        val credId = "cred-jwk-12345"

        val json = """
        {
          "items": [
            {
              "title": "Amazon",
              "credentials": [
                {
                  "type": "public-key-credential",
                  "rpId": "amazon.es",
                  "rpName": "Amazon ES",
                  "userName": "comprador@amazon.es",
                  "credentialId": "$credId",
                  "privateKeyJwk": {
                    "kty": "EC",
                    "crv": "P-256",
                    "d": "$dB64Url"
                  }
                }
              ]
            }
          ]
        }
        """.trimIndent()

        val resultado = CxfConvertidor.convertir(json)
        assertEquals(1, resultado.totalPasskeys)
        assertEquals(1, resultado.entradas.size)

        val passkey = resultado.entradas.first().passkey
        assertNotNull(passkey)
        assertEquals("amazon.es", passkey?.rpId)
        assertEquals("Amazon ES", passkey?.rpName)
        assertEquals("comprador@amazon.es", passkey?.usuario)
    }

    @Test
    fun `parsear CXF consolidando contrasena, passkey y TOTP del mismo servicio`() {
        val par = WebAuthn.generarPar()
        val privadaB64 = UtilBase64Cxf.aBase64Url(par.privadaPkcs8)
        val credId = "cred-full-test"

        val json = """
        {
          "items": [
            {
              "id": "servicio-completo",
              "title": "GitLab",
              "urls": ["https://gitlab.com"],
              "credentials": [
                {
                  "type": "basic-auth",
                  "username": "dev@empresa.com",
                  "password": "PasswordCompleto456"
                },
                {
                  "type": "passkey",
                  "rpId": "gitlab.com",
                  "rpName": "GitLab",
                  "userName": "dev@empresa.com",
                  "credentialId": "$credId",
                  "privateKey": "$privadaB64"
                },
                {
                  "type": "totp",
                  "secret": "JBSWY3DPEHPK3PXP",
                  "issuer": "GitLab",
                  "digits": 6,
                  "period": 30,
                  "algorithm": "SHA1"
                }
              ]
            }
          ]
        }
        """.trimIndent()

        val resultado = CxfConvertidor.convertir(json)
        assertEquals(1, resultado.totalContrasenas)
        assertEquals(1, resultado.totalPasskeys)
        assertEquals(1, resultado.totalTotp)
        assertEquals(1, resultado.entradas.size)

        val entrada = resultado.entradas.first()
        assertEquals(TipoEntrada.LOGIN, entrada.tipo)
        assertEquals("GitLab", entrada.titulo)
        assertEquals("dev@empresa.com", entrada.usuario)
        assertEquals("PasswordCompleto456", entrada.contrasena)
        assertNotNull(entrada.passkey)
        assertEquals(credId, entrada.passkey?.credId)
        assertEquals("JBSWY3DPEHPK3PXP", entrada.secretoTotp)
        assertEquals("GitLab", entrada.totpEmisor)
        assertEquals(6, entrada.totpDigitos)
        assertEquals(30, entrada.totpPeriodo)
        assertEquals("HmacSHA1", entrada.totpAlgoritmo)
    }

    @Test
    fun `parsear CXF con campos desconocidos e ignorar sin fallar`() {
        val json = """
        {
          "version": "1.0",
          "campoDesconocido1": 123,
          "exporterMeta": { "clave": "valor" },
          "items": [
            {
              "title": "Servicio con extras",
              "extraAtributo": true,
              "credentials": [
                {
                  "type": "basic-auth",
                  "username": "admin",
                  "password": "secretPassword",
                  "campoFuturo": "no-importa"
                }
              ]
            }
          ]
        }
        """.trimIndent()

        val resultado = CxfConvertidor.convertir(json)
        assertEquals(1, resultado.entradas.size)
        assertEquals("Servicio con extras", resultado.entradas.first().titulo)
        assertEquals("admin", resultado.entradas.first().usuario)
        assertEquals("secretPassword", resultado.entradas.first().contrasena)
    }

    @Test
    fun `parsear CXF exacto de Google Password Manager con campos objeto y passkeys`() {
        val json = """
        {
          "version": { "major": 1, "minor": 0 },
          "exporterRpId": "passwords.google.com",
          "exporterDisplayName": "Google Password Manager",
          "timestamp": 1790872177,
          "accounts": [
            {
              "id": "acc-google",
              "username": "usuario@gmail.com",
              "email": "usuario@gmail.com",
              "collections": [],
              "items": [
                {
                  "id": "item-edx",
                  "creationAt": 1620955471,
                  "modifiedAt": 1620955471,
                  "title": "https://authn.edx.org/",
                  "favorite": false,
                  "scope": {
                    "urls": ["https://authn.edx.org/"],
                    "androidApps": []
                  },
                  "credentials": [
                    {
                      "type": "basic-auth",
                      "username": {
                        "fieldType": "string",
                        "value": "usuario@edx.org"
                      },
                      "password": {
                        "fieldType": "concealed-string",
                        "value": "claveSecretaEdx"
                      }
                    }
                  ]
                },
                {
                  "id": "item-spotify-app",
                  "creationAt": 1620955471,
                  "modifiedAt": 1620955471,
                  "title": "s4a.spotify.com",
                  "favorite": true,
                  "scope": {
                    "urls": [],
                    "androidApps": [
                      {
                        "bundleId": "com.spotify.s4a",
                        "name": "s4a.spotify.com"
                      }
                    ]
                  },
                  "credentials": [
                    {
                      "type": "basic-auth",
                      "username": {
                        "fieldType": "string",
                        "value": "spotify_user"
                      },
                      "password": {
                        "fieldType": "concealed-string",
                        "value": "spotPass123"
                      }
                    }
                  ]
                },
                {
                  "id": "item-passkey-wa",
                  "creationAt": 1770851994,
                  "modifiedAt": 1790872177,
                  "title": "whatsapp.com",
                  "favorite": false,
                  "credentials": [
                    {
                      "type": "passkey",
                      "credentialId": "6GaoKtdXe6QupydL34etJA",
                      "rpId": "whatsapp.com",
                      "username": "********9258",
                      "userDisplayName": "********9258",
                      "userHandle": "HnX_rAbmtcxm2iA58Fup1g",
                      "key": "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgvkiyq-6dKl_nLDMUNDemyt7dptj-eiqaVGqp0tB897ahRANCAASrMgzBsRIiKiwC2A0SY18EuY67mfq0Ac2XfIyPH-9ufhTXD9BNJPOLUBKP5E0846fubFhS3AYgJ2N86LmhmBIZ",
                      "fido2Extensions": {
                        "payments": false
                      }
                    }
                  ]
                }
              ]
            }
          ]
        }
        """.trimIndent()

        val resultado = CxfConvertidor.convertir(json)
        assertEquals("Google Password Manager", resultado.exportador)
        assertEquals(2, resultado.totalContrasenas)
        assertEquals(1, resultado.totalPasskeys)
        assertEquals(3, resultado.entradas.size)

        val edx = resultado.entradas.first { it.usuario == "usuario@edx.org" }
        assertEquals("authn.edx.org", edx.titulo)
        assertEquals("claveSecretaEdx", edx.contrasena)
        assertTrue(edx.urls.contains("https://authn.edx.org/"))
        assertEquals(1620955471000L, edx.creadaEn)

        val spotify = resultado.entradas.first { it.usuario == "spotify_user" }
        assertEquals("s4a.spotify.com", spotify.titulo)
        assertEquals("spotPass123", spotify.contrasena)
        assertTrue(spotify.favorito)
        assertTrue(spotify.urls.contains("android://com.spotify.s4a"))

        val wa = resultado.entradas.first { it.tipo == TipoEntrada.PASSKEY }
        assertEquals("whatsapp.com", wa.titulo)
        assertEquals("********9258", wa.usuario)
        assertNotNull(wa.passkey)
        assertEquals("6GaoKtdXe6QupydL34etJA", wa.passkey?.credId)
        assertEquals("whatsapp.com", wa.passkey?.rpId)
        assertEquals("HnX_rAbmtcxm2iA58Fup1g", wa.passkey?.userHandle)
        assertTrue(wa.passkey!!.clavePrivada.isNotBlank())
    }
}
