package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraInferiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.BarraChipsFiltroDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.FiltroDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.IlustracionSinDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.TarjetaGrupoDuplicado
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.util.Haptica

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
    val totalDuplicadasSobrantes = remember(grupos) { grupos.sumOf { it.entradasSecundarias.size } }

    val cantAppsAndroid = remember(grupos) { grupos.count { it.esAppAndroid } }
    val cantWeb = remember(grupos) { grupos.count { !it.esAppAndroid } }
    val cantMismaCuenta = remember(grupos) { grupos.count { it.tipo == TipoDuplicado.MISMA_CUENTA_DISTINTA_CLAVE } }
    val cantVariantes = remember(grupos) { grupos.count { it.tipo == TipoDuplicado.VARIANTE_USUARIO } }

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
        com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas(ajustes.densidadLista)
    }
    var gruposExpandidos by rememberSaveable { mutableStateOf(emptySet<String>()) }

    val gruposFiltrados = remember(grupos, filtroActivo, textoBusqueda, ajustes.agruparPorSitio) {
        val filtrados = grupos.filter { grupo ->
            val coincideFiltro = when (filtroActivo) {
                FiltroDuplicados.TODOS -> true
                FiltroDuplicados.IDENTICOS -> grupo.tipo == TipoDuplicado.IDENTICO
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
        BarraSuperiorPantalla(
            titulo = "Contraseñas duplicadas",
            idEtiqueta = "03-LST-DUP",
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
                if (totalSobrantesIdenticas > 0) {
                    BotonIconoCabecera(
                        onClick = {
                            haptica.toque()
                            confirmarLimpiezaMasiva = true
                        },
                        icono = Icons.Filled.AutoFixHigh,
                        descripcion = "Limpieza rápida masiva",
                        tint = Menta
                    )
                }
                var menuAbiertoDup by remember { mutableStateOf(false) }
                Box {
                    BotonIconoCabecera(
                        onClick = { menuAbiertoDup = true },
                        icono = Icons.Filled.MoreVert,
                        descripcion = "Más opciones"
                    )

                    com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda(
                        expanded = menuAbiertoDup,
                        onDismissRequest = { menuAbiertoDup = false },
                        modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                    ) {
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Salud de la bóveda...",
                            icono = Icons.Filled.HealthAndSafety,
                            colorIcono = Ambar,
                            onClick = {
                                menuAbiertoDup = false
                                vm.ir(Pantalla.SaludBoveda())
                            }
                        )
                        com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu()
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Copia preventiva...",
                            icono = Icons.Filled.Backup,
                            colorIcono = Ambar,
                            onClick = {
                                menuAbiertoDup = false
                                vm.ir(Pantalla.CopiaSeguridad("05-COP-SEG"))
                            }
                        )
                    }
                }
            }
        )

        // Buscador animado desplegable
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
                // Selector de filtros con chips compacto
                BarraChipsFiltroDuplicados(
                    filtroActivo = filtroActivo,
                    totalGrupos = grupos.size,
                    totalSobrantesIdenticas = totalSobrantesIdenticas,
                    cantAppsAndroid = cantAppsAndroid,
                    cantWeb = cantWeb,
                    cantMismaCuenta = cantMismaCuenta,
                    cantVariantes = cantVariantes,
                    alSeleccionarFiltro = { filtroActivo = it }
                )

                Spacer(Modifier.height(8.dp))

                // Contenedor de lista a pantalla completa con botón flotante
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(espaciadoFilas),
                        contentPadding = PaddingValues(bottom = if (modoSeleccion) 96.dp else if (totalSobrantesIdenticas > 0) 88.dp else 16.dp)
                    ) {
                        if (!ajustes.agruparPorSitio) {
                            val todasEntradas = gruposFiltrados.flatMap { grupo ->
                                grupo.entradas.map { entrada ->
                                    Triple(entrada, entrada.id == grupo.sugeridaPrincipal.id, grupo)
                                }
                            }
                            items(todasEntradas, key = { it.first.id }) { (entrada, esSugerida, grupo) ->
                                com.jlnavas3.bovedalocal.ui.pantallas.duplicados.FilaEntradaDuplicada(
                                    entrada = entrada,
                                    esSugerida = esSugerida,
                                    alConservar = {
                                        val secundarias = grupo.entradas.filterNot { it.id == entrada.id }
                                        vm.eliminarVarias(secundarias.map { it.id }.toSet())
                                        vm.avisar("Copia seleccionada conservada")
                                    },
                                    alVerDetalle = { vm.ir(Pantalla.Detalle(entrada.id, idsContexto = grupo.entradas.map { it.id })) },
                                    mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                    enGrupo = false,
                                    seleccionActiva = modoSeleccion,
                                    seleccionado = seleccionados.contains(entrada.id),
                                    alAlternarSeleccion = {
                                        seleccionados = if (seleccionados.contains(entrada.id)) {
                                            seleccionados - entrada.id
                                        } else {
                                            seleccionados + entrada.id
                                        }
                                    },
                                    alPulsarLargo = {
                                        haptica.toque()
                                        seleccionados = seleccionados + entrada.id
                                    }
                                )
                            }
                        } else {
                            items(gruposFiltrados, key = { it.idGrupo }) { grupo ->
                                TarjetaGrupoDuplicado(
                                    grupo = grupo,
                                    expandido = gruposExpandidos.contains(grupo.idGrupo),
                                    alAlternar = {
                                        gruposExpandidos = if (gruposExpandidos.contains(grupo.idGrupo)) {
                                            gruposExpandidos - grupo.idGrupo
                                        } else {
                                            gruposExpandidos + grupo.idGrupo
                                        }
                                    },
                                    alConservar = { elegida ->
                                        val secundarias = grupo.entradas.filterNot { it.id == elegida.id }
                                        vm.eliminarVarias(secundarias.map { it.id }.toSet())
                                        vm.avisar("Copia seleccionada conservada")
                                    },
                                    alVerDetalle = { id ->
                                        vm.ir(Pantalla.Detalle(id, idsContexto = grupo.entradas.map { it.id }))
                                    },
                                    mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                    seleccionActiva = modoSeleccion,
                                    seleccionados = seleccionados,
                                    alAlternarSeleccion = { id ->
                                        seleccionados = if (seleccionados.contains(id)) {
                                            seleccionados - id
                                        } else {
                                            seleccionados + id
                                        }
                                    },
                                    alPulsarLargo = { id ->
                                        haptica.toque()
                                        seleccionados = seleccionados + id
                                    }
                                )
                            }
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
                    } else {
                        // Botón Flotante cuadrado para Limpieza Masiva en 1 toque
                        androidx.compose.animation.AnimatedVisibility(
                            visible = totalSobrantesIdenticas > 0,
                            enter = fadeIn() + slideInVertically { it / 2 },
                            exit = fadeOut() + slideOutVertically { it / 2 },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp, bottom = 16.dp)
                        ) {
                            val formaFab = RoundedCornerShape(CurvaturaEsquinas)
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(formaFab)
                                    .background(Menta)
                                    .then(
                                        if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                                            Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                                        } else Modifier
                                    )
                                    .clickable {
                                        haptica.toque()
                                        confirmarLimpiezaMasiva = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AutoFixHigh,
                                    contentDescription = "Limpiar $totalSobrantesIdenticas copias idénticas",
                                    tint = colorContraste(Menta),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (confirmarLimpiezaMasiva) {
        DialogoConfirmacionBoveda(
            titulo = "Limpiar $totalSobrantesIdenticas copias idénticas",
            mensaje = "Se enviarán $totalSobrantesIdenticas entradas duplicadas a la papelera, conservando automáticamente la copia más completa y reciente de cada servicio. Podrás recuperarlas de la papelera en los próximos 30 días si lo necesitas.",
            textoConfirmar = "Limpiar ahora",
            tipoConfirmacion = TipoBotonTexto.PRIMARIO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {
                confirmarLimpiezaMasiva = false
                vm.eliminarDuplicadasExactasMasivo(gruposIdenticos)
            },
            alDescartar = { confirmarLimpiezaMasiva = false }
        )
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
}
