package com.jlnavas3.bovedalocal.ui.pantallas.senuelo

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
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaExplicativaSenuelo(
    mostrarIdsAjustes: Boolean,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "¿Cómo funciona?",
        idGrupo = "01.2.G1",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Apertura ficticia transparente ante coacción o amenaza física",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = null,
                    tint = ColorSeguridad,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Protección anti-extorsión física",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Si alguien te obliga a desbloquear la app, introduce tu PIN de coacción en la pantalla principal. Bóveda Local se abrirá normalmente pero mostrará solo cuentas señuelo inofensivas. Tu bóveda real permanece 100% cifrada e inaccesible.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp
            )
        }
    }
}
