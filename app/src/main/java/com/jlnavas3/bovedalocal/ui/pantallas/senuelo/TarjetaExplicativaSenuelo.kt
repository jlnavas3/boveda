package com.jlnavas3.bovedalocal.ui.pantallas.senuelo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
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
fun TarjetaExplicativaSenuelo(
    mostrarIdsAjustes: Boolean,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Información",
        icono = Icons.Filled.Security,
        colorIcono = ColorSeguridad,
        idGrupo = "01-SEG-SEN-G01",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Si alguien te obliga a desbloquear la app, introduce tu PIN de coacción en la pantalla principal. Bóveda Local se abrirá normalmente pero mostrará solo cuentas señuelo inofensivas. Tu bóveda real permanece 100% cifrada e inaccesible.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp
            )
        }
    }
}
