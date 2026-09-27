package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.util.InformeDiagnostico

@Composable
fun GrupoMemoriaAlmacenamiento(
    datos: InformeDiagnostico.DatosAuditoria,
    modifier: Modifier = Modifier
) {
    GrupoAjustes(
        etiqueta = "Memoria y almacenamiento local",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ItemMetrica("Memoria RAM", "${datos.ramLibreMb} MB libres de ${datos.ramTotalMb} MB (Estado crítico: ${if (datos.ramBaja) "Sí" else "No"})")
            ItemMetrica("Heap JVM", "${datos.heapUsadoMb} MB en uso de ${datos.heapMaxMb} MB límite")
            ItemMetrica("Almacenamiento interno", "${datos.almacenamientoLibreMb} MB libres de ${datos.almacenamientoTotalMb} MB")
            ItemMetrica("Ruta de la bóveda", datos.rutaBoveda)
            ItemMetrica("Tamaño de archivo", "${datos.tamanoBovedaBytes} bytes")
            ItemMetrica("Integridad de escritura", "Atomic Rename + fsync() a prueba de fallos de energía")
        }
    }
}
