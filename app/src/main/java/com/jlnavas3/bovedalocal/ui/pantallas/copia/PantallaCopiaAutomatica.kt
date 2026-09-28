package com.jlnavas3.bovedalocal.ui.pantallas.copia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaCopiaAutomatica(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var mostrarPassBackupAuto by remember { mutableStateOf(false) }

    ProveedorResaltadoAjustes(seccionId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Copia automática",
                idEtiqueta = "05-COP-ATM",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("05-COP-ATM-G01", "Programación"),
                            AccionSaltoGrupo("05-COP-ATM-G02", "Seguridad y formato"),
                            AccionSaltoGrupo("05-COP-ATM-G03", "Respaldo inmediato")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.ajustarBackupAutoFrecuenciaDias(0)
                            vm.ajustarBackupAutoMaxCopias(5)
                            vm.ajustarBackupAutoPasswordCifrado("")
                            vm.ajustarBackupAutoPatronNombre("{99}-backup-{FECHA}")
                            vm.avisar("Copia automática restablecida")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Grupo 1: Programación
                val opcionesFrecuencia = remember {
                    AlmacenAjustes.OPCIONES_FRECUENCIA_BACKUP_AUTO.map { (dias, etiqueta) ->
                        val desc = when (dias) {
                            0 -> "Desactiva la generación automática de respaldos"
                            1 -> "Copia diaria de seguridad en almacenamiento local"
                            7 -> "Copia semanal recomendada para uso habitual"
                            14, 30, 60 -> "Copia periódica para bóvedas con pocos cambios"
                            else -> "Respaldo automático con rotación"
                        }
                        val icono = when (dias) {
                            0 -> Icons.Filled.Block
                            1 -> Icons.Filled.Today
                            7 -> Icons.Filled.DateRange
                            14 -> Icons.Filled.CalendarMonth
                            30 -> Icons.Filled.EventRepeat
                            60 -> Icons.Filled.Event
                            else -> Icons.Filled.CalendarToday
                        }
                        OpcionSelectorModal(dias, etiqueta, etiqueta, desc, icono)
                    }
                }

                val opcionesMaxCopias = remember {
                    AlmacenAjustes.OPCIONES_MAX_COPIAS_BACKUP_AUTO.map { (max, etiqueta) ->
                        val desc = when (max) {
                            5 -> "Mantiene las últimas 5 copias generadas"
                            10 -> "Mantiene las últimas 10 copias generadas"
                            20 -> "Mantiene las últimas 20 copias generadas"
                            100 -> "Mantiene un historial amplio de 100 copias"
                            0 -> "Conserva todas las copias automáticas sin eliminarlas"
                            else -> "$max copias"
                        }
                        val icono = when (max) {
                            0 -> Icons.Filled.AllInclusive
                            else -> Icons.Filled.FilterNone
                        }
                        OpcionSelectorModal(max, etiqueta, etiqueta, desc, icono)
                    }
                }

                ComponenteGrupo(
                    etiqueta = "Programación",
                    icono = Icons.Filled.Autorenew,
                    colorIcono = ColorExportacion,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarBackupAutoFrecuenciaDias(0)
                        vm.ajustarBackupAutoMaxCopias(5)
                        vm.avisar("Programación restablecida")
                    },
                    idGrupo = "05-COP-ATM-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSelectorModal(
                        titulo = "Frecuencia de copia",
                        descripcionModal = "Periodicidad con la que se genera un respaldo cifrado en Descargas",
                        icono = null,
                        idFila = "05-COP-ATM-FRQ",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.backupAutoFrecuenciaDias,
                        opciones = opcionesFrecuencia,
                        alSeleccionar = { dias ->
                            haptica.tic()
                            vm.ajustarBackupAutoFrecuenciaDias(dias)
                        }
                    )

                    if (ajustes.backupAutoFrecuenciaDias > 0) {
                        ComponenteSeparador(sangriaInicio = 16.dp)

                        ComponenteSelectorModal(
                            titulo = "Copias a conservar",
                            descripcionModal = "Número de copias automáticas que se conservan antes de rotar y borrar las más antiguas",
                            icono = null,
                            idFila = "05-COP-ATM-ROT",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            valorSeleccionado = ajustes.backupAutoMaxCopias,
                            opciones = opcionesMaxCopias,
                            alSeleccionar = { max ->
                                haptica.tic()
                                vm.ajustarBackupAutoMaxCopias(max)
                            }
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Seguridad y formato
                ComponenteGrupo(
                    etiqueta = "Seguridad y formato",
                    icono = Icons.Filled.Security,
                    colorIcono = ColorExportacion,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarBackupAutoPasswordCifrado("")
                        vm.ajustarBackupAutoPatronNombre("{99}-backup-{FECHA}")
                        vm.avisar("Seguridad y formato restablecidos")
                    },
                    idGrupo = "05-COP-ATM-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ComponenteCampoTexto(
                            valor = ajustes.backupAutoPasswordCifrado,
                            etiqueta = "Contraseña para la copia automática",
                            alCambiar = { vm.ajustarBackupAutoPasswordCifrado(it) },
                            tipo = TipoCampoTexto.CONTRASENA,
                            mostrarContrasena = mostrarPassBackupAuto,
                            alAlternarMostrarContrasena = { mostrarPassBackupAuto = !mostrarPassBackupAuto }
                        )

                        Spacer(Modifier.height(10.dp))

                        ComponenteCampoTexto(
                            valor = ajustes.backupAutoPatronNombre,
                            etiqueta = "Patrón del nombre del archivo",
                            alCambiar = { vm.ajustarBackupAutoPatronNombre(it) },
                            monoespaciada = true
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Usa {99} para número secuencial y {FECHA} para fecha/hora. Se guarda como .bvda en Descargas/BovedaLocal/Backups/",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(Modifier.height(12.dp))

                        val textoUltima = if (ajustes.backupAutoUltimaEjecucion > 0L) {
                            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            "Última copia: ${sdf.format(Date(ajustes.backupAutoUltimaEjecucion))}"
                        } else {
                            "Última copia: Aún no realizada"
                        }
                        val textoRotacion = if (ajustes.backupAutoMaxCopias <= 0) {
                            "Rotación: Infinitas"
                        } else {
                            "Rotación máxima: ${ajustes.backupAutoMaxCopias} copias"
                        }
                        Text(
                            text = "$textoUltima ($textoRotacion)",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Grupo 3: Respaldo inmediato
                ComponenteGrupo(
                    etiqueta = "Respaldo inmediato",
                    icono = Icons.Filled.FolderSpecial,
                    colorIcono = ColorExportacion,
                    idGrupo = "05-COP-ATM-G03",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Crear respaldo en Descargas ahora",
                        subtitulo = "Genera una copia inmediata usando estos parámetros",
                        icono = Icons.Filled.PlayArrow,
                        colorIcono = ColorExportacion,
                        idFila = "05-COP-ATM-NOW",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.toque()
                            vm.ejecutarBackupAutomatico(manual = true)
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
