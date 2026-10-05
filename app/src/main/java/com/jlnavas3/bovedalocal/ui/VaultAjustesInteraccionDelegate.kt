package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoActivo
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoDuracionMs
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoIntensidad
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoRepeticiones
import com.jlnavas3.bovedalocal.util.CambiadorIcono

/**
 * Sub-delegado especializado en interacción táctil (háptica), efectos de alumbrado, icono launcher y opciones de sistema.
 */
interface VaultAjustesInteraccionDelegate {
    val repositorio: VaultRepository
    fun obtenerApp(): Application

    fun ajustarNombrePersonalizado(nombre: String) =
        repositorio.ajustes.actualizar { it.copy(nombrePersonalizado = nombre) }

    /** Cambia el icono del launcher (las 20 variantes precompiladas de Android). */
    fun ajustarIconoLauncher(clave: String) {
        repositorio.ajustes.actualizar { it.copy(iconoLauncher = clave) }
        CambiadorIcono.aplicar(obtenerApp(), clave)
    }

    fun restablecerIconoLauncher() = ajustarIconoLauncher(AjustesDefaults.Tema.ICONO_LAUNCHER)

    fun ajustarMostrarIdsAjustes(mostrar: Boolean) {
        repositorio.ajustes.actualizar { it.copy(mostrarIdsAjustes = mostrar) }
    }

    fun ajustarAutofillSugerenciasTeclado(activado: Boolean) {
        repositorio.ajustes.actualizar { it.copy(autofillSugerenciasTeclado = activado) }
    }

    fun ajustarAlumbradoActivo(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(alumbradoActivo = activo) }
        AlumbradoActivo = activo
    }

    fun ajustarAlumbradoIntensidad(intensidad: Float) {
        val valor = intensidad.coerceIn(0.1f, 1.0f)
        repositorio.ajustes.actualizar { it.copy(alumbradoIntensidad = valor) }
        AlumbradoIntensidad = valor
    }

    fun ajustarAlumbradoRepeticiones(repeticiones: Int) {
        val valor = repeticiones.coerceIn(1, 5)
        repositorio.ajustes.actualizar { it.copy(alumbradoRepeticiones = valor) }
        AlumbradoRepeticiones = valor
    }

    fun ajustarAlumbradoDuracionMs(duracionMs: Int) {
        val valor = duracionMs.coerceIn(300, 2000)
        repositorio.ajustes.actualizar { it.copy(alumbradoDuracionMs = valor) }
        AlumbradoDuracionMs = valor
    }

    fun restablecerAlumbrado() {
        repositorio.ajustes.actualizar {
            it.copy(
                alumbradoActivo = AjustesDefaults.Interaccion.ALUMBRADO_ACTIVO,
                alumbradoIntensidad = AjustesDefaults.Interaccion.ALUMBRADO_INTENSIDAD,
                alumbradoRepeticiones = AjustesDefaults.Interaccion.ALUMBRADO_REPETICIONES,
                alumbradoDuracionMs = AjustesDefaults.Interaccion.ALUMBRADO_DURACION_MS
            )
        }
    }

    fun ajustarHapticaApp(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(hapticaApp = activo) }

    fun ajustarHapticaAppIntensidad(intensidad: Float) =
        repositorio.ajustes.actualizar { it.copy(hapticaAppIntensidad = intensidad.coerceIn(0.01f, 1.0f)) }

    fun restablecerHapticaApp() {
        repositorio.ajustes.actualizar {
            it.copy(
                hapticaApp = AjustesDefaults.Interaccion.HAPTICA_APP,
                hapticaAppIntensidad = AjustesDefaults.Interaccion.HAPTICA_APP_INTENSIDAD
            )
        }
    }

    fun recargarAjustes() {
        repositorio.ajustes.recargar()
    }
}
