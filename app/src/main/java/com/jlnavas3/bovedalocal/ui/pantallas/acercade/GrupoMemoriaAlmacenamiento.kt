package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.util.DatosAuditoria

@Composable
fun GrupoMemoriaAlmacenamiento(
    datos: DatosAuditoria,
    modifier: Modifier = Modifier,
    mostrarIdsAjustes: Boolean = false
) {
    ComponenteGrupo(
        etiqueta = "Memoria y almacenamiento local",
        icono = Icons.Filled.SdCard,
        colorIcono = Color(0xFF607D8B),
        idGrupo = "06.3.G4",
        mostrarId = mostrarIdsAjustes,
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
