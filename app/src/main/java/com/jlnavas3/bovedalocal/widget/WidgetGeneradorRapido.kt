package com.jlnavas3.bovedalocal.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.quicksettings.GeneradorRapidoHelper

class WidgetGeneradorRapido : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (id in appWidgetIds) {
            actualizarWidget(context, appWidgetManager, id)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_GENERAR) {
            GeneradorRapidoHelper.generar(context, "Widget 1x1")
        }
    }

    companion object {
        const val ACTION_GENERAR = "com.jlnavas3.bovedalocal.widget.GENERAR_RAPIDO"

        fun actualizarWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_generador_rapido)

            val intent = Intent(context, WidgetGeneradorRapido::class.java).apply {
                action = ACTION_GENERAR
            }
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, flags)
            views.setOnClickPendingIntent(R.id.widget_generador_contenedor, pendingIntent)
            views.setOnClickPendingIntent(R.id.widget_generador_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun actualizarTodos(context: Context) {
            val manager = AppWidgetManager.getInstance(context) ?: return
            val ids = manager.getAppWidgetIds(ComponentName(context, WidgetGeneradorRapido::class.java))
            for (id in ids) {
                actualizarWidget(context, manager, id)
            }
        }
    }
}
