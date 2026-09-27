package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.historial.BannerAutodestruccionHistorial
import com.jlnavas3.bovedalocal.ui.pantallas.historial.DialogoVaciarHistorial
import com.jlnavas3.bovedalocal.ui.pantallas.historial.EstadoVacioHistorial
import com.jlnavas3.bovedalocal.ui.pantallas.historial.FilaClaveHistorial
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PantallaHistorialClaves(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var confirmarVaciar by remember { mutableStateOf(false) }
    var ahora by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        vm.recargarAjustes()
        while (true) {
            ahora = System.currentTimeMillis()
            delay(1000)
        }
    }

    val clavesVigentes = remember(ajustes.historialClaves, ahora, ajustes.historialClavesVaciadoAuto, ajustes.historialClavesTiempoAutoDestruccion) {
        if (ajustes.historialClavesVaciadoAuto && ajustes.historialClavesTiempoAutoDestruccion > 0) {
            ajustes.historialClaves.filter { ahora - it.generadaEn < ajustes.historialClavesTiempoAutoDestruccion }
        } else {
            ajustes.historialClaves
        }
    }

    val formatoFecha = remember { SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault()) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            BarraSuperiorPantalla(
                titulo = "Historial de claves",
                alVolver = { vm.volverAtras() },
                acciones = {
                    if (clavesVigentes.isNotEmpty()) {
                        IconButton(onClick = {
                            haptica.toque()
                            confirmarVaciar = true
                        }) {
                            Icon(
                                imageVector = Icons.Filled.DeleteSweep,
                                contentDescription = "Vaciar historial",
                                tint = Peligro,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )
            DescripcionPantalla(
                subtitulo = if (clavesVigentes.isEmpty()) "Sin contraseñas recientes" else "${clavesVigentes.size} contraseña${if (clavesVigentes.size == 1) "" else "s"} generada${if (clavesVigentes.size == 1) "" else "s"}"
            )

            Spacer(Modifier.height(8.dp))

            BannerAutodestruccionHistorial(
                autodestruccionActiva = ajustes.historialClavesVaciadoAuto,
                tiempoAutoDestruccion = ajustes.historialClavesTiempoAutoDestruccion
            )

            Spacer(Modifier.height(10.dp))

            if (clavesVigentes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    EstadoVacioHistorial()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(clavesVigentes, key = { it.id }) { item ->
                        FilaClaveHistorial(
                            item = item,
                            formatoFecha = formatoFecha,
                            ahora = ahora,
                            vaciadoAuto = ajustes.historialClavesVaciadoAuto,
                            tiempoDestruccion = ajustes.historialClavesTiempoAutoDestruccion,
                            alCopiar = { vm.copiar("Contraseña", item.clave, true) },
                            alEliminar = { vm.eliminarDeHistorialClaves(item.id) }
                        )
                    }
                }
            }
        }
    }

    if (confirmarVaciar) {
        DialogoVaciarHistorial(
            alConfirmar = {
                confirmarVaciar = false
                vm.vaciarHistorialClaves()
            },
            alDescartar = { confirmarVaciar = false }
        )
    }
}
