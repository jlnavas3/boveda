package com.jlnavas3.bovedalocal.cxf

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Registro de diagnóstico para el flujo de Credential Transfer FIDO CXF.
 * Escribe en Logcat y persiste en almacenamiento accesible para depuración vía ADB.
 */
object CxfRegistroDiagnostico {

    private const val TAG = "BovedaCXF"
    private val formatoFecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ROOT)

    fun log(context: Context?, mensaje: String, excepcion: Throwable? = null) {
        val stamp = formatoFecha.format(Date())
        val linea = if (excepcion != null) {
            "[$stamp] $mensaje: ${excepcion.message}\n${Log.getStackTraceString(excepcion)}"
        } else {
            "[$stamp] $mensaje"
        }

        if (excepcion != null) {
            Log.e(TAG, mensaje, excepcion)
        } else {
            Log.i(TAG, mensaje)
        }

        if (context == null) return

        try {
            val interno = File(context.filesDir, "boveda_cxf_log.txt")
            interno.appendText("$linea\n")

            // Si supera 200 KB, recortar la mitad más antigua
            if (interno.length() > 200 * 1024) {
                val lineas = interno.readLines()
                interno.writeText(lineas.takeLast(lineas.size / 2).joinToString("\n", postfix = "\n"))
            }
        } catch (_: Exception) {
        }
    }
}
