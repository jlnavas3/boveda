package com.jlnavas3.bovedalocal.ui.pantallas.tema

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.jlnavas3.bovedalocal.ui.componentes.colorAhsv
import com.jlnavas3.bovedalocal.ui.theme.aHexConAlfa
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Utilidad microgranular para serializar y copiar la paleta de colores y parámetros de diseño
 * del Laboratorio de Temas en formato JSON al portapapeles.
 */
object ExportadorPaletaLaboratorio {

    fun copiarAlPortapapeles(
        contexto: Context,
        haptica: Haptica,
        portapapeles: ClipboardManager,
        modoOscuro: Boolean,
        colorFondo: Color,
        colorTarjeta: Color,
        colorCampo: Color,
        colorBorde: Color,
        colorTextoPrincipal: Color,
        colorTextoSecundario: Color,
        colorAcentoActual: Color,
        unificarTonos: Boolean,
        tonoGlobal: Float,
        saturacionTinte: Float,
        lumFondo: Float,
        tonoEfectivoFondo: Float,
        lumTarjeta: Float,
        tonoEfectivoTarjeta: Float,
        lumCampo: Float,
        tonoEfectivoCampo: Float,
        lumBorde: Float,
        tonoEfectivoBorde: Float,
        lumTextoPrincipal: Float,
        tonoEfectivoTextoPrincipal: Float,
        lumTextoSecundario: Float,
        tonoEfectivoTextoSecundario: Float
    ) {
        haptica.exito()
        val (hAcento, sAcento, vAcento) = colorAhsv(colorAcentoActual)
        val jsonDetallado = """
        {
          "modo": "${if (modoOscuro) "oscuro" else "claro"}",
          "fondo": "${colorFondo.aHexConAlfa()}",
          "tarjeta": "${colorTarjeta.aHexConAlfa()}",
          "campo": "${colorCampo.aHexConAlfa()}",
          "borde": "${colorBorde.aHexConAlfa()}",
          "textoPrincipal": "${colorTextoPrincipal.aHexConAlfa()}",
          "textoSecundario": "${colorTextoSecundario.aHexConAlfa()}",
          "acento": "${colorAcentoActual.aHexConAlfa()}",
          "parametrosLaboratorio": {
            "unificarTonos": $unificarTonos,
            "tonoGlobal": "${tonoGlobal.toInt()}°",
            "saturacionTinteGrises": "${(saturacionTinte * 100).toInt()}%",
            "capas": {
              "fondo": {
                "hex": "${colorFondo.aHexConAlfa()}",
                "luminancia": "${(lumFondo * 100).toInt()}%",
                "tono": "${tonoEfectivoFondo.toInt()}°"
              },
              "tarjeta": {
                "hex": "${colorTarjeta.aHexConAlfa()}",
                "luminancia": "${(lumTarjeta * 100).toInt()}%",
                "tono": "${tonoEfectivoTarjeta.toInt()}°"
              },
              "campo": {
                "hex": "${colorCampo.aHexConAlfa()}",
                "luminancia": "${(lumCampo * 100).toInt()}%",
                "tono": "${tonoEfectivoCampo.toInt()}°"
              },
              "borde": {
                "hex": "${colorBorde.aHexConAlfa()}",
                "luminancia": "${(lumBorde * 100).toInt()}%",
                "tono": "${tonoEfectivoBorde.toInt()}°"
              },
              "textoPrincipal": {
                "hex": "${colorTextoPrincipal.aHexConAlfa()}",
                "luminancia": "${(lumTextoPrincipal * 100).toInt()}%",
                "tono": "${tonoEfectivoTextoPrincipal.toInt()}°"
              },
              "textoSecundario": {
                "hex": "${colorTextoSecundario.aHexConAlfa()}",
                "luminancia": "${(lumTextoSecundario * 100).toInt()}%",
                "tono": "${tonoEfectivoTextoSecundario.toInt()}°"
              }
            },
            "acento": {
              "hex": "${colorAcentoActual.aHexConAlfa()}",
              "tono": "${hAcento.toInt()}°",
              "saturacion": "${(sAcento * 100).toInt()}%",
              "brillo": "${(vAcento * 100).toInt()}%",
              "opacidad": "${(colorAcentoActual.alpha * 100).toInt()}%",
              "canalAlfa": ${colorAcentoActual.alpha}
            }
          }
        }
        """.trimIndent()
        portapapeles.setText(AnnotatedString(jsonDetallado))
        Toast.makeText(contexto, "¡Paleta y parámetros copiados! Pégala en el chat.", Toast.LENGTH_LONG).show()
    }
}
