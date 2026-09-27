package com.jlnavas3.bovedalocal.ui.pantallas.autodestruccion

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteAlerta
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoAlerta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

@Composable
fun AlertaCriticaAutodestruccion(
    modifier: Modifier = Modifier
) {
    ComponenteAlerta(
        tipo = TipoAlerta.DANGER,
        titulo = "AVISO CRÍTICO: ACCIÓN TOTALMENTE IRREVERSIBLE",
        icono = Icons.Filled.Warning,
        modifier = modifier,
        contenido = {
            Text(
                text = "Si introduces este PIN en la pantalla de desbloqueo, la bóveda local, todas sus contraseñas, notas, passkeys y registros criptográficos serán destruidos de forma instantánea y permanente en este dispositivo.",
                style = MaterialTheme.typography.bodySmall,
                color = TextoPrincipal,
                lineHeight = 17.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "⚠️ ES IMPRESCINDIBLE TENER UN RESPALDO EXTERNO:\nAsegúrate de contar con una Copia de Seguridad Cifrada (.boveda) o un Kit de Emergencia resguardado en otro lugar antes de habilitar esta opción.",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = Peligro,
                lineHeight = 16.sp
            )
        }
    )
}
