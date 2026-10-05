package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Contenedor del cuerpo de PantallaLista: recordatorio de exportacion, barra de filtros y lista/vacio.
 */
@Composable
fun ColumnScope.ContenidoPrincipalLista(
    visibles: List<Entrada>,
    entradas: List<Entrada>,
    ajustes: AjustesApp,
    criterioOrdenacion: CriterioOrdenacion,
    busqueda: String,
    filtro: TipoEntrada?,
    soloFavoritos: Boolean,
    filtroEtiqueta: String?,
    etiquetasDisponibles: List<String>,
    modoSeleccion: Boolean,
    seleccionados: Set<String>,
    gruposExpandidos: Set<String>,
    densidadAltura: Dp,
    densidadMonograma: Int,
    espaciadoFilas: Dp,
    vm: VaultViewModel,
    haptica: Haptica,
    alImportarDirectoCxf: () -> Unit,
    alEntrarEnSeleccion: (String) -> Unit,
    alAlternarSeleccion: (String) -> Unit,
    alEntrarEnSeleccionLote: (Set<String>) -> Unit,
    alAlternarSeleccionLote: (Set<String>) -> Unit,
    alAlternarGrupo: (String) -> Unit,
    identidades: List<Identidad> = emptyList(),
    categorias: List<Categoria> = emptyList()
) {
    val recordatorio = remember(ajustes, entradas) { vm.recordatorioExportacionInfo() }
    if (recordatorio != null) {
        Spacer(Modifier.height((EspaciadoComponentes * 0.8f).coerceAtLeast(6.dp)))
        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            BannerRecordatorioExportacion(
                info = recordatorio,
                alIr = { vm.ir(Pantalla.CopiaSeguridad("05-COP-MAN")) }
            )
        }
    }

    FilaFiltrosYEtiquetasLista(
        soloFavoritos = soloFavoritos,
        filtroTipo = filtro,
        filtroEtiqueta = filtroEtiqueta,
        etiquetasDisponibles = etiquetasDisponibles,
        alAlternarFavoritos = { vm.alternarSoloFavoritos() },
        alLimpiarTipo = { vm.filtrarPorTipo(null) },
        alLimpiarEtiqueta = { vm.filtrarPorEtiqueta(null) },
        alSeleccionarEtiqueta = { vm.filtrarPorEtiqueta(it) }
    )

    Spacer(Modifier.height((EspaciadoComponentes * 0.8f).coerceAtLeast(6.dp)))

    if (visibles.isEmpty()) {
        EstadoVacioLista(
            entradasVacias = entradas.isEmpty(),
            alImportarCopia = {
                haptica.tic()
                vm.ir(Pantalla.CopiaSeguridad("05-COP-MAN-IMP"))
            },
            alImportarCsvGoogle = {
                haptica.tic()
                vm.ir(Pantalla.CsvGoogle("05-COP-CSV-IMP"))
            },
            alImportarGoogleAuthenticator = {
                haptica.tic()
                vm.ir(Pantalla.Escaner())
            },
            alImportarDirectoCxf = alImportarDirectoCxf
        )
    } else {
        CuerpoListaEntradas(
            visibles = visibles,
            ajustes = ajustes,
            criterioOrdenacion = criterioOrdenacion,
            busqueda = busqueda,
            modoSeleccion = modoSeleccion,
            seleccionados = seleccionados,
            gruposExpandidos = gruposExpandidos,
            densidadAltura = densidadAltura,
            densidadMonograma = densidadMonograma,
            espaciadoFilas = espaciadoFilas,
            identidades = identidades,
            categorias = categorias,
            alAbrirEntrada = { id ->
                val listaIdsVisibles = visibles.map { it.id }
                vm.ir(Pantalla.Detalle(id, idsContexto = listaIdsVisibles))
            },
            alCopiarUsuario = { id, usuario ->
                haptica.toque()
                vm.copiar("Usuario", usuario, sensible = false)
                vm.registrarUsoEntrada(id)
            },
            alCopiarContrasena = { id, contrasena ->
                haptica.exito()
                vm.copiar("Contraseña", contrasena, sensible = true)
                vm.registrarUsoEntrada(id)
            },
            alCopiarCodigoTotp = { id, codigo ->
                haptica.exito()
                vm.copiar("Código", codigo, sensible = true)
                vm.registrarUsoEntrada(id)
            },
            alAlternarFavorito = { id ->
                haptica.tic()
                vm.alternarFavorito(id)
            },
            alEntrarEnSeleccion = alEntrarEnSeleccion,
            alAlternarSeleccion = alAlternarSeleccion,
            alEntrarEnSeleccionLote = alEntrarEnSeleccionLote,
            alAlternarSeleccionLote = alAlternarSeleccionLote,
            alAlternarGrupo = alAlternarGrupo
        )
    }
}
