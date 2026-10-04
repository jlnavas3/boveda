package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBoveda
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaAvisoPasskey() {
    TarjetaBoveda {
        Text("Ese QR es de una llave de acceso, no de un 2FA", color = Peligro, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Text(
            buildAnnotatedString {
                append("Ese código lo enseña el navegador de tu ordenador para pasar la llave al móvil, y para eso hace falta internet y Bluetooth. Yo no tengo permiso de red, así que no puedo leerlo, y prefiero decírtelo a fingir que funciona.\n\nLas llaves de acceso no se escanean aquí. Abre la web en el navegador del propio móvil y, cuando te pregunte dónde guardar la llave, elige ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Bóveda local") }
                append(". Si no aparezco en esa lista, actívame en Ajustes.")
            },
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
