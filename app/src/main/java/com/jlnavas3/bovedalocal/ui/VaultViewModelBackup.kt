package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.crypto.Zeroizar
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

interface VaultBackupDelegate {
    val repositorio: VaultRepository
    val avisoInterno: MutableStateFlow<String?>
    val errorInterno: MutableStateFlow<String?>
    fun ejecutar(bloque: suspend () -> Unit)

    fun exportar(password: String, escritor: (ByteArray) -> Unit) {
        ejecutar {
            val chars = password.toCharArray()
            try {
                val datos = withContext(Dispatchers.Default) { repositorio.exportar(chars) }
                withContext(Dispatchers.IO) { escritor(datos) }
                repositorio.ajustes.actualizar { it.copy(ultimaExportacionEn = System.currentTimeMillis()) }
                Diagnostico.apuntar("bóveda", "Copia de seguridad cifrada exportada correctamente")
                avisoInterno.value = "Bóveda exportada y cifrada"
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al exportar copia de seguridad: ${e.message ?: "error desconocido"}", e)
                errorInterno.value = "No se pudo exportar: ${e.message ?: "error desconocido"}"
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun importar(password: String, lector: () -> ByteArray) {
        ejecutar {
            val chars = password.toCharArray()
            try {
                val datos = withContext(Dispatchers.IO) { lector() }
                val nuevas = withContext(Dispatchers.Default) { repositorio.importar(datos, chars) }
                Diagnostico.apuntar("bóveda", "Bóveda importada desde copia cifrada ($nuevas entradas incorporadas)")
                avisoInterno.value = "Importadas $nuevas entradas"
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al importar archivo cifrado: contraseña incorrecta o archivo inválido", e)
                errorInterno.value = "No se pudo importar: contraseña incorrecta o archivo inválido"
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun importarCsv(lector: () -> ByteArray, onResultado: ((Int) -> Unit)? = null) {
        ejecutar {
            try {
                val datos = withContext(Dispatchers.IO) { lector() }
                val nuevas = withContext(Dispatchers.Default) { repositorio.importarCsv(datos) }
                Diagnostico.apuntar("bóveda", "Importación CSV completada con éxito ($nuevas entradas incorporadas)")
                avisoInterno.value = "Importadas $nuevas entradas desde CSV"
                onResultado?.invoke(nuevas)
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al importar archivo CSV: ${e.message ?: "formato no reconocido"}", e)
                errorInterno.value = "No se pudo importar el CSV: ${e.message ?: "formato no reconocido"}"
            }
        }
    }

    fun registrarCsvGoogleImportado(ruta: String, uri: String, cuentas: Int) {
        repositorio.ajustes.actualizar {
            it.copy(
                csvGoogleRuta = ruta,
                csvGoogleUri = uri,
                csvGoogleCuentas = cuentas,
                csvGoogleEliminado = false
            )
        }
    }

    fun marcarCsvGoogleEliminado(eliminado: Boolean) {
        repositorio.ajustes.actualizar {
            it.copy(csvGoogleEliminado = eliminado)
        }
        if (eliminado) {
            Diagnostico.apuntar("seguridad", "Archivo CSV de Google eliminado de forma segura del almacenamiento")
        }
    }

    fun descartarAvisoCsvGoogle() {
        repositorio.ajustes.actualizar {
            it.copy(
                csvGoogleRuta = "",
                csvGoogleUri = "",
                csvGoogleCuentas = 0,
                csvGoogleEliminado = false
            )
        }
    }

    fun cambiarContrasenaMaestra(actual: String, nueva: String) {
        ejecutar {
            val viejaChars = actual.toCharArray()
            val nuevaChars = nueva.toCharArray()
            try {
                val correcta = withContext(Dispatchers.Default) { repositorio.verificarContrasena(viejaChars) }
                if (!correcta) {
                    Diagnostico.apuntar("bóveda", "Intento de cambio de contraseña maestra rechazado (contraseña actual incorrecta)")
                    errorInterno.value = "La contraseña actual no es correcta"
                    return@ejecutar
                }
                withContext(Dispatchers.Default) { repositorio.cambiarContrasenaMaestra(nuevaChars) }
                Diagnostico.apuntar("bóveda", "Contraseña maestra de la bóveda modificada exitosamente")
                avisoInterno.value = "Contraseña maestra cambiada. Vuelve a activar la biometría."
            } finally {
                Zeroizar.borrar(viejaChars)
                Zeroizar.borrar(nuevaChars)
            }
        }
    }
}
