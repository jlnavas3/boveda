package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

private data class SegmentoIndicador(
    val activo: Boolean,
    val color: Color
)

/**
 * Muestra una barra horizontal superior dividida en 6 segmentos de color
 * que representan los datos presentes en una entrada (Usuario, Contraseña, 2FA, Passkey, Web, App).
 */
@Composable
fun IndicadorContenidoTarjeta(
    entrada: Entrada,
    modifier: Modifier = Modifier
) {
    val segmentos = remember(
        entrada,
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
        val urlsClasificadas = entrada.urls.map { url ->
            url to LanzadorEnlaces.extraerPaquete(url)
        }
        val tieneWeb = urlsClasificadas.any { (url, paquete) ->
            paquete == null &&
                (url.contains(".") || url.startsWith("http", ignoreCase = true))
        }
        val tieneApp = urlsClasificadas.any { (_, paquete) ->
            paquete != null
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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(3.5.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        segmentos.forEach { seg ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (seg.activo) seg.color else seg.color.copy(alpha = 0.12f))
            )
        }
    }
}
