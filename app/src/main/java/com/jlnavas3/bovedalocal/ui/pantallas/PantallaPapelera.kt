package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.DialogoConflictoRestaurar
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.FilaPapeleraNativa
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.IlustracionPapeleraVacia
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.diasDesde
import com.jlnavas3.bovedalocal.ui.theme.Peligro

private const val DIAS_PAPELERA = 30L

@Composable
fun PantallaPapelera(
    vm: VaultViewModel,
    estado: EstadoBoveda,
    seccionDestino: String? = null
) {
    val papelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera ?: emptyList()
    val entradasActivas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val ahora = remember { System.currentTimeMillis() }
    var confirmarVaciar by remember { mutableStateOf(false) }
    var aBorrarDefinitivo by remember { mutableStateOf<Entrada?>(null) }
    var conflictoRestaurar by remember { mutableStateOf<Entrada?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Papelera",
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo,
            acciones = {
                if (papelera.isNotEmpty()) {
                    IconButton(onClick = { confirmarVaciar = true }) {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = "Vaciar papelera",
                            tint = Peligro,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (papelera.isEmpty()) "Sin elementos en la papelera" else "${papelera.size} entrada${if (papelera.size == 1) "" else "s"} · Se borran tras $DIAS_PAPELERA días"
            )

            Spacer(Modifier.height(10.dp))

            if (papelera.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IlustracionPapeleraVacia()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        val ordenadas = papelera.sortedByDescending { it.eliminadaEn }
                        GrupoAjustes(etiqueta = "Elementos eliminados (${papelera.size})") {
                            ordenadas.forEachIndexed { index, entrada ->
                                if (index > 0) SeparadorFilaSimple()
                                val diasRestantes = (DIAS_PAPELERA - diasDesde(entrada.eliminadaEn, ahora)).coerceAtLeast(0)
                                FilaPapeleraNativa(
                                    entrada = entrada,
                                    diasRestantes = diasRestantes,
                                    alRestaurar = {
                                        val conflicto = entradasActivas.find {
                                            it.id == entrada.id || (it.titulo.trim().equals(entrada.titulo.trim(), ignoreCase = true) && it.usuario.trim() == entrada.usuario.trim())
                                        }
                                        if (conflicto != null) {
                                            conflictoRestaurar = entrada
                                        } else {
                                            vm.restaurarDeLaPapelera(entrada.id)
                                        }
                                    },
                                    alBorrarDefinitivo = { aBorrarDefinitivo = entrada }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (confirmarVaciar) {
        DialogoConfirmacionBoveda(
            titulo = "¿Vaciar toda la papelera?",
            mensaje = "Se destruirán definitivamente todas las ${papelera.size} entradas. Esta acción no se puede deshacer.",
            textoConfirmar = "Vaciar definitivamente",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {
                confirmarVaciar = false
                vm.vaciarPapelera()
                vm.avisar("Papelera vaciada")
            },
            alDescartar = { confirmarVaciar = false }
        )
    }

    aBorrarDefinitivo?.let { entrada ->
        DialogoConfirmacionBoveda(
            titulo = "¿Borrar definitivamente?",
            mensaje = "Se destruirá permanentemente \"${entrada.titulo.ifBlank { "Sin título" }}\". Esta acción no se puede deshacer.",
            textoConfirmar = "Destruir",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {
                val id = entrada.id
                aBorrarDefinitivo = null
                vm.borrarDefinitivamente(id)
                vm.avisar("Entrada destruida")
            },
            alDescartar = { aBorrarDefinitivo = null }
        )
    }

    conflictoRestaurar?.let { entrada ->
        DialogoConflictoRestaurar(
            entrada = entrada,
            alCerrar = { conflictoRestaurar = null },
            alSustituir = {
                val id = entrada.id
                conflictoRestaurar = null
                vm.restaurarDeLaPapelera(id, sustituir = true)
            },
            alDuplicar = {
                val id = entrada.id
                conflictoRestaurar = null
                vm.restaurarDeLaPapelera(id, sustituir = false)
            }
        )
    }
}
