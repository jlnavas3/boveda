package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
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
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AppInstalada
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.Haptica

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
    val esOscuro = isSystemInDarkTheme()
    val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White

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

    Dialog(
        onDismissRequest = alDescartar,
        properties = DialogProperties(
            decorFitsSystemWindows = false,
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { alDescartar() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(fondoModal)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Detener propagación de click */ }
                    .padding(top = 20.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Cabecera
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(FormaPequena)
                                    .background(ColorAcento.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Android,
                                    contentDescription = null,
                                    tint = ColorAcento,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Aplicaciones instaladas",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextoPrincipal
                                )
                                Text(
                                    text = if (cargando) "Cargando lista..." else "${appsFiltradas.size} aplicaciones",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextoSecundario
                                )
                            }
                        }

                        IconButton(onClick = alDescartar) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Cerrar",
                                tint = TextoSecundario
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

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
                                .height(260.dp),
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
                                .heightIn(min = 180.dp, max = 360.dp)
                        ) {
                            items(appsFiltradas, key = { it.paquete }) { app ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(FormaPequena)
                                        .clickable {
                                            haptica.toque()
                                            alSeleccionarApp(app.paquete)
                                            alDescartar()
                                        }
                                        .padding(horizontal = 10.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (app.iconoBitmap != null) {
                                        Image(
                                            bitmap = app.iconoBitmap,
                                            contentDescription = app.nombre,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(FormaPequena)
                                                .background(TextoSecundario.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Android,
                                                contentDescription = null,
                                                tint = TextoSecundario,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = app.nombre,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                                color = TextoPrincipal,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f, fill = false)
                                            )
                                            if (app.esDeSistema) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(ColorAcento.copy(alpha = 0.15f))
                                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                                ) {
                                                    Text(
                                                        text = "Sistema",
                                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                                        color = ColorAcento
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = app.paquete,
                                            style = EstiloMono.copy(fontSize = 11.sp),
                                            color = TextoSecundario,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                            if (esPaqueteDirecto) {
                                item(key = "opcion_paquete_directo") {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(FormaPequena)
                                            .clickable {
                                                haptica.toque()
                                                alSeleccionarApp(busquedaLimpia)
                                                alDescartar()
                                            }
                                            .padding(horizontal = 10.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(FormaPequena)
                                                .background(ColorAcento.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Add,
                                                contentDescription = null,
                                                tint = ColorAcento,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Usar paquete: $busquedaLimpia",
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                                color = ColorAcento,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "Agregar directamente como enlace",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextoSecundario,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }

                            if (busquedaLimpia.isNotBlank()) {
                                item(key = "opcion_play_store") {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(FormaPequena)
                                            .clickable {
                                                haptica.toque()
                                                val uri = Uri.encode(busquedaLimpia)
                                                LanzadorEnlaces.abrir(contexto, "market://search?q=$uri")
                                                alDescartar()
                                            }
                                            .padding(horizontal = 10.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(FormaPequena)
                                                .background(Ambar.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.ShoppingBag,
                                                contentDescription = null,
                                                tint = Ambar,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(Modifier.width(12.dp))
                                        Text(
                                            text = "Buscar en Play Store",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = Ambar,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
