/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion

import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.obtenerEngranajesColor
import com.jlnavas3.bovedalocal.data.obtenerPuertaColor
import com.jlnavas3.bovedalocal.data.obtenerPuertaColorDestello
import org.json.JSONObject

/**
 * Genera un volcado JSON formateado con todos los parámetros geométricos, físicos
 * y de colores (tanto claro como oscuro) de la animación actual.
 */
fun generarJsonConfiguracionAnimacion(ajustes: AjustesApp): String {
    val esEngranajes = ajustes.animacionDesbloqueo == "engranajes"
    val root = JSONObject()
    root.put("tipoAnimacion", ajustes.animacionDesbloqueo)

    if (esEngranajes) {
        val geometria = JSONObject().apply {
            put("velocidad", ajustes.engranajesVelocidad)
            put("grosorBorde", ajustes.engranajesGrosorBorde)
            put("alturaDientes", ajustes.engranajesAlturaDientes)
            put("anchoDientes", ajustes.engranajesAnchoDientes)
            put("grosorRadios", ajustes.engranajesGrosorRadios)
            put("curvaturaRadios", ajustes.engranajesCurvaturaRadios)
            put("cantidadRadios", ajustes.engranajesCantidadRadios)
            put("radioInterior", ajustes.engranajesRadioInterior)
            put("tamanoEje", ajustes.engranajesTamanoEje)
            put("sombraIntensidad", ajustes.engranajesSombraIntensidad)
        }
        root.put("geometria", geometria)

        val piezas = listOf("brillo", "principal", "sombra_medio", "sombra_oscuro", "bisel", "interior", "cubo", "eje")

        val coloresOscuro = JSONObject()
        piezas.forEach { p ->
            coloresOscuro.put(p, ajustes.obtenerEngranajesColor(p, esOscuro = true))
        }
        root.put("coloresOscuro", coloresOscuro)

        val coloresClaro = JSONObject()
        piezas.forEach { p ->
            coloresClaro.put(p, ajustes.obtenerEngranajesColor(p, esOscuro = false))
        }
        root.put("coloresClaro", coloresClaro)
    } else {
        val geometria = JSONObject().apply {
            put("velocidad", ajustes.puertaVelocidad)
            put("grosorAnillos", ajustes.puertaGrosorAnillos)
        }
        root.put("geometria", geometria)

        val coloresOscuro = JSONObject().apply {
            put("puertaColor", ajustes.obtenerPuertaColor(esOscuro = true))
            put("puertaColorDestello", ajustes.obtenerPuertaColorDestello(esOscuro = true))
        }
        root.put("coloresOscuro", coloresOscuro)

        val coloresClaro = JSONObject().apply {
            put("puertaColor", ajustes.obtenerPuertaColor(esOscuro = false))
            put("puertaColorDestello", ajustes.obtenerPuertaColorDestello(esOscuro = false))
        }
        root.put("coloresClaro", coloresClaro)
    }

    return root.toString(2)
}
