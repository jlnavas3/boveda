package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada

/**
 * Pantalla principal para la administración de [Identidad] (CRUD completo).
 */
@Composable
fun PantallaGestionIdentidades(
    vm: VaultViewModel,
    idDestinoInicial: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    val estado by vm.estado.collectAsStateWithLifecycle()
    val identidades = (estado as? EstadoBoveda.Desbloqueada)?.identidades ?: emptyList()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    // Conteo en vivo de cuentas asociadas por cada identidad
    val conteosPorIdentidad = remember(entradas, identidades) {
        identidades.associate { iden ->
            iden.id to entradas.count { entrada ->
                resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
            }
        }
    }

    var mostrandoDialogoCrear by remember { mutableStateOf(false) }
    var identidadAEditar by remember { mutableStateOf<Identidad?>(null) }
    var identidadAEliminar by remember { mutableStateOf<Identidad?>(null) }

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            BarraSuperiorPantalla(
                titulo = "Identidades",
                idEtiqueta = idDestinoInicial ?: "03-LST-IDE",
                alVolver = { vm.volverAtras() },
                conSeparador = listState.firstVisibleItemScrollOffset > 0,
                colorFondo = ColorAjustesFondo
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    haptica.tic()
                    mostrandoDialogoCrear = true
                },
                containerColor = ColorAcento,
                contentColor = ColorSobreAcento
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nueva identidad"
                )
            }
        },
        containerColor = ColorAjustesFondo
    ) { paddingValues ->
        if (identidades.isEmpty()) {
            EstadoVacioIdentidades(
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    TextoCuerpo(
                        texto = "Vinculación inteligente activa. Las cuentas que utilicen el correo principal o alias de cada identidad se agrupan automáticamente sin necesidad de editarlas a mano.",
                        tamano = TamanoCuerpo.PEQUENO,
                        color = TextoSecundario,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                    )
                }

                items(identidades, key = { it.id }) { iden ->
                    FilaGestionIdentidad(
                        identidad = iden,
                        conteoCuentas = conteosPorIdentidad[iden.id] ?: 0,
                        alEditar = {
                            haptica.tic()
                            identidadAEditar = iden
                        },
                        alEliminar = {
                            haptica.tic()
                            identidadAEliminar = iden
                        }
                    )
                }
            }
        }
    }

    if (mostrandoDialogoCrear) {
        DialogoEditarIdentidad(
            identidadAEditar = null,
            alGuardar = { nombre, correoPrincipal, correosSecundarios, colorHex, icono ->
                mostrandoDialogoCrear = false
                vm.crearIdentidad(
                    nombre = nombre,
                    correoPrincipal = correoPrincipal,
                    correosSecundarios = correosSecundarios,
                    colorHex = colorHex,
                    icono = icono
                )
            },
            alDescartar = { mostrandoDialogoCrear = false }
        )
    }

    identidadAEditar?.let { iden ->
        DialogoEditarIdentidad(
            identidadAEditar = iden,
            alGuardar = { nombre, correoPrincipal, correosSecundarios, colorHex, icono ->
                identidadAEditar = null
                vm.actualizarIdentidad(
                    id = iden.id,
                    nombre = nombre,
                    correoPrincipal = correoPrincipal,
                    correosSecundarios = correosSecundarios,
                    colorHex = colorHex,
                    icono = icono
                )
            },
            alDescartar = { identidadAEditar = null }
        )
    }

    identidadAEliminar?.let { iden ->
        val vinculadas = conteosPorIdentidad[iden.id] ?: 0
        DialogoEliminarIdentidad(
            identidad = iden,
            cantidadEntradasVinculadas = vinculadas,
            alConfirmar = {
                identidadAEliminar = null
                vm.eliminarIdentidad(iden.id)
            },
            alDescartar = { identidadAEliminar = null }
        )
    }
}
