package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp

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

fun aplicarPersonalizacionTipografia(ajustes: AjustesApp) {
    escalaTextoBase = ajustes.escalaTexto
    pesoTextoBase = ajustes.pesoTexto
    cursivaTextoBase = ajustes.cursivaTexto
    espaciadoLetrasSpBase = ajustes.espaciadoLetrasSp
    interlineadoFactorBase = ajustes.interlineadoFactor
    familiaFuenteBase = ajustes.familiaFuente
}

val TipografiaDinamica: Typography
    get() = Typography(
        displaySmall = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.Bold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (34 * EscalaTexto).sp,
            lineHeight = (42 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = ((-0.5f) + EspaciadoLetrasSp).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.Bold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (28 * EscalaTexto).sp,
            lineHeight = (34 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = ((-0.4f) + EspaciadoLetrasSp).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.Bold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (22 * EscalaTexto).sp,
            lineHeight = (28 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        titleLarge = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.Bold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (20 * EscalaTexto).sp,
            lineHeight = (26 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        titleMedium = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.SemiBold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (18 * EscalaTexto).sp,
            lineHeight = (24 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = EspaciadoLetrasSp.sp
        ),
        titleSmall = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = if (PesoTextoClave == "normal") FontWeight.SemiBold else PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (15 * EscalaTexto).sp,
            lineHeight = (20 * EscalaTexto * InterlineadoFactor).sp,
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
            lineHeight = (20 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = (0.2f + EspaciadoLetrasSp).sp
        ),
        labelMedium = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (12 * EscalaTexto).sp,
            lineHeight = (16 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = (0.2f + EspaciadoLetrasSp).sp
        ),
        labelSmall = TextStyle(
            fontFamily = FamiliaFuenteActual,
            fontWeight = PesoTextoActual,
            fontStyle = EstiloFuenteActual,
            fontSize = (10 * EscalaTexto).sp,
            lineHeight = (14 * EscalaTexto * InterlineadoFactor).sp,
            letterSpacing = (0.2f + EspaciadoLetrasSp).sp
        )
    )

val EstiloMonoGrande: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = (26 * EscalaTexto).sp,
        lineHeight = (32 * EscalaTexto * InterlineadoFactor).sp,
        letterSpacing = (1f + EspaciadoLetrasSp).sp
    )

val EstiloMono: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = PesoTextoActual,
        fontSize = (16 * EscalaTexto).sp,
        lineHeight = (22 * EscalaTexto * InterlineadoFactor).sp,
        letterSpacing = (0.5f + EspaciadoLetrasSp).sp
    )

