package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.math.cos
import kotlin.math.sin

/**
 * Algoritmos trigonométricos de generación vectorial para dientes y calados de radios mecánicos.
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

fun oscurecerColor(color: Color, factor: Float): Color {
    return Color(
        red = (color.red * factor).coerceIn(0f, 1f),
        green = (color.green * factor).coerceIn(0f, 1f),
        blue = (color.blue * factor).coerceIn(0f, 1f),
        alpha = color.alpha
    )
}

fun crearPathsVentanas(
    centro: Offset,
    rHub: Float,
    rRim: Float,
    numRadios: Int,
    radioPaso: Float,
    grosorRadios: Float,
    curvatura: Float
): List<Path> {
    if (numRadios <= 0 || grosorRadios <= 0f || rRim <= rHub + 6f) {
        return emptyList()
    }
    val lista = ArrayList<Path>(numRadios)
    val pasoAngular = (2.0 * Math.PI / numRadios).toFloat()
    val rMid = (rHub + rRim) * 0.5f

    // Ancho esbelto y equilibrado del radio en su zona central
    val anchoRadio = (radioPaso * 0.065f * grosorRadios).coerceAtLeast(1.5f)
    val semiAnguloRadio = anchoRadio / (2f * rMid)
    val semiAnguloVentana = pasoAngular * 0.5f - semiAnguloRadio
    if (semiAnguloVentana < 0.02f) return emptyList()

    // Filete / radio de curvatura en los extremos donde el radio se une a la llanta y al cubo central
    val rFillet = anchoRadio * 0.85f * curvatura.coerceIn(0f, 1f)
    val dr = rFillet.coerceAtMost((rRim - rHub) * 0.35f)
    val dphiIn = (rFillet / rHub).coerceAtMost(semiAnguloVentana * 0.40f)
    val dphiOut = (rFillet / rRim).coerceAtMost(semiAnguloVentana * 0.40f)

    for (k in 0 until numRadios) {
        val angCentro = (k + 0.5f) * pasoAngular
        val phi1 = angCentro - semiAnguloVentana
        val phi2 = angCentro + semiAnguloVentana

        val p = Path()

        // 1. Arco interior en rHub
        val a1x = centro.x + rHub * cos(phi1 + dphiIn)
        val a1y = centro.y + rHub * sin(phi1 + dphiIn)
        p.moveTo(a1x, a1y)

        val pasosArco = 6
        val aIniIn = phi1 + dphiIn
        val aFinIn = phi2 - dphiIn
        for (s in 1..pasosArco) {
            val a = aIniIn + (aFinIn - aIniIn) * (s.toFloat() / pasosArco)
            p.lineTo(
                centro.x + rHub * cos(a),
                centro.y + rHub * sin(a)
            )
        }

        // 2. Esquina interior-derecha con curvatura tangencial
        val c1x = centro.x + rHub * cos(phi2)
        val c1y = centro.y + rHub * sin(phi2)
        val b1x = centro.x + (rHub + dr) * cos(phi2)
        val b1y = centro.y + (rHub + dr) * sin(phi2)
        p.quadraticTo(c1x, c1y, b1x, b1y)

        // 3. Arista radial derecha
        val b2x = centro.x + (rRim - dr) * cos(phi2)
        val b2y = centro.y + (rRim - dr) * sin(phi2)
        p.lineTo(b2x, b2y)

        // 4. Esquina exterior-derecha
        val c2x = centro.x + rRim * cos(phi2)
        val c2y = centro.y + rRim * sin(phi2)
        val d2x = centro.x + rRim * cos(phi2 - dphiOut)
        val d2y = centro.y + rRim * sin(phi2 - dphiOut)
        p.quadraticTo(c2x, c2y, d2x, d2y)

        // 5. Arco exterior en rRim
        val aIniOut = phi2 - dphiOut
        val aFinOut = phi1 + dphiOut
        for (s in 1..pasosArco) {
            val a = aIniOut + (aFinOut - aIniOut) * (s.toFloat() / pasosArco)
            p.lineTo(
                centro.x + rRim * cos(a),
                centro.y + rRim * sin(a)
            )
        }

        // 6. Esquina exterior-izquierda
        val c3x = centro.x + rRim * cos(phi1)
        val c3y = centro.y + rRim * sin(phi1)
        val e2x = centro.x + (rRim - dr) * cos(phi1)
        val e2y = centro.y + (rRim - dr) * sin(phi1)
        p.quadraticTo(c3x, c3y, e2x, e2y)

        // 7. Arista radial izquierda
        val e1x = centro.x + (rHub + dr) * cos(phi1)
        val e1y = centro.y + (rHub + dr) * sin(phi1)
        p.lineTo(e1x, e1y)

        // 8. Esquina interior-izquierda
        val c4x = centro.x + rHub * cos(phi1)
        val c4y = centro.y + rHub * sin(phi1)
        p.quadraticTo(c4x, c4y, a1x, a1y)

        p.close()
        lista.add(p)
    }
    return lista
}
