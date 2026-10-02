package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraInferiorSeleccion
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraSuperiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjusteMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.salud.ContenidoPestanaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.DIAS_AVISO_ANTIGUEDAD
import com.jlnavas3.bovedalocal.ui.pantallas.salud.DialogoCambioRapidoClave
import com.jlnavas3.bovedalocal.ui.pantallas.salud.PestanaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.ResumenAuditoriaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.SelectorPestanasSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.coincideBusquedaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.diasDesde
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
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
    var busquedaVisible by rememberSaveable { mutableStateOf(false) }
    var mostrarModalAuditoria by rememberSaveable { mutableStateOf(false) }
    var entradaParaCambioRapido by remember { mutableStateOf<Entrada?>(null) }
    var seleccionados by rememberSaveable { mutableStateOf(emptySet<String>()) }
    val modoSeleccion = seleccionados.isNotEmpty()
    var confirmarBorradoSeleccion by remember { mutableStateOf(false) }
    val haptica = remember { Haptica(contexto) }

    BackHandler(enabled = modoSeleccion) {
        seleccionados = emptySet()
    }

    val pestanasConDatos = remember(duplicadas.size, muyComunes.size, debiles.size, antiguas.size) {
        buildList {
            if (duplicadas.isNotEmpty()) add(PestanaSalud.REPETIDAS)
            if (muyComunes.isNotEmpty()) add(PestanaSalud.COMUNES)
            if (debiles.isNotEmpty()) add(PestanaSalud.DEBILES)
            if (antiguas.isNotEmpty()) add(PestanaSalud.ANTIGUAS)
        }
    }

    LaunchedEffect(pestanasConDatos) {
        if (pestanaActiva !in pestanasConDatos && pestanasConDatos.isNotEmpty()) {
            pestanaActiva = pestanasConDatos.first()
        }
    }

    LaunchedEffect(claves.size) {
        val resumen = "Auditoría de salud ejecutada: ${claves.size} claves analizadas (${debiles.size} débiles, ${duplicadas.size} grupos repetidos, ${muyComunes.size} comunes, ${antiguas.size} antiguas)"
        Diagnostico.apuntar("salud", resumen)
    }

    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val entradasVisiblesPestana = remember(pestanaActiva, duplicadas, muyComunes, debiles, antiguas, textoBusqueda) {
        val lista = when (pestanaActiva) {
            PestanaSalud.REPETIDAS -> duplicadas.flatten()
            PestanaSalud.COMUNES -> muyComunes
            PestanaSalud.DEBILES -> debiles
            PestanaSalud.ANTIGUAS -> antiguas
        }
        if (textoBusqueda.isBlank()) lista
        else lista.filter { coincideBusquedaSalud(it, textoBusqueda) }
    }
    val todoSeleccionado = entradasVisiblesPestana.isNotEmpty() &&
            entradasVisiblesPestana.all { seleccionados.contains(it.id) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
                .imePadding()
        ) {
            if (modoSeleccion) {
                BarraSuperiorSeleccion(
                    cantidad = seleccionados.size,
                    todoSeleccionado = todoSeleccionado,
                    alCancelar = { seleccionados = emptySet() },
                    alSeleccionarTodo = {
                        seleccionados = entradasVisiblesPestana.map { it.id }.toSet()
                    },
                    alDeseleccionarTodo = { seleccionados = emptySet() }
                )
            } else {
                BarraSuperiorPantalla(
                    titulo = "Salud de la Bóveda",
                    idEtiqueta = "03-LST-SLD",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alVolver = { vm.volverAtras() },
                    colorFondo = ColorAjustesFondo,
                    acciones = {
                        BotonIconoCabecera(
                            onClick = {
                                haptica.toque()
                                busquedaVisible = !busquedaVisible
                                if (!busquedaVisible) textoBusqueda = ""
                            },
                            icono = Icons.Filled.Search,
                            descripcion = "Buscar",
                            tint = if (busquedaVisible || textoBusqueda.isNotBlank()) Ambar else ColorIconosInternos
                        )
                        BotonIconoCabecera(
                            onClick = {
                                haptica.toque()
                                mostrarModalAuditoria = true
                            },
                            icono = Icons.Filled.Analytics,
                            descripcion = "Resumen de auditoría",
                            tint = ColorSalud
                        )
                        var menuAbiertoSalud by remember { mutableStateOf(false) }
                        Box {
                            BotonIconoCabecera(
                                onClick = { menuAbiertoSalud = true },
                                icono = androidx.compose.material.icons.Icons.Filled.MoreVert,
                                descripcion = "Más opciones"
                            )

                            com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda(
                                expanded = menuAbiertoSalud,
                                onDismissRequest = { menuAbiertoSalud = false },
                                modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                            ) {
                                com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                                    texto = "Contraseñas duplicadas...",
                                    icono = androidx.compose.material.icons.Icons.Filled.ContentCopy,
                                    colorIcono = ColorSalud,
                                    onClick = {
                                        menuAbiertoSalud = false
                                        vm.ir(Pantalla.Duplicados())
                                    }
                                )
                                com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu()
                                com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                                    texto = "Ajustes de seguridad...",
                                    icono = androidx.compose.material.icons.Icons.Filled.Security,
                                    colorIcono = ColorSalud,
                                    onClick = {
                                        menuAbiertoSalud = false
                                        vm.ir(Pantalla.Seguridad("01-SEG"))
                                    }
                                )
                                com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu()
                                com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                                    texto = "Generador de contraseñas...",
                                    icono = androidx.compose.material.icons.Icons.Filled.Key,
                                    colorIcono = ColorSalud,
                                    onClick = {
                                        menuAbiertoSalud = false
                                        vm.ir(Pantalla.Generador)
                                    }
                                )
                            }
                        }
                    }
                )
            }

            // Buscador animado desplegable
            AnimatedVisibility(
                visible = !modoSeleccion && (busquedaVisible || textoBusqueda.isNotBlank()),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                // Selector de Pestañas (solo se muestra si hay al menos una pestaña con elementos)
                if (pestanasConDatos.isNotEmpty()) {
                    val gruposDuplicadosFiltrados = remember(duplicadas, textoBusqueda) {
                        val q = textoBusqueda.trim()
                        if (q.isEmpty()) duplicadas.size
                        else duplicadas.count { grupo -> grupo.any { coincideBusquedaSalud(it, q) } }
                    }
                    val totalDuplicadasFiltradas = remember(duplicadas, textoBusqueda) {
                        val q = textoBusqueda.trim()
                        if (q.isEmpty()) duplicadas.sumOf { it.size }
                        else duplicadas.sumOf { grupo -> grupo.count { coincideBusquedaSalud(it, q) } }
                    }
                    val muyComunesFiltrados = remember(muyComunes, textoBusqueda) {
                        val q = textoBusqueda.trim()
                        if (q.isEmpty()) muyComunes.size
                        else muyComunes.count { coincideBusquedaSalud(it, q) }
                    }
                    val debilesFiltrados = remember(debiles, textoBusqueda) {
                        val q = textoBusqueda.trim()
                        if (q.isEmpty()) debiles.size
                        else debiles.count { coincideBusquedaSalud(it, q) }
                    }
                    val antiguasFiltradas = remember(antiguas, textoBusqueda) {
                        val q = textoBusqueda.trim()
                        if (q.isEmpty()) antiguas.size
                        else antiguas.count { coincideBusquedaSalud(it, q) }
                    }

                    SelectorPestanasSalud(
                        pestanaActiva = pestanaActiva,
                        alSeleccionarPestana = {
                            pestanaActiva = it
                            seleccionados = emptySet()
                        },
                        gruposDuplicadosCount = duplicadas.size,
                        totalDuplicadasCount = duplicadas.sumOf { it.size },
                        muyComunesCount = muyComunes.size,
                        debilesCount = debiles.size,
                        antiguasCount = antiguas.size,
                        textoBusqueda = textoBusqueda,
                        gruposDuplicadosFiltrados = gruposDuplicadosFiltrados,
                        totalDuplicadasFiltradas = totalDuplicadasFiltradas,
                        muyComunesFiltrados = muyComunesFiltrados,
                        debilesFiltrados = debilesFiltrados,
                        antiguasFiltradas = antiguasFiltradas
                    )
                    Spacer(Modifier.height(8.dp))
                }

                // Contenido dinámico por pestaña que ocupa toda la pantalla disponible
                ContenidoPestanaSalud(
                    pestanaActiva = pestanaActiva,
                    duplicadas = duplicadas,
                    muyComunes = muyComunes,
                    debiles = debiles,
                    antiguas = antiguas,
                    textoBusqueda = textoBusqueda,
                    ahora = ahora,
                    alCambiarClave = { entrada -> entradaParaCambioRapido = entrada },
                    alVerDetalle = { id -> vm.ir(Pantalla.Detalle(id)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    agruparPorSitio = ajustes.agruparPorSitio,
                    mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                    espaciadoFilas = com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas(ajustes.densidadLista),
                    seleccionActiva = modoSeleccion,
                    seleccionados = seleccionados,
                    alAlternarSeleccion = { id ->
                        seleccionados = if (seleccionados.contains(id)) seleccionados - id else seleccionados + id
                    },
                    alPulsarLargo = { id ->
                        haptica.toque()
                        seleccionados = seleccionados + id
                    }
                )
            }
        }

        if (modoSeleccion) {
            val itemsSeleccionados = remember(entradas, seleccionados) {
                entradas.filter { seleccionados.contains(it.id) }
            }
            val todosSonFavoritos = remember(itemsSeleccionados) {
                itemsSeleccionados.isNotEmpty() && itemsSeleccionados.all { it.favorito }
            }

            BarraInferiorSeleccion(
                cantidad = seleccionados.size,
                todosSonFavoritos = todosSonFavoritos,
                alAlternarFavoritos = {
                    haptica.exito()
                    vm.alternarFavoritosVarias(seleccionados)
                    seleccionados = emptySet()
                },
                alComparar = {
                    haptica.toque()
                    val primera = seleccionados.first()
                    vm.ir(
                        Pantalla.Detalle(
                            id = primera,
                            idsContexto = seleccionados.toList(),
                            modoComparacion = true
                        )
                    )
                },
                alRespaldar = {
                    haptica.tic()
                    val idsParam = seleccionados.joinToString(",")
                    seleccionados = emptySet()
                    vm.ir(Pantalla.ExportarSelectivo("ids:$idsParam"))
                },
                alBorrar = {
                    haptica.error()
                    confirmarBorradoSeleccion = true
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    if (confirmarBorradoSeleccion) {
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar ${seleccionados.size} entrada${if (seleccionados.size > 1) "s" else ""}?",
            mensaje = "Las entradas seleccionadas se enviarán a la papelera. Podrás recuperarlas en los próximos 30 días si lo necesitas.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {
                confirmarBorradoSeleccion = false
                val cant = seleccionados.size
                vm.eliminarVarias(seleccionados)
                vm.avisar("$cant entrada${if (cant > 1) "s enviadas" else " enviada"} a la papelera")
                seleccionados = emptySet()
            },
            alDescartar = { confirmarBorradoSeleccion = false }
        )
    }

    // Modal de Auditoría de Salud
    ModalInferiorBoveda(
        abierto = mostrarModalAuditoria,
        alCerrar = { mostrarModalAuditoria = false },
        titulo = "Auditoría de Salud",
        descripcion = "${claves.size} contraseñas analizadas",
        icono = Icons.Filled.Analytics,
        colorIcono = ColorSalud
    ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (totalSobrantesDuplicadas > 0) {
                    GrupoAjustes(etiqueta = "Duplicados detectados") {
                        FilaAjusteMenu(
                            titulo = "Detectadas $totalSobrantesDuplicadas copias repetidas",
                            subtitulo = "Entradas idénticas de importación. Pulsa para limpiar con 1 toque",
                            icono = Icons.Filled.AutoFixHigh,
                            colorIcono = ColorSalud,
                            alPulsar = {
                                mostrarModalAuditoria = false
                                vm.ir(Pantalla.Duplicados)
                            }
                        )
                    }
                }

                ResumenAuditoriaSalud(
                    clavesCount = claves.size,
                    repetidasCount = duplicadas.sumOf { it.size },
                    muyComunesCount = muyComunes.size,
                    debilesCount = debiles.size,
                    antiguasCount = antiguas.size,
                    expandido = true,
                    alAlternarExpandido = {}
                )
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
