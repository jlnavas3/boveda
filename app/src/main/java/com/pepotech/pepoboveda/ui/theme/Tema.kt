package com.pepotech.pepoboveda.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
private var esOscuroActivo by mutableStateOf(true)

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

// El acento es de estado global (Snapshot de Compose) a propósito: así cualquier
// pantalla que lo lea se recompone sola al cambiar de paleta en Ajustes, sin
// tener que enhebrar un CompositionLocal por toda la app.
private var acentoBase by mutableStateOf(Color(0xFFFFB74D))
private var acentoFuerte by mutableStateOf(Color(0xFFFF8A3D))

var Ambar: Color
    get() = acentoBase
    set(valor) { acentoBase = valor }

var AmbarFuerte: Color
    get() = acentoFuerte
    set(valor) { acentoFuerte = valor }

val DegradadoAmbar: Brush get() = Brush.horizontalGradient(listOf(Ambar, AmbarFuerte))

fun degradadoAmbarVertical() = Brush.verticalGradient(listOf(Ambar, AmbarFuerte))

/** Paletas de acento que puede elegir el usuario en Ajustes > Apariencia: colorean el tema Y el icono del launcher a la vez (20 colores, como en Fossify). */
enum class PaletaAcento(val clave: String, val etiqueta: String, val base: Color, val fuerte: Color) {
    AMBAR("ambar", "Ámbar", Color(0xFFFFB74D), Color(0xFFFF8A3D)),
    MENTA("menta", "Menta", Color(0xFF57E6B4), Color(0xFF23C08D)),
    AZUL("azul", "Azul", Color(0xFF6FA8FF), Color(0xFF3D7EFF)),
    ROSA("rosa", "Rosa", Color(0xFFFF8FCB), Color(0xFFFF5FA8)),
    VIOLETA("violeta", "Violeta", Color(0xFFB98BFF), Color(0xFF8C5CFF)),
    ROJO("rojo", "Rojo", Color(0xFFF44336), Color(0xFFD32F2F)),
    PURPURA("purpura", "Púrpura", Color(0xFF9C27B0), Color(0xFF7B1FA2)),
    PURPURA_OSCURO("purpura_oscuro", "Púrpura oscuro", Color(0xFF673AB7), Color(0xFF512DA8)),
    INDIGO("indigo", "Índigo", Color(0xFF3F51B5), Color(0xFF303F9F)),
    CELESTE("celeste", "Celeste", Color(0xFF03A9F4), Color(0xFF0288D1)),
    CIAN("cian", "Cian", Color(0xFF00BCD4), Color(0xFF0097A7)),
    VERDE("verde", "Verde", Color(0xFF4CAF50), Color(0xFF388E3C)),
    VERDE_CLARO("verde_claro", "Verde claro", Color(0xFF8BC34A), Color(0xFF689F38)),
    LIMA("lima", "Lima", Color(0xFFCDDC39), Color(0xFFAFB42B)),
    AMARILLO("amarillo", "Amarillo", Color(0xFFFFEB3B), Color(0xFFFBC02D)),
    NARANJA("naranja", "Naranja", Color(0xFFFF9800), Color(0xFFF57C00)),
    NARANJA_OSCURO("naranja_oscuro", "Naranja oscuro", Color(0xFFFF5722), Color(0xFFE64A19)),
    MARRON("marron", "Marrón", Color(0xFF795548), Color(0xFF5D4037)),
    GRIS("gris", "Gris", Color(0xFF9E9E9E), Color(0xFF616161)),
    GRIS_AZULADO("gris_azulado", "Gris azulado", Color(0xFF607D8B), Color(0xFF455A64));

    companion object {
        fun desde(clave: String): PaletaAcento = entries.firstOrNull { it.clave == clave } ?: AMBAR
    }
}

fun aplicarPaletaAcento(paleta: PaletaAcento) {
    Ambar = paleta.base
    AmbarFuerte = paleta.fuerte
}

private val esquemaActual: androidx.compose.material3.ColorScheme
    @Composable get() = if (esOscuroActivo) {
        darkColorScheme(
            primary = Ambar,
            onPrimary = Obsidiana,
            primaryContainer = AmbarFuerte,
            onPrimaryContainer = Obsidiana,
            secondary = Menta,
            onSecondary = Obsidiana,
            background = Obsidiana,
            onBackground = TextoPrincipal,
            surface = Superficie,
            onSurface = TextoPrincipal,
            surfaceVariant = SuperficieAlta,
            onSurfaceVariant = TextoSecundario,
            outline = Borde,
            error = Peligro,
            onError = Obsidiana
        )
    } else {
        lightColorScheme(
            primary = Ambar,
            onPrimary = Obsidiana,
            primaryContainer = AmbarFuerte,
            onPrimaryContainer = Obsidiana,
            secondary = Menta,
            onSecondary = Obsidiana,
            background = Obsidiana,
            onBackground = TextoPrincipal,
            surface = Superficie,
            onSurface = TextoPrincipal,
            surfaceVariant = SuperficieAlta,
            onSurfaceVariant = TextoSecundario,
            outline = Borde,
            error = Peligro,
            onError = Obsidiana
        )
    }

private val formas = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

private val tipografia = Typography(
    displaySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.4).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        letterSpacing = 0.2.sp
    )
)

val EstiloMonoGrande = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Medium,
    fontSize = 26.sp,
    letterSpacing = 1.sp
)

val EstiloMono = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.5.sp
)

@Composable
fun PepoBovedaTheme(temaApp: String = "sistema", contenido: @Composable () -> Unit) {
    aplicarTema(temaApp, isSystemInDarkTheme())
    MaterialTheme(
        colorScheme = esquemaActual,
        typography = tipografia,
        shapes = formas,
        content = contenido
    )
}
