package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import android.content.Context
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Orquesta la exportación selectiva automática: cifra las entradas seleccionadas con la clave
 * indicada y las persiste directamente en el almacenamiento.
 */
fun ejecutarExportacionAuto(
    contexto: Context,
    vm: VaultViewModel,
    ids: Set<String>,
    password: String,
    nombreBruto: String
) {
    val nombreArchivo = sanearNombreArchivoBackup(nombreBruto)
    vm.exportarSelectivo(ids, password) { bytes ->
        val exito = guardarBackupEnAlmacenamiento(contexto, nombreArchivo, bytes)
        if (exito) {
            vm.avisar("Guardado en Descargas/BovedaLocal/Backups/$nombreArchivo")
        } else {
            vm.avisar("No se pudo guardar automáticamente. Usa el explorador de archivos.")
        }
    }
}
