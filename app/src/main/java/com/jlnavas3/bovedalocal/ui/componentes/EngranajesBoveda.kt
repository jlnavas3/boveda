package com.jlnavas3.bovedalocal.ui.componentes

import android.os.SystemClock
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

data class EngranajesConfig(
    val velocidad: Float = 24f,
    val grosorBorde: Float = 0.7f,
    val alturaDientes: Float = 0.76f,
    val anchoDientes: Float = 1.00f,
    val grosorRadios: Float = 1.40f,
    val curvaturaRadios: Float = 1.00f,
    val cantidadRadios: Int = 6,
    val radioInterior: Float = 0.80f,
    val tamanoEje: Float = 1.23f,
    val sombraIntensidad: Float = 0.95f,
    val colorBrillo: Color = Color(0xFFABA799),
    val colorPrincipal: Color = Color(0xFF918D7E),
    val colorSombraMedio: Color = Color(0xFF635C57),
    val colorSombraOscuro: Color = Color(0xFF404038),
    val colorBisel: Color = Color(0xFFA5A19D),
    val colorInterior: Color = Color.Transparent,
    val colorCubo: Color = Color(0xFFD3D1C8),
    val colorEje: Color = Color(0xFF141316)
)

fun AjustesApp.aEngranajesConfig(): EngranajesConfig = EngranajesConfig(
    velocidad = engranajesVelocidad,
    grosorBorde = engranajesGrosorBorde,
    alturaDientes = engranajesAlturaDientes,
    anchoDientes = engranajesAnchoDientes,
    grosorRadios = engranajesGrosorRadios,
    curvaturaRadios = engranajesCurvaturaRadios,
    cantidadRadios = engranajesCantidadRadios,
    radioInterior = engranajesRadioInterior,
    tamanoEje = engranajesTamanoEje,
    sombraIntensidad = engranajesSombraIntensidad,
    colorBrillo = parsearColorO(engranajesColorBrillo, Color(0xFFABA799)),
    colorPrincipal = parsearColorO(engranajesColorPrincipal, Color(0xFF918D7E)),
    colorSombraMedio = parsearColorO(engranajesColorSombraMedio, Color(0xFF635C57)),
    colorSombraOscuro = parsearColorO(engranajesColorSombraOscuro, Color(0xFF404038)),
    colorBisel = parsearColorO(engranajesColorBisel, Color(0xFFA5A19D)),
    colorInterior = parsearColorO(engranajesColorInterior, Color.Transparent),
    colorCubo = parsearColorO(engranajesColorCubo, Color(0xFFD3D1C8)),
    colorEje = parsearColorO(engranajesColorEje, Color(0xFF141316))
)

/** Engranajes mecánicos de bóveda / relojería: tren de ruedas dentadas acopladas matemáticamente en Canvas. */
@Composable
fun EngranajesBoveda(
    abierta: Boolean,
    modifier: Modifier = Modifier,
    tamano: Int? = null,
    config: EngranajesConfig = EngranajesConfig()
) {
    var progreso by remember { mutableFloatStateOf(0f) }
    var tiempoSegundos by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val inicio = SystemClock.uptimeMillis()
        while (isActive) {
            withFrameMillis { frameTime ->
                tiempoSegundos = (frameTime - inicio) / 1000f
            }
        }
    }

    LaunchedEffect(abierta) {
        if (abierta) {
            val inicio = SystemClock.uptimeMillis()
            val duracion = 500f
            while (isActive) {
                withFrameMillis { ahora ->
                    val t = ((ahora - inicio) / duracion).coerceIn(0f, 1f)
                    val factor = 1f - t
                    progreso = 1f - factor * factor * factor
                }
                if (progreso >= 1f) break
            }
        } else {
            progreso = 0f
        }
    }

    // Geometría canónica de radios de paso según módulo constante m = 4.888889f
    val rPaso24 = 58.67f
    val rPaso20 = 48.89f
    val rPaso18 = 44.00f
    val rPaso16 = 39.11f
    val rPaso14 = 34.22f
    val rPaso12 = 29.33f
    val rPaso10 = 24.44f
    val rPasoCore = 17.00f

    val pathZ24 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso24, 24, config.alturaDientes, config.anchoDientes) }
    val pathZ20 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso20, 20, config.alturaDientes, config.anchoDientes) }
    val pathZ18 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso18, 18, config.alturaDientes, config.anchoDientes) }
    val pathZ16 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso16, 16, config.alturaDientes, config.anchoDientes) }
    val pathZ14 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso14, 14, config.alturaDientes, config.anchoDientes) }
    val pathZ12 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso12, 12, config.alturaDientes, config.anchoDientes) }
    val pathZ10 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso10, 10, config.alturaDientes, config.anchoDientes) }
    val pathCore = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPasoCore, 10, config.alturaDientes, config.anchoDientes) }

    val boxModifier = if (tamano != null) modifier.size(tamano.dp) else modifier

    Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
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
            // CAPA PROFUNDA / SUBTERRÁNEA (Engranajes de fondo tipo mecanismo de reloj)
            // =========================================================================
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

            // --- RENDERIZADO POR CAPAS DE PROFUNDIDAD (Z-ORDER RELOJERO) ---

            // =========================================================================
            // CAPA 0: ENGRANAJES DE FONDO (Segundo nivel profundo, más oscuros)
            // =========================================================================
            dibujarRuedaDentada(centro = cBG8, radioPaso = rPaso24 * escala, rotacionDeg = rotBG8, path = pathZ24, escala = escala, alpha = alpha, numRadios = 6, config = config, esFondo = true)
            dibujarRuedaDentada(centro = cBG5, radioPaso = rPaso20 * escala, rotacionDeg = rotBG5, path = pathZ20, escala = escala, alpha = alpha, numRadios = 5, config = config, esFondo = true)
            dibujarRuedaDentada(centro = cBG7, radioPaso = rPaso18 * escala, rotacionDeg = rotBG7, path = pathZ18, escala = escala, alpha = alpha, numRadios = 5, config = config, esFondo = true)
            dibujarRuedaDentada(centro = cBG2, radioPaso = rPaso18 * escala, rotacionDeg = rotBG2, path = pathZ18, escala = escala, alpha = alpha, numRadios = 5, config = config, esFondo = true)
            dibujarRuedaDentada(centro = cBG3, radioPaso = rPaso16 * escala, rotacionDeg = rotBG3, path = pathZ16, escala = escala, alpha = alpha, numRadios = 4, config = config, esFondo = true)
            dibujarRuedaDentada(centro = cBG9, radioPaso = rPaso16 * escala, rotacionDeg = rotBG9, path = pathZ16, escala = escala, alpha = alpha, numRadios = 4, config = config, esFondo = true)
            dibujarRuedaDentada(centro = cBG1, radioPaso = rPaso14 * escala, rotacionDeg = rotBG1, path = pathZ14, escala = escala, alpha = alpha, numRadios = 4, config = config, esFondo = true)

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
    }
}

private fun crearPathEngranaje(
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

private fun oscurecerColor(color: Color, factor: Float): Color {
    return Color(
        red = (color.red * factor).coerceIn(0f, 1f),
        green = (color.green * factor).coerceIn(0f, 1f),
        blue = (color.blue * factor).coerceIn(0f, 1f),
        alpha = color.alpha
    )
}

private fun crearPathsVentanas(
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

private fun DrawScope.dibujarRuedaDentada(
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

    // Colores 100% opacos (evitamos transparencias tipo fantasma; oscurecemos los canales RGB)
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

        // Si tiene ventanas caladas, calamos con EvenOdd para que el fondo se vea a través de los huecos redondeados
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

        // Cavidad interior: SOLO si el usuario configuró explícitamente un color opaco no predeterminado
        val colorInteriorVal = config.colorInterior
        val esInteriorTransparente = colorInteriorVal.alpha <= 0.05f || colorInteriorVal == Color(0xFF19181C)
        if (!esInteriorTransparente && ventanasCuerpo.isNotEmpty()) {
            val colorIntFinal = (if (esFondo) oscurecerColor(colorInteriorVal, factorLuzFondo) else colorInteriorVal)
                .copy(alpha = colorInteriorVal.alpha * alpha)
            for (v in ventanasCuerpo) {
                drawPath(path = v, color = colorIntFinal)
            }
        }

        // 3. BISEL Y RELIEVE EN LAS VENTANAS CALADAS (Bordes redondeados mecánicos tipo reloj)
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
