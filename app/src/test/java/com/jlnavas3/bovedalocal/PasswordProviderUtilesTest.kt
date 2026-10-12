/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.passkey.objetivoSolicitante
import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordProviderUtilesTest {

    @Test
    fun `objetivoSolicitante extrae el dominio raiz cuando existe origen web`() {
        assertEquals("ejemplo.com", objetivoSolicitante("com.android.chrome", "https://login.ejemplo.com"))
        assertEquals("banco.co.uk", objetivoSolicitante("com.android.chrome", "https://app.banco.co.uk:8443"))
    }

    @Test
    fun `objetivoSolicitante devuelve el paquete si el origen es nulo o vacio`() {
        assertEquals("com.miempresa.app", objetivoSolicitante("com.miempresa.app", null))
        assertEquals("com.miempresa.app", objetivoSolicitante("com.miempresa.app", ""))
        assertEquals("com.miempresa.app", objetivoSolicitante("com.miempresa.app", "   "))
    }
}
