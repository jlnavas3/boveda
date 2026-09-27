package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.BarraBusquedaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjusteMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.salud.ContenidoPestanaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.DIAS_AVISO_ANTIGUEDAD
import com.jlnavas3.bovedalocal.ui.pantallas.salud.DialogoCambioRapidoClave
import com.jlnavas3.bovedalocal.ui.pantallas.salud.PestanaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.ResumenAuditoriaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.SelectorPestanasSalud
import com.jlnavas3.bovedalocal.ui.pantallas.salud.diasDesde
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.MedidorFuerza

@Composable
fun PantallaSaludBoveda(
    vm: VaultViewModel,
    estado: EstadoBoveda,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val claves = remember(entradas) { entradas.filter { it.tipo == TipoEntrada.LOGIN && it.contrasena.isNotBlank() } }

    val duplicadas = remember(claves) {
        claves.groupBy { it.contrasena }.values.filter { it.size > 1 }
    }
    val debiles = remember(claves) {
        claves.filter { MedidorFuerza.medir(it.contrasena).puntuacion <= 1 }
            .sortedBy { MedidorFuerza.medir(it.contrasena).puntuacion }
    }
    val muyComunes = remember(claves) {
        claves.filter { ContrasenasComunes.esComun(contexto, it.contrasena) }
    }
    val ahora = remember { System.currentTimeMillis() }
    val antiguas = remember(claves) {
        claves.filter { it.modificadaEn > 0 && diasDesde(it.modificadaEn, ahora) >= DIAS_AVISO_ANTIGUEDAD }
            .sortedBy { it.modificadaEn }
    }

    // Análisis de duplicados para alertar de copias de CSV
    val gruposDuplicados = remember(entradas) { AnalizadorDuplicados.analizar(entradas) }
    val totalSobrantesDuplicadas = remember(gruposDuplicados) { gruposDuplicados.sumOf { it.entradasSecundarias.size } }

    var pestanaActiva by remember { mutableStateOf(PestanaSalud.REPETIDAS) }
    var textoBusqueda by remember { mutableStateOf("") }
    var entradaParaCambioRapido by remember { mutableStateOf<Entrada?>(null) }
    var resumenExpandido by rememberSaveable { mutableStateOf(true) }
    val haptica = remember { Haptica(contexto) }

    val buscando = textoBusqueda.isNotBlank()

    LaunchedEffect(claves.size) {
        val resumen = "Auditoría de salud ejecutada: ${claves.size} claves analizadas (${debiles.size} débiles, ${duplicadas.size} grupos repetidos, ${muyComunes.size} comunes, ${antiguas.size} antiguas)"
        Diagnostico.apuntar("salud", resumen)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
            .imePadding()
    ) {
        BarraSuperiorPantalla(
            titulo = "Salud de la Bóveda",
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = "${claves.size} contraseñas auditadas"
            )

            // Banner inteligente de Duplicados de CSV
            AnimatedVisibility(
                visible = totalSobrantesDuplicadas > 0 && !buscando,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(10.dp))
                    GrupoAjustes(etiqueta = "Duplicados detectados") {
                        FilaAjusteMenu(
                            titulo = "Detectadas $totalSobrantesDuplicadas copias repetidas",
                            subtitulo = "Entradas idénticas de importación. Pulsa para limpiar con 1 toque",
                            icono = Icons.Filled.AutoFixHigh,
                            colorIcono = ColorSalud,
                            alPulsar = { vm.ir(Pantalla.Duplicados) }
                        )
                    }
                }
            }

            // Resumen de Auditoría
            AnimatedVisibility(
                visible = !buscando,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(10.dp))
                    ResumenAuditoriaSalud(
                        clavesCount = claves.size,
                        repetidasCount = duplicadas.sumOf { it.size },
                        muyComunesCount = muyComunes.size,
                        debilesCount = debiles.size,
                        antiguasCount = antiguas.size,
                        expandido = resumenExpandido,
                        alAlternarExpandido = {
                            haptica.tic()
                            resumenExpandido = !resumenExpandido
                        }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Buscador nativo
            BarraBusquedaAjustes(
                texto = textoBusqueda,
                alCambiarTexto = { textoBusqueda = it },
                placeholder = "Buscar servicio o cuenta..."
            )

            Spacer(Modifier.height(10.dp))

            // Selector de Pestañas
            SelectorPestanasSalud(
                pestanaActiva = pestanaActiva,
                alSeleccionarPestana = { pestanaActiva = it },
                gruposDuplicadosCount = duplicadas.size,
                totalDuplicadasCount = duplicadas.sumOf { it.size },
                muyComunesCount = muyComunes.size,
                debilesCount = debiles.size,
                antiguasCount = antiguas.size
            )

            Spacer(Modifier.height(12.dp))

            // Contenido dinámico por pestaña
            ContenidoPestanaSalud(
                pestanaActiva = pestanaActiva,
                duplicadas = duplicadas,
                muyComunes = muyComunes,
                debiles = debiles,
                antiguas = antiguas,
                textoBusqueda = textoBusqueda,
                ahora = ahora,
                alCambiarClave = { entrada -> entradaParaCambioRapido = entrada },
                alVerDetalle = { id -> vm.ir(Pantalla.Detalle(id)) },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
        }
    }

    // Modal de Cambio Rápido de Contraseña con Generador
    entradaParaCambioRapido?.let { entrada ->
        DialogoCambioRapidoClave(
            entrada = entrada,
            alDescartar = { entradaParaCambioRapido = null },
            alGuardar = { nuevaClave ->
                vm.actualizarContrasenaRapida(entrada.id, nuevaClave)
                entradaParaCambioRapido = null
            },
            alCopiar = { clave ->
                vm.copiar("Contraseña", clave, sensible = true)
            }
        )
    }
}
