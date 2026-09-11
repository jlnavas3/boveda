package com.jlnavas3.bovedalocal.quicksettings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.AlmacenAjustes

class TileGeneradorRapido : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        val tile = qsTile ?: return
        tile.state = Tile.STATE_ACTIVE
        tile.label = "Generador Rápido"
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        val ajustes = AlmacenAjustes(applicationContext).actual
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
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            if (cm != null) {
                val clip = ClipData.newPlainText("Contraseña generada", clave)
                cm.setPrimaryClip(clip)
            }
        }

        if (ajustes.tileHaptica) {
            ejecutarVibracion()
        }

        if (ajustes.tileMostrarToast) {
            val mensaje = if (ajustes.tileCopiarPortapapeles) {
                "Contraseña generada y copiada"
            } else {
                "Clave generada: $clave"
            }
            Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    private fun ejecutarVibracion() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                val v = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                v?.vibrate(35)
            }
        } catch (_: Exception) {
        }
    }
}
