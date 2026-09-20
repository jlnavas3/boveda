package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionHistorialClaves(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica,
    inicialmenteAbierta: Boolean = false,
    seccionDestino: String? = null
) {
    TarjetaAjuste(
        titulo = "Historial de contraseñas generadas",
        icono = Icons.Filled.History,
        descripcion = "Control de retención temporal, auto-destrucción y acceso al registro.",
        colorIcono = ColorGenerador,
        inicialmenteAbierta = inicialmenteAbierta || (seccionDestino != null && seccionDestino.startsWith("04")),
        idEtiqueta = "04",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        Spacer(Modifier.height(8.dp))
        
        // Acceso directo a la pantalla de historial
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(FormaCampo)
                .clickable {
                    haptica.tic()
                    vm.irPorId("04")
                }
                .padding(vertical = 10.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.History,
                contentDescription = null,
                tint = ColorAcento,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Abrir pantalla del historial",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextoPrincipal
                )
                Text(
                    "${ajustes.historialClaves.size} contraseñas almacenadas actualmente",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.height(14.dp))

        // Selector de cantidad máxima
        val seleccionadoMaxTexto = AlmacenAjustes.OPCIONES_HISTORIAL_MAX
            .find { it.first == ajustes.historialClavesMax }?.second
            ?: "${ajustes.historialClavesMax} contraseñas"

        SelectorAjuste(
            titulo = "Contraseñas a retener",
            icono = Icons.Filled.Key,
            seleccionado = seleccionadoMaxTexto,
            opciones = AlmacenAjustes.OPCIONES_HISTORIAL_MAX.map { (cantidad, etiqueta) ->
                OpcionAjuste(
                    valor = cantidad.toString(),
                    texto = etiqueta,
                    icono = Icons.Filled.Key
                )
            },
            alSeleccionar = { valor ->
                haptica.tic()
                vm.ajustarHistorialClavesMax(valor.toInt())
            }
        )

        Spacer(Modifier.height(14.dp))

        // Switch de vaciado automático
        FilaAjuste(
            titulo = "Vaciado automático",
            descripcion = "Destruye automáticamente las claves tras cumplirse el tiempo límite",
            activo = ajustes.historialClavesVaciadoAuto,
            alCambiar = { activo ->
                haptica.toque()
                vm.ajustarHistorialClavesVaciadoAuto(activo)
            }
        )

        if (ajustes.historialClavesVaciadoAuto) {
            Spacer(Modifier.height(14.dp))

            val seleccionadoTiempoTexto = AlmacenAjustes.OPCIONES_AUTODESTRUCCION_HISTORIAL
                .find { it.first == ajustes.historialClavesTiempoAutoDestruccion }?.second
                ?: "30 minutos"

            val opcionesTiempo = AlmacenAjustes.OPCIONES_AUTODESTRUCCION_HISTORIAL.map { (millis, etiqueta) ->
                val icono = when {
                    millis <= 10 * 60 * 1000L -> Icons.Filled.Timer
                    millis <= 30 * 60 * 1000L -> Icons.Filled.HourglassBottom
                    millis <= 3 * 60 * 60 * 1000L -> Icons.Filled.Schedule
                    millis == 24 * 60 * 60 * 1000L -> Icons.Filled.Today
                    millis <= 3 * 24 * 60 * 60 * 1000L -> Icons.Filled.DateRange
                    else -> Icons.Filled.CalendarMonth
                }
                OpcionAjuste(
                    valor = millis.toString(),
                    texto = etiqueta,
                    icono = icono
                )
            }

            SelectorAjuste(
                titulo = "Tiempo de autodestrucción",
                icono = Icons.Filled.Timer,
                seleccionado = seleccionadoTiempoTexto,
                opciones = opcionesTiempo,
                alSeleccionar = { valor ->
                    haptica.tic()
                    vm.ajustarHistorialClavesTiempoAutoDestruccion(valor.toLong())
                }
            )
        }

        Spacer(Modifier.height(8.dp))
        BotonRestablecerItem {
            haptica.toque()
            vm.ajustarHistorialClavesMax(15)
            vm.ajustarHistorialClavesVaciadoAuto(true)
            vm.ajustarHistorialClavesTiempoAutoDestruccion(30 * 60 * 1000L)
        }
    }
}
