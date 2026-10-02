package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.aHex

/** Estado reactivo global para previsualizar y ajustar la paleta sobria en tiempo real */
var paletaSobriaEnVivo by mutableStateOf<PaletaSobria?>(null)

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
          "fondo": "${fondo.aHex()}",
          "tarjeta": "${tarjeta.aHex()}",
          "campo": "${campo.aHex()}",
          "borde": "${borde.aHex()}",
          "textoPrincipal": "${textoPrincipal.aHex()}",
          "textoSecundario": "${textoSecundario.aHex()}",
          "acento": "${acento.aHex()}"
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
            fondo = Color(0x${fondo.aHex().removePrefix("#")}),
            tarjeta = Color(0x${tarjeta.aHex().removePrefix("#")}),
            campo = Color(0x${campo.aHex().removePrefix("#")}),
            borde = Color(0x${borde.aHex().removePrefix("#")}),
            textoPrincipal = Color(0x${textoPrincipal.aHex().removePrefix("#")}),
            textoSecundario = Color(0x${textoSecundario.aHex().removePrefix("#")}),
            acento = Color(0x${acento.aHex().removePrefix("#")})
        )
        """.trimIndent()
    }
}

object PaletaSobriaDefaults {
    val OSCURA = PaletaSobria(
        esOscuro = true,
        fondo = Color(0xFF101012),
        tarjeta = Color(0xFF1A1A1E),
        campo = Color(0xFF26262B),
        borde = Color(0xFF33333D),
        textoPrincipal = Color(0xFFF3F3F6),
        textoSecundario = Color(0xFF9A9AA4),
        acento = Color(0xFFE5A93C)
    )

    val CLARA = PaletaSobria(
        esOscuro = false,
        fondo = Color(0xFFF5F6F9),
        tarjeta = Color(0xFFFFFFFF),
        campo = Color(0xFFECEEF2),
        borde = Color(0xFFD9DCE3),
        textoPrincipal = Color(0xFF141519),
        textoSecundario = Color(0xFF5E636E),
        acento = Color(0xFFD48B12)
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
