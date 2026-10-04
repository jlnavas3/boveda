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
        titulo = "Biometría",
        subtitulo = "Huella dactilar, bloqueo de app y portapapeles",
        icono = Icons.Filled.Fingerprint,
        colorIcono = Color(0xFF1E88E5),
        idEtiqueta = "01-SEG-BIO",
        grupo = "Seguridad",
        palabrasClave = "huella biometria pin contrasena bloqueo inactividad flag secure pantalla portapapeles",
        alPulsar = { vm.ir(Pantalla.Seguridad("01-SEG-BIO")) }
    ),
    ElementoMenuAjustes(
        titulo = "Clave maestra",
        subtitulo = "Cambiar la contraseña principal de la bóveda",
        icono = Icons.Filled.Lock,
        colorIcono = Color(0xFF00897B),
        idEtiqueta = "01-SEG-PAS",
        grupo = "Seguridad",
        palabrasClave = "clave contrasena maestra cambiar pass principal",
        alPulsar = alAbrirCambioMaestra
    ),
    ElementoMenuAjustes(
        titulo = "Modo señuelo",
        subtitulo = "PIN de coacción y cuentas simuladas",
        icono = Icons.Filled.Security,
        colorIcono = Color(0xFFFB8C00),
        idEtiqueta = "01-SEG-SEN",
        grupo = "Seguridad",
        palabrasClave = "senuelo coaccion pin falso fake simulado cuentas",
        alPulsar = { vm.ir(Pantalla.AjustesSenuelo("01-SEG-SEN")) }
    ),
    ElementoMenuAjustes(
        titulo = "Autodestrucción",
        subtitulo = "Borrado irreversible por PIN de emergencia",
        icono = Icons.Filled.DeleteForever,
        colorIcono = Color(0xFFE53935),
        idEtiqueta = "01-SEG-DES",
        grupo = "Seguridad",
        palabrasClave = "autodestruccion borrar destruir emergencia peligro pin panico",
        alPulsar = { vm.ir(Pantalla.AjustesAutodestruccion("01-SEG-DES")) }
    ),
    ElementoMenuAjustes(
        titulo = "Cifrado",
        subtitulo = "Parámetros Argon2id de resistencia KDF",
        icono = Icons.Filled.Memory,
        colorIcono = Color(0xFF5C6BC0),
        idEtiqueta = "01-SEG-CRY",
        grupo = "Seguridad",
        palabrasClave = "argon2id cifrado algoritmo hash ram memoria hilos kdf",
        alPulsar = { vm.ir(Pantalla.Argon2id("01-SEG-CRY")) }
    )
)
