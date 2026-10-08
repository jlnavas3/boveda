package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraSuperiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.salud.BarraAccionesSeleccionSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.BarraSuperiorSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.ContenidoPestanaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.DialogoBorradoSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.DialogoCambioRapidoClave
import com.jlnavas3.bovedalocal.ui.pantallas.salud.ModalAuditoriaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.PestanaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.SelectorPestanasSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.coincideBusquedaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.diasDesde
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.MedidorFuerza

/**
 * Pantalla de auditoría de salud de la bóveda:
 * Identifica contraseñas repetidas, débiles, comunes, antiguas e ignoradas.
 */
@Composable
fun PantallaSaludBoveda(
    vm: VaultViewModel,
    estado: EstadoBoveda,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val ahora = remember { System.currentTimeMillis() }
    val datosSalud = com.jlnavas3.bovedalocal.ui.pantallas.salud.rememberDatosSaludBoveda(
        entradas = entradas,
        umbralDias = ajustes.umbralAntiguedadDias,
        ahora = ahora,
        contexto = contexto
    )
    val claves = datosSalud.claves
    val ignoradas = datosSalud.ignoradas
    val duplicadas = datosSalud.duplicadas
    val debiles = datosSalud.debiles
    val muyComunes = datosSalud.muyComunes
    val antiguas = datosSalud.antiguas
    val totalSobrantesDuplicadas = datosSalud.totalSobrantesDuplicadas
    val pestanasConDatos = datosSalud.pestanasConDatos

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

    LaunchedEffect(pestanasConDatos) {
        if (pestanaActiva !in pestanasConDatos && pestanasConDatos.isNotEmpty()) {
            pestanaActiva = pestanasConDatos.first()
        }
    }

    LaunchedEffect(claves.size) {
        val resumen = "Auditoría de salud ejecutada: ${claves.size} claves analizadas (${debiles.size} débiles, ${duplicadas.size} grupos repetidos, ${muyComunes.size} comunes, ${antiguas.size} antiguas, ${ignoradas.size} ignoradas)"
        Diagnostico.apuntar("salud", resumen)
    }

    val entradasVisiblesPestana = remember(pestanaActiva, duplicadas, muyComunes, debiles, antiguas, ignoradas, textoBusqueda) {
        val lista = when (pestanaActiva) {
            PestanaSalud.REPETIDAS -> duplicadas.flatten()
            PestanaSalud.COMUNES -> muyComunes
            PestanaSalud.DEBILES -> debiles
            PestanaSalud.ANTIGUAS -> antiguas
            PestanaSalud.IGNORADAS -> ignoradas
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
                .cerrarTecladoAlTocarFuera()
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
                BarraSuperiorSalud(
                    mostrarId = ajustes.mostrarIdsAjustes,
                    busquedaVisible = busquedaVisible,
                    textoBusqueda = textoBusqueda,
                    haptica = haptica,
                    alVolver = { vm.volverAtras() },
                    alAlternarBusqueda = {
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible) textoBusqueda = ""
                    },
                    alAbrirAuditoria = { mostrarModalAuditoria = true },
                    alIrDuplicados = { vm.ir(Pantalla.Duplicados) },
                    alIrSeguridad = { vm.ir(Pantalla.Seguridad("01-SEG")) },
                    alIrGenerador = { vm.ir(Pantalla.Generador) }
                )
            }

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
                if (pestanasConDatos.isNotEmpty()) {
                    SelectorPestanasSalud(
                        pestanaActiva = pestanaActiva,
                        alSeleccionarPestana = {
                            pestanaActiva = it
                            seleccionados = emptySet()
                        },
                        duplicadas = duplicadas,
                        muyComunes = muyComunes,
                        debiles = debiles,
                        antiguas = antiguas,
                        ignoradas = ignoradas,
                        textoBusqueda = textoBusqueda
                    )
                    Spacer(Modifier.height(8.dp))
                }

                ContenidoPestanaSalud(
                    pestanaActiva = pestanaActiva,
                    duplicadas = duplicadas,
                    muyComunes = muyComunes,
                    debiles = debiles,
                    antiguas = antiguas,
                    ignoradas = ignoradas,
                    textoBusqueda = textoBusqueda,
                    ahora = ahora,
                    alCambiarClave = { entrada -> entradaParaCambioRapido = entrada },
                    alVerDetalle = { id -> vm.ir(Pantalla.Detalle(id)) },
                    alIgnorar = { entrada -> vm.alternarIgnorarSalud(entrada.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    ajustes = ajustes,
                    agruparPorSitio = ajustes.agruparPorSitio,
                    mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                    espaciadoFilas = calcularEspaciadoFilas(ajustes.densidadLista),
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
            BarraAccionesSeleccionSalud(
                seleccionados = seleccionados,
                entradas = entradas,
                haptica = haptica,
                alAlternarFavoritos = { ids ->
                    vm.alternarFavoritosVarias(ids)
                    seleccionados = emptySet()
                },
                alComparar = { primera, todos ->
                    vm.ir(
                        Pantalla.Detalle(
                            id = primera,
                            idsContexto = todos,
                            modoComparacion = true
                        )
                    )
                },
                alAbrirBorrado = { confirmarBorradoSeleccion = true },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    DialogoBorradoSalud(
        visible = confirmarBorradoSeleccion,
        cantidad = seleccionados.size,
        alConfirmar = {
            confirmarBorradoSeleccion = false
            val cant = seleccionados.size
            vm.eliminarVarias(seleccionados)
            vm.avisar("$cant entrada${if (cant > 1) "s enviadas" else " enviada"} a la papelera")
            seleccionados = emptySet()
        },
        alDescartar = { confirmarBorradoSeleccion = false }
    )

    ModalAuditoriaSalud(
        abierto = mostrarModalAuditoria,
        alCerrar = { mostrarModalAuditoria = false },
        clavesCount = claves.size,
        totalSobrantesDuplicadas = totalSobrantesDuplicadas,
        repetidasCount = duplicadas.sumOf { it.size },
        muyComunesCount = muyComunes.size,
        debilesCount = debiles.size,
        antiguasCount = antiguas.size,
        ignoradasCount = ignoradas.size,
        umbralDias = ajustes.umbralAntiguedadDias,
        alIrDuplicados = { vm.ir(Pantalla.Duplicados) }
    )

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
