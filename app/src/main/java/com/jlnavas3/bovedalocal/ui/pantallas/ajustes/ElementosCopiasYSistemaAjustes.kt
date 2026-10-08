package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Define los elementos de menú del Hub de Ajustes para Copias de seguridad y Sistema.
 */
fun crearElementosCopiasYSistemaAjustes(
    vm: VaultViewModel
): Pair<List<ElementoMenuAjustes>, List<ElementoMenuAjustes>> {
    val copias = listOf(
        ElementoMenuAjustes(
            titulo = "Copia de seguridad",
            subtitulo = "Exportar, restaurar y copias automáticas",
            icono = Icons.Filled.Backup,
            colorIcono = Color(0xFF43A047),
            idEtiqueta = "05-COP-MAN",
            grupo = "Copias y datos",
            palabrasClave = "copia seguridad backup exportar importar restaurar auto automatica",
            alPulsar = { vm.ir(Pantalla.CopiaSeguridad()) }
        ),
        ElementoMenuAjustes(
            titulo = "Importar de Google",
            subtitulo = "Importar archivo CSV de Google Passwords",
            icono = Icons.Filled.FileDownload,
            colorIcono = Color(0xFF0288D1),
            idEtiqueta = "05-COP-CSV",
            grupo = "Copias y datos",
            palabrasClave = "google passwords csv importar chrome navegador",
            alPulsar = { vm.ir(Pantalla.CsvGoogle()) }
        ),
        ElementoMenuAjustes(
            titulo = "Kit de emergencia",
            subtitulo = "Ficha física imprimible con claves maestras",
            icono = Icons.Filled.Description,
            colorIcono = Color(0xFFFFB300),
            idEtiqueta = "05-COP-KIT",
            grupo = "Copias y datos",
            palabrasClave = "kit emergencia papel pdf imprimir hoja rescate",
            alPulsar = { vm.ir(Pantalla.KitEmergencia()) }
        )
    )

    val sistema = listOf(
        ElementoMenuAjustes(
            titulo = "Opciones avanzadas",
            subtitulo = "Respuesta táctil, desarrollo y peligro",
            icono = Icons.Filled.Tune,
            colorIcono = Color(0xFFC2185B),
            idEtiqueta = "06-SIS-AVZ",
            grupo = "Sistema",
            palabrasClave = "avanzada maestra clave cambiar borrar ids desarrollo haptica vibracion",
            alPulsar = { vm.ir(Pantalla.Avanzada()) }
        ),
        ElementoMenuAjustes(
            titulo = "Registro de eventos",
            subtitulo = "Auditoría de acciones, accesos y seguridad",
            icono = Icons.Filled.History,
            colorIcono = Color(0xFF00897B),
            idEtiqueta = "06-SIS-LOG",
            grupo = "Sistema",
            palabrasClave = "registro eventos logs historial auditoria fallos accesos",
            alPulsar = { vm.ir(Pantalla.Registro()) }
        ),
        ElementoMenuAjustes(
            titulo = "Diagnóstico de seguridad",
            subtitulo = "Auditoría de integridad, versión y licencias",
            icono = Icons.Filled.Security,
            colorIcono = Color(0xFF607D8B),
            idEtiqueta = "06-SIS-DGN",
            grupo = "Sistema",
            palabrasClave = "diagnostico seguridad acerca de version info auditoria integridad",
            alPulsar = { vm.ir(Pantalla.AcercaDe()) }
        )
    )

    return Pair(copias, sistema)
}
