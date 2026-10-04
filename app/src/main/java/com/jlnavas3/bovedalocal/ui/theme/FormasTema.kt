package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp

// -------------------------------------------------------------------------------------------------
// Tokens Dinámicos de Bordes, Formas y Espaciado (Snapshot State en tiempo real)
// -------------------------------------------------------------------------------------------------

private var curvaturaEsquinasDpBase by mutableStateOf(6f)
private var grosorBordeDpBase by mutableStateOf(0.8f)
private var estiloBordeBase by mutableStateOf("ninguno")
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
    else -> Borde
}

/** Borde externo para el contenedor de menús desplegables (DropdownMenu), adaptativo y sutil. */
val ColorBordeDropdown: Color get() = when (estiloBordeBase) {
    "acento" -> ColorAcento.copy(alpha = if (esOscuroActivo) 0.45f else 0.55f)
    "marcado" -> if (esOscuroActivo) Color(0xFF4E4D53) else Color(0xFFB8BFCE)
    "ninguno" -> if (esOscuroActivo) Color(0xFF2D2C30) else Color(0xFFE2E5EC)
    else -> if (esOscuroActivo) Color(0xFF38373C) else Color(0xFFD4D8E2)
}

/** Separador o divisor sutil entre las opciones de un menú desplegable (DropdownMenu). */
val ColorSeparadorDropdown: Color get() = if (esOscuroActivo) Color(0xFF2D2C30) else Color(0xFFE0E3EB)

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

val FormasDinamicas: Shapes
    get() = Shapes(
        extraSmall = FormaTarjeta,
        small = FormaPequena,
        medium = FormaCampo,
        large = FormaTarjeta,
        extraLarge = RoundedCornerShape((CurvaturaEsquinas * 1.35f).coerceAtLeast(8.dp))
    )

