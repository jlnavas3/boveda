package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.graphics.Path
import kotlin.math.cos
import kotlin.math.sin

/**
 * Algoritmo trigonométrico de generación vectorial para dientes de engranaje mecánico.
 */
fun crearPathEngranaje(
    radioPaso: Float,
    numDientes: Int,
    factorAltura: Float = 1.0f,
    factorAncho: Float = 1.0f
): Path {
    val path = Path()
    val modulo = (2f * radioPaso) / numDientes
    val h = modulo * 0.75f * factorAltura
    val rRaiz = (radioPaso - h * 0.9f).coerceAtLeast(radioPaso * 0.2f)
    val rPunta = radioPaso + h * 0.85f
    val pasoAngular = (2f * Math.PI / numDientes).toFloat()
    val wRaiz = (pasoAngular * 0.30f * factorAncho).coerceIn(0.01f, pasoAngular * 0.49f)
    val wPunta = (pasoAngular * 0.16f * factorAncho).coerceIn(0.005f, wRaiz)

    for (k in 0 until numDientes) {
        val angCentro = k * pasoAngular
        val aRaizIni = angCentro - wRaiz
        val aPuntaIni = angCentro - wPunta
        val aPuntaFin = angCentro + wPunta
        val aRaizFin = angCentro + wRaiz

        val p1x = rRaiz * cos(aRaizIni)
        val p1y = rRaiz * sin(aRaizIni)
        val p2x = rPunta * cos(aPuntaIni)
        val p2y = rPunta * sin(aPuntaIni)
        val p3x = rPunta * cos(aPuntaFin)
        val p3y = rPunta * sin(aPuntaFin)
        val p4x = rRaiz * cos(aRaizFin)
        val p4y = rRaiz * sin(aRaizFin)

        if (k == 0) {
            path.moveTo(p1x, p1y)
        } else {
            path.lineTo(p1x, p1y)
        }
        path.lineTo(p2x, p2y)
        path.lineTo(p3x, p3y)
        path.lineTo(p4x, p4y)
    }
    path.close()
    return path
}
