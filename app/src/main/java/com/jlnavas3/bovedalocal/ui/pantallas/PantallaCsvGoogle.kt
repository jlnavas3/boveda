package com.jlnavas3.bovedalocal.ui.pantallas

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteAlerta
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoAlerta
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoBorradoManualCsv
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoImportarCsv
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
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
                idEtiqueta = "02.2",
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
                DescripcionPantalla(subtitulo = "Importa contraseñas exportadas desde Google Password Manager")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Importación de CSV
                ComponenteGrupo(
                    etiqueta = "Importación",
                    idGrupo = "02.2.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Carga de archivo 'Google Passwords.csv' desde el almacenamiento"
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Puedes exportar tu archivo CSV 'Google Passwords.csv' desde el administrador de contraseñas de Google (https://passwords.google.com/) e importarlo directamente aquí.",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    SeparadorFilaSimple()
                    ComponenteBotonFila(
                        titulo = "Importar contraseñas de Google",
                        colorIcono = ColorExportacion,
                        icono = Icons.Filled.FileUpload,
                        idFila = "02.2.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            dialogoImportarCsv = true
                        }
                    )
                }

                if (ajustes.csvGoogleRuta.isNotBlank()) {
                    Spacer(Modifier.height(18.dp))

                    // Grupo 2: Seguridad del archivo descargado
                    ComponenteGrupo(
                        etiqueta = "Seguridad del archivo CSV",
                        idGrupo = "02.2.G2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Estado de protección contra fugas de texto claro"
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (ajustes.csvGoogleEliminado) {
                                ComponenteAlerta(
                                    tipo = TipoAlerta.SUCCESS,
                                    titulo = "Archivo CSV eliminado de forma segura",
                                    mensaje = "Se importaron ${ajustes.csvGoogleCuentas} cuentas.",
                                    icono = Icons.Filled.CheckCircle
                                )
                            } else {
                                ComponenteAlerta(
                                    tipo = TipoAlerta.DANGER,
                                    titulo = "¡Atención: archivo CSV sin cifrar!",
                                    mensaje = "El archivo descargado contiene todas tus claves en texto claro y cualquier aplicación con permiso de almacenamiento podría leerlo.",
                                    icono = Icons.Filled.Security,
                                    accion = {
                                        ComponenteBotonFila(
                                            titulo = "Eliminar archivo CSV original",
                                            colorIcono = Peligro,
                                            icono = Icons.Filled.Delete,
                                            alPulsar = {
                                                mostrarDialogoBorradoManual = true
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
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
