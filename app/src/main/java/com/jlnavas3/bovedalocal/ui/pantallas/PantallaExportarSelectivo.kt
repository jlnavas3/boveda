package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.BarraControlesSeleccionExportar
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.BotonesAccionInferioresExportar
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ChipsCategoriasExportacion
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.DialogoClaveExportarSelectivo
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.GuardadorBackupSelectivo
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ListaEntradasExportarSelectivo
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ProveedorCategoriasExportacion
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla de exportación selectiva que permite marcar entradas por categorías y
 * exportarlas a un archivo .bvda cifrado con contraseña.
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

    val entradasTotales = remember(estadoBoveda) {
        val desbloqueada = estadoBoveda as? EstadoBoveda.Desbloqueada ?: return@remember emptyList()
        desbloqueada.entradas.filter { it.eliminadaEn == 0L }
    }

    // Definición de categorías usando el proveedor centralizado
    val todasCategorias = remember { ProveedorCategoriasExportacion.obtenerTodas() }

    // Filtro dinámico: solo mostrar categorías que tengan al menos 1 entrada
    val categoriasDisponibles = remember(entradasTotales, todasCategorias) {
        todasCategorias.filter { cat ->
            if (cat.id == "todos") entradasTotales.isNotEmpty()
            else entradasTotales.any(cat.filtro)
        }
    }

    var seccionActiva by remember(seccionInicial, categoriasDisponibles) {
        val existe = categoriasDisponibles.any { it.id == seccionInicial }
        mutableStateOf(if (existe) seccionInicial else (categoriasDisponibles.firstOrNull()?.id ?: "todos"))
    }

    // Estado para campo de búsqueda en la barra superior
    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Persistencia de IDs seleccionados en memoria
    val idsSeleccionados = remember { mutableStateListOf<String>() }

    var dialogoExportar by remember { mutableStateOf(false) }
    var passwordAUsar by remember { mutableStateOf("") }

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

    val entradasPagina = remember(entradasTotales, categoriaActual) {
        entradasTotales.filter(categoriaActual.filtro)
    }

    val entradasPaginaBusqueda = remember(entradasPagina, textoBusqueda) {
        val q = textoBusqueda.trim().lowercase()
        if (q.isBlank()) entradasPagina
        else entradasPagina.filter {
            it.titulo.lowercase().contains(q) || it.usuario.lowercase().contains(q)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Exportación selectiva",
            idEtiqueta = "02.1",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo,
            acciones = {
                BotonIconoCabecera(
                    onClick = {
                        haptica.tic()
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible) textoBusqueda = ""
                    },
                    icono = Icons.Filled.Search,
                    descripcion = "Buscar entradas",
                    tint = if (busquedaVisible || textoBusqueda.isNotBlank()) Ambar else ColorIconosInternos
                )

                BotonIconoCabecera(
                    onClick = {
                        if (idsSeleccionados.isNotEmpty()) {
                            haptica.tic()
                            dialogoExportar = true
                        }
                    },
                    icono = Icons.Filled.Check,
                    descripcion = "Confirmar exportación",
                    tint = if (idsSeleccionados.isNotEmpty()) ColorAcento else ColorIconosInternos.copy(alpha = 0.3f),
                    habilitado = idsSeleccionados.isNotEmpty()
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = "Marca las entradas específicas que deseas guardar en un archivo .bvda cifrado:"
            )

            if (busquedaVisible) {
                Spacer(Modifier.height(8.dp))
                ComponenteCampoTexto(
                    valor = textoBusqueda,
                    etiqueta = "Buscar en esta categoría",
                    alCambiar = { textoBusqueda = it },
                    icono = Icons.Filled.Search,
                    mostrarIcono = true,
                    botonLimpiar = true
                )
            }

            Spacer(Modifier.height(10.dp))

            // Chips dinámicos de categorías
            ChipsCategoriasExportacion(
                categorias = categoriasDisponibles,
                categoriaActivaId = seccionActiva,
                alSeleccionarCategoria = { seccionActiva = it },
                totalPorCategoria = { cat ->
                    if (cat.id == "todos") entradasTotales.size
                    else entradasTotales.count(cat.filtro)
                },
                onTic = { haptica.tic() }
            )

            if (categoriasDisponibles.size > 1) {
                Spacer(Modifier.height(10.dp))
            }

            // Barra de controles de selección rápida
            BarraControlesSeleccionExportar(
                seleccionadas = idsSeleccionados.size,
                total = entradasTotales.size,
                alMarcarVisibles = {
                    haptica.tic()
                    entradasPaginaBusqueda.forEach { if (!idsSeleccionados.contains(it.id)) idsSeleccionados.add(it.id) }
                },
                alDeseleccionarTodas = {
                    haptica.tic()
                    idsSeleccionados.clear()
                }
            )

            Spacer(Modifier.height(4.dp))

            // Lista de entradas filtradas por categoría
            ListaEntradasExportarSelectivo(
                entradas = entradasPaginaBusqueda,
                idsSeleccionados = idsSeleccionados,
                alAlternarSeleccion = { id, checked ->
                    haptica.tic()
                    if (checked) idsSeleccionados.add(id)
                    else idsSeleccionados.remove(id)
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.height(12.dp))

            // Botones inferiores en Fila Horizontal (50% - 50%)
            BotonesAccionInferioresExportar(
                cantidadSeleccionada = idsSeleccionados.size,
                alExportar = {
                    haptica.tic()
                    dialogoExportar = true
                },
                alCancelar = {
                    vm.volverAtras()
                }
            )

            Spacer(Modifier.height(16.dp))
        }
    }

    if (dialogoExportar) {
        DialogoClaveExportarSelectivo(
            cantidad = idsSeleccionados.size,
            autoPassword = ajustes.backupAutoPasswordCifrado,
            alDescartar = { dialogoExportar = false },
            alConfirmar = { nombreArchivoResolved, clavePass, directoAuto ->
                dialogoExportar = false
                passwordAUsar = clavePass
                if (directoAuto) {
                    GuardadorBackupSelectivo.ejecutarExportacionAuto(
                        contexto = contexto,
                        vm = vm,
                        ids = idsSeleccionados.toSet(),
                        password = clavePass,
                        nombreBruto = nombreArchivoResolved
                    )
                } else {
                    BovedaApp.salidaPendiente(contexto)
                    try {
                        lanzadorGuardarBvda.launch(nombreArchivoResolved)
                    } catch (e: Exception) {
                        BovedaApp.salidaTerminada(contexto)
                        vm.avisar("No se encontró ningún selector de archivos para guardar")
                    }
                }
            }
        )
    }
}
