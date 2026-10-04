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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
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
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.BarraAccionesSeleccionDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.BarraChipsFiltroDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.BarraSuperiorDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.BotonLimpiezaMasivaFab
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.ContenidoListaDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.DialogosDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.FiltroDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.IlustracionSinDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla de análisis y gestión de credenciales duplicadas, variantes de usuario y cuentas repetidas.
 */
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

    val cantAppsAndroid = remember(grupos) { grupos.count { it.esAppAndroid } }
    val cantWeb = remember(grupos) { grupos.count { !it.esAppAndroid } }
    val cantMismaCuenta = remember(grupos) { grupos.count { it.tipo == TipoDuplicado.MISMA_CUENTA_DISTINTA_CLAVE } }
    val cantVariantes = remember(grupos) { grupos.count { it.tipo == TipoDuplicado.VARIANTE_USUARIO } }
    val cantPasskeys = remember(grupos) {
        grupos.count { grupo ->
            grupo.tipo == TipoDuplicado.PASSKEY || grupo.entradas.count { it.passkey != null } > 1
        }
    }
    val cantTotp = remember(grupos) {
        grupos.count { grupo ->
            grupo.tipo == TipoDuplicado.TOTP || grupo.entradas.count { !it.secretoTotp.isNullOrBlank() } > 1
        }
    }

    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var filtroActivo by remember { mutableStateOf(FiltroDuplicados.TODOS) }
    var textoBusqueda by remember { mutableStateOf("") }
    var busquedaVisible by rememberSaveable { mutableStateOf(false) }
    var confirmarLimpiezaMasiva by remember { mutableStateOf(false) }
    var seleccionados by rememberSaveable { mutableStateOf(emptySet<String>()) }
    val modoSeleccion = seleccionados.isNotEmpty()
    var confirmarBorradoSeleccion by remember { mutableStateOf(false) }

    BackHandler(enabled = modoSeleccion) {
        seleccionados = emptySet()
    }

    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val espaciadoFilas = remember(ajustes.densidadLista) {
        calcularEspaciadoFilas(ajustes.densidadLista)
    }
    var gruposExpandidos by rememberSaveable { mutableStateOf(emptySet<String>()) }

    val gruposFiltrados = remember(grupos, filtroActivo, textoBusqueda, ajustes.agruparPorSitio) {
        val filtrados = grupos.filter { grupo ->
            val coincideFiltro = when (filtroActivo) {
                FiltroDuplicados.TODOS -> true
                FiltroDuplicados.IDENTICOS -> grupo.tipo == TipoDuplicado.IDENTICO
                FiltroDuplicados.PASSKEY -> grupo.tipo == TipoDuplicado.PASSKEY || grupo.entradas.count { it.passkey != null } > 1
                FiltroDuplicados.TOTP -> grupo.tipo == TipoDuplicado.TOTP || grupo.entradas.count { !it.secretoTotp.isNullOrBlank() } > 1
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
        if (ajustes.agruparPorSitio) {
            filtrados.sortedBy { it.claveVisual.lowercase() }
        } else {
            filtrados
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorDuplicados(
            mostrarId = ajustes.mostrarIdsAjustes,
            busquedaVisible = busquedaVisible,
            textoBusqueda = textoBusqueda,
            totalSobrantesIdenticas = totalSobrantesIdenticas,
            haptica = haptica,
            alVolver = { vm.volverAtras() },
            alAlternarBusqueda = {
                busquedaVisible = !busquedaVisible
                if (!busquedaVisible) textoBusqueda = ""
            },
            alPedirLimpiezaMasiva = { confirmarLimpiezaMasiva = true },
            alIrSalud = { vm.ir(Pantalla.SaludBoveda()) },
            alIrCopia = { vm.ir(Pantalla.CopiaSeguridad("05-COP-SEG")) }
        )

        AnimatedVisibility(
            visible = busquedaVisible || textoBusqueda.isNotBlank(),
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
                BarraChipsFiltroDuplicados(
                    filtroActivo = filtroActivo,
                    totalGrupos = grupos.size,
                    totalSobrantesIdenticas = totalSobrantesIdenticas,
                    cantPasskeys = cantPasskeys,
                    cantTotp = cantTotp,
                    cantAppsAndroid = cantAppsAndroid,
                    cantWeb = cantWeb,
                    cantMismaCuenta = cantMismaCuenta,
                    cantVariantes = cantVariantes,
                    alSeleccionarFiltro = { filtroActivo = it }
                )

                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    ContenidoListaDuplicados(
                        gruposFiltrados = gruposFiltrados,
                        agruparPorSitio = ajustes.agruparPorSitio,
                        espaciadoFilas = espaciadoFilas,
                        modoSeleccion = modoSeleccion,
                        totalSobrantesIdenticas = totalSobrantesIdenticas,
                        ajustes = ajustes,
                        gruposExpandidos = gruposExpandidos,
                        seleccionados = seleccionados,
                        haptica = haptica,
                        alAlternarGrupo = { idGrupo ->
                            gruposExpandidos = if (gruposExpandidos.contains(idGrupo)) {
                                gruposExpandidos - idGrupo
                            } else {
                                gruposExpandidos + idGrupo
                            }
                        },
                        alConservarCopia = { elegida, grupo ->
                            val secundarias = grupo.entradas.filterNot { it.id == elegida.id }
                            vm.eliminarVarias(secundarias.map { it.id }.toSet())
                            vm.avisar("Copia seleccionada conservada")
                        },
                        alVerDetalle = { id, idsContexto ->
                            vm.ir(Pantalla.Detalle(id, idsContexto = idsContexto))
                        },
                        alAlternarSeleccion = { id ->
                            seleccionados = if (seleccionados.contains(id)) seleccionados - id else seleccionados + id
                        },
                        alPulsarLargo = { id ->
                            seleccionados = seleccionados + id
                        }
                    )

                    if (modoSeleccion) {
                        BarraAccionesSeleccionDuplicados(
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
                    } else {
                        BotonLimpiezaMasivaFab(
                            visible = totalSobrantesIdenticas > 0,
                            totalSobrantesIdenticas = totalSobrantesIdenticas,
                            haptica = haptica,
                            alPulsar = { confirmarLimpiezaMasiva = true },
                            modifier = Modifier.align(Alignment.BottomEnd)
                        )
                    }
                }
            }
        }
    }

    DialogosDuplicados(
        confirmarLimpiezaMasiva = confirmarLimpiezaMasiva,
        totalSobrantesIdenticas = totalSobrantesIdenticas,
        alConfirmarLimpiezaMasiva = {
            confirmarLimpiezaMasiva = false
            vm.eliminarDuplicadasExactasMasivo(gruposIdenticos)
        },
        alDescartarLimpiezaMasiva = { confirmarLimpiezaMasiva = false },
        confirmarBorradoSeleccion = confirmarBorradoSeleccion,
        cantidadSeleccionados = seleccionados.size,
        alConfirmarBorradoSeleccion = {
            confirmarBorradoSeleccion = false
            val cant = seleccionados.size
            vm.eliminarVarias(seleccionados)
            vm.avisar("$cant entrada${if (cant > 1) "s enviadas" else " enviada"} a la papelera")
            seleccionados = emptySet()
        },
        alDescartarBorradoSeleccion = { confirmarBorradoSeleccion = false }
    )
}
