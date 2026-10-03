package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.aHex

/** Estado reactivo global para paletas sobrias guardadas */
var paletaSobriaGuardadaOscura by mutableStateOf<PaletaSobria?>(null)
var paletaSobriaGuardadaClara by mutableStateOf<PaletaSobria?>(null)

/**
 * Representa una configuración armónica de colores basada en la filosofía 90/10:
 * 90% escala neutra de grises por capas de elevación + 10% acento esencial.
 */
data class PaletaSobria(
    val esOscuro: Boolean,
    val fondo: Color,
    val tarjeta: Color,
    val campo: Color,
    val borde: Color,
    val textoPrincipal: Color,
    val textoSecundario: Color,
    val acento: Color
) {
    /**
     * Exporta la paleta en formato JSON para que el usuario pueda copiarla fácilmente
     * y compartirla en el chat.
     */
    fun aJson(): String {
        return """
        {
          "modo": "${if (esOscuro) "oscuro" else "claro"}",
          "fondo": "${fondo.aHexConAlfa()}",
          "tarjeta": "${tarjeta.aHexConAlfa()}",
          "campo": "${campo.aHexConAlfa()}",
          "borde": "${borde.aHexConAlfa()}",
          "textoPrincipal": "${textoPrincipal.aHexConAlfa()}",
          "textoSecundario": "${textoSecundario.aHexConAlfa()}",
          "acento": "${acento.aHexConAlfa()}"
        }
        """.trimIndent()
    }

    /**
     * Exporta la definición lista en Kotlin para aplicarse como preset permanente.
     */
    fun aCodigoKotlin(): String {
        val sufijo = if (esOscuro) "Oscura" else "Clara"
        return """
        val paletaPersonalizada$sufijo = PaletaSobria(
            esOscuro = $esOscuro,
            fondo = Color(0x${fondo.aHexConAlfa().removePrefix("#")}),
            tarjeta = Color(0x${tarjeta.aHexConAlfa().removePrefix("#")}),
            campo = Color(0x${campo.aHexConAlfa().removePrefix("#")}),
            borde = Color(0x${borde.aHexConAlfa().removePrefix("#")}),
            textoPrincipal = Color(0x${textoPrincipal.aHexConAlfa().removePrefix("#")}),
            textoSecundario = Color(0x${textoSecundario.aHexConAlfa().removePrefix("#")}),
            acento = Color(0x${acento.aHexConAlfa().removePrefix("#")})
        )
        """.trimIndent()
    }
}

object PaletaSobriaDefaults {
    val OSCURA = PaletaSobria(
        esOscuro = true,
        fondo = Color(0xFF09090A),
        tarjeta = Color(0xFF1A1C1E),
        campo = Color(0xFF303337),
        borde = Color(0xFF242629),
        textoPrincipal = Color(0xFF8A929E),
        textoSecundario = Color(0xFF7A828C),
        acento = Color(0xFF959AA7)
    )

    val CLARA = PaletaSobria(
        esOscuro = false,
        fondo = Color(0xFFB2BDCC),
        tarjeta = Color(0xFFCAD6E7),
        campo = Color(0xFFDEECFF),
        borde = Color(0xFFB2BDCC),
        textoPrincipal = Color(0xFF43484D),
        textoSecundario = Color(0xFF646A73),
        acento = Color(0xFF323842)
    )
}

data class ParametrosLaboratorioPreset(
    val tonoGlobal: Float,
    val saturacionTinte: Float,
    val lumFondo: Float,
    val lumTarjeta: Float,
    val lumCampo: Float,
    val lumBorde: Float,
    val lumTextoPrincipal: Float,
    val lumTextoSecundario: Float,
    val acentoHue: Float,
    val acentoSat: Float,
    val acentoVal: Float,
    val acentoAlfa: Float
)

object PaletaSobriaParametrosDefaults {
    val OSCURO = ParametrosLaboratorioPreset(
        tonoGlobal = 215f,
        saturacionTinte = 0.12f,
        lumFondo = 0.00f,
        lumTarjeta = 0.11f,
        lumCampo = 0.21f,
        lumBorde = 0.16f,
        lumTextoPrincipal = 0.62f,
        lumTextoSecundario = 0.53f,
        acentoHue = 223f,
        acentoSat = 0.10f,
        acentoVal = 0.65f,
        acentoAlfa = 1.0f
    )

    val CLARO = ParametrosLaboratorioPreset(
        tonoGlobal = 215f,
        saturacionTinte = 0.12f,
        lumFondo = 0.80f,
        lumTarjeta = 0.90f,
        lumCampo = 1.00f,
        lumBorde = 0.80f,
        lumTextoPrincipal = 0.30f,
        lumTextoSecundario = 0.45f,
        acentoHue = 217f,
        acentoSat = 0.24f,
        acentoVal = 0.25f,
        acentoAlfa = 1.0f
    )
}

/**
 * Calcula un gris puro a partir de un valor de luminosidad entre 0.0f y 1.0f.
 */
fun crearGris(luminosidad: Float): Color {
    val l = luminosidad.coerceIn(0f, 1f)
    return Color(red = l, green = l, blue = l, alpha = 1f)
}

/**
 * Restringe la luminosidad a los límites estrictos de legibilidad según el modo:
 * - Modo Claro: Superficies claras (0.80..1.00), textos oscuros (0.05..0.45).
 * - Modo Oscuro: Superficies oscuras (0.04..0.22), textos claros (0.55..0.98).
 */
fun restringirLuminancia(
    valor: Float,
    esOscuro: Boolean,
    esSuperficie: Boolean
): Float {
    return if (esOscuro) {
        if (esSuperficie) valor.coerceIn(0.04f, 0.22f) else valor.coerceIn(0.55f, 0.98f)
    } else {
        if (esSuperficie) valor.coerceIn(0.80f, 1.00f) else valor.coerceIn(0.05f, 0.45f)
    }
}

object GestorPaletaSobria {
    private const val PREFS_NAME = "boveda_paleta_sobria"

    fun restablecer(context: android.content.Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        paletaSobriaGuardadaOscura = null
        paletaSobriaGuardadaClara = null
    }

    fun guardar(context: android.content.Context, paletaOscura: PaletaSobria?, paletaClara: PaletaSobria?) {
        val prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
        val ed = prefs.edit()
        if (paletaOscura != null) {
            paletaSobriaGuardadaOscura = paletaOscura
            ed.putString("oscuro_fondo", paletaOscura.fondo.aHexConAlfa())
            ed.putString("oscuro_tarjeta", paletaOscura.tarjeta.aHexConAlfa())
            ed.putString("oscuro_campo", paletaOscura.campo.aHexConAlfa())
            ed.putString("oscuro_borde", paletaOscura.borde.aHexConAlfa())
            ed.putString("oscuro_texto_principal", paletaOscura.textoPrincipal.aHexConAlfa())
            ed.putString("oscuro_texto_secundario", paletaOscura.textoSecundario.aHexConAlfa())
            ed.putString("oscuro_acento", paletaOscura.acento.aHexConAlfa())
            ed.putBoolean("tiene_guardado_oscuro", true)
        }
        if (paletaClara != null) {
            paletaSobriaGuardadaClara = paletaClara
            ed.putString("claro_fondo", paletaClara.fondo.aHexConAlfa())
            ed.putString("claro_tarjeta", paletaClara.tarjeta.aHexConAlfa())
            ed.putString("claro_campo", paletaClara.campo.aHexConAlfa())
            ed.putString("claro_borde", paletaClara.borde.aHexConAlfa())
            ed.putString("claro_texto_principal", paletaClara.textoPrincipal.aHexConAlfa())
            ed.putString("claro_texto_secundario", paletaClara.textoSecundario.aHexConAlfa())
            ed.putString("claro_acento", paletaClara.acento.aHexConAlfa())
            ed.putBoolean("tiene_guardado_claro", true)
        }
        ed.apply()
    }

    fun cargar(context: android.content.Context): Pair<PaletaSobria?, PaletaSobria?> {
        val prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
        val oscura = if (prefs.getBoolean("tiene_guardado_oscuro", false)) {
            PaletaSobria(
                esOscuro = true,
                fondo = parsearColorHex(prefs.getString("oscuro_fondo", null), PaletaSobriaDefaults.OSCURA.fondo),
                tarjeta = parsearColorHex(prefs.getString("oscuro_tarjeta", null), PaletaSobriaDefaults.OSCURA.tarjeta),
                campo = parsearColorHex(prefs.getString("oscuro_campo", null), PaletaSobriaDefaults.OSCURA.campo),
                borde = parsearColorHex(prefs.getString("oscuro_borde", null), PaletaSobriaDefaults.OSCURA.borde),
                textoPrincipal = parsearColorHex(prefs.getString("oscuro_texto_principal", null), PaletaSobriaDefaults.OSCURA.textoPrincipal),
                textoSecundario = parsearColorHex(prefs.getString("oscuro_texto_secundario", null), PaletaSobriaDefaults.OSCURA.textoSecundario),
                acento = parsearColorHex(prefs.getString("oscuro_acento", null), PaletaSobriaDefaults.OSCURA.acento)
            )
        } else null

        val clara = if (prefs.getBoolean("tiene_guardado_claro", false)) {
            PaletaSobria(
                esOscuro = false,
                fondo = parsearColorHex(prefs.getString("claro_fondo", null), PaletaSobriaDefaults.CLARA.fondo),
                tarjeta = parsearColorHex(prefs.getString("claro_tarjeta", null), PaletaSobriaDefaults.CLARA.tarjeta),
                campo = parsearColorHex(prefs.getString("claro_campo", null), PaletaSobriaDefaults.CLARA.campo),
                borde = parsearColorHex(prefs.getString("claro_borde", null), PaletaSobriaDefaults.CLARA.borde),
                textoPrincipal = parsearColorHex(prefs.getString("claro_texto_principal", null), PaletaSobriaDefaults.CLARA.textoPrincipal),
                textoSecundario = parsearColorHex(prefs.getString("claro_texto_secundario", null), PaletaSobriaDefaults.CLARA.textoSecundario),
                acento = parsearColorHex(prefs.getString("claro_acento", null), PaletaSobriaDefaults.CLARA.acento)
            )
        } else null

        return oscura to clara
    }

    private fun parsearColorHex(hex: String?, fallback: Color): Color {
        if (hex.isNullOrBlank()) return fallback
        return try {
            val limpio = hex.removePrefix("#").trim()
            val uLongVal = limpio.toLong(16)
            if (limpio.length <= 6) {
                Color(uLongVal or 0xFF000000)
            } else {
                Color(uLongVal)
            }
        } catch (_: Exception) {
            fallback
        }
    }
}

