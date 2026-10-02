package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/**
 * Contenedor destacado tipo aviso o banner (equivalente a un callout en UI).
 */
@Composable
fun ContenedorDestacado(
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorBordeAcento: Color = ColorAcento,
    accion: (@Composable () -> Unit)? = null
) {
    val forma = FormaTarjeta
    val colorLegible = colorLegibleParaTema(colorBordeAcento)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(fondoBadgeParaTema(colorBordeAcento))
            .then(
                if (GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, colorLegible.copy(alpha = 0.35f), forma)
                } else {
                    Modifier
                }
            )
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icono != null) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(FormaPequena)
                            .background(fondoBadgeParaTema(colorBordeAcento)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = colorLegible,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                }
                Text(
                    text = titulo,
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Text(
                text = descripcion,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
            if (accion != null) {
                Spacer(Modifier.height(4.dp))
                accion()
            }
        }
    }
}

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

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaAccionPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ContenedorDestacado(
                titulo = "Aviso de Seguridad",
                descripcion = "Se recomienda activar la biometría para un desbloqueo más rápido y seguro."
            )

            TarjetaAccion(
                titulo = "Copia de Seguridad",
                descripcion = "Crea un respaldo cifrado de todas tus credenciales en almacenamiento local.",
                textoBoton = "Crear Respaldo Ahora",
                colorBoton = ColorAcento,
                alPulsar = {}
            )
        }
    }
}

