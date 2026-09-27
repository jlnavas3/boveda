package com.jlnavas3.bovedalocal.ui.pantallas.copia

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun GrupoRecordatorioRespaldo(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica
) {
    ComponenteGrupo(
        etiqueta = "Recordatorio de respaldo",
        idGrupo = "02.1.G3",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Aviso periódico en la bóveda si pasa mucho tiempo sin exportar"
    ) {
        val opcionesRecordatorio = remember {
            AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION.map { (dias, etiqueta) ->
                val desc = when (dias) {
                    0 -> "No mostrar avisos por falta de respaldo"
                    -30 -> "Modo de prueba rápido (notifica cada media hora)"
                    7 -> "Aviso semanal para respaldos frecuentes"
                    30 -> "Frecuencia recomendada para copias manuales"
                    60 -> "Aviso bimestral si la bóveda no cambia a menudo"
                    90 -> "Aviso trimestral para mínimo mantenimiento"
                    else -> "Recordatorio periódico en la pantalla principal"
                }
                val icono = when (dias) {
                    0 -> Icons.Filled.Block
                    -30 -> Icons.Filled.Today
                    7 -> Icons.Filled.DateRange
                    30 -> Icons.Filled.CalendarToday
                    60 -> Icons.Filled.CalendarMonth
                    else -> Icons.Filled.EventRepeat
                }
                OpcionSelectorModal(dias, etiqueta, etiqueta, desc, icono)
            }
        }

        ComponenteSelectorModal(
            titulo = "Frecuencia del recordatorio",
            descripcionModal = "Periodicidad con la que se avisa en la bóveda si no se ha exportado",
            icono = Icons.Filled.EventRepeat,
            colorIcono = ColorExportacion,
            idFila = "02.1.5",
            mostrarId = ajustes.mostrarIdsAjustes,
            valorSeleccionado = ajustes.recordatorioExportacionDias,
            opciones = opcionesRecordatorio,
            alSeleccionar = { dias ->
                haptica.tic()
                vm.ajustarRecordatorioExportacion(dias)
            }
        )

        ComponenteSeparador()

        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                haptica.toque()
                vm.ajustarRecordatorioExportacion(30)
            }
        )
    }
}
