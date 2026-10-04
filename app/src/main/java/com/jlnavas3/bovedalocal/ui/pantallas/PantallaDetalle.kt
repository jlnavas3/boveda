package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.ColumnaAccionesFlotantesDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.ContenidoEntradaDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.DialogosDetalle
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DialogoCompartirQr
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.BarraSuperiorDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.CabeceraHeroDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.EstadoEntradaNoEncontrada
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.PieMetadatosDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaCamposDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaCredencialesDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaEtiquetasDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaHistorialDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaNotasDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaPasskeyDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaSitiosYAppsDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaTotpDetalle
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

@Composable
fun PantallaDetalle(
    vm: VaultViewModel,
    id: String,
    idsContexto: List<String> = emptyList(),
    modoComparacion: Boolean = false
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estado by vm.estado.collectAsStateWithLifecycle()
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val todasLasEntradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    val listaIds = remember(idsContexto, todasLasEntradas) {
        val base = if (idsContexto.isNotEmpty()) idsContexto else todasLasEntradas.map { it.id }
        val idsExistentes = todasLasEntradas.map { it.id }.toSet()
        val filtrados = base.filter { it in idsExistentes }
        if (filtrados.isNotEmpty()) filtrados else if (id in idsExistentes) listOf(id) else emptyList()
    }

    if (listaIds.isEmpty()) {
        EstadoEntradaNoEncontrada(alVolver = { vm.volverAtras() })
        return
    }

    val total = listaIds.size
    val startIndex = remember(id, listaIds) {
        val idx = listaIds.indexOf(id)
        if (idx >= 0) idx else 0
    }

    val esBucleInfinito = modoComparacion && total >= 2
    val multiplicadorVirtual = if (esBucleInfinito) 40_000 else 1
    val totalPaginas = total * multiplicadorVirtual
    val offsetMedio = if (esBucleInfinito) (totalPaginas / 2) - ((totalPaginas / 2) % total) else 0
    val paginaInicial = offsetMedio + startIndex

    val pagerState = rememberPagerState(
        initialPage = paginaInicial,
        pageCount = { totalPaginas }
    )

    val indiceActual = remember(pagerState.currentPage, total) {
        if (total == 0) 0 else pagerState.currentPage % total
    }
    val idActual = listaIds.getOrElse(indiceActual) { id }
    val entradaActual = todasLasEntradas.find { it.id == idActual } ?: vm.entrada(idActual)

    var confirmarBorrado by remember { mutableStateOf(false) }
    var mostrarDialogoQr by remember { mutableStateOf(false) }

    LaunchedEffect(entradaActual?.id) {
        if (entradaActual != null) {
            val tipoDesc = entradaActual.tipo.etiqueta.lowercase()
            Diagnostico.apuntar("bóveda", "Detalle de entrada consultado ($tipoDesc)")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BarraSuperiorDetalle(
                conSeparador = true,
                alVolver = { vm.volverAtras() },
                alIrCopiaRapida = { vm.ir(Pantalla.AjustesCopiaAutomatica("03.2.1")) },
                alIrFormatosCampos = { vm.ir(Pantalla.FormatosCampos("03-LST-CAM")) },
                alIrSeguridadDatos = { vm.ir(Pantalla.Seguridad("01-SEG-DAT")) },
                alIrColoresIds = { vm.ir(Pantalla.ColoresIdentificadores("06-AVN-IDS")) }
            )

            // Indicador de modo comparación
            if (modoComparacion && total >= 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorTarjetaAjustes)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                            contentDescription = null,
                            tint = ColorAcento,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Comparando ${indiceActual + 1} de $total",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = ColorAcento
                        )
                    }
                    Text(
                        text = "Desliza para alternar",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextoSecundario
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                val idx = if (total > 0) page % total else 0
                val idEntrada = listaIds.getOrElse(idx) { "" }
                val entradaPagina = todasLasEntradas.find { it.id == idEntrada } ?: vm.entrada(idEntrada)

                if (entradaPagina != null) {
                    ContenidoEntradaDetalle(
                        entrada = entradaPagina,
                        ajustes = ajustes,
                        vm = vm,
                        haptica = haptica,
                        alMostrarQr = {
                            haptica.tic()
                            mostrarDialogoQr = true
                        }
                    )
                } else {
                    EstadoEntradaNoEncontrada(alVolver = { vm.volverAtras() })
                }
            }
        }

        ColumnaAccionesFlotantesDetalle(
            esFavorito = entradaActual?.favorito == true,
            alEliminar = {
                haptica.tic()
                confirmarBorrado = true
            },
            alCompartirQr = {
                haptica.tic()
                mostrarDialogoQr = true
            },
            alAlternarFavorito = {
                haptica.tic()
                entradaActual?.let { vm.alternarFavorito(it.id) }
            },
            alEditar = {
                entradaActual?.let {
                    haptica.toque()
                    vm.ir(Pantalla.Editar(it.id))
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
        )
    }

    DialogosDetalle(
        confirmarBorrado = confirmarBorrado,
        entradaActual = entradaActual,
        alConfirmarBorrado = {
            confirmarBorrado = false
            haptica.error()
            val idABorrar = entradaActual?.id ?: return@DialogosDetalle
            vm.eliminar(idABorrar)
            if (listaIds.size <= 1) {
                vm.volverAtras()
            }
        },
        alDescartarBorrado = { confirmarBorrado = false },
        mostrarDialogoQr = mostrarDialogoQr,
        alDescartarQr = { mostrarDialogoQr = false }
    )
}
