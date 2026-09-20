package com.jlnavas3.bovedalocal.quicksettings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.util.Diagnostico

object GeneradorRapidoHelper {

    fun generar(context: Context, origen: String = "Acceso Rápido"): String {
        val appContext = context.applicationContext
        val repo = com.jlnavas3.bovedalocal.data.VaultRepository.obtener(appContext)
        val ajustes = repo.ajustes.actual
        val clave = if (ajustes.tileModo == "patron") {
            PasswordGenerator.generarPorPatron(ajustes.tilePatron)
        } else {
            PasswordGenerator.generarAleatoria(
                OpcionesGenerador(
                    longitud = ajustes.tileLongitud,
                    mayusculas = true,
                    minusculas = true,
                    digitos = true,
                    simbolos = true
                )
            )
        }

        if (ajustes.tileCopiarPortapapeles) {
            val cm = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            if (cm != null) {
                val clip = ClipData.newPlainText("Contraseña generada", clave)
                cm.setPrimaryClip(clip)
                Diagnostico.apuntar("portapapeles", "Contraseña generada desde $origen")
            }
        } else {
            Diagnostico.apuntar("bóveda", "Contraseña generada desde $origen")
        }

        if (ajustes.tileHaptica) {
            ejecutarVibracion(appContext)
        }

        if (ajustes.tileMostrarToast) {
            val mensaje = if (ajustes.tileCopiarPortapapeles) {
                "Contraseña generada y copiada al portapapeles"
            } else {
                "Clave generada: $clave"
            }
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(appContext, mensaje, Toast.LENGTH_SHORT).show()
            }
        }

        registrarEnHistorial(appContext, clave, origen)

        return clave
    }

    fun registrarEnHistorial(context: Context, clave: String, origen: String) {
        try {
            val repo = com.jlnavas3.bovedalocal.data.VaultRepository.obtener(context.applicationContext)
            val ahora = System.currentTimeMillis()
            repo.ajustes.actualizar { actual ->
                val listaLimpia = if (actual.historialClavesVaciadoAuto && actual.historialClavesTiempoAutoDestruccion > 0) {
                    actual.historialClaves.filter { ahora - it.generadaEn < actual.historialClavesTiempoAutoDestruccion }
                } else {
                    actual.historialClaves
                }
                val nuevoRegistro = com.jlnavas3.bovedalocal.data.RegistroClaveGenerada(
                    clave = clave,
                    generadaEn = ahora,
                    origen = origen
                )
                val nuevaLista = (listOf(nuevoRegistro) + listaLimpia).take(actual.historialClavesMax.coerceAtLeast(1))
                actual.copy(historialClaves = nuevaLista)
            }
            Diagnostico.apuntar("historial", "Registrada clave generada desde $origen. Total en historial: ${repo.ajustes.actual.historialClaves.size}")
        } catch (e: Exception) {
            Diagnostico.apuntar("historial", "Error al registrar en historial: ${e.message}")
        }
    }

    fun ejecutarVibracion(context: Context) {
        try {
            val v = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            } ?: return

            if (v.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val efecto = if (v.hasAmplitudeControl()) {
                        VibrationEffect.createOneShot(45, 200)
                    } else {
                        VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE)
                    }
                    v.vibrate(efecto)
                } else {
                    @Suppress("DEPRECATION")
                    v.vibrate(45)
                }
            }
        } catch (_: Exception) {
        }
    }
}
