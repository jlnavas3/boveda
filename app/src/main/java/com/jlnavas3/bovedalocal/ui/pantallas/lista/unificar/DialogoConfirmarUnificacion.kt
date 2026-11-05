/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.unificar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMerge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AnalizadorDiscrepanciasUnificacion
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Diálogo modal de confirmación inteligente antes de fusionar dos o más entradas.
 * Permite escoger la entrada principal e inspecciona discrepancias de servicios, usuarios,
 * contraseñas y factores de autenticación.
 */
@Composable
fun DialogoConfirmarUnificacion(
    entradas: List<Entrada>,
    alConfirmar: (principal: Entrada, secundarias: List<Entrada>) -> Unit,
    alDescartar: () -> Unit
) {
    if (entradas.size < 2) return

    var entradaPrincipal by remember(entradas) {
        mutableStateOf(AnalizadorDuplicados.seleccionarMejorEntrada(entradas))
    }

    val discrepancias = remember(entradas, entradaPrincipal) {
        AnalizadorDiscrepanciasUnificacion.analizar(entradas, entradaPrincipal)
    }

    val textoBotonConfirmar = if (discrepancias.hayServiciosDiferentes) {
        "Unificar de todos modos"
    } else {
        "Unificar"
    }

    val varianteBotonConfirmar = if (discrepancias.hayServiciosDiferentes) {
        VarianteBoton.PELIGRO
    } else {
        VarianteBoton.PRIMARIO
    }

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Unificar seleccionados",
        icono = Icons.AutoMirrored.Filled.CallMerge,
        colorIcono = ColorIconosInternos,
        botonConfirmar = {
            BotonBoveda(
                texto = textoBotonConfirmar,
                variante = varianteBotonConfirmar,
                alPulsar = {
                    val secundarias = entradas.filterNot { it.id == entradaPrincipal.id }
                    alConfirmar(entradaPrincipal, secundarias)
                }
            )
        },
        botonDescartar = {
            BotonBoveda(
                texto = "Cancelar",
                variante = VarianteBoton.SECUNDARIO,
                alPulsar = alDescartar
            )
        }
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 420.dp)
                .verticalScroll(scrollState)
        ) {
            CabeceraDialogoUnificar(cantidad = entradas.size)
            Spacer(Modifier.height(12.dp))
            SelectorEntradaPrincipalUnificar(
                entradas = entradas,
                entradaPrincipal = entradaPrincipal,
                alSeleccionarPrincipal = { entradaPrincipal = it }
            )
            Spacer(Modifier.height(12.dp))
            BloqueAdvertenciasUnificacion(
                discrepancias = discrepancias,
                principal = entradaPrincipal
            )
        }
    }
}
