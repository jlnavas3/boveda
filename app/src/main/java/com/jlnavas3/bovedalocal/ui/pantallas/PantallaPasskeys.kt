package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
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
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.BarraSuperiorPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.EstadoVacioPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.passkeys.FilaPasskey
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

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }
    var criterioOrdenacion by remember { mutableStateOf(CriterioOrdenacion.NOMBRE_AZ) }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }

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
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
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
            alRestablecerFiltros = {
                menuOpcionesDesplegado = false
                haptica.tic()
                textoBusqueda = ""
                soloFavoritos = false
                criterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (passkeysFiltradas.isEmpty()) "Sin llaves registradas" else "${passkeysFiltradas.size} llave${if (passkeysFiltradas.size == 1) "" else "s"} de acceso FIDO2 almacenada${if (passkeysFiltradas.size == 1) "" else "s"}"
            )

            Spacer(Modifier.height(10.dp))

            if (passkeysFiltradas.isEmpty()) {
                EstadoVacioPasskeys(
                    sinPasskeysEnTotal = todasLasPasskeys.isEmpty()
                )
            } else {
                GrupoAjustes {
                    passkeysFiltradas.forEachIndexed { index, entrada ->
                        if (index > 0) SeparadorFilaSimple()
                        FilaPasskey(
                            entrada = entrada,
                            formato = formato,
                            haptica = haptica,
                            alPulsar = { vm.ir(Pantalla.Detalle(entrada.id)) },
                            alAlternarFavorito = {
                                haptica.tic()
                                vm.alternarFavorito(entrada.id)
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
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
}
