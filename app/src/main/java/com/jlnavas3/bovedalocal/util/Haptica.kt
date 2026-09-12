package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class Haptica(contexto: Context) {

    private val vibrador: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val gestor = contexto.getSystemService(VibratorManager::class.java)
        gestor?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        contexto.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    fun tic() {
        efecto(25, 120)
    }

    fun toque() {
        efecto(45, 200)
    }

    fun exito() = patron(longArrayOf(0, 25, 60, 45), intArrayOf(0, 150, 0, 220))

    fun error() = patron(longArrayOf(0, 50, 80, 50), intArrayOf(0, 200, 0, 200))

    private fun efecto(duracion: Long, amplitud: Int) {
        val v = vibrador ?: return
        if (!v.hasVibrator()) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val efecto = if (v.hasAmplitudeControl()) {
                    VibrationEffect.createOneShot(duracion, amplitud.coerceIn(1, 255))
                } else {
                    VibrationEffect.createOneShot(duracion, VibrationEffect.DEFAULT_AMPLITUDE)
                }
                v.vibrate(efecto)
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(duracion)
            }
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (v.hasAmplitudeControl()) {
                    v.vibrate(VibrationEffect.createWaveform(tiempos, amplitudes, -1))
                } else {
                    val defaultAmplitudes = IntArray(amplitudes.size) { i ->
                        if (amplitudes[i] > 0) VibrationEffect.DEFAULT_AMPLITUDE else 0
                    }
                    v.vibrate(VibrationEffect.createWaveform(tiempos, defaultAmplitudes, -1))
                }
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(tiempos, -1)
            }
        } catch (_: Exception) {
            try {
                @Suppress("DEPRECATION")
                v.vibrate(tiempos, -1)
            } catch (_: Exception) { }
        }
    }

}
