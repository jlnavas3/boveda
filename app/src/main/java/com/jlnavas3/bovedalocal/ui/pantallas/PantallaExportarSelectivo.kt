package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.BarraControlesSeleccionExportar
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ChipsCategoriasExportacion
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.DialogoClaveExportarSelectivo
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.GuardadorBackupSelectivo
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ListaEntradasExportarSelectivo
import com.jlnavas3.bovedalocal.ui.pantallas.exportar.ProveedorCategoriasExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
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

    val densidad = ajustes.densidadLista
    val densidadAltura = when (densidad) {
        "compacta" -> 48.dp
        "comoda" -> 60.dp
        else -> 74.dp
    }
    val densidadMonograma = when (densidad) {
        "compacta" -> 34
        "comoda" -> 40
        else -> 46
    }
    val espaciadoFilas = calcularEspaciadoFilas(densidad)

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

    androidx.compose.runtime.LaunchedEffect(seccionInicial) {
        if (seccionInicial.startsWith("ids:")) {
            val ids = seccionInicial.removePrefix("ids:").split(",").map { it.trim() }.filter { it.isNotEmpty() }
            idsSeleccionados.clear()
            idsSeleccionados.addAll(ids)
        }
    }

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

    val todoMarcado = remember(entradasPaginaBusqueda, idsSeleccionados) {
        entradasPaginaBusqueda.isNotEmpty() && entradasPaginaBusqueda.all { idsSeleccionados.contains(it.id) }
    }

    val formaFab = RoundedCornerShape(CurvaturaEsquinas)
    val exportarHabilitado = idsSeleccionados.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BarraSuperiorPantalla(
                titulo = "Exportación selectiva",
                idEtiqueta = "05-COP-EXP",
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
                        tint = if (busquedaVisible || textoBusqueda.isNotBlank()) ColorAcento else ColorIconosInternos
                    )
                }
            )

            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                DescripcionPantalla(
                    subtitulo = "Marca las entradas específicas que deseas guardar en un archivo .bvda cifrado:"
                )
            }

            if (busquedaVisible) {
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                    ComponenteCampoTexto(
                        valor = textoBusqueda,
                        etiqueta = "Buscar en esta categoría",
                        alCambiar = { textoBusqueda = it },
                        icono = Icons.Filled.Search,
                        mostrarIcono = true,
                        botonLimpiar = true
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Chips dinámicos de categorías (estilo colecciones)
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

            Spacer(Modifier.height(4.dp))

            // Contador de selección
            BarraControlesSeleccionExportar(
                seleccionadas = idsSeleccionados.size,
                total = entradasTotales.size
            )

            Spacer(Modifier.height(4.dp))

            // Lista de entradas filtradas por categoría con dimensiones y espaciados de PantallaLista
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
                modifier = Modifier.weight(1f)
            )
        }

        // Columna vertical de botones flotantes (Deseleccionar todo, Seleccionar todo y Exportar)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Deseleccionar todo (arriba)
            SmallFloatingActionButton(
                onClick = {
                    if (idsSeleccionados.isNotEmpty()) {
                        haptica.tic()
                        idsSeleccionados.clear()
                    }
                },
                containerColor = ColorTarjetaAjustes,
                contentColor = if (idsSeleccionados.isNotEmpty()) TextoPrincipal else TextoSecundario.copy(alpha = 0.35f),
                shape = formaFab,
                modifier = Modifier.then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                    } else Modifier
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Deselect,
                    contentDescription = "Deseleccionar todo",
                    modifier = Modifier.size(20.dp)
                )
            }

            // 2. Seleccionar todo (al medio)
            SmallFloatingActionButton(
                onClick = {
                    haptica.tic()
                    entradasPaginaBusqueda.forEach { if (!idsSeleccionados.contains(it.id)) idsSeleccionados.add(it.id) }
                },
                containerColor = ColorTarjetaAjustes,
                contentColor = if (todoMarcado) ColorAcento else TextoPrincipal,
                shape = formaFab,
                modifier = Modifier.then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                    } else Modifier
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.SelectAll,
                    contentDescription = "Seleccionar todo",
                    modifier = Modifier.size(20.dp)
                )
            }

            // 3. Botón flotante principal: Exportar (con contador debajo del ícono)
            FloatingActionButton(
                onClick = {
                    if (exportarHabilitado) {
                        haptica.tic()
                        dialogoExportar = true
                    }
                },
                containerColor = if (exportarHabilitado) ColorAcento else ColorTarjetaAjustes,
                contentColor = if (exportarHabilitado) ColorSobreAcento else TextoSecundario.copy(alpha = 0.4f),
                shape = formaFab,
                modifier = Modifier.then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                    } else Modifier
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Backup,
                        contentDescription = "Exportar",
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${idsSeleccionados.size}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
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
