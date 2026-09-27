package com.jlnavas3.bovedalocal.ui.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.GestorBackupAutomatico
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoContrasena
import com.jlnavas3.bovedalocal.ui.pantallas.copia.GrupoAccionesCopiaCifrada
import com.jlnavas3.bovedalocal.ui.pantallas.copia.GrupoCopiaAutomaticaLocal
import com.jlnavas3.bovedalocal.ui.pantallas.copia.GrupoRecordatorioRespaldo
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            GestorBackupAutomatico.indexarBackups(contexto)
        }
    }

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
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
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Exporta, restaura y respalda tus contraseñas de forma cifrada")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Acciones principales de respaldo (.bvda, selectiva, importar)
                GrupoAccionesCopiaCifrada(
                    ajustes = ajustes,
                    vm = vm,
                    contexto = contexto,
                    lanzadorAbrir = lanzadorAbrir,
                    alSolicitarExportar = { dialogoExportar = true }
                )

                Spacer(Modifier.height(18.dp))

                // Grupo 2: Copia automática rotativa
                GrupoCopiaAutomaticaLocal(
                    ajustes = ajustes,
                    seccionDestino = seccionDestino,
                    vm = vm,
                    haptica = haptica
                )

                Spacer(Modifier.height(18.dp))

                // Grupo 3: Recordatorio de exportación
                GrupoRecordatorioRespaldo(
                    ajustes = ajustes,
                    vm = vm,
                    haptica = haptica
                )

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
