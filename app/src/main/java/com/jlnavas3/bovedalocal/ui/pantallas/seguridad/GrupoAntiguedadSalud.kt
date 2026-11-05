/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud

import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador

@Composable
fun GrupoAntiguedadSalud(
    umbralAntiguedadDias: Int,
    mostrarIdsAjustes: Boolean,
    alAjustarUmbral: (Int) -> Unit,
    alRestablecer: () -> Unit,
    saludUmbralPuntuacionDebil: Int = AjustesDefaults.SaludContrasenas.UMBRAL_PUNTUACION_DEBIL,
    saludLongitudMinimaRecomendada: Int = AjustesDefaults.SaludContrasenas.LONGITUD_MINIMA_RECOMENDADA,
    alAjustarSaludUmbralPuntuacionDebil: (Int) -> Unit = {},
    alAjustarSaludLongitudMinimaRecomendada: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Auditoría de contraseñas",
        icono = Icons.Filled.HealthAndSafety,
        colorIcono = ColorSalud,
        alRestablecer = alRestablecer,
        idGrupo = "01-SEG-BIO-G04",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        val opcionesUmbral = remember {
            AlmacenAjustes.OPCIONES_UMBRAL_ANTIGUEDAD.map { (valor, etiqueta) ->
                val desc = when (valor) {
                    0 -> "No alertar sobre contraseñas antiguas"
                    180 -> "Recomendado (aviso si no se renueva en 6 meses)"
                    else -> "Aviso si la contraseña supera $etiqueta de antigüedad"
                }
                OpcionSelectorModal(valor, etiqueta, etiqueta, desc, Icons.Filled.History)
            }
        }
        ComponenteSelectorModal(
            titulo = "Umbral de antigüedad",
            descripcionModal = "Tiempo límite sin actualizar antes de clasificar una contraseña como antigua en Salud",
            icono = null,
            idFila = "01-SEG-BIO-ANT",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = umbralAntiguedadDias,
            opciones = opcionesUmbral,
            alSeleccionar = alAjustarUmbral
        )

        ComponenteSeparador()

        val opcionesUmbralDebil = remember {
            listOf(
                OpcionSelectorModal(0, "Muy permisivo (0 estrellas)", "0 estrellas", "Sólo clasifica como débiles las contraseñas extremadamente triviales", Icons.Filled.Shield),
                OpcionSelectorModal(1, "Estándar recomendado (1 estrella)", "1 estrella", "Clasifica como débiles contraseñas con puntuación 0 y 1", Icons.Filled.Shield),
                OpcionSelectorModal(2, "Exigente (2 estrellas)", "2 estrellas", "Clasifica como débiles contraseñas con puntuación menor o igual a 2", Icons.Filled.Shield),
                OpcionSelectorModal(3, "Máxima exigencia (3 estrellas)", "3 estrellas", "Requiere nivel 4 de robustez para considerarse segura", Icons.Filled.Shield)
            )
        }
        ComponenteSelectorModal(
            titulo = "Exigencia para contraseñas débiles",
            descripcionModal = "Nivel de fortaleza zxcvbn por debajo del cual una contraseña se clasifica como débil en Salud",
            icono = null,
            idFila = "01-SEG-BIO-DEB",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = saludUmbralPuntuacionDebil,
            opciones = opcionesUmbralDebil,
            alSeleccionar = alAjustarSaludUmbralPuntuacionDebil
        )

        ComponenteSeparador()

        val opcionesLongitud = remember {
            AjustesDefaults.SaludContrasenas.OPCIONES_LONGITUD_MINIMA.map { len ->
                val desc = when (len) {
                    6 -> "Mínimo absoluto para cuentas no críticas"
                    8 -> "Estándar recomendado de la industria"
                    10 -> "Equilibrio sólido de seguridad"
                    12 -> "Alta seguridad para servicios esenciales"
                    16 -> "Máxima solidez contra fuerza bruta"
                    else -> "Requiere al menos $len caracteres"
                }
                OpcionSelectorModal(len, "$len caracteres", "$len caracteres", desc, Icons.Filled.Pin)
            }
        }
        ComponenteSelectorModal(
            titulo = "Longitud mínima recomendada",
            descripcionModal = "Cualquier contraseña con menos caracteres se marcará automáticamente como débil en la auditoría",
            icono = null,
            idFila = "01-SEG-BIO-MIN",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = saludLongitudMinimaRecomendada,
            opciones = opcionesLongitud,
            alSeleccionar = alAjustarSaludLongitudMinimaRecomendada
        )
    }
}
