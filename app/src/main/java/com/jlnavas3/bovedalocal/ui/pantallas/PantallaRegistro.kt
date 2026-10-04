package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo
import com.jlnavas3.bovedalocal.ui.pantallas.registro.BarraSuperiorRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.CATEGORIAS_OPCIONES_REGISTRO
import com.jlnavas3.bovedalocal.ui.pantallas.registro.ContenidoListaRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.CriterioOrdenRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.DialogoOrdenacionRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.ModalCategoriasRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.filtrarYOrdenarEventos
import com.jlnavas3.bovedalocal.ui.pantallas.registro.parsearEventosRegistro
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles

@Composable
fun PantallaRegistro(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var registro by remember { mutableStateOf<List<String>>(emptyList()) }
    var refresco by remember { mutableIntStateOf(0) }
    var busquedaVisible by remember { mutableStateOf(false) }
    var filtroTexto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var criterioOrden by remember { mutableStateOf(CriterioOrdenRegistro.RECIENTES) }
    var mostrarModalCategorias by remember { mutableStateOf(false) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }

    val categoriasOpciones = remember { CATEGORIAS_OPCIONES_REGISTRO }

    LifecycleResumeEffect(Unit) {
        refresco++
        onPauseOrDispose { }
    }
    LaunchedEffect(refresco) {
        registro = Diagnostico.ultimas(Diagnostico.MAX_LINEAS_MEMORIA)
    }

    val eventosParseados = remember(registro) {
        parsearEventosRegistro(registro)
    }

    val eventosOrdenados = remember(eventosParseados, filtroTexto, categoriaSeleccionada, criterioOrden) {
        filtrarYOrdenarEventos(eventosParseados, filtroTexto, categoriaSeleccionada, criterioOrden)
    }

    fun textoRegistroFiltrado(): String = eventosOrdenados.joinToString("\n") { it.textoCompleto }

    val tieneFiltrosActivos = categoriaSeleccionada != "Todos" ||
        criterioOrden != CriterioOrdenRegistro.RECIENTES ||
        filtroTexto.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorRegistro(
            busquedaVisible = busquedaVisible,
            filtroTexto = filtroTexto,
            categoriaSeleccionada = categoriaSeleccionada,
            tieneFiltrosActivos = tieneFiltrosActivos,
            idEtiqueta = "06-SIS-LOG",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            alAlternarBusqueda = {
                busquedaVisible = !busquedaVisible
                if (!busquedaVisible && filtroTexto.isNotBlank()) {
                    filtroTexto = ""
                }
            },
            alMostrarCategorias = { mostrarModalCategorias = true },
            alMostrarOrdenacion = { mostrarDialogoOrdenacion = true },
            alCopiarRegistro = {
                haptica.tic()
                Portapapeles.copiar(contexto, "Registro Bóveda local", textoRegistroFiltrado())
                vm.avisar("Registro copiado")
            },
            alCompartirRegistro = {
                haptica.tic()
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_SUBJECT, "Registro Bóveda local")
                    .putExtra(Intent.EXTRA_TEXT, textoRegistroFiltrado())
                if (!AjustesSistema.abrir(contexto, Intent.createChooser(intent, "Compartir registro"))) {
                    vm.avisar("No hay ninguna app con la que compartirlo")
                }
            },
            alBorrarRegistro = {
                haptica.toque()
                Diagnostico.borrar()
                registro = emptyList()
                vm.avisar("Registro borrado")
            },
            alRestablecerFiltros = {
                haptica.tic()
                categoriaSeleccionada = "Todos"
                criterioOrden = CriterioOrdenRegistro.RECIENTES
                filtroTexto = ""
                busquedaVisible = false
            }
        )

        AnimatedVisibility(
            visible = busquedaVisible || filtroTexto.isNotBlank(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                BarraBusquedaAnimada(
                    valor = filtroTexto,
                    alCambiar = { filtroTexto = it },
                    alCerrar = {
                        busquedaVisible = false
                        filtroTexto = ""
                    }
                )
            }
        }

        if (categoriaSeleccionada != "Todos") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChipFiltroActivo(
                    texto = "Categoría: $categoriaSeleccionada",
                    alLimpiar = {
                        haptica.tic()
                        categoriaSeleccionada = "Todos"
                    }
                )
            }
        }

        ContenidoListaRegistro(
            eventosOrdenados = eventosOrdenados,
            totalEventos = registro.size,
            criterioOrden = criterioOrden
        )
    }

    if (mostrarModalCategorias) {
        ModalCategoriasRegistro(
            categoriasOpciones = categoriasOpciones,
            categoriaSeleccionada = categoriaSeleccionada,
            alSeleccionarCategoria = { categoriaSeleccionada = it },
            alCerrar = { mostrarModalCategorias = false }
        )
    }

    if (mostrarDialogoOrdenacion) {
        DialogoOrdenacionRegistro(
            criterioActual = criterioOrden,
            alSeleccionarCriterio = { criterioOrden = it },
            alCerrar = { mostrarDialogoOrdenacion = false }
        )
    }
}
