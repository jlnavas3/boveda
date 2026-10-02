package com.jlnavas3.bovedalocal.cxf

import androidx.core.os.OutcomeReceiverCompat
import androidx.credentials.provider.CallingAppInfo
import androidx.credentials.providerevents.exception.ExportCredentialsException
import androidx.credentials.providerevents.exception.ExportCredentialsSystemErrorException
import androidx.credentials.providerevents.exception.GetCredentialTransferCapabilitiesException
import androidx.credentials.providerevents.exception.GetCredentialTransferCapabilitiesSystemErrorException
import androidx.credentials.providerevents.exception.ImportCredentialsException
import androidx.credentials.providerevents.exception.ImportCredentialsSystemErrorException
import androidx.credentials.providerevents.service.DeviceSetupService
import androidx.credentials.providerevents.transfer.CredentialTransferCapabilities
import androidx.credentials.providerevents.transfer.CredentialTransferCapabilitiesRequest
import androidx.credentials.providerevents.transfer.CredentialTypes
import androidx.credentials.providerevents.transfer.ExportCredentialsRequest
import androidx.credentials.providerevents.transfer.ExportCredentialsResponse
import androidx.credentials.providerevents.transfer.ImportCredentialsRequest
import androidx.credentials.providerevents.transfer.ImportCredentialsResponse
import androidx.credentials.providerevents.transfer.PerTypeExportResult
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Servicio del sistema Android para atender solicitudes de transferencia directa de credenciales
 * (rol de Exporter) mediante las Credential Transfer APIs oficiales sin requerir conexión a internet.
 */
class BovedaDeviceSetupService : DeviceSetupService() {

    private val scope = CoroutineScope(Dispatchers.IO)

    private fun obtenerEntradas(): List<Entrada> {
        val repo = VaultRepository.obtener(applicationContext)
        return repo.entradas().filter { it.eliminadaEn == 0L }
    }

    override fun onGetCredentialTransferCapabilities(
        request: CredentialTransferCapabilitiesRequest,
        callingAppInfo: CallingAppInfo,
        callback: OutcomeReceiverCompat<CredentialTransferCapabilities, GetCredentialTransferCapabilitiesException>
    ) {
        Diagnostico.apuntar("cxf_service", "Solicitud de capacidades de transferencia recibida desde ${callingAppInfo.packageName}")
        scope.launch {
            try {
                val entradas = obtenerEntradas()
                val jsonPayload = CxfGestorTransferencia.obtenerJsonExportacion()
                    ?: CxfExportador.exportarAJson(entradas)

                val numPasswords = if (entradas.isNotEmpty()) {
                    entradas.count { it.contrasena.isNotBlank() }
                } else {
                    CxfGestorTransferencia.numPasswordsActivas
                }

                val numPasskeys = if (entradas.isNotEmpty()) {
                    entradas.count { it.passkey != null }
                } else {
                    CxfGestorTransferencia.numPasskeysActivas
                }

                val totalNum = if (entradas.isNotEmpty()) {
                    entradas.size
                } else {
                    CxfGestorTransferencia.totalEntradasActivas.takeIf { it > 0 } ?: 1
                }

                val jsonSize = jsonPayload.toByteArray(Charsets.UTF_8).size.toLong()

                val capabilities = CredentialTransferCapabilities(
                    totalNumCredentials = totalNum,
                    numPasswords = numPasswords,
                    numPublicKeyCredentials = numPasskeys,
                    totalSizeBytes = jsonSize
                )
                callback.onResult(capabilities)
            } catch (e: Exception) {
                Diagnostico.apuntar("cxf_service", "Error al obtener capacidades de transferencia", e)
                callback.onError(GetCredentialTransferCapabilitiesSystemErrorException("Error al consultar capacidades: ${e.message}"))
            }
        }
    }

    override fun onExportCredentialsRequest(
        request: ExportCredentialsRequest,
        callingAppInfo: CallingAppInfo,
        callback: OutcomeReceiverCompat<ExportCredentialsResponse, ExportCredentialsException>
    ) {
        Diagnostico.apuntar("cxf_service", "Solicitud de exportación entrante (recepción) desde ${callingAppInfo.packageName}")
        scope.launch {
            try {
                val jsonPayload = request.credentialsJson
                val resultado = if (jsonPayload.isNotBlank()) {
                    val res = CxfConvertidor.convertir(jsonPayload)
                    val repo = VaultRepository.obtener(applicationContext)
                    res.entradas.forEach { entrada ->
                        repo.guardarEntrada(entrada)
                    }
                    res
                } else null

                val numPasswords = resultado?.totalContrasenas ?: 0
                val numPasskeys = resultado?.totalPasskeys ?: 0
                val numTotp = resultado?.totalTotp ?: 0
                val numNotas = resultado?.entradas?.count { it.tipo == TipoEntrada.NOTA } ?: 0

                val resultsMap = mutableMapOf<String, PerTypeExportResult>()
                if (numPasskeys > 0) {
                    resultsMap[CredentialTypes.CREDENTIAL_TYPE_PUBLIC_KEY] =
                        PerTypeExportResult(CredentialTypes.CREDENTIAL_TYPE_PUBLIC_KEY, numPasskeys, 0, 0)
                }
                if (numPasswords > 0) {
                    resultsMap[CredentialTypes.CREDENTIAL_TYPE_BASIC_AUTH] =
                        PerTypeExportResult(CredentialTypes.CREDENTIAL_TYPE_BASIC_AUTH, numPasswords, 0, 0)
                }
                if (numTotp > 0) {
                    resultsMap[CredentialTypes.CREDENTIAL_TYPE_TOTP] =
                        PerTypeExportResult(CredentialTypes.CREDENTIAL_TYPE_TOTP, numTotp, 0, 0)
                }
                if (numNotas > 0) {
                    resultsMap[CredentialTypes.CREDENTIAL_TYPE_NOTE] =
                        PerTypeExportResult(CredentialTypes.CREDENTIAL_TYPE_NOTE, numNotas, 0, 0)
                }

                Diagnostico.apuntar(
                    "cxf_service",
                    "Credenciales recibidas y procesadas: $numPasskeys passkeys, $numPasswords contraseñas, $numTotp TOTP"
                )
                callback.onResult(ExportCredentialsResponse(resultsMap))
            } catch (e: Exception) {
                Diagnostico.apuntar("cxf_service", "Error al procesar exportación entrante", e)
                callback.onError(ExportCredentialsSystemErrorException("Fallo en la exportación: ${e.message}"))
            }
        }
    }

    override fun onImportCredentialsRequest(
        request: ImportCredentialsRequest,
        callingAppInfo: CallingAppInfo,
        callback: OutcomeReceiverCompat<ImportCredentialsResponse, ImportCredentialsException>
    ) {
        Diagnostico.apuntar("cxf_service", "Solicitud de importación entrante desde ${callingAppInfo.packageName}")
        scope.launch {
            try {
                val jsonPayload = CxfGestorTransferencia.obtenerJsonExportacion()
                    ?: CxfExportador.exportarAJson(obtenerEntradas())

                if (jsonPayload.isBlank()) {
                    Diagnostico.apuntar("cxf_service", "Error: No hay credenciales disponibles para exportar")
                    callback.onError(ImportCredentialsSystemErrorException("No hay credenciales disponibles o la bóveda está bloqueada."))
                    return@launch
                }

                Diagnostico.apuntar(
                    "cxf_service",
                    "Exportación directa completada exitosamente hacia ${callingAppInfo.packageName} (${jsonPayload.length} caracteres)"
                )
                callback.onResult(ImportCredentialsResponse(jsonPayload))
            } catch (e: Exception) {
                Diagnostico.apuntar("cxf_service", "Error al procesar solicitud de importación", e)
                callback.onError(ImportCredentialsSystemErrorException("Error al transferir credenciales: ${e.message}"))
            }
        }
    }
}
