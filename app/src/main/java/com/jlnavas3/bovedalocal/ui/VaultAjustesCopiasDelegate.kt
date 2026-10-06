package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico

/**
 * Sub-delegado especializado en copias de seguridad automáticas y parámetros de resguardo cifrado.
 */
interface VaultAjustesCopiasDelegate {
    val repositorio: VaultRepository

    fun restablecerAjustesBackup() {
        repositorio.ajustes.actualizar {
            it.copy(
                backupAutoFrecuenciaDias = AjustesDefaults.HistorialCopias.BACKUP_AUTO_FRECUENCIA_DIAS,
                backupAutoMaxCopias = AjustesDefaults.HistorialCopias.BACKUP_AUTO_MAX_COPIAS,
                backupAutoPatronNombre = AjustesDefaults.HistorialCopias.BACKUP_AUTO_PATRON_NOMBRE
            )
        }
    }

    fun ajustarBackupAutoFrecuenciaDias(dias: Int) {
        repositorio.ajustes.actualizar { it.copy(backupAutoFrecuenciaDias = dias) }
        val desc = if (dias == 0) "desactivado" else "cada $dias días"
        Diagnostico.apuntar("backup", "Frecuencia de backup automático establecida en $desc")
    }

    fun ajustarBackupAutoPassword(password: String) {
        repositorio.ajustes.actualizar { it.copy(backupAutoPasswordCifrado = password) }
    }

    fun ajustarBackupAutoPasswordCifrado(password: String) = ajustarBackupAutoPassword(password)

    fun ajustarBackupAutoMaxCopias(max: Int) {
        repositorio.ajustes.actualizar { it.copy(backupAutoMaxCopias = max) }
    }

    fun ajustarBackupAutoPatronNombre(patron: String) {
        repositorio.ajustes.actualizar { it.copy(backupAutoPatronNombre = patron) }
    }

    fun ajustarBackupAutoSecuencia(secuencia: Int) {
        repositorio.ajustes.actualizar { it.copy(backupAutoSecuencia = secuencia) }
    }

    fun ajustarDiasRetencionPapelera(dias: Int) {
        repositorio.ajustes.actualizar { it.copy(diasRetencionPapelera = dias) }
        val desc = if (dias <= 0) "sin expiración automática" else "$dias días"
        Diagnostico.apuntar("papelera", "Retención de papelera establecida en $desc")
    }

    fun ajustarMaxHistorialContrasenasPorEntrada(max: Int) {
        repositorio.ajustes.actualizar { it.copy(maxHistorialContrasenasPorEntrada = max) }
        val desc = if (max <= 0) "desactivado" else "$max versiones"
        Diagnostico.apuntar("historial", "Historial de contraseñas por entrada establecido en $desc")
    }

    fun restablecerAjustesRetencionEHistorial() {
        repositorio.ajustes.actualizar {
            it.copy(
                diasRetencionPapelera = AjustesDefaults.HistorialCopias.DIAS_RETENCION_PAPELERA,
                maxHistorialContrasenasPorEntrada = AjustesDefaults.HistorialCopias.MAX_HISTORIAL_CONTRASENAS_POR_ENTRADA
            )
        }
    }
}
