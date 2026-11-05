/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.colores

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteColorPicker
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb

@Composable
fun GrupoSelectoresColoresDatos(
    mostrarIdsAjustes: Boolean,
    onSeleccionarColor: (clave: String, colorInicial: Color) -> Unit,
    modifier: Modifier = Modifier,
    alRestablecer: (() -> Unit)? = null
) {
    ComponenteGrupo(
        etiqueta = "Colores de datos e indicadores",
        alRestablecer = alRestablecer,
        idGrupo = "02-APA-THM-DAT-G01",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteColorPicker(
            titulo = "Identidades y Titulares",
            subtitulo = "Usuarios, correos, titulares bancarios, SSH y documentos",
            colorActual = ColorDatosUsuario,
            icono = Icons.Filled.Person,
            colorIcono = ColorDatosUsuario,
            alPulsar = { onSeleccionarColor("usuario", ColorDatosUsuario) }
        )

        ComponenteSeparador()

        ComponenteColorPicker(
            titulo = "Contraseñas y Secretos",
            subtitulo = "Claves web, contraseñas Wi-Fi, CVV, PIN y claves privadas",
            colorActual = ColorDatosContrasena,
            icono = Icons.Filled.Key,
            colorIcono = ColorDatosContrasena,
            alPulsar = { onSeleccionarColor("contrasena", ColorDatosContrasena) }
        )

        ComponenteSeparador()

        ComponenteColorPicker(
            titulo = "Códigos y Vigencia (2FA)",
            subtitulo = "Tokens TOTP, seguridad Wi-Fi y fechas de vencimiento",
            colorActual = ColorDatos2FA,
            icono = Icons.Filled.Timer,
            colorIcono = ColorDatos2FA,
            alPulsar = { onSeleccionarColor("2fa", ColorDatos2FA) }
        )

        ComponenteSeparador()

        ComponenteColorPicker(
            titulo = "Criptografía y Llaves avanzadas",
            subtitulo = "Passkeys FIDO2, frases semilla y claves públicas",
            colorActual = ColorDatosPasskey,
            icono = Icons.Filled.Fingerprint,
            colorIcono = ColorDatosPasskey,
            alPulsar = { onSeleccionarColor("passkey", ColorDatosPasskey) }
        )

        ComponenteSeparador()

        ComponenteColorPicker(
            titulo = "Redes, Servidores y Web",
            subtitulo = "Sitios web, hosts de servidores, SSID Wi-Fi y bancos",
            colorActual = ColorDatosWeb,
            icono = Icons.Filled.Language,
            colorIcono = ColorDatosWeb,
            alPulsar = { onSeleccionarColor("web", ColorDatosWeb) }
        )

        ComponenteSeparador()

        ComponenteColorPicker(
            titulo = "Aplicaciones vinculadas",
            subtitulo = "Paquetes y aplicaciones Android asociadas",
            colorActual = ColorDatosApp,
            icono = Icons.Filled.Android,
            colorIcono = ColorDatosApp,
            alPulsar = { onSeleccionarColor("app", ColorDatosApp) }
        )
    }
}
