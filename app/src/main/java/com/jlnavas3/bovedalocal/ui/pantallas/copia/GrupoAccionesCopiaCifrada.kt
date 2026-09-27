package com.jlnavas3.bovedalocal.ui.pantallas.copia

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion

@Composable
fun GrupoAccionesCopiaCifrada(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    contexto: Context,
    lanzadorAbrir: ActivityResultLauncher<Array<String>>,
    alSolicitarExportar: () -> Unit
) {
    ComponenteGrupo(
        etiqueta = "Copia cifrada (.bvda)",
        idGrupo = "02.1.G1",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Archivo seguro cifrado con Argon2id + ChaCha20-Poly1305"
    ) {
        ComponenteNavegacion(
            titulo = "Exportar bóveda cifrada",
            icono = Icons.Filled.FileDownload,
            colorIcono = ColorExportacion,
            valorTexto = ".bvda",
            idFila = "02.1.1",
            mostrarId = ajustes.mostrarIdsAjustes,
            alPulsar = alSolicitarExportar
        )
        ComponenteSeparador()
        ComponenteNavegacion(
            titulo = "Exportación selectiva",
            icono = Icons.Filled.FileDownload,
            colorIcono = ColorExportacion,
            valorTexto = ".bvda",
            idFila = "02.1.2",
            mostrarId = ajustes.mostrarIdsAjustes,
            alPulsar = { vm.ir(Pantalla.ExportarSelectivo("todos")) }
        )
        ComponenteSeparador()
        ComponenteNavegacion(
            titulo = "Importar copia de seguridad",
            icono = Icons.Filled.FileUpload,
            colorIcono = ColorExportacion,
            idFila = "02.1.3",
            mostrarId = ajustes.mostrarIdsAjustes,
            alPulsar = {
                BovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorAbrir.launch(arrayOf("*/*"))
                } catch (e: Exception) {
                    BovedaApp.salidaTerminada(contexto)
                    vm.avisar("No se encontró ningún selector de archivos disponible")
                }
            }
        )
    }
}
