package com.jlnavas3.bovedalocal.autofill

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.IconosMarcas
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

/**
 * Resuelve el glifo o silueta vectorial con fondo transparente para sugerencias en línea
 * del teclado (Gboard/IME), asegurando que el tinte del tema del teclado no genere bloques sólidos.
 */
object ResolverIconoGlifoTeclado {

    fun resolver(
        contexto: Context,
        entrada: Entrada? = null,
        titulo: String = entrada?.titulo.orEmpty(),
        urls: List<String> = entrada?.urls.orEmpty(),
        paquete: String?,
        dominioWeb: String?,
        tamanoPx: Int = 72
    ): Bitmap {
        // 1. Intentar resolver mediante el catálogo vectorial de marcas (IconosMarcas)
        val semillas = buildList {
            dominioWeb?.takeIf { it.isNotBlank() }?.let { add(it) }
            urls.forEach { add(it) }
            paquete?.takeIf { it.isNotBlank() }?.let { add(it) }
        }

        for (semilla in semillas) {
            val resMarca = IconosMarcas.buscar(semilla, titulo)
            if (resMarca != null) {
                val bitmapVector = renderizarVectorABitmap(contexto, resMarca, tamanoPx)
                if (bitmapVector != null) return bitmapVector
            }
        }

        // 2. Si no está en el catálogo pero la app está instalada, extraer el primer plano del icono adaptativo
        if (paquete != null && LanzadorEnlaces.estaInstalada(contexto, paquete)) {
            val bitmapPrimerPlano = extraerPrimerPlanoAdaptativo(contexto, paquete, tamanoPx)
            if (bitmapPrimerPlano != null) return bitmapPrimerPlano
        }

        // 3. Fallback al candado de Bóveda Local
        return renderizarVectorABitmap(contexto, R.drawable.ic_candado_boveda, tamanoPx)
            ?: Bitmap.createBitmap(tamanoPx, tamanoPx, Bitmap.Config.ARGB_8888)
    }

    private fun renderizarVectorABitmap(contexto: Context, resId: Int, tamanoPx: Int): Bitmap? {
        return try {
            val drawable = ContextCompat.getDrawable(contexto, resId) ?: return null
            drawable.toBitmap(
                width = tamanoPx,
                height = tamanoPx,
                config = Bitmap.Config.ARGB_8888
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun extraerPrimerPlanoAdaptativo(contexto: Context, paquete: String, tamanoPx: Int): Bitmap? {
        return try {
            val pm = contexto.packageManager
            val appInfo = pm.getApplicationInfo(paquete, 0)
            val drawable = appInfo.loadIcon(pm) ?: return null
            if (drawable is AdaptiveIconDrawable) {
                val primerPlano = drawable.foreground ?: return null
                val bitmap = Bitmap.createBitmap(tamanoPx, tamanoPx, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                val margen = (tamanoPx * 0.12f).toInt()
                primerPlano.setBounds(margen, margen, tamanoPx - margen, tamanoPx - margen)
                primerPlano.draw(canvas)
                bitmap
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}
