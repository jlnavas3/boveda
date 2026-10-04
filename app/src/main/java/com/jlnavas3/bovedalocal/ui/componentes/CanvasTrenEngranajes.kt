package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Encapsula las matemáticas de acoplamiento de engranajes y la renderización en Canvas
 * ordenada por capas de profundidad (z-order relojero).
 */
fun DrawScope.dibujarTrenEngranajesMecanicos(
    tiempoSegundos: Float,
    progreso: Float,
    config: EngranajesConfig,
    pathZ24: Path,
    pathZ20: Path,
    pathZ18: Path,
    pathZ16: Path,
    pathZ14: Path,
    pathZ12: Path,
    pathZ10: Path,
    pathCore: Path,
    rPaso24: Float,
    rPaso20: Float,
    rPaso18: Float,
    rPaso16: Float,
    rPaso14: Float,
    rPaso12: Float,
    rPaso10: Float,
    rPasoCore: Float
) {
    val esPantallaCompleta = size.height > 400.dp.toPx()
    val centro = if (esPantallaCompleta) {
        Offset(size.width / 2f, size.height * 0.25f)
    } else {
        Offset(size.width / 2f, size.height * 0.52f)
    }
    val escala = if (esPantallaCompleta) {
        (size.width / 240f)
    } else {
        (size.width / 320f)
    }
    val p = progreso
    val alpha = (1f - p).coerceIn(0f, 1f)

    // 1. Motor Central (Engranaje 1, z=18)
    val c1 = Offset(
        centro.x,
        centro.y + p * 12f * escala
    )
    val rot1 = (tiempoSegundos * config.velocidad) + (p * 240f)

    // --- Rama Superior-Izquierda ---
    // G2 (z=14) sobre G1
    val ang12 = -140f
    val rad12 = Math.toRadians(ang12.toDouble())
    val d12 = (rPaso18 + rPaso14) * escala
    val c2 = Offset(
        c1.x + (d12 * cos(rad12).toFloat()) - p * 38f * escala,
        c1.y + (d12 * sin(rad12).toFloat()) - p * 32f * escala
    )
    val rot2 = (ang12 + 180f) - (18f / 14f) * (rot1 - ang12) + (180f / 14f)

    // G7 (z=18) sobre G2
    val ang27 = -110f
    val rad27 = Math.toRadians(ang27.toDouble())
    val d27 = (rPaso14 + rPaso18) * escala
    val c7 = Offset(
        c2.x + (d27 * cos(rad27).toFloat()) - p * 65f * escala,
        c2.y + (d27 * sin(rad27).toFloat()) - p * 60f * escala
    )
    val rot7 = (ang27 + 180f) - (14f / 18f) * (rot2 - ang27) + (180f / 18f)

    // G8 (z=14) sobre G7
    val ang78 = -155f
    val rad78 = Math.toRadians(ang78.toDouble())
    val d78 = (rPaso18 + rPaso14) * escala
    val c8 = Offset(
        c7.x + (d78 * cos(rad78).toFloat()) - p * 95f * escala,
        c7.y + (d78 * sin(rad78).toFloat()) - p * 80f * escala
    )
    val rot8 = (ang78 + 180f) - (18f / 14f) * (rot7 - ang78) + (180f / 14f)

    // G17 (z=20) sobre G8 (desborde extremo superior izquierdo)
    val ang817 = -135f
    val rad817 = Math.toRadians(ang817.toDouble())
    val d817 = (rPaso14 + rPaso20) * escala
    val c17 = Offset(
        c8.x + (d817 * cos(rad817).toFloat()) - p * 130f * escala,
        c8.y + (d817 * sin(rad817).toFloat()) - p * 110f * escala
    )
    val rot17 = (ang817 + 180f) - (14f / 20f) * (rot8 - ang817) + (180f / 20f)

    // --- Rama Lateral Izquierda ---
    // G4 (z=10) sobre G1
    val ang14 = -178f
    val rad14 = Math.toRadians(ang14.toDouble())
    val d14 = (rPaso18 + rPaso10) * escala
    val c4 = Offset(
        c1.x + (d14 * cos(rad14).toFloat()) - p * 40f * escala,
        c1.y + (d14 * sin(rad14).toFloat()) - p * 10f * escala
    )
    val rot4 = (ang14 + 180f) - (18f / 10f) * (rot1 - ang14) + (180f / 10f)

    // G5 (z=16) sobre G4
    val ang45 = 175f
    val rad45 = Math.toRadians(ang45.toDouble())
    val d45 = (rPaso10 + rPaso16) * escala
    val c5 = Offset(
        c4.x + (d45 * cos(rad45).toFloat()) - p * 70f * escala,
        c4.y + (d45 * sin(rad45).toFloat()) - p * 15f * escala
    )
    val rot5 = (ang45 + 180f) - (10f / 16f) * (rot4 - ang45) + (180f / 16f)

    // G6 (z=20) sobre G5
    val ang56 = -170f
    val rad56 = Math.toRadians(ang56.toDouble())
    val d56 = (rPaso16 + rPaso20) * escala
    val c6 = Offset(
        c5.x + (d56 * cos(rad56).toFloat()) - p * 110f * escala,
        c5.y + (d56 * sin(rad56).toFloat()) - p * 20f * escala
    )
    val rot6 = (ang56 + 180f) - (16f / 20f) * (rot5 - ang56) + (180f / 20f)

    // G16 (z=24) sobre G6 (desborde masivo borde lateral izquierdo)
    val ang616 = 175f
    val rad616 = Math.toRadians(ang616.toDouble())
    val d616 = (rPaso20 + rPaso24) * escala
    val c16 = Offset(
        c6.x + (d616 * cos(rad616).toFloat()) - p * 150f * escala,
        c6.y + (d616 * sin(rad616).toFloat()) - p * 25f * escala
    )
    val rot16 = (ang616 + 180f) - (20f / 24f) * (rot6 - ang616) + (180f / 24f)

    // --- Rama Superior Central ---
    // G9 (z=16) sobre G7
    val ang79 = -25f
    val rad79 = Math.toRadians(ang79.toDouble())
    val d79 = (rPaso18 + rPaso16) * escala
    val c9 = Offset(
        c7.x + (d79 * cos(rad79).toFloat()) - p * 20f * escala,
        c7.y + (d79 * sin(rad79).toFloat()) - p * 75f * escala
    )
    val rot9 = (ang79 + 180f) - (18f / 16f) * (rot7 - ang79) + (180f / 16f)

    // G10 (z=24) sobre G9 (corona superior)
    val ang910 = -85f
    val rad910 = Math.toRadians(ang910.toDouble())
    val d910 = (rPaso16 + rPaso24) * escala
    val c10 = Offset(
        c9.x + (d910 * cos(rad910).toFloat()),
        c9.y + (d910 * sin(rad910).toFloat()) - p * 120f * escala
    )
    val rot10 = (ang910 + 180f) - (16f / 24f) * (rot9 - ang910) + (180f / 24f)

    // G19 (z=20) sobre G10 (desborde masivo borde superior)
    val ang1019 = -90f
    val rad1019 = Math.toRadians(ang1019.toDouble())
    val d1019 = (rPaso24 + rPaso20) * escala
    val c19 = Offset(
        c10.x + (d1019 * cos(rad1019).toFloat()),
        c10.y + (d1019 * sin(rad1019).toFloat()) - p * 160f * escala
    )
    val rot19 = (ang1019 + 180f) - (24f / 20f) * (rot10 - ang1019) + (180f / 20f)

    // --- Rama Superior-Derecha ---
    // G3 (z=12) sobre G1
    val ang13 = -60f
    val rad13 = Math.toRadians(ang13.toDouble())
    val d13 = (rPaso18 + rPaso12) * escala
    val c3 = Offset(
        c1.x + (d13 * cos(rad13).toFloat()) + p * 38f * escala,
        c1.y + (d13 * sin(rad13).toFloat()) - p * 32f * escala
    )
    val rot3 = (ang13 + 180f) - (18f / 12f) * (rot1 - ang13) + (180f / 12f)

    // G11 (z=14) sobre G3
    val ang311 = -20f
    val rad311 = Math.toRadians(ang311.toDouble())
    val d311 = (rPaso12 + rPaso14) * escala
    val c11 = Offset(
        c3.x + (d311 * cos(rad311).toFloat()) + p * 50f * escala,
        c3.y + (d311 * sin(rad311).toFloat()) - p * 15f * escala
    )
    val rot11 = (ang311 + 180f) - (12f / 14f) * (rot3 - ang311) + (180f / 14f)

    // G12 (z=18) sobre G11
    val ang1112 = -75f
    val rad1112 = Math.toRadians(ang1112.toDouble())
    val d1112 = (rPaso14 + rPaso18) * escala
    val c12 = Offset(
        c11.x + (d1112 * cos(rad1112).toFloat()) + p * 70f * escala,
        c11.y + (d1112 * sin(rad1112).toFloat()) - p * 65f * escala
    )
    val rot12 = (ang1112 + 180f) - (14f / 18f) * (rot11 - ang1112) + (180f / 18f)

    // G13 (z=12) sobre G12
    val ang1213 = -40f
    val rad1213 = Math.toRadians(ang1213.toDouble())
    val d1213 = (rPaso18 + rPaso12) * escala
    val c13 = Offset(
        c12.x + (d1213 * cos(rad1213).toFloat()) + p * 95f * escala,
        c12.y + (d1213 * sin(rad1213).toFloat()) - p * 80f * escala
    )
    val rot13 = (ang1213 + 180f) - (18f / 12f) * (rot12 - ang1213) + (180f / 12f)

    // G18 (z=18) sobre G13 (desborde extremo superior derecho)
    val ang1318 = -35f
    val rad1318 = Math.toRadians(ang1318.toDouble())
    val d1318 = (rPaso12 + rPaso18) * escala
    val c18 = Offset(
        c13.x + (d1318 * cos(rad1318).toFloat()) + p * 130f * escala,
        c13.y + (d1318 * sin(rad1318).toFloat()) - p * 110f * escala
    )
    val rot18 = (ang1318 + 180f) - (12f / 18f) * (rot13 - ang1318) + (180f / 18f)

    // --- Rama Lateral Derecha ---
    // G14 (z=20) sobre G11
    val ang1114 = 10f
    val rad1114 = Math.toRadians(ang1114.toDouble())
    val d1114 = (rPaso14 + rPaso20) * escala
    val c14 = Offset(
        c11.x + (d1114 * cos(rad1114).toFloat()) + p * 105f * escala,
        c11.y + (d1114 * sin(rad1114).toFloat()) + p * 10f * escala
    )
    val rot14 = (ang1114 + 180f) - (14f / 20f) * (rot11 - ang1114) + (180f / 20f)

    // G15 (z=14) sobre G14
    val ang1415 = 48f
    val rad1415 = Math.toRadians(ang1415.toDouble())
    val d1415 = (rPaso20 + rPaso14) * escala
    val c15 = Offset(
        c14.x + (d1415 * cos(rad1415).toFloat()) + p * 90f * escala,
        c14.y + (d1415 * sin(rad1415).toFloat()) + p * 45f * escala
    )
    val rot15 = (ang1415 + 180f) - (20f / 14f) * (rot14 - ang1415) + (180f / 14f)

    // G20 (z=24) sobre G14 (desborde masivo borde lateral derecho)
    val ang1420 = -5f
    val rad1420 = Math.toRadians(ang1420.toDouble())
    val d1420 = (rPaso20 + rPaso24) * escala
    val c20 = Offset(
        c14.x + (d1420 * cos(rad1420).toFloat()) + p * 150f * escala,
        c14.y + (d1420 * sin(rad1420).toFloat()) + p * 15f * escala
    )
    val rot20 = (ang1420 + 180f) - (20f / 24f) * (rot14 - ang1420) + (180f / 24f)

    // =========================================================================
    // CAPA 0: ENGRANAJES DE FONDO (Segundo nivel profundo)
    // =========================================================================
    dibujarCapaFondoEngranajes(
        c1 = c1, c2 = c2, c3 = c3, c4 = c4,
        rot1 = rot1, rot2 = rot2, rot3 = rot3, rot4 = rot4,
        p = p, escala = escala, alpha = alpha, config = config,
        rPaso24 = rPaso24, rPaso20 = rPaso20, rPaso18 = rPaso18, rPaso16 = rPaso16, rPaso14 = rPaso14,
        pathZ24 = pathZ24, pathZ20 = pathZ20, pathZ18 = pathZ18, pathZ16 = pathZ16, pathZ14 = pathZ14
    )

    // =========================================================================
    // CAPA 1: ENGRANAJES PERIFÉRICOS DE DESBORDE Y CORONAS PRINCIPALES
    // =========================================================================
    dibujarRuedaDentada(centro = c19, radioPaso = rPaso20 * escala, rotacionDeg = rot19, path = pathZ20, escala = escala, alpha = alpha, numRadios = 5, config = config)
    dibujarRuedaDentada(centro = c16, radioPaso = rPaso24 * escala, rotacionDeg = rot16, path = pathZ24, escala = escala, alpha = alpha, numRadios = 6, config = config)
    dibujarRuedaDentada(centro = c20, radioPaso = rPaso24 * escala, rotacionDeg = rot20, path = pathZ24, escala = escala, alpha = alpha, numRadios = 6, config = config)
    dibujarRuedaDentada(centro = c17, radioPaso = rPaso20 * escala, rotacionDeg = rot17, path = pathZ20, escala = escala, alpha = alpha, numRadios = 5, config = config)
    dibujarRuedaDentada(centro = c18, radioPaso = rPaso18 * escala, rotacionDeg = rot18, path = pathZ18, escala = escala, alpha = alpha, numRadios = 5, config = config)

    dibujarRuedaDentada(centro = c10, radioPaso = rPaso24 * escala, rotacionDeg = rot10, path = pathZ24, escala = escala, alpha = alpha, numRadios = 6, config = config)
    dibujarRuedaDentada(centro = c6,  radioPaso = rPaso20 * escala, rotacionDeg = rot6,  path = pathZ20, escala = escala, alpha = alpha, numRadios = 5, config = config)
    dibujarRuedaDentada(centro = c14, radioPaso = rPaso20 * escala, rotacionDeg = rot14, path = pathZ20, escala = escala, alpha = alpha, numRadios = 5, config = config)
    dibujarRuedaDentada(centro = c8,  radioPaso = rPaso14 * escala, rotacionDeg = rot8,  path = pathZ14, escala = escala, alpha = alpha, numRadios = 4, config = config)
    dibujarRuedaDentada(centro = c13, radioPaso = rPaso12 * escala, rotacionDeg = rot13, path = pathZ12, escala = escala, alpha = alpha, numRadios = 4, config = config)
    dibujarRuedaDentada(centro = c15, radioPaso = rPaso14 * escala, rotacionDeg = rot15, path = pathZ14, escala = escala, alpha = alpha, numRadios = 4, config = config)
    dibujarRuedaDentada(centro = c5,  radioPaso = rPaso16 * escala, rotacionDeg = rot5,  path = pathZ16, escala = escala, alpha = alpha, numRadios = 4, config = config)
    dibujarRuedaDentada(centro = c9,  radioPaso = rPaso16 * escala, rotacionDeg = rot9,  path = pathZ16, escala = escala, alpha = alpha, numRadios = 4, config = config)
    dibujarRuedaDentada(centro = c12, radioPaso = rPaso18 * escala, rotacionDeg = rot12, path = pathZ18, escala = escala, alpha = alpha, numRadios = 5, config = config)

    // =========================================================================
    // CAPA 2: ENGRANAJES INTERMEDIOS
    // =========================================================================
    dibujarRuedaDentada(centro = c4,  radioPaso = rPaso10 * escala, rotacionDeg = rot4,  path = pathZ10, escala = escala, alpha = alpha, numRadios = 3, config = config)
    dibujarRuedaDentada(centro = c11, radioPaso = rPaso14 * escala, rotacionDeg = rot11, path = pathZ14, escala = escala, alpha = alpha, numRadios = 4, config = config)
    dibujarRuedaDentada(centro = c2,  radioPaso = rPaso14 * escala, rotacionDeg = rot2,  path = pathZ14, escala = escala, alpha = alpha, numRadios = 4, config = config)
    dibujarRuedaDentada(centro = c3,  radioPaso = rPaso12 * escala, rotacionDeg = rot3,  path = pathZ12, escala = escala, alpha = alpha, numRadios = 4, config = config)
    dibujarRuedaDentada(centro = c7,  radioPaso = rPaso18 * escala, rotacionDeg = rot7,  path = pathZ18, escala = escala, alpha = alpha, numRadios = 5, config = config)

    // =========================================================================
    // CAPA 3: RUEDA MOTRIZ CENTRAL Y PIÑONES CONCÉNTRICOS EN PRIMER PLANO
    // =========================================================================
    dibujarRuedaDentada(centro = c1,  radioPaso = rPaso18 * escala, rotacionDeg = rot1,  path = pathZ18, escala = escala, alpha = alpha, numRadios = 5, config = config)
    dibujarRuedaDentada(centro = c7,  radioPaso = rPasoCore * escala, rotacionDeg = rot7, path = pathCore, escala = escala, alpha = alpha, numRadios = 0, config = config)
    dibujarRuedaDentada(centro = c12, radioPaso = rPasoCore * escala, rotacionDeg = rot12, path = pathCore, escala = escala, alpha = alpha, numRadios = 0, config = config)
    dibujarRuedaDentada(centro = c1,  radioPaso = rPasoCore * escala, rotacionDeg = rot1,  path = pathCore, escala = escala, alpha = alpha, numRadios = 0, config = config)
}
