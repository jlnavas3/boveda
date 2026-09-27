package com.jlnavas3.bovedalocal.ui.pantallas.autodestruccion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaExplicativaAutodestruccion(
    mostrarIdsAjustes: Boolean,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "¿Cómo funciona la autodestrucción?",
        idGrupo = "01.3.G1",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Mecanismo defensivo de borrado seguro",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = null,
                    tint = Peligro,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Mecanismo defensivo de borrado seguro",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Spacer(Modifier.height(10.dp))
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
