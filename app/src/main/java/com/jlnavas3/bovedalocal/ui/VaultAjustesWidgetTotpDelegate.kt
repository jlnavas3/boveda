package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos

/**
 * Sub-delegado especializado en calibración visual y háptica del Widget TOTP.
 */
interface VaultAjustesWidgetTotpDelegate {
    val repositorio: VaultRepository
    fun obtenerApp(): Application

    fun ajustarWidgetColorFilas(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorFilas = colorHex) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetTransparenciaFilas(transparencia: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetTransparenciaFilas = transparencia) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetGrosorBorde(grosor: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetGrosorBordeDp = grosor) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetCurvaturaEsquinas(curvatura: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetCurvaturaEsquinasDp = curvatura) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetTransparenciaFondo(transparencia: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetTransparenciaFondo = transparencia) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetColorBorde(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorBorde = colorHex) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetColorContador(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorContador = colorHex) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetColorCodigo(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorCodigo = colorHex) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetColorTituloIcono(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorTituloIcono = colorHex) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetTotpVidrioEsmerilado(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(widgetTotpVidrioEsmerilado = activo) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetTotpEsmeriladoIntensidad(intensidad: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetTotpEsmeriladoIntensidad = intensidad) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetTotpEsmeriladoLuz(luz: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetTotpEsmeriladoLuz = luz) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun aplicarPresetEstiloWidgetTotp(
        curvaturaDp: Float,
        grosorDp: Float,
        transparenciaFondo: Float,
        transparenciaFilas: Float,
        colorBorde: String,
        colorCodigo: String,
        colorContador: String,
        colorTitulo: String,
        colorFilas: String,
        vidrioEsmerilado: Boolean = false,
        esmeriladoIntensidad: Float = 0.60f
    ) {
        repositorio.ajustes.actualizar {
            it.copy(
                widgetCurvaturaEsquinasDp = curvaturaDp,
                widgetGrosorBordeDp = grosorDp,
                widgetTransparenciaFondo = transparenciaFondo,
                widgetTransparenciaFilas = transparenciaFilas,
                widgetColorBorde = colorBorde,
                widgetColorCodigo = colorCodigo,
                widgetColorContador = colorContador,
                widgetColorTituloIcono = colorTitulo,
                widgetColorFilas = colorFilas,
                widgetTotpVidrioEsmerilado = vidrioEsmerilado,
                widgetTotpEsmeriladoIntensidad = esmeriladoIntensidad
            )
        }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetHaptica(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(widgetHaptica = activo) }

    fun ajustarWidgetHapticaIntensidad(intensidad: Float) =
        repositorio.ajustes.actualizar { it.copy(widgetHapticaIntensidad = intensidad.coerceIn(0.01f, 1.0f)) }

    fun restablecerAjustesWidget() {
        repositorio.ajustes.actualizar {
            it.copy(
                widgetGrosorBordeDp = AjustesDefaults.WidgetTotp.GROSOR_BORDE_DP,
                widgetCurvaturaEsquinasDp = AjustesDefaults.WidgetTotp.CURVATURA_ESQUINAS_DP,
                widgetTransparenciaFondo = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FONDO,
                widgetColorBorde = AjustesDefaults.WidgetTotp.COLOR_BORDE,
                widgetColorContador = AjustesDefaults.WidgetTotp.COLOR_CONTADOR,
                widgetColorCodigo = AjustesDefaults.WidgetTotp.COLOR_CODIGO,
                widgetColorTituloIcono = AjustesDefaults.WidgetTotp.COLOR_TITULO_ICONO,
                widgetColorFilas = AjustesDefaults.WidgetTotp.COLOR_FILAS,
                widgetTransparenciaFilas = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FILAS,
                widgetHaptica = AjustesDefaults.WidgetTotp.HAPTICA,
                widgetHapticaIntensidad = AjustesDefaults.WidgetTotp.HAPTICA_INTENSIDAD
            )
        }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun restablecerColoresWidgetTotp() {
        repositorio.ajustes.actualizar {
            it.copy(
                widgetColorBorde = AjustesDefaults.WidgetTotp.COLOR_BORDE,
                widgetColorContador = AjustesDefaults.WidgetTotp.COLOR_CONTADOR,
                widgetColorCodigo = AjustesDefaults.WidgetTotp.COLOR_CODIGO,
                widgetColorTituloIcono = AjustesDefaults.WidgetTotp.COLOR_TITULO_ICONO,
                widgetColorFilas = AjustesDefaults.WidgetTotp.COLOR_FILAS
            )
        }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun restablecerFormaWidgetTotp() {
        repositorio.ajustes.actualizar {
            it.copy(
                widgetGrosorBordeDp = AjustesDefaults.WidgetTotp.GROSOR_BORDE_DP,
                widgetCurvaturaEsquinasDp = AjustesDefaults.WidgetTotp.CURVATURA_ESQUINAS_DP,
                widgetTransparenciaFondo = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FONDO,
                widgetTransparenciaFilas = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FILAS
            )
        }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }
}
