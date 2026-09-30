package com.jlnavas3.bovedalocal.ui.pantallas.historial

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun DialogoAjustesHistorial(
    ajustes: AjustesApp,
    alCerrar: () -> Unit,
    alCambiarMax: (Int) -> Unit,
    alCambiarVaciadoAuto: (Boolean) -> Unit,
    alCambiarTiempoAutoDestruccion: (Long) -> Unit,
    alRestablecer: () -> Unit
) {
    val contexto = LocalContext.current

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Ajustes del historial",
        icono = Icons.Filled.History,
        colorIcono = ColorGenerador,
        botonConfirmar = {
            TextButton(onClick = alCerrar) {
                Text("Listo", color = ColorAcento, fontWeight = FontWeight.Bold)
            }
        },
        botonDescartar = {
            IconButton(onClick = {
                Haptica(contexto).tic()
                alRestablecer()
                Toast.makeText(contexto, "Ajustes del historial restablecidos", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Filled.RestartAlt,
                    contentDescription = "Restablecer valores",
                    tint = TextoSecundario
                )
            }
        }
    ) {
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
                SwitchBoveda(
                    checked = ajustes.historialClavesVaciadoAuto,
                    onCheckedChange = alCambiarVaciadoAuto
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
    }
}
