package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Pantalla interactiva que se presenta al usuario cuando un gestor externo (Google, Dashlane, etc.)
 * solicita importar credenciales de Bóveda Local mediante Android Credential Transfer.
 * Incluye ID de pantalla (05-COP-CXF-EXP), búsqueda expandible, botón flotante compacto (FAB con icono y contador),
 * soporte para teclado (imePadding) y respeto a los tokens de geometría (02-APA-GEO) y tipografía (02-APA-TYP).
 */
@Composable
fun PantallaExportacionInteractivaCxf(
    gestorReceptor: String,
    entradasDisponibles: List<Entrada>,
    idsIniciales: Set<String>,
    alConfirmar: (List<Entrada>) -> Unit,
    alCancelar: () -> Unit,
    modifier: Modifier = Modifier,
    mostrarIdAjustes: Boolean = false
) {
    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }

    val idsSeleccionadas = remember(entradasDisponibles, idsIniciales) {
        mutableStateListOf<String>().apply {
            if (idsIniciales.isNotEmpty()) {
                addAll(idsIniciales.filter { id -> entradasDisponibles.any { it.id == id } })
            } else {
                addAll(entradasDisponibles.map { it.id })
            }
        }
    }

    val entradasVisibles = remember(entradasDisponibles, textoBusqueda) {
        if (textoBusqueda.isBlank()) entradasDisponibles
        else {
            val q = textoBusqueda.trim().lowercase()
            entradasDisponibles.filter {
                it.titulo.lowercase().contains(q) ||
                it.usuario.lowercase().contains(q) ||
                it.urls.any { u -> u.lowercase().contains(q) } ||
                it.notas.lowercase().contains(q)
            }
        }
    }

    val seleccionadasCountTotal = idsSeleccionadas.size

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        containerColor = ColorAjustesFondo,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorAjustesFondo)
            ) {
                BarraSuperiorPantalla(
                    titulo = "Transferir credenciales",
                    idEtiqueta = "05-COP-CXF-EXP",
                    mostrarId = mostrarIdAjustes,
                    alVolver = alCancelar,
                    colorFondo = ColorAjustesFondo,
                    acciones = {
                        BotonIconoCabecera(
                            onClick = {
                                busquedaVisible = !busquedaVisible
                                if (!busquedaVisible) textoBusqueda = ""
                            },
                            icono = if (busquedaVisible) Icons.Filled.Close else Icons.Filled.Search,
                            descripcion = if (busquedaVisible) "Cerrar búsqueda" else "Buscar credenciales"
                        )
                    }
                )

                AnimatedVisibility(
                    visible = busquedaVisible || textoBusqueda.isNotBlank(),
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        BarraBusquedaAnimada(
                            valor = textoBusqueda,
                            alCambiar = { textoBusqueda = it },
                            alCerrar = {
                                textoBusqueda = ""
                                busquedaVisible = false
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (seleccionadasCountTotal > 0) {
                FloatingActionButton(
                    onClick = {
                        val seleccionadas = entradasDisponibles.filter { idsSeleccionadas.contains(it.id) }
                        alConfirmar(seleccionadas)
                    },
                    containerColor = Ambar,
                    contentColor = ColorSobreAcento,
                    shape = FormaTarjeta,
                    modifier = Modifier.then(
                        if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && ColorBordeActual != androidx.compose.ui.graphics.Color.Transparent) {
                            Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                        } else Modifier
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Transferir credenciales",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "$seleccionadasCountTotal",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = (11 * EscalaTexto).sp
                            ),
                            color = ColorSobreAcento
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(EspaciadoComponentes.coerceAtLeast(8.dp))
        ) {
            item {
                CabeceraExportacionCxf(
                    gestorReceptor = gestorReceptor,
                    totalElementos = entradasDisponibles.size,
                    seleccionados = seleccionadasCountTotal,
                    alSeleccionarTodas = {
                        entradasVisibles.forEach {
                            if (!idsSeleccionadas.contains(it.id)) idsSeleccionadas.add(it.id)
                        }
                    },
                    alDeseleccionarTodas = {
                        if (textoBusqueda.isBlank()) {
                            idsSeleccionadas.clear()
                        } else {
                            entradasVisibles.forEach {
                                idsSeleccionadas.remove(it.id)
                            }
                        }
                    }
                )
            }

            items(entradasVisibles, key = { it.id }) { entrada ->
                val estaSeleccionada = idsSeleccionadas.contains(entrada.id)
                FilaSeleccionCredencialCxf(
                    entrada = entrada,
                    seleccionada = estaSeleccionada,
                    alAlternar = { activo ->
                        if (activo) {
                            if (!idsSeleccionadas.contains(entrada.id)) idsSeleccionadas.add(entrada.id)
                        } else {
                            idsSeleccionadas.remove(entrada.id)
                        }
                    }
                )
            }

            item {
                // Espaciador para evitar que el FAB tape el último elemento
                Spacer(modifier = Modifier.height(84.dp))
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
@Composable
private fun PantallaExportacionInteractivaCxfPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda(padding = 0.dp) {
        PantallaExportacionInteractivaCxf(
            gestorReceptor = "Google Credential Manager",
            entradasDisponibles = listOf(
                com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
                com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaBancaria
            ),
            idsIniciales = setOf("mock-1"),
            alConfirmar = {},
            alCancelar = {},
            mostrarIdAjustes = true
        )
    }
}

