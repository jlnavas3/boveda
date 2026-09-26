package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.BarraBusquedaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjusteMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.salud.DIAS_AVISO_ANTIGUEDAD
import com.jlnavas3.bovedalocal.ui.pantallas.salud.DialogoCambioRapidoClave
import com.jlnavas3.bovedalocal.ui.pantallas.salud.FilaMetricaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.FilaProblemaAgil
import com.jlnavas3.bovedalocal.ui.pantallas.salud.MensajeExitoPestana
import com.jlnavas3.bovedalocal.ui.pantallas.salud.PestanaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.TarjetaGrupoRepetido
import com.jlnavas3.bovedalocal.ui.pantallas.salud.diasDesde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.MedidorFuerza

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
