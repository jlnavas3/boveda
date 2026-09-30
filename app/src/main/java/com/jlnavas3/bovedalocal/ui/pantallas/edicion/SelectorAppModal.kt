package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AppInstalada
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

/**
 * Diálogo modal para explorar y seleccionar aplicaciones instaladas en el dispositivo.
 */
@Composable
fun SelectorAppModal(
    alDescartar: () -> Unit,
    alSeleccionarApp: (String) -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    var apps by remember { mutableStateOf<List<AppInstalada>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var consultaBusqueda by remember { mutableStateOf("") }
    var incluirSistema by remember { mutableStateOf(false) }

    LaunchedEffect(incluirSistema) {
        cargando = true
        apps = GestorAppsInstaladas.obtenerAppsInstaladas(contexto, incluirSistema = incluirSistema)
        cargando = false
    }

    val appsFiltradas = remember(apps, consultaBusqueda) {
        GestorAppsInstaladas.filtrarApps(apps, consultaBusqueda)
    }

    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Aplicaciones instaladas",
        descripcion = if (cargando) "Cargando lista..." else "${appsFiltradas.size} aplicaciones",
        icono = Icons.Filled.Android,
        colorIcono = ColorAcento,
        fondoIcono = ColorAcento.copy(alpha = 0.15f),
        mostrarBotonCerrar = true
    ) {

                    // Campo de Búsqueda
                    ComponenteCampoTexto(
                        valor = consultaBusqueda,
                        etiqueta = "Buscar por nombre o paquete",
                        alCambiar = { consultaBusqueda = it },
                        icono = Icons.Filled.Search,
                        mostrarIcono = true,
                        placeholder = "Ej. WhatsApp, Spotify, com.android..."
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaPequena)
                            .clickable {
                                haptica.tic()
                                incluirSistema = !incluirSistema
                            }
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Dns,
                                contentDescription = null,
                                tint = if (incluirSistema) ColorAcento else TextoSecundario,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Incluir apps de sistema",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = if (incluirSistema) TextoPrincipal else TextoSecundario
                            )
                        }
                        SwitchBoveda(
                            checked = incluirSistema,
                            onCheckedChange = {
                                haptica.tic()
                                incluirSistema = it
                            }
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Lista de aplicaciones
                    if (cargando) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = ColorAcento,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    } else {
                        val busquedaLimpia = consultaBusqueda.trim()
                        val esPaqueteDirecto = remember(busquedaLimpia) { LanzadorEnlaces.esNombrePaquete(busquedaLimpia) }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 280.dp)
                        ) {
                            items(appsFiltradas, key = { it.paquete }) { app ->
                                FilaAppInstalada(
                                    app = app,
                                    alSeleccionarApp = {
                                        alSeleccionarApp(it)
                                        alDescartar()
                                    }
                                )
                            }

                            if (esPaqueteDirecto) {
                                item(key = "opcion_paquete_directo") {
                                    FilaOpcionPaqueteDirecto(
                                        paquete = busquedaLimpia,
                                        alSeleccionarApp = {
                                            alSeleccionarApp(it)
                                            alDescartar()
                                        }
                                    )
                                }
                            }

                            if (busquedaLimpia.isNotBlank()) {
                                item(key = "opcion_play_store") {
                                    FilaOpcionPlayStore(
                                        terminoBusqueda = busquedaLimpia,
                                        alDescartar = alDescartar
                                    )
                                }
                            }
                        }
                    }
    }
}
