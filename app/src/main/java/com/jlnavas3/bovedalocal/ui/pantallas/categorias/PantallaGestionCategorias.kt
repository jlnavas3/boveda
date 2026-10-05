package com.jlnavas3.bovedalocal.ui.pantallas.categorias

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
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla principal para la administracion de [Categoria] (CRUD completo).
 */
@Composable
fun PantallaGestionCategorias(
    vm: VaultViewModel,
    idDestinoInicial: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    val estado by vm.estado.collectAsStateWithLifecycle()
    val categorias = (estado as? EstadoBoveda.Desbloqueada)?.categorias ?: emptyList()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    val conteosPorCategoria = remember(entradas, categorias) {
        categorias.associate { col ->
            col.id to entradas.count { it.categorias.contains(col.id) }
        }
    }

    var mostrandoDialogoCrear by remember { mutableStateOf(false) }
    var categoriaAEditar by remember { mutableStateOf<Categoria?>(null) }
    var categoriaAEliminar by remember { mutableStateOf<Categoria?>(null) }

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            BarraSuperiorPantalla(
                titulo = "Categorías",
                idEtiqueta = idDestinoInicial ?: "03-LST-CAT",
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
                    contentDescription = "Nueva categoría"
                )
            }
        },
        containerColor = ColorAjustesFondo
    ) { paddingValues ->
        if (categorias.isEmpty()) {
            EstadoVacioCategorias(
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
                        texto = "Las categorías te permiten agrupar credenciales por área o temática (ej. Finanzas, Trabajo, Streaming). Puedes asignar varias categorías a una sola entrada.",
                        tamano = TamanoCuerpo.PEQUENO,
                        color = TextoSecundario,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                    )
                }

                items(categorias, key = { it.id }) { cat ->
                    FilaGestionCategoria(
                        categoria = cat,
                        conteoCuentas = conteosPorCategoria[cat.id] ?: 0,
                        alEditar = {
                            haptica.tic()
                            categoriaAEditar = cat
                        },
                        alEliminar = {
                            haptica.tic()
                            categoriaAEliminar = cat
                        }
                    )
                }
            }
        }
    }

    if (mostrandoDialogoCrear) {
        DialogoEditarCategoria(
            categoriaAEditar = null,
            alGuardar = { nombre, icono, colorHex ->
                mostrandoDialogoCrear = false
                vm.crearColeccion(
                    nombre = nombre,
                    icono = icono,
                    colorHex = colorHex
                )
            },
            alDescartar = { mostrandoDialogoCrear = false }
        )
    }

    categoriaAEditar?.let { cat ->
        DialogoEditarCategoria(
            categoriaAEditar = cat,
            alGuardar = { nombre, icono, colorHex ->
                categoriaAEditar = null
                vm.actualizarColeccion(
                    id = cat.id,
                    nombre = nombre,
                    icono = icono,
                    colorHex = colorHex
                )
            },
            alDescartar = { categoriaAEditar = null }
        )
    }

    categoriaAEliminar?.let { cat ->
        val vinculadas = conteosPorCategoria[cat.id] ?: 0
        DialogoEliminarCategoria(
            categoria = cat,
            cantidadEntradasVinculadas = vinculadas,
            alConfirmar = {
                categoriaAEliminar = null
                vm.eliminarColeccion(cat.id)
            },
            alDescartar = { categoriaAEliminar = null }
        )
    }
}
