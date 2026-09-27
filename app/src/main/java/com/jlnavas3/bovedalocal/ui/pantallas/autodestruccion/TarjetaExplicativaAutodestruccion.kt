package com.jlnavas3.bovedalocal.ui.pantallas.autodestruccion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaExplicativaAutodestruccion(
    mostrarIdsAjustes: Boolean,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Información",
        icono = Icons.Filled.Info,
        colorIcono = Peligro,
        idGrupo = "01.4.G1",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "1. Ante una situación de coerción donde se te obligue a abrir la bóveda y no sea suficiente una Bóveda Señuelo, puedes introducir este PIN de emergencia especial.\n\n" +
                    "2. El sistema simulará procesar la solicitud pero inmediatamente sanitizará la memoria RAM (zeroizing), borrará definitivamente el archivo de la bóveda del almacenamiento y reiniciará la app al estado inicial de bienvenida (onboarding).\n\n" +
                    "3. Para un observador externo o atacante, los datos quedan completamente inaccesibles e irrecuperables en el dispositivo.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp
            )
        }
    }
}
