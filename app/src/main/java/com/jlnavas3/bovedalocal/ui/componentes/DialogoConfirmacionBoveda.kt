package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Diálogo de confirmación estándar y elegante para la aplicación.
 * Sigue la estética unificada con fondo adaptativo claro/oscuro, esquinas dinámicas según el tema,
 * tonalElevation 0.dp y botones usando `BotonTextoBoveda`.
 */
@Composable
fun DialogoConfirmacionBoveda(
    titulo: String,
    mensaje: String,
    textoConfirmar: String,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit,
    textoCancelar: String = "Cancelar",
    tipoConfirmacion: TipoBotonTexto = TipoBotonTexto.PRIMARIO,
    iconoHeader: ImageVector? = null
) {
    DialogoBoveda(
        onDismissRequest = alDescartar,
        icon = if (iconoHeader != null) {
            {
                val tintColor = when (tipoConfirmacion) {
                    TipoBotonTexto.PELIGRO -> Peligro
                    TipoBotonTexto.SECUNDARIO -> TextoSecundario
                    TipoBotonTexto.PRIMARIO -> ColorAcento
                }
                Icon(
                    imageVector = iconoHeader,
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        } else null,
        title = {
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Text(
                text = mensaje,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            BotonTextoBoveda(
                texto = textoConfirmar,
                alPulsar = alConfirmar,
                tipo = tipoConfirmacion
            )
        },
        dismissButton = {
            BotonTextoBoveda(
                texto = textoCancelar,
                alPulsar = alDescartar,
                tipo = TipoBotonTexto.SECUNDARIO
            )
        }
    )
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun DialogoConfirmacionBovedaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar credencial?",
            mensaje = "Esta acción moverá la credencial seleccionada a la papelera de reciclaje.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            alConfirmar = {},
            alDescartar = {}
        )
    }
}

