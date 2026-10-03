package com.jlnavas3.bovedalocal.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.Toast

object WidgetPinHelper {
    /**
     * Solicita al Launcher del sistema añadir el widget indicado directamente a la pantalla de inicio.
     * Compatible con Android 8.0 (API 26) o superior.
     */
    fun solicitarColocarWidget(contexto: Context, claseWidget: Class<*>) {
        val manager = AppWidgetManager.getInstance(contexto)
        if (manager != null && manager.isRequestPinAppWidgetSupported) {
            val provider = ComponentName(contexto, claseWidget)
            manager.requestPinAppWidget(provider, null, null)
        } else {
            Toast.makeText(
                contexto,
                "Tu pantalla de inicio no permite añadir widgets automáticamente. Puedes colocarlo manualmente manteniendo pulsado el escritorio.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
