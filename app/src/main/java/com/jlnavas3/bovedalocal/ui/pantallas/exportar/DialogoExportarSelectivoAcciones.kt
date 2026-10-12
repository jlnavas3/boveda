/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Gestiona el diálogo de contraseña y la invocación del guardador o lanzador del sistema SAF.
 */
@Composable
fun DialogoExportarSelectivoAcciones(
    dialogoVisible: Boolean,
    cantidadSeleccionadas: Int,
    idsSeleccionados: Set<String>,
    ajustes: AjustesApp,
    contexto: Context,
    vm: VaultViewModel,
    lanzadorGuardarBvda: ActivityResultLauncher<String>,
    alDescartar: () -> Unit,
    alEstablecerPasswordAUsar: (String) -> Unit
) {
    if (!dialogoVisible) return

    DialogoClaveExportarSelectivo(
        cantidad = cantidadSeleccionadas,
        autoPassword = ajustes.backupAutoPasswordCifrado,
        alDescartar = alDescartar,
        alConfirmar = { nombreArchivoResolved, clavePass, directoAuto ->
            alDescartar()
            alEstablecerPasswordAUsar(clavePass)
            if (directoAuto) {
                ejecutarExportacionAuto(
                    contexto = contexto,
                    vm = vm,
                    ids = idsSeleccionados,
                    password = clavePass,
                    nombreBruto = nombreArchivoResolved
                )
            } else {
                BovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorGuardarBvda.launch(nombreArchivoResolved)
                } catch (e: Exception) {
                    BovedaApp.salidaTerminada(contexto)
                    vm.avisar("No se encontró ningún selector de archivos para guardar")
                }
            }
        }
    )
}
