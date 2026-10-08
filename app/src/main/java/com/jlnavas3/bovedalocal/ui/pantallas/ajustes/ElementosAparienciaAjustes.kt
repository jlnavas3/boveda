package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Define los elementos de menú del Hub de Ajustes para Apariencia, Temas y Tipografía.
 */
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
        alPulsar = { vm.ir(Pantalla.Tema()) }
    ),
    ElementoMenuAjustes(
        titulo = "Tipografía",
        subtitulo = "Familia, peso y escala tipográfica",
        icono = Icons.Filled.TextFields,
        colorIcono = Color(0xFF5E35B1),
        idEtiqueta = "02-APA-TYP",
        grupo = "Apariencia",
        palabrasClave = "fuente tipografia letra mono sans tamano escala peso",
        valorTexto = ajustes.familiaFuente.replaceFirstChar { it.uppercase() },
        alPulsar = { vm.ir(Pantalla.Tipografia()) }
    ),
    ElementoMenuAjustes(
        titulo = "Formas y bordes",
        subtitulo = "Curvatura, grosor de líneas y separación",
        icono = Icons.Filled.SquareFoot,
        colorIcono = Color(0xFF3949AB),
        idEtiqueta = "02-APA-GEO",
        grupo = "Apariencia",
        palabrasClave = "formas bordes esquinas curvatura radio contorno tarjetas estilo separacion",
        valorTexto = "${ajustes.curvaturaEsquinasDp.toInt()}dp",
        alPulsar = { vm.ir(Pantalla.Formas()) }
    ),
    ElementoMenuAjustes(
        titulo = "Identificadores de campos",
        subtitulo = "Pastillas de color por tipo de credencial",
        icono = Icons.Filled.Badge,
        colorIcono = Color(0xFF1E88E5),
        idEtiqueta = "02-APA-COL",
        grupo = "Apariencia",
        palabrasClave = "colores etiquetas tipos credenciales pastillas insignias",
        alPulsar = { vm.ir(Pantalla.ColoresDatos()) }
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
