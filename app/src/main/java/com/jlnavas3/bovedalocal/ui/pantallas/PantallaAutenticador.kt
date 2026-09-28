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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.BarraSuperiorAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.DialogoComoFuncionaTotp
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.ListaCuentasTotp
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.SeccionBusquedaYFiltrosAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

@Composable
fun PantallaAutenticador(vm: VaultViewModel, estado: EstadoBoveda) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val conTotp = vm.entradasConTotp(entradas)

    var ahora by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
    LaunchedEffect(Unit) {
        while (true) {
            ahora = System.currentTimeMillis() / 1000
            delay(500)
        }
    }

    val scrollState = rememberScrollState()

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }
    var criterioOrdenacion by remember { mutableStateOf(CriterioOrdenacion.NOMBRE_AZ) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }
    var dialogoComoFunciona by remember { mutableStateOf(false) }

    val totpFiltrados = remember(conTotp, textoBusqueda, soloFavoritos, criterioOrdenacion) {
        val q = textoBusqueda.trim().lowercase()
        conTotp
            .filter { entrada ->
                val coincideTexto = if (q.isBlank()) true else {
                    entrada.titulo.lowercase().contains(q) ||
                    entrada.totpEmisor.lowercase().contains(q) ||
                    entrada.usuario.lowercase().contains(q)
                }
                val coincideFavorito = if (soloFavoritos) entrada.favorito else true
                coincideTexto && coincideFavorito
            }
            .sortedWith { a, b ->
                val nombreA = a.totpEmisor.ifBlank { a.titulo }
                val nombreB = b.totpEmisor.ifBlank { b.titulo }
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
        // Cabecera modular
        BarraSuperiorAutenticador(
            busquedaVisible = busquedaVisible,
            textoBusqueda = textoBusqueda,
            soloFavoritos = soloFavoritos,
            criterioOrdenacion = criterioOrdenacion,
            conSeparador = scrollState.value > 0,
            haptica = haptica,
            idEtiqueta = "04-HER-2FA",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            alAlternarBusqueda = {
                busquedaVisible = !busquedaVisible
                if (!busquedaVisible) textoBusqueda = ""
            },
            alEscanearQr = { vm.ir(Pantalla.Escaner()) },
            alAgregarManual = { vm.ir(Pantalla.Escaner(soloManual = true)) },
            alSolicitarOrdenacion = { mostrarDialogoOrdenacion = true },
            alAlternarFavoritos = { soloFavoritos = !soloFavoritos },
            alExportarSelectivo = { vm.ir(Pantalla.ExportarSelectivo("2fa")) },
            alImportarGoogleAuthenticator = { vm.ir(Pantalla.Escaner()) },
            alMostrarComoFunciona = { dialogoComoFunciona = true },
            alRestablecerFiltros = {
                soloFavoritos = false
                criterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ
                textoBusqueda = ""
                busquedaVisible = false
            }
        )

        // Barra de búsqueda animada y chip de favoritos
        SeccionBusquedaYFiltrosAutenticador(
            busquedaVisible = busquedaVisible,
            textoBusqueda = textoBusqueda,
            soloFavoritos = soloFavoritos,
            alCambiarTextoBusqueda = { textoBusqueda = it },
            alCerrarBusqueda = {
                busquedaVisible = false
                textoBusqueda = ""
            },
            alLimpiarFavoritos = { soloFavoritos = false }
        )

        // Contenido scrolleable
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            DescripcionPantalla(subtitulo = "Códigos de doble factor calculados en el dispositivo")
            Spacer(Modifier.height(12.dp))

            // Lista de cuentas o estado vacío
            ListaCuentasTotp(
                totpFiltrados = totpFiltrados,
                totalTotp = conTotp.size,
                ahora = ahora,
                separarDigitos = ajustes.totpSepararDigitos,
                haptica = haptica,
                alCopiarCodigo = { codigo ->
                    vm.copiar("Código 2FA", codigo, true)
                },
                alAlternarFavorito = { id ->
                    vm.alternarFavorito(id)
                }
            )

            Spacer(Modifier.height(32.dp))
        }
    }

    // Diálogo de ordenación
    if (mostrarDialogoOrdenacion) {
        DialogoOrdenacionLista(
            criterioActual = criterioOrdenacion,
            alSeleccionarCriterio = { crit ->
                haptica.tic()
                criterioOrdenacion = crit
            },
            alCerrar = { mostrarDialogoOrdenacion = false }
        )
    }

    // Modal informativo: ¿Cómo funciona el 2FA?
    if (dialogoComoFunciona) {
        DialogoComoFuncionaTotp(
            alDescartar = { dialogoComoFunciona = false }
        )
    }
}
