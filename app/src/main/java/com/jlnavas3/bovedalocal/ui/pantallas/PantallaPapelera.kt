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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.DialogoConflictoRestaurar
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.FilaPapeleraNativa
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.IlustracionPapeleraVacia
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.diasDesde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorTitulo

private const val DIAS_PAPELERA = 30L

@Composable
fun PantallaPapelera(
    vm: VaultViewModel,
    estado: EstadoBoveda,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val papelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera ?: emptyList()
    val entradasActivas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val ahora = remember { System.currentTimeMillis() }
    var confirmarVaciar by remember { mutableStateOf(false) }
    var aBorrarDefinitivo by remember { mutableStateOf<Entrada?>(null) }
    var conflictoRestaurar by remember { mutableStateOf<Entrada?>(null) }
    var gruposExpandidos by rememberSaveable { mutableStateOf(emptySet<String>()) }
    val espaciadoFilas = calcularEspaciadoFilas(ajustes.densidadLista)

    val ordenadas = remember(papelera) { papelera.sortedByDescending { it.eliminadaEn } }
    val itemsAgrupados = remember(ordenadas, ajustes.agruparPorSitio) {
        construirItemsAgrupadosPorTitulo(
            entradas = ordenadas,
            agrupar = ajustes.agruparPorSitio,
            expandido = { false }
        )
    }

    val alRestaurarEntrada = { entrada: Entrada ->
        val conflicto = entradasActivas.find {
            it.id == entrada.id || (it.titulo.trim().equals(entrada.titulo.trim(), ignoreCase = true) && it.usuario.trim() == entrada.usuario.trim())
        }
        if (conflicto != null) {
            conflictoRestaurar = entrada
        } else {
            vm.restaurarDeLaPapelera(entrada.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Papelera",
            idEtiqueta = "03-LST-PAP",
            mostrarId = ajustes.mostrarIdsAjustes,
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
                var menuAbiertoPap by remember { mutableStateOf(false) }
                Box {
                    IconButton(onClick = { menuAbiertoPap = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Más opciones",
                            tint = com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda(
                        expanded = menuAbiertoPap,
                        onDismissRequest = { menuAbiertoPap = false },
                        modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                    ) {
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Diseño de lista...",
                            icono = androidx.compose.material.icons.Icons.Filled.Layers,
                            colorIcono = ColorAcento,
                            onClick = {
                                menuAbiertoPap = false
                                vm.ir(Pantalla.OrganizacionLista("03-LST-DES-GRP"))
                            }
                        )
                        com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu()
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Ajustes de autodestrucción...",
                            icono = androidx.compose.material.icons.Icons.Filled.Timer,
                            colorIcono = ColorAcento,
                            onClick = {
                                menuAbiertoPap = false
                                vm.ir(Pantalla.AjustesAutodestruccion("01-SEG-DES"))
                            }
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
                    verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
                ) {
                    if (!ajustes.agruparPorSitio) {
                        items(ordenadas, key = { it.id }) { entrada ->
                            val diasRestantes = (DIAS_PAPELERA - diasDesde(entrada.eliminadaEn, ahora)).coerceAtLeast(0)
                            FilaPapeleraNativa(
                                entrada = entrada,
                                diasRestantes = diasRestantes,
                                alRestaurar = { alRestaurarEntrada(entrada) },
                                alBorrarDefinitivo = { aBorrarDefinitivo = entrada },
                                ajustes = ajustes,
                                mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                enGrupo = false
                            )
                        }
                    } else {
                        items(itemsAgrupados, key = { item ->
                            when (item) {
                                is ItemAgrupado.Grupo -> "grupo_${item.clave}"
                                is ItemAgrupado.Suelto -> "suelto_${item.entrada.id}"
                                is ItemAgrupado.Hijo -> "hijo_${item.entrada.id}"
                            }
                        }) { item ->
                            when (item) {
                                is ItemAgrupado.Grupo -> {
                                    ComponenteGrupoLista(
                                        clave = item.clave,
                                        entradas = item.entradas,
                                        expandido = gruposExpandidos.contains(item.clave),
                                        alAlternar = {
                                            gruposExpandidos = if (gruposExpandidos.contains(item.clave)) {
                                                gruposExpandidos - item.clave
                                            } else {
                                                gruposExpandidos + item.clave
                                            }
                                        },
                                        contenidoEntrada = { entradaHija, _, _ ->
                                            val diasRestantes = (DIAS_PAPELERA - diasDesde(entradaHija.eliminadaEn, ahora)).coerceAtLeast(0)
                                            FilaPapeleraNativa(
                                                entrada = entradaHija,
                                                diasRestantes = diasRestantes,
                                                alRestaurar = { alRestaurarEntrada(entradaHija) },
                                                alBorrarDefinitivo = { aBorrarDefinitivo = entradaHija },
                                                ajustes = ajustes,
                                                mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                                enGrupo = true
                                            )
                                        }
                                    )
                                }
                                is ItemAgrupado.Suelto -> {
                                    val diasRestantes = (DIAS_PAPELERA - diasDesde(item.entrada.eliminadaEn, ahora)).coerceAtLeast(0)
                                    FilaPapeleraNativa(
                                        entrada = item.entrada,
                                        diasRestantes = diasRestantes,
                                        alRestaurar = { alRestaurarEntrada(item.entrada) },
                                        alBorrarDefinitivo = { aBorrarDefinitivo = item.entrada },
                                        ajustes = ajustes,
                                        mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                        enGrupo = false
                                    )
                                }
                                is ItemAgrupado.Hijo -> Unit
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
