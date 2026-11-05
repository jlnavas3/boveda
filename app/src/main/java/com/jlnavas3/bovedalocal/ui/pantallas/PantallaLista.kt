/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia
import com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo
import com.jlnavas3.bovedalocal.data.contactos.LectorArchivoContactos
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.lista.CabeceraPrincipalLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.CajonLateralLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.CapaInferiorAccionesLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ContenidoPrincipalLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogosPantallaLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.rememberEstadoDialogosLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.rememberEstadoSeleccionLista
import com.jlnavas3.bovedalocal.ui.scroll.rememberGruposExpandidosPersistentes
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.DetectorServiciosAccesibilidad
import com.jlnavas3.bovedalocal.util.DiagnosticoAccesibilidad
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(vm: VaultViewModel, estado: EstadoBoveda) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val totalDuplicadas by vm.numDuplicadas.collectAsStateWithLifecycle()
    val totalProblemasSalud by vm.numProblemasSalud.collectAsStateWithLifecycle()

    val duenoCicloVida = LocalLifecycleOwner.current
    var triggerCicloVida by remember { mutableStateOf(0) }
    DisposableEffect(duenoCicloVida) {
        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                triggerCicloVida++
            }
        }
        duenoCicloVida.lifecycle.addObserver(observador)
        onDispose { duenoCicloVida.lifecycle.removeObserver(observador) }
    }
    var diagnosticoAccesibilidad by remember {
        mutableStateOf(
            DiagnosticoAccesibilidad(
                totalActivos = 0,
                servicios = emptyList(),
                serviciosSospechosos = emptyList(),
                esSeguro = true
            )
        )
    }
    LaunchedEffect(ajustes.listaBlancaAccesibilidad, triggerCicloVida) {
        val resultado = withContext(Dispatchers.IO) {
            DetectorServiciosAccesibilidad.evaluar(contexto, ajustes.listaBlancaAccesibilidad)
        }
        diagnosticoAccesibilidad = resultado
    }

    val filtro by vm.filtroTipo.collectAsStateWithLifecycle()
    val soloFavoritos by vm.soloFavoritos.collectAsStateWithLifecycle()
    val solo2fa by vm.solo2fa.collectAsStateWithLifecycle()
    val filtroEtiqueta by vm.filtroEtiqueta.collectAsStateWithLifecycle()
    val criterioOrdenacion by vm.criterioOrdenacion.collectAsStateWithLifecycle()
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
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val categorias = remember(estado) { (estado as? EstadoBoveda.Desbloqueada)?.categorias ?: emptyList() }
    val identidades = remember(estado) { (estado as? EstadoBoveda.Desbloqueada)?.identidades ?: emptyList() }
    val categoriaSeleccionadaId by vm.filtroCategoria.collectAsStateWithLifecycle()
    val identidadSeleccionadaId by vm.identidadSeleccionadaId.collectAsStateWithLifecycle()

    val jerarquia = ajustes.jerarquiaOrganizacionEfectiva

    val (categoriasEfectivas, conteoPorCategoria) = remember(
        entradas, categorias, identidades, identidadSeleccionadaId, jerarquia
    ) {
        if (jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA) {
            com.jlnavas3.bovedalocal.util.filtrarCategoriasPorIdentidad(categorias, entradas, identidades, identidadSeleccionadaId)
        } else {
            val conteos = mutableMapOf<String, Int>()
            for (ent in entradas) {
                for (catId in ent.categorias) {
                    conteos[catId] = (conteos[catId] ?: 0) + 1
                }
            }
            Pair(categorias, conteos)
        }
    }

    val (identidadesEfectivas, conteoPorIdentidad, conteoSinIdentidad) = remember(
        entradas, identidades, categoriaSeleccionadaId, jerarquia
    ) {
        if (jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD) {
            com.jlnavas3.bovedalocal.util.filtrarIdentidadesPorCategoria(identidades, entradas, categoriaSeleccionadaId)
        } else {
            val conteos = identidades.associate { iden ->
                iden.id to entradas.count { entrada ->
                    com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
                }
            }
            val sinIden = entradas.count { entrada ->
                com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(entrada, identidades) == null
            }
            Triple(identidades, conteos, sinIden)
        }
    }

    // Si la categoría seleccionada ya no tiene elementos bajo la identidad activa, deseleccionar
    LaunchedEffect(categoriasEfectivas, categoriaSeleccionadaId, jerarquia) {
        if (jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA &&
            categoriaSeleccionadaId != null &&
            categoriasEfectivas.none { it.id == categoriaSeleccionadaId }
        ) {
            vm.seleccionarCategoria(null)
        }
    }

    // Si la identidad seleccionada ya no tiene elementos bajo la categoría activa, deseleccionar
    LaunchedEffect(identidadesEfectivas, identidadSeleccionadaId, jerarquia) {
        if (jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD &&
            identidadSeleccionadaId != null &&
            identidadSeleccionadaId != "__SIN_IDENTIDAD__" &&
            identidadesEfectivas.none { it.id == identidadSeleccionadaId }
        ) {
            vm.seleccionarIdentidad(null)
        }
    }

    val visibles = remember(entradas, busqueda, filtro, soloFavoritos, solo2fa, filtroEtiqueta, categoriaSeleccionadaId, identidadSeleccionadaId, criterioOrdenacion, identidades) {
        vm.entradasVisibles(entradas, identidades)
    }
    val etiquetasDisponibles = remember(entradas) { vm.etiquetasUsadas() }
    val conteoPorEtiqueta = remember(entradas) {
        entradas.flatMap { it.etiquetas }.groupingBy { it }.eachCount()
    }
    val conteoPorTipo = remember(entradas) {
        entradas.groupingBy { it.tipo }.eachCount()
    }
    val conteo2fa = remember(entradas) {
        entradas.count { it.tieneTotp }
    }

    var busquedaVisible by remember { mutableStateOf(false) }
    var barraPildorasVisible by remember { mutableStateOf(true) }

    LaunchedEffect(busquedaVisible, busqueda) {
        if (busquedaVisible || busqueda.isNotBlank()) {
            barraPildorasVisible = true
        }
    }

    val conexionScrollPildoras = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -12f) {
                    if (barraPildorasVisible) barraPildorasVisible = false
                } else if (available.y > 12f) {
                    if (!barraPildorasVisible) barraPildorasVisible = true
                }
                return Offset.Zero
            }
        }
    }

    val abrirDrawerAlVolver by vm.abrirMenuLateralAlVolverALista.collectAsStateWithLifecycle()
    val estadoCajon = rememberDrawerState(
        initialValue = if (vm.abrirMenuLateralAlVolverALista.value) DrawerValue.Open else DrawerValue.Closed
    )
    val ambitoCorutina = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    fun abrirMenu() = ambitoCorutina.launch {
        focusManager.clearFocus()
        keyboardController?.hide()
        estadoCajon.open()
    }
    fun cerrarMenu() = ambitoCorutina.launch { estadoCajon.close() }

    val actividad = remember(contexto) {
        var c = contexto
        while (c is android.content.ContextWrapper) {
            if (c is android.app.Activity) break
            c = c.baseContext
        }
        c as? android.app.Activity
    }

    val dispararImportacionDirectoCxf: () -> Unit = {
        val act = actividad
        if (act != null) {
            haptica.tic()
            ambitoCorutina.launch {
                when (val res = CxfGestorTransferencia.importarCredenciales(act)) {
                    is ResultadoImportacionCxf.Exito -> {
                        haptica.exito()
                        vm.ir(Pantalla.ConfirmarImportacionCxf(res.jsonPayload))
                    }
                    is ResultadoImportacionCxf.Cancelado -> { }
                    is ResultadoImportacionCxf.SinOpciones -> {
                        haptica.error()
                        vm.mostrarAviso(res.mensaje)
                    }
                    is ResultadoImportacionCxf.Error -> {
                        haptica.error()
                        vm.mostrarError(res.mensaje)
                    }
                }
            }
        }
    }

    LaunchedEffect(abrirDrawerAlVolver) {
        if (abrirDrawerAlVolver) {
            if (!estadoCajon.isOpen) {
                try {
                    estadoCajon.snapTo(DrawerValue.Open)
                } catch (_: Exception) {}
                if (!estadoCajon.isOpen) {
                    try {
                        estadoCajon.open()
                    } catch (_: Exception) {}
                }
            }
            delay(150)
            vm.abrirMenuLateralAlVolverALista.value = false
        }
    }

    val estadoSeleccion = rememberEstadoSeleccionLista(entradas, haptica)
    val dialogos = rememberEstadoDialogosLista()
    var mostrandoModalContactos by remember { mutableStateOf(false) }
    var listaContactosAImportar by remember { mutableStateOf<List<ContactoDispositivo>>(emptyList()) }

    val lanzadorImportarContactos = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        BovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            ambitoCorutina.launch {
                try {
                    val texto = withContext(Dispatchers.IO) {
                        contexto.contentResolver.openInputStream(uri)?.use {
                            it.bufferedReader().readText()
                        }
                    }
                    if (texto.isNullOrBlank()) {
                        vm.avisar("El archivo seleccionado está vacío")
                        return@launch
                    }

                    val contactos = withContext(Dispatchers.Default) {
                        LectorArchivoContactos.parsear(texto)
                    }

                    if (contactos.isNotEmpty()) {
                        listaContactosAImportar = contactos
                        mostrandoModalContactos = true
                    } else {
                        vm.avisar("No se encontraron contactos en formato vCard (.vcf) o CSV en el archivo")
                    }
                } catch (e: Exception) {
                    vm.avisar("Error al procesar archivo de contactos: ${e.message}")
                }
            }
        }
    }

    val dispararImportacionContactos: () -> Unit = {
        BovedaApp.salidaPendiente(contexto)
        try {
            lanzadorImportarContactos.launch(arrayOf(
                "text/vcard",
                "text/x-vcard",
                "text/csv",
                "text/comma-separated-values",
                "text/plain",
                "*/*"
            ))
        } catch (_: Exception) {
            BovedaApp.salidaTerminada(contexto)
            vm.avisar("No se encontró ningún selector de archivos disponible")
        }
    }

    val visiblesIds = remember(visibles) { visibles.map { it.id } }
    val todoSeleccionado = visiblesIds.isNotEmpty() && estadoSeleccion.seleccionados.containsAll(visiblesIds)
    var gruposExpandidos by rememberGruposExpandidosPersistentes(
        "lista_principal",
        vm.gestorGrupos,
        valoresPorDefecto = emptySet()
    )

    BackHandler(enabled = estadoCajon.isOpen || estadoSeleccion.modoSeleccion || busqueda.isNotEmpty()) {
        when {
            estadoCajon.isOpen -> cerrarMenu()
            estadoSeleccion.modoSeleccion -> estadoSeleccion.salirDeSeleccion()
            busqueda.isNotEmpty() -> vm.buscar("")
        }
    }

    val conteosListaCuentas = remember(entradas, estado, totalDuplicadas, totalProblemasSalud) {
        val conteosPorTipo = entradas.groupingBy { it.tipo }.eachCount()
        val numLogin = conteosPorTipo[com.jlnavas3.bovedalocal.data.TipoEntrada.LOGIN] ?: 0
        val numPasskeys = entradas.count { it.passkey != null }
        val numTotp = entradas.count { !it.secretoTotp.isNullOrBlank() }
        val numNota = conteosPorTipo[com.jlnavas3.bovedalocal.data.TipoEntrada.NOTA] ?: 0
        val numTarjeta = conteosPorTipo[com.jlnavas3.bovedalocal.data.TipoEntrada.TARJETA] ?: 0
        val numContacto = conteosPorTipo[com.jlnavas3.bovedalocal.data.TipoEntrada.CONTACTO] ?: 0
        val numWifi = conteosPorTipo[com.jlnavas3.bovedalocal.data.TipoEntrada.WIFI] ?: 0
        val numCuentaBancaria = conteosPorTipo[com.jlnavas3.bovedalocal.data.TipoEntrada.CUENTA_BANCARIA] ?: 0
        val numIdentidad = conteosPorTipo[com.jlnavas3.bovedalocal.data.TipoEntrada.IDENTIDAD] ?: 0
        val numServidor = conteosPorTipo[com.jlnavas3.bovedalocal.data.TipoEntrada.SERVIDOR] ?: 0
        val numWallet = conteosPorTipo[com.jlnavas3.bovedalocal.data.TipoEntrada.WALLET] ?: 0

        val numIdentidades = vm.repositorio.identidades().size
        val numPapelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera?.size ?: vm.repositorio.papelera().size
        val numCategorias = vm.repositorio.categorias().size
        val numDuplicadas = totalDuplicadas
        val numProblemasSalud = totalProblemasSalud

        mapOf(
            "03-LST-LOG" to numLogin,
            "03-LST-PAS" to numPasskeys,
            "04-HER-PAS" to numPasskeys,
            "04-HER-PSK" to numPasskeys,
            "03-LST-2FA" to numTotp,
            "04-HER-2FA" to numTotp,
            "03-LST-NOT" to numNota,
            "03-LST-TAR" to numTarjeta,
            "03-LST-CON" to numContacto,
            "03-LST-WIF" to numWifi,
            "03-LST-BNC" to numCuentaBancaria,
            "03-LST-IDN" to numIdentidad,
            "03-LST-SRV" to numServidor,
            "03-LST-WAL" to numWallet,
            "03-LST-IDE" to numIdentidades,
            "03-LST-SLD" to numProblemasSalud,
            "03-LST-DUP" to numDuplicadas,
            "03-LST-PAP" to numPapelera,
            "03-LST-CAT" to numCategorias
        )
    }

    val avisos by vm.avisosInternos.collectAsStateWithLifecycle()

    CajonLateralLista(
        estadoCajon = estadoCajon,
        ajustes = ajustes,
        totalEntradas = entradas.size,
        totalPapelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera?.size ?: 0,
        totalDuplicadas = totalDuplicadas,
        conteosListaCuentas = conteosListaCuentas,
        perfilArgon2 = vm.repositorio.perfilArgon2Actual(),
        alIr = { destino -> vm.irDesdeMenuLateral(destino) },
        alBloquear = { cerrarMenu(); haptica.toque(); vm.bloquear() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(conexionScrollPildoras)
                .cerrarTecladoAlTocarFuera()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                CabeceraPrincipalLista(
                    modoSeleccion = estadoSeleccion.modoSeleccion,
                    cantidadSeleccionados = estadoSeleccion.seleccionados.size,
                    todoSeleccionado = todoSeleccionado,
                    alCancelarSeleccion = { estadoSeleccion.salirDeSeleccion() },
                    alSeleccionarTodo = { estadoSeleccion.alternarSeleccionarTodo(visiblesIds) },
                    alDeseleccionarTodo = { estadoSeleccion.salirDeSeleccion() },
                    nombreBoveda = ajustes.nombrePersonalizado,
                    totalEntradas = entradas.size,
                    busquedaVisible = busquedaVisible,
                    busqueda = busqueda,
                    filtro = filtro,
                    conteoPorTipo = conteoPorTipo,
                    soloFavoritos = soloFavoritos,
                    solo2fa = solo2fa,
                    conteo2fa = conteo2fa,
                    alSeleccionar2fa = {
                        haptica.tic()
                        vm.filtrarPor2fa(true)
                    },
                    filtroEtiqueta = filtroEtiqueta,
                    criterioOrdenacion = criterioOrdenacion,
                    agruparPorSitio = ajustes.agruparPorSitio,
                    mostrarIndicadoresContenido = ajustes.mostrarIndicadoresContenido,
                    categorias = categoriasEfectivas,
                    categoriaSeleccionadaId = categoriaSeleccionadaId,
                    conteoPorCategoria = conteoPorCategoria,
                    alAbrirMenu = { abrirMenu() },
                    alAlternarBusqueda = {
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible && busqueda.isNotBlank()) {
                            vm.buscar("")
                        }
                    },
                    alCambiarBusqueda = { vm.buscar(it) },
                    alCerrarBusqueda = {
                        busquedaVisible = false
                        vm.buscar("")
                    },
                    alSeleccionarCriterioOrdenacion = { vm.cambiarCriterioOrdenacion(it) },
                    alSeleccionarFiltroTipo = { vm.filtrarPorTipo(it) },
                    alCrearTipo = { tipo -> vm.ir(Pantalla.Editar(id = null, tipoInicial = tipo)) },
                    alAlternarSoloFavoritos = { vm.alternarSoloFavoritos() },
                    alIrNormalizadorTitulos = { vm.ir(Pantalla.NormalizadorTitulos()) },
                    alIrTema = { vm.ir(Pantalla.Tema()) },
                    alIrFormas = { vm.ir(Pantalla.Formas()) },
                    alIrTipografia = { vm.ir(Pantalla.Tipografia()) },
                    alIrOrganizacionGrupo = { vm.ir(Pantalla.OrganizacionLista("02-APA-DES-GRP")) },
                    alIrOrganizacionIndicadores = { vm.ir(Pantalla.ColoresDatos("02-APA-COL")) },
                    alIrIndiceAz = { vm.ir(Pantalla.AjustesIndice()) },
                    alIrTransiciones = { vm.ir(Pantalla.AjustesTransiciones()) },
                    alIrExportarSelectivo = { vm.ir(Pantalla.ExportarSelectivo("todos")) },
                    alIrCopiaSeguridadManual = { vm.ir(Pantalla.CopiaManual("05-COP-MAN-EXP")) },
                    alIrCopiaSeguridad = { vm.ir(Pantalla.CopiaManual("05-COP-MAN-IMP")) },
                    alIrCsvGoogle = { vm.ir(Pantalla.CsvGoogle("05-COP-CSV-IMP")) },
                    alImportarGoogleAuthenticator = { vm.ir(Pantalla.Escaner()) },
                    alImportarDirectoCxf = dispararImportacionDirectoCxf,
                    alImportarContactos = dispararImportacionContactos,
                    alExportarDirectoCxf = { dialogos.exportarCxf = true },
                    alIrCopiaRapida = { vm.ir(Pantalla.AjustesCopiaAutomatica("03.2.1")) },
                    alIrFormatosCampos = { vm.ir(Pantalla.FormatosCampos("03-LST-CAM")) },
                    alIrSeguridadDatos = { vm.ir(Pantalla.SeguridadVisual("01-SEG-BIO-VIS")) },
                    alIrColoresIds = { vm.ir(Pantalla.ColoresDatos("02-APA-DES-IND")) },
                    alIrDatosTecnicosPasskey = { vm.ir(Pantalla.AjustesAutocompletado("04-HER-PSK-TEC")) },
                    alIrUmbralContrasenas = { vm.ir(Pantalla.AuditoriaSeguridad("01-SEG-BIO-ANT")) },
                    alIrContrasenasComunes = { vm.ir(Pantalla.ContrasenasComunes("01-SEG-COM")) },
                    alIrPlantillasPersonalizadas = { vm.ir(Pantalla.PlantillasCampos("02-APA-PLT")) },
                    alIrAjustesGenerales = { vm.ir(Pantalla.Ajustes()) },
                    alAbrirAyuda = { vm.ir(com.jlnavas3.bovedalocal.ui.Pantalla.Ayuda("06-SIS-AYU")) },
                    mostrarCampanaAvisos = ajustes.mostrarCampanaAvisosCabecera,
                    cantidadAvisos = avisos.size,
                    alAbrirAvisos = {
                        if (avisos.isNotEmpty()) {
                            vm.ir(avisos.first().pantallaDestino)
                        }
                    },
                    alAbrirSalud = { vm.ir(com.jlnavas3.bovedalocal.ui.Pantalla.SaludBoveda) },
                    cantidadProblemasSalud = conteosListaCuentas["03-LST-SLD"] ?: 0,
                    alRestablecerFiltros = {
                        vm.filtrarPorTipo(null)
                        vm.filtrarPor2fa(false)
                        if (soloFavoritos) vm.alternarSoloFavoritos()
                        vm.filtrarPorEtiqueta(null)
                    },
                    alSeleccionarCategoria = { id ->
                        haptica.tic()
                        vm.seleccionarCategoria(id)
                    },
                    alCrearCategoria = { dialogos.crearCategoria = true },
                    alEditarCategoria = { cat -> dialogos.categoriaParaEditar = cat },
                    alEliminarCategoria = { cat -> dialogos.categoriaParaEliminar = cat },
                    jerarquiaOrganizacion = jerarquia,
                    modoVisualizacionIdentidades = ajustes.modoVisualizacionIdentidades,
                    identidades = identidadesEfectivas,
                    identidadSeleccionadaId = identidadSeleccionadaId,
                    conteoPorIdentidad = conteoPorIdentidad,
                    conteoSinIdentidad = conteoSinIdentidad,
                    alSeleccionarIdentidad = { id ->
                        haptica.tic()
                        vm.seleccionarIdentidad(id)
                    },
                    barraPildorasVisible = barraPildorasVisible,
                    etiquetasDisponibles = etiquetasDisponibles,
                    alGestionarIdentidades = {
                        haptica.tic()
                        vm.ir(Pantalla.Perfiles("02-APA-DES-GID"))
                    },
                    alGestionarCategorias = {
                        haptica.tic()
                        vm.ir(Pantalla.Categorias("03-LST-CAT"))
                    },
                    conteoPorEtiqueta = conteoPorEtiqueta,
                    alSeleccionarEtiqueta = { et ->
                        haptica.tic()
                        vm.filtrarPorEtiqueta(et)
                    },
                    alLimpiarTipo = {
                        haptica.tic()
                        vm.filtrarPorTipo(null)
                        vm.filtrarPor2fa(false)
                    },
                    entradas = entradas
                )

                ContenidoPrincipalLista(
                    visibles = visibles,
                    entradas = entradas,
                    ajustes = ajustes,
                    criterioOrdenacion = criterioOrdenacion,
                    busqueda = busqueda,
                    filtro = filtro,
                    soloFavoritos = soloFavoritos,
                    filtroEtiqueta = filtroEtiqueta,
                    etiquetasDisponibles = etiquetasDisponibles,
                    modoSeleccion = estadoSeleccion.modoSeleccion,
                    seleccionados = estadoSeleccion.seleccionados,
                    gruposExpandidos = gruposExpandidos,
                    densidadAltura = densidadAltura,
                    densidadMonograma = densidadMonograma,
                    espaciadoFilas = espaciadoFilas,
                    vm = vm,
                    haptica = haptica,
                    alImportarDirectoCxf = dispararImportacionDirectoCxf,
                    alImportarContactos = dispararImportacionContactos,
                    alEntrarEnSeleccion = { estadoSeleccion.entrarEnSeleccion(it) },
                    alAlternarSeleccion = { estadoSeleccion.alternarSeleccion(it) },
                    alEntrarEnSeleccionLote = { estadoSeleccion.entrarEnSeleccionLote(it) },
                    alAlternarSeleccionLote = { estadoSeleccion.alternarSeleccionLote(it) },
                    alAlternarGrupo = { clave ->
                        haptica.tic()
                        gruposExpandidos = if (gruposExpandidos.contains(clave)) {
                            gruposExpandidos - clave
                        } else {
                            gruposExpandidos + clave
                        }
                    },
                    identidades = identidades,
                    categorias = categorias
                )
            }

            CapaInferiorAccionesLista(
                estadoSeleccion = estadoSeleccion,
                entradas = entradas,
                vm = vm,
                haptica = haptica,
                tieneAlertaAccesibilidad = !diagnosticoAccesibilidad.esSeguro,
                alAbrirAuditoriaAccesibilidad = { vm.ir(Pantalla.AuditoriaAccesibilidad()) },
                avisos = avisos,
                alNavegarAviso = { destino -> vm.ir(destino) },
                alTransferirCxf = { copia -> dialogos.prepararTransferirCxf(copia) },
                alRenombrar = { titulo -> dialogos.iniciarRenombrar(titulo) },
                alAsignarCategoria = { dialogos.asignarCategorias = true },
                alSolicitarUnificar = { copia -> dialogos.prepararUnificar(copia) },
                alBorrar = { dialogos.borrarSeleccion = true }
            )
        }
    }

    DialogosPantallaLista(
        dialogos = dialogos,
        vm = vm,
        estado = estado,
        filtroActual = filtro,
        criterioActual = criterioOrdenacion,
        estadoSeleccion = estadoSeleccion,
        entradas = entradas,
        categorias = categorias,
        haptica = haptica
    )

    if (mostrandoModalContactos) {
        com.jlnavas3.bovedalocal.ui.pantallas.contactos.ModalImportarContactos(
            abierto = true,
            contactos = listaContactosAImportar,
            alCerrar = {
                mostrandoModalContactos = false
                listaContactosAImportar = emptyList()
            },
            alImportar = { entradas ->
                vm.importarEntradasContactos(entradas)
                mostrandoModalContactos = false
                listaContactosAImportar = emptyList()
            }
        )
    }
}
