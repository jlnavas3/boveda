package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Microcomponente que define los elementos de menú del Hub de Ajustes para:
 * - Seguridad y Cifrado
 * - Apariencia, Temas y Tipografía
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

fun crearElementosAparienciaAjustes(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    alAbrirNombreBoveda: () -> Unit
): List<ElementoMenuAjustes> = listOf(
    ElementoMenuAjustes(
        titulo = "Tema y colores",
        subtitulo = "Modo oscuro, paleta y acento dinámico",
        icono = Icons.Filled.Palette,
        colorIcono = Color(0xFF8E24AA),
        idEtiqueta = "02-APA-THM",
        grupo = "Apariencia",
        palabrasClave = "tema colores paleta acento apariencia aspecto oscuro claro sistema",
        valorTexto = when (ajustes.temaApp) {
            "claro" -> "Claro"
            "oscuro" -> "Oscuro"
            else -> "Sistema"
        },
        alPulsar = { vm.ir(Pantalla.Tema("02-APA-THM")) }
    ),
    ElementoMenuAjustes(
        titulo = "Tipografía",
        subtitulo = "Familia, peso y escala tipográfica",
        icono = Icons.Filled.TextFields,
        colorIcono = Color(0xFF5E35B1),
        idEtiqueta = "02-APA-FNT",
        grupo = "Apariencia",
        palabrasClave = "fuente tipografia letra mono sans tamano escala peso",
        valorTexto = ajustes.familiaFuente.replaceFirstChar { it.uppercase() },
        alPulsar = { vm.ir(Pantalla.Tipografia("02-APA-FNT")) }
    ),
    ElementoMenuAjustes(
        titulo = "Formas y bordes",
        subtitulo = "Curvatura, grosor de líneas y separación",
        icono = Icons.Filled.SquareFoot,
        colorIcono = Color(0xFF3949AB),
        idEtiqueta = "02-APA-SHP",
        grupo = "Apariencia",
        palabrasClave = "formas bordes esquinas curvatura radio contorno tarjetas estilo separacion",
        valorTexto = "${ajustes.curvaturaEsquinasDp.toInt()}dp",
        alPulsar = { vm.ir(Pantalla.Formas("02-APA-SHP")) }
    ),
    ElementoMenuAjustes(
        titulo = "Identificadores de campos",
        subtitulo = "Pastillas de color por tipo de credencial",
        icono = Icons.Filled.Badge,
        colorIcono = Color(0xFF1E88E5),
        idEtiqueta = "02-APA-COL",
        grupo = "Apariencia",
        palabrasClave = "colores etiquetas tipos credenciales pastillas insignias",
        alPulsar = { vm.ir(Pantalla.ColoresDatos("02-APA-COL")) }
    ),
    ElementoMenuAjustes(
        titulo = "Nombre de la bóveda",
        subtitulo = "Personalizar el texto de la cabecera",
        icono = Icons.Filled.Badge,
        colorIcono = Color(0xFF00897B),
        idEtiqueta = "02-APA-LNC",
        grupo = "Apariencia",
        palabrasClave = "nombre personalizada titulo encabezado boveda local",
        valorTexto = ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
        alPulsar = alAbrirNombreBoveda
    )
)
