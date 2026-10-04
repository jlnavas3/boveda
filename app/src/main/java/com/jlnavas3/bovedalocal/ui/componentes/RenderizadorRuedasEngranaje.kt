package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin

/**
 * Renderizador de una rueda dentada individual con iluminación espacial,
 * relieves mecánicos, sombras proyectadas y acabados metálicos.
 */
fun DrawScope.dibujarRuedaDentada(
    centro: Offset,
    radioPaso: Float,
    rotacionDeg: Float,
    path: Path,
    escala: Float,
    alpha: Float,
    numRadios: Int = 4,
    config: EngranajesConfig,
    esFondo: Boolean = false
) {
    // Número efectivo de radios (si el usuario eligió un valor global de 3 a 12, se aplica a todas las ruedas que tengan radios)
    val nRadiosEfectivos = if (numRadios == 0) 0 else if (config.cantidadRadios in 3..12) config.cantidadRadios else numRadios

    // Dimensiones mecánicas: corona exterior, llanta interior y cubo central
    val rBisel = radioPaso * config.radioInterior.coerceIn(0.1f, 0.95f)
    val rHub = (radioPaso * 0.24f * config.tamanoEje).coerceIn(radioPaso * 0.1f, rBisel * 0.85f)
    val rRim = (rBisel - 1.5f * escala).coerceAtLeast(rHub + 6f * escala)

    // Ventanas caladas entre radios para el cuerpo del engranaje
    val ventanasCuerpo = if (nRadiosEfectivos > 0 && config.grosorRadios > 0f) {
        crearPathsVentanas(
            centro = centro,
            rHub = rHub,
            rRim = rRim,
            numRadios = nRadiosEfectivos,
            radioPaso = radioPaso,
            grosorRadios = config.grosorRadios,
            curvatura = config.curvaturaRadios
        )
    } else {
        emptyList()
    }

    // 1. SOMBRA RELOJERA PROYECTADA CON DIRECCIÓN FIJA (135° hacia abajo y a la derecha)
    if (!esFondo && config.sombraIntensidad > 0f) {
        val distSombra = 6.5f * escala
        val centroSombra = Offset(centro.x + distSombra, centro.y + distSombra * 1.25f)
        val colorSombra = Color(0xFF000000).copy(alpha = alpha * 0.70f * config.sombraIntensidad)
        val ventanasSombra = if (nRadiosEfectivos > 0 && config.grosorRadios > 0f) {
            crearPathsVentanas(
                centro = centroSombra,
                rHub = rHub,
                rRim = rRim,
                numRadios = nRadiosEfectivos,
                radioPaso = radioPaso,
                grosorRadios = config.grosorRadios,
                curvatura = config.curvaturaRadios
            )
        } else {
            emptyList()
        }
        rotate(degrees = rotacionDeg, pivot = centroSombra) {
            val pathSombraDientes = Path().apply {
                addPath(path)
                Matrix().let { m ->
                    m.translate(centroSombra.x, centroSombra.y)
                    m.scale(escala, escala)
                    transform(m)
                }
            }
            val pathSombraCuerpo = if (ventanasSombra.isNotEmpty()) {
                Path().apply {
                    fillType = PathFillType.EvenOdd
                    addPath(pathSombraDientes)
                    for (v in ventanasSombra) {
                        addPath(v)
                    }
                }
            } else {
                pathSombraDientes
            }
            drawPath(path = pathSombraCuerpo, color = colorSombra)
        }
    }

    // Colores 100% opacos
    val factorLuzFondo = 0.50f
    val colorBrilloActual = if (esFondo) oscurecerColor(config.colorPrincipal, 0.60f) else config.colorBrillo
    val colorPrincipalActual = if (esFondo) oscurecerColor(config.colorPrincipal, factorLuzFondo) else config.colorPrincipal
    val colorSombraMedioActual = if (esFondo) oscurecerColor(config.colorSombraMedio, factorLuzFondo) else config.colorSombraMedio
    val colorSombraOscuroActual = if (esFondo) oscurecerColor(config.colorSombraOscuro, factorLuzFondo) else config.colorSombraOscuro
    val colorBiselActual = if (esFondo) oscurecerColor(config.colorSombraMedio, factorLuzFondo) else config.colorBisel

    // 2. CORONA EXTERIOR, DIENTES, RADIOS Y CUERPO MONOLÍTICO
    rotate(degrees = rotacionDeg, pivot = centro) {
        val pathDientes = Path().apply {
            addPath(path)
            Matrix().let { m ->
                m.translate(centro.x, centro.y)
                m.scale(escala, escala)
                transform(m)
            }
        }

        val pathCuerpo = if (ventanasCuerpo.isNotEmpty()) {
            Path().apply {
                fillType = PathFillType.EvenOdd
                addPath(pathDientes)
                for (v in ventanasCuerpo) {
                    addPath(v)
                }
            }
        } else {
            pathDientes
        }

        // Fuente de luz espacial fija en el cuadrante superior-izquierdo (-135° en pantalla)
        val angLuzRad = Math.toRadians(-135.0 - rotacionDeg)
        val offsetLuz = radioPaso * 0.35f
        val centroLuz = Offset(
            centro.x + (offsetLuz * cos(angLuzRad).toFloat()),
            centro.y + (offsetLuz * sin(angLuzRad).toFloat())
        )

        // Cuerpo dentado completo con degradado continuo
        drawPath(
            path = pathCuerpo,
            brush = Brush.radialGradient(
                colors = listOf(
                    colorBrilloActual.copy(alpha = colorBrilloActual.alpha * alpha),
                    colorPrincipalActual.copy(alpha = colorPrincipalActual.alpha * alpha),
                    colorSombraMedioActual.copy(alpha = colorSombraMedioActual.alpha * alpha),
                    colorSombraOscuroActual.copy(alpha = colorSombraOscuroActual.alpha * alpha)
                ),
                center = centroLuz,
                radius = radioPaso * 1.35f
            )
        )

        // Bisel exterior de los dientes
        if (config.grosorBorde > 0f) {
            drawPath(
                path = pathDientes,
                color = colorBiselActual.copy(alpha = colorBiselActual.alpha * alpha),
                style = Stroke(width = (if (esFondo) config.grosorBorde * 0.8f else config.grosorBorde) * escala)
            )
        }

        // Cavidad interior
        val colorInteriorVal = config.colorInterior
        val esInteriorTransparente = colorInteriorVal.alpha <= 0.05f || colorInteriorVal == Color(0xFF19181C)
        if (!esInteriorTransparente && ventanasCuerpo.isNotEmpty()) {
            val colorIntFinal = (if (esFondo) oscurecerColor(colorInteriorVal, factorLuzFondo) else colorInteriorVal)
                .copy(alpha = colorInteriorVal.alpha * alpha)
            for (v in ventanasCuerpo) {
                drawPath(path = v, color = colorIntFinal)
            }
        }

        // 3. BISEL Y RELIEVE EN LAS VENTANAS CALADAS
        if (config.grosorBorde > 0f && ventanasCuerpo.isNotEmpty()) {
            val anchoBordeVentana = (if (esFondo) config.grosorBorde * 0.75f else config.grosorBorde * 0.90f) * escala
            for (v in ventanasCuerpo) {
                drawPath(
                    path = v,
                    color = colorBiselActual.copy(alpha = colorBiselActual.alpha * alpha),
                    style = Stroke(width = anchoBordeVentana)
                )
            }
        }

        // 4. CUBO CENTRAL / REMACHE Y EJE
        if (config.grosorBorde > 0f) {
            drawCircle(
                color = colorBiselActual.copy(alpha = colorBiselActual.alpha * alpha),
                radius = rHub,
                center = centro,
                style = Stroke(width = (config.grosorBorde * 1.10f) * escala)
            )
        }

        // Eje central con muesca metálica
        val colorEjeActual = if (esFondo) oscurecerColor(config.colorEje, factorLuzFondo) else config.colorEje
        val rEje = (rHub * 0.42f).coerceAtLeast(2f * escala)
        drawCircle(
            color = colorEjeActual.copy(alpha = colorEjeActual.alpha * alpha),
            radius = rEje,
            center = centro
        )
        if (config.grosorBorde > 0f) {
            drawLine(
                color = colorBiselActual.copy(alpha = colorBiselActual.alpha * alpha * 0.75f),
                start = Offset(centro.x - rEje * 0.7f, centro.y),
                end = Offset(centro.x + rEje * 0.7f, centro.y),
                strokeWidth = (config.grosorBorde * 1.05f) * escala,
                cap = StrokeCap.Round
            )
        }
    }
}
