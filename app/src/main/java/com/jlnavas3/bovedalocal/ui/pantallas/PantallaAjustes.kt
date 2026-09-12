package com.jlnavas3.bovedalocal.ui.pantallas

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.PepoBovedaApp
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.modoBiometriaActivo
import com.jlnavas3.bovedalocal.ui.FlujoBiometria
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoBorradoManualCsv
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoBorrarBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoCambioMaestra
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoContrasena
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoImportarCsv
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoModoCompatible
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeccionApariencia
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeccionAutenticador2FA
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeccionAvanzada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeccionCamara
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeccionCopiaSeguridad
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeccionCsvGoogle
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeccionSeguridad
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeccionTileRapido
import com.jlnavas3.bovedalocal.util.Biometria
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAjustes(vm: VaultViewModel, actividad: FragmentActivity) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var passwordExportacion by remember { mutableStateOf("") }
    var dialogoExportar by remember { mutableStateOf(false) }
    var dialogoImportar by remember { mutableStateOf(false) }
    var dialogoCambio by remember { mutableStateOf(false) }
    var dialogoBorrar by remember { mutableStateOf(false) }
    var dialogoImportarCsv by remember { mutableStateOf(false) }
    var mostrarDialogoBorradoManual by remember { mutableStateOf(false) }
    var uriPendiente by remember { mutableStateOf<Uri?>(null) }

    val lanzadorCrear = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        PepoBovedaApp.salidaTerminada(contexto)
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
        PepoBovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            uriPendiente = uri
            dialogoImportar = true
        }
    }

    val lanzadorAbrirCsv = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        PepoBovedaApp.salidaTerminada(contexto)
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

    // --------------------------------------------------------------- huella
    val flujo = remember { FlujoBiometria(actividad, vm.repositorio) }
    // La capacidad se vuelve a preguntar al volver a esta pantalla: si el usuario acaba
    // de registrar una huella en Android, aquí tiene que aparecer sin reiniciar nada.
    var capacidad by remember { mutableStateOf(Biometria.capacidad(contexto)) }
    LifecycleResumeEffect(Unit) {
        capacidad = Biometria.capacidad(contexto)
        onPauseOrDispose { }
    }
    val nivel = Biometria.decidirNivel(capacidad)
    val modoActivo = ajustes.modoBiometriaActivo
    var dialogoCompatible by remember { mutableStateOf<String?>(null) }

    fun tratarActivacion(resultado: FlujoBiometria.ResultadoActivacion) {
        when (resultado) {
            is FlujoBiometria.ResultadoActivacion.Activada -> {
                haptica.exito()
                vm.avisar(
                    if (resultado.modo == BiometricKeyStore.Modo.FUERTE) "Huella activada en modo fuerte"
                    else "Huella activada en modo compatible"
                )
            }
            FlujoBiometria.ResultadoActivacion.Cancelada -> vm.avisar("Huella cancelada")
            is FlujoBiometria.ResultadoActivacion.FuerteRota -> {
                haptica.error()
                dialogoCompatible = "Android acepta tu huella, pero el Keystore de este móvil la rechaza al usarla " +
                    "(fallo típico de ROMs personalizadas). Detalle técnico: ${resultado.detalle}."
            }
            is FlujoBiometria.ResultadoActivacion.Error -> {
                haptica.error()
                vm.avisar(resultado.texto)
            }
        }
    }

    // La comprobación de "bóveda abierta" la hace FlujoBiometria.activar; aquí no se repite.
    fun activarFuerte() = flujo.activar(BiometricKeyStore.Modo.FUERTE, ::tratarActivacion)

    fun ofrecerCompatible(motivo: String) {
        dialogoCompatible = motivo
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        CabeceraPantalla(
            titulo = "Ajustes",
            subtitulo = "Configuración y personalización de la bóveda",
            alVolver = { vm.volverAtras() }
        )

        SeccionSeguridad(
            vm = vm,
            ajustes = ajustes,
            contexto = contexto,
            haptica = haptica,
            flujo = flujo,
            modoActivo = modoActivo,
            nivel = nivel,
            capacidad = capacidad,
            activarFuerte = ::activarFuerte,
            ofrecerCompatible = ::ofrecerCompatible
        )

        Spacer(Modifier.height(16.dp))

        SeccionTileRapido(
            vm = vm,
            ajustes = ajustes,
            contexto = contexto,
            haptica = haptica
        )

        Spacer(Modifier.height(16.dp))

        SeccionCamara(
            vm = vm,
            ajustes = ajustes,
            haptica = haptica
        )

        Spacer(Modifier.height(16.dp))

        SeccionCopiaSeguridad(
            vm = vm,
            ajustes = ajustes,
            haptica = haptica,
            alExportar = { dialogoExportar = true },
            alImportar = {
                PepoBovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorAbrir.launch(arrayOf("*/*"))
                } catch (e: Exception) {
                    PepoBovedaApp.salidaTerminada(contexto)
                    vm.avisar("Este móvil no tiene ningún selector de archivos que pueda abrir")
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        SeccionAutenticador2FA(
            vm = vm,
            ajustes = ajustes,
            haptica = haptica
        )

        Spacer(Modifier.height(16.dp))

        SeccionCsvGoogle(
            vm = vm,
            ajustes = ajustes,
            contexto = contexto,
            haptica = haptica,
            alAbrirDialogoImportar = { dialogoImportarCsv = true },
            alMostrarDialogoBorradoManual = { mostrarDialogoBorradoManual = true }
        )

        Spacer(Modifier.height(16.dp))

        SeccionApariencia(
            vm = vm,
            ajustes = ajustes,
            haptica = haptica
        )

        Spacer(Modifier.height(16.dp))

        SeccionAvanzada(
            vm = vm,
            ajustes = ajustes,
            haptica = haptica,
            alCambiarMaestra = { dialogoCambio = true },
            alBorrarBoveda = { dialogoBorrar = true }
        )

        Spacer(Modifier.height(32.dp))
    }

    dialogoCompatible?.let { motivo ->
        DialogoModoCompatible(
            motivo = motivo,
            alDescartar = { dialogoCompatible = null },
            alConfirmar = {
                dialogoCompatible = null
                flujo.activar(BiometricKeyStore.Modo.COMPATIBLE, ::tratarActivacion)
            }
        )
    }

    if (dialogoExportar) {
        DialogoContrasena(
            titulo = "Contraseña de la copia",
            descripcion = "Elige una contraseña solo para este archivo. Apúntala donde toque: sin ella la copia no se abre.",
            textoBoton = "Exportar",
            alConfirmar = { clave ->
                passwordExportacion = clave
                dialogoExportar = false
                PepoBovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorCrear.launch("pepo-boveda-${System.currentTimeMillis()}.bvda")
                } catch (e: Exception) {
                    PepoBovedaApp.salidaTerminada(contexto)
                    passwordExportacion = ""
                    vm.avisar("Este móvil no tiene ningún selector de archivos que pueda abrir")
                }
            },
            alCancelar = { dialogoExportar = false }
        )
    }

    if (dialogoImportar) {
        DialogoContrasena(
            titulo = "Contraseña de la copia",
            descripcion = "Escribe la contraseña con la que cifraste ese archivo.",
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
            alCancelar = { dialogoImportar = false; uriPendiente = null }
        )
    }

    if (dialogoCambio) {
        DialogoCambioMaestra(
            alDescartar = { dialogoCambio = false },
            alConfirmar = { actual, nueva ->
                dialogoCambio = false
                vm.cambiarContrasenaMaestra(actual, nueva)
            }
        )
    }

    if (dialogoImportarCsv) {
        DialogoImportarCsv(
            alDescartar = { dialogoImportarCsv = false },
            alConfirmar = {
                dialogoImportarCsv = false
                PepoBovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorAbrirCsv.launch(arrayOf("text/csv", "text/comma-separated-values", "text/plain", "*/*"))
                } catch (e: Exception) {
                    PepoBovedaApp.salidaTerminada(contexto)
                    vm.avisar("Este móvil no tiene ningún selector de archivos que pueda abrir")
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

    if (dialogoBorrar) {
        DialogoBorrarBoveda(
            alDescartar = { dialogoBorrar = false },
            alConfirmar = {
                dialogoBorrar = false
                vm.repositorio.borrarTodo()
                vm.ir(Pantalla.Onboarding)
            }
        )
    }
}
