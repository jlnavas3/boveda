package com.jlnavas3.bovedalocal.ui.theme

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp

/**
 * El fondo/superficie/texto son de estado global (Snapshot de Compose), igual que
 * el acento más abajo: cualquier pantalla que los lea se recompone sola al cambiar
 * de tema en Ajustes, sin enhebrar un CompositionLocal por toda la app.
 */
internal class PaletaBase(
    val fondo: Color,
    val superficie: Color,
    val superficieAlta: Color,
    val borde: Color,
    val textoPrincipal: Color,
    val textoSecundario: Color,
    val menta: Color,
    val peligro: Color
)

internal val paletaOscura = PaletaBase(
    fondo = Color(0xFF000000),
    superficie = Color(0xFF212023),
    superficieAlta = Color(0xFF2A292E),
    borde = Color(0xFF38373C),
    // Gris claro, no blanco puro: de noche, el blanco a #FFF brilla y molesta a la vista.
    textoPrincipal = Color(0xFFE6E6EA),
    textoSecundario = Color(0xFF9E9EA4),
    menta = Color(0xFF57E6B4),
    peligro = Color(0xFFFF5D5D)
)

internal val paletaClara = PaletaBase(
    fondo = Color(0xFFF7F7FA),
    superficie = Color(0xFFFFFFFF),
    superficieAlta = Color(0xFFEFEFF3),
    borde = Color(0xFFDBDEE6),
    textoPrincipal = Color(0xFF15171F),
    textoSecundario = Color(0xFF5C6270),
    menta = Color(0xFF1F9C74),
    peligro = Color(0xFFD23F3F)
)

internal var paletaActiva by mutableStateOf(paletaOscura)
var esOscuroActivo by mutableStateOf(true)

val Obsidiana: Color get() = paletaActiva.fondo
val Superficie: Color get() = paletaActiva.superficie
val SuperficieAlta: Color get() = paletaActiva.superficieAlta
val Borde: Color get() = paletaActiva.borde
val Menta: Color get() = paletaActiva.menta
val Peligro: Color get() = paletaActiva.peligro
val TextoPrincipal: Color get() = paletaActiva.textoPrincipal
val TextoSecundario: Color get() = paletaActiva.textoSecundario

/** "sistema", "claro" u "oscuro"; "sistema" sigue el tema actual del teléfono. */
fun aplicarTema(claveTema: String, sistemaEnOscuro: Boolean) {
    esOscuroActivo = when (claveTema) {
        "claro" -> false
        "oscuro" -> true
        else -> sistemaEnOscuro
    }
    paletaActiva = if (esOscuroActivo) paletaOscura else paletaClara
}

internal var paletaAcentoActiva by mutableStateOf<PaletaAcento?>(PaletaAcento.AMBAR)
internal var colorAcentoManual by mutableStateOf<Color?>(null)
internal var colorAcentoFuerteManual by mutableStateOf<Color?>(null)
internal var colorDinamicoMonet by mutableStateOf<Color?>(null)
internal var colorDinamicoFuerteMonet by mutableStateOf<Color?>(null)
private var colorIconosBase by mutableStateOf<Color?>(null)
private var colorTitulosBase by mutableStateOf<Color?>(null)
private var colorTarjetasBase by mutableStateOf<Color?>(null)

var ColorAcento: Color
    get() {
        if (colorDinamicoSistemaBase && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && colorDinamicoMonet != null) {
            return colorDinamicoMonet!!
        }
        return colorAcentoManual ?: paletaAcentoActiva?.base ?: PaletaAcento.AMBAR.base
    }
    set(valor) {
        colorAcentoManual = valor
        colorAcentoFuerteManual = valor
    }

var ColorAcentoFuerte: Color
    get() {
        if (colorDinamicoSistemaBase && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && colorDinamicoFuerteMonet != null) {
            return colorDinamicoFuerteMonet!!
        }
        return colorAcentoFuerteManual ?: paletaAcentoActiva?.fuerte ?: PaletaAcento.AMBAR.fuerte
    }
    set(valor) { colorAcentoFuerteManual = valor }

val ColorSobreAcento: Color get() = colorContraste(ColorAcento)

var ColorIconosInternos: Color
    get() = colorIconosBase ?: if (esOscuroActivo) Color(0xFFD6DAE2) else Color(0xFF1E232E)
    set(valor) { colorIconosBase = valor }

var ColorTitulos: Color
    get() = colorTitulosBase ?: if (esOscuroActivo) Color(0xFFF3F4F8) else Color(0xFF11141A)
    set(valor) { colorTitulosBase = valor }

var ColorTarjetas: Color
    get() = colorTarjetasBase ?: if (esOscuroActivo) paletaOscura.superficieAlta else paletaClara.superficie
    set(valor) { colorTarjetasBase = valor }

val ColorSobreTarjetas: Color get() = colorContraste(ColorTarjetas)

// Compatibilidad directa con el código existente
var Ambar: Color
    get() = ColorAcento
    set(valor) { ColorAcento = valor }

var AmbarFuerte: Color
    get() = ColorAcentoFuerte
    set(valor) { ColorAcentoFuerte = valor }

val DegradadoAmbar: Brush get() = Brush.horizontalGradient(listOf(ColorAcento, ColorAcentoFuerte))

fun degradadoAmbarVertical() = Brush.verticalGradient(listOf(ColorAcento, ColorAcentoFuerte))

internal var colorDinamicoSistemaBase by mutableStateOf(true)

var ColorDinamicoSistema: Boolean
    get() = colorDinamicoSistemaBase
    set(valor) { colorDinamicoSistemaBase = valor }

fun aplicarPersonalizacionColores(ajustes: AjustesApp) {
    colorDinamicoSistemaBase = ajustes.colorDinamicoSistema
    val paleta = PaletaAcento.entries.firstOrNull { it.clave == ajustes.colorAcento }
    if (paleta != null) {
        paletaAcentoActiva = paleta
        colorAcentoManual = null
        colorAcentoFuerteManual = null
    } else {
        paletaAcentoActiva = null
        val custom = parsearColorO(ajustes.colorAcento, PaletaAcento.AMBAR.base)
        colorAcentoManual = custom
        colorAcentoFuerteManual = custom
    }

    colorIconosBase = if (ajustes.colorIconosInternos.isNotBlank()) {
        parsearColorO(ajustes.colorIconosInternos, if (esOscuroActivo) Color(0xFFD6DAE2) else Color(0xFF1E232E))
    } else {
        null
    }

    colorTitulosBase = if (ajustes.colorTitulos.isNotBlank()) {
        parsearColorO(ajustes.colorTitulos, if (esOscuroActivo) Color(0xFFF3F4F8) else Color(0xFF11141A))
    } else {
        null
    }

    colorTarjetasBase = if (ajustes.colorTarjetas.isNotBlank()) {
        parsearColorO(ajustes.colorTarjetas, if (esOscuroActivo) paletaOscura.superficieAlta else paletaClara.superficie)
    } else {
        null
    }

    // Colores semánticos de secciones funcionales
    colorSeguridadBase = parsearColorO(ajustes.colorSeguridad, Color(0xFF0284C7))
    colorArgon2Base = parsearColorO(ajustes.colorArgon2, Color(0xFF2563EB))
    colorCamaraBase = parsearColorO(ajustes.colorCamara, Color(0xFF06B6D4))
    color2FABase = parsearColorO(ajustes.color2FA, Color(0xFFF97316))
    colorPasskeysBase = parsearColorO(ajustes.colorPasskeys, Color(0xFF8B5CF6))
    colorGeneradorBase = parsearColorO(ajustes.colorGenerador, Color(0xFF0D9488))
    colorSaludBase = parsearColorO(ajustes.colorSalud, Color(0xFF10B981))
    colorPapeleraBase = parsearColorO(ajustes.colorPapelera, Color(0xFFEF4444))
    colorExportacionBase = parsearColorO(ajustes.colorExportacion, Color(0xFF6366F1))

    // Colores aislados exclusivos para datos e indicadores de tarjetas
    colorDatosUsuarioBase = parsearColorO(ajustes.colorDatosUsuario, Color(0xFF0284C7))
    colorDatosContrasenaBase = parsearColorO(ajustes.colorDatosContrasena, Color(0xFF0D9488))
    colorDatos2FABase = parsearColorO(ajustes.colorDatos2FA, Color(0xFFF97316))
    colorDatosPasskeyBase = parsearColorO(ajustes.colorDatosPasskey, Color(0xFF8B5CF6))
    colorDatosWebBase = parsearColorO(ajustes.colorDatosWeb, Color(0xFF06B6D4))
    colorDatosAppBase = parsearColorO(ajustes.colorDatosApp, Color(0xFF10B981))
}

// Colores Semánticos de Secciones Funcionales
private var colorSeguridadBase by mutableStateOf(Color(0xFF0284C7))
private var colorArgon2Base by mutableStateOf(Color(0xFF2563EB))
private var colorCamaraBase by mutableStateOf(Color(0xFF06B6D4))
private var color2FABase by mutableStateOf(Color(0xFFF97316))
private var colorPasskeysBase by mutableStateOf(Color(0xFF8B5CF6))
private var colorGeneradorBase by mutableStateOf(Color(0xFF0D9488))
private var colorSaludBase by mutableStateOf(Color(0xFF10B981))
private var colorPapeleraBase by mutableStateOf(Color(0xFFEF4444))
private var colorExportacionBase by mutableStateOf(Color(0xFF6366F1))

// Colores Aislados Exclusivos para Datos e Indicadores de Tarjetas
private var colorDatosUsuarioBase by mutableStateOf(Color(0xFF0284C7))
private var colorDatosContrasenaBase by mutableStateOf(Color(0xFF0D9488))
private var colorDatos2FABase by mutableStateOf(Color(0xFFF97316))
private var colorDatosPasskeyBase by mutableStateOf(Color(0xFF8B5CF6))
private var colorDatosWebBase by mutableStateOf(Color(0xFF06B6D4))
private var colorDatosAppBase by mutableStateOf(Color(0xFF10B981))

var ColorDatosUsuario: Color
    get() = colorDatosUsuarioBase
    set(valor) { colorDatosUsuarioBase = valor }

var ColorDatosContrasena: Color
    get() = colorDatosContrasenaBase
    set(valor) { colorDatosContrasenaBase = valor }

var ColorDatos2FA: Color
    get() = colorDatos2FABase
    set(valor) { colorDatos2FABase = valor }

var ColorDatosPasskey: Color
    get() = colorDatosPasskeyBase
    set(valor) { colorDatosPasskeyBase = valor }

var ColorDatosWeb: Color
    get() = colorDatosWebBase
    set(valor) { colorDatosWebBase = valor }

var ColorDatosApp: Color
    get() = colorDatosAppBase
    set(valor) { colorDatosAppBase = valor }

var ColorSeguridad: Color
    get() = colorSeguridadBase
    set(valor) { colorSeguridadBase = valor }

var ColorArgon2: Color
    get() = colorArgon2Base
    set(valor) { colorArgon2Base = valor }

var ColorCamara: Color
    get() = colorCamaraBase
    set(valor) { colorCamaraBase = valor }

var Color2FA: Color
    get() = color2FABase
    set(valor) { color2FABase = valor }

var ColorPasskeys: Color
    get() = colorPasskeysBase
    set(valor) { colorPasskeysBase = valor }

var ColorGenerador: Color
    get() = colorGeneradorBase
    set(valor) { colorGeneradorBase = valor }

var ColorSalud: Color
    get() = colorSaludBase
    set(valor) { colorSaludBase = valor }

var ColorPapelera: Color
    get() = colorPapeleraBase
    set(valor) { colorPapeleraBase = valor }

var ColorExportacion: Color
    get() = colorExportacionBase
    set(valor) { colorExportacionBase = valor }
