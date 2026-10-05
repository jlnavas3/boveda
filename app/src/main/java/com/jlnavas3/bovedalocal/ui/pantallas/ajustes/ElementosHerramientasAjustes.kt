package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Define los elementos de menú del Hub de Ajustes para Herramientas.
 */
fun crearElementosHerramientasAjustes(
    vm: VaultViewModel,
    alAbrirProveedorPasskeys: () -> Unit
): List<ElementoMenuAjustes> = listOf(
    ElementoMenuAjustes(
        titulo = "Verificación en dos pasos",
        subtitulo = "Parámetros predeterminados de códigos de dos pasos",
        icono = Icons.Filled.Timer,
        colorIcono = Color(0xFF3949AB),
        idEtiqueta = "04-HER-AUT",
        grupo = "Herramientas",
        palabrasClave = "2fa totp autenticador codigos periodo hmac digitos verificacion dos pasos",
        alPulsar = { vm.ir(Pantalla.AjustesAutenticador("04-HER-AUT")) }
    ),
    ElementoMenuAjustes(
        titulo = "Historial de claves",
        subtitulo = "Retención temporal y autodestrucción",
        icono = Icons.Filled.History,
        colorIcono = Color(0xFFFF9800),
        idEtiqueta = "04-HER-HST",
        grupo = "Herramientas",
        palabrasClave = "historial contrasenas generadas retencion autodestruccion claves temporal tiempo",
        alPulsar = { vm.ir(Pantalla.AjustesHistorial("04-HER-HST")) }
    ),
    ElementoMenuAjustes(
        titulo = "Cámara y escáner",
        subtitulo = "Motor óptico CameraX y compatibilidad",
        icono = Icons.Filled.CameraAlt,
        colorIcono = Color(0xFF00897B),
        idEtiqueta = "04-HER-CAM",
        grupo = "Herramientas",
        palabrasClave = "camara escaner qr camerax optico lector",
        alPulsar = { vm.ir(Pantalla.AjustesCamara("04-HER-CAM")) }
    ),
    ElementoMenuAjustes(
        titulo = "Widgets",
        subtitulo = "Generador 1x1 y accesos directos TOTP",
        icono = Icons.Filled.Widgets,
        colorIcono = Color(0xFF7CB342),
        idEtiqueta = "04-HER-WGT",
        grupo = "Herramientas",
        palabrasClave = "widgets escritorio inicio favoritos totp generador 1x1",
        alPulsar = { vm.ir(Pantalla.AjustesWidget("04-HER-WGT")) }
    ),
    ElementoMenuAjustes(
        titulo = "Mosaico rápido",
        subtitulo = "Generación desde barra de notificaciones",
        icono = Icons.Filled.Tune,
        colorIcono = Color(0xFFFBC02D),
        idEtiqueta = "04-HER-MSK",
        grupo = "Herramientas",
        palabrasClave = "tile mosaico barra estado cortina notificaciones rapido",
        alPulsar = { vm.ir(Pantalla.TileRapido("04-HER-MSK")) }
    ),
    ElementoMenuAjustes(
        titulo = "Autocompletado y llaves",
        subtitulo = "Sugerencias en teclado y proveedor de credenciales",
        icono = Icons.Filled.Key,
        colorIcono = Color(0xFF8B5CF6),
        idEtiqueta = "04-HER-PSK",
        grupo = "Herramientas",
        palabrasClave = "autofill autocompletado teclado sugerencias passkey passkeys proveedor credenciales llaves paso acceso android servicio activar",
        alPulsar = { vm.ir(Pantalla.AjustesAutocompletado("04-HER-PSK")) }
    )
)
