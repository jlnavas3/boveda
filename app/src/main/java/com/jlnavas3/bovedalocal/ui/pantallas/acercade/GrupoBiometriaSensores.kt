package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.DatosAuditoria

@Composable
fun GrupoBiometriaSensores(
    datos: DatosAuditoria,
    modifier: Modifier = Modifier,
    mostrarIdsAjustes: Boolean = false
) {
    ComponenteGrupo(
        etiqueta = "Biometría y sensores",
        icono = Icons.Filled.Fingerprint,
        colorIcono = ColorSeguridad,
        idGrupo = "06-SIS-DGN-G05",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            datos.lineasBiometria.forEachIndexed { idx, linea ->
                if (linea.ok != null) {
                    FilaAuditoria(
                        ok = linea.ok,
                        titulo = linea.texto,
                        detalle = linea.detalle ?: if (linea.ok) "Verificado y soportado" else "No disponible",
                        indentada = linea.indentada
                    )
                } else {
                    Text(
                        text = linea.texto,
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                if (idx < datos.lineasBiometria.lastIndex) {
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}
