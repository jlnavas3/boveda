package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults

fun aplicarPersonalizacionColores(ajustes: AjustesApp) {
    ColorDinamicoSistema = ajustes.colorDinamicoSistema
    if (ajustes.colorAcento == "sobrio" || ajustes.colorAcento.isBlank()) {
        paletaAcentoActiva = null
        colorAcentoManual = null
        colorAcentoFuerteManual = null
    } else if (ajustes.colorAcento.startsWith("#") || ajustes.colorAcento.startsWith("0x")) {
        paletaAcentoActiva = null
        val fallback = PaletaSobriaDefaults.OSCURA.acento
        val custom = parsearColorO(ajustes.colorAcento, fallback)
        colorAcentoManual = custom
        colorAcentoFuerteManual = custom
    } else {
        val paleta = PaletaAcento.entries.firstOrNull { it.clave == ajustes.colorAcento }
        paletaAcentoActiva = paleta
        colorAcentoManual = null
        colorAcentoFuerteManual = null
    }

    if (ajustes.colorIconosInternos.isNotBlank()) {
        ColorIconosInternos = parsearColorO(ajustes.colorIconosInternos, if (esOscuroActivo) Color(0xFFD6DAE2) else Color(0xFF1E232E))
    } else {
        colorIconosBase = null
    }

    if (ajustes.colorTitulos.isNotBlank()) {
        ColorTitulos = parsearColorO(ajustes.colorTitulos, if (esOscuroActivo) Color(0xFFF3F4F8) else Color(0xFF11141A))
    } else {
        colorTitulosBase = null
    }

    if (ajustes.colorTarjetas.isNotBlank()) {
        ColorTarjetas = parsearColorO(ajustes.colorTarjetas, if (esOscuroActivo) paletaOscura.superficieAlta else paletaClara.superficie)
    } else {
        colorTarjetasBase = null
    }

    // Colores semánticos de secciones funcionales
    ColorSeguridad = parsearColorO(ajustes.colorSeguridad, Color(0xFF0284C7))
    ColorArgon2 = parsearColorO(ajustes.colorArgon2, Color(0xFF2563EB))
    ColorCamara = parsearColorO(ajustes.colorCamara, Color(0xFF06B6D4))
    Color2FA = parsearColorO(ajustes.color2FA, Color(0xFFF97316))
    ColorPasskeys = parsearColorO(ajustes.colorPasskeys, Color(0xFF8B5CF6))
    ColorGenerador = parsearColorO(ajustes.colorGenerador, Color(0xFF0D9488))
    ColorSalud = parsearColorO(ajustes.colorSalud, Color(0xFF10B981))
    ColorPapelera = parsearColorO(ajustes.colorPapelera, Color(0xFFEF4444))
    ColorExportacion = parsearColorO(ajustes.colorExportacion, Color(0xFF6366F1))

    // Colores aislados exclusivos para datos e indicadores de tarjetas
    ColorDatosUsuario = parsearColorO(ajustes.colorDatosUsuario, Color(0xFF0284C7))
    ColorDatosContrasena = parsearColorO(ajustes.colorDatosContrasena, Color(0xFF0D9488))
    ColorDatos2FA = parsearColorO(ajustes.colorDatos2FA, Color(0xFFF97316))
    ColorDatosPasskey = parsearColorO(ajustes.colorDatosPasskey, Color(0xFF8B5CF6))
    ColorDatosWeb = parsearColorO(ajustes.colorDatosWeb, Color(0xFF06B6D4))
    ColorDatosApp = parsearColorO(ajustes.colorDatosApp, Color(0xFF10B981))

    // Colores de identificadores jerárquicos de Ajustes (06-SIS-AVZ-COL)
    ColorIdSeguridad = parsearColorO(ajustes.colorIdSeguridad, parsearColorO(AjustesDefaults.ColoresIds.SEGURIDAD, Color(0xFF3F51B5)))
    ColorIdApariencia = parsearColorO(ajustes.colorIdApariencia, parsearColorO(AjustesDefaults.ColoresIds.APARIENCIA, Color(0xFF8E24AA)))
    ColorIdLista = parsearColorO(ajustes.colorIdLista, parsearColorO(AjustesDefaults.ColoresIds.LISTA, Color(0xFF00897B)))
    ColorIdHerramientas = parsearColorO(ajustes.colorIdHerramientas, parsearColorO(AjustesDefaults.ColoresIds.HERRAMIENTAS, Color(0xFFFB8C00)))
    ColorIdCopias = parsearColorO(ajustes.colorIdCopias, parsearColorO(AjustesDefaults.ColoresIds.COPIAS, Color(0xFF1E88E5)))
    ColorIdSistema = parsearColorO(ajustes.colorIdSistema, parsearColorO(AjustesDefaults.ColoresIds.SISTEMA, Color(0xFF607D8B)))
}
