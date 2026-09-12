package com.jlnavas3.bovedalocal.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.core.view.WindowCompat
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * El fondo/superficie/texto son de estado global (Snapshot de Compose), igual que
 * el acento más abajo: cualquier pantalla que los lea se recompone sola al cambiar
 * de tema en Ajustes, sin enhebrar un CompositionLocal por toda la app.
 */
private class PaletaBase(
    val fondo: Color,
    val superficie: Color,
    val superficieAlta: Color,
    val borde: Color,
    val textoPrincipal: Color,
    val textoSecundario: Color,
    val menta: Color,
    val peligro: Color
)

private val paletaOscura = PaletaBase(
    fondo = Color(0xFF0E0F13),
    superficie = Color(0xFF1B1F2A),
    superficieAlta = Color(0xFF252B3A),
    borde = Color(0xFF3A4356),
    // Gris claro, no blanco puro: de noche, el blanco a #FFF brilla y molesta a la vista.
    textoPrincipal = Color(0xFFD6DAE2),
    textoSecundario = Color(0xFF9AA3B8),
    menta = Color(0xFF57E6B4),
    peligro = Color(0xFFFF5D5D)
)

private val paletaClara = PaletaBase(
    fondo = Color(0xFFF7F7FA),
    superficie = Color(0xFFFFFFFF),
    superficieAlta = Color(0xFFEFEFF3),
    borde = Color(0xFFDBDEE6),
    textoPrincipal = Color(0xFF15171F),
    textoSecundario = Color(0xFF5C6270),
    menta = Color(0xFF1F9C74),
    peligro = Color(0xFFD23F3F)
)

private var paletaActiva by mutableStateOf(paletaOscura)
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

fun colorContraste(fondo: Color): Color {
    val luminancia = 0.2126f * fondo.red + 0.7152f * fondo.green + 0.0722f * fondo.blue
    return if (luminancia > 0.45f) Color(0xFF15171F) else Color(0xFFFFFFFF)
}

fun parsearColorO(hex: String, porDefecto: Color): Color {
    if (hex.isBlank()) return porDefecto
    return try {
        val limpio = hex.removePrefix("#").trim()
        val valorLong = limpio.toLong(16)
        when (limpio.length) {
            6 -> Color((0xFF000000 or valorLong).toInt())
            8 -> Color(valorLong)
            else -> porDefecto
        }
    } catch (_: Exception) {
        porDefecto
    }
}

fun Color.aHex(): String {
    val r = (red * 255f).toInt().coerceIn(0, 255)
    val g = (green * 255f).toInt().coerceIn(0, 255)
    val b = (blue * 255f).toInt().coerceIn(0, 255)
    return String.format("#%02X%02X%02X", r, g, b)
}

private var paletaAcentoActiva by mutableStateOf<PaletaAcento?>(PaletaAcento.AMBAR)
private var colorAcentoManual by mutableStateOf<Color?>(null)
private var colorAcentoFuerteManual by mutableStateOf<Color?>(null)
private var colorIconosBase by mutableStateOf<Color?>(null)
private var colorTitulosBase by mutableStateOf<Color?>(null)
private var colorTarjetasBase by mutableStateOf<Color?>(null)

var ColorAcento: Color
    get() = colorAcentoManual ?: paletaAcentoActiva?.base ?: PaletaAcento.AMBAR.base
    set(valor) {
        colorAcentoManual = valor
        colorAcentoFuerteManual = valor
    }

var ColorAcentoFuerte: Color
    get() = colorAcentoFuerteManual ?: paletaAcentoActiva?.fuerte ?: PaletaAcento.AMBAR.fuerte
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

private var colorDinamicoSistemaBase by mutableStateOf(false)

var ColorDinamicoSistema: Boolean
    get() = colorDinamicoSistemaBase
    set(valor) { colorDinamicoSistemaBase = valor }

fun aplicarPersonalizacionColores(ajustes: com.jlnavas3.bovedalocal.data.AjustesApp) {
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
    color2FABase = parsearColorO(ajustes.color2FA, Color(0xFFF97316))
    colorPasskeysBase = parsearColorO(ajustes.colorPasskeys, Color(0xFF8B5CF6))
    colorGeneradorBase = parsearColorO(ajustes.colorGenerador, Color(0xFF0D9488))
    colorSaludBase = parsearColorO(ajustes.colorSalud, Color(0xFF10B981))
    colorPapeleraBase = parsearColorO(ajustes.colorPapelera, Color(0xFFEF4444))
    colorExportacionBase = parsearColorO(ajustes.colorExportacion, Color(0xFF6366F1))
}

// Colores Semánticos de Secciones Funcionales
private var colorSeguridadBase by mutableStateOf(Color(0xFF0284C7))
private var color2FABase by mutableStateOf(Color(0xFFF97316))
private var colorPasskeysBase by mutableStateOf(Color(0xFF8B5CF6))
private var colorGeneradorBase by mutableStateOf(Color(0xFF0D9488))
private var colorSaludBase by mutableStateOf(Color(0xFF10B981))
private var colorPapeleraBase by mutableStateOf(Color(0xFFEF4444))
private var colorExportacionBase by mutableStateOf(Color(0xFF6366F1))

var ColorSeguridad: Color
    get() = colorSeguridadBase
    set(valor) { colorSeguridadBase = valor }

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

// -------------------------------------------------------------------------------------------------
// Tokens Dinámicos de Bordes, Formas y Espaciado (Snapshot State en tiempo real)
// -------------------------------------------------------------------------------------------------

private var curvaturaEsquinasDpBase by mutableStateOf(6f)
private var grosorBordeDpBase by mutableStateOf(0.8f)
private var estiloBordeBase by mutableStateOf("marcado")
private var espaciadoComponentesDpBase by mutableStateOf(14f)

var CurvaturaEsquinasDp: Float
    get() = curvaturaEsquinasDpBase
    set(valor) { curvaturaEsquinasDpBase = valor }

val CurvaturaEsquinas: Dp get() = curvaturaEsquinasDpBase.dp

var GrosorBordeDp: Float
    get() = grosorBordeDpBase
    set(valor) { grosorBordeDpBase = valor }

val GrosorBorde: Dp get() = grosorBordeDpBase.dp

var EstiloBorde: String
    get() = estiloBordeBase
    set(valor) { estiloBordeBase = valor }

var EspaciadoComponentesDp: Float
    get() = espaciadoComponentesDpBase
    set(valor) { espaciadoComponentesDpBase = valor }

val EspaciadoComponentes: Dp get() = espaciadoComponentesDpBase.dp

val ColorBordeActual: Color get() = when (estiloBordeBase) {
    "acento" -> ColorAcento.copy(alpha = 0.55f)
    "marcado" -> if (esOscuroActivo) Color(0xFF63718E) else Color(0xFF9AA3B8)
    "ninguno" -> Color.Transparent
    else -> paletaActiva.borde
}

/** Borde externo para el contenedor de menús desplegables (DropdownMenu), adaptativo y sutil. */
val ColorBordeDropdown: Color get() = when (estiloBordeBase) {
    "acento" -> ColorAcento.copy(alpha = if (esOscuroActivo) 0.45f else 0.55f)
    "marcado" -> if (esOscuroActivo) Color(0xFF455066) else Color(0xFFB8BFCE)
    "ninguno" -> if (esOscuroActivo) Color(0xFF2E3545) else Color(0xFFE2E5EC)
    else -> if (esOscuroActivo) Color(0xFF3A4356) else Color(0xFFD4D8E2)
}

/** Separador o divisor sutil entre las opciones de un menú desplegable (DropdownMenu). */
val ColorSeparadorDropdown: Color get() = if (esOscuroActivo) Color(0xFF2A3140) else Color(0xFFE0E3EB)

/** Tono del encabezado para tarjetas desplegables: sutilmente más oscuro que el cuerpo. */
val ColorEncabezadoTarjeta: Color get() {
    val factor = if (esOscuroActivo) 0.72f else 0.91f
    return Color(
        red = (ColorTarjetas.red * factor).coerceIn(0f, 1f),
        green = (ColorTarjetas.green * factor).coerceIn(0f, 1f),
        blue = (ColorTarjetas.blue * factor).coerceIn(0f, 1f),
        alpha = 1f
    )
}

val FormaTarjeta: RoundedCornerShape get() = RoundedCornerShape(CurvaturaEsquinas)
val FormaBoton: RoundedCornerShape get() = RoundedCornerShape(CurvaturaEsquinas)
val FormaCampo: RoundedCornerShape get() = RoundedCornerShape((CurvaturaEsquinas * 0.9f).coerceAtLeast(4.dp))
val FormaPequena: RoundedCornerShape get() = RoundedCornerShape((CurvaturaEsquinas * 0.6f).coerceAtLeast(3.dp))

fun aplicarPersonalizacionFormas(ajustes: com.jlnavas3.bovedalocal.data.AjustesApp) {
    curvaturaEsquinasDpBase = ajustes.curvaturaEsquinasDp
    grosorBordeDpBase = ajustes.grosorBordeDp
    estiloBordeBase = ajustes.estiloBorde
    espaciadoComponentesDpBase = ajustes.espaciadoComponentesDp
}

// -------------------------------------------------------------------------------------------------
// Tokens Dinámicos de Tipografía y Textos (Snapshot State en tiempo real)
// -------------------------------------------------------------------------------------------------

private var escalaTextoBase by mutableStateOf(1.0f)
private var pesoTextoBase by mutableStateOf("normal")
private var cursivaTextoBase by mutableStateOf(false)
private var espaciadoLetrasSpBase by mutableStateOf(0.0f)
private var interlineadoFactorBase by mutableStateOf(1.0f)
private var familiaFuenteBase by mutableStateOf("sans")

var EscalaTexto: Float
    get() = escalaTextoBase
    set(valor) { escalaTextoBase = valor }

var PesoTextoClave: String
    get() = pesoTextoBase
    set(valor) { pesoTextoBase = valor }

var CursivaTexto: Boolean
    get() = cursivaTextoBase
    set(valor) { cursivaTextoBase = valor }

var EspaciadoLetrasSp: Float
    get() = espaciadoLetrasSpBase
    set(valor) { espaciadoLetrasSpBase = valor }

var InterlineadoFactor: Float
    get() = interlineadoFactorBase
    set(valor) { interlineadoFactorBase = valor }

var FamiliaFuenteClave: String
    get() = familiaFuenteBase
    set(valor) { familiaFuenteBase = valor }

val FamiliaFuenteActual: FontFamily get() = when (familiaFuenteBase) {
    "mono" -> FontFamily.Monospace
    "serif" -> FontFamily.Serif
    "cursiva" -> FontFamily.Cursive
    else -> FontFamily.SansSerif
}

val PesoTextoActual: FontWeight get() = when (pesoTextoBase) {
    "fino" -> FontWeight.Light
    "medio" -> FontWeight.Medium
    "seminegrita" -> FontWeight.SemiBold
    "negrita" -> FontWeight.Bold
    else -> FontWeight.Normal
}

val EstiloFuenteActual: FontStyle get() = if (cursivaTextoBase) FontStyle.Italic else FontStyle.Normal

fun aplicarPersonalizacionTipografia(ajustes: com.jlnavas3.bovedalocal.data.AjustesApp) {
    escalaTextoBase = ajustes.escalaTexto
    pesoTextoBase = ajustes.pesoTexto
    cursivaTextoBase = ajustes.cursivaTexto
    espaciadoLetrasSpBase = ajustes.espaciadoLetrasSp
    interlineadoFactorBase = ajustes.interlineadoFactor
    familiaFuenteBase = ajustes.familiaFuente
}

fun aplicarPersonalizacionTemaCompleto(ajustes: com.jlnavas3.bovedalocal.data.AjustesApp) {
    aplicarPersonalizacionColores(ajustes)
    aplicarPersonalizacionFormas(ajustes)
    aplicarPersonalizacionTipografia(ajustes)
}

/** Paletas de acento que puede elegir el usuario en Ajustes > Apariencia: optimizadas para contraste en tema oscuro y claro. */
enum class PaletaAcento(
    val clave: String,
    val etiqueta: String,
    val baseOscura: Color,
    val baseClara: Color,
    val fuerteOscuro: Color,
    val fuerteClaro: Color
) {
    AMBAR("ambar", "Ámbar", Color(0xFFFFB74D), Color(0xFFC05621), Color(0xFFFF8A3D), Color(0xFF9C4221)),
    MENTA("menta", "Menta", Color(0xFF57E6B4), Color(0xFF0D9488), Color(0xFF23C08D), Color(0xFF0F766E)),
    AZUL("azul", "Azul", Color(0xFF6FA8FF), Color(0xFF2563EB), Color(0xFF3D7EFF), Color(0xFF1D4ED8)),
    ROSA("rosa", "Rosa", Color(0xFFFF8FCB), Color(0xFFDB2777), Color(0xFFFF5FA8), Color(0xFFBE185D)),
    VIOLETA("violeta", "Violeta", Color(0xFFB98BFF), Color(0xFF7C3AED), Color(0xFF8C5CFF), Color(0xFF6D28D9)),
    ROJO("rojo", "Rojo", Color(0xFFF44336), Color(0xFFDC2626), Color(0xFFD32F2F), Color(0xFFB91C1C)),
    PURPURA("purpura", "Púrpura", Color(0xFF9C27B0), Color(0xFF7E22CE), Color(0xFF7B1FA2), Color(0xFF6B21A8)),
    PURPURA_OSCURO("purpura_oscuro", "Púrpura oscuro", Color(0xFF673AB7), Color(0xFF5B21B6), Color(0xFF512DA8), Color(0xFF4C1D95)),
    INDIGO("indigo", "Índigo", Color(0xFF3F51B5), Color(0xFF3730A3), Color(0xFF303F9F), Color(0xFF312E81)),
    CELESTE("celeste", "Celeste", Color(0xFF03A9F4), Color(0xFF0284C7), Color(0xFF0288D1), Color(0xFF0369A1)),
    CIAN("cian", "Cian", Color(0xFF00BCD4), Color(0xFF0891B2), Color(0xFF0097A7), Color(0xFF0E7490)),
    VERDE("verde", "Verde", Color(0xFF4CAF50), Color(0xFF16A34A), Color(0xFF388E3C), Color(0xFF15803D)),
    VERDE_CLARO("verde_claro", "Verde claro", Color(0xFF8BC34A), Color(0xFF4D7C0F), Color(0xFF689F38), Color(0xFF3F6212)),
    LIMA("lima", "Lima", Color(0xFFCDDC39), Color(0xFF4D7C0F), Color(0xFFAFB42B), Color(0xFF3F6212)),
    AMARILLO("amarillo", "Amarillo", Color(0xFFFFEB3B), Color(0xFFB45309), Color(0xFFFBC02D), Color(0xFF92400E)),
    NARANJA("naranja", "Naranja", Color(0xFFFF9800), Color(0xFFC2410C), Color(0xFFF57C00), Color(0xFF9A3412)),
    NARANJA_OSCURO("naranja_oscuro", "Naranja oscuro", Color(0xFFFF5722), Color(0xFFC2410C), Color(0xFFE64A19), Color(0xFF9A3412)),
    MARRON("marron", "Marrón", Color(0xFF8D6E63), Color(0xFF5D4037), Color(0xFF6D4C41), Color(0xFF4E342E)),
    GRIS("gris", "Gris", Color(0xFFB0BEC5), Color(0xFF475569), Color(0xFF90A4AE), Color(0xFF334155)),
    GRIS_AZULADO("gris_azulado", "Gris azulado", Color(0xFF90A4AE), Color(0xFF334155), Color(0xFF78909C), Color(0xFF1E293B));

    val base: Color get() = if (esOscuroActivo) baseOscura else baseClara
    val fuerte: Color get() = if (esOscuroActivo) fuerteOscuro else fuerteClaro

    companion object {
        fun desde(clave: String): PaletaAcento = entries.firstOrNull { it.clave == clave } ?: AMBAR
    }
}

fun aplicarPaletaAcento(paleta: PaletaAcento) {
    ColorAcento = paleta.base
    ColorAcentoFuerte = paleta.fuerte
}

private val esquemaActual: androidx.compose.material3.ColorScheme
    @Composable get() = if (esOscuroActivo) {
        darkColorScheme(
            primary = ColorAcento,
            onPrimary = ColorSobreAcento,
            primaryContainer = ColorAcentoFuerte,
            onPrimaryContainer = ColorSobreAcento,
            secondary = Menta,
            onSecondary = Color(0xFF15171F),
            background = Obsidiana,
            onBackground = TextoPrincipal,
            surface = Superficie,
            onSurface = TextoPrincipal,
            surfaceVariant = ColorTarjetas,
            onSurfaceVariant = TextoSecundario,
            outline = Borde,
            error = Peligro,
            onError = Color(0xFFFFFFFF)
        )
    } else {
        lightColorScheme(
            primary = ColorAcento,
            onPrimary = ColorSobreAcento,
            primaryContainer = ColorAcentoFuerte,
            onPrimaryContainer = ColorSobreAcento,
            secondary = Menta,
            onSecondary = Color(0xFFFFFFFF),
            background = Obsidiana,
            onBackground = TextoPrincipal,
            surface = Superficie,
            onSurface = TextoPrincipal,
            surfaceVariant = ColorTarjetas,
            onSurfaceVariant = TextoSecundario,
            outline = Borde,
            error = Peligro,
            onError = Color(0xFFFFFFFF)
        )
    }

val FormasDinamicas: Shapes
    get() = Shapes(
        extraSmall = FormaTarjeta,
        small = FormaPequena,
        medium = FormaCampo,
        large = FormaTarjeta,
        extraLarge = RoundedCornerShape((CurvaturaEsquinas * 1.35f).coerceAtLeast(8.dp))
    )

val TipografiaDinamica: Typography
    get() = Typography(
        displaySmall = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.Bold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (34 * EscalaTexto).sp,
            letterSpacing = ((-0.5f) + EspaciadoLetrasSp).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.Bold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (28 * EscalaTexto).sp,
            letterSpacing = ((-0.4f) + EspaciadoLetrasSp).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.Bold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (22 * EscalaTexto).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        titleLarge = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.Bold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (20 * EscalaTexto).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        titleMedium = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.SemiBold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (18 * EscalaTexto).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        titleSmall = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.SemiBold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (15 * EscalaTexto).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (16 * EscalaTexto).sp,
            lineHeight = (24 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (14 * EscalaTexto).sp,
            lineHeight = (20 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        bodySmall = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (12 * EscalaTexto).sp,
            lineHeight = (16 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        labelLarge = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.SemiBold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (15 * EscalaTexto).sp,
            letterSpacing = (0.2f + EspaciadoLetrasSp).sp
        ),
        labelMedium = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (12 * EscalaTexto).sp,
            letterSpacing = (0.2f + EspaciadoLetrasSp).sp
        ),
        labelSmall = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (10 * EscalaTexto).sp,
            letterSpacing = (0.2f + EspaciadoLetrasSp).sp
        )
    )

val EstiloMonoGrande: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = (26 * EscalaTexto).sp,
        letterSpacing = (1f + EspaciadoLetrasSp).sp
    )

val EstiloMono: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = PesoTextoActual,
        fontSize = (16 * EscalaTexto).sp,
        letterSpacing = (0.5f + EspaciadoLetrasSp).sp
    )

@Composable
fun PepoBovedaTheme(temaApp: String = "sistema", contenido: @Composable () -> Unit) {
    aplicarTema(temaApp, isSystemInDarkTheme())
    val contexto = LocalContext.current
    val vista = LocalView.current

    if (!vista.isInEditMode) {
        SideEffect {
            val ventana = vista.context.encontrarActividad()?.window
            if (ventana != null) {
                val insetsController = WindowCompat.getInsetsController(ventana, vista)
                // En modo claro (esOscuroActivo == false): isAppearanceLightStatusBars = true (íconos oscuros)
                // En modo oscuro (esOscuroActivo == true): isAppearanceLightStatusBars = false (íconos blancos)
                insetsController.isAppearanceLightStatusBars = !esOscuroActivo
                insetsController.isAppearanceLightNavigationBars = !esOscuroActivo
                @Suppress("DEPRECATION")
                ventana.statusBarColor = Obsidiana.toArgb()
                @Suppress("DEPRECATION")
                ventana.navigationBarColor = Obsidiana.toArgb()
            }
        }
    }

    val usarDinamico = colorDinamicoSistemaBase && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val esquema = if (usarDinamico) {
        val dinamico = if (esOscuroActivo) dynamicDarkColorScheme(contexto) else dynamicLightColorScheme(contexto)
        LaunchedEffect(dinamico.primary) {
            ColorAcento = dinamico.primary
            ColorAcentoFuerte = dinamico.primaryContainer
        }
        dinamico.copy(
            surface = Superficie,
            background = Obsidiana,
            surfaceVariant = ColorTarjetas,
            onSurface = TextoPrincipal,
            onBackground = TextoPrincipal,
            outline = Borde
        )
    } else {
        esquemaActual
    }

    MaterialTheme(
        colorScheme = esquema,
        typography = TipografiaDinamica,
        shapes = FormasDinamicas,
        content = contenido
    )
}

private tailrec fun Context.encontrarActividad(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.encontrarActividad()
    else -> null
}
