package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.componentes.encontrarIndiceParaLetra
import com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorSitio
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CuerpoListaEntradas(
    visibles: List<Entrada>,
    ajustes: AjustesApp,
    criterioOrdenacion: CriterioOrdenacion,
    busqueda: String,
    modoSeleccion: Boolean,
    seleccionados: Set<String>,
    gruposExpandidos: Set<String>,
    densidadAltura: Dp,
    densidadMonograma: Int,
    espaciadoFilas: Dp,
    alAbrirEntrada: (String) -> Unit,
    alCopiarUsuario: (String, String) -> Unit,
    alCopiarContrasena: (String, String) -> Unit,
    alCopiarCodigoTotp: (String, String) -> Unit,
    alAlternarFavorito: (String) -> Unit,
    alEntrarEnSeleccion: (String) -> Unit,
    alAlternarSeleccion: (String) -> Unit,
    alAlternarGrupo: (String) -> Unit,
    alEntrarEnSeleccionLote: ((Set<String>) -> Unit)? = null,
    alAlternarSeleccionLote: ((Set<String>) -> Unit)? = null,
    identidades: List<com.jlnavas3.bovedalocal.data.Identidad> = emptyList(),
    colecciones: List<com.jlnavas3.bovedalocal.data.Coleccion> = emptyList()
) {
    val ambitoCorutina = rememberCoroutineScope()
    val densidad = LocalDensity.current
    val expandidoEnLista: (String) -> Boolean = { clave ->
        busqueda.isNotBlank() || gruposExpandidos.contains(clave)
    }

    val esModoSecciones = ajustes.modoVisualizacionIdentidades == com.jlnavas3.bovedalocal.data.ModoVisualizacionIdentidades.SECCIONES
    val jerarquia = ajustes.jerarquiaOrganizacionEfectiva

    val esModoSeccionesIdentidad = esModoSecciones &&
        identidades.isNotEmpty() &&
        jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.IDENTIDAD_SOBRE_COLECCION

    val esModoSeccionesColeccion = esModoSecciones &&
        colecciones.isNotEmpty() &&
        jerarquia == com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion.COLECCION_SOBRE_IDENTIDAD

    val itemsIdentidades = remember(visibles, identidades, gruposExpandidos, busqueda, esModoSeccionesIdentidad) {
        if (!esModoSeccionesIdentidad) emptyList()
        else com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorIdentidad(visibles, identidades) { clave ->
            busqueda.isNotBlank() || gruposExpandidos.contains("identidad-$clave")
        }
    }

    val itemsColecciones = remember(visibles, colecciones, gruposExpandidos, busqueda, esModoSeccionesColeccion) {
        if (!esModoSeccionesColeccion) emptyList()
        else com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorColeccion(visibles, colecciones) { clave ->
            busqueda.isNotBlank() || gruposExpandidos.contains("coleccion-$clave")
        }
    }

    val itemsAMostrar = remember(visibles, modoSeleccion, criterioOrdenacion, ajustes.agruparPorSitio) {
        construirItemsAgrupadosPorSitio(
            entradas = visibles,
            criterio = criterioOrdenacion,
            agrupar = ajustes.agruparPorSitio,
            expandido = { false }
        )
    }

    val estadoLista = rememberLazyListState()
    val mostrarIndice = ajustes.mostrarIndiceAlfabetico &&
        itemsAMostrar.size >= 5 &&
        criterioOrdenacion == CriterioOrdenacion.NOMBRE_AZ

    var letraArrastrada by remember { mutableStateOf<Char?>(null) }

    fun itemCoincideConLetra(item: ItemAgrupado, letra: Char?, incluirEnie: Boolean): Boolean {
        if (letra == null) return false
        val titulo = when (item) {
            is ItemAgrupado.Suelto -> item.entrada.titulo
            is ItemAgrupado.Grupo -> item.clave.removePrefix("www.")
            is ItemAgrupado.Hijo -> item.entrada.titulo
        }
        return letraInicialIndice(titulo, incluirEnie) == letra
    }

    val primerIndiceCoincidente = remember(itemsAMostrar, letraArrastrada, ajustes.indiceIncluirEnie, ajustes.indiceResaltarEntradas, ajustes.indiceResaltarSoloPrimera) {
        if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) null
        else if (ajustes.indiceResaltarSoloPrimera) {
            itemsAMostrar.indexOfFirst { itemCoincideConLetra(it, letraArrastrada, ajustes.indiceIncluirEnie) }.takeIf { it >= 0 }
        } else null
    }

    var segundosUnix by remember { mutableStateOf(System.currentTimeMillis() / 1000) }
    LaunchedEffect(Unit) {
        while (true) {
            segundosUnix = System.currentTimeMillis() / 1000
            delay(1000)
        }
    }

    if (esModoSeccionesIdentidad) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = estadoLista,
                modifier = Modifier
                    .fillMaxSize()
                    .reboteElastico(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 110.dp
                ),
                verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
            ) {
                items(
                    itemsIdentidades,
                    key = { item ->
                        when (item) {
                            is com.jlnavas3.bovedalocal.util.ItemAgrupadoIdentidad.CabeceraIdentidad -> "cab-iden-${item.identidad.id}"
                            is com.jlnavas3.bovedalocal.util.ItemAgrupadoIdentidad.CabeceraSinIdentidad -> "cab-sin-iden"
                            is com.jlnavas3.bovedalocal.util.ItemAgrupadoIdentidad.EntradaHija -> "ent-iden-${item.entrada.id}"
                        }
                    }
                ) { item ->
                    when (item) {
                        is com.jlnavas3.bovedalocal.util.ItemAgrupadoIdentidad.CabeceraIdentidad -> {
                            val claveGrupo = "identidad-${item.identidad.id}"
                            val expandido = busqueda.isNotBlank() || gruposExpandidos.contains(claveGrupo)
                            val colorBase = com.jlnavas3.bovedalocal.ui.theme.parsearColorO(item.identidad.colorHex ?: "", com.jlnavas3.bovedalocal.ui.theme.ColorAcento)
                            SeccionGrupoIdentidad(
                                nombre = item.identidad.nombre,
                                subtitulo = item.identidad.correoPrincipal,
                                cantidad = item.totalEntradas,
                                expandido = expandido,
                                colorBase = colorBase,
                                alAlternar = { alAlternarGrupo(claveGrupo) }
                            )
                        }
                        is com.jlnavas3.bovedalocal.util.ItemAgrupadoIdentidad.CabeceraSinIdentidad -> {
                            val claveGrupo = "identidad-__SIN_IDENTIDAD__"
                            val expandido = busqueda.isNotBlank() || gruposExpandidos.contains(claveGrupo)
                            SeccionGrupoIdentidad(
                                nombre = "Sin identidad asignada",
                                subtitulo = "Wi-Fi, routers y credenciales varias",
                                cantidad = item.totalEntradas,
                                expandido = expandido,
                                colorBase = com.jlnavas3.bovedalocal.ui.theme.TextoSecundario,
                                icono = Icons.Filled.Security,
                                alAlternar = { alAlternarGrupo(claveGrupo) }
                            )
                        }
                        is com.jlnavas3.bovedalocal.util.ItemAgrupadoIdentidad.EntradaHija -> {
                            FilaEntrada(
                                entrada = item.entrada,
                                seleccionActiva = modoSeleccion,
                                seleccionado = seleccionados.contains(item.entrada.id),
                                alAbrir = { alAbrirEntrada(item.entrada.id) },
                                alCopiarUsuario = { alCopiarUsuario(item.entrada.id, item.entrada.usuario) },
                                alCopiarContrasena = { alCopiarContrasena(item.entrada.id, item.entrada.contrasena) },
                                alFavorito = { alAlternarFavorito(item.entrada.id) },
                                alCopiarCodigo = { alCopiarCodigoTotp(item.entrada.id, it) },
                                alPulsarLargo = { alEntrarEnSeleccion(item.entrada.id) },
                                alAlternarSeleccion = { alAlternarSeleccion(item.entrada.id) },
                                segundosUnix = segundosUnix,
                                alturaFila = densidadAltura,
                                tamanoMonograma = densidadMonograma,
                                resaltado = false,
                                separarDigitosTotp = ajustes.totpSepararDigitos,
                                mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                enGrupo = true,
                                esUltimoEnGrupo = item.esUltimaEnSeccion,
                                ocultarUsuario = ajustes.seguridadVisualActiva && ajustes.ocultarUsuario,
                                ocultarTotp = ajustes.seguridadVisualActiva && ajustes.ocultarTotp,
                                estiloOcultamiento = ajustes.estiloOcultamientoVisual,
                                identidadAsociada = item.identidad,
                                ocultarEmailIdentidad = item.identidad != null
                            )
                        }
                    }
                }
            }
        }
        return
    }

    if (esModoSeccionesColeccion) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = estadoLista,
                modifier = Modifier
                    .fillMaxSize()
                    .reboteElastico(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 110.dp
                ),
                verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
            ) {
                items(
                    itemsColecciones,
                    key = { item ->
                        when (item) {
                            is com.jlnavas3.bovedalocal.util.ItemAgrupadoColeccion.CabeceraColeccion -> "cab-col-${item.coleccion.id}"
                            is com.jlnavas3.bovedalocal.util.ItemAgrupadoColeccion.CabeceraSinColeccion -> "cab-sin-col"
                            is com.jlnavas3.bovedalocal.util.ItemAgrupadoColeccion.EntradaHija -> "ent-col-${item.entrada.id}"
                        }
                    }
                ) { item ->
                    when (item) {
                        is com.jlnavas3.bovedalocal.util.ItemAgrupadoColeccion.CabeceraColeccion -> {
                            val claveGrupo = "coleccion-${item.coleccion.id}"
                            val expandido = busqueda.isNotBlank() || gruposExpandidos.contains(claveGrupo)
                            val colorBase = com.jlnavas3.bovedalocal.ui.theme.parsearColorO(item.coleccion.colorHex ?: "", com.jlnavas3.bovedalocal.ui.theme.ColorAcento)
                            val iconoVector = com.jlnavas3.bovedalocal.ui.pantallas.colecciones.IconosColecciones.obtenerIcono(item.coleccion.icono)
                            SeccionGrupoColeccion(
                                nombre = item.coleccion.nombre,
                                subtitulo = "Colección temática",
                                cantidad = item.totalEntradas,
                                expandido = expandido,
                                colorBase = colorBase,
                                icono = iconoVector,
                                alAlternar = { alAlternarGrupo(claveGrupo) }
                            )
                        }
                        is com.jlnavas3.bovedalocal.util.ItemAgrupadoColeccion.CabeceraSinColeccion -> {
                            val claveGrupo = "coleccion-__SIN_COLECCION__"
                            val expandido = busqueda.isNotBlank() || gruposExpandidos.contains(claveGrupo)
                            SeccionGrupoColeccion(
                                nombre = "Sin colección",
                                subtitulo = "Elementos sin carpeta asignada",
                                cantidad = item.totalEntradas,
                                expandido = expandido,
                                colorBase = com.jlnavas3.bovedalocal.ui.theme.TextoSecundario,
                                icono = androidx.compose.material.icons.Icons.Filled.Security,
                                alAlternar = { alAlternarGrupo(claveGrupo) }
                            )
                        }
                        is com.jlnavas3.bovedalocal.util.ItemAgrupadoColeccion.EntradaHija -> {
                            val iden = com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(item.entrada, identidades)
                            FilaEntrada(
                                entrada = item.entrada,
                                seleccionActiva = modoSeleccion,
                                seleccionado = seleccionados.contains(item.entrada.id),
                                alAbrir = { alAbrirEntrada(item.entrada.id) },
                                alCopiarUsuario = { alCopiarUsuario(item.entrada.id, item.entrada.usuario) },
                                alCopiarContrasena = { alCopiarContrasena(item.entrada.id, item.entrada.contrasena) },
                                alFavorito = { alAlternarFavorito(item.entrada.id) },
                                alCopiarCodigo = { alCopiarCodigoTotp(item.entrada.id, it) },
                                alPulsarLargo = { alEntrarEnSeleccion(item.entrada.id) },
                                alAlternarSeleccion = { alAlternarSeleccion(item.entrada.id) },
                                segundosUnix = segundosUnix,
                                alturaFila = densidadAltura,
                                tamanoMonograma = densidadMonograma,
                                resaltado = false,
                                separarDigitosTotp = ajustes.totpSepararDigitos,
                                mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                enGrupo = true,
                                esUltimoEnGrupo = item.esUltimaEnSeccion,
                                ocultarUsuario = ajustes.seguridadVisualActiva && ajustes.ocultarUsuario,
                                ocultarTotp = ajustes.seguridadVisualActiva && ajustes.ocultarTotp,
                                estiloOcultamiento = ajustes.estiloOcultamientoVisual,
                                identidadAsociada = iden,
                                ocultarEmailIdentidad = iden != null
                            )
                        }
                    }
                }
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = estadoLista,
            modifier = Modifier
                .fillMaxSize()
                .reboteElastico(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = if (mostrarIndice) 36.dp else 20.dp,
                bottom = 110.dp
            ),
            verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
        ) {
            itemsIndexed(
                itemsAMostrar,
                key = { _, item ->
                    when (item) {
                        is ItemAgrupado.Suelto -> item.entrada.id
                        is ItemAgrupado.Grupo -> "grupo-${item.clave}"
                        is ItemAgrupado.Hijo -> "hijo-${item.entrada.id}"
                    }
                },
                contentType = { _, item ->
                    when (item) {
                        is ItemAgrupado.Suelto -> 0
                        is ItemAgrupado.Grupo -> 1
                        is ItemAgrupado.Hijo -> 2
                    }
                }
            ) { indice, item ->
                val coincideLetra = if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) {
                    false
                } else if (ajustes.indiceResaltarSoloPrimera) {
                    indice == primerIndiceCoincidente
                } else {
                    itemCoincideConLetra(item, letraArrastrada, ajustes.indiceIncluirEnie)
                }
                when (item) {
                    is ItemAgrupado.Grupo -> {
                        val idsHijos = remember(item.entradas) { item.entradas.map { it.id }.toSet() }
                        val todosHijosSeleccionados = idsHijos.isNotEmpty() && seleccionados.containsAll(idsHijos)
                        val algunoHijoSeleccionado = idsHijos.any { seleccionados.contains(it) }
                        val parcialmenteSeleccionado = algunoHijoSeleccionado && !todosHijosSeleccionados

                        ComponenteGrupoLista(
                            clave = item.clave,
                            entradas = item.entradas,
                            expandido = expandidoEnLista(item.clave),
                            alturaFila = densidadAltura,
                            tamanoMonograma = densidadMonograma,
                            resaltado = coincideLetra,
                            seleccionActiva = modoSeleccion,
                            seleccionado = todosHijosSeleccionados,
                            parcialmenteSeleccionado = parcialmenteSeleccionado,
                            alAlternar = {
                                if (modoSeleccion) {
                                    if (alAlternarSeleccionLote != null) {
                                        alAlternarSeleccionLote(idsHijos)
                                    } else {
                                        if (todosHijosSeleccionados) {
                                            item.entradas.forEach { if (seleccionados.contains(it.id)) alAlternarSeleccion(it.id) }
                                        } else {
                                            item.entradas.forEach { if (!seleccionados.contains(it.id)) alAlternarSeleccion(it.id) }
                                        }
                                    }
                                } else {
                                    alAlternarGrupo(item.clave)
                                }
                            },
                            alPulsarLargo = {
                                if (!modoSeleccion) {
                                    if (alEntrarEnSeleccionLote != null) {
                                        alEntrarEnSeleccionLote(idsHijos)
                                    } else {
                                        item.entradas.firstOrNull()?.let { alEntrarEnSeleccion(it.id) }
                                    }
                                } else {
                                    if (alAlternarSeleccionLote != null) {
                                        alAlternarSeleccionLote(idsHijos)
                                    } else {
                                        if (todosHijosSeleccionados) {
                                            item.entradas.forEach { if (seleccionados.contains(it.id)) alAlternarSeleccion(it.id) }
                                        } else {
                                            item.entradas.forEach { if (!seleccionados.contains(it.id)) alAlternarSeleccion(it.id) }
                                        }
                                    }
                                }
                            },
                            alAlternarExpansion = {
                                alAlternarGrupo(item.clave)
                            },
                            contenidoEntrada = { entradaHija, indiceHijo, totalHijos ->
                            val coincideLetraHijo = if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) {
                                false
                            } else {
                                letraInicialIndice(entradaHija.titulo, ajustes.indiceIncluirEnie) == letraArrastrada
                            }
                            FilaEntrada(
                                entrada = entradaHija,
                                seleccionActiva = modoSeleccion,
                                seleccionado = seleccionados.contains(entradaHija.id),
                                alAbrir = { alAbrirEntrada(entradaHija.id) },
                                alCopiarUsuario = { alCopiarUsuario(entradaHija.id, entradaHija.usuario) },
                                alCopiarContrasena = { alCopiarContrasena(entradaHija.id, entradaHija.contrasena) },
                                alFavorito = { alAlternarFavorito(entradaHija.id) },
                                alCopiarCodigo = { alCopiarCodigoTotp(entradaHija.id, it) },
                                alPulsarLargo = { alEntrarEnSeleccion(entradaHija.id) },
                                alAlternarSeleccion = { alAlternarSeleccion(entradaHija.id) },
                                segundosUnix = segundosUnix,
                                alturaFila = densidadAltura,
                                tamanoMonograma = densidadMonograma,
                                resaltado = coincideLetraHijo,
                                separarDigitosTotp = ajustes.totpSepararDigitos,
                                mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                                enGrupo = true,
                                esUltimoEnGrupo = indiceHijo == totalHijos - 1,
                                ocultarUsuario = ajustes.seguridadVisualActiva && ajustes.ocultarUsuario,
                                ocultarTotp = ajustes.seguridadVisualActiva && ajustes.ocultarTotp,
                                estiloOcultamiento = ajustes.estiloOcultamientoVisual,
                                identidadAsociada = com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(entradaHija, identidades)
                            )
                        }
                    )
                }
                is ItemAgrupado.Suelto -> FilaEntrada(
                        entrada = item.entrada,
                        seleccionActiva = modoSeleccion,
                        seleccionado = seleccionados.contains(item.entrada.id),
                        alAbrir = { alAbrirEntrada(item.entrada.id) },
                        alCopiarUsuario = { alCopiarUsuario(item.entrada.id, item.entrada.usuario) },
                        alCopiarContrasena = { alCopiarContrasena(item.entrada.id, item.entrada.contrasena) },
                        alFavorito = { alAlternarFavorito(item.entrada.id) },
                        alCopiarCodigo = { alCopiarCodigoTotp(item.entrada.id, it) },
                        alPulsarLargo = { alEntrarEnSeleccion(item.entrada.id) },
                        alAlternarSeleccion = { alAlternarSeleccion(item.entrada.id) },
                        segundosUnix = segundosUnix,
                        alturaFila = densidadAltura,
                        tamanoMonograma = densidadMonograma,
                        resaltado = coincideLetra,
                        separarDigitosTotp = ajustes.totpSepararDigitos,
                        mostrarIndicadores = ajustes.mostrarIndicadoresContenido,
                        enGrupo = false,
                        ocultarUsuario = ajustes.seguridadVisualActiva && ajustes.ocultarUsuario,
                        ocultarTotp = ajustes.seguridadVisualActiva && ajustes.ocultarTotp,
                        estiloOcultamiento = ajustes.estiloOcultamientoVisual,
                        identidadAsociada = com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(item.entrada, identidades)
                    )
                    is ItemAgrupado.Hijo -> Unit
                }
            }
        }

        if (mostrarIndice) {
            IndiceAlfabeticoLista(
                ajustes = ajustes,
                itemsAMostrar = itemsAMostrar,
                densidadAltura = densidadAltura,
                espaciadoFilas = espaciadoFilas,
                estadoLista = estadoLista,
                alCambiarLetraActiva = { letraArrastrada = it },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 4.dp, bottom = 100.dp)
            )
        }
    }
}
