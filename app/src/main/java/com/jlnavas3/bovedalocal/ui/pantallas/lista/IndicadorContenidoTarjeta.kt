package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.AlturaIndicadores
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.OpacidadIndicadores
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

private data class SegmentoIndicador(
    val activo: Boolean,
    val color: Color
)

/**
 * Muestra una barra horizontal superior o fondo segmentado dividido en 6 segmentos de color
 * que representan los datos presentes en una entrada (Usuario, Contraseña, 2FA, Passkey, Web, App).
 * Optimizado para dibujar directamente en GPU vía Canvas reduciendo la jerarquía a un único LayoutNode.
 */
@Composable
fun IndicadorContenidoTarjeta(
    entrada: Entrada,
    modifier: Modifier = Modifier,
    altura: Dp = AlturaIndicadores,
    opacidad: Float = OpacidadIndicadores
) {
    val segmentos = remember(
        entrada.usuario,
        entrada.contrasena,
        entrada.secretoTotp,
        entrada.passkey,
        entrada.urls,
        ColorDatosUsuario,
        ColorDatosContrasena,
        ColorDatos2FA,
        ColorDatosPasskey,
        ColorDatosWeb,
        ColorDatosApp
    ) {
        val tieneUsuario = entrada.usuario.isNotBlank()
        val tieneContrasena = entrada.contrasena.isNotBlank()
        val tiene2FA = !entrada.secretoTotp.isNullOrBlank()
        val tienePasskey = entrada.passkey != null

        var tieneWeb = false
        var tieneApp = false
        for (u in entrada.urls) {
            if (tieneWeb && tieneApp) break
            val paquete = LanzadorEnlaces.extraerPaquete(u)
            if (paquete != null) {
                tieneApp = true
            } else if (u.contains(".") || u.startsWith("http", ignoreCase = true)) {
                tieneWeb = true
            }
        }

        listOf(
            SegmentoIndicador(tieneUsuario, ColorDatosUsuario),
            SegmentoIndicador(tieneContrasena, ColorDatosContrasena),
            SegmentoIndicador(tiene2FA, ColorDatos2FA),
            SegmentoIndicador(tienePasskey, ColorDatosPasskey),
            SegmentoIndicador(tieneWeb, ColorDatosWeb),
            SegmentoIndicador(tieneApp, ColorDatosApp)
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(altura)
    ) {
        val espaciadoPx = 2.dp.toPx()
        val maxCornerRadiusPx = if (size.height > 8.dp.toPx()) 2.dp.toPx() else 1.dp.toPx()
        val cornerRadiusPx = (size.height / 2f).coerceAtMost(maxCornerRadiusPx)
        val numSegmentos = segmentos.size
        val anchoSegmento = (size.width - (espaciadoPx * (numSegmentos - 1))) / numSegmentos
        val cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)

        var xOffset = 0f
        val alfaEfectivo = opacidad.coerceIn(0.01f, 1f)
        for (seg in segmentos) {
            if (seg.activo) {
                drawRoundRect(
                    color = seg.color.copy(alpha = alfaEfectivo),
                    topLeft = Offset(xOffset, 0f),
                    size = Size(anchoSegmento, size.height),
                    cornerRadius = cornerRadius
                )
            }
            xOffset += anchoSegmento + espaciadoPx
        }
    }
}
