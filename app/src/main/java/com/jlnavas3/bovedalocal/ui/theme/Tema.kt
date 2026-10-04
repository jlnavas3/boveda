@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.jlnavas3.bovedalocal.data.AjustesApp

// -------------------------------------------------------------------------------------------------
// Tokens Dinámicos de Alumbrado de Navegación en Ajustes (Snapshot State en tiempo real)
// -------------------------------------------------------------------------------------------------

private var alumbradoActivoBase by mutableStateOf(true)
private var alumbradoIntensidadBase by mutableStateOf(0.5f)
private var alumbradoRepeticionesBase by mutableStateOf(2)
private var alumbradoDuracionMsBase by mutableStateOf(600)

var AlumbradoActivo: Boolean
    get() = alumbradoActivoBase
    set(valor) { alumbradoActivoBase = valor }

var AlumbradoIntensidad: Float
    get() = alumbradoIntensidadBase
    set(valor) { alumbradoIntensidadBase = valor }

var AlumbradoRepeticiones: Int
    get() = alumbradoRepeticionesBase
    set(valor) { alumbradoRepeticionesBase = valor }

var AlumbradoDuracionMs: Int
    get() = alumbradoDuracionMsBase
    set(valor) { alumbradoDuracionMsBase = valor }


private val esquemaActual: ColorScheme
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

@Composable
fun BovedaTheme(temaApp: String = "sistema", contenido: @Composable () -> Unit) {
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
                if (Build.VERSION.SDK_INT < 35) {
                    @Suppress("DEPRECATION")
                    ventana.statusBarColor = Obsidiana.toArgb()
                    @Suppress("DEPRECATION")
                    ventana.navigationBarColor = Obsidiana.toArgb()
                }
            }
        }
    }

    val usarDinamico = colorDinamicoSistemaBase && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val esquema = if (usarDinamico) {
        val dinamico = if (esOscuroActivo) dynamicDarkColorScheme(contexto) else dynamicLightColorScheme(contexto)
        if (colorDinamicoMonet != dinamico.primary || colorDinamicoFuerteMonet != dinamico.primaryContainer) {
            colorDinamicoMonet = dinamico.primary
            colorDinamicoFuerteMonet = dinamico.primaryContainer
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
        if (colorDinamicoMonet != null || colorDinamicoFuerteMonet != null) {
            colorDinamicoMonet = null
            colorDinamicoFuerteMonet = null
        }
        esquemaActual
    }

    MaterialTheme(
        colorScheme = esquema,
        typography = TipografiaDinamica,
        shapes = FormasDinamicas,
        content = {
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.foundation.LocalOverscrollConfiguration provides androidx.compose.foundation.OverscrollConfiguration(
                    glowColor = ColorAcento,
                    drawPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                )
            ) {
                contenido()
            }
        }
    )
}

private tailrec fun Context.encontrarActividad(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.encontrarActividad()
    else -> null
}
