package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.migracion.BarraControlesSeleccionMigracion
import com.jlnavas3.bovedalocal.ui.pantallas.migracion.BotonesAccionInferioresMigracion
import com.jlnavas3.bovedalocal.ui.pantallas.migracion.EstadoVacioMigracion
import com.jlnavas3.bovedalocal.ui.pantallas.migracion.FilaCuentaMigracion
import com.jlnavas3.bovedalocal.util.CuentaGoogleAuth
import com.jlnavas3.bovedalocal.util.GoogleAuthMigration
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla interactiva que muestra el listado de cuentas extraídas de un QR de Google Authenticator,
 * permitiendo seleccionar/desmarcar con checkboxes antes de registrarlas en la bóveda.
 *
 * Incluye detección de cuentas ya existentes en la bóveda, desmarcándolas automáticamente por defecto.
 */
@Composable
fun PantallaConfirmarMigracion(
    vm: VaultViewModel,
    urlMigracion: String
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()

    val entradasExistentes = (estadoBoveda as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    val secretosEnBoveda = remember(entradasExistentes) {
        entradasExistentes.mapNotNull { it.secretoTotp?.uppercase()?.trim() }.toSet()
    }

    val cuentasExtraidas = remember(urlMigracion) {
        GoogleAuthMigration.decodificar(urlMigracion)
    }

    val listaCuentas = remember(cuentasExtraidas, secretosEnBoveda) {
        mutableStateListOf<CuentaGoogleAuth>().apply {
            val preparadas = cuentasExtraidas.map { c ->
                val yaExiste = secretosEnBoveda.contains(c.secretoBase32.uppercase().trim())
                c.copy(seleccionada = !yaExiste)
            }
            addAll(preparadas)
        }
    }

    val cuantasSeleccionadas = listaCuentas.count { it.seleccionada }
    val todasSonExistentes = remember(listaCuentas, secretosEnBoveda) {
        listaCuentas.isNotEmpty() && listaCuentas.all { secretosEnBoveda.contains(it.secretoBase32.uppercase().trim()) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Importar de Google Authenticator",
            idEtiqueta = "2FA",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (listaCuentas.isEmpty())
                    "No se pudieron leer cuentas válidas en este código"
                else if (todasSonExistentes)
                    "Todas las cuentas de este código QR ya existen en tu Bóveda Local."
                else
                    "Se encontraron ${listaCuentas.size} cuentas. Las cuentas nuevas están seleccionadas automáticamente:"
            )

            Spacer(Modifier.height(10.dp))

            if (listaCuentas.isEmpty()) {
                EstadoVacioMigracion()
            } else {
                BarraControlesSeleccionMigracion(
                    cuantasSeleccionadas = cuantasSeleccionadas,
                    totalCuentas = listaCuentas.size,
                    alSeleccionarSoloNuevas = {
                        haptica.tic()
                        for (i in listaCuentas.indices) {
                            val yaExiste = secretosEnBoveda.contains(listaCuentas[i].secretoBase32.uppercase().trim())
                            listaCuentas[i] = listaCuentas[i].copy(seleccionada = !yaExiste)
                        }
                    },
                    alSeleccionarTodas = {
                        haptica.tic()
                        for (i in listaCuentas.indices) {
                            listaCuentas[i] = listaCuentas[i].copy(seleccionada = true)
                        }
                    },
                    alSeleccionarNinguna = {
                        haptica.tic()
                        for (i in listaCuentas.indices) {
                            listaCuentas[i] = listaCuentas[i].copy(seleccionada = false)
                        }
                    }
                )

                Spacer(Modifier.height(4.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(listaCuentas, key = { _, item -> item.id }) { index, cuenta ->
                        val yaExisteEnBoveda = secretosEnBoveda.contains(cuenta.secretoBase32.uppercase().trim())

                        FilaCuentaMigracion(
                            cuenta = cuenta,
                            yaExisteEnBoveda = yaExisteEnBoveda,
                            alAlternarSeleccion = {
                                haptica.tic()
                                listaCuentas[index] = cuenta.copy(seleccionada = !cuenta.seleccionada)
                            },
                            onCheckedChange = { checked ->
                                listaCuentas[index] = cuenta.copy(seleccionada = checked)
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            BotonesAccionInferioresMigracion(
                mostrarBotonImportar = listaCuentas.isNotEmpty(),
                cuantasSeleccionadas = cuantasSeleccionadas,
                alImportar = {
                    haptica.exito()
                    vm.importarCuentasGoogleAuth(listaCuentas)
                }
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}
