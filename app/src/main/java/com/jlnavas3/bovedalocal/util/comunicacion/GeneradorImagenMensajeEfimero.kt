/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util.comunicacion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Microgenerador desacoplado para convertir un texto confidencial en una imagen Bitmap
 * renderizada sobre un lienzo seguro de alto contraste con cabecera de confidencialidad
 * y marca de tiempo, guardada temporalmente para compartir mediante FileProvider.
 */
object GeneradorImagenMensajeEfimero {

    private const val CARPETA_EFIMERA = "mensajes_efimeros"
    private const val NOMBRE_ARCHIVO = "msg_efimero.png"
    private const val ANCHO_LIENZO = 1080

    fun generarImagen(context: Context, texto: String): Uri? {
        if (texto.isBlank()) return null

        // 1. Limpieza preventiva de restos anteriores
        PurgadorArchivosEfimeros.purgar(context)

        // 2. Preparación de pinturas y tipografías
        val paddingHorizontal = 64f
        val paddingVertical = 64f
        val anchoUtil = (ANCHO_LIENZO - (paddingHorizontal * 2)).toInt()

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 42f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }

        val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(180, 180, 180)
            textSize = 28f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(130, 130, 130)
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        // Construcción del layout de texto multilinea
        @Suppress("DEPRECATION")
        val staticLayout = StaticLayout(
            texto,
            textPaint,
            anchoUtil,
            Layout.Alignment.ALIGN_NORMAL,
            1.25f,
            0f,
            false
        )

        val alturaEncabezado = 80f
        val alturaPie = 80f
        val alturaContenido = staticLayout.height
        val altoTotal = (paddingVertical * 2 + alturaEncabezado + alturaContenido + alturaPie).toInt().coerceAtLeast(400)

        // 3. Dibujo sobre Bitmap
        val bitmap = Bitmap.createBitmap(ANCHO_LIENZO, altoTotal, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Fondo oscuro de alta confidencialidad
        canvas.drawColor(Color.rgb(18, 18, 18))

        // Borde sutil
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(45, 45, 45)
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        canvas.drawRect(8f, 8f, (ANCHO_LIENZO - 8).toFloat(), (altoTotal - 8).toFloat(), borderPaint)

        // Cabecera
        canvas.drawText("BÓVEDA LOCAL • MENSAJE CONFIDENCIAL", paddingHorizontal, paddingVertical + 30f, headerPaint)

        // Línea divisoria superior
        val lineaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(40, 40, 40)
            strokeWidth = 2f
        }
        canvas.drawLine(
            paddingHorizontal,
            paddingVertical + alturaEncabezado - 16f,
            ANCHO_LIENZO - paddingHorizontal,
            paddingVertical + alturaEncabezado - 16f,
            lineaPaint
        )

        // Contenido del texto confidencial
        canvas.save()
        canvas.translate(paddingHorizontal, paddingVertical + alturaEncabezado)
        staticLayout.draw(canvas)
        canvas.restore()

        // Línea divisoria inferior
        val yDivisoriaInferior = paddingVertical + alturaEncabezado + alturaContenido + 20f
        canvas.drawLine(
            paddingHorizontal,
            yDivisoriaInferior,
            ANCHO_LIENZO - paddingHorizontal,
            yDivisoriaInferior,
            lineaPaint
        )

        // Pie con marca de tiempo
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val marcaTiempo = "Generado: ${sdf.format(Date())} • Vista única"
        canvas.drawText(marcaTiempo, paddingHorizontal, yDivisoriaInferior + 38f, footerPaint)

        // 4. Guardar en almacenamiento de caché segura
        return try {
            val dir = File(context.cacheDir, CARPETA_EFIMERA).apply { mkdirs() }
            val archivo = File(dir, NOMBRE_ARCHIVO)
            FileOutputStream(archivo).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
            }
            bitmap.recycle()

            val autoridad = "${context.packageName}.fileprovider"
            FileProvider.getUriForFile(context, autoridad, archivo)
        } catch (_: Exception) {
            bitmap.recycle()
            null
        }
    }
}
