/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.obtenerEngranajesColor
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.aHexConAlfa
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

data class InfoParteEngranaje(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val colorActual: Color,
    val colorPorDefecto: Color,
    val mutador: (Color) -> Unit
)

fun crearPartesEngranajes(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    esOscuro: Boolean = true
): List<InfoParteEngranaje> {
    val defBrillo = if (esOscuro) {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_BRILLO, Color(0xFF43433F))
    } else {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_BRILLO_CLARO, Color(0xFFF3F2EB))
    }
    val defPrincipal = if (esOscuro) {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_PRINCIPAL, Color(0xFF37393A))
    } else {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_PRINCIPAL_CLARO, Color(0xFFF2F1E6))
    }
    val defSombraMedio = if (esOscuro) {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_MEDIO, Color(0xFF42423D))
    } else {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_MEDIO_CLARO, Color(0xFFF1F1EA))
    }
    val defSombraOscuro = if (esOscuro) {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_OSCURO, Color(0xFF2D2D2B))
    } else {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_OSCURO_CLARO, Color(0xFFEDEDE1))
    }
    val defBisel = if (esOscuro) {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_BISEL, Color(0xFF545450))
    } else {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_BISEL_CLARO, Color(0xFFCAC9C0))
    }
    val defInterior = if (esOscuro) {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_INTERIOR, Color(0x00000000))
    } else {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_INTERIOR_CLARO, Color(0x00000000))
    }
    val defCubo = if (esOscuro) {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_CUBO, Color(0xFFD3D1C8))
    } else {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_CUBO_CLARO, Color(0xFFFFFFF1))
    }
    val defEje = if (esOscuro) {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_EJE, Color(0xFF363630))
    } else {
        parsearColorO(AjustesDefaults.Animacion.Engranajes.COLOR_EJE_CLARO, Color(0xFFFFFDF2))
    }

    return listOf(
        InfoParteEngranaje(
            id = "brillo",
            nombre = "Cuerpo: Brillo superior",
            descripcion = "Reflejo metálico en la parte superior del engranaje",
            colorActual = parsearColorO(ajustes.obtenerEngranajesColor("brillo", esOscuro), defBrillo),
            colorPorDefecto = defBrillo,
            mutador = { vm.ajustarEngranajesColor("brillo", it.aHexConAlfa(), esOscuro) }
        ),
        InfoParteEngranaje(
            id = "principal",
            nombre = "Cuerpo: Color principal",
            descripcion = "Tono base dominante del cuerpo de los dientes",
            colorActual = parsearColorO(ajustes.obtenerEngranajesColor("principal", esOscuro), defPrincipal),
            colorPorDefecto = defPrincipal,
            mutador = { vm.ajustarEngranajesColor("principal", it.aHexConAlfa(), esOscuro) }
        ),
        InfoParteEngranaje(
            id = "sombra_medio",
            nombre = "Cuerpo: Sombra degradado",
            descripcion = "Tono intermedio del degradado radial",
            colorActual = parsearColorO(ajustes.obtenerEngranajesColor("sombra_medio", esOscuro), defSombraMedio),
            colorPorDefecto = defSombraMedio,
            mutador = { vm.ajustarEngranajesColor("sombra_medio", it.aHexConAlfa(), esOscuro) }
        ),
        InfoParteEngranaje(
            id = "sombra_oscuro",
            nombre = "Cuerpo: Sombra profunda",
            descripcion = "Relieve y sombra inferior del cuerpo metálico",
            colorActual = parsearColorO(ajustes.obtenerEngranajesColor("sombra_oscuro", esOscuro), defSombraOscuro),
            colorPorDefecto = defSombraOscuro,
            mutador = { vm.ajustarEngranajesColor("sombra_oscuro", it.aHexConAlfa(), esOscuro) }
        ),
        InfoParteEngranaje(
            id = "bisel",
            nombre = "Bordes y biseles",
            descripcion = "Líneas de contorno, bisel de dientes y remaches",
            colorActual = parsearColorO(ajustes.obtenerEngranajesColor("bisel", esOscuro), defBisel),
            colorPorDefecto = defBisel,
            mutador = { vm.ajustarEngranajesColor("bisel", it.aHexConAlfa(), esOscuro) }
        ),
        InfoParteEngranaje(
            id = "interior",
            nombre = "Fondo cavidad interior",
            descripcion = "Fondo de hendidura (transparente para ver a través de las ruedas)",
            colorActual = parsearColorO(ajustes.obtenerEngranajesColor("interior", esOscuro), defInterior),
            colorPorDefecto = defInterior,
            mutador = { vm.ajustarEngranajesColor("interior", it.aHexConAlfa(), esOscuro) }
        ),
        InfoParteEngranaje(
            id = "cubo",
            nombre = "Cubo central (remache)",
            descripcion = "Centro de rotación del engranaje",
            colorActual = parsearColorO(ajustes.obtenerEngranajesColor("cubo", esOscuro), defCubo),
            colorPorDefecto = defCubo,
            mutador = { vm.ajustarEngranajesColor("cubo", it.aHexConAlfa(), esOscuro) }
        ),
        InfoParteEngranaje(
            id = "eje",
            nombre = "Eje central (núcleo)",
            descripcion = "Orificio interior y ranura del eje de giro",
            colorActual = parsearColorO(ajustes.obtenerEngranajesColor("eje", esOscuro), defEje),
            colorPorDefecto = defEje,
            mutador = { vm.ajustarEngranajesColor("eje", it.aHexConAlfa(), esOscuro) }
        )
    )
}
