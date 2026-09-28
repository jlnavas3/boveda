package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.BarraBusquedaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.BannerLimpiezaMasivaDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.BarraChipsFiltroDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.FiltroDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.IlustracionSinDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.duplicados.TarjetaGrupoDuplicado

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

    var filtroActivo by remember { mutableStateOf(FiltroDuplicados.TODOS) }
    var textoBusqueda by remember { mutableStateOf("") }
    var confirmarLimpiezaMasiva by remember { mutableStateOf(false) }

    val gruposFiltrados = remember(grupos, filtroActivo, textoBusqueda) {
        grupos.filter { grupo ->
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
    }

    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

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
            colorFondo = ColorAjustesFondo
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (grupos.isEmpty()) "Tu bóveda no tiene duplicados" else "${grupos.size} grupos encontrados · $totalDuplicadasSobrantes entradas redundantes"
            )

            Spacer(Modifier.height(10.dp))

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
                // Banner de Limpieza Rápida Masiva para Duplicados Idénticos
                if (totalSobrantesIdenticas > 0 && filtroActivo != FiltroDuplicados.MISMA_CUENTA) {
                    BannerLimpiezaMasivaDuplicados(
                        totalSobrantesIdenticas = totalSobrantesIdenticas,
                        alConfirmarLimpieza = { confirmarLimpiezaMasiva = true }
                    )
                    Spacer(Modifier.height(14.dp))
                }

                // Buscador nativo
                BarraBusquedaAjustes(
                    texto = textoBusqueda,
                    alCambiarTexto = { textoBusqueda = it },
                    placeholder = "Buscar en duplicados..."
                )

                Spacer(Modifier.height(12.dp))

                // Selector de filtros con chips
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

                Spacer(Modifier.height(14.dp))

                // Lista de grupos duplicados
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(gruposFiltrados, key = { it.idGrupo }) { grupo ->
                        TarjetaGrupoDuplicado(
                            grupo = grupo,
                            alConservar = { elegida ->
                                val secundarias = grupo.entradas.filterNot { it.id == elegida.id }
                                vm.eliminarVarias(secundarias.map { it.id }.toSet())
                                vm.avisar("Copia seleccionada conservada")
                            },
                            alUnificar = {
                                vm.unificarEntradas(grupo.sugeridaPrincipal, grupo.entradasSecundarias)
                            },
                            alVerDetalle = { id ->
                                vm.ir(Pantalla.Detalle(id))
                            }
                        )
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
}
