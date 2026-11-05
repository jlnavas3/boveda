/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorDeslizamientoBoveda

/**
 * Contenedor interactivo que gestiona el gesto de deslizamiento horizontal (swipe-to-action)
 * contextualizado por [com.jlnavas3.bovedalocal.data.TipoEntrada].
 */
@Composable
fun ContenedorDeslizamientoFila(
    entrada: Entrada,
    alturaFila: Dp,
    forma: Shape,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit,
    modifier: Modifier = Modifier,
    enGrupo: Boolean = false,
    alCopiarDato: ((etiqueta: String, valor: String, sensible: Boolean) -> Unit)? = null,
    alAvisar: ((String) -> Unit)? = null,
    contenido: @Composable () -> Unit
) {
    val (accionIzquierda, accionDerecha) = remember(entrada) {
        ResolvedorAccionesDeslizamientoEntrada.resolver(
            entrada = entrada,
            alCopiar = { etiqueta, valor, sensible ->
                if (alCopiarDato != null) {
                    alCopiarDato(etiqueta, valor, sensible)
                } else {
                    if (sensible) alCopiarContrasena() else alCopiarUsuario()
                }
            },
            alAvisar = { alAvisar?.invoke(it) }
        )
    }

    ContenedorDeslizamientoBoveda(
        idItem = entrada.id,
        forma = forma,
        alturaFila = alturaFila,
        enGrupo = enGrupo,
        accionIzquierda = accionIzquierda,
        accionDerecha = accionDerecha,
        modifier = modifier,
        contenido = contenido
    )
}
