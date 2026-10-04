package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin

/**
 * Renderiza la capa profunda / subterránea del tren de engranajes (mecanismo secundario de fondo).
 */
fun DrawScope.dibujarCapaFondoEngranajes(
    c1: Offset,
    c2: Offset,
    c3: Offset,
    c4: Offset,
    rot1: Float,
    rot2: Float,
    rot3: Float,
    rot4: Float,
    p: Float,
    escala: Float,
    alpha: Float,
    config: EngranajesConfig,
    rPaso24: Float,
    rPaso20: Float,
    rPaso18: Float,
    rPaso16: Float,
    rPaso14: Float,
    pathZ24: Path,
    pathZ20: Path,
    pathZ18: Path,
    pathZ16: Path,
    pathZ14: Path
) {
    // Eje BG1 (z=14) sobre eje de C1, conectado con BG2 (z=18)
    val rotBG1 = -rot1 * 0.75f
    val cBG1 = c1
    val angBG12 = 30f
    val radBG12 = Math.toRadians(angBG12.toDouble())
    val dBG12 = (rPaso14 + rPaso18) * escala
    val cBG2 = Offset(
        cBG1.x + (dBG12 * cos(radBG12).toFloat()),
        cBG1.y + (dBG12 * sin(radBG12).toFloat()) + p * 20f * escala
    )
    val rotBG2 = (angBG12 + 180f) - (14f / 18f) * (rotBG1 - angBG12) + (180f / 18f)

    // BG3 (z=16) sobre BG2 hacia el flanco derecho medio
    val angBG23 = -40f
    val radBG23 = Math.toRadians(angBG23.toDouble())
    val dBG23 = (rPaso18 + rPaso16) * escala
    val cBG3 = Offset(
        cBG2.x + (dBG23 * cos(radBG23).toFloat()) - p * 15f * escala,
        cBG2.y + (dBG23 * sin(radBG23).toFloat()) + p * 25f * escala
    )
    val rotBG3 = (angBG23 + 180f) - (18f / 16f) * (rotBG2 - angBG23) + (180f / 16f)

    // BG4 (z=20) sobre c2 hacia el cuadrante superior izquierdo profundo
    val rotBG4_axis = rot2 * 0.8f
    val angBG45 = -70f
    val radBG45 = Math.toRadians(angBG45.toDouble())
    val dBG45 = (rPaso12 + rPaso20) * escala
    val cBG5 = Offset(
        c2.x + (dBG45 * cos(radBG45).toFloat()) - p * 30f * escala,
        c2.y + (dBG45 * sin(radBG45).toFloat()) - p * 40f * escala
    )
    val rotBG5 = (angBG45 + 180f) - (12f / 20f) * (rotBG4_axis - angBG45) + (180f / 20f)

    // BG6 (z=18) sobre c3 hacia el cuadrante superior derecho profundo
    val rotBG6_axis = rot3 * 0.8f
    val angBG67 = -115f
    val radBG67 = Math.toRadians(angBG67.toDouble())
    val dBG67 = (rPaso10 + rPaso18) * escala
    val cBG7 = Offset(
        c3.x + (dBG67 * cos(radBG67).toFloat()) + p * 25f * escala,
        c3.y + (dBG67 * sin(radBG67).toFloat()) - p * 40f * escala
    )
    val rotBG7 = (angBG67 + 180f) - (10f / 18f) * (rotBG6_axis - angBG67) + (180f / 18f)

    // BG8 (z=24) sobre BG7 hacia el hueco central superior
    val angBG78 = -170f
    val radBG78 = Math.toRadians(angBG78.toDouble())
    val dBG78 = (rPaso18 + rPaso24) * escala
    val cBG8 = Offset(
        cBG7.x + (dBG78 * cos(radBG78).toFloat()),
        cBG7.y + (dBG78 * sin(radBG78).toFloat()) - p * 60f * escala
    )
    val rotBG8 = (angBG78 + 180f) - (18f / 24f) * (rotBG7 - angBG78) + (180f / 24f)

    // BG9 (z=16) entre c4 y c5 en el flanco izquierdo bajo
    val angBG49 = 115f
    val radBG49 = Math.toRadians(angBG49.toDouble())
    val dBG49 = (rPaso10 + rPaso16) * escala
    val cBG9 = Offset(
        c4.x + (dBG49 * cos(radBG49).toFloat()) - p * 45f * escala,
        c4.y + (dBG49 * sin(radBG49).toFloat()) + p * 10f * escala
    )
    val rotBG9 = (angBG49 + 180f) - (10f / 16f) * (rot4 - angBG49) + (180f / 16f)

    // Capa 0 de Fondo: segundo nivel profundo, más oscuros
    dibujarRuedaDentada(centro = cBG8, radioPaso = rPaso24 * escala, rotacionDeg = rotBG8, path = pathZ24, escala = escala, alpha = alpha, numRadios = 6, config = config, esFondo = true)
    dibujarRuedaDentada(centro = cBG5, radioPaso = rPaso20 * escala, rotacionDeg = rotBG5, path = pathZ20, escala = escala, alpha = alpha, numRadios = 5, config = config, esFondo = true)
    dibujarRuedaDentada(centro = cBG7, radioPaso = rPaso18 * escala, rotacionDeg = rotBG7, path = pathZ18, escala = escala, alpha = alpha, numRadios = 5, config = config, esFondo = true)
    dibujarRuedaDentada(centro = cBG2, radioPaso = rPaso18 * escala, rotacionDeg = rotBG2, path = pathZ18, escala = escala, alpha = alpha, numRadios = 5, config = config, esFondo = true)
    dibujarRuedaDentada(centro = cBG3, radioPaso = rPaso16 * escala, rotacionDeg = rotBG3, path = pathZ16, escala = escala, alpha = alpha, numRadios = 4, config = config, esFondo = true)
    dibujarRuedaDentada(centro = cBG9, radioPaso = rPaso16 * escala, rotacionDeg = rotBG9, path = pathZ16, escala = escala, alpha = alpha, numRadios = 4, config = config, esFondo = true)
    dibujarRuedaDentada(centro = cBG1, radioPaso = rPaso14 * escala, rotacionDeg = rotBG1, path = pathZ14, escala = escala, alpha = alpha, numRadios = 4, config = config, esFondo = true)
}

private const val rPaso12 = 29.33f
private const val rPaso10 = 24.44f
