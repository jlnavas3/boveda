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
        val esWidget1x1 = origen == "Widget 1x1"

        val modo = if (esWidget1x1) ajustes.widget1x1Modo else ajustes.tileModo
        val longitud = if (esWidget1x1) ajustes.widget1x1Longitud else ajustes.tileLongitud
        val patron = if (esWidget1x1) ajustes.widget1x1Patron else ajustes.tilePatron
        val copiar = if (esWidget1x1) ajustes.widget1x1CopiarPortapapeles else ajustes.tileCopiarPortapapeles
        val mostrarToast = if (esWidget1x1) ajustes.widget1x1MostrarToast else ajustes.tileMostrarToast
        val hapticaActiva = if (esWidget1x1) ajustes.widget1x1Haptica else ajustes.tileHaptica
        val hapticaIntensidad = if (esWidget1x1) ajustes.widget1x1HapticaIntensidad else ajustes.tileHapticaIntensidad

        val clave = when (modo) {
            "patron" -> PasswordGenerator.generarPorPatron(patron)
            "diceware" -> {
                val palabras = if (esWidget1x1) ajustes.widget1x1DicewarePalabras else ajustes.tileDicewarePalabras
                val separador = if (esWidget1x1) ajustes.widget1x1DicewareSeparador else ajustes.tileDicewareSeparador
                PasswordGenerator.generarFrase(numeroPalabras = palabras, separador = separador)
            }
            else -> PasswordGenerator.generarAleatoria(
                OpcionesGenerador(
                    longitud = longitud,
                    mayusculas = true,
                    minusculas = true,
                    digitos = true,
                    simbolos = true,
                    simbolosPersonalizados = if (esWidget1x1) ajustes.widget1x1Simbolos else ajustes.tileSimbolos
                )
            )
        }

        if (copiar) {
            val cm = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            if (cm != null) {
                val clip = ClipData.newPlainText("Contraseña generada", clave)
                cm.setPrimaryClip(clip)
                Diagnostico.apuntar("portapapeles", "Contraseña generada desde $origen")
            }
        } else {
            Diagnostico.apuntar("bóveda", "Contraseña generada desde $origen")
        }

        if (hapticaActiva) {
            com.jlnavas3.bovedalocal.util.Haptica.vibrarExterno(appContext, activo = true, intensidad = hapticaIntensidad)
        }

        if (mostrarToast) {
            val mensaje = if (copiar) {
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
            val repo = com.jlnavas3.bovedalocal.data.VaultRepository.obtener(context.applicationContext)
            val ajustes = repo.ajustes.actual
            com.jlnavas3.bovedalocal.util.Haptica.vibrarExterno(
                context = context,
                activo = ajustes.tileHaptica,
                intensidad = ajustes.tileHapticaIntensidad
            )
        } catch (_: Exception) {
        }
    }

    fun solicitarAgregarTile(contexto: Context, alTerminar: ((Boolean) -> Unit)? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val statusBarManager = contexto.getSystemService(android.app.StatusBarManager::class.java)
            val componentName = android.content.ComponentName(contexto, TileGeneradorRapido::class.java)
            val icon = android.graphics.drawable.Icon.createWithResource(
                contexto,
                com.jlnavas3.bovedalocal.R.drawable.ic_tile_generador
            )

            statusBarManager?.requestAddTileService(
                componentName,
                "Generador Rápido",
                icon,
                androidx.core.content.ContextCompat.getMainExecutor(contexto)
            ) { resultCode ->
                when (resultCode) {
                    android.app.StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED -> {
                        Toast.makeText(contexto, "Mosaico añadido a Ajustes Rápidos", Toast.LENGTH_SHORT).show()
                        alTerminar?.invoke(true)
                    }
                    android.app.StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED -> {
                        Toast.makeText(contexto, "El mosaico ya está en los Ajustes Rápidos", Toast.LENGTH_SHORT).show()
                        alTerminar?.invoke(true)
                    }
                    android.app.StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_NOT_ADDED -> {
                        alTerminar?.invoke(false)
                    }
                }
            }
        } else {
            Toast.makeText(
                contexto,
                "Desliza el panel de notificaciones y pulsa Editar para arrastrar «Generador Rápido»",
                Toast.LENGTH_LONG
            ).show()
            alTerminar?.invoke(false)
        }
    }
}
