/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.data.GestorBackupAutomatico
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.BarraAccionesExportarSelectivo
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.BarraSuperiorExportar
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ChipsCategoriasExportacion
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ListaEntradasExportarSelectivo
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.PanelConfiguracionExportacion
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ProveedorCategoriasExportacion
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ejecutarExportacionAuto
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla de exportación (.bvda) que permite guardar entradas en un archivo cifrado con contraseña.
 * - En modo selección previa: muestra únicamente las credenciales preseleccionadas, sin ruido de chips ni búsqueda.
 * - En modo general: permite filtrar y explorar la totalidad de la bóveda.
 */
@Composable
fun PantallaExportarSelectivo(
    vm: VaultViewModel,
    seccionInicial: String = "todos"
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()

    val esSeleccionPrevia = remember(seccionInicial) { seccionInicial.startsWith("ids:") }
    val idsIniciales = remember(seccionInicial) {
        if (seccionInicial.startsWith("ids:")) {
            seccionInicial.removePrefix("ids:").split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        } else {
            emptySet()
        }
    }

    val densidad = ajustes.densidadLista
    val densidadAltura = when (densidad) {
        "compacta" -> 42.dp
        "comoda" -> 54.dp
        else -> 64.dp
    }
    val densidadMonograma = when (densidad) {
        "compacta" -> 30
        "comoda" -> 36
        else -> 40
    }
    val espaciadoFilas = calcularEspaciadoFilas(densidad)

    val entradasTotales = remember(estadoBoveda, esSeleccionPrevia, idsIniciales) {
        val desbloqueada = estadoBoveda as? EstadoBoveda.Desbloqueada ?: return@remember emptyList()
        val activas = desbloqueada.entradas.filter { it.eliminadaEn == 0L }
        if (esSeleccionPrevia) activas.filter { idsIniciales.contains(it.id) } else activas
    }

    val todasCategorias = remember { ProveedorCategoriasExportacion.obtenerTodas() }

    val categoriasDisponibles = remember(entradasTotales, todasCategorias, esSeleccionPrevia) {
        if (esSeleccionPrevia) emptyList()
        else todasCategorias.filter { cat ->
            if (cat.id == "todos") entradasTotales.isNotEmpty()
            else entradasTotales.any(cat.filtro)
        }
    }

    var seccionActiva by remember(seccionInicial, categoriasDisponibles) {
        val existe = categoriasDisponibles.any { it.id == seccionInicial }
        mutableStateOf(if (existe) seccionInicial else (categoriasDisponibles.firstOrNull()?.id ?: "todos"))
    }

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    val idsSeleccionados = remember { mutableStateListOf<String>() }

    LaunchedEffect(seccionInicial, idsIniciales) {
        if (esSeleccionPrevia) {
            idsSeleccionados.clear()
            idsSeleccionados.addAll(idsIniciales)
        }
    }

    var patronNombre by remember { mutableStateOf("{##}-selectivo-{FECHA}_{HORA}") }
    var clavePassword by remember { mutableStateOf("") }
    var mostrarClave by remember { mutableStateOf(false) }
    var usarAutoPassword by remember { mutableStateOf(false) }
    var guardarEnDirectorioAuto by remember { mutableStateOf(true) }
    var passwordAUsar by remember { mutableStateOf("") }

    val autoPassword = ajustes.backupAutoPasswordCifrado
    val tieneAutoPassword = autoPassword.isNotBlank()

    val secuencia = remember(contexto) {
        try {
            val dir = GestorBackupAutomatico.obtenerDirectorioBackups(contexto)
            GestorBackupAutomatico.calcularSiguienteSecuencia(dir, 0)
        } catch (_: Exception) {
            1
        }
    }

    val nombreFinalResuelto = remember(patronNombre, secuencia) {
        val base = GestorBackupAutomatico.resolverNombreArchivo(patronNombre, secuencia)
        if (base.endsWith(".bvda", ignoreCase = true)) base else "$base.bvda"
    }

    val lanzadorGuardarBvda = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        BovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            vm.exportarSelectivo(idsSeleccionados.toSet(), passwordAUsar) { bytes ->
                contexto.contentResolver.openOutputStream(uri)?.use { it.write(bytes) }
            }
        }
    }

    val categoriaActual = remember(seccionActiva, todasCategorias) {
        todasCategorias.firstOrNull { it.id == seccionActiva } ?: todasCategorias.first()
    }

    val entradasPagina = remember(entradasTotales, categoriaActual, esSeleccionPrevia) {
        if (esSeleccionPrevia) entradasTotales
        else entradasTotales.filter(categoriaActual.filtro)
    }

    val entradasPaginaBusqueda = remember(entradasPagina, textoBusqueda, esSeleccionPrevia) {
        val q = textoBusqueda.trim().lowercase()
        if (q.isBlank() || esSeleccionPrevia) entradasPagina
        else entradasPagina.filter {
            it.titulo.lowercase().contains(q) || it.usuario.lowercase().contains(q)
        }
    }

    val exportarHabilitado = idsSeleccionados.isNotEmpty()

    val subtitulo = remember(idsSeleccionados.size, entradasTotales.size, esSeleccionPrevia) {
        if (esSeleccionPrevia) {
            val count = idsSeleccionados.size
            if (count == 1) "1 credencial seleccionada" else "$count seleccionadas"
        } else if (idsSeleccionados.isNotEmpty()) {
            "${idsSeleccionados.size} de ${entradasTotales.size} seleccionadas"
        } else {
            "${entradasTotales.size} credenciales"
        }
    }

    val ejecutarExportacion: () -> Unit = {
        if (!exportarHabilitado) {
            // Sin selección
        } else if (clavePassword.isBlank()) {
            haptica.error()
            vm.avisar("Introduce una contraseña para cifrar el archivo")
        } else {
            haptica.exito()
            passwordAUsar = clavePassword
            if (guardarEnDirectorioAuto) {
                ejecutarExportacionAuto(
                    contexto = contexto,
                    vm = vm,
                    ids = idsSeleccionados.toSet(),
                    password = clavePassword,
                    nombreBruto = nombreFinalResuelto
                )
            } else {
                BovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorGuardarBvda.launch(nombreFinalResuelto)
                } catch (e: Exception) {
                    BovedaApp.salidaTerminada(contexto)
                    vm.avisar("No se encontró ningún selector de archivos para guardar")
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorExportar(
            subtitulo = subtitulo,
            mostrarId = ajustes.mostrarIdsAjustes,
            esSeleccionPrevia = esSeleccionPrevia,
            busquedaVisible = busquedaVisible,
            textoBusqueda = textoBusqueda,
            totalDisponibles = entradasTotales.size,
            alVolver = { vm.volverAtras() },
            alAlternarBusqueda = {
                haptica.tic()
                busquedaVisible = !busquedaVisible
                if (!busquedaVisible) textoBusqueda = ""
            },
            alCambiarTextoBusqueda = { textoBusqueda = it },
            alCerrarBusqueda = {
                busquedaVisible = false
                textoBusqueda = ""
            }
        )

        val cabeceraLista: (@Composable () -> Unit)? = if (!esSeleccionPrevia && categoriasDisponibles.size > 1) {
            {
                ChipsCategoriasExportacion(
                    categorias = categoriasDisponibles,
                    categoriaActivaId = seccionActiva,
                    alSeleccionarCategoria = { seccionActiva = it },
                    totalPorCategoria = { cat ->
                        if (cat.id == "todos") entradasTotales.size
                        else entradasTotales.count(cat.filtro)
                    },
                    onTic = { haptica.tic() },
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        } else null

        ListaEntradasExportarSelectivo(
            entradas = entradasPaginaBusqueda,
            idsSeleccionados = idsSeleccionados,
            alAlternarSeleccion = { id, checked ->
                haptica.tic()
                if (checked) idsSeleccionados.add(id)
                else idsSeleccionados.remove(id)
            },
            espaciadoFilas = espaciadoFilas,
            densidadAltura = densidadAltura,
            densidadMonograma = densidadMonograma,
            cabecera = cabeceraLista,
            modifier = Modifier.weight(1f)
        )

        PanelConfiguracionExportacion(
            patronNombre = patronNombre,
            alCambiarPatronNombre = { patronNombre = it },
            nombreResuelto = nombreFinalResuelto,
            clavePassword = clavePassword,
            alCambiarClavePassword = {
                clavePassword = it
                usarAutoPassword = (it == autoPassword && tieneAutoPassword)
            },
            mostrarClave = mostrarClave,
            alAlternarMostrarClave = {
                haptica.tic()
                mostrarClave = !mostrarClave
            },
            guardarEnDirectorioAuto = guardarEnDirectorioAuto,
            alCambiarGuardarEnDirectorioAuto = {
                haptica.tic()
                guardarEnDirectorioAuto = it
            },
            usarAutoPassword = usarAutoPassword,
            alCambiarUsarAutoPassword = { checked ->
                if (tieneAutoPassword) {
                    haptica.tic()
                    usarAutoPassword = checked
                    clavePassword = if (checked) autoPassword else ""
                }
            },
            tieneAutoPassword = tieneAutoPassword
        )

        BarraAccionesExportarSelectivo(
            idsSeleccionadosCount = idsSeleccionados.size,
            exportarHabilitado = exportarHabilitado,
            alExportar = ejecutarExportacion
        )
    }
}
