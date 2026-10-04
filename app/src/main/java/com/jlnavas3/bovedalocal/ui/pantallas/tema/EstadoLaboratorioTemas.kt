package com.jlnavas3.bovedalocal.ui.pantallas.tema

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.GestorPaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaParametrosDefaults
import com.jlnavas3.bovedalocal.ui.theme.crearGris
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.restringirLuminancia

/**
 * Gestor de estado reactivo para el Laboratorio de Temas.
 * Mantiene de manera desacoplada los parámetros para modo claro y oscuro,
 * permitiendo previsualizar y revertir cambios sin alterar el archivo principal de interfaz.
 */
class EstadoLaboratorioTemas(contexto: Context) {
    var modoOscuro by mutableStateOf(esOscuroActivo)

    val paramOscuro = PaletaSobriaParametrosDefaults.OSCURO
    val defOscuro = PaletaSobriaDefaults.OSCURA
    val paramClaro = PaletaSobriaParametrosDefaults.CLARO
    val defClaro = PaletaSobriaDefaults.CLARA

    val guardados = GestorPaletaSobria.cargarParametros(contexto)
    val guardadoOsc = guardados.oscuro
    val guardadoCla = guardados.claro
    val paletasGuardadas = GestorPaletaSobria.cargar(contexto)

    var lumFondoOscuro by mutableFloatStateOf(guardadoOsc?.lumFondo ?: paramOscuro.lumFondo)
    var lumTarjetaOscuro by mutableFloatStateOf(guardadoOsc?.lumTarjeta ?: paramOscuro.lumTarjeta)
    var lumCampoOscuro by mutableFloatStateOf(guardadoOsc?.lumCampo ?: paramOscuro.lumCampo)
    var lumBordeOscuro by mutableFloatStateOf(guardadoOsc?.lumBorde ?: paramOscuro.lumBorde)
    var lumTextoPrincipalOscuro by mutableFloatStateOf(guardadoOsc?.lumTextoPrincipal ?: paramOscuro.lumTextoPrincipal)
    var lumTextoSecundarioOscuro by mutableFloatStateOf(guardadoOsc?.lumTextoSecundario ?: paramOscuro.lumTextoSecundario)
    var colorAcentoOscuro by mutableStateOf(paletasGuardadas.first?.acento ?: defOscuro.acento)
    var tonoGlobalOscuro by mutableFloatStateOf(guardadoOsc?.tonoGlobal ?: paramOscuro.tonoGlobal)
    var saturacionTinteOscuro by mutableFloatStateOf(guardadoOsc?.saturacionTinte ?: paramOscuro.saturacionTinte)
    var tonoFondoOscuro by mutableFloatStateOf(guardadoOsc?.tonoFondo ?: paramOscuro.tonoGlobal)
    var tonoTarjetaOscuro by mutableFloatStateOf(guardadoOsc?.tonoTarjeta ?: paramOscuro.tonoGlobal)
    var tonoCampoOscuro by mutableFloatStateOf(guardadoOsc?.tonoCampo ?: paramOscuro.tonoGlobal)
    var tonoBordeOscuro by mutableFloatStateOf(guardadoOsc?.tonoBorde ?: paramOscuro.tonoGlobal)
    var tonoTextoPrincipalOscuro by mutableFloatStateOf(guardadoOsc?.tonoTextoPrincipal ?: paramOscuro.tonoGlobal)
    var tonoTextoSecundarioOscuro by mutableFloatStateOf(guardadoOsc?.tonoTextoSecundario ?: paramOscuro.tonoGlobal)
    var huePersonalizadoOscuro by mutableFloatStateOf(guardadoOsc?.acentoHue ?: paramOscuro.acentoHue)
    var satPersonalizadoOscuro by mutableFloatStateOf(guardadoOsc?.acentoSat ?: paramOscuro.acentoSat)
    var valPersonalizadoOscuro by mutableFloatStateOf(guardadoOsc?.acentoVal ?: paramOscuro.acentoVal)
    var alfaPersonalizadoOscuro by mutableFloatStateOf(guardadoOsc?.acentoAlfa ?: paramOscuro.acentoAlfa)
    var esPersonalizadoActivoOscuro by mutableStateOf(guardadoOsc?.esPersonalizado ?: false)
    var mostrarAjustePersonalizadoOscuro by mutableStateOf(guardadoOsc?.esPersonalizado ?: false)

    var lumFondoClaro by mutableFloatStateOf(guardadoCla?.lumFondo ?: paramClaro.lumFondo)
    var lumTarjetaClaro by mutableFloatStateOf(guardadoCla?.lumTarjeta ?: paramClaro.lumTarjeta)
    var lumCampoClaro by mutableFloatStateOf(guardadoCla?.lumCampo ?: paramClaro.lumCampo)
    var lumBordeClaro by mutableFloatStateOf(guardadoCla?.lumBorde ?: paramClaro.lumBorde)
    var lumTextoPrincipalClaro by mutableFloatStateOf(guardadoCla?.lumTextoPrincipal ?: paramClaro.lumTextoPrincipal)
    var lumTextoSecundarioClaro by mutableFloatStateOf(guardadoCla?.lumTextoSecundario ?: paramClaro.lumTextoSecundario)
    var colorAcentoClaro by mutableStateOf(paletasGuardadas.second?.acento ?: defClaro.acento)
    var tonoGlobalClaro by mutableFloatStateOf(guardadoCla?.tonoGlobal ?: paramClaro.tonoGlobal)
    var saturacionTinteClaro by mutableFloatStateOf(guardadoCla?.saturacionTinte ?: paramClaro.saturacionTinte)
    var tonoFondoClaro by mutableFloatStateOf(guardadoCla?.tonoFondo ?: paramClaro.tonoGlobal)
    var tonoTarjetaClaro by mutableFloatStateOf(guardadoCla?.tonoTarjeta ?: paramClaro.tonoGlobal)
    var tonoCampoClaro by mutableFloatStateOf(guardadoCla?.tonoCampo ?: paramClaro.tonoGlobal)
    var tonoBordeClaro by mutableFloatStateOf(guardadoCla?.tonoBorde ?: paramClaro.tonoGlobal)
    var tonoTextoPrincipalClaro by mutableFloatStateOf(guardadoCla?.tonoTextoPrincipal ?: paramClaro.tonoGlobal)
    var tonoTextoSecundarioClaro by mutableFloatStateOf(guardadoCla?.tonoTextoSecundario ?: paramClaro.tonoGlobal)
    var huePersonalizadoClaro by mutableFloatStateOf(guardadoCla?.acentoHue ?: paramClaro.acentoHue)
    var satPersonalizadoClaro by mutableFloatStateOf(guardadoCla?.acentoSat ?: paramClaro.acentoSat)
    var valPersonalizadoClaro by mutableFloatStateOf(guardadoCla?.acentoVal ?: paramClaro.acentoVal)
    var alfaPersonalizadoClaro by mutableFloatStateOf(guardadoCla?.acentoAlfa ?: paramClaro.acentoAlfa)
    var esPersonalizadoActivoClaro by mutableStateOf(guardadoCla?.esPersonalizado ?: false)
    var mostrarAjustePersonalizadoClaro by mutableStateOf(guardadoCla?.esPersonalizado ?: false)

    var unificarTonos by mutableStateOf(guardados.unificarTonos)
    var modificadoOscuro by mutableStateOf(false)
    var modificadoClaro by mutableStateOf(false)

    var lumFondo: Float
        get() = if (modoOscuro) lumFondoOscuro else lumFondoClaro
        set(v) { if (modoOscuro) lumFondoOscuro = v else lumFondoClaro = v }

    var lumTarjeta: Float
        get() = if (modoOscuro) lumTarjetaOscuro else lumTarjetaClaro
        set(v) { if (modoOscuro) lumTarjetaOscuro = v else lumTarjetaClaro = v }

    var lumCampo: Float
        get() = if (modoOscuro) lumCampoOscuro else lumCampoClaro
        set(v) { if (modoOscuro) lumCampoOscuro = v else lumCampoClaro = v }

    var lumBorde: Float
        get() = if (modoOscuro) lumBordeOscuro else lumBordeClaro
        set(v) { if (modoOscuro) lumBordeOscuro = v else lumBordeClaro = v }

    var lumTextoPrincipal: Float
        get() = if (modoOscuro) lumTextoPrincipalOscuro else lumTextoPrincipalClaro
        set(v) { if (modoOscuro) lumTextoPrincipalOscuro = v else lumTextoPrincipalClaro = v }

    var lumTextoSecundario: Float
        get() = if (modoOscuro) lumTextoSecundarioOscuro else lumTextoSecundarioClaro
        set(v) { if (modoOscuro) lumTextoSecundarioOscuro = v else lumTextoSecundarioClaro = v }

    var colorAcentoActual: Color
        get() = if (modoOscuro) colorAcentoOscuro else colorAcentoClaro
        set(v) { if (modoOscuro) colorAcentoOscuro = v else colorAcentoClaro = v }

    var tonoGlobal: Float
        get() = if (modoOscuro) tonoGlobalOscuro else tonoGlobalClaro
        set(v) { if (modoOscuro) tonoGlobalOscuro = v else tonoGlobalClaro = v }

    var saturacionTinte: Float
        get() = if (modoOscuro) saturacionTinteOscuro else saturacionTinteClaro
        set(v) { if (modoOscuro) saturacionTinteOscuro = v else saturacionTinteClaro = v }

    var tonoFondo: Float
        get() = if (modoOscuro) tonoFondoOscuro else tonoFondoClaro
        set(v) { if (modoOscuro) tonoFondoOscuro = v else tonoFondoClaro = v }

    var tonoTarjeta: Float
        get() = if (modoOscuro) tonoTarjetaOscuro else tonoTarjetaClaro
        set(v) { if (modoOscuro) tonoTarjetaOscuro = v else tonoTarjetaClaro = v }

    var tonoCampo: Float
        get() = if (modoOscuro) tonoCampoOscuro else tonoCampoClaro
        set(v) { if (modoOscuro) tonoCampoOscuro = v else tonoCampoClaro = v }

    var tonoBorde: Float
        get() = if (modoOscuro) tonoBordeOscuro else tonoBordeClaro
        set(v) { if (modoOscuro) tonoBordeOscuro = v else tonoBordeClaro = v }

    var tonoTextoPrincipal: Float
        get() = if (modoOscuro) tonoTextoPrincipalOscuro else tonoTextoPrincipalClaro
        set(v) { if (modoOscuro) tonoTextoPrincipalOscuro = v else tonoTextoPrincipalClaro = v }

    var tonoTextoSecundario: Float
        get() = if (modoOscuro) tonoTextoSecundarioOscuro else tonoTextoSecundarioClaro
        set(v) { if (modoOscuro) tonoTextoSecundarioOscuro = v else tonoTextoSecundarioClaro = v }

    var huePersonalizado: Float
        get() = if (modoOscuro) huePersonalizadoOscuro else huePersonalizadoClaro
        set(v) { if (modoOscuro) huePersonalizadoOscuro = v else huePersonalizadoClaro = v }

    var satPersonalizado: Float
        get() = if (modoOscuro) satPersonalizadoOscuro else satPersonalizadoClaro
        set(v) { if (modoOscuro) satPersonalizadoOscuro = v else satPersonalizadoClaro = v }

    var valPersonalizado: Float
        get() = if (modoOscuro) valPersonalizadoOscuro else valPersonalizadoClaro
        set(v) { if (modoOscuro) valPersonalizadoOscuro = v else valPersonalizadoClaro = v }

    var alfaPersonalizado: Float
        get() = if (modoOscuro) alfaPersonalizadoOscuro else alfaPersonalizadoClaro
        set(v) { if (modoOscuro) alfaPersonalizadoOscuro = v else alfaPersonalizadoClaro = v }

    var esPersonalizadoActivo: Boolean
        get() = if (modoOscuro) esPersonalizadoActivoOscuro else esPersonalizadoActivoClaro
        set(v) { if (modoOscuro) esPersonalizadoActivoOscuro = v else esPersonalizadoActivoClaro = v }

    var mostrarAjustePersonalizado: Boolean
        get() = if (modoOscuro) mostrarAjustePersonalizadoOscuro else mostrarAjustePersonalizadoClaro
        set(v) { if (modoOscuro) mostrarAjustePersonalizadoOscuro = v else mostrarAjustePersonalizadoClaro = v }

    fun marcarModificado() {
        if (modoOscuro) modificadoOscuro = true else modificadoClaro = true
    }

    fun construirPaleta(esOscuro: Boolean): PaletaSobria {
        val sat = if (esOscuro) saturacionTinteOscuro else saturacionTinteClaro
        val tonoG = if (esOscuro) tonoGlobalOscuro else tonoGlobalClaro
        val effFondo = if (unificarTonos) tonoG else (if (esOscuro) tonoFondoOscuro else tonoFondoClaro)
        val effTarjeta = if (unificarTonos) tonoG else (if (esOscuro) tonoTarjetaOscuro else tonoTarjetaClaro)
        val effCampo = if (unificarTonos) tonoG else (if (esOscuro) tonoCampoOscuro else tonoCampoClaro)
        val effBorde = if (unificarTonos) tonoG else (if (esOscuro) tonoBordeOscuro else tonoBordeClaro)
        val effTextoP = if (unificarTonos) tonoG else (if (esOscuro) tonoTextoPrincipalOscuro else tonoTextoPrincipalClaro)
        val effTextoS = if (unificarTonos) tonoG else (if (esOscuro) tonoTextoSecundarioOscuro else tonoTextoSecundarioClaro)

        fun calc(tono: Float, lum: Float, esSuperficie: Boolean): Color {
            val lumRestringida = restringirLuminancia(lum, esOscuro, esSuperficie)
            return if (sat <= 0.001f) {
                crearGris(lumRestringida)
            } else {
                Color.hsv(tono, sat.coerceIn(0f, 1f), lumRestringida)
            }
        }

        return PaletaSobria(
            esOscuro = esOscuro,
            fondo = calc(effFondo, if (esOscuro) lumFondoOscuro else lumFondoClaro, true),
            tarjeta = calc(effTarjeta, if (esOscuro) lumTarjetaOscuro else lumTarjetaClaro, true),
            campo = calc(effCampo, if (esOscuro) lumCampoOscuro else lumCampoClaro, true),
            borde = calc(effBorde, if (esOscuro) lumBordeOscuro else lumBordeClaro, true),
            textoPrincipal = calc(effTextoP, if (esOscuro) lumTextoPrincipalOscuro else lumTextoPrincipalClaro, false),
            textoSecundario = calc(effTextoS, if (esOscuro) lumTextoSecundarioOscuro else lumTextoSecundarioClaro, false),
            acento = if (esOscuro) colorAcentoOscuro else colorAcentoClaro
        )
    }

    fun restablecerValores(contexto: Context) {
        lumFondoOscuro = paramOscuro.lumFondo
        lumTarjetaOscuro = paramOscuro.lumTarjeta
        lumCampoOscuro = paramOscuro.lumCampo
        lumBordeOscuro = paramOscuro.lumBorde
        lumTextoPrincipalOscuro = paramOscuro.lumTextoPrincipal
        lumTextoSecundarioOscuro = paramOscuro.lumTextoSecundario
        tonoGlobalOscuro = paramOscuro.tonoGlobal
        saturacionTinteOscuro = paramOscuro.saturacionTinte
        tonoFondoOscuro = paramOscuro.tonoGlobal
        tonoTarjetaOscuro = paramOscuro.tonoGlobal
        tonoCampoOscuro = paramOscuro.tonoGlobal
        tonoBordeOscuro = paramOscuro.tonoGlobal
        tonoTextoPrincipalOscuro = paramOscuro.tonoGlobal
        tonoTextoSecundarioOscuro = paramOscuro.tonoGlobal
        colorAcentoOscuro = defOscuro.acento
        huePersonalizadoOscuro = paramOscuro.acentoHue
        satPersonalizadoOscuro = paramOscuro.acentoSat
        valPersonalizadoOscuro = paramOscuro.acentoVal
        alfaPersonalizadoOscuro = paramOscuro.acentoAlfa
        esPersonalizadoActivoOscuro = false
        mostrarAjustePersonalizadoOscuro = false
        modificadoOscuro = false

        lumFondoClaro = paramClaro.lumFondo
        lumTarjetaClaro = paramClaro.lumTarjeta
        lumCampoClaro = paramClaro.lumCampo
        lumBordeClaro = paramClaro.lumBorde
        lumTextoPrincipalClaro = paramClaro.lumTextoPrincipal
        lumTextoSecundarioClaro = paramClaro.lumTextoSecundario
        tonoGlobalClaro = paramClaro.tonoGlobal
        saturacionTinteClaro = paramClaro.saturacionTinte
        tonoFondoClaro = paramClaro.tonoGlobal
        tonoTarjetaClaro = paramClaro.tonoGlobal
        tonoCampoClaro = paramClaro.tonoGlobal
        tonoBordeClaro = paramClaro.tonoGlobal
        tonoTextoPrincipalClaro = paramClaro.tonoGlobal
        tonoTextoSecundarioClaro = paramClaro.tonoGlobal
        colorAcentoClaro = defClaro.acento
        huePersonalizadoClaro = paramClaro.acentoHue
        satPersonalizadoClaro = paramClaro.acentoSat
        valPersonalizadoClaro = paramClaro.acentoVal
        alfaPersonalizadoClaro = paramClaro.acentoAlfa
        esPersonalizadoActivoClaro = false
        mostrarAjustePersonalizadoClaro = false
        modificadoClaro = false

        unificarTonos = true

        GestorPaletaSobria.restablecer(contexto)
    }
}
