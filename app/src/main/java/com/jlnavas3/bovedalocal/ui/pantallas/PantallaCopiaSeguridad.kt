package com.jlnavas3.bovedalocal.ui.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoContrasena
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaCopiaSeguridad(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var passwordExportacion by remember { mutableStateOf("") }
    var dialogoExportar by remember { mutableStateOf(false) }
    var dialogoImportar by remember { mutableStateOf(false) }
    var uriPendiente by remember { mutableStateOf<Uri?>(null) }
    var mostrarPassBackupAuto by remember { mutableStateOf(false) }

    val lanzadorCrear = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        BovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            val clave = passwordExportacion
            passwordExportacion = ""
            vm.exportar(clave) { datos ->
                contexto.contentResolver.openOutputStream(uri)?.use { it.write(datos) }
            }
        }
    }

    val lanzadorAbrir = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        BovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            uriPendiente = uri
            dialogoImportar = true
        }
    }

    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Copia de seguridad",
                idEtiqueta = "02.1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Exporta, restaura y respalda tus contraseñas de forma cifrada")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Acciones principales de respaldo
                ComponenteGrupo(
                    etiqueta = "Copia cifrada (.bvda)",
                    idGrupo = "02.1.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Archivo seguro cifrado con Argon2id + ChaCha20-Poly1305"
                ) {
                    ComponenteNavegacion(
                        titulo = "Exportar bóveda cifrada",
                        icono = Icons.Filled.FileDownload,
                        colorIcono = ColorExportacion,
                        valorTexto = ".bvda",
                        idFila = "02.1.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { dialogoExportar = true }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Importar copia de seguridad",
                        icono = Icons.Filled.FileUpload,
                        colorIcono = ColorExportacion,
                        idFila = "02.1.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            BovedaApp.salidaPendiente(contexto)
                            try {
                                lanzadorAbrir.launch(arrayOf("*/*"))
                            } catch (e: Exception) {
                                BovedaApp.salidaTerminada(contexto)
                                vm.avisar("No se encontró ningún selector de archivos disponible")
                            }
                        }
                    )
                }

                Spacer(Modifier.height(18.dp))

                // Grupo 2: Copia automática rotativa
                ComponenteGrupo(
                    etiqueta = "Copia automática local",
                    idGrupo = "02.1.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Copias periódicas cifradas en Descargas con rotación de versiones"
                ) {
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

                    ComponenteSelectorModal(
                        titulo = "Frecuencia de copia",
                        descripcionModal = "Periodicidad con la que se genera un respaldo cifrado en Descargas",
                        icono = Icons.Filled.Autorenew,
                        colorIcono = ColorExportacion,
                        idFila = "02.1.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.backupAutoFrecuenciaDias,
                        opciones = opcionesFrecuencia,
                        alSeleccionar = { dias ->
                            haptica.tic()
                            vm.ajustarBackupAutoFrecuenciaDias(dias)
                        }
                    )

                    if (ajustes.backupAutoFrecuenciaDias > 0) {
                        ComponenteSeparador()
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                            ComponenteCampoTexto(
                                valor = ajustes.backupAutoPasswordCifrado,
                                etiqueta = "Contraseña para la copia automática",
                                alCambiar = { vm.ajustarBackupAutoPasswordCifrado(it) },
                                tipo = TipoCampoTexto.CONTRASENA,
                                mostrarContrasena = mostrarPassBackupAuto,
                                alAlternarMostrarContrasena = { mostrarPassBackupAuto = !mostrarPassBackupAuto }
                            )
                            Spacer(Modifier.height(8.dp))
                            ComponenteCampoTexto(
                                valor = ajustes.backupAutoPatronNombre,
                                etiqueta = "Patrón del nombre del archivo",
                                alCambiar = { vm.ajustarBackupAutoPatronNombre(it) },
                                monoespaciada = true
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Usa {99} para número secuencial y {FECHA} para fecha/hora. Se guarda como .bvda en Descargas/BovedaLocal/Backups/",
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
                            Text(
                                "$textoUltima (Rotación máxima: ${ajustes.backupAutoMaxCopias} copias)",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer grupo",
                            alPulsar = {
                                haptica.toque()
                                vm.ajustarBackupAutoFrecuenciaDias(0)
                                vm.ajustarBackupAutoPasswordCifrado("")
                                vm.ajustarBackupAutoPatronNombre("{99}-backup-{FECHA}")
                            }
                        )
                    }

                    if (ajustes.backupAutoFrecuenciaDias > 0) {
                        ComponenteSeparador()
                        ComponenteNavegacion(
                            titulo = "Ejecutar copia automática ahora",
                            icono = Icons.Filled.PlayArrow,
                            colorIcono = ColorExportacion,
                            idFila = "02.1.4",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alPulsar = {
                                haptica.toque()
                                vm.ejecutarBackupAutomatico(manual = true)
                            }
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                // Grupo 3: Recordatorio de exportación
                ComponenteGrupo(
                    etiqueta = "Recordatorio de respaldo",
                    idGrupo = "02.1.G3",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Aviso periódico en la bóveda si pasa mucho tiempo sin exportar"
                ) {
                    val opcionesRecordatorio = remember {
                        AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION.map { (dias, etiqueta) ->
                            val desc = when (dias) {
                                0 -> "No mostrar avisos por falta de respaldo"
                                -30 -> "Modo de prueba rápido (notifica cada media hora)"
                                30 -> "Frecuencia recomendada para copias manuales"
                                60 -> "Aviso bimestral si la bóveda no cambia a menudo"
                                90 -> "Aviso trimestral para mínimo mantenimiento"
                                else -> "Recordatorio periódico en la pantalla principal"
                            }
                            val icono = when (dias) {
                                0 -> Icons.Filled.Block
                                -30 -> Icons.Filled.Today
                                30 -> Icons.Filled.CalendarToday
                                60 -> Icons.Filled.CalendarMonth
                                else -> Icons.Filled.EventRepeat
                            }
                            OpcionSelectorModal(dias, etiqueta, etiqueta, desc, icono)
                        }
                    }

                    ComponenteSelectorModal(
                        titulo = "Frecuencia del recordatorio",
                        descripcionModal = "Periodicidad con la que se avisa en la bóveda si no se ha exportado",
                        icono = Icons.Filled.EventRepeat,
                        colorIcono = ColorExportacion,
                        idFila = "02.1.5",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.recordatorioExportacionDias,
                        opciones = opcionesRecordatorio,
                        alSeleccionar = { dias ->
                            haptica.tic()
                            vm.ajustarRecordatorioExportacion(dias)
                        }
                    )

                    ComponenteSeparador()

                    ComponenteBotonFila(
                        titulo = "Restablecer",
                        alPulsar = {
                            haptica.toque()
                            vm.ajustarRecordatorioExportacion(30)
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (dialogoExportar) {
        DialogoContrasena(
            titulo = "Contraseña de la copia",
            descripcion = "Elige una contraseña exclusiva para este archivo. Apúntala bien: sin ella la copia no se podrá recuperar jamás.",
            textoBoton = "Exportar",
            alConfirmar = { clave ->
                passwordExportacion = clave
                dialogoExportar = false
                BovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorCrear.launch("boveda-local-${System.currentTimeMillis()}.bvda")
                } catch (e: Exception) {
                    BovedaApp.salidaTerminada(contexto)
                    passwordExportacion = ""
                    vm.avisar("No se encontró ningún selector de archivos disponible")
                }
            },
            alCancelar = { dialogoExportar = false }
        )
    }

    if (dialogoImportar) {
        DialogoContrasena(
            titulo = "Contraseña de la copia",
            descripcion = "Escribe la contraseña con la que protegiste ese archivo al crearlo.",
            textoBoton = "Importar",
            alConfirmar = { clave ->
                val uri = uriPendiente
                dialogoImportar = false
                uriPendiente = null
                if (uri != null) {
                    vm.importar(clave) {
                        contexto.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                            ?: throw IllegalStateException("No se pudo leer el archivo")
                    }
                }
            },
            alCancelar = {
                dialogoImportar = false
                uriPendiente = null
            }
        )
    }
}
