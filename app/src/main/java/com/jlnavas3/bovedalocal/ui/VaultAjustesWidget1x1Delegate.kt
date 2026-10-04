package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido

/**
 * Sub-delegado especializado en calibración visual, dimensiones y comportamiento del Widget 1x1.
 */
interface VaultAjustesWidget1x1Delegate {
    val repositorio: VaultRepository
    fun obtenerApp(): Application

    fun actualizarWidget1x1(modificar: (AjustesApp) -> AjustesApp) {
        repositorio.ajustes.actualizar(modificar)
        WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1DicewarePalabras(palabras: Int) =
        repositorio.ajustes.actualizar { it.copy(widget1x1DicewarePalabras = palabras) }

    fun ajustarWidget1x1DicewareSeparador(separador: String) =
        repositorio.ajustes.actualizar { it.copy(widget1x1DicewareSeparador = separador) }

    fun ajustarWidget1x1Haptica(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(widget1x1Haptica = activo) }

    fun ajustarWidget1x1HapticaIntensidad(intensidad: Float) =
        repositorio.ajustes.actualizar { it.copy(widget1x1HapticaIntensidad = intensidad.coerceIn(0.01f, 1.0f)) }

    fun ajustarWidget1x1Modo(modo: String) =
        repositorio.ajustes.actualizar { it.copy(widget1x1Modo = modo) }

    fun ajustarWidget1x1Longitud(longitud: Int) =
        repositorio.ajustes.actualizar { it.copy(widget1x1Longitud = longitud) }

    fun ajustarWidget1x1Patron(patron: String) =
        repositorio.ajustes.actualizar { it.copy(widget1x1Patron = patron) }

    fun ajustarWidget1x1Simbolos(simbolos: String) =
        repositorio.ajustes.actualizar { it.copy(widget1x1Simbolos = simbolos) }

    fun ajustarWidget1x1CopiarPortapapeles(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(widget1x1CopiarPortapapeles = activo) }

    fun ajustarWidget1x1MostrarToast(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(widget1x1MostrarToast = activo) }

    fun ajustarWidget1x1GrosorBorde(grosor: Float) =
        actualizarWidget1x1 { it.copy(widget1x1GrosorBordeDp = grosor) }

    fun ajustarWidget1x1CurvaturaEsquinas(curvatura: Float) =
        actualizarWidget1x1 { it.copy(widget1x1CurvaturaEsquinasDp = curvatura) }

    fun ajustarWidget1x1TransparenciaFondo(transparencia: Float) =
        actualizarWidget1x1 { it.copy(widget1x1TransparenciaFondo = transparencia) }

    fun ajustarWidget1x1Tamano(tamano: Float) =
        actualizarWidget1x1 {
            it.copy(
                widget1x1TamanoDp = tamano,
                widget1x1AnchoDp = tamano,
                widget1x1AltoDp = tamano
            )
        }

    fun ajustarWidget1x1Ancho(ancho: Float) =
        actualizarWidget1x1 {
            if (it.widget1x1BloquearProporcion) {
                it.copy(widget1x1AnchoDp = ancho, widget1x1AltoDp = ancho, widget1x1TamanoDp = ancho)
            } else {
                it.copy(widget1x1AnchoDp = ancho, widget1x1TamanoDp = ancho)
            }
        }

    fun ajustarWidget1x1Alto(alto: Float) =
        actualizarWidget1x1 {
            if (it.widget1x1BloquearProporcion) {
                it.copy(widget1x1AnchoDp = alto, widget1x1AltoDp = alto, widget1x1TamanoDp = alto)
            } else {
                it.copy(widget1x1AltoDp = alto)
            }
        }

    fun ajustarWidget1x1BloquearProporcion(bloquear: Boolean) =
        actualizarWidget1x1 {
            if (bloquear) {
                it.copy(widget1x1BloquearProporcion = true, widget1x1AltoDp = it.widget1x1AnchoDp)
            } else {
                it.copy(widget1x1BloquearProporcion = false)
            }
        }

    fun ajustarWidget1x1OffsetX(offset: Float) =
        actualizarWidget1x1 { it.copy(widget1x1OffsetX = offset) }

    fun ajustarWidget1x1OffsetY(offset: Float) =
        actualizarWidget1x1 { it.copy(widget1x1OffsetY = offset) }

    fun ajustarWidget1x1PresetTamano(dp: Float) =
        actualizarWidget1x1 {
            it.copy(
                widget1x1AnchoDp = dp,
                widget1x1AltoDp = dp,
                widget1x1TamanoDp = dp
            )
        }

    fun ajustarWidget1x1Alineamiento(alineamiento: String) =
        actualizarWidget1x1 { it.copy(widget1x1Alineamiento = alineamiento) }

    fun ajustarWidget1x1ColorBorde(colorHex: String) =
        actualizarWidget1x1 { it.copy(widget1x1ColorBorde = colorHex) }

    fun ajustarWidget1x1ColorIcono(colorHex: String) =
        actualizarWidget1x1 { it.copy(widget1x1ColorIcono = colorHex) }

    fun ajustarWidget1x1ColorFondo(colorHex: String) =
        actualizarWidget1x1 { it.copy(widget1x1ColorFondo = colorHex) }

    fun ajustarWidget1x1VidrioEsmerilado(activo: Boolean) =
        actualizarWidget1x1 { it.copy(widget1x1VidrioEsmerilado = activo) }

    fun ajustarWidget1x1EsmeriladoIntensidad(intensidad: Float) =
        actualizarWidget1x1 { it.copy(widget1x1EsmeriladoIntensidad = intensidad) }

    fun ajustarWidget1x1EsmeriladoLuz(luz: Float) =
        actualizarWidget1x1 { it.copy(widget1x1EsmeriladoLuz = luz) }

    fun aplicarPresetEstiloWidget1x1(
        curvaturaDp: Float,
        grosorDp: Float,
        transparenciaFondo: Float,
        colorFondo: String,
        colorBorde: String,
        colorIcono: String,
        vidrioEsmerilado: Boolean = false,
        esmeriladoIntensidad: Float = 0.60f,
        bloquearProporcion: Boolean? = null
    ) = actualizarWidget1x1 {
        it.copy(
            widget1x1CurvaturaEsquinasDp = curvaturaDp,
            widget1x1GrosorBordeDp = grosorDp,
            widget1x1TransparenciaFondo = transparenciaFondo,
            widget1x1ColorFondo = colorFondo,
            widget1x1ColorBorde = colorBorde,
            widget1x1ColorIcono = colorIcono,
            widget1x1VidrioEsmerilado = vidrioEsmerilado,
            widget1x1EsmeriladoIntensidad = esmeriladoIntensidad,
            widget1x1BloquearProporcion = bloquearProporcion ?: it.widget1x1BloquearProporcion
        )
    }

    fun aplicarPresetHonorWidget1x1() = actualizarWidget1x1 {
        it.copy(
            widget1x1GrosorBordeDp = AjustesDefaults.Widget1x1.GROSOR_BORDE_DP,
            widget1x1CurvaturaEsquinasDp = AjustesDefaults.Widget1x1.CURVATURA_ESQUINAS_DP,
            widget1x1AnchoDp = AjustesDefaults.Widget1x1.ANCHO_DP,
            widget1x1AltoDp = AjustesDefaults.Widget1x1.ALTO_DP,
            widget1x1TamanoDp = AjustesDefaults.Widget1x1.TAMANO_DP,
            widget1x1BloquearProporcion = AjustesDefaults.Widget1x1.BLOQUEAR_PROPORCION,
            widget1x1OffsetX = AjustesDefaults.Widget1x1.OFFSET_X,
            widget1x1OffsetY = AjustesDefaults.Widget1x1.OFFSET_Y,
            widget1x1ColorBorde = AjustesDefaults.Widget1x1.COLOR_BORDE,
            widget1x1ColorIcono = AjustesDefaults.Widget1x1.COLOR_ICONO,
            widget1x1ColorFondo = AjustesDefaults.Widget1x1.COLOR_FONDO,
            widget1x1Modo = AjustesDefaults.Widget1x1.MODO
        )
    }

    fun restablecerAjustesWidget1x1() = actualizarWidget1x1 {
        it.copy(
            widget1x1Haptica = AjustesDefaults.Widget1x1.HAPTICA,
            widget1x1HapticaIntensidad = AjustesDefaults.Widget1x1.HAPTICA_INTENSIDAD,
            widget1x1Modo = AjustesDefaults.Widget1x1.MODO,
            widget1x1Longitud = AjustesDefaults.Widget1x1.LONGITUD,
            widget1x1Patron = AjustesDefaults.Widget1x1.PATRON,
            widget1x1Simbolos = AjustesDefaults.Widget1x1.SIMBOLOS,
            widget1x1CopiarPortapapeles = AjustesDefaults.Widget1x1.COPIAR_PORTAPAPELES,
            widget1x1MostrarToast = AjustesDefaults.Widget1x1.MOSTRAR_TOAST,
            widget1x1GrosorBordeDp = AjustesDefaults.Widget1x1.GROSOR_BORDE_DP,
            widget1x1CurvaturaEsquinasDp = AjustesDefaults.Widget1x1.CURVATURA_ESQUINAS_DP,
            widget1x1TransparenciaFondo = AjustesDefaults.Widget1x1.TRANSPARENCIA_FONDO,
            widget1x1TamanoDp = AjustesDefaults.Widget1x1.TAMANO_DP,
            widget1x1AnchoDp = AjustesDefaults.Widget1x1.ANCHO_DP,
            widget1x1AltoDp = AjustesDefaults.Widget1x1.ALTO_DP,
            widget1x1BloquearProporcion = AjustesDefaults.Widget1x1.BLOQUEAR_PROPORCION,
            widget1x1OffsetX = AjustesDefaults.Widget1x1.OFFSET_X,
            widget1x1OffsetY = AjustesDefaults.Widget1x1.OFFSET_Y,
            widget1x1Alineamiento = AjustesDefaults.Widget1x1.ALINEAMIENTO,
            widget1x1ColorBorde = AjustesDefaults.Widget1x1.COLOR_BORDE,
            widget1x1ColorIcono = AjustesDefaults.Widget1x1.COLOR_ICONO,
            widget1x1ColorFondo = AjustesDefaults.Widget1x1.COLOR_FONDO,
            widget1x1DicewarePalabras = AjustesDefaults.Widget1x1.DICEWARE_PALABRAS,
            widget1x1DicewareSeparador = AjustesDefaults.Widget1x1.DICEWARE_SEPARADOR
        )
    }

    fun restablecerAspectoWidget1x1() = actualizarWidget1x1 {
        it.copy(
            widget1x1GrosorBordeDp = AjustesDefaults.Widget1x1.GROSOR_BORDE_DP,
            widget1x1CurvaturaEsquinasDp = AjustesDefaults.Widget1x1.CURVATURA_ESQUINAS_DP,
            widget1x1TransparenciaFondo = AjustesDefaults.Widget1x1.TRANSPARENCIA_FONDO
        )
    }

    fun restablecerDimensionesWidget1x1() = actualizarWidget1x1 {
        it.copy(
            widget1x1AnchoDp = AjustesDefaults.Widget1x1.ANCHO_DP,
            widget1x1AltoDp = AjustesDefaults.Widget1x1.ALTO_DP,
            widget1x1BloquearProporcion = AjustesDefaults.Widget1x1.BLOQUEAR_PROPORCION,
            widget1x1Alineamiento = AjustesDefaults.Widget1x1.ALINEAMIENTO,
            widget1x1OffsetY = AjustesDefaults.Widget1x1.OFFSET_Y,
            widget1x1OffsetX = AjustesDefaults.Widget1x1.OFFSET_X
        )
    }

    fun restablecerModoGeneracionWidget1x1() = actualizarWidget1x1 {
        it.copy(
            widget1x1Modo = AjustesDefaults.Widget1x1.MODO,
            widget1x1Longitud = AjustesDefaults.Widget1x1.LONGITUD,
            widget1x1Simbolos = AjustesDefaults.Widget1x1.SIMBOLOS,
            widget1x1DicewarePalabras = AjustesDefaults.Widget1x1.DICEWARE_PALABRAS,
            widget1x1DicewareSeparador = AjustesDefaults.Widget1x1.DICEWARE_SEPARADOR,
            widget1x1Patron = AjustesDefaults.Widget1x1.PATRON
        )
    }

    fun restablecerComportamientoWidget1x1() = actualizarWidget1x1 {
        it.copy(
            widget1x1CopiarPortapapeles = AjustesDefaults.Widget1x1.COPIAR_PORTAPAPELES,
            widget1x1MostrarToast = AjustesDefaults.Widget1x1.MOSTRAR_TOAST,
            widget1x1Haptica = AjustesDefaults.Widget1x1.HAPTICA,
            widget1x1HapticaIntensidad = AjustesDefaults.Widget1x1.HAPTICA_INTENSIDAD
        )
    }

    fun restablecerColoresWidget1x1() = actualizarWidget1x1 {
        it.copy(
            widget1x1ColorBorde = AjustesDefaults.Widget1x1.COLOR_BORDE,
            widget1x1ColorIcono = AjustesDefaults.Widget1x1.COLOR_ICONO,
            widget1x1ColorFondo = AjustesDefaults.Widget1x1.COLOR_FONDO
        )
    }
}
