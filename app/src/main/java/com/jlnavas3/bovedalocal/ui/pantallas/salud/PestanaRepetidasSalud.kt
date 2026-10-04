package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.claveAgrupacionPorTitulo

/**
 * Microcomponente que gestiona la pestaña de contraseñas repetidas en la auditoría de salud.
 */
@Composable
fun PestanaRepetidasSalud(
    duplicadas: List<List<Entrada>>,
    consulta: String,
    agruparPorSitio: Boolean,
    espaciadoFilas: Dp,
    rellenoInferior: PaddingValues,
    ajustes: AjustesApp?,
    mostrarIndicadores: Boolean,
    seleccionActiva: Boolean,
    seleccionados: Set<String>,
    alCambiarClave: (Entrada) -> Unit,
    alVerDetalle: (String) -> Unit,
    alIgnorar: (Entrada) -> Unit,
    alAlternarSeleccion: ((String) -> Unit)?,
    alPulsarLargo: ((String) -> Unit)?
) {
    var gruposExpandidos by rememberSaveable { mutableStateOf(emptySet<String>()) }

    val gruposFiltrados = remember(duplicadas, consulta) {
        if (consulta.isEmpty()) {
            duplicadas.map { grupo -> grupo to grupo }
        } else {
            duplicadas.mapNotNull { grupo ->
                val coincidentes = grupo.filter { coincideBusquedaSalud(it, consulta) }
                if (coincidentes.isNotEmpty()) grupo to coincidentes else null
            }
        }
    }

    if (gruposFiltrados.isEmpty()) {
        if (consulta.isNotEmpty()) {
            MensajeExitoPestana(
                icono = Icons.Filled.SearchOff,
                titulo = "Sin resultados",
                subtitulo = "No se encontraron contraseñas repetidas para \"$consulta\".",
                colorIcono = ColorAcento
            )
        } else {
            MensajeExitoPestana(
                icono = Icons.Filled.Check,
                titulo = "Sin contraseñas repetidas",
                subtitulo = "Todas tus contraseñas son únicas en sus respectivas cuentas."
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(espaciadoFilas),
            contentPadding = rellenoInferior
        ) {
            if (!agruparPorSitio) {
                val todasEntradas = gruposFiltrados.flatMap { it.second }
                items(todasEntradas, key = { it.id }) { entrada ->
                    FilaProblemaAgil(
                        entrada = entrada,
                        etiquetaDetalle = "Repetida",
                        colorDetalle = Peligro,
                        alCambiarRapido = { alCambiarClave(entrada) },
                        alVerDetalle = { alVerDetalle(entrada.id) },
                        ajustes = ajustes,
                        alIgnorar = { alIgnorar(entrada) },
                        mostrarIndicadores = mostrarIndicadores,
                        enGrupo = false,
                        seleccionActiva = seleccionActiva,
                        seleccionado = seleccionados.contains(entrada.id),
                        alAlternarSeleccion = { alAlternarSeleccion?.invoke(entrada.id) },
                        alPulsarLargo = { alPulsarLargo?.invoke(entrada.id) }
                    )
                }
            } else {
                items(gruposFiltrados, key = { it.first.first().contrasena }) { (grupoOriginal, entradasVisibles) ->
                    val claveGrupo = remember(grupoOriginal) { "rep_${grupoOriginal.first().contrasena.hashCode()}" }
                    val entradasOrdenadas = remember(entradasVisibles) {
                        entradasVisibles.sortedBy { claveAgrupacionPorTitulo(it) }
                    }
                    TarjetaGrupoRepetido(
                        grupo = entradasOrdenadas,
                        totalEnGrupo = grupoOriginal.size,
                        alCambiarClave = { alCambiarClave(it) },
                        alVerDetalle = { alVerDetalle(it) },
                        ajustes = ajustes,
                        alIgnorar = alIgnorar,
                        mostrarIndicadores = mostrarIndicadores,
                        colapsable = true,
                        expandido = gruposExpandidos.contains(claveGrupo),
                        alAlternar = {
                            gruposExpandidos = if (gruposExpandidos.contains(claveGrupo)) {
                                gruposExpandidos - claveGrupo
                            } else {
                                gruposExpandidos + claveGrupo
                            }
                        },
                        seleccionActiva = seleccionActiva,
                        seleccionados = seleccionados,
                        alAlternarSeleccion = alAlternarSeleccion,
                        alPulsarLargo = alPulsarLargo
                    )
                }
            }
        }
    }
}
