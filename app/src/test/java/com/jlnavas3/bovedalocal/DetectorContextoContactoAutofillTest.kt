/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.autofill.DetectorContextoContactoAutofill
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DetectorContextoContactoAutofillTest {

    @Test
    fun `esAppComunicacion reconoce paquetes populares de mensajeria y contactos`() {
        assertTrue(DetectorContextoContactoAutofill.esAppComunicacion("com.whatsapp"))
        assertTrue(DetectorContextoContactoAutofill.esAppComunicacion("com.whatsapp.w4b"))
        assertTrue(DetectorContextoContactoAutofill.esAppComunicacion("org.telegram.messenger"))
        assertTrue(DetectorContextoContactoAutofill.esAppComunicacion("com.google.android.contacts"))
        assertTrue(DetectorContextoContactoAutofill.esAppComunicacion("com.samsung.android.app.contacts"))
        assertTrue(DetectorContextoContactoAutofill.esAppComunicacion("com.hihonor.contacts"))

        // Apps que NO son de mensajería o agenda
        assertFalse(DetectorContextoContactoAutofill.esAppComunicacion("com.android.chrome"))
        assertFalse(DetectorContextoContactoAutofill.esAppComunicacion("org.mozilla.firefox"))
        assertFalse(DetectorContextoContactoAutofill.esAppComunicacion("com.amazon.mShop.android.shopping"))
        assertFalse(DetectorContextoContactoAutofill.esAppComunicacion("com.spotify.music"))
    }

    @Test
    fun `esCandidatoContacto es positivo unicamente para apps de comunicacion sin contrasena ni dominio web`() {
        // Caso ideal: WhatsApp creando contacto
        assertTrue(
            DetectorContextoContactoAutofill.esCandidatoContacto(
                paquete = "com.whatsapp",
                tieneContrasena = false,
                dominioWeb = null,
                tieneCampoRelevante = true
            )
        )

        // Caso Telegram creando contacto
        assertTrue(
            DetectorContextoContactoAutofill.esCandidatoContacto(
                paquete = "org.telegram.messenger",
                tieneContrasena = false,
                dominioWeb = "",
                tieneCampoRelevante = true
            )
        )

        // Caso: WhatsApp pero hay campo de contraseña (ej. pantalla de bloqueo/2FA)
        assertFalse(
            DetectorContextoContactoAutofill.esCandidatoContacto(
                paquete = "com.whatsapp",
                tieneContrasena = true,
                dominioWeb = null,
                tieneCampoRelevante = true
            )
        )

        // Caso: WebView o dominio web presente (ej. whatsapp.com en navegador)
        assertFalse(
            DetectorContextoContactoAutofill.esCandidatoContacto(
                paquete = "com.whatsapp",
                tieneContrasena = false,
                dominioWeb = "whatsapp.com",
                tieneCampoRelevante = true
            )
        )

        // Caso: En navegador web (Chrome) rellenando formulario de teléfono
        assertFalse(
            DetectorContextoContactoAutofill.esCandidatoContacto(
                paquete = "com.android.chrome",
                tieneContrasena = false,
                dominioWeb = "tienda.com",
                tieneCampoRelevante = true
            )
        )

        // Caso: App de mensajería pero sin ningún campo relevante de contacto
        assertFalse(
            DetectorContextoContactoAutofill.esCandidatoContacto(
                paquete = "com.whatsapp",
                tieneContrasena = false,
                dominioWeb = null,
                tieneCampoRelevante = false
            )
        )
    }

    @Test
    fun `esAppComunicacion y esCandidatoContacto respetan paquetes y pistas personalizadas`() {
        val paquetesPersonalizados = setOf("com.session.privacy", "im.vector.app")
        val pistasTelefonoPersonalizadas = listOf("mi_telefono_custom")
        val pistasNombrePersonalizadas = listOf("alias_custom")

        assertTrue(DetectorContextoContactoAutofill.esAppComunicacion("com.session.privacy", paquetesPersonalizados))
        assertFalse(DetectorContextoContactoAutofill.esAppComunicacion("com.whatsapp", paquetesPersonalizados))

        assertTrue(
            DetectorContextoContactoAutofill.esCandidatoContacto(
                paquete = "im.vector.app",
                tieneContrasena = false,
                dominioWeb = null,
                tieneCampoRelevante = true,
                paquetesConfigurados = paquetesPersonalizados
            )
        )
        assertFalse(
            DetectorContextoContactoAutofill.esCandidatoContacto(
                paquete = "com.whatsapp",
                tieneContrasena = false,
                dominioWeb = null,
                tieneCampoRelevante = true,
                paquetesConfigurados = paquetesPersonalizados
            )
        )
    }
}

