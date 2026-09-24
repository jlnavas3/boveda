package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

import com.jlnavas3.bovedalocal.data.AjustesApp
import kotlin.math.roundToInt

class Haptica(contexto: Context) {

    private val vibrador: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val gestor = contexto.getSystemService(VibratorManager::class.java)
        gestor?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        contexto.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    fun tic() {
        if (!appHapticaActiva) return
        val (dur, amp) = calcularParametros(25, 120, appHapticaIntensidad)
        efecto(dur, amp)
    }

    fun toque() {
        if (!appHapticaActiva) return
        val (dur, amp) = calcularParametros(45, 200, appHapticaIntensidad)
        efecto(dur, amp)
    }

    fun exito() {
        if (!appHapticaActiva) return
        val factor = appHapticaIntensidad.coerceIn(0.01f, 1.0f)
        val amp1 = (1 + 149 * ((factor - 0.01f) / 0.99f)).roundToInt().coerceIn(1, 255)
        val amp2 = (1 + 219 * ((factor - 0.01f) / 0.99f)).roundToInt().coerceIn(1, 255)
        val dur1 = (25 * (0.35f + 0.65f * factor)).toLong().coerceIn(5, 25)
        val dur2 = (45 * (0.35f + 0.65f * factor)).toLong().coerceIn(8, 45)
        patron(longArrayOf(0, dur1, 60, dur2), intArrayOf(0, amp1, 0, amp2))
    }

    fun error() {
        if (!appHapticaActiva) return
        val factor = appHapticaIntensidad.coerceIn(0.01f, 1.0f)
        val amp = (1 + 199 * ((factor - 0.01f) / 0.99f)).roundToInt().coerceIn(1, 255)
        val dur = (50 * (0.35f + 0.65f * factor)).toLong().coerceIn(8, 50)
        patron(longArrayOf(0, dur, 80, dur), intArrayOf(0, amp, 0, amp))
    }

    /**
     * Prueba táctil con intensidad directa para calibración (e.g. mientras se mueve el slider).
     */
    fun probar(intensidad: Float = appHapticaIntensidad) {
        val (dur, amp) = calcularParametros(35, 220, intensidad)
        efecto(dur, amp)
    }

    fun efectoPersonalizado(duracion: Long, intensidad: Float) {
        val (dur, amp) = calcularParametros(duracion, 255, intensidad)
        efecto(dur, amp)
    }

    private fun calcularParametros(duracionBase: Long, amplitudBase: Int, intensidad: Float): Pair<Long, Int> {
        val factor = intensidad.coerceIn(0.01f, 1.0f)
        // Desde el valor más mínimo posible: factor=0.01f da amplitud 1
        val amplitud = (1 + (amplitudBase - 1) * ((factor - 0.01f) / 0.99f)).roundToInt().coerceIn(1, 255)
        // La duración también se modula sutilmente para hardware binario
        val duracion = (duracionBase * (0.35f + 0.65f * factor)).toLong().coerceAtLeast(6L)
        return Pair(duracion, amplitud)
    }

    private fun efecto(duracion: Long, amplitud: Int) {
        val v = vibrador ?: return
        if (!v.hasVibrator()) return
        try {
            val efecto = if (v.hasAmplitudeControl()) {
                VibrationEffect.createOneShot(duracion, amplitud.coerceIn(1, 255))
            } else {
                VibrationEffect.createOneShot(duracion, VibrationEffect.DEFAULT_AMPLITUDE)
            }
            v.vibrate(efecto)
        } catch (_: Exception) {
            try {
                @Suppress("DEPRECATION")
                v.vibrate(duracion)
            } catch (_: Exception) { }
        }
    }

    private fun patron(tiempos: LongArray, amplitudes: IntArray) {
        val v = vibrador ?: return
        if (!v.hasVibrator()) return
        try {
            if (v.hasAmplitudeControl()) {
                v.vibrate(VibrationEffect.createWaveform(tiempos, amplitudes, -1))
            } else {
                val defaultAmplitudes = IntArray(amplitudes.size) { i ->
                    if (amplitudes[i] > 0) VibrationEffect.DEFAULT_AMPLITUDE else 0
                }
                v.vibrate(VibrationEffect.createWaveform(tiempos, defaultAmplitudes, -1))
            }
        } catch (_: Exception) {
            try {
                @Suppress("DEPRECATION")
                v.vibrate(tiempos, -1)
            } catch (_: Exception) { }
        }
    }

    companion object {
        @Volatile var appHapticaActiva: Boolean = true
        @Volatile var appHapticaIntensidad: Float = 0.10f

        fun sincronizar(ajustes: AjustesApp) {
            appHapticaActiva = ajustes.hapticaApp
            appHapticaIntensidad = ajustes.hapticaAppIntensidad
        }

        /**
         * Ejecuta vibración desde componentes en segundo plano (Widgets, Tiles, BroadcastReceivers).
         * Utiliza USAGE_ALARM para evitar que Android 12+ descarte la vibración por falta de foco de ventana.
         */
        fun vibrarExterno(context: Context, activo: Boolean, intensidad: Float) {
            if (!activo) return
            try {
                val factor = intensidad.coerceIn(0.01f, 1.0f)
                val amplitud = (1 + 254 * ((factor - 0.01f) / 0.99f)).roundToInt().coerceIn(1, 255)
                val duracion = (35 * (0.35f + 0.65f * factor)).toLong().coerceAtLeast(8L)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val gestor = context.getSystemService(VibratorManager::class.java)
                    val vibrador = gestor?.defaultVibrator
                    if (vibrador != null && vibrador.hasVibrator()) {
                        val efecto = if (vibrador.hasAmplitudeControl()) {
                            VibrationEffect.createOneShot(duracion, amplitud)
                        } else {
                            VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                        }
                        val attrs = VibrationAttributes.Builder()
                            .setUsage(VibrationAttributes.USAGE_ALARM)
                            .build()
                        vibrador.vibrate(efecto, attrs)
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val gestor = context.getSystemService(VibratorManager::class.java)
                    val vibrador = gestor?.defaultVibrator
                    if (vibrador != null && vibrador.hasVibrator()) {
                        val efecto = if (vibrador.hasAmplitudeControl()) {
                            VibrationEffect.createOneShot(duracion, amplitud)
                        } else {
                            VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                        }
                        @Suppress("DEPRECATION")
                        val audioAttrs = AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                        @Suppress("DEPRECATION")
                        vibrador.vibrate(efecto, audioAttrs)
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val vibrador = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                    if (vibrador != null && vibrador.hasVibrator()) {
                        val efecto = if (vibrador.hasAmplitudeControl()) {
                            VibrationEffect.createOneShot(duracion, amplitud)
                        } else {
                            VibrationEffect.createOneShot(duracion, VibrationEffect.DEFAULT_AMPLITUDE)
                        }
                        @Suppress("DEPRECATION")
                        val audioAttrs = AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                        @Suppress("DEPRECATION")
                        vibrador.vibrate(efecto, audioAttrs)
                    }
                }
            } catch (_: Exception) {
                try {
                    @Suppress("DEPRECATION")
                    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                    @Suppress("DEPRECATION")
                    v?.vibrate(35L)
                } catch (_: Exception) {}
            }
        }
    }
}
