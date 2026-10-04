package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.activity.compose.BackHandler
import com.jlnavas3.bovedalocal.data.Entrada
import kotlinx.coroutines.launch
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraInferiorSeleccion
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraSuperiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoBorrarSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoRenombrarSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.BarraSuperiorPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.EstadoVacioPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.FilaPasskey
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.Haptica
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PantallaPasskeys(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val todasLasPasskeys = remember(vm.repositorio.entradas()) { vm.repositorio.passkeys() }
    val formato = remember { SimpleDateFormat("d MMM yyyy", Locale.forLanguageTag("es-ES")) }
    val scrollState = rememberScrollState()
    val espaciadoFilas = calcularEspaciadoFilas(ajustes.densidadLista)

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }
    var criterioOrdenacion by remember { mutableStateOf(CriterioOrdenacion.NOMBRE_AZ) }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }

    val ambitoCorutina = androidx.compose.runtime.rememberCoroutineScope()
    val actividad = remember(contexto) {
        var c = contexto
        while (c is android.content.ContextWrapper) {
            if (c is android.app.Activity) break
            c = c.baseContext
        }
        c as? android.app.Activity
    }

    val dispararImportacionPasskeys: () -> Unit = {
        val act = actividad
        if (act != null) {
            haptica.tic()
            ambitoCorutina.launch {
                when (val res = com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.importarCredenciales(act)) {
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.Exito -> {
                        haptica.exito()
                        vm.ir(Pantalla.ConfirmarImportacionCxf(res.jsonPayload))
                    }
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.Cancelado -> {
                        // Cancelado por el usuario
                    }
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.SinOpciones -> {
                        haptica.error()
                        vm.mostrarAviso(res.mensaje)
                    }
                    is com.jlnavas3.bovedalocal.cxf.ResultadoImportacionCxf.Error -> {
                        haptica.error()
                        vm.mostrarError(res.mensaje)
                    }
                }
            }
        }
    }

    var seleccionados by remember { mutableStateOf(setOf<String>()) }
    val modoSeleccion = seleccionados.isNotEmpty()
    var dialogoRenombrarSeleccion by remember { mutableStateOf(false) }
    var nuevoTituloRenombrar by remember { mutableStateOf("") }
    var dialogoBorrarSeleccion by remember { mutableStateOf(false) }
    var mostrarDialogoExportarCxf by remember { mutableStateOf(false) }
    var entradasParaTransferirCxf by remember { mutableStateOf<List<Entrada>?>(null) }

    BackHandler(enabled = modoSeleccion) {
        seleccionados = emptySet()
    }

    val passkeysFiltradas = remember(todasLasPasskeys, textoBusqueda, soloFavoritos, criterioOrdenacion) {
        val q = textoBusqueda.trim().lowercase()
        todasLasPasskeys
            .filter { entrada ->
                val datos = entrada.passkey ?: return@filter false
                val coincideTexto = if (q.isBlank()) true else {
                    entrada.titulo.lowercase().contains(q) ||
                    datos.rpName.lowercase().contains(q) ||
                    datos.rpId.lowercase().contains(q) ||
                    datos.usuario.lowercase().contains(q) ||
                    entrada.usuario.lowercase().contains(q)
                }
                val coincideFavorito = if (soloFavoritos) entrada.favorito else true
                coincideTexto && coincideFavorito
            }
            .sortedWith { a, b ->
                val datosA = a.passkey
                val datosB = b.passkey
                val nombreA = datosA?.rpName?.ifBlank { datosA.rpId } ?: a.titulo
                val nombreB = datosB?.rpName?.ifBlank { datosB.rpId } ?: b.titulo
                when (criterioOrdenacion) {
                    CriterioOrdenacion.NOMBRE_AZ -> nombreA.compareTo(nombreB, ignoreCase = true)
                    CriterioOrdenacion.NOMBRE_ZA -> nombreB.compareTo(nombreA, ignoreCase = true)
                    CriterioOrdenacion.MODIFICACION_RECIENTE -> b.modificadaEn.compareTo(a.modificadaEn)
                    CriterioOrdenacion.ANTIGUEDAD -> a.creadaEn.compareTo(b.creadaEn)
                    CriterioOrdenacion.CREACION_RECIENTE -> b.creadaEn.compareTo(a.creadaEn)
                    CriterioOrdenacion.USO_RECIENTE -> b.ultimoUsoEn.compareTo(a.ultimoUsoEn)
                    CriterioOrdenacion.IGNORADAS -> b.ignoradaEnSalud.compareTo(a.ignoradaEnSalud)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        if (modoSeleccion) {
            BarraSuperiorSeleccion(
                cantidad = seleccionados.size,
                todoSeleccionado = passkeysFiltradas.isNotEmpty() && seleccionados.size == passkeysFiltradas.size,
                alCancelar = {
                    haptica.tic()
                    seleccionados = emptySet()
                },
                alSeleccionarTodo = {
                    haptica.tic()
                    seleccionados = passkeysFiltradas.map { it.id }.toSet()
                },
                alDeseleccionarTodo = {
                    haptica.tic()
                    seleccionados = emptySet()
                }
            )
        } else {
            BarraSuperiorPasskeys(
                conSeparador = scrollState.value > 0,
                busquedaVisible = busquedaVisible,
                textoBusqueda = textoBusqueda,
                soloFavoritos = soloFavoritos,
                criterioOrdenacion = criterioOrdenacion,
                menuOpcionesDesplegado = menuOpcionesDesplegado,
                haptica = haptica,
                idEtiqueta = "04-HER-PSK",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                alAlternarBusqueda = {
                    busquedaVisible = !busquedaVisible
                    if (!busquedaVisible) textoBusqueda = ""
                },
                alCambiarTextoBusqueda = { textoBusqueda = it },
                alCerrarBusqueda = {
                    busquedaVisible = false
                    textoBusqueda = ""
                },
                alAbrirMenu = { menuOpcionesDesplegado = true },
                alCerrarMenu = { menuOpcionesDesplegado = false },
                alAbrirOrdenacion = {
                    menuOpcionesDesplegado = false
                    mostrarDialogoOrdenacion = true
                },
                alAlternarFavoritos = {
                    menuOpcionesDesplegado = false
                    haptica.tic()
                    soloFavoritos = !soloFavoritos
                },
                alIrExportacionSelectiva = {
                    menuOpcionesDesplegado = false
                    haptica.tic()
                    vm.ir(Pantalla.ExportarSelectivo("passkeys"))
                },
                alIrSeguridadBiometria = {
                    vm.ir(Pantalla.Seguridad("01-SEG-BIO"))
                },
                alIrCopiaSeguridad = {
                    vm.ir(Pantalla.CopiaSeguridad("05-COP-SEG"))
                },
                alImportarPasskeys = dispararImportacionPasskeys,
                alExportarDirectoCxf = { mostrarDialogoExportarCxf = true },
                alRestablecerFiltros = {
                    menuOpcionesDesplegado = false
                    haptica.tic()
                    textoBusqueda = ""
                    soloFavoritos = false
                    criterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (passkeysFiltradas.isEmpty()) "Sin llaves registradas" else "${passkeysFiltradas.size} llave${if (passkeysFiltradas.size == 1) "" else "s"} de paso almacenada${if (passkeysFiltradas.size == 1) "" else "s"}"
            )

            Spacer(Modifier.height(10.dp))

            if (passkeysFiltradas.isEmpty()) {
                EstadoVacioPasskeys(
                    sinPasskeysEnTotal = todasLasPasskeys.isEmpty(),
                    alImportarPasskeys = dispararImportacionPasskeys
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
                ) {
                    passkeysFiltradas.forEach { entrada ->
                        FilaPasskey(
                            entrada = entrada,
                            formato = formato,
                            haptica = haptica,
                            seleccionActiva = modoSeleccion,
                            seleccionado = seleccionados.contains(entrada.id),
                            alPulsarLargo = {
                                haptica.toque()
                                seleccionados = seleccionados + entrada.id
                            },
                            alAlternarSeleccion = {
                                seleccionados = if (seleccionados.contains(entrada.id)) {
                                    seleccionados - entrada.id
                                } else {
                                    seleccionados + entrada.id
                                }
                            },
                            alPulsar = { vm.ir(Pantalla.Detalle(entrada.id, idsContexto = passkeysFiltradas.map { it.id })) },
                            alAlternarFavorito = {
                                haptica.tic()
                                vm.alternarFavorito(entrada.id)
                            },
                            ajustes = ajustes,
                            mostrarIndicadores = ajustes.mostrarIndicadoresContenido
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        if (modoSeleccion) {
            val itemsSeleccionados = remember(todasLasPasskeys, seleccionados) {
                todasLasPasskeys.filter { seleccionados.contains(it.id) }
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
                alTransferirCxf = {
                    haptica.tic()
                    val copia = itemsSeleccionados.toList()
                    seleccionados = emptySet()
                    entradasParaTransferirCxf = copia
                },
                alRenombrar = {
                    haptica.tic()
                    val primerSeleccionado = todasLasPasskeys.find { it.id == seleccionados.firstOrNull() }
                    nuevoTituloRenombrar = primerSeleccionado?.titulo ?: ""
                    dialogoRenombrarSeleccion = true
                },
                alBorrar = {
                    haptica.error()
                    dialogoBorrarSeleccion = true
                }
            )
        }
    }

    if (mostrarDialogoOrdenacion) {
        DialogoOrdenacionLista(
            criterioActual = criterioOrdenacion,
            alSeleccionarCriterio = { criterio ->
                haptica.tic()
                criterioOrdenacion = criterio
                mostrarDialogoOrdenacion = false
            },
            alCerrar = { mostrarDialogoOrdenacion = false }
        )
    }

    if (dialogoBorrarSeleccion) {
        DialogoBorrarSeleccion(
            cantidad = seleccionados.size,
            alConfirmar = {
                haptica.exito()
                vm.eliminarVarias(seleccionados)
                seleccionados = emptySet()
                dialogoBorrarSeleccion = false
            },
            alDescartar = { dialogoBorrarSeleccion = false }
        )
    }

    if (dialogoRenombrarSeleccion) {
        DialogoRenombrarSeleccion(
            cantidad = seleccionados.size,
            textoNuevoTitulo = nuevoTituloRenombrar,
            alCambiarTexto = { nuevoTituloRenombrar = it },
            alConfirmar = {
                haptica.exito()
                vm.renombrarVarias(seleccionados, nuevoTituloRenombrar)
                seleccionados = emptySet()
                dialogoRenombrarSeleccion = false
            },
            alDescartar = { dialogoRenombrarSeleccion = false }
        )
    }

    entradasParaTransferirCxf?.let { passkeysSeleccionadas ->
        com.jlnavas3.bovedalocal.ui.pantallas.cxf.DialogoExportacionDirectaCxf(
            entradas = passkeysSeleccionadas,
            esSeleccionPersonalizada = true,
            alCerrar = { entradasParaTransferirCxf = null }
        )
    }

    if (mostrarDialogoExportarCxf) {
        com.jlnavas3.bovedalocal.ui.pantallas.cxf.DialogoExportacionDirectaCxf(
            entradas = todasLasPasskeys,
            esSeleccionPersonalizada = false,
            alCerrar = { mostrarDialogoExportarCxf = false }
        )
    }
}
