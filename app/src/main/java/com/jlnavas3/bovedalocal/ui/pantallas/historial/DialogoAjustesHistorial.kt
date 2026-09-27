package com.jlnavas3.bovedalocal.ui.pantallas.historial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoAjustesHistorial(
    ajustes: AjustesApp,
    alCerrar: () -> Unit,
    alCambiarMax: (Int) -> Unit,
    alCambiarVaciadoAuto: (Boolean) -> Unit,
    alCambiarTiempoAutoDestruccion: (Long) -> Unit,
    alRestablecer: () -> Unit
) {
    val tiempoSeleccionado = AlmacenAjustes.OPCIONES_AUTODESTRUCCION_HISTORIAL
        .find { it.first == ajustes.historialClavesTiempoAutoDestruccion }?.second ?: "30 minutos"

    val maxSeleccionado = AlmacenAjustes.OPCIONES_HISTORIAL_MAX
        .find { it.first == ajustes.historialClavesMax }?.second ?: "${ajustes.historialClavesMax} claves"

    AlertDialog(
        onDismissRequest = alCerrar,
        shape = FormaTarjeta,
        containerColor = Superficie,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = ColorGenerador,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = "Ajustes del historial",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextoPrincipal,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = alRestablecer) {
                    Icon(
                        imageVector = Icons.Filled.RestartAlt,
                        contentDescription = "Restablecer valores",
                        tint = TextoSecundario
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                ComponenteSelectorModal(
                    titulo = "Contraseñas a retener",
                    valorSeleccionado = ajustes.historialClavesMax,
                    opciones = AlmacenAjustes.OPCIONES_HISTORIAL_MAX.map { (cant, label) ->
                        OpcionSelectorModal(
                            valor = cant,
                            etiquetaFila = label,
                            etiquetaModal = label,
                            descripcionModal = "Almacena como máximo $label en el historial"
                        )
                    },
                    alSeleccionar = { alCambiarMax(it) }
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Vaciado automático",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoPrincipal
                        )
                        Text(
                            text = "Destruye las contraseñas al expirar su tiempo",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoSecundario
                        )
                    }
                    Switch(
                        checked = ajustes.historialClavesVaciadoAuto,
                        onCheckedChange = alCambiarVaciadoAuto,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ColorAcento,
                            checkedTrackColor = ColorAcento.copy(alpha = 0.35f)
                        )
                    )
                }

                if (ajustes.historialClavesVaciadoAuto) {
                    Spacer(Modifier.height(10.dp))
                    ComponenteSelectorModal(
                        titulo = "Tiempo de autodestrucción",
                        valorSeleccionado = ajustes.historialClavesTiempoAutoDestruccion,
                        opciones = AlmacenAjustes.OPCIONES_AUTODESTRUCCION_HISTORIAL.map { (ms, label) ->
                            OpcionSelectorModal(
                                valor = ms,
                                etiquetaFila = label,
                                etiquetaModal = label,
                                descripcionModal = "Las claves expiran tras $label de generarse"
                            )
                        },
                        alSeleccionar = { alCambiarTiempoAutoDestruccion(it) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = alCerrar) {
                Text("Listo", color = ColorAcento, fontWeight = FontWeight.Bold)
            }
        }
    )
}
