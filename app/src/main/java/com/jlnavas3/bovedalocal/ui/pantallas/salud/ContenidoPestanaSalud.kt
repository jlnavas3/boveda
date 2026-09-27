package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.MedidorFuerza

@Composable
fun ContenidoPestanaSalud(
    pestanaActiva: PestanaSalud,
    duplicadas: List<List<Entrada>>,
    muyComunes: List<Entrada>,
    debiles: List<Entrada>,
    antiguas: List<Entrada>,
    textoBusqueda: String,
    ahora: Long,
    alCambiarClave: (Entrada) -> Unit,
    alVerDetalle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (pestanaActiva) {
            PestanaSalud.REPETIDAS -> {
                val gruposFiltrados = remember(duplicadas, textoBusqueda) {
                    if (textoBusqueda.isBlank()) duplicadas
                    else duplicadas.filter { grupo ->
                        grupo.any { it.titulo.contains(textoBusqueda, ignoreCase = true) || it.usuario.contains(textoBusqueda, ignoreCase = true) }
                    }
                }

                if (gruposFiltrados.isEmpty()) {
                    MensajeExitoPestana(
                        icono = Icons.Filled.Check,
                        titulo = "Sin contraseñas repetidas",
                        subtitulo = "Todas tus contraseñas son únicas en sus respectivas cuentas."
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(gruposFiltrados, key = { it.first().contrasena }) { grupo ->
                            TarjetaGrupoRepetido(
                                grupo = grupo,
                                alCambiarClave = { alCambiarClave(it) },
                                alVerDetalle = { alVerDetalle(it) }
                            )
                        }
                    }
                }
            }

            PestanaSalud.COMUNES -> {
                val filtradas = remember(muyComunes, textoBusqueda) {
                    if (textoBusqueda.isBlank()) muyComunes
                    else muyComunes.filter { it.titulo.contains(textoBusqueda, ignoreCase = true) || it.usuario.contains(textoBusqueda, ignoreCase = true) }
                }

                if (filtradas.isEmpty()) {
                    MensajeExitoPestana(
                        icono = Icons.Filled.Check,
                        titulo = "Sin contraseñas filtradas",
                        subtitulo = "Ninguna de tus contraseñas coincide con listas globales de filtraciones públicas."
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            GrupoAjustes(etiqueta = "Contraseñas filtradas o comunes (${filtradas.size})") {
                                filtradas.forEachIndexed { index, entrada ->
                                    if (index > 0) SeparadorFilaSimple()
                                    FilaProblemaAgil(
                                        entrada = entrada,
                                        etiquetaDetalle = "Filtrada",
                                        colorDetalle = Peligro,
                                        alCambiarRapido = { alCambiarClave(entrada) },
                                        alVerDetalle = { alVerDetalle(entrada.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            PestanaSalud.DEBILES -> {
                val filtradas = remember(debiles, textoBusqueda) {
                    if (textoBusqueda.isBlank()) debiles
                    else debiles.filter { it.titulo.contains(textoBusqueda, ignoreCase = true) || it.usuario.contains(textoBusqueda, ignoreCase = true) }
                }

                if (filtradas.isEmpty()) {
                    MensajeExitoPestana(
                        icono = Icons.Filled.Check,
                        titulo = "Sin contraseñas débiles",
                        subtitulo = "Todas tus contraseñas tienen suficiente longitud y complejidad."
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            GrupoAjustes(etiqueta = "Contraseñas de baja entropía (${filtradas.size})") {
                                filtradas.forEachIndexed { index, entrada ->
                                    if (index > 0) SeparadorFilaSimple()
                                    val fuerza = MedidorFuerza.medir(entrada.contrasena)
                                    FilaProblemaAgil(
                                        entrada = entrada,
                                        etiquetaDetalle = fuerza.etiqueta,
                                        colorDetalle = Peligro,
                                        alCambiarRapido = { alCambiarClave(entrada) },
                                        alVerDetalle = { alVerDetalle(entrada.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            PestanaSalud.ANTIGUAS -> {
                val filtradas = remember(antiguas, textoBusqueda) {
                    if (textoBusqueda.isBlank()) antiguas
                    else antiguas.filter { it.titulo.contains(textoBusqueda, ignoreCase = true) || it.usuario.contains(textoBusqueda, ignoreCase = true) }
                }

                if (filtradas.isEmpty()) {
                    MensajeExitoPestana(
                        icono = Icons.Filled.Check,
                        titulo = "Contraseñas al día",
                        subtitulo = "Tus credenciales han sido actualizadas recientemente."
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            GrupoAjustes(etiqueta = "Sin actualizar en más de $DIAS_AVISO_ANTIGUEDAD días (${filtradas.size})") {
                                filtradas.forEachIndexed { index, entrada ->
                                    if (index > 0) SeparadorFilaSimple()
                                    val dias = diasDesde(entrada.modificadaEn, ahora)
                                    FilaProblemaAgil(
                                        entrada = entrada,
                                        etiquetaDetalle = "hace $dias d",
                                        colorDetalle = ColorAcento,
                                        alCambiarRapido = { alCambiarClave(entrada) },
                                        alVerDetalle = { alVerDetalle(entrada.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
