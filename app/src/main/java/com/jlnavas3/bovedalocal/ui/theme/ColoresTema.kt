package com.jlnavas3.bovedalocal.ui.theme

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults

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
    fondo = PaletaSobriaDefaults.OSCURA.fondo,
    superficie = PaletaSobriaDefaults.OSCURA.tarjeta,
    superficieAlta = PaletaSobriaDefaults.OSCURA.campo,
    borde = PaletaSobriaDefaults.OSCURA.borde,
    textoPrincipal = PaletaSobriaDefaults.OSCURA.textoPrincipal,
    textoSecundario = PaletaSobriaDefaults.OSCURA.textoSecundario,
    menta = Color(0xFF57E6B4),
    peligro = Color(0xFFFF5D5D)
)

internal val paletaClara = PaletaBase(
    fondo = PaletaSobriaDefaults.CLARA.fondo,
    superficie = PaletaSobriaDefaults.CLARA.tarjeta,
    superficieAlta = PaletaSobriaDefaults.CLARA.campo,
    borde = PaletaSobriaDefaults.CLARA.borde,
    textoPrincipal = PaletaSobriaDefaults.CLARA.textoPrincipal,
    textoSecundario = PaletaSobriaDefaults.CLARA.textoSecundario,
    menta = Color(0xFF1F9C74),
    peligro = Color(0xFFD23F3F)
)

internal var paletaActiva by mutableStateOf(paletaOscura)
var esOscuroActivo by mutableStateOf(true)

val paletaSobriaEfectiva: PaletaSobria
    get() = if (esOscuroActivo) (paletaSobriaGuardadaOscura ?: PaletaSobriaDefaults.OSCURA) else (paletaSobriaGuardadaClara ?: PaletaSobriaDefaults.CLARA)

val Obsidiana: Color get() = paletaSobriaEfectiva.fondo
val Superficie: Color get() = paletaSobriaEfectiva.tarjeta
val SuperficieAlta: Color get() = paletaSobriaEfectiva.campo
val Borde: Color get() = paletaSobriaEfectiva.borde
val Menta: Color get() = paletaActiva.menta
val Peligro: Color get() = paletaActiva.peligro
val Advertencia: Color get() = if (esOscuroActivo) Color(0xFFFBBF24) else Color(0xFFD97706)
val TextoPrincipal: Color get() = paletaSobriaEfectiva.textoPrincipal
val TextoSecundario: Color get() = paletaSobriaEfectiva.textoSecundario


internal var paletaAcentoActiva by mutableStateOf<PaletaAcento?>(null)
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
        if (colorAcentoManual != null) return colorAcentoManual!!
        if (paletaAcentoActiva != null) return paletaAcentoActiva!!.base
        val guardada = if (esOscuroActivo) paletaSobriaGuardadaOscura else paletaSobriaGuardadaClara
        if (guardada != null) return guardada.acento
        return if (esOscuroActivo) PaletaSobriaDefaults.OSCURA.acento else PaletaSobriaDefaults.CLARA.acento
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
        if (colorAcentoFuerteManual != null) return colorAcentoFuerteManual!!
        if (paletaAcentoActiva != null) return paletaAcentoActiva!!.fuerte
        val guardada = if (esOscuroActivo) paletaSobriaGuardadaOscura else paletaSobriaGuardadaClara
        if (guardada != null) return guardada.acento
        return if (esOscuroActivo) PaletaSobriaDefaults.OSCURA.acento else PaletaSobriaDefaults.CLARA.acento
    }
    set(valor) { colorAcentoFuerteManual = valor }

val ColorSobreAcento: Color get() = colorContraste(ColorAcento)

var ColorIconosInternos: Color
    get() = colorIconosBase ?: if (esOscuroActivo) (paletaSobriaGuardadaOscura?.textoSecundario ?: Color(0xFFD6DAE2)) else (paletaSobriaGuardadaClara?.textoSecundario ?: Color(0xFF1E232E))
    set(valor) { colorIconosBase = valor }

var ColorTitulos: Color
    get() = colorTitulosBase ?: if (esOscuroActivo) (paletaSobriaGuardadaOscura?.textoPrincipal ?: Color(0xFFF3F4F8)) else (paletaSobriaGuardadaClara?.textoPrincipal ?: Color(0xFF11141A))
    set(valor) { colorTitulosBase = valor }

var ColorTarjetas: Color
    get() = colorTarjetasBase ?: if (esOscuroActivo) (paletaSobriaGuardadaOscura?.tarjeta ?: paletaOscura.superficieAlta) else (paletaSobriaGuardadaClara?.tarjeta ?: paletaClara.superficie)
    set(valor) { colorTarjetasBase = valor }

val ColorSobreTarjetas: Color get() = colorContraste(ColorTarjetas)

// Compatibilidad heredada con código previo (se recomienda usar ColorAcento)
@Deprecated("Usar ColorAcento en su lugar", ReplaceWith("ColorAcento"))
var Ambar: Color
    get() = ColorAcento
    set(valor) { ColorAcento = valor }

@Deprecated("Usar ColorAcentoFuerte en su lugar", ReplaceWith("ColorAcentoFuerte"))
var AmbarFuerte: Color
    get() = ColorAcentoFuerte
    set(valor) { ColorAcentoFuerte = valor }

val DegradadoAcento: Brush get() = Brush.horizontalGradient(listOf(ColorAcento, ColorAcentoFuerte))
fun degradadoAcentoVertical() = Brush.verticalGradient(listOf(ColorAcento, ColorAcentoFuerte))

@Deprecated("Usar DegradadoAcento en su lugar", ReplaceWith("DegradadoAcento"))
val DegradadoAmbar: Brush get() = DegradadoAcento
internal var colorDinamicoSistemaBase by mutableStateOf(false)

var ColorDinamicoSistema: Boolean
    get() = colorDinamicoSistemaBase
    set(valor) { colorDinamicoSistemaBase = valor }

val DegradadoAcentoVertical: Brush get() = Brush.verticalGradient(listOf(ColorAcento, ColorAcentoFuerte))

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

// Colores de Identificadores Jerárquicos de Ajustes (01..06)
private var colorIdSeguridadBase by mutableStateOf(Color(0xFF3F51B5))
private var colorIdAparienciaBase by mutableStateOf(Color(0xFF8E24AA))
private var colorIdListaBase by mutableStateOf(Color(0xFF00897B))
private var colorIdHerramientasBase by mutableStateOf(Color(0xFFFB8C00))
private var colorIdCopiasBase by mutableStateOf(Color(0xFF1E88E5))
private var colorIdSistemaBase by mutableStateOf(Color(0xFF607D8B))

var ColorIdSeguridad: Color
    get() = colorIdSeguridadBase
    set(valor) { colorIdSeguridadBase = valor }

var ColorIdApariencia: Color
    get() = colorIdAparienciaBase
    set(valor) { colorIdAparienciaBase = valor }

var ColorIdLista: Color
    get() = colorIdListaBase
    set(valor) { colorIdListaBase = valor }

var ColorIdHerramientas: Color
    get() = colorIdHerramientasBase
    set(valor) { colorIdHerramientasBase = valor }

var ColorIdCopias: Color
    get() = colorIdCopiasBase
    set(valor) { colorIdCopiasBase = valor }

var ColorIdSistema: Color
    get() = colorIdSistemaBase
    set(valor) { colorIdSistemaBase = valor }

