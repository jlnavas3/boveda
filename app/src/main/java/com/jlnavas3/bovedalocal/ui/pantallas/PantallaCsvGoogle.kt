package com.jlnavas3.bovedalocal.ui.pantallas

import android.provider.OpenableColumns
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoBorradoManualCsv
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoImportarCsv
import com.jlnavas3.bovedalocal.ui.pantallas.csv.GrupoImportacionCsvGoogle
import com.jlnavas3.bovedalocal.ui.pantallas.csv.GrupoSeguridadArchivoCsvGoogle
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaCsvGoogle(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var dialogoImportarCsv by remember { mutableStateOf(false) }
    var mostrarDialogoBorradoManual by remember { mutableStateOf(false) }

    val lanzadorAbrirCsv = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        BovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            var nombreArchivo = uri.lastPathSegment ?: "Google Passwords.csv"
            try {
                contexto.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1 && cursor.moveToFirst()) {
                        nombreArchivo = cursor.getString(nameIndex)
                    }
                }
            } catch (_: Exception) {}

            vm.importarCsv({
                contexto.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalStateException("No se pudo leer el archivo")
            }) { nuevas ->
                if (nuevas > 0) {
                    vm.registrarCsvGoogleImportado(
                        ruta = nombreArchivo,
                        uri = uri.toString(),
                        cuentas = nuevas
                    )
                }
            }
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
                titulo = "Contraseñas de Google",
                idEtiqueta = "05.2",
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
                // Grupo 1: Importación de CSV
                GrupoImportacionCsvGoogle(
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alIniciarImportacion = { dialogoImportarCsv = true }
                )

                if (ajustes.csvGoogleRuta.isNotBlank()) {
                    Spacer(Modifier.height(14.dp))

                    // Grupo 2: Seguridad del archivo descargado
                    GrupoSeguridadArchivoCsvGoogle(
                        mostrarId = ajustes.mostrarIdsAjustes,
                        csvGoogleEliminado = ajustes.csvGoogleEliminado,
                        csvGoogleCuentas = ajustes.csvGoogleCuentas,
                        alSolicitarBorrado = { mostrarDialogoBorradoManual = true }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (dialogoImportarCsv) {
        DialogoImportarCsv(
            alDescartar = { dialogoImportarCsv = false },
            alConfirmar = {
                dialogoImportarCsv = false
                BovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorAbrirCsv.launch(arrayOf("text/csv", "text/comma-separated-values", "text/plain", "*/*"))
                } catch (e: Exception) {
                    BovedaApp.salidaTerminada(contexto)
                    vm.avisar("No se encontró ningún selector de archivos disponible")
                }
            }
        )
    }

    if (mostrarDialogoBorradoManual) {
        DialogoBorradoManualCsv(
            rutaArchivo = ajustes.csvGoogleRuta,
            alDescartar = { mostrarDialogoBorradoManual = false },
            alConfirmar = {
                mostrarDialogoBorradoManual = false
                vm.descartarAvisoCsvGoogle()
            }
        )
    }
}
