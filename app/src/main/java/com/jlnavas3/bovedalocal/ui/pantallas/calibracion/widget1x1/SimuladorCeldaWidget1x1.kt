package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SimuladorCeldaWidget1x1(
    ajustes: AjustesApp,
    colorBorde: Color,
    colorIcono: Color,
    colorFondo: Color,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    val alineacionPreview = when (ajustes.widget1x1Alineamiento.lowercase()) {
        "abajo" -> Alignment.BottomCenter
        "centro" -> Alignment.Center
        "izquierda" -> Alignment.CenterStart
        "derecha" -> Alignment.CenterEnd
        else -> Alignment.TopCenter
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF101216))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        // Marco simulador de la celda 1x1 del launcher (116x116 dp)
        Box(
            modifier = Modifier
                .size(116.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF17191E))
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                .padding(4.dp),
            contentAlignment = alineacionPreview
        ) {
            val forma1x1 = RoundedCornerShape(ajustes.widget1x1CurvaturaEsquinasDp.dp)
            val colorFondo1x1 = colorFondo.copy(alpha = ajustes.widget1x1TransparenciaFondo.coerceIn(0f, 1f))
            val grosor1x1Dp = ajustes.widget1x1GrosorBordeDp.dp
            val ancho1x1Dp = ajustes.widget1x1AnchoDp.dp
            val alto1x1Dp = ajustes.widget1x1AltoDp.dp
            val minDimDp = minOf(ajustes.widget1x1AnchoDp, ajustes.widget1x1AltoDp).dp

            Box(
                modifier = Modifier
                    .offset(x = ajustes.widget1x1OffsetX.dp, y = ajustes.widget1x1OffsetY.dp)
                    .size(width = ancho1x1Dp, height = alto1x1Dp)
                    .clip(forma1x1)
                    .background(colorFondo1x1)
                    .then(
                        if (ajustes.widget1x1GrosorBordeDp > 0.1f) {
                            Modifier.border(
                                grosor1x1Dp,
                                colorBorde.copy(alpha = (ajustes.widget1x1TransparenciaFondo.coerceAtLeast(0.6f))),
                                forma1x1
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickable {
                        haptica.probar(ajustes.widget1x1HapticaIntensidad)
                        val clave = when (ajustes.widget1x1Modo) {
                            "patron" -> PasswordGenerator.generarPorPatron(ajustes.widget1x1Patron)
                            "diceware" -> PasswordGenerator.generarFrase(
                                numeroPalabras = ajustes.widget1x1DicewarePalabras,
                                separador = ajustes.widget1x1DicewareSeparador
                            )
                            else -> PasswordGenerator.generarAleatoria(
                                OpcionesGenerador(
                                    longitud = ajustes.widget1x1Longitud,
                                    mayusculas = true,
                                    minusculas = true,
                                    digitos = true,
                                    simbolos = true,
                                    simbolosPersonalizados = ajustes.widget1x1Simbolos
                                )
                            )
                        }
                        Toast.makeText(contexto, "Clave: $clave", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Key,
                    contentDescription = "Generador Rápido 1x1",
                    tint = colorIcono,
                    modifier = Modifier.size((minDimDp * 0.52f).coerceAtLeast(16.dp))
                )
            }
        }
    }
}
