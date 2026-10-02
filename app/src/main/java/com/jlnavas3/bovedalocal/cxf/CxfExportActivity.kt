package com.jlnavas3.bovedalocal.cxf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.credentials.providerevents.IntentHandler
import androidx.credentials.providerevents.exception.ImportCredentialsSystemErrorException
import androidx.credentials.providerevents.transfer.ImportCredentialsResponse
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.pantallas.cxf.PantallaExportacionInteractivaCxf
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme

/**
 * Actividad interactiva que atiende la acción `androidx.identitycredentials.action.IMPORT_CREDENTIALS`.
 * Invocada por el selector de credenciales del sistema Android (Google Password Manager, Dashlane, etc.)
 * cuando el usuario selecciona Bóveda Local como origen de la transferencia.
 *
 * Muestra una pantalla interactiva para que el usuario seleccione exactamente qué credenciales
 * transferir (toda la bóveda o selección granular) antes de generar y entregar el paquete FIDO CXF.
 */
class CxfExportActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CxfRegistroDiagnostico.log(this, ">>> CxfExportActivity iniciada con Action: ${intent.action}")

        val providerRequest = try {
            IntentHandler.retrieveProviderImportCredentialsRequest(intent)
        } catch (e: Exception) {
            CxfRegistroDiagnostico.log(this, "Fallo al procesar intent con IntentHandler", e)
            null
        }

        val destinationUri = providerRequest?.uri ?: intent.data
        CxfRegistroDiagnostico.log(this, "URI destino para transferencia: $destinationUri")

        if (destinationUri == null) {
            val msgError = "No se encontró URI de destino de transferencia"
            CxfRegistroDiagnostico.log(this, msgError)
            mostrarToast("Bóveda Local: $msgError")
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }

        val rawCallingPackage = providerRequest?.callingAppInfo?.packageName
            ?: intent.getStringExtra("androidx.credentials.providerevents.extra.CALLING_PACKAGE_NAME")
            ?: callingActivity?.packageName
            ?: "desconocido"

        val gestorReceptor = when {
            rawCallingPackage.contains("google.android.gms", ignoreCase = true) -> "Google Password Manager"
            rawCallingPackage.contains("dashlane", ignoreCase = true) -> "Dashlane"
            rawCallingPackage.contains("bitwarden", ignoreCase = true) -> "Bitwarden"
            rawCallingPackage.contains("1password", ignoreCase = true) -> "1Password"
            rawCallingPackage.contains("proton", ignoreCase = true) -> "Proton Pass"
            else -> rawCallingPackage
        }

        val requestedTypes = providerRequest?.request?.credentialTypes ?: emptySet()
        CxfRegistroDiagnostico.log(this, "Gestor receptor: $gestorReceptor ($rawCallingPackage). Tipos solicitados: $requestedTypes")

        val repo = VaultRepository.obtener(applicationContext)
        val ajustes = repo.ajustes.actual
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTemaCompleto(ajustes)

        // Cargar entradas disponibles (de memoria activa o del estado preparado previamente)
        val entradasDisponibles = run {
            val enMemoria = try {
                repo.entradas().filter { it.eliminadaEn == 0L }
            } catch (_: Exception) {
                emptyList()
            }
            if (enMemoria.isNotEmpty()) {
                enMemoria
            } else {
                val jsonGuardado = CxfPersistenciaPayload.recuperarPayload(applicationContext)
                    ?: CxfGestorTransferencia.obtenerJsonExportacion()
                if (!jsonGuardado.isNullOrBlank()) {
                    CxfConvertidor.convertir(jsonGuardado).entradas
                } else {
                    emptyList()
                }
            }
        }

        if (entradasDisponibles.isEmpty()) {
            val msgVacio = "La bóveda no contiene credenciales activas para transferir"
            CxfRegistroDiagnostico.log(this, msgVacio)
            mostrarToast("Bóveda Local: $msgVacio")

            val resultIntent = Intent()
            IntentHandler.setImportCredentialsException(
                resultIntent,
                ImportCredentialsSystemErrorException(msgVacio)
            )
            setResult(Activity.RESULT_CANCELED, resultIntent)
            finish()
            return
        }

        val esPersonalizada = CxfGestorTransferencia.esSeleccionPersonalizada(this)
        val idsPreseleccionadas = CxfGestorTransferencia.obtenerIdsSeleccionados(this) ?: emptySet()
        CxfRegistroDiagnostico.log(this, "Entradas en bóveda: ${entradasDisponibles.size}, Preseleccionadas: ${idsPreseleccionadas.size}, Personalizada: $esPersonalizada")

        val entradasAMostrar = CxfFiltradorEntradasExportacion.filtrar(
            entradasDisponibles = entradasDisponibles,
            idsPreseleccionadas = idsPreseleccionadas,
            esSeleccionPersonalizada = esPersonalizada
        )

        setContent {
            val ajustesState by repo.ajustes.ajustes.collectAsStateWithLifecycle()
            androidx.compose.runtime.LaunchedEffect(ajustesState) {
                com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTemaCompleto(ajustesState)
            }
            BovedaTheme(temaApp = ajustesState.temaApp) {
                PantallaExportacionInteractivaCxf(
                    gestorReceptor = gestorReceptor,
                    entradasDisponibles = entradasAMostrar,
                    idsIniciales = idsPreseleccionadas,
                    mostrarIdAjustes = ajustesState.mostrarIdsAjustes,
                    alConfirmar = { seleccionadas ->
                        completarTransferencia(destinationUri, seleccionadas, gestorReceptor)
                    },
                    alCancelar = {
                        CxfRegistroDiagnostico.log(this@CxfExportActivity, "Transferencia cancelada por el usuario en pantalla de selección")
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                )
            }
        }
    }

    private fun completarTransferencia(
        destinationUri: Uri,
        entradasAExportar: List<Entrada>,
        nombreGestor: String
    ) {
        if (entradasAExportar.isEmpty()) {
            mostrarToast("Selecciona al menos una credencial para transferir")
            return
        }

        CxfRegistroDiagnostico.log(this, "Generando FIDO CXF para ${entradasAExportar.size} credenciales seleccionadas...")
        val jsonPayload = CxfExportador.exportarAJson(entradasAExportar)
        CxfRegistroDiagnostico.log(this, "Payload CXF generado (${jsonPayload.length} caracteres)")

        var transferidoConExito = false
        val resultIntent = Intent()

        // Intento A: Método estándar IntentHandler de androidx.credentials.providerevents
        try {
            IntentHandler.setImportCredentialsResponse(
                this,
                destinationUri,
                resultIntent,
                ImportCredentialsResponse(jsonPayload)
            )
            transferidoConExito = true
            CxfRegistroDiagnostico.log(this, "IntentHandler escribió correctamente en $destinationUri")
        } catch (e: Exception) {
            CxfRegistroDiagnostico.log(this, "Fallo IntentHandler al escribir en URI: ${e.message}", e)
        }

        // Intento B: Escritura directa a través de ContentResolver
        if (!transferidoConExito) {
            try {
                contentResolver.openOutputStream(destinationUri, "wt")?.use { outputStream ->
                    outputStream.bufferedWriter(Charsets.UTF_8).use { writer ->
                        writer.write(jsonPayload)
                        writer.flush()
                    }
                    transferidoConExito = true
                    CxfRegistroDiagnostico.log(this, "ContentResolver escribió directamente en $destinationUri")
                }
            } catch (e: Exception) {
                CxfRegistroDiagnostico.log(this, "Fallo en escritura directa por ContentResolver: ${e.message}", e)
            }
        }

        if (transferidoConExito) {
            CxfRegistroDiagnostico.log(this, "Transferencia completada exitosamente hacia $nombreGestor")
            mostrarToast("Bóveda Local: Credenciales enviadas a $nombreGestor")
            resultIntent.data = destinationUri
            resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            setResult(Activity.RESULT_OK, resultIntent)
        } else {
            val msgFallo = "No se pudieron escribir las credenciales en la URI de transferencia"
            CxfRegistroDiagnostico.log(this, msgFallo)
            mostrarToast("Bóveda Local: Error al entregar credenciales")
            IntentHandler.setImportCredentialsException(
                resultIntent,
                ImportCredentialsSystemErrorException(msgFallo)
            )
            setResult(Activity.RESULT_CANCELED, resultIntent)
        }

        finish()
    }

    private fun mostrarToast(mensaje: String) {
        Handler(Looper.getMainLooper()).post {
            try {
                Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
            } catch (_: Exception) {}
        }
    }
}
