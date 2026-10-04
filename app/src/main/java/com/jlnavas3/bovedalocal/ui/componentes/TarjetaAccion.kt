package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Tarjeta de acción normalizada según el patrón de diseño de 'Apariencia'.
 * Muestra título descriptivo, texto de contexto y un botón prominente con color semántico.
 */
@Composable
fun TarjetaAccion(
    titulo: String,
    descripcion: String,
    textoBoton: String,
    colorBoton: Color,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    activo: Boolean = true,
    alPulsar: () -> Unit
) {
    ContenedorTarjeta(
        modifier = modifier,
        paddingInterno = 16.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (icono != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(FormaPequena)
                        .background(colorBoton.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorBoton,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        if (descripcion.isNotBlank()) {
            Text(
                text = descripcion,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
        }
        BotonColorido(
            texto = textoBoton,
            color = colorBoton,
            icono = icono,
            activo = activo,
            alPulsar = alPulsar
        )
    }
}
