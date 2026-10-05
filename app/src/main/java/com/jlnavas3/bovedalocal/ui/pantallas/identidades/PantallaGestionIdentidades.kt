package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada

/**
 * Pantalla completa de administración y configuración de Identidades y Perfiles.
 */
@Composable
fun PantallaGestionIdentidades(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val desbloqueada = estadoBoveda as? EstadoBoveda.Desbloqueada
    val identidades = desbloqueada?.identidades ?: emptyList()
    val entradas = desbloqueada?.entradas ?: emptyList()

    var identidadAEditar by remember { mutableStateOf<Identidad?>(null) }
    var mostrandoDialogoCrear by remember { mutableStateOf(false) }
    var identidadAEliminar by remember { mutableStateOf<Identidad?>(null) }

    val listState = rememberLazyListState()

    // Conteo inteligente de entradas asociadas a cada identidad
    val conteosPorIdentidad = remember(entradas, identidades) {
        identidades.associate { iden ->
            val total = entradas.count { entrada ->
                resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
            }
            iden.id to total
        }
    }

    ProveedorResaltadoAjustes(seccionId) {
        Scaffold(
            topBar = {
                BarraSuperiorPantalla(
                    titulo = "Identidades y cuentas",
                    idEtiqueta = "03-LST-IDE",
                    mostrarId = ajustes.mostrarIdsAjustes,
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(ColorAcento.copy(alpha = 0.12f), MaterialTheme.shapes.extraLarge),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Sin identidades creadas",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextoPrincipal,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Crea identidades (ej. Personal, Trabajo, Compras) asociadas a tus correos. Las credenciales existentes se vincularán de forma automática e inteligente.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario,
                            textAlign = TextAlign.Center
                        )
                    }
                }
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
                        Text(
                            text = "Vinculación inteligente activa. Las cuentas que utilicen el correo principal o alias de cada identidad se agrupan automáticamente sin necesidad de editarlas a mano.",
                            style = MaterialTheme.typography.bodySmall,
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
    }

    if (mostrandoDialogoCrear) {
        DialogoEditarIdentidad(
            identidadAEditar = null,
            alGuardar = { nombre, correoPrincipal, alias, colorHex, icono ->
                haptica.exito()
                vm.crearIdentidad(nombre, correoPrincipal, alias, colorHex, icono)
                mostrandoDialogoCrear = false
            },
            alDescartar = { mostrandoDialogoCrear = false }
        )
    }

    identidadAEditar?.let { iden ->
        DialogoEditarIdentidad(
            identidadAEditar = iden,
            alGuardar = { nombre, correoPrincipal, alias, colorHex, icono ->
                haptica.exito()
                vm.actualizarIdentidad(iden.id, nombre, correoPrincipal, alias, colorHex, icono)
                identidadAEditar = null
            },
            alDescartar = { identidadAEditar = null }
        )
    }

    identidadAEliminar?.let { iden ->
        DialogoEliminarIdentidad(
            identidad = iden,
            cantidadEntradasVinculadas = conteosPorIdentidad[iden.id] ?: 0,
            alConfirmar = {
                haptica.exito()
                vm.eliminarIdentidad(iden.id)
                identidadAEliminar = null
            },
            alDescartar = { identidadAEliminar = null }
        )
    }
}
