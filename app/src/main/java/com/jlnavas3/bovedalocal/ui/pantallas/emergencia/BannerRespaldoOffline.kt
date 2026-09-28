package com.jlnavas3.bovedalocal.ui.pantallas.emergencia

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun BannerRespaldoOffline(
    mostrarIdsAjustes: Boolean,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Copia física de seguridad",
        icono = Icons.Filled.Shield,
        colorIcono = ColorSeguridad,
        idGrupo = "05-COP-KIT-G01",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Respaldo 100% offline sin servidores ni telemetría",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Genera un documento que puedes imprimir en papel o guardar en PDF localmente. Bóveda Local opera 100% offline sin servidores en la nube ni telemetría.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp
            )
        }
    }
}
