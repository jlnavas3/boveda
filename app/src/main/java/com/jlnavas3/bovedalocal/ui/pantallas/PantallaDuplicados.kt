package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.MergeType
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.GrupoDuplicado
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.BarraBusquedaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class FiltroDuplicados {
    TODOS,
    IDENTICOS,
    APPS_ANDROID,
    SITIOS_WEB,
    MISMA_CUENTA,
    VARIANTES
}

@Composable
fun PantallaDuplicados(
    vm: VaultViewModel,
    estado: EstadoBoveda,
    seccionDestino: String? = null
) {
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val grupos = remember(entradas) { AnalizadorDuplicados.analizar(entradas) }

    val gruposIdenticos = remember(grupos) { grupos.filter { it.tipo == TipoDuplicado.IDENTICO } }
    val totalSobrantesIdenticas = remember(gruposIdenticos) { gruposIdenticos.sumOf { it.entradasSecundarias.size } }
    val totalDuplicadasSobrantes = remember(grupos) { grupos.sumOf { it.entradasSecundarias.size } }

    val cantAppsAndroid = remember(grupos) { grupos.count { it.esAppAndroid } }
    val cantWeb = remember(grupos) { grupos.count { !it.esAppAndroid } }
    val cantMismaCuenta = remember(grupos) { grupos.count { it.tipo == TipoDuplicado.MISMA_CUENTA_DISTINTA_CLAVE } }
    val cantVariantes = remember(grupos) { grupos.count { it.tipo == TipoDuplicado.VARIANTE_USUARIO } }

    var filtroActivo by remember { mutableStateOf(FiltroDuplicados.TODOS) }
    var textoBusqueda by remember { mutableStateOf("") }
    var confirmarLimpiezaMasiva by remember { mutableStateOf(false) }

    val gruposFiltrados = remember(grupos, filtroActivo, textoBusqueda) {
        grupos.filter { grupo ->
            val coincideFiltro = when (filtroActivo) {
                FiltroDuplicados.TODOS -> true
                FiltroDuplicados.IDENTICOS -> grupo.tipo == TipoDuplicado.IDENTICO
                FiltroDuplicados.APPS_ANDROID -> grupo.esAppAndroid
                FiltroDuplicados.SITIOS_WEB -> !grupo.esAppAndroid
                FiltroDuplicados.MISMA_CUENTA -> grupo.tipo == TipoDuplicado.MISMA_CUENTA_DISTINTA_CLAVE
                FiltroDuplicados.VARIANTES -> grupo.tipo == TipoDuplicado.VARIANTE_USUARIO
            }
            coincideFiltro && (
                textoBusqueda.isBlank() ||
                grupo.claveVisual.contains(textoBusqueda, ignoreCase = true) ||
                grupo.entradas.any { it.titulo.contains(textoBusqueda, ignoreCase = true) || it.usuario.contains(textoBusqueda, ignoreCase = true) }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Contraseñas duplicadas",
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (grupos.isEmpty()) "Tu bóveda no tiene duplicados" else "${grupos.size} grupos encontrados · $totalDuplicadasSobrantes entradas redundantes"
            )

            Spacer(Modifier.height(10.dp))

            if (grupos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IlustracionSinDuplicados()
                }
            } else {
                // Banner de Limpieza Rápida Masiva para Duplicados Idénticos
                if (totalSobrantesIdenticas > 0 && filtroActivo != FiltroDuplicados.MISMA_CUENTA) {
                    GrupoAjustes(etiqueta = "Limpieza rápida masiva") {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(fondoBadgeParaTema(Menta)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AutoFixHigh,
                                        contentDescription = null,
                                        tint = colorLegibleParaTema(Menta),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Limpiar $totalSobrantesIdenticas copias idénticas",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = ColorTextoAjustes
                                    )
                                    Text(
                                        "Detectadas de importaciones repetidas de CSV. Conserva la mejor copia de cada cuenta.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ColorAjusteGris
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            BotonColorido(
                                texto = "Limpiar copias idénticas en 1 toque",
                                color = Menta,
                                icono = Icons.Filled.Delete,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                confirmarLimpiezaMasiva = true
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                }

                // Buscador nativo
                BarraBusquedaAjustes(
                    texto = textoBusqueda,
                    alCambiarTexto = { textoBusqueda = it },
                    placeholder = "Buscar en duplicados..."
                )

                Spacer(Modifier.height(12.dp))

                // Selector de filtros con chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = filtroActivo == FiltroDuplicados.TODOS,
                            onClick = { filtroActivo = FiltroDuplicados.TODOS },
                            label = { Text("Todos (${grupos.size})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = fondoBadgeParaTema(ColorAcento),
                                selectedLabelColor = colorLegibleParaTema(ColorAcento)
                            )
                        )
                    }
                    if (totalSobrantesIdenticas > 0) {
                        item {
                            FilterChip(
                                selected = filtroActivo == FiltroDuplicados.IDENTICOS,
                                onClick = { filtroActivo = FiltroDuplicados.IDENTICOS },
                                label = { Text("Idénticos ($totalSobrantesIdenticas)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = fondoBadgeParaTema(Menta),
                                    selectedLabelColor = colorLegibleParaTema(Menta)
                                )
                            )
                        }
                    }
                    if (cantAppsAndroid > 0) {
                        item {
                            FilterChip(
                                selected = filtroActivo == FiltroDuplicados.APPS_ANDROID,
                                onClick = { filtroActivo = FiltroDuplicados.APPS_ANDROID },
                                label = { Text("Apps Android ($cantAppsAndroid)") }
                            )
                        }
                    }
                    if (cantWeb > 0) {
                        item {
                            FilterChip(
                                selected = filtroActivo == FiltroDuplicados.SITIOS_WEB,
                                onClick = { filtroActivo = FiltroDuplicados.SITIOS_WEB },
                                label = { Text("Sitios web ($cantWeb)") }
                            )
                        }
                    }
                    if (cantMismaCuenta > 0) {
                        item {
                            FilterChip(
                                selected = filtroActivo == FiltroDuplicados.MISMA_CUENTA,
                                onClick = { filtroActivo = FiltroDuplicados.MISMA_CUENTA },
                                label = { Text("Misma cuenta ($cantMismaCuenta)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = fondoBadgeParaTema(Peligro),
                                    selectedLabelColor = colorLegibleParaTema(Peligro)
                                )
                            )
                        }
                    }
                    if (cantVariantes > 0) {
                        item {
                            FilterChip(
                                selected = filtroActivo == FiltroDuplicados.VARIANTES,
                                onClick = { filtroActivo = FiltroDuplicados.VARIANTES },
                                label = { Text("Variantes ($cantVariantes)") }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Lista de grupos duplicados
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(gruposFiltrados, key = { it.idGrupo }) { grupo ->
                        TarjetaGrupoDuplicado(
                            grupo = grupo,
                            alConservar = { elegida ->
                                val secundarias = grupo.entradas.filterNot { it.id == elegida.id }
                                vm.eliminarVarias(secundarias.map { it.id }.toSet())
                                vm.avisar("Copia seleccionada conservada")
                            },
                            alUnificar = {
                                vm.unificarEntradas(grupo.sugeridaPrincipal, grupo.entradasSecundarias)
                            },
                            alVerDetalle = { id ->
                                vm.ir(Pantalla.Detalle(id))
                            }
                        )
                    }
                }
            }
        }
    }

    if (confirmarLimpiezaMasiva) {
        AlertDialog(
            onDismissRequest = { confirmarLimpiezaMasiva = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = ColorTarjetaAjustes,
            tonalElevation = 0.dp,
            title = {
                Text(
                    text = "Limpiar $totalSobrantesIdenticas copias idénticas",
                    color = ColorTextoAjustes,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Se enviarán $totalSobrantesIdenticas entradas duplicadas a la papelera, conservando automáticamente la copia más completa y reciente de cada servicio. Podrás recuperarlas de la papelera en los próximos 30 días si lo necesitas.",
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmarLimpiezaMasiva = false
                        vm.eliminarDuplicadasExactasMasivo(gruposIdenticos)
                    }
                ) {
                    Text("Limpiar ahora", color = Menta, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarLimpiezaMasiva = false }) {
                    Text("Cancelar", color = ColorAjusteGris)
                }
            }
        )
    }
}

@Composable
private fun TarjetaGrupoDuplicado(
    grupo: GrupoDuplicado,
    alConservar: (Entrada) -> Unit,
    alUnificar: () -> Unit,
    alVerDetalle: (String) -> Unit
) {
    val colorBadge = when (grupo.tipo) {
        TipoDuplicado.IDENTICO -> Menta
        TipoDuplicado.MISMA_CUENTA_DISTINTA_CLAVE -> Peligro
        TipoDuplicado.VARIANTE_USUARIO -> ColorAcento
    }

    val iconoGrupo = when {
        grupo.tipo == TipoDuplicado.IDENTICO -> Icons.Filled.ContentCopy
        grupo.esAppAndroid -> Icons.Filled.Android
        else -> Icons.AutoMirrored.Filled.MergeType
    }
    val origen = if (grupo.esAppAndroid) "App Android" else "Web"
    val etiquetaGrupo = "${grupo.claveVisual.uppercase()} · $origen · ${grupo.tipo.titulo.uppercase()}"

    GrupoAjustes(etiqueta = etiquetaGrupo) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(fondoBadgeParaTema(colorBadge)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconoGrupo,
                        contentDescription = null,
                        tint = colorLegibleParaTema(colorBadge),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = grupo.claveVisual,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = ColorTextoAjustes
                    )
                    Text(
                        text = grupo.tipo.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = ColorAjusteGris
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botones de acción rápida para el grupo
            if (grupo.tipo == TipoDuplicado.IDENTICO) {
                BotonColorido(
                    texto = "Conservar la mejor versión",
                    color = Menta,
                    icono = Icons.Filled.Check,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    alConservar(grupo.sugeridaPrincipal)
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    BotonColorido(
                        texto = "Unificar (conservar todas las claves)",
                        color = ColorAcento,
                        icono = Icons.AutoMirrored.Filled.MergeType,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        alUnificar()
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Guarda las claves alternativas en campos personalizados e historial para no perder ninguna.",
                        style = MaterialTheme.typography.labelSmall,
                        color = ColorAjusteGris
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Comparativa de entradas dentro del grupo
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                for (entrada in grupo.entradas) {
                    val esPrincipal = entrada.id == grupo.sugeridaPrincipal.id
                    var claveVisible by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (esPrincipal) fondoBadgeParaTema(colorBadge).copy(alpha = 0.2f) else Superficie)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = entrada.titulo.ifBlank { "Sin título" },
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                        color = ColorTextoAjustes
                                    )
                                    if (esPrincipal) {
                                        Spacer(Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(fondoBadgeParaTema(Menta))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "Sugerida",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                                color = colorLegibleParaTema(Menta)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "Usuario: ${entrada.usuario.ifBlank { "(Vacío)" }}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ColorAjusteGris
                                )
                            }

                            TextButton(onClick = { alVerDetalle(entrada.id) }) {
                                Text("Ver", color = ColorAcento)
                            }
                        }

                        Spacer(Modifier.height(4.dp))

                        // Contraseña
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (claveVisible) entrada.contrasena.ifBlank { "(Sin clave)" } else "•".repeat(entrada.contrasena.length.coerceIn(8, 14)),
                                style = EstiloMono,
                                color = ColorTextoAjustes,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { claveVisible = !claveVisible },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (claveVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = "Mostrar",
                                    tint = ColorAjusteGris,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Metadatos y URLs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Modif: ${formatoFecha.format(Date(entrada.modificadaEn))}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorAjusteGris
                            )
                            if (entrada.urls.isNotEmpty()) {
                                Text(
                                    text = entrada.urls.first(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ColorAjusteGris,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }

                        // Si no es la principal, botón para elegirla como la que se desea conservar
                        if (!esPrincipal) {
                            Spacer(Modifier.height(8.dp))
                            TextButton(
                                onClick = { alConservar(entrada) },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(Icons.Filled.DoneAll, contentDescription = null, tint = Menta, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Conservar esta copia", color = Menta, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IlustracionSinDuplicados(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Menta.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Menta,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Sin duplicados en tu bóveda",
            style = MaterialTheme.typography.titleLarge,
            color = ColorTextoAjustes,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "No se encontraron contraseñas ni cuentas redundantes. Tu bóveda se encuentra perfectamente organizada.",
            style = MaterialTheme.typography.bodyMedium,
            color = ColorAjusteGris,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
