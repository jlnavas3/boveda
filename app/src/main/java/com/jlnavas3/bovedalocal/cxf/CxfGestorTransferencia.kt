package com.jlnavas3.bovedalocal.cxf

import android.app.Activity
import androidx.credentials.providerevents.ProviderEventsManager
import androidx.credentials.providerevents.exception.ImportCredentialsCancellationException
import androidx.credentials.providerevents.exception.ImportCredentialsNoExportOptionException
import androidx.credentials.providerevents.transfer.CredentialTypes
import androidx.credentials.providerevents.transfer.ImportCredentialsRequest
import com.jlnavas3.bovedalocal.util.Diagnostico
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface ResultadoImportacionCxf {
    data class Exito(val jsonPayload: String, val resultado: ResultadoConversionCxf) : ResultadoImportacionCxf
    object Cancelado : ResultadoImportacionCxf
    data class SinOpciones(val mensaje: String) : ResultadoImportacionCxf
    data class Error(val mensaje: String, val excepcion: Throwable?) : ResultadoImportacionCxf
}

sealed interface ResultadoRegistroExportacion {
    data class Exito(val totalRegistradas: Int) : ResultadoRegistroExportacion
    data class Error(val mensaje: String, val excepcion: Throwable?) : ResultadoRegistroExportacion
}

sealed interface ResultadoLimpiezaExportacion {
    object Exito : ResultadoLimpiezaExportacion
    data class Error(val mensaje: String, val excepcion: Throwable?) : ResultadoLimpiezaExportacion
}

/**
 * Gestor del flujo de transferencia de credenciales (rol Importer y Exporter) utilizando
 * la API oficial de Android (androidx.credentials.providerevents).
 *
 * Comunica de forma local y segura entre proveedores instalados sin acceder a ninguna red.
 */
object CxfGestorTransferencia {

    suspend fun importarCredenciales(activity: Activity): ResultadoImportacionCxf {
        return try {
            val manager = ProviderEventsManager.create(activity)
            val request = ImportCredentialsRequest(
                credentialTypes = setOf(
                    CredentialTypes.CREDENTIAL_TYPE_PUBLIC_KEY,
                    CredentialTypes.CREDENTIAL_TYPE_BASIC_AUTH,
                    CredentialTypes.CREDENTIAL_TYPE_TOTP,
                    CredentialTypes.CREDENTIAL_TYPE_NOTE
                ),
                knownExtensions = emptySet()
            )

            Diagnostico.apuntar("cxf", "Iniciando transferencia directa de credenciales del sistema...")
            val response = manager.importCredentials(activity, request)
            val jsonString = response.response.responseJson

            if (jsonString.isBlank()) {
                ResultadoImportacionCxf.Error("El proveedor de contraseñas devolvió una respuesta vacía", null)
            } else {
                val resultado = CxfConvertidor.convertir(jsonString)
                Diagnostico.apuntar(
                    "cxf",
                    "Transferencia recibida: ${resultado.entradas.size} credenciales " +
                        "(${resultado.totalPasskeys} llaves de paso, ${resultado.totalContrasenas} contraseñas, " +
                        "${resultado.totalTotp} verificación en dos pasos) de ${resultado.exportador ?: "proveedor externo"}"
                )
                ResultadoImportacionCxf.Exito(jsonString, resultado)
            }
        } catch (_: ImportCredentialsCancellationException) {
            Diagnostico.apuntar("cxf", "Transferencia cancelada por el usuario")
            ResultadoImportacionCxf.Cancelado
        } catch (e: ImportCredentialsNoExportOptionException) {
            Diagnostico.apuntar("cxf", "No se encontraron gestores con credenciales exportables", e)
            ResultadoImportacionCxf.SinOpciones("No se encontraron gestores de contraseñas con credenciales listas para transferir en este dispositivo.")
        } catch (e: Exception) {
            Diagnostico.apuntar("cxf", "Fallo al importar credenciales: ${e.message ?: "error desconocido"}", e)
            ResultadoImportacionCxf.Error("Fallo en la transferencia de credenciales: ${e.message ?: "error desconocido"}", e)
        }
    }

    @Volatile
    private var jsonExportacionActiva: String? = null

    @Volatile
    var totalEntradasActivas: Int = 0
        private set

    @Volatile
    var numPasskeysActivas: Int = 0
        private set

    @Volatile
    var numPasswordsActivas: Int = 0
        private set

    @Volatile
    private var idsSeleccionadasParaTransferir: Set<String>? = null

    @Volatile
    private var esSeleccionPersonalizadaActiva: Boolean = false

    fun obtenerJsonExportacion(): String? = jsonExportacionActiva

    fun esSeleccionPersonalizada(context: android.content.Context): Boolean {
        return esSeleccionPersonalizadaActiva || CxfPersistenciaPayload.esSeleccionPersonalizada(context)
    }

    fun obtenerIdsSeleccionados(context: android.content.Context): Set<String>? {
        return idsSeleccionadasParaTransferir ?: CxfPersistenciaPayload.recuperarIdsSeleccionados(context)
    }

    fun limpiarMemoriaExportacion(context: android.content.Context? = null) {
        jsonExportacionActiva = null
        idsSeleccionadasParaTransferir = null
        esSeleccionPersonalizadaActiva = false
        totalEntradasActivas = 0
        numPasskeysActivas = 0
        numPasswordsActivas = 0
        context?.let { CxfPersistenciaPayload.limpiar(it) }
    }

    suspend fun registrarExportacion(
        context: android.content.Context,
        entradas: List<com.jlnavas3.bovedalocal.data.Entrada>,
        esSeleccionPersonalizada: Boolean = false
    ): ResultadoRegistroExportacion {
        return try {
            val activas = entradas.filter { it.eliminadaEn == 0L }
            if (activas.isEmpty()) {
                return ResultadoRegistroExportacion.Error("No hay credenciales activas para transferir", null)
            }

            // Generar y almacenar en memoria el documento FIDO CXF para entrega inmediata al servicio
            val json = CxfExportador.exportarAJson(activas)
            jsonExportacionActiva = json
            totalEntradasActivas = activas.size
            numPasskeysActivas = activas.count { it.passkey != null }
            numPasswordsActivas = activas.count { it.contrasena.isNotBlank() }

            val ids = activas.map { it.id }.toSet()
            idsSeleccionadasParaTransferir = ids
            esSeleccionPersonalizadaActiva = esSeleccionPersonalizada
            CxfPersistenciaPayload.guardarIdsSeleccionados(context, ids)
            CxfPersistenciaPayload.guardarEsSeleccionPersonalizada(context, esSeleccionPersonalizada)
            CxfPersistenciaPayload.guardarPayload(context, json)

            val manager = ProviderEventsManager.create(context)
            val icono = CxfGeneradorIcono.generar(context)

            val types = mutableSetOf<String>()
            if (activas.any { it.passkey != null }) types.add(CredentialTypes.CREDENTIAL_TYPE_PUBLIC_KEY)
            if (activas.any { it.contrasena.isNotBlank() }) types.add(CredentialTypes.CREDENTIAL_TYPE_BASIC_AUTH)
            if (activas.any { !it.secretoTotp.isNullOrBlank() }) types.add(CredentialTypes.CREDENTIAL_TYPE_TOTP)
            if (activas.any { it.tipo == com.jlnavas3.bovedalocal.data.TipoEntrada.NOTA && it.notas.isNotBlank() }) {
                types.add(CredentialTypes.CREDENTIAL_TYPE_NOTE)
            }
            if (types.isEmpty()) {
                types.add(CredentialTypes.CREDENTIAL_TYPE_BASIC_AUTH)
                types.add(CredentialTypes.CREDENTIAL_TYPE_PUBLIC_KEY)
            }

            val descripcionUsuario = if (esSeleccionPersonalizada) {
                "${activas.size} ${if (activas.size == 1) "credencial seleccionada" else "credenciales seleccionadas"}"
            } else {
                "Bóveda completa (${activas.size} credenciales)"
            }

            val exportEntries = listOf(
                androidx.credentials.providerevents.transfer.ExportEntry(
                    id = "boveda_local_principal",
                    accountDisplayName = "Bóveda Local",
                    userDisplayName = descripcionUsuario,
                    icon = icono,
                    supportedCredentialTypes = types
                )
            )

            Diagnostico.apuntar("cxf", "Registrando $descripcionUsuario para transferencia directa...")
            try {
                manager.clearExport(androidx.credentials.providerevents.transfer.ClearExportRequest())
            } catch (_: Exception) {
            }
            val request = androidx.credentials.providerevents.transfer.RegisterExportRequest.create(context, exportEntries)
            manager.registerExport(request)
            android.util.Log.i("BovedaCXF", "manager.registerExport completado exitosamente con GMS")
            Diagnostico.apuntar("cxf", "Exportación registrada exitosamente con el sistema Android")
            ResultadoRegistroExportacion.Exito(activas.size)
        } catch (e: Exception) {
            limpiarMemoriaExportacion(context)
            android.util.Log.e("BovedaCXF", "Fallo al registrar exportación con ProviderEventsManager", e)
            Diagnostico.apuntar("cxf", "Fallo al registrar exportación: ${e.message}", e)
            ResultadoRegistroExportacion.Error("No se pudo registrar la transferencia: ${e.message ?: "error desconocido"}", e)
        }
    }

    suspend fun limpiarExportacion(context: android.content.Context): ResultadoLimpiezaExportacion {
        limpiarMemoriaExportacion(context)
        return try {
            val manager = ProviderEventsManager.create(context)
            Diagnostico.apuntar("cxf", "Limpiando registro de exportación...")
            manager.clearExport(androidx.credentials.providerevents.transfer.ClearExportRequest())
            Diagnostico.apuntar("cxf", "Registro de exportación limpiado correctamente")
            ResultadoLimpiezaExportacion.Exito
        } catch (e: Exception) {
            Diagnostico.apuntar("cxf", "Fallo al limpiar exportación: ${e.message}", e)
            ResultadoLimpiezaExportacion.Error("No se pudo revocar la exportación: ${e.message ?: "error desconocido"}", e)
        }
    }
}
