/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Define los elementos de menú del Hub de Ajustes para Seguridad y Cifrado.
 */
fun crearElementosSeguridadAjustes(
    vm: VaultViewModel,
    alAbrirCambioMaestra: () -> Unit
): List<ElementoMenuAjustes> = listOf(
    ElementoMenuAjustes(
        titulo = "Biometría y seguridad",
        subtitulo = "Políticas de acceso, autenticación biométrica y cifrado de datos",
        icono = Icons.Filled.Fingerprint,
        colorIcono = Color(0xFF1E88E5),
        idEtiqueta = "01-SEG-BIO",
        grupo = "Seguridad",
        palabrasClave = "huella biometria pin contrasena bloqueo inactividad flag secure pantalla portapapeles autodestruccion argon2id cifrado senuelo",
        alPulsar = { vm.ir(Pantalla.Seguridad()) }
    ),
    ElementoMenuAjustes(
        titulo = "Auditoría de accesibilidad",
        subtitulo = "Detección de aplicaciones espía y servicios con acceso a la pantalla",
        icono = Icons.Filled.Security,
        colorIcono = Color(0xFFE65100),
        idEtiqueta = "01-SEG-ACC",
        grupo = "Seguridad",
        palabrasClave = "accesibilidad permiso pantalla espia scraping cube acr lista blanca confianza servicios",
        alPulsar = { vm.ir(Pantalla.AuditoriaAccesibilidad()) }
    ),
    ElementoMenuAjustes(
        titulo = "Contraseñas comunes",
        subtitulo = "Lista negra de contraseñas inseguras y auditoría de salud",
        icono = Icons.Filled.Security,
        colorIcono = Color(0xFFE53935),
        idEtiqueta = "01-SEG-COM",
        grupo = "Seguridad",
        palabrasClave = "contrasenas comunes prohibidas salud lista negra debiles exclusiones",
        alPulsar = { vm.ir(Pantalla.ContrasenasComunes()) }
    )
)

