package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.BarraBusquedaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjusteMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.MedidorFuerza
import java.util.concurrent.TimeUnit

private const val DIAS_AVISO_ANTIGUEDAD = 180L

private enum class PestanaSalud(val titulo: String) {
    REPETIDAS("Repetidas"),
    COMUNES("Filtradas"),
    DEBILES("Débiles"),
    ANTIGUAS("Antiguas")
}

@Composable
fun PantallaSaludBoveda(
    vm: VaultViewModel,
    estado: EstadoBoveda,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val claves = remember(entradas) { entradas.filter { it.tipo == TipoEntrada.LOGIN && it.contrasena.isNotBlank() } }

    val duplicadas = remember(claves) {
        claves.groupBy { it.contrasena }.values.filter { it.size > 1 }
    }
    val debiles = remember(claves) {
        claves.filter { MedidorFuerza.medir(it.contrasena).puntuacion <= 1 }
            .sortedBy { MedidorFuerza.medir(it.contrasena).puntuacion }
    }
    val muyComunes = remember(claves) {
        claves.filter { ContrasenasComunes.esComun(contexto, it.contrasena) }
    }
    val ahora = remember { System.currentTimeMillis() }
    val antiguas = remember(claves) {
        claves.filter { it.modificadaEn > 0 && diasDesde(it.modificadaEn, ahora) >= DIAS_AVISO_ANTIGUEDAD }
            .sortedBy { it.modificadaEn }
    }

    // Análisis de duplicados para alertar de copias de CSV
    val gruposDuplicados = remember(entradas) { AnalizadorDuplicados.analizar(entradas) }
    val totalSobrantesDuplicadas = remember(gruposDuplicados) { gruposDuplicados.sumOf { it.entradasSecundarias.size } }

    var pestanaActiva by remember { mutableStateOf(PestanaSalud.REPETIDAS) }
    var textoBusqueda by remember { mutableStateOf("") }
    var entradaParaCambioRapido by remember { mutableStateOf<Entrada?>(null) }
    var resumenExpandido by rememberSaveable { mutableStateOf(true) }
    val haptica = remember { Haptica(contexto) }

    val buscando = textoBusqueda.isNotBlank()
    val mostrarResumenCompleto = resumenExpandido && !buscando

    LaunchedEffect(claves.size) {
        val resumen = "Auditoría de salud ejecutada: ${claves.size} claves analizadas (${debiles.size} débiles, ${duplicadas.size} grupos repetidos, ${muyComunes.size} comunes, ${antiguas.size} antiguas)"
        Diagnostico.apuntar("salud", resumen)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
            .imePadding()
    ) {
        BarraSuperiorPantalla(
            titulo = "Salud de la Bóveda",
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = "${claves.size} contraseñas auditadas"
            )

            // Banner inteligente de Duplicados de CSV (se oculta al buscar para priorizar resultados)
            AnimatedVisibility(
                visible = totalSobrantesDuplicadas > 0 && !buscando,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(10.dp))
                    GrupoAjustes(etiqueta = "Duplicados detectados") {
                        FilaAjusteMenu(
                            titulo = "Detectadas $totalSobrantesDuplicadas copias repetidas",
                            subtitulo = "Entradas idénticas de importación. Pulsa para limpiar con 1 toque",
                            icono = Icons.Filled.AutoFixHigh,
                            colorIcono = ColorSalud,
                            alPulsar = { vm.ir(Pantalla.Duplicados) }
                        )
                    }
                }
            }

            // Cabecera interactiva del Resumen de Auditoría
            AnimatedVisibility(
                visible = !buscando,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                haptica.tic()
                                resumenExpandido = !resumenExpandido
                            }
                            .padding(vertical = 4.dp, horizontal = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RESUMEN DE AUDITORÍA",
                            color = ColorAjusteGris,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (resumenExpandido) "Ocultar" else "Mostrar",
                                color = ColorAjusteGris,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Icon(
                                imageVector = if (resumenExpandido) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = if (resumenExpandido) "Plegar resumen" else "Desplegar resumen",
                                tint = ColorAjusteGris,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    if (mostrarResumenCompleto) {
                        // Tarjeta completa de auditoría
                        GrupoAjustes {
                            FilaMetricaSalud(
                                etiqueta = "Contraseñas analizadas",
                                valor = claves.size.toString(),
                                color = ColorTextoAjustes
                            )
                            SeparadorFilaSimple()
                            FilaMetricaSalud(
                                etiqueta = "Repetidas (en grupos)",
                                valor = duplicadas.sumOf { it.size }.toString(),
                                color = if (duplicadas.isEmpty()) Menta else Peligro
                            )
                            SeparadorFilaSimple()
                            FilaMetricaSalud(
                                etiqueta = "Muy comunes o filtradas",
                                valor = muyComunes.size.toString(),
                                color = if (muyComunes.isEmpty()) Menta else Peligro
                            )
                            SeparadorFilaSimple()
                            FilaMetricaSalud(
                                etiqueta = "Débiles (baja entropía)",
                                valor = debiles.size.toString(),
                                color = if (debiles.isEmpty()) Menta else Peligro
                            )
                            SeparadorFilaSimple()
                            FilaMetricaSalud(
                                etiqueta = "Sin actualizar (> $DIAS_AVISO_ANTIGUEDAD días)",
                                valor = antiguas.size.toString(),
                                color = if (antiguas.isEmpty()) Menta else ColorAcento
                            )
                        }
                    } else {
                        // Modo compacto: cápsula horizontal estilo Honor MagicOS / One UI
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(ColorTarjetaAjustes)
                                .clickable {
                                    haptica.tic()
                                    resumenExpandido = true
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${claves.size} analizadas · ${duplicadas.sumOf { it.size }} repetidas · ${muyComunes.size} comunes · ${debiles.size} débiles",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = ColorTextoAjustes,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Buscador nativo
            BarraBusquedaAjustes(
                texto = textoBusqueda,
                alCambiarTexto = { textoBusqueda = it },
                placeholder = "Buscar servicio o cuenta..."
            )

            Spacer(Modifier.height(10.dp))

            // Selector de Pestañas tipo chip nativo
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    val cant = duplicadas.size
                    val cantTotal = duplicadas.sumOf { it.size }
                    FilterChip(
                        selected = pestanaActiva == PestanaSalud.REPETIDAS,
                        onClick = { pestanaActiva = PestanaSalud.REPETIDAS },
                        label = { Text("Repetidas ($cant g / $cantTotal)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = fondoBadgeParaTema(if (cant > 0) Peligro else Menta),
                            selectedLabelColor = colorLegibleParaTema(if (cant > 0) Peligro else Menta)
                        )
                    )
                }
                item {
                    val cant = muyComunes.size
                    FilterChip(
                        selected = pestanaActiva == PestanaSalud.COMUNES,
                        onClick = { pestanaActiva = PestanaSalud.COMUNES },
                        label = { Text("Filtradas ($cant)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = fondoBadgeParaTema(if (cant > 0) Peligro else Menta),
                            selectedLabelColor = colorLegibleParaTema(if (cant > 0) Peligro else Menta)
                        )
                    )
                }
                item {
                    val cant = debiles.size
                    FilterChip(
                        selected = pestanaActiva == PestanaSalud.DEBILES,
                        onClick = { pestanaActiva = PestanaSalud.DEBILES },
                        label = { Text("Débiles ($cant)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = fondoBadgeParaTema(if (cant > 0) Peligro else Menta),
                            selectedLabelColor = colorLegibleParaTema(if (cant > 0) Peligro else Menta)
                        )
                    )
                }
                item {
                    val cant = antiguas.size
                    FilterChip(
                        selected = pestanaActiva == PestanaSalud.ANTIGUAS,
                        onClick = { pestanaActiva = PestanaSalud.ANTIGUAS },
                        label = { Text("Antiguas ($cant)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = fondoBadgeParaTema(ColorAcento),
                            selectedLabelColor = colorLegibleParaTema(ColorAcento)
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Contenido según pestaña activa usando LazyColumn de alto rendimiento
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                when (pestanaActiva) {
                    PestanaSalud.REPETIDAS -> {
                        val gruposFiltrados = remember(duplicadas, textoBusqueda) {
                            if (textoBusqueda.isBlank()) duplicadas
                            else duplicadas.filter { grupo ->
                                grupo.any { it.titulo.contains(textoBusqueda, ignoreCase = true) || it.usuario.contains(textoBusqueda, ignoreCase = true) }
                            }
                        }

                        if (gruposFiltrados.isEmpty()) {
                            MensajeExitoPestana(
                                icono = Icons.Filled.Check,
                                titulo = "Sin contraseñas repetidas",
                                subtitulo = "Todas tus contraseñas son únicas en sus respectivas cuentas."
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                items(gruposFiltrados, key = { it.first().contrasena }) { grupo ->
                                    TarjetaGrupoRepetido(
                                        grupo = grupo,
                                        alCambiarClave = { entrada -> entradaParaCambioRapido = entrada },
                                        alVerDetalle = { id -> vm.ir(Pantalla.Detalle(id)) }
                                    )
                                }
                            }
                        }
                    }

                    PestanaSalud.COMUNES -> {
                        val filtradas = remember(muyComunes, textoBusqueda) {
                            if (textoBusqueda.isBlank()) muyComunes
                            else muyComunes.filter { it.titulo.contains(textoBusqueda, ignoreCase = true) || it.usuario.contains(textoBusqueda, ignoreCase = true) }
                        }

                        if (filtradas.isEmpty()) {
                            MensajeExitoPestana(
                                icono = Icons.Filled.Check,
                                titulo = "Sin contraseñas filtradas",
                                subtitulo = "Ninguna de tus contraseñas coincide con listas globales de filtraciones públicas."
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    GrupoAjustes(etiqueta = "Contraseñas filtradas o comunes (${filtradas.size})") {
                                        filtradas.forEachIndexed { index, entrada ->
                                            if (index > 0) SeparadorFilaSimple()
                                            FilaProblemaAgil(
                                                entrada = entrada,
                                                etiquetaDetalle = "Filtrada",
                                                colorDetalle = Peligro,
                                                alCambiarRapido = { entradaParaCambioRapido = entrada },
                                                alVerDetalle = { vm.ir(Pantalla.Detalle(entrada.id)) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    PestanaSalud.DEBILES -> {
                        val filtradas = remember(debiles, textoBusqueda) {
                            if (textoBusqueda.isBlank()) debiles
                            else debiles.filter { it.titulo.contains(textoBusqueda, ignoreCase = true) || it.usuario.contains(textoBusqueda, ignoreCase = true) }
                        }

                        if (filtradas.isEmpty()) {
                            MensajeExitoPestana(
                                icono = Icons.Filled.Check,
                                titulo = "Sin contraseñas débiles",
                                subtitulo = "Todas tus contraseñas tienen suficiente longitud y complejidad."
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    GrupoAjustes(etiqueta = "Contraseñas de baja entropía (${filtradas.size})") {
                                        filtradas.forEachIndexed { index, entrada ->
                                            if (index > 0) SeparadorFilaSimple()
                                            val fuerza = MedidorFuerza.medir(entrada.contrasena)
                                            FilaProblemaAgil(
                                                entrada = entrada,
                                                etiquetaDetalle = fuerza.etiqueta,
                                                colorDetalle = Peligro,
                                                alCambiarRapido = { entradaParaCambioRapido = entrada },
                                                alVerDetalle = { vm.ir(Pantalla.Detalle(entrada.id)) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    PestanaSalud.ANTIGUAS -> {
                        val filtradas = remember(antiguas, textoBusqueda) {
                            if (textoBusqueda.isBlank()) antiguas
                            else antiguas.filter { it.titulo.contains(textoBusqueda, ignoreCase = true) || it.usuario.contains(textoBusqueda, ignoreCase = true) }
                        }

                        if (filtradas.isEmpty()) {
                            MensajeExitoPestana(
                                icono = Icons.Filled.Check,
                                titulo = "Contraseñas al día",
                                subtitulo = "Tus credenciales han sido actualizadas recientemente."
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    GrupoAjustes(etiqueta = "Sin actualizar en más de $DIAS_AVISO_ANTIGUEDAD días (${filtradas.size})") {
                                        filtradas.forEachIndexed { index, entrada ->
                                            if (index > 0) SeparadorFilaSimple()
                                            val dias = diasDesde(entrada.modificadaEn, ahora)
                                            FilaProblemaAgil(
                                                entrada = entrada,
                                                etiquetaDetalle = "hace $dias d",
                                                colorDetalle = ColorAcento,
                                                alCambiarRapido = { entradaParaCambioRapido = entrada },
                                                alVerDetalle = { vm.ir(Pantalla.Detalle(entrada.id)) }
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

    // Modal de Cambio Rápido de Contraseña con Generador
    entradaParaCambioRapido?.let { entrada ->
        DialogoCambioRapidoClave(
            entrada = entrada,
            alDescartar = { entradaParaCambioRapido = null },
            alGuardar = { nuevaClave ->
                vm.actualizarContrasenaRapida(entrada.id, nuevaClave)
                entradaParaCambioRapido = null
            },
            alCopiar = { clave ->
                vm.copiar("Contraseña", clave, sensible = true)
            }
        )
    }
}

@Composable
private fun TarjetaGrupoRepetido(
    grupo: List<Entrada>,
    alCambiarClave: (Entrada) -> Unit,
    alVerDetalle: (String) -> Unit
) {
    GrupoAjustes(etiqueta = "${grupo.size} cuentas con la misma clave") {
        grupo.forEachIndexed { index, entrada ->
            if (index > 0) SeparadorFilaSimple()
            FilaProblemaAgil(
                entrada = entrada,
                etiquetaDetalle = "Repetida",
                colorDetalle = Peligro,
                alCambiarRapido = { alCambiarClave(entrada) },
                alVerDetalle = { alVerDetalle(entrada.id) }
            )
        }
    }
}

@Composable
private fun FilaProblemaAgil(
    entrada: Entrada,
    etiquetaDetalle: String,
    colorDetalle: Color,
    alCambiarRapido: () -> Unit,
    alVerDetalle: () -> Unit
) {
    val (iconoTipo, colorTipo) = when (entrada.tipo) {
        TipoEntrada.LOGIN -> Icons.Filled.Lock to ColorSeguridad
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint to ColorPasskeys
        TipoEntrada.NOTA -> Icons.Filled.Description to ColorAcento
        TipoEntrada.TARJETA -> Icons.Filled.CreditCard to ColorGenerador
        TipoEntrada.WIFI -> Icons.Filled.Wifi to ColorSalud
        TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance to ColorSeguridad
        TipoEntrada.IDENTIDAD -> Icons.Filled.Badge to ColorExportacion
        TipoEntrada.SERVIDOR -> Icons.Filled.Dns to ColorIconosInternos
        TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet to ColorAcento
    }
    val colorLegible = colorLegibleParaTema(colorTipo)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { alVerDetalle() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(fondoBadgeParaTema(colorTipo)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconoTipo,
                contentDescription = null,
                tint = colorLegible,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = entrada.titulo.ifBlank { "Sin título" },
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val subtitulo = entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" }
            Text(
                text = subtitulo,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(fondoBadgeParaTema(colorDetalle))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = etiquetaDetalle,
                color = colorLegibleParaTema(colorDetalle),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(Modifier.width(8.dp))

        // Botón de cambio rápido de contraseña
        IconButton(
            onClick = alCambiarRapido,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(fondoBadgeParaTema(Menta))
        ) {
            Icon(
                imageVector = Icons.Filled.AutoFixHigh,
                contentDescription = "Cambiar clave rápida",
                tint = colorLegibleParaTema(Menta),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(4.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Ver detalle",
            tint = ColorAjusteGris,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun DialogoCambioRapidoClave(
    entrada: Entrada,
    alDescartar: () -> Unit,
    alGuardar: (String) -> Unit,
    alCopiar: (String) -> Unit
) {
    val contexto = LocalContext.current
    var longitud by remember { mutableIntStateOf(20) }
    var claveGenerada by remember {
        mutableStateOf(PasswordGenerator.generar(OpcionesGenerador(longitud = 20)))
    }
    val fuerza = remember(claveGenerada) { MedidorFuerza.medir(claveGenerada) }

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = RoundedCornerShape(20.dp),
        containerColor = ColorTarjetaAjustes,
        tonalElevation = 0.dp,
        title = {
            Column {
                Text(
                    text = "Actualizar contraseña rápida",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = ColorTextoAjustes
                )
                Text(
                    text = entrada.titulo.ifBlank { "Sin título" },
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorAjusteGris
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Se ha generado una clave robusta para sustituir la actual:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ColorAjusteGris
                )

                Spacer(Modifier.height(10.dp))

                val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
                val fondoGenerada = if (esOscuro) Color(0xFF161518) else Color(0xFFF4F4F6)
                val bordeGenerada = if (esOscuro) Color(0xFF333238) else Color(0xFFDFDFE3)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(fondoGenerada)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = claveGenerada,
                        style = EstiloMono,
                        color = ColorTextoAjustes,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            claveGenerada = PasswordGenerator.generar(OpcionesGenerador(longitud = longitud))
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Regenerar clave",
                            tint = ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Fortaleza: ${fuerza.etiqueta}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Menta
                    )
                    Text(
                        text = "${claveGenerada.length} caracteres",
                        style = MaterialTheme.typography.labelSmall,
                        color = ColorAjusteGris
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BotonColorido(
                        texto = "Copiar clave",
                        color = ColorAcento,
                        icono = Icons.Filled.ContentCopy,
                        modifier = Modifier.weight(1f)
                    ) {
                        alCopiar(claveGenerada)
                    }

                    val urlValida = entrada.urls.firstOrNull { it.isNotBlank() }
                    if (urlValida != null) {
                        BotonColorido(
                            texto = "Ir al sitio",
                            color = ColorSeguridad,
                            icono = Icons.AutoMirrored.Filled.OpenInNew,
                            modifier = Modifier.weight(1f)
                        ) {
                            try {
                                var u = urlValida.trim()
                                if (!u.startsWith("http://") && !u.startsWith("https://")) u = "https://$u"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(u))
                                contexto.startActivity(intent)
                            } catch (_: Exception) {}
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { alGuardar(claveGenerada) }) {
                Text("Guardar en bóveda", color = Menta, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = ColorAjusteGris)
            }
        }
    )
}

@Composable
private fun MensajeExitoPestana(icono: androidx.compose.ui.graphics.vector.ImageVector, titulo: String, subtitulo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Menta.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Menta,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = ColorTextoAjustes,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = subtitulo,
            style = MaterialTheme.typography.bodyMedium,
            color = ColorAjusteGris,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun FilaMetricaSalud(etiqueta: String, valor: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(etiqueta, color = ColorTextoAjustes, style = MaterialTheme.typography.bodyMedium)
        Text(valor, color = color, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

private fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))
